using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZPG_World_Movement {
    class MainGWindow : GameWindow{
        //Utility
        private const double degToRad = Math.PI/180;
        //Wordl
        private const String WORLD_FILE = "Data/World.txt";
        private SimpleObjects.Sector sector1;

        //Movement
        private const float MOVEMENT_SPEED = 0.05f;
        private const float ROTATION_SPEED = 1f;
        private float xpos = 0;
        private float zpos = 0;
        private float movz = 0;
        private float roty = 0;
        private float transx = 0;
        private float transy = 0;
        private float transz = 0;
        

        //Texture
        private readonly String[] TEXTURES = new String[] {"Data/Mud.bmp"};
        private const int NUM_OF_TEXTURES = 1;
        private int[] texID = new int[NUM_OF_TEXTURES];
        private int filter = 0;


        public MainGWindow() :base(500, 500, GraphicsMode.Default, "Movement_World") {
            Keyboard.KeyDown += Keyboard_KeyDown;
        }

        private void Keyboard_KeyDown(object sender, OpenTK.Input.KeyboardKeyEventArgs e) {
            //Exit on escape
            if(e.Key == OpenTK.Input.Key.Escape) {
                Exit();
                return;
            }

            //toggle fullscreen
            if(e.Key == OpenTK.Input.Key.F1) {
                Console.Write("Fullscreen toggle: ");
                if(WindowState != WindowState.Fullscreen) {
                    this.WindowState = WindowState.Fullscreen;
                    CursorVisible = false;
                    Console.WriteLine("ON");
                } else {
                    this.WindowState = WindowState.Normal;
                    CursorVisible = true;
                    Console.WriteLine("OFF");
                }
                return;
            }
        }

        
        protected override void OnLoad(EventArgs e) {
            base.OnLoad(e);

            loadTextures();
            loadWorld();

            //Smooth shading
            GL.ShadeModel(ShadingModel.Smooth);
            //Background color
            GL.ClearColor(0f, 0f, 0.25f, 1.0f);
            //Depth buffer
            GL.ClearDepth(1.0f);
            //DepthTest
            GL.Enable(EnableCap.DepthTest);
            GL.DepthFunc(DepthFunction.Less);
            //Perspective correction
            GL.Hint(HintTarget.PerspectiveCorrectionHint, HintMode.Nicest);
            //Textures
            GL.Enable(EnableCap.Texture2D);
            //Blending
            GL.BlendFunc(BlendingFactorSrc.SrcAlpha, BlendingFactorDest.One);

            if(!loadTextures()) {
                Console.WriteLine("Error: Loading textures!");
                Exit();
            }

            if(!loadWorld()) {
                Console.WriteLine("Error: Loading world!");
                Exit();
            }
        }

        private System.Boolean loadWorld() {
            System.IO.StreamReader file = new System.IO.StreamReader(WORLD_FILE);
            String line;
            String[] pLine;
            char[] split = new char[] {' '};
            System.Boolean num = true;

            while(num && (line = file.ReadLine()) != null) {
                pLine = line.Split(' ');
                if(pLine[0].Equals("NUMPOLLIES")) {
                    sector1.list = new List<SimpleObjects.Triangle>(
                        int.Parse(pLine[1])
                        );
                    num = false;
                }
            }

            int count = 0;
            SimpleObjects.Vertex[] v = new SimpleObjects.Vertex[3];

            while((line = file.ReadLine()) != null) {
                pLine = line.Split(split, StringSplitOptions.RemoveEmptyEntries);
                if( pLine.Length != 5 || 
                    pLine[0].ElementAt(0) == '/') continue;

                v[count] = new SimpleObjects.Vertex(
                    float.Parse(pLine[3]),
                    float.Parse(pLine[4]),
                    float.Parse(pLine[0]),
                    float.Parse(pLine[1]),
                    float.Parse(pLine[2])
                    );

                count++;
                if(count > 2) {
                    sector1.list.Add(new SimpleObjects.Triangle(
                        v[0], v[1], v[2]
                        ));
                    count = 0;
                }
            }
            if(count != 0) {
                Console.WriteLine("Wrong number of points!");
                Exit();
            }
            return true;
        }

        private System.Boolean createBitmap(String path, out Bitmap bmp) {
            if(!File.Exists(path)) {
                Console.WriteLine("Texture not found: " + path);
                bmp = null;
                return false;
            }
            bmp = new Bitmap(path);
            return true;
        }

        private void createTexture(Bitmap bmp, int indexID, int texFilterMin, int texFilterMag) {
            GL.GenTextures(indexID, out texID[indexID]);
            GL.BindTexture(TextureTarget.Texture2D, texID[indexID]);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, texFilterMin);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, texFilterMag);
            BitmapData data = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadOnly,
                System.Drawing.Imaging.PixelFormat.Format24bppRgb
                );

            GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba,
                data.Width, data.Height, 0, OpenTK.Graphics.OpenGL.PixelFormat.Bgr,
                PixelType.UnsignedByte, data.Scan0);

            if(texFilterMin == (int)TextureMinFilter.LinearMipmapNearest) {
                GL.GenerateMipmap(GenerateMipmapTarget.Texture2D);
            }

            bmp.UnlockBits(data);
        }

        private System.Boolean loadTextures() {
            Bitmap bmp;
            if(!createBitmap(TEXTURES[0], out bmp)) {
                return false;
            }

            createTexture(bmp, 0, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear);

            return true;
        }

        protected override void OnUpdateFrame(FrameEventArgs e) {
            base.OnUpdateFrame(e);

            //Movement
            if(Keyboard[OpenTK.Input.Key.Up]) {
                xpos += (float)Math.Sin(roty * degToRad) * MOVEMENT_SPEED;
                zpos += (float)Math.Cos(roty * degToRad) * MOVEMENT_SPEED;
                //movz += MOVEMENT_SPEED;
                
            }
            if(Keyboard[OpenTK.Input.Key.Down]) {
                xpos -= (float)Math.Sin(roty * degToRad) * MOVEMENT_SPEED;
                zpos -= (float)Math.Cos(roty * degToRad) * MOVEMENT_SPEED;
                //movz -= MOVEMENT_SPEED;
            }

            if(Keyboard[OpenTK.Input.Key.Left])
                roty += ROTATION_SPEED;
            if(Keyboard[OpenTK.Input.Key.Right])
                roty -= ROTATION_SPEED;
        }

        private void renderWorld() {
            foreach(SimpleObjects.Triangle item in sector1.list) {

                GL.BindTexture(TextureTarget.Texture2D, texID[filter]);
                GL.Begin(PrimitiveType.Triangles);
                {
                    GL.Normal3(0f, 0f, 1f);
                    //First
                    GL.TexCoord2(
                        item.verts[0].u,
                        item.verts[0].v
                        );
                    GL.Vertex3(
                        item.verts[0].x,
                        item.verts[0].y,
                        item.verts[0].z
                        );
                    //Second
                    GL.TexCoord2(
                        item.verts[1].u,
                        item.verts[1].v
                        );
                    GL.Vertex3(
                        item.verts[1].x,
                        item.verts[1].y,
                        item.verts[1].z
                        );
                    //Third
                    GL.TexCoord2(
                        item.verts[2].u,
                        item.verts[2].v
                        );
                    GL.Vertex3(
                        item.verts[2].x,
                        item.verts[2].y,
                        item.verts[2].z
                        );
                }
                GL.End();
            }
        }

        protected override void OnRenderFrame(FrameEventArgs e) {
            base.OnRenderFrame(e);
            GL.Clear(ClearBufferMask.ColorBufferBit | ClearBufferMask.DepthBufferBit);

            //View direction
            Matrix4 modelview = Matrix4.LookAt(Vector3.Zero, Vector3.UnitZ, Vector3.UnitY);
            GL.MatrixMode(MatrixMode.Modelview);
            GL.LoadMatrix(ref modelview);
            GL.LoadIdentity();

            GL.Translate(0f, -0.5f, 0f);
            GL.Rotate(360 - roty, 0f, 1f, 0f);
            GL.Translate(xpos, 0f, zpos);

            renderWorld();

            SwapBuffers();
        }

        protected override void OnResize(EventArgs e) {
            base.OnResize(e);
            GL.Viewport(ClientRectangle.X, ClientRectangle.Y, ClientRectangle.Width, ClientRectangle.Height);
            Matrix4 projection = Matrix4.CreatePerspectiveFieldOfView((float)Math.PI / 4, Width / (float)Height, 1.0f, 64.0f);
            GL.MatrixMode(MatrixMode.Projection);
            GL.LoadMatrix(ref projection);
        }

        protected override void OnUnload(EventArgs e) {
            base.OnUnload(e);
        }
        
    }
}
