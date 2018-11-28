using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;

namespace KPG_9
{
    public partial class Form1 : Form
    {
        private Bitmap canvasBitmap; //canvas bitmap
        private Graphics g;          //graphics helper

        private Bitmap[] cloudTextures; //particle textures
        private List<Particle> particles = new List<Particle>(); //list of all particles
        private Matrix coordSystemOrigin = new Matrix(); //basic system tranlsation (system origin)
        private Random random = new Random();

        //system settings
        private PointF coordOrigin = new PointF(472, 195);
        private int maxNumOfParticles = 500;
        private int lifeLength = 200;
        private float windSpeed = 0.5f;


        public Form1()
        {
            InitializeComponent();

            //load different particle bitmaps
            cloudTextures = new Bitmap[3];

            cloudTextures[0] = (Bitmap)Image.FromFile("Data\\cloud_01.png");
            cloudTextures[1] = (Bitmap)Image.FromFile("Data\\cloud_02.png");
            cloudTextures[2] = (Bitmap)Image.FromFile("Data\\cloud_03.png");

            //create canvas bitmap
            canvasBitmap = new Bitmap(canvas.Width, canvas.Height);
            canvasBitmap.SetResolution(cloudTextures[0].HorizontalResolution, cloudTextures[0].VerticalResolution);
            canvas.Image = canvasBitmap;

            //graphics helper
            g = Graphics.FromImage(canvasBitmap);

            //origin of particle the system
            coordSystemOrigin.Translate(coordOrigin.X, coordOrigin.Y);
        }

        /// <summary>
        /// Just rotation
        /// </summary>
        private PointF rotate(PointF vector, double angle)
        {
            vector = new PointF(vector.X * (float)Math.Cos(angle) - vector.Y * (float)Math.Sin(angle),
                                vector.X * (float)Math.Sin(angle) + vector.Y * (float)Math.Cos(angle));

            return vector;
        }

        /// <summary>
        /// Starts particle system
        /// </summary>
        private void startClick(object sender, EventArgs e)
        {
            particles.Clear();

            Timer t = new Timer();
            t.Tick += new EventHandler(runSimulation);
            t.Interval = 30;
            t.Enabled = true;
            t.Start();
        }

        /// <summary>
        /// Generates one more particle if needed
        /// </summary>
        /// <param name="numOfParticles">Max num of particles</param>
        private void generateParticle(int numOfParticles)
        {
            if (particles.Count < numOfParticles)
            {
                //generate one particle
                Particle p = new Particle();
                p.bitmapIndex = random.Next(3); // 3 different bitmaps
                p.position = new PointF(random.Next(-1, 1), random.Next(-5, 5));
                p.rotation = random.Next(360);
                p.rotationIncrement = (random.NextDouble() - 0.5) * 5; // speed of rotation
                p.size = (float)random.NextDouble() * 0.05f + 0.05f;   //initial size of particle
                p.center = new PointF(cloudTextures[p.bitmapIndex].Width / 2, cloudTextures[p.bitmapIndex].Height / 2);
                p.life = 0;
                p.lifeEnd = random.Next(lifeLength, lifeLength * 3);

                particles.Add(p);
            }

            label4.Text = "Particles: " + particles.Count + " / " + trackBar3.Value;
        }

        /// <summary>
        /// One step of particle system
        /// </summary>
        private void runSimulation(object sender, EventArgs e)
        {
            generateParticle(maxNumOfParticles);
            drawParticles(particles); //draw particles
            canvas.Invalidate();      //update canvas data
        }

        /// <summary>
        /// Draws whole set of particles
        /// </summary>
        /// <param name="particles">List of particles</param>
        private void drawParticles(List<Particle> particles)
        {
            //clear canvas
            g.Clear(Color.Transparent);
            g.Transform.Reset();

            //transformation matrices for one particle
            Matrix scale = new Matrix();
            Matrix translation = new Matrix();
            Matrix rotation = new Matrix();

            int parCnt = particles.Count;
            for (int i = 0; i < parCnt; i++ )
            {
                //reset transformation
                scale.Reset();
                rotation.Reset();
                translation.Reset();

                //particle transparency from 0 to 1(max transparency)
                float transparency = (float)Math.Min((double)(particles[i].lifeEnd - particles[i].life + 0.0001) / (float)particles[i].lifeEnd, 1.0);
                transparency = (float)Math.Pow(transparency, 2);

                //calculate transformation data
                scale.Scale(particles[i].size, particles[i].size);                                         //particle scale
                rotation.RotateAt((float)particles[i].rotation, particles[i].center);                      //particle rotation
                translation.Translate(particles[i].position.X - particles[i].size * particles[i].center.X, //particle translation
                                      particles[i].position.Y - particles[i].size * particles[i].center.Y);

                //rotation, scale, translation
                Matrix res = new Matrix();
                res.Reset();
                res.Multiply(coordSystemOrigin);
                res.Multiply(translation);
                res.Multiply(scale);
                res.Multiply(rotation);
                g.Transform = res;

                drawAlphaMask(cloudTextures[particles[i].bitmapIndex], 0, 0, 0, transparency);

                //particle simulation
                particles[i].rotation += particles[i].rotationIncrement; //rotate particle
                particles[i].life++;                                     //increment particle lifetime

                particles[i].position.Y -= (float)Math.Pow(Math.Max(0, particles[i].lifeEnd - particles[i].life) / (float)particles[i].lifeEnd, 8);
                particles[i].position.X -= windSpeed;

                particles[i].size += 0.001f;
            }

            //remove dead particles
            for (int i = 0; i < particles.Count; i++)
            {
                if (particles[i].life > particles[i].lifeEnd)
                {
                    particles.RemoveAt(i);
                }
            }
        }

        /// <summary>
        /// Draws transparent image with specified colour and transparency
        /// </summary>
        /// <param name="bitmap">Bitmap to draw</param>
        /// <param name="redIntensity">Red</param>
        /// <param name="greenIntensity">Green</param>
        /// <param name="blueIntensity">Blue</param>
        /// <param name="alphaIntensity">Alpha - transparency</param>
        private void drawAlphaMask(Bitmap bitmap, float redIntensity, float greenIntensity, float blueIntensity, float alphaIntensity)
        {
            //transformation matrix for colours and transparency
            ColorMatrix matrix = new ColorMatrix();
            matrix.Matrix00 = redIntensity;
            matrix.Matrix11 = greenIntensity;
            matrix.Matrix22 = blueIntensity;
            matrix.Matrix33 = alphaIntensity;

            //attributes for drawing
            ImageAttributes attr = new ImageAttributes();
            attr.SetColorMatrix(matrix, ColorMatrixFlag.Default, ColorAdjustType.Default);

            g.DrawImage((Image)bitmap,
                            new Rectangle(0, 0, bitmap.Width, bitmap.Height),
                            0, 0, bitmap.Width, bitmap.Height,
                            GraphicsUnit.Pixel, attr);
        }

        /// <summary>
        /// Saves screenshot of the application canvas
        /// </summary>
        private void printscreenButton_Click(object sender, EventArgs e)
        {
            Bitmap result = (Bitmap)canvas.BackgroundImage.Clone();
            Graphics gr = Graphics.FromImage(result);
            gr.DrawImage(canvas.Image, new Point(0, 0));
            gr.Dispose();
            result.Save("out.png", ImageFormat.Png);
        }

        ///
        /// Particle System settings interaction 
        ///

        private void trackBar1_Scroll(object sender, EventArgs e)
        {
            windSpeed = trackBar1.Value / 100.0f;
        }

        private void trackBar2_Scroll(object sender, EventArgs e)
        {
            lifeLength = trackBar2.Value;
        }

        private void trackBar3_Scroll(object sender, EventArgs e)
        {
            maxNumOfParticles = trackBar3.Value;
            label4.Text = "Particles: " + particles.Count + " / " + trackBar3.Value;
        }
    }
}
