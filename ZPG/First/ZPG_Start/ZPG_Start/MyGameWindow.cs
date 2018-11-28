using OpenTK;
using OpenTK.Graphics.OpenGL;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZPG_Start {
    class MyGameWindow : GameWindow {
        //Stars
        private const System.Boolean SETTING_FOR_STARS = true;
        private const int NUMBER_OF_STARS = 50;
        private Star[] stars = new Star[NUMBER_OF_STARS];
        private Random rnd = new Random(new System.DateTime().Millisecond);
        private System.Boolean twinkle = true;
        private float spin = 0;

        //Texture
        private readonly String[] TEXTURES = new String[] { "Data/Crate.bmp",
                                                            "Data/Glass.bmp",
                                                            "Data/Star.bmp"};
        private const int TEXTURE_ID_INDEX_GLASS = 3;
        private const int TEXTURE_ID_INDEX_STAR = 4;
        private const int NUM_OF_TEXTURES = 5;
        private int[] texID = new int[NUM_OF_TEXTURES];

        //Rotation
        private float xrot = 0f;
        private float yrot = 0f;
        private float zrot = 0f;

        private float rotTriangle =         0;
        private float rotRectangle =        0;

        private const float rotTriangleStep =     1.0f;
        private const float rotRectangleStep =    2.0f;
        private float ANGLE_FOR_DIAMOND = (float)(Math.Asin(1/Math.Sqrt(3))*180/Math.PI);

        //Movement (translation)
        private const float MOV_SPEED = 0.02f;
        private float movx = 0f;
        private float movy = 0f;
        private float movz = 0f;

        //Light
        private System.Boolean LIGHTS_ON  = true;
        private readonly float[] LIGHT_AMBIENT  = new float[] {0.5f, 0.5f, 0.5f, 1.0f};
        private readonly float[] LIGHT_DIFFUSE  = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
        private readonly float[] LIGHT_POSITION = new float[] {0.0f, 0.0f, 2.0f, 1.0f};

        //Blending
        private System.Boolean BLENDING_ALLOWED = false; 
        private int filter = 0;

        //View
        private float zoom = -10f;
        private float tilt = 15f;

        public MyGameWindow() : base(500, 500, OpenTK.Graphics.GraphicsMode.Default, "First Window") {
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

            //toggle filter
            if(e.Key == OpenTK.Input.Key.F) {
                filter++;
                if(filter > 2) filter = 0;
                Console.WriteLine("Filter: "+filter);
            }

            //toggle lights
            if(e.Key == OpenTK.Input.Key.L) {
                if(LIGHTS_ON == true) {
                    GL.Disable(EnableCap.Lighting);
                    LIGHTS_ON = false;
                    Console.WriteLine("Lighting: OFF");
                }else {
                    GL.Enable(EnableCap.Lighting);
                    LIGHTS_ON = true;
                    Console.WriteLine("Lighting: ON");
                }
            }

            //toggle blending
            if(e.Key == OpenTK.Input.Key.B) {
                if(BLENDING_ALLOWED) {
                    BLENDING_ALLOWED = false;
                    Console.WriteLine("Blending: OFF");
                }else {
                    BLENDING_ALLOWED = true;
                    Console.WriteLine("Blending: ON");
                }
            }

        }

        private void settingForStars() {
            GL.DepthFunc(DepthFunction.Never);
            GL.Disable(EnableCap.DepthTest);
            GL.Enable(EnableCap.Blend);
            createStars();
        }

        private void createStars() {
            for(int i = 0; i < stars.Length; i++) {
                stars[i] = new Star();
                stars[i].angle = (float)rnd.Next(0, 360);
                stars[i].distance = i / (float)NUMBER_OF_STARS * 5f;
                stars[i].r = rnd.Next(0, 255);
                stars[i].g = rnd.Next(0, 255);
                stars[i].b = rnd.Next(0, 255);
            }
        }

        private void setBlending(System.Boolean blend) {
            if(BLENDING_ALLOWED == false) {
                GL.Disable(EnableCap.Blend);
                GL.Enable(EnableCap.DepthTest);
                return;
            }

            if(blend) {
                GL.Enable(EnableCap.Blend);
                GL.Disable(EnableCap.DepthTest);
            }else {
                GL.Disable(EnableCap.Blend);
                GL.Enable(EnableCap.DepthTest);
            }
        }

        /// <summary>
        /// Load resources.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnLoad(EventArgs e) {
            base.OnLoad(e);
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
            //Light
            GL.Light(LightName.Light0, LightParameter.Ambient, LIGHT_AMBIENT);
            GL.Light(LightName.Light0, LightParameter.Diffuse, LIGHT_DIFFUSE);
            GL.Light(LightName.Light0, LightParameter.Position, LIGHT_POSITION);
            GL.Enable(EnableCap.Light0);
            GL.Enable(EnableCap.Lighting);
            //Textures
            GL.Enable(EnableCap.Texture2D);

            if(!loadTextures()) {
                Console.WriteLine("Loading textures: Error");
                Exit();
            }else {
                Console.WriteLine("Loading textures: OK");
            }
            //Blending
            GL.BlendFunc(BlendingFactorSrc.SrcAlpha, BlendingFactorDest.One);

            if(SETTING_FOR_STARS)
                settingForStars();
        }

        private System.Boolean loadTextures() {
            Bitmap bmp;

            if(!createBitmap(TEXTURES[0], out bmp)) {
                return false;
            }
            createTexture(bmp, 0, (int)TextureMinFilter.Nearest, (int)TextureMagFilter.Nearest);
            createTexture(bmp, 1, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear);
            createTexture(bmp, 2, (int)TextureMinFilter.LinearMipmapNearest, (int)TextureMagFilter.Linear);
            
            if(!createBitmap(TEXTURES[1], out bmp)) {
                return false;
            }
            createTexture(bmp, TEXTURE_ID_INDEX_GLASS, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear);

            if(!createBitmap(TEXTURES[2], out bmp)) {
                return false;
            }
            createTexture(bmp, TEXTURE_ID_INDEX_STAR, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear);

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
            GL.GenTextures(indexID + 1, out texID[indexID]);
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

        /// <summary>
        /// Called when is time to set up next frame.
        /// Game logic here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnUpdateFrame(FrameEventArgs e) {
            base.OnUpdateFrame(e);

            //Automatic rotation
            if(rotTriangle >= 360) {
                rotTriangle = 0;
            }else {
                rotTriangle += rotTriangleStep;
            }

            if(rotRectangle >= 360) {
                rotRectangle = 0;
            }else {
                rotRectangle += rotRectangleStep;
            }

            //Movement
            if(Keyboard[OpenTK.Input.Key.PageUp])
                movy += MOV_SPEED;
            if(Keyboard[OpenTK.Input.Key.PageDown])
                movy -= MOV_SPEED;
            if(Keyboard[OpenTK.Input.Key.Up])
                movz -= MOV_SPEED;
            if(Keyboard[OpenTK.Input.Key.Down])
                movz += MOV_SPEED;
            if(Keyboard[OpenTK.Input.Key.Left])
                movx -= MOV_SPEED;
            if(Keyboard[OpenTK.Input.Key.Right])
                movx += MOV_SPEED;

        }

        /// <summary>
        /// Called when it is time to render next frame.
        /// Rendering code here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnRenderFrame(FrameEventArgs e) {
            base.OnRenderFrame(e);
            GL.Clear(ClearBufferMask.ColorBufferBit | ClearBufferMask.DepthBufferBit);

            //View direction
            Matrix4 modelview = Matrix4.LookAt(Vector3.Zero, Vector3.UnitZ, Vector3.UnitY);
            GL.MatrixMode(MatrixMode.Modelview);
            GL.LoadMatrix(ref modelview);

            GL.LoadIdentity();

            drawStars();

            /*
            GL.Translate(0.0f, 0.0f, -1.5f);
            GL.Translate(movx, movy, movz);     //movement

            GL.Rotate(xrot, 1f, 0f, 0f);
            GL.Rotate(yrot, 0f, 1f, 0f);
            GL.Rotate(zrot, 0f, 0f, 1f);
            */

            //simpleCubeRotating();

            //movingTriangle();
            //movingRectangle(1.5f, 0f, 1);
            //movingRectangle(1f, 0.75f, -2);
            //movingRectangle(0.5f, 1.5f, 1f);

            GL.Flush();
            SwapBuffers();
        }

        private void drawStars() {
            GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_ID_INDEX_STAR]);

            for(int i = 0; i < stars.Length; i++) {
                GL.LoadIdentity();
                GL.Translate(0f, 0f, zoom);
                GL.Rotate(tilt, 1f, 0f, 0f);
                GL.Rotate(stars[i].angle, 0f, 1f, 0f);
                GL.Translate(stars[i].distance, 0f, 0f);
                //reverse roration -> texture is always straight to viewer
                GL.Rotate(-stars[i].angle, 0f, 1f, 0f);
                GL.Rotate(-tilt, 1f, 0f, 0f);

                if(twinkle) {
                    if(spin > 360)
                        //spin = 0;

                    GL.Rotate(spin, 0f, 0f, 1f);
                    spin += 0.02f;
                    GL.Color4((float)stars[i].r/255f, (float)stars[i].g/255f, (float)stars[i].b/255f, 1f);

                    GL.Begin(PrimitiveType.Quads);
                    {
                        GL.TexCoord2(0.0f, 0.0f); GL.Vertex3(-1.0f, -1.0f, 0.0f);
                        GL.TexCoord2(1.0f, 0.0f); GL.Vertex3(1.0f, -1.0f, 0.0f);
                        GL.TexCoord2(1.0f, 1.0f); GL.Vertex3(1.0f, 1.0f, 0.0f);
                        GL.TexCoord2(0.0f, 1.0f); GL.Vertex3(-1.0f, 1.0f, 0.0f);
                    }
                    GL.End();
                    stars[i].angle += i / (float)NUMBER_OF_STARS;
                    stars[i].distance -= 0.01f;

                    if(stars[i].distance <= 0) {
                        stars[i].distance = 5f;
                    }
                }
            }
        }

        private void simpleCubeRotating() {

            GL.Rotate(rotTriangle, 1f, 1f, 1f);

            //Front and back side
            GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_ID_INDEX_GLASS]);
            setBlending(true);
            GL.Color4(1f, 1f, 1f, 0.9f);
            
            
            GL.Begin(PrimitiveType.Quads);
            {
                //Front side
                //GL.Color3(0.8f, 0.2f, 0.2f);
                GL.Normal3(0f, 0f, 1f);
                GL.TexCoord2(0, 0); GL.Vertex3(-0.25f, 0.25f, 0.25f);
                GL.TexCoord2(1, 0); GL.Vertex3(0.25f, 0.25f, 0.25f);
                GL.TexCoord2(1, 1); GL.Vertex3(0.25f, -0.25f, 0.25f);
                GL.TexCoord2(0, 1); GL.Vertex3(-0.25f, -0.25f, 0.25f);

                //Back side
                //GL.Color3(0.8f, 0.2f, 0.2f);
                GL.Normal3(0f, 0f, -1f);
                GL.TexCoord2(1, 0); GL.Vertex3(-0.25f, 0.25f, -0.25f);
                GL.TexCoord2(0, 0); GL.Vertex3(0.25f, 0.25f, -0.25f);
                GL.TexCoord2(0, 1); GL.Vertex3(0.25f, -0.25f, -0.25f);
                GL.TexCoord2(1, 1); GL.Vertex3(-0.25f, -0.25f, -0.25f);
            }
            GL.End();

            setBlending(false);
            
            //Other sides
            GL.BindTexture(TextureTarget.Texture2D, texID[filter]);
            GL.Color4(1f, 1f, 1f, 0.0f);
            GL.Begin(PrimitiveType.Quads);
            {
                //Left side
                //GL.Color3(0.2f, 0.8f, 0.2f);
                GL.Normal3(-1f, 0f, 0f);
                GL.TexCoord2(0, 0); GL.Vertex3(-0.25f, 0.25f, -0.25f);
                GL.TexCoord2(1, 0); GL.Vertex3(-0.25f, 0.25f, 0.25f);
                GL.TexCoord2(1, 1); GL.Vertex3(-0.25f, -0.25f, 0.25f);
                GL.TexCoord2(0, 1); GL.Vertex3(-0.25f, -0.25f, -0.25f);

                //Right side
                //GL.Color3(0.2f, 0.8f, 0.2f);
                GL.Normal3(1f, 0f, 0f);
                GL.TexCoord2(1, 0); GL.Vertex3(0.25f, 0.25f, -0.25f);
                GL.TexCoord2(0, 0); GL.Vertex3(0.25f, 0.25f, 0.25f);
                GL.TexCoord2(0, 1); GL.Vertex3(0.25f, -0.25f, 0.25f);
                GL.TexCoord2(1, 1); GL.Vertex3(0.25f, -0.25f, -0.25f);

                //Top side
                //GL.Color3(0.2f, 0.2f, 0.8f);
                GL.Normal3(0f, 1f, 0f);
                GL.TexCoord2(0, 0); GL.Vertex3(-0.25f, 0.25f, -0.25f);
                GL.TexCoord2(1, 0); GL.Vertex3(0.25f, 0.25f, -0.25f);
                GL.TexCoord2(1, 1); GL.Vertex3(0.25f, 0.25f, 0.25f);
                GL.TexCoord2(0, 1); GL.Vertex3(-0.25f, 0.25f, 0.25f);

                //Bottom side
                //GL.Color3(0.2f, 0.2f, 0.8f);
                GL.Normal3(0f, -1f, 0f);
                GL.TexCoord2(0, 1); GL.Vertex3(-0.25f, -0.25f, -0.25f);
                GL.TexCoord2(1, 1); GL.Vertex3(0.25f, -0.25f, -0.25f);
                GL.TexCoord2(1, 0); GL.Vertex3(0.25f, -0.25f, 0.25f);
                GL.TexCoord2(0, 0); GL.Vertex3(-0.25f, -0.25f, 0.25f);
            }
            GL.End();
        }

        private void movingTriangle() {
            //Triangle
            GL.Translate(0.0f, 0.0f, -5.5f);
            GL.Rotate(rotTriangle, 1f, 0f, 0.5f);     //overall rotation
            GL.Rotate(-rotTriangle, 0.0f, 1.0f, 0.0f);
            GL.Begin(PrimitiveType.Triangles);
            {
                //Bottom side
                GL.Color3(0.2f, 0.2f, 0.8f);
                GL.Vertex3(-0.5f, 0f, -0.5f);
                GL.Vertex3(0.5f, 0f, -0.5f);
                GL.Vertex3(-0.5f, 0f, 0.5f);

                GL.Vertex3(0.5f, 0f, -0.5f);
                GL.Vertex3(0.5f, 0f, 0.5f);
                GL.Vertex3(-0.5f, 0f, 0.5f);

                //Front side
                GL.Color3(0.8f, 0.2f, 0.2f);
                GL.Vertex3(-0.5f, 0f, 0.5f);
                GL.Vertex3(0.5f, 0f, 0.5f);
                GL.Vertex3(0f, 1f, 0f);

                //Back side
                GL.Color3(0.8f, 0.2f, 0.2f);
                GL.Vertex3(-0.5f, 0f, -0.5f);
                GL.Vertex3(0f, 1f, 0f);
                GL.Vertex3(0.5f, 0f, -0.5f);

                //Left side
                GL.Color3(0.2f, 0.8f, 0.2f);
                GL.Vertex3(-0.5f, 0f, -0.5f);
                GL.Vertex3(-0.5f, 0f, 0.5f);
                GL.Vertex3(0f, 1f, 0f);

                //Right side
                GL.Color3(0.2f, 0.8f, 0.2f);
                GL.Vertex3(0.5f, 0f, -0.5f);
                GL.Vertex3(0f, 1f, 0f);
                GL.Vertex3(0.5f, 0f, 0.5f);


            }
            GL.End();

            GL.LoadIdentity();
        }

        private void movingRectangle(float height, float radius, float speed) {
            //Rectangle
            GL.Translate(0.0f, 0.0f, -5.5f);
            GL.Rotate(rotTriangle, 1f, 0f, 0.5f); //overall rotation
            GL.Translate(0.0f, height, 0.0f);

            GL.Rotate(rotRectangle * speed, 0f, 1f, 0f);
            GL.Translate(radius, 0.0f, 0f);
            GL.Rotate(rotRectangle * speed, 0f, 1f, 0f);
            GL.Rotate(ANGLE_FOR_DIAMOND, 1f, 0f, 0f);
            GL.Rotate(45, 0f, 0f, 1f);

            GL.Begin(PrimitiveType.Quads);
            {
                //Front side
                GL.Color3(0.8f, 0.2f, 0.2f);
                GL.Vertex3(-0.25f, 0.25f, 0.25f);
                GL.Vertex3(0.25f, 0.25f, 0.25f);
                GL.Vertex3(0.25f, -0.25f, 0.25f);
                GL.Vertex3(-0.25f, -0.25f, 0.25f);

                //Back side
                GL.Color3(0.8f, 0.2f, 0.2f);
                GL.Vertex3(-0.25f, 0.25f, -0.25f);
                GL.Vertex3(0.25f, 0.25f, -0.25f);
                GL.Vertex3(0.25f, -0.25f, -0.25f);
                GL.Vertex3(-0.25f, -0.25f, -0.25f);

                //Left side
                GL.Color3(0.2f, 0.8f, 0.2f);
                GL.Vertex3(-0.25f, 0.25f, -0.25f);
                GL.Vertex3(-0.25f, 0.25f, 0.25f);
                GL.Vertex3(-0.25f, -0.25f, 0.25f);
                GL.Vertex3(-0.25f, -0.25f, -0.25f);

                //Right side
                GL.Color3(0.2f, 0.8f, 0.2f);
                GL.Vertex3(0.25f, 0.25f, -0.25f);
                GL.Vertex3(0.25f, 0.25f, 0.25f);
                GL.Vertex3(0.25f, -0.25f, 0.25f);
                GL.Vertex3(0.25f, -0.25f, -0.25f);

                //Top side
                GL.Color3(0.2f, 0.2f, 0.8f);
                GL.Vertex3(-0.25f, 0.25f, -0.25f);
                GL.Vertex3(0.25f, 0.25f, -0.25f);
                GL.Vertex3(0.25f, 0.25f, 0.25f);
                GL.Vertex3(-0.25f, 0.25f, 0.25f);

                //Bottom side
                GL.Color3(0.2f, 0.2f, 0.8f);
                GL.Vertex3(-0.25f, -0.25f, -0.25f);
                GL.Vertex3(0.25f, -0.25f, -0.25f);
                GL.Vertex3(0.25f, -0.25f, 0.25f);
                GL.Vertex3(-0.25f, -0.25f, 0.25f);

            }
            GL.End();
            GL.LoadIdentity();
        }

        /// <summary>
        /// Called when window is resized.
        /// Set viewport, pojection matrix here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnResize(EventArgs e) {
            base.OnResize(e);
            GL.Viewport(ClientRectangle.X, ClientRectangle.Y, ClientRectangle.Width, ClientRectangle.Height);
            Matrix4 projection = Matrix4.CreatePerspectiveFieldOfView((float)Math.PI / 4, Width / (float)Height, 1.0f, 64.0f);
            GL.MatrixMode(MatrixMode.Projection);
            GL.LoadMatrix(ref projection);
        }

        protected override void OnUnload(EventArgs e) {
            base.OnUnload(e);
            for(int i = 0; i < texID.Length; i++) {
                GL.DeleteTextures(i + 1, ref texID[i]);
            }
        }
    }
}
