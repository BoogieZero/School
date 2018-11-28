using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;

namespace KPG01_ciste
{
    public partial class Form1 : Form
    {

        public Graphics graphics;
        public Pen pero;
        public Bitmap bmp;
        public Brush vypln;
        public Point pocatecniBod;
        public bool kliknutoL = false;
        public bool kliknutoP = false;
        public SolidBrush stetec;
        public float tloustka = 5;
        Color barvaKresleni;
        Point tecka;
        
        public int prepinacAnimace;
        int x, y, t;
        public Timer mujTimer;

        /// <summary>
        /// ///23.3.2016
        /// </summary>
        private PointF relativePosition;
        private ColorF[,] myPixels;
        Color vybranaBarva;


        /// <summary>
        /// 30.3.2016
        /// </summary>
        int whiteSpaceLeft = 0;
        int whiteSpaceTop = 0;



        public Form1()
        {
            InitializeComponent();
            bmp = new Bitmap(panel1.Width, panel1.Height);
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(Color.White);
            pero = new Pen(Color.Black);
            barvaKresleni = Color.Red;
            stetec = new SolidBrush(barvaKresleni);
            tecka = new Point(0, 0);
            inicializaceCasovace();
        }

        private void Guma_BTN(object sender, EventArgs e)
        {
            gumujPanelDoBela();
            vykresliDoPanelu();
        }

        private void gumujPanelDoBela()
        {
            graphics.Clear(Color.White);
        }

        private void Cara_BTN(object sender, EventArgs e)
        {
            kresliCaru(Color.Blue, 10, new Point(200, 200), new Point(100, 300));
        }

        private void kresliCaru(Color c, float tloustka, Point a, Point b)
        {
            pero = new Pen(c, tloustka);
            graphics.DrawLine(pero, a, b);
            vykresliDoPanelu();
        }

        private void vykresliDoPanelu()
        {
            panel1_Paint(this, null);
            //panel1.BackgroundImage = bmp;            
            //panel1.Invalidate();
            //panel1.Refresh();
            //panel1.Invalidate();
        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {
            //panel1.CreateGraphics().Clear(Color.White);
            if (bmp == null) return;
            Graphics grp = panel1.CreateGraphics();


           
            double aspectRatioSrc = (double)bmp.Width / bmp.Height;
            double aspectRatioDst = (double)panel1.Width / panel1.Height;
            if (aspectRatioSrc > aspectRatioDst)
            {
                whiteSpaceTop = panel1.Height - (int)(panel1.Width / aspectRatioSrc);
                whiteSpaceTop >>= 1;
                whiteSpaceLeft = 0;
            }
            else
            {
                whiteSpaceLeft = panel1.Width - (int)(panel1.Height * aspectRatioSrc);
                whiteSpaceLeft >>= 1;
                whiteSpaceTop = 0;
            }
            Rectangle rSource = new Rectangle(0, 0, bmp.Width, bmp.Height);
            Rectangle rDestination = new Rectangle(whiteSpaceLeft, whiteSpaceTop, panel1.Width - (whiteSpaceLeft << 1), panel1.Height - (whiteSpaceTop << 1));
            grp.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
            grp.DrawImage(bmp, rDestination, rSource, GraphicsUnit.Pixel);

           
           // grp.DrawImage(bmp, 0, 0, panel1.Width, panel1.Height);
            grp.Dispose();
        }

        private void kresliObdelnik(Color c, float x, float y, float width, float height)
        {
            vypln = new SolidBrush(c);
            graphics.FillRectangle(vypln, x, y, width, height);
        }

        private void Obdelnik_BTN(object sender, EventArgs e)
        {
            kresliObdelnik(Color.Red, 50, 50, 150, 150);
            vykresliDoPanelu();
        }


        private void panel1_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left && checkBox1.Checked)
            {
                pocatecniBod = e.Location;
                kliknutoL = true;

                graphics.FillEllipse(stetec, e.X - tloustka * 0.5f, e.Y - tloustka * 0.5f, tloustka, tloustka);
                vykresliDoPanelu();
            }
            else if (e.Button == MouseButtons.Right && checkBox1.Checked)
            {
                pocatecniBod = e.Location;
                kliknutoP = true;

                graphics.FillEllipse(Brushes.White, e.X - tloustka * 0.5f, e.Y - tloustka * 0.5f, tloustka, tloustka);
                vykresliDoPanelu();
            }
            else if (e.Button == MouseButtons.Left && checkBox2.Checked)
            {

                kliknutoL = true;
                SolidBrush sb = new SolidBrush(Color.White);
                float offset = 1f;
                float tloustkaF = tloustka * offset;
                graphics.FillEllipse(sb, tecka.X - tloustkaF * 0.5f, tecka.Y - tloustkaF * 0.5f, tloustkaF, tloustkaF);
                vykresliDoPanelu();
            }
            else if (e.Button == MouseButtons.Left && checkBox3.Checked) {
                vybranaBarva = bmp.GetPixel((int)relativePosition.X, (int)relativePosition.Y);
                label3.Text = relativePosition.ToString();
                label4.Text = vybranaBarva.ToString();
                panel3.BackColor = vybranaBarva;
                panel3.Invalidate();
            }


        }

        private void panel1_MouseMove(object sender, MouseEventArgs e)
        {
            relativePosition.X = (float)(e.X - whiteSpaceLeft) / (panel1.Width - 2 * whiteSpaceLeft);
            relativePosition.Y = (float)(e.Y - whiteSpaceTop) / (panel1.Height - 2 * whiteSpaceTop);

            relativePosition.X = relativePosition.X * bmp.Width;
            relativePosition.Y = relativePosition.Y * bmp.Height;

            if (checkBox1.Checked && kliknutoL)
            {             //kresleni stetcem



                graphics.DrawLine(pero, pocatecniBod, e.Location);
                graphics.FillEllipse(stetec, e.X - tloustka * 0.5f, e.Y - tloustka * 0.5f, tloustka, tloustka);
                pocatecniBod = e.Location;
                //graphics.FillEllipse(stetec, e.X, e.Y, 14, 14);
                vykresliDoPanelu();
            }
            else if (checkBox1.Checked && kliknutoP)        //guma
            {

                pero = new Pen(Color.White, tloustka);

                graphics.DrawLine(pero, pocatecniBod, e.Location);
                graphics.FillEllipse(Brushes.White, e.X - tloustka * 0.5f, e.Y - tloustka * 0.5f, tloustka, tloustka);
                pocatecniBod = e.Location;
                vykresliDoPanelu();
            }

        }

        private void panel1_MouseUp(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left && checkBox1.Checked)
            {
                kliknutoL = false;
            }
            else if (e.Button == MouseButtons.Right)
            {
                kliknutoP = false;
            }
            else if (e.Button == MouseButtons.Left && checkBox2.Checked)
            {
                tecka = e.Location;
                kliknutoL = false;
                graphics.FillEllipse(stetec, tecka.X - tloustka * 0.5f, tecka.Y - tloustka * 0.5f, tloustka, tloustka);
                vykresliDoPanelu();
            }
        }

        private void definujBarvy()
        {

            panel2.BackColor = barvaKresleni;
            stetec = new SolidBrush(barvaKresleni);
            pero = new Pen(barvaKresleni, tloustka);
        }

        private void button4_Click(object sender, EventArgs e)
        {
            ColorDialog colorDlg = new ColorDialog();
            colorDlg.AllowFullOpen = true;
            colorDlg.AnyColor = true;
            colorDlg.SolidColorOnly = false;
            colorDlg.Color = Color.Red;

            if (colorDlg.ShowDialog() == DialogResult.OK)
            {
                barvaKresleni = colorDlg.Color;
            }
            definujBarvy();
        }

        private void numericUpDown2_ValueChanged(object sender, EventArgs e)
        {
            tloustka = (float)numericUpDown2.Value;
            definujBarvy();
        }

        ////////////////////CV4//////////////////////////////////////////

        public void inicializaceCasovace()
        {
            mujTimer = new Timer();
            mujTimer.Enabled = false;
            mujTimer.Tick += new EventHandler(animace);
        }

        private void button8_Click(object sender, EventArgs e)
        {
            prepinacAnimace = 1;
            mujTimer.Start();
        }

        private void button9_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();
        }

       

        private void button10_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();
            x = 0;
            y = 0;
        }

        private void animace(object sender, EventArgs e)
        {
            if (prepinacAnimace == 1)
            {
                x += 10;
                y += 10;

                kresliCaru(Color.Coral, 5, new Point(x, y), new Point(x + 2, y + 2));
            }
            else if (prepinacAnimace == 2)
            {
                float rx = 200;
                float ry = 200;
                float posunX = bmp.Width / 2;
                float posunY = bmp.Height / 2;
                if (t > 360) return;

                double angle = t * System.Math.PI / 180;
                int xX = (int)(posunX + rx * System.Math.Cos(angle));
                int yY = (int)(posunY + ry * System.Math.Sin(angle));
                //bmp.SetPixel(xX, yY, Color.Red);
                graphics.FillRectangle(vypln, xX, yY, 10, 10);
                t++;

            }


            vykresliDoPanelu();
            //drawsomething();
            return;
        }


        /////////////////////////////////////////////////////23.3.2016////////////////////////////////////////////////////////////////////////////////////////

        private void button5_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.FileName = string.Empty;
            ofd.Filter = "Podporovane obrazky (bmp, gif, jpeg, png, tiff)|*.bmp;*.gif;*.jpg;*.jpeg;*.png;*.tif;*.tiff";
            ofd.Multiselect = false;
            ofd.CheckFileExists = true;
            DialogResult dr = ofd.ShowDialog(this);
            if (dr != System.Windows.Forms.DialogResult.OK)
            {
                return;
            }
            Image importedImage = null;
            string errMsg = "Neznama chyba pri nahravani obrazku.";
            try
            {
                importedImage = Image.FromFile(ofd.FileName);
            }
            catch (Exception exc)
            {
                importedImage = null;
                errMsg = exc.Message;
            }
            if (importedImage == null)
            {
                MessageBox.Show(errMsg, "Chyba cteni souboru", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            graphics.Dispose();
            bmp.Dispose();
            bmp = (Bitmap)importedImage;
            graphics = Graphics.FromImage(bmp);
            myPixels = bitmapToColorf(bmp);
            panel1.CreateGraphics().Clear(Color.White);
            vykresliDoPanelu();
            
        }

        private void button6_Click(object sender, EventArgs e)
        {
            Color c;
            Color tmp;

            for (int i = 0; i < bmp.Width; i++)
            {
                for (int j = 0; j < bmp.Height; j++)
                {
                    c = bmp.GetPixel(i, j);
                    //R G B 
                    tmp = Color.FromArgb(c.R, c.B, c.G);
                    bmp.SetPixel(i, j, tmp);
                }
            }


            vykresliDoPanelu();
        }

        private void button7_Click(object sender, EventArgs e)
        {
            ColorF pixel;

            for (int i = 0; i < myPixels.GetLength(0); i++)
            {
                for (int j = 0; j < myPixels.GetLength(1); j++)
                {
                    pixel = myPixels[i, j];
                    myPixels[i, j].b = pixel.g;
                    myPixels[i, j].g = pixel.b;
                }
            }

            bmp = colorfToBitmap(myPixels);
            vykresliDoPanelu();
        }

        


        /// <summary>
        /// Prevede obrazek na pole float4 barev.
        /// </summary>
        /// <param name="souce"></param>
        /// <returns></returns>
        private ColorF[,] bitmapToColorf(Bitmap souce)
        {
            if (souce == null) return null;
            ColorF[,] rv = new ColorF[souce.Height, souce.Width];

            BitmapData bd = null;
            try
            {
                bd = bmp.LockBits(new Rectangle(0, 0, bmp.Width, bmp.Height), ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);
            }
            catch (Exception exc)
            {
                Console.WriteLine(exc.Message);
                return null;
            }
            ColorF c = new ColorF();
            unsafe
            {
                byte* ptr = null;
                byte* lnptr = (byte*)bd.Scan0;

                for (int i = 0; i < bd.Height; ++i)
                {
                    ptr = lnptr;
                    for (int j = 0; j < bd.Width; ++j)
                    {
                        //*ptr, *ptr+1, *ptr+2, *ptr+3 = B, G, R, A
                        c.b = (float)(*ptr) / 255.0f;
                        c.g = (float)(*(ptr + 1)) / 255.0f;
                        c.r = (float)(*(ptr + 2)) / 255.0f;
                        c.a = (float)(*(ptr + 3)) / 255.0f;

                        rv[i, j] = c;
                        ptr += 4; //ARGB = 4 bytes
                    }
                    lnptr += bd.Stride;
                }

            }

            bmp.UnlockBits(bd);

            return rv;
        }

        /// <summary>
        /// Prevede pole float4 barev do obrazku.
        /// </summary>
        /// <param name="source"></param>
        /// <returns></returns>
        private Bitmap colorfToBitmap(ColorF[,] source)
        {
            Bitmap rv = new Bitmap(source.GetLength(1), source.GetLength(0), PixelFormat.Format32bppArgb);

            BitmapData bd = null;
            try
            {
                bd = rv.LockBits(new Rectangle(0, 0, bmp.Width, bmp.Height), ImageLockMode.WriteOnly, PixelFormat.Format32bppArgb);
            }
            catch (Exception exc)
            {
                Console.WriteLine(exc.Message);
                return null;
            }
            unsafe
            {
                byte* ptr = null;
                byte* lnptr = (byte*)bd.Scan0;
                ColorF c;
                for (int i = 0; i < bd.Height; ++i)
                {
                    ptr = lnptr;
                    for (int j = 0; j < bd.Width; ++j)
                    {
                        c = source[i, j];
                        //*ptr, *ptr+1, *ptr+2, *ptr+3 = B, G, R, A
                        *ptr = (byte)(c.b * 255.0f);
                        *(ptr + 1) = (byte)(c.g * 255.0f);
                        *(ptr + 2) = (byte)(c.r * 255.0f);
                        *(ptr + 3) = (byte)(c.a * 255.0f);

                        ptr += 4; //ARGB = 4 bytes
                    }
                    lnptr += bd.Stride;
                }

            }

            rv.UnlockBits(bd);

            return rv;
        }

       
        //////////////////////////////////////////////////////////////////////////////////////////30.3.2016///////////////////////////////////////////

        private void panel1_Resize(object sender, EventArgs e)
        {
            panel1.CreateGraphics().Clear(Color.White);
            panel1_Paint(null, null);
        }

       
        /// <summary>
        /// odstin sedi
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button11_Click(object sender, EventArgs e)
        {
            ColorF[,] tmp = bitmapToColorf(bmp);
            int w = tmp.GetLength(1);
            int h = tmp.GetLength(0);

            float intenzita;
            for (int i = 0; i < h; ++i)
            {
                for (int j = 0; j < w; ++j)
                {
                    intenzita = 0.299f * tmp[i, j].r + 0.587f * tmp[i, j].g + 0.114f * tmp[i, j].b;
                    tmp[i, j].r = intenzita;
                    tmp[i, j].g = intenzita;
                    tmp[i, j].b = intenzita;
                }
            }

            bmp = colorfToBitmap(tmp);
            vykresliDoPanelu();
        }

       
        /// <summary>
        /// Emboss
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button12_Click(object sender, EventArgs e)
        {
            ColorF[,] tmp = bitmapToColorf(bmp);
            ColorF[,] tmp2 = bitmapToColorf(bmp);
            int w = tmp.GetLength(1);
            int h = tmp.GetLength(0);

            ColorF jedna = new ColorF(1f, 1f, 1f);
            int posun = 3;

            for (int i = 0; i < h; ++i)
            {
                for (int j = 0; j < w; ++j)
                {
                    if (j < w - posun && i < h - posun)
                    {
                        //0.5*(I+(1-t(I)))
                        tmp2[i, j] = (tmp[i, j] + (jedna - tmp[i + posun, j + posun])) * 0.5f;
                    }
                    else
                    {
                        tmp2[i, j] = tmp[i, j];
                    }

                }
            }

            tmp = tmp2;
            bmp = colorfToBitmap(tmp);

            vykresliDoPanelu();
        }

        /// <summary>
        /// invertovani barev
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button13_Click(object sender, EventArgs e)
        {
            ColorF pixel;

            for (int i = 0; i < myPixels.GetLength(0); i++)
            {
                for (int j = 0; j < myPixels.GetLength(1); j++)
                {
                    pixel = myPixels[i, j];
                    myPixels[i, j].r = 1 - pixel.r;
                    myPixels[i, j].g = 1 - pixel.g;
                    myPixels[i, j].b = 1 - pixel.b;
                }
            }

            bmp = colorfToBitmap(myPixels);
            vykresliDoPanelu();
        }

        private void button16_Click(object sender, EventArgs e) {
            ColorF  pixelTL, pixelTM, pixelTR,
                    pixelML, pixelMM, pixelMR,
                    pixelBL, pixelBM, pixelBR;

            for(int i = 1; i < myPixels.GetLength(0)-1; i++) {
                for(int j = 1; j < myPixels.GetLength(1)-1; j++) {

                    pixelTL = myPixels[i - 1, j - 1];
                    pixelTM = myPixels[i    , j - 1];
                    pixelTR = myPixels[i + 1, j - 1];

                    pixelML = myPixels[i - 1, j];
                    pixelMM = myPixels[i    , j];
                    pixelML = myPixels[i + 1, j];

                    pixelTL = myPixels[i - 1, j + 1];
                    pixelTM = myPixels[i    , j + 1];
                    pixelTR = myPixels[i + 1, j + 1];

                    //set

                    
                    /*
                    myPixels[i, j].r = 1 - pixel.r;
                    myPixels[i, j].g = 1 - pixel.g;
                    myPixels[i, j].b = 1 - pixel.b;
                    */
                }
            }

            bmp = colorfToBitmap(myPixels);
            vykresliDoPanelu();
        }

        private void button15_Click(object sender, EventArgs e)
        {
            //ColorF pixel;

            Parallel.For(0, myPixels.GetLength(0), i =>
            {
                for (int j = 0; j < myPixels.GetLength(1); j++)
                {
                    ColorF pixel = myPixels[i, j];
                    myPixels[i, j].r = 1 - pixel.r;
                    myPixels[i, j].g = 1 - pixel.g;
                    myPixels[i, j].b = 1 - pixel.b;
                }
            });

            bmp = colorfToBitmap(myPixels);
            vykresliDoPanelu();
        }

       

        /// <summary>
        /// Sépie
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button14_Click(object sender, EventArgs e)
        {
            ColorF pixel;

            for (int i = 0; i < myPixels.GetLength(0); i++)
            {
                for (int j = 0; j < myPixels.GetLength(1); j++)
                {
                    pixel = myPixels[i, j];
                    float sepia = (float)(0.299 * pixel.r + 0.587 * pixel.g + 0.114 * pixel.b);
                    myPixels[i, j].r = (float)((sepia > 0.8078) ? 1 : sepia + 0.1921);
                    myPixels[i, j].g = (float)((sepia < 0.0547) ? 0 : sepia - 0.0549);
                    myPixels[i, j].b = (float)((sepia < 0.2196) ? 0 : sepia - 0.2196);
                }
            }

            bmp = colorfToBitmap(myPixels);
            vykresliDoPanelu();
        }





    }
}
