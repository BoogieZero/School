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
                            PATH_TEXTURES+"Cube.bmp"};

        private int[] texID;                    //array of textures in OpenGL
        private int filter = 0;                 //number of actual filter

        //Asteroid colors (box)
            //box
        private float[][] boxColor = {
            new float[] { 1.0f, 0.0f, 0.0f },
            new float[] { 1.0f, 0.5f, 0.0f },
            new float[] { 1.0f, 1.0f, 0.0f },
            new float[] { 0.0f, 1.0f, 0.0f },
            new float[] { 0.0f, 1.0f, 1.0f }
        };

            //top
        private float[][] topColor = {
            new float[] {0.5f,0.0f,0.0f},
            new float[] {0.5f,0.25f,0.0f},
            new float[] {0.5f,0.5f,0.0f},
            new float[] {0.0f,0.5f,0.0f},
            new float[] {0.0f,0.5f,0.5f}
        };

        //Pregenerated lists
        private int displayList;

        //Wordl
        private const String PATH_WORLDS = "Data/World/";
        private const String WORLD_FILE = PATH_WORLDS+"Flag.txt";
        private SimpleStructures.Sector sector1;

        //Movement
        private const System.Boolean MOVEMENT_CENTRAL = true;
        private const float PERSON_HEIGHT = 0.25f;      //camera height
        private const float MOVEMENT_SPEED = 0.05f;
        private const float ROTATION_SPEED = 2.0f;
            //Transformation values for actual position
        private float xpos = 0;
        private float zpos = 0;
        private float yrot = 0;
            //Center movement
        private float ztransC = 0;
        private float yrotC = 0;
        private float xrotC = 0;
        private System.Boolean btnShiftDown = false;    //true for key shift pressed down

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
            Keyboard.KeyUp += Keyboard_KeyUp;
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

            //shift modifier
            if(e.Key == OpenTK.Input.Key.ShiftLeft) {
                btnShiftDown = true;
            }
        }

        /// <summary>
        /// Reseting key modifiers.
        /// </summary>
        /// <param name="sender">not used</param>
        /// <param name="e">key released event</param>
        private void Keyboard_KeyUp(object sender, OpenTK.Input.KeyboardKeyEventArgs e) {
            if(e.Key == OpenTK.Input.Key.ShiftLeft) {
                btnShiftDown = false;
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
            GL.Enable(EnableCap.ColorMaterial);
            //Blending
            GL.BlendFunc(BlendingFactorSrc.SrcAlpha, BlendingFactorDest.OneMinusSrcAlpha);
            //Textures
            GL.Enable(EnableCap.Texture2D);
            //GL.TexEnv(TextureEnvTarget.TextureEnv, TextureEnvParameter.TextureEnvMode, (int)TextureEnvMode.Modulate);
            
            if(!TextureLoader.load(TEXTURES, out texID)) {
                Exit();
            }
            //Generate lists
            genLists();

            //Front side filled with polygons
            GL.PolygonMode(MaterialFace.Front, PolygonMode.Fill);
            //Back side filled by grid
            GL.PolygonMode(MaterialFace.Back, PolygonMode.Line);
        }

        /// <summary>
        /// Called when is time to set up next frame.
        /// Game logic here.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnUpdateFrame(FrameEventArgs e) {
            base.OnUpdateFrame(e);

            //Movement
            if(MOVEMENT_CENTRAL) {
                movementCenteral();
            }else {
                movementDefault();
            }

        }

        /// <summary>
        /// Movement around center of coordinates.
        /// </summary>
        private void movementCenteral() {
            //mov Z
            if(Keyboard[OpenTK.Input.Key.Up] && !btnShiftDown)
                ztransC += MOVEMENT_SPEED;
            if(Keyboard[OpenTK.Input.Key.Down] && !btnShiftDown) 
                ztransC -= MOVEMENT_SPEED;
            //rot X
            if(Keyboard[OpenTK.Input.Key.Up] && btnShiftDown)
                xrotC += ROTATION_SPEED;
            if(Keyboard[OpenTK.Input.Key.Down] && btnShiftDown)
                xrotC -= ROTATION_SPEED;
            //rot Y
            if(Keyboard[OpenTK.Input.Key.Left])
                yrotC += ROTATION_SPEED;
            if(Keyboard[OpenTK.Input.Key.Right])
                yrotC -= ROTATION_SPEED;
        }

        /// <summary>
        /// Default movement through 3D scene.
        /// </summary>
        private void movementDefault() {
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
            //GL.Translate(0f, -PERSON_HEIGHT, 0f);

            //movement
            movTrans();
            
            drawAteroids();

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

        /// <summary>
        /// Generates list for box and top
        /// </summary>
        private void genLists() {
            displayList = GL.GenLists(2);

            //Box
            GL.NewList(displayList, ListMode.Compile);
            {
                GL.Begin(PrimitiveType.Quads);
                {
                    //Bottom
                    GL.Normal3(0.0f, -1.0f, 0.0f);
                    GL.TexCoord2(1.0f, 1f-1.0f); GL.Vertex3(-1.0f, -1.0f, -1.0f);
                    GL.TexCoord2(0.0f, 1f-1.0f); GL.Vertex3(1.0f, -1.0f, -1.0f);
                    GL.TexCoord2(0.0f, 1f-0.0f); GL.Vertex3(1.0f, -1.0f, 1.0f);
                    GL.TexCoord2(1.0f, 1f-0.0f); GL.Vertex3(-1.0f, -1.0f, 1.0f);
                    //Front
                    GL.Normal3(0.0f, 0.0f, 1.0f);
                    GL.TexCoord2(0.0f, 1f-0.0f); GL.Vertex3(-1.0f, -1.0f, 1.0f);
                    GL.TexCoord2(1.0f, 1f-0.0f); GL.Vertex3(1.0f, -1.0f, 1.0f);
                    GL.TexCoord2(1.0f, 1f-1.0f); GL.Vertex3(1.0f, 1.0f, 1.0f);
                    GL.TexCoord2(0.0f, 1f-1.0f); GL.Vertex3(-1.0f, 1.0f, 1.0f);
                    //Back
                    GL.Normal3(0.0f, 0.0f, -1.0f);
                    GL.TexCoord2(1.0f, 1f-0.0f); GL.Vertex3(-1.0f, -1.0f, -1.0f);
                    GL.TexCoord2(1.0f, 1f-1.0f); GL.Vertex3(-1.0f, 1.0f, -1.0f);
                    GL.TexCoord2(0.0f, 1f-1.0f); GL.Vertex3(1.0f, 1.0f, -1.0f);
                    GL.TexCoord2(0.0f, 1f-0.0f); GL.Vertex3(1.0f, -1.0f, -1.0f);
                    //Right
                    GL.Normal3(1.0f, 0.0f, 0.0f);
                    GL.TexCoord2(1.0f, 1f-0.0f); GL.Vertex3(1.0f, -1.0f, -1.0f);
                    GL.TexCoord2(1.0f, 1f-1.0f); GL.Vertex3(1.0f, 1.0f, -1.0f);
                    GL.TexCoord2(0.0f, 1f-1.0f); GL.Vertex3(1.0f, 1.0f, 1.0f);
                    GL.TexCoord2(0.0f, 1f-0.0f); GL.Vertex3(1.0f, -1.0f, 1.0f);
                    //Left
                    GL.Normal3(-1.0f, 0.0f, 0.0f);
                    GL.TexCoord2(0.0f, 1f-0.0f); GL.Vertex3(-1.0f, -1.0f, -1.0f);
                    GL.TexCoord2(1.0f, 1f-0.0f); GL.Vertex3(-1.0f, -1.0f, 1.0f);
                    GL.TexCoord2(1.0f, 1f-1.0f); GL.Vertex3(-1.0f, 1.0f, 1.0f);
                    GL.TexCoord2(0.0f, 1f-1.0f); GL.Vertex3(-1.0f, 1.0f, -1.0f);
                }
                GL.End();
            }
            GL.EndList();

            //Top
            GL.NewList(displayList + 1, ListMode.Compile);
            {
                GL.Begin(PrimitiveType.Quads);
                {
                    //Top
                    GL.Normal3(0.0f, 1.0f, 0.0f);
                    GL.TexCoord2(0.0f, 1f - 1.0f); GL.Vertex3(-1.0f, 1.0f, -1.0f);
                    GL.TexCoord2(0.0f, 1f - 0.0f); GL.Vertex3(-1.0f, 1.0f, 1.0f);
                    GL.TexCoord2(1.0f, 1f - 0.0f); GL.Vertex3(1.0f, 1.0f, 1.0f);
                    GL.TexCoord2(1.0f, 1f - 1.0f); GL.Vertex3(1.0f, 1.0f, -1.0f);
                }
                GL.End();
            }
            GL.EndList();
        }

        /// <summary>
        /// Draws asteroids.
        /// </summary>
        private void drawAteroids() {
            GL.BindTexture(TextureTarget.Texture2D, texID[3+filter]);

            for(int yloop = 0; yloop < 6; yloop++) {
                for(int xloop = 0; xloop < yloop; xloop++) {
                    GL.PushMatrix();
                    GL.Translate(
                        (xloop*2.8f) - (yloop*1.4f) + 1.4f, //+1.4f
                        (6f - yloop) * 2f - 7f,
                        0
                        );
                    GL.Rotate(45f, 0f, 1f, 0f);
                    GL.Color3(boxColor[yloop - 1]);
                    GL.CallList(displayList);
                    GL.Color3(topColor[yloop - 1]);
                    GL.CallList(displayList + 1);
                    GL.PopMatrix();
                }
            }
        }

        /// <summary>
        /// Movement transformation according.
        /// </summary>
        private void movTrans() {
            if(MOVEMENT_CENTRAL) {
                GL.Translate(0f, 0f, -4f);
                //movementCentral
                GL.Translate(0f, 0f, ztransC);
                GL.Rotate(xrotC, 1f, 0f, 0f);
                GL.Rotate(yrotC, 0f, 1f, 0f);
            } else {
                //movementDefault
                GL.Rotate(360 - yrot, 0f, 1f, 0f);
                GL.Translate(xpos, 0f, zpos);
                //starting position
                GL.Translate(0f, 0f, -4f);
            }
        }
    }
}
