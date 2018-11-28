using OpenTK;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenTK.Graphics.OpenGL;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;

namespace Lesson_12 {
    class OpenTKWindow : GameWindow {
        //Conversion
        private const double DEG_TO_RAD = Math.PI / 180;

        //Textures
        private const int NUM_FILTERS = 3;      //number of diferent filters used for each texture
        private const String PATH_TEXTURES = "Data/Textures/";
        private readonly String[] TEXTURES      //references to all textures
            = new String[] {PATH_TEXTURES+"Crate.bmp",
                            PATH_TEXTURES+"Tim.bmp"};

        private int[] texID;                    //array of textures in OpenGL
        private int filter = 0;                 //number of actual filter

        //Wordl
        private const String PATH_WORLDS = "Data/World/";
        private const String WORLD_FILE = PATH_WORLDS+"Flag.txt";
        private SimpleStructures.Sector sector1;

        //Ripple
        private int wiggleCount = 0;    //speed
        private int hold;               //smooth ripple
        private float[,,] wavePoints;

        //Movement
        private const float PERSON_HEIGHT = 0.25f;      //camera height
        private const float MOVEMENT_SPEED = 0.05f;
        private const float ROTATION_SPEED = 2.0f;
            //Transformation values for actual position
        private float xpos = 0;
        private float zpos = 0;
        private float yrot = 0;

        //Lighting
        private readonly float[] LIGHT_AMBIENT = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
        private readonly float[] LIGHT_DIFFUSE = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        private readonly float[] LIGHT_POSITION = new float[] { 0.0f, 0.0f, 2.0f, 1.0f };
        private System.Boolean LIGHTS_ON = false;       //default setting for lights

        //Blending
        private System.Boolean BLENDING_ALLOW = false;  //default setting for blending

        /// <summary>
        /// Constructor for new instance of window.
        /// </summary>
        public OpenTKWindow(int width, int height) 
            : base(width, height, OpenTK.Graphics.GraphicsMode.Default) {
            Keyboard.KeyDown += Keyboard_KeyDown;
            initWave();
        }

        private void initWave() {
            wavePoints = new float[45, 45, 3];
            for(int i = 0; i < 45; i++) {
                for(int j = 0; j < 45; j++) {
                    wavePoints[i, j, 0] = i / 5f - 4.5f;
                    wavePoints[i, j, 1] = j / 5f - 4.5f;
                    wavePoints[i, j, 2] = (float)(Math.Sin(
                        ((i / 5.0f) * 40.0f) * DEG_TO_RAD
                        ));
                }
            }
        }

        /// <summary>
        /// Key pressed handler.
        ///     Exit
        ///     Fullscreen
        ///     Filter
        ///     Lights
        ///     Blending
        /// </summary>
        /// <param name="sender">not used</param>
        /// <param name="e">keyboard key event</param>
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
                Console.WriteLine("Filter: " + filter);
            }

            //toggle lights
            if(e.Key == OpenTK.Input.Key.L) {
                if(LIGHTS_ON == true) {
                    GL.Disable(EnableCap.Lighting);
                    LIGHTS_ON = false;
                    Console.WriteLine("Lighting: OFF");
                } else {
                    GL.Enable(EnableCap.Lighting);
                    LIGHTS_ON = true;
                    Console.WriteLine("Lighting: ON");
                }
            }

            //toggle blending
            if(e.Key == OpenTK.Input.Key.B) {
                if(BLENDING_ALLOW) {
                    BLENDING_ALLOW = false;
                    Console.WriteLine("Blending: OFF");
                } else {
                    BLENDING_ALLOW = true;
                    Console.WriteLine("Blending: ON");
                }
            }
        }

        /// <summary>
        /// Loads resources and one time settings for OpenGL.
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
            //Blending
            GL.BlendFunc(BlendingFactorSrc.SrcAlpha, BlendingFactorDest.OneMinusSrcAlpha);
            //Textures
            GL.Enable(EnableCap.Texture2D);
            //GL.TexEnv(TextureEnvTarget.TextureEnv, TextureEnvParameter.TextureEnvMode, (int)TextureEnvMode.Modulate);
            
            if(!TextureLoader.load(TEXTURES, out texID)) {
                Exit();
            }

            if(!loadWorld()) {
                Console.WriteLine("Load world: Error");
                Exit();
            }else {
                Console.WriteLine("Load world: OK");
            }

            //Front side filled with polygons
            GL.PolygonMode(MaterialFace.Front, PolygonMode.Line);
            //Back side filled by grid
            GL.PolygonMode(MaterialFace.Back, PolygonMode.Fill);
        }

        /// <summary>
        /// Called when is time to set up next frame.
        /// Game logic here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnUpdateFrame(FrameEventArgs e) {
            base.OnUpdateFrame(e);

            //Movement
            if(Keyboard[OpenTK.Input.Key.Up]) {
                xpos += (float)Math.Sin(yrot * DEG_TO_RAD) * MOVEMENT_SPEED;
                zpos += (float)Math.Cos(yrot * DEG_TO_RAD) * MOVEMENT_SPEED;

            }
            if(Keyboard[OpenTK.Input.Key.Down]) {
                xpos -= (float)Math.Sin(yrot * DEG_TO_RAD) * MOVEMENT_SPEED;
                zpos -= (float)Math.Cos(yrot * DEG_TO_RAD) * MOVEMENT_SPEED;
            }

            if(Keyboard[OpenTK.Input.Key.Left])
                yrot += ROTATION_SPEED;
            if(Keyboard[OpenTK.Input.Key.Right])
                yrot -= ROTATION_SPEED;
        }

        /// <summary>
        /// Called when it is time to render next frame.
        /// Rendering code here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnRenderFrame(FrameEventArgs e) {
            base.OnRenderFrame(e);
            //Clear buffer
            GL.Clear(ClearBufferMask.ColorBufferBit | ClearBufferMask.DepthBufferBit);

            //View direction
            Matrix4 modelview = Matrix4.LookAt(Vector3.Zero, Vector3.UnitZ, Vector3.UnitY);
            GL.MatrixMode(MatrixMode.Modelview);
            GL.LoadMatrix(ref modelview);

            GL.LoadIdentity();

            //view height
            GL.Translate(0f, -PERSON_HEIGHT, 0f);
            //movement
            GL.Rotate(360 - yrot, 0f, 1f, 0f);
            GL.Translate(xpos, 0f, zpos);
            //starting position
            GL.Translate(0f, 0f, -4f);

            drawFlag();
            //drawHouse();

            SwapBuffers();
        }

        /// <summary>
        /// Called when window is resized.
        /// Set viewport, pojection matrix here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnResize(EventArgs e) {
            base.OnResize(e);
            GL.Viewport(ClientRectangle.X, ClientRectangle.Y, ClientRectangle.Width, ClientRectangle.Height);
            Matrix4 projection = Matrix4.CreatePerspectiveFieldOfView((float)Math.PI / 4, Width / (float)Height, 0.5f, 64.0f);
            GL.MatrixMode(MatrixMode.Projection);
            GL.LoadMatrix(ref projection);
        }

        /// <summary>
        /// Unloading textures.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnUnload(EventArgs e) {
            base.OnUnload(e);
            for(int i = 0; i < texID.Length; i++) {
                GL.DeleteTextures(i, ref texID[i]);
            }
        }

        /// <summary>
        /// Sets blending if blending is allowed.
        /// </summary>
        /// <param name="blend">true - turns blending ON</param>
        private void setBlending(System.Boolean blend) {
            if(BLENDING_ALLOW == false) {
                GL.Disable(EnableCap.Blend);
                GL.Enable(EnableCap.DepthTest);
                return;
            }

            if(blend) {
                GL.Enable(EnableCap.Blend);
                GL.Disable(EnableCap.DepthTest);
            } else {
                GL.Disable(EnableCap.Blend);
                GL.Enable(EnableCap.DepthTest);
            }
        }

        /// <summary>
        /// Loads world from file and creates sector1.
        /// </summary>
        /// <returns>true for succesfull load</returns>
        private System.Boolean loadWorld() {
            System.IO.StreamReader file = new System.IO.StreamReader(WORLD_FILE);
            String line;
            String[] pLine;
            char[] split = new char[] { ' ', '\t' };

            sector1.list = new List<SimpleStructures.Triangle>();
            int count = 0;
            SimpleStructures.Vertex[] v = new SimpleStructures.Vertex[3];

            while((line = file.ReadLine()) != null) {
                pLine = line.Split(split, StringSplitOptions.RemoveEmptyEntries);
                if(pLine.Length != 5 ||
                    pLine[0].ElementAt(0) == '/') continue;

                v[count] = new SimpleStructures.Vertex(
                    float.Parse(pLine[0]),
                    float.Parse(pLine[1]),
                    float.Parse(pLine[2]),
                    float.Parse(pLine[3]),
                    float.Parse(pLine[4])
                    );

                count++;
                if(count > 2) {
                    sector1.list.Add(new SimpleStructures.Triangle(
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

        private void drawFlag() {
            float x = 0;
            float y = 0;
            float xt = 0;
            float yt = 0;

            GL.BindTexture(TextureTarget.Texture2D, texID[3+filter]);

            GL.Begin(PrimitiveType.Triangles);
            {
                for(int j = 0; j < 44; j++) {
                    for(int i = 0; i < 44; i++) {
                        x = i / 44f;
                        y = j / 44f;

                        xt = (i + 1) / 44f;
                        yt = (j + 1) / 44f;

                        //First

                        GL.TexCoord2(x, 1f-y);
                        GL.Vertex3(wavePoints[i, j, 0], wavePoints[i, j, 1], wavePoints[i, j, 2]);

                        GL.TexCoord2(x, 1f-yt);
                        GL.Vertex3(wavePoints[i, j + 1, 0], wavePoints[i, j + 1, 1], wavePoints[i, j + 1, 2]);

                        GL.TexCoord2(xt, 1f-yt);
                        GL.Vertex3(wavePoints[i + 1, j + 1, 0], wavePoints[i + 1, j + 1, 1], wavePoints[i + 1, j + 1, 2]);

                        //Second
                        GL.TexCoord2(x, 1f-y);
                        GL.Vertex3(wavePoints[i, j, 0], wavePoints[i, j, 1], wavePoints[i, j, 2]);

                        GL.TexCoord2(xt, 1f-yt);
                        GL.Vertex3(wavePoints[i + 1, j + 1, 0], wavePoints[i + 1, j + 1, 1], wavePoints[i + 1, j + 1, 2]);

                        GL.TexCoord2(xt, 1f-y);
                        GL.Vertex3(wavePoints[i + 1, j, 0], wavePoints[i + 1, j, 1], wavePoints[i + 1, j, 2]);

                    }
                }
            }
            GL.End();
            
        }

        /// <summary>
        /// Draws house.
        /// Triangles in order:
        /// Front side: 2-4
        /// Back side:  4-6
        /// Left side:  6-8
        /// Right side: 8-10
        /// Attic:      10-14
        /// Roof:       14-16
        /// </summary>
        private void drawHouse() {
            //Front side
            GL.BindTexture(TextureTarget.Texture2D, texID[filter]);
            drawTriangles(2, 16);
        }

        /// <summary>
        /// Draws triangles belonging to sector1.
        /// </summary>
        /// <param name="from">starting triangle</param>
        /// <param name="to">last triangle</param>
        private void drawTriangles(int from, int to) {
            SimpleStructures.Triangle item;
            GL.Begin(PrimitiveType.Triangles);
            {
                for(int i = from; i < to; i++) {
                    item = sector1.list[i];

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

            }
            GL.End();
        }
    }
}
