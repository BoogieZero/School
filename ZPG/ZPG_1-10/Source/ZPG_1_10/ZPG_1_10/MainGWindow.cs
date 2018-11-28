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

namespace ZPG_1_10 {
    class MainGWindow : GameWindow {
        //Conversion
        private const double DEG_TO_RAD = Math.PI / 180;

        //Random generation
        private Random rnd;

        //Textures
        private readonly String[] TEXTURES 
            = new String[] {"Data/Crate.bmp",
                            "Data/Grass256x256.bmp",
                            "Data/Brick256x256.bmp",
                            "Data/Roof256x256.bmp",
                            "Data/BrickDoor256x256.bmp",
                            "Data/Flower.png"};

        private const int TEXTURE_CRATE_OFFSET = 0;
        private const int TEXTURE_GRASS_OFFSET = 3;
        private const int TEXTURE_BRICK_OFFSET = 6;
        private const int TEXTURE_ROOF_OFFSET = 9;
        private const int TEXTURE_BDOOR_OFFSET = 12;
        private const int TEXTURE_FLOWER_OFFSET = 15;

        private const int NUM_OF_TEXTURES = 18;
        private int[] texID = new int[NUM_OF_TEXTURES];
        private int filter = 0;

        //Wordl
        private const String WORLD_FILE = "Data/World.txt";
        private const int NUM_OF_FLOWERS = 50;
        private float[,] rFlowers;
        private SimpleStructures.Sector sector1;

        //Movement
        private const float PERSON_HEIGHT   = 0.25f;
        private const float MOVEMENT_SPEED  = 0.05f;
        private const float ROTATION_SPEED  = 2.0f;
        private float xpos = 0;
        private float zpos = 0;
        private float yrot = 0;

        //Lighting
        private readonly float[] LIGHT_AMBIENT = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
        private readonly float[] LIGHT_DIFFUSE = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        private readonly float[] LIGHT_POSITION = new float[] { 0.0f, 0.0f, 2.0f, 1.0f };
        private System.Boolean LIGHTS_ON = false;

        //Blending
        private System.Boolean BLENDING_ALLOW = false;

        public MainGWindow() : base(500, 500, GraphicsMode.Default) {
            rnd = new Random(new System.DateTime().Millisecond);
            randomizeFlowers();
            Keyboard.KeyDown += Keyboard_KeyDown;
        }

        /// <summary>
        /// Creates values for random translation on x,z axis and scale 
        /// to rFlower field.
        /// </summary>
        private void randomizeFlowers() {
            rFlowers = new float[NUM_OF_FLOWERS, 3];
            for(int i = 0; i < NUM_OF_FLOWERS; i++) {
                rFlowers[i, 0] = rnd.Next(-30, 30) / 10f;       //x
                rFlowers[i, 1] = rnd.Next(-10, 30) / 10f;       //z
                rFlowers[i, 2] = rnd.Next(5, 8) / 10f;          //scale
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
        /// <param name="sender"></param>
        /// <param name="e"></param>
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
            GL.TexEnv(TextureEnvTarget.TextureEnv, TextureEnvParameter.TextureEnvMode, (int)TextureEnvMode.Modulate);

            if(!loadTextures()) {
                Console.WriteLine("Loading textures: Error");
                Console.ReadKey();
                Exit();
            } else {
                Console.WriteLine("Loading textures: OK");
            }

            if(!loadWorld()) {
                Console.WriteLine("Loading world: Error");
                Console.ReadKey();
                Exit();
            }else {
                Console.WriteLine("Loading world: OK");
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
        /// Creates textures from paths in TEXTURE[]
        /// </summary>
        /// <returns>true if loading textures was succesfull</returns>
        private bool loadTextures() {
            int index = 0;
            Bitmap bmp;

            for(int i = 0; i < TEXTURES.Length; i++) {
                if(!createBitmap(TEXTURES[i], out bmp)) {
                    return false;
                }
                index = texForFilters(bmp, index);
            }

            return true;
        }

        /// <summary>
        /// Creates texture for each filter.
        /// (Nearest, Linear, MipMap)
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="index">index for first texture</param>
        /// <returns></returns>
        private int texForFilters(Bitmap bmp, int index) {
            if(index == 15) {
                createTexture(bmp, index, (int)TextureMinFilter.Nearest, (int)TextureMagFilter.Nearest, true);
                createTexture(bmp, index + 1, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear, true);
                createTexture(bmp, index + 2, (int)TextureMinFilter.LinearMipmapNearest, (int)TextureMagFilter.Linear, true);
            } else {
                createTexture(bmp, index, (int)TextureMinFilter.Nearest, (int)TextureMagFilter.Nearest);
                createTexture(bmp, index+1, (int)TextureMinFilter.Linear, (int)TextureMagFilter.Linear);
                createTexture(bmp, index+2, (int)TextureMinFilter.LinearMipmapNearest, (int)TextureMagFilter.Linear);
            }
            return index + 3;
        }

        /// <summary>
        /// Creates non-flower texture for each filter.
        /// </summary>
        /// <param name="bmp"></param>
        /// <param name="indexID"></param>
        /// <param name="texFilterMin"></param>
        /// <param name="texFilterMag"></param>
        private void createTexture(Bitmap bmp, int indexID, int texFilterMin, int texFilterMag) {
            createTexture(bmp, indexID, texFilterMin, texFilterMag, false);
        }

        /// <summary>
        /// Creates texture by given parameters.
        /// </summary>
        /// <param name="bmp">source Bitmap</param>
        /// <param name="indexID">ID of new texture</param>
        /// <param name="texFilterMin">texture filter min</param>
        /// <param name="texFilterMag">texture filter mag</param>
        /// <param name="flower">true for flower texture</param>
        private void createTexture(Bitmap bmp, int indexID, int texFilterMin, int texFilterMag, System.Boolean flower) {
            GL.GenTextures(indexID, out texID[indexID]);
            GL.BindTexture(TextureTarget.Texture2D, texID[indexID]);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, texFilterMin);
            GL.TexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, texFilterMag);

            BitmapData data;
            if(flower) {
                data = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadOnly,
                System.Drawing.Imaging.PixelFormat.Format32bppArgb
                );

                GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba,
                    data.Width, data.Height, 0, OpenTK.Graphics.OpenGL.PixelFormat.Bgra,
                    PixelType.UnsignedByte, data.Scan0);
            } else {
                data = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadOnly,
                System.Drawing.Imaging.PixelFormat.Format24bppRgb
                );

                GL.TexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.Rgba,
                    data.Width, data.Height, 0, OpenTK.Graphics.OpenGL.PixelFormat.Bgr,
                    PixelType.UnsignedByte, data.Scan0);
            }

            if(texFilterMin == (int)TextureMinFilter.LinearMipmapNearest) {
                GL.GenerateMipmap(GenerateMipmapTarget.Texture2D);
            }

            bmp.UnlockBits(data);
        }

        /// <summary>
        /// Creates Bitmap from file on given path
        /// </summary>
        /// <param name="path">path to file</param>
        /// <param name="bmp">created bitmap</param>
        /// <returns></returns>
        private bool createBitmap(string path, out Bitmap bmp) {
            if(!File.Exists(path)) {
                Console.WriteLine("Texture not found: " + path);
                bmp = null;
                return false;
            }
            bmp = new Bitmap(path);
            return true;
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

            renderWorld();

            SwapBuffers();
        }

        /// <summary>
        /// Draws world from first sector.
        /// </summary>
        private void renderWorld() {
            //Floor
            setBlending(false);
            drawTileFloor(1.0f,1.0f,3);

            //House
            GL.PushMatrix();
            
            GL.Translate(-3f, 0f, -1.5f);
            GL.Rotate(15f, 0f, 1f, 0f);
            for(int i = 0; i <= 2; i++) {
                GL.Translate(1.5f, 0f, 0f);
                GL.Rotate(-15f, 0f, 1f, 0f);
                drawHouse();
            }
            GL.PopMatrix();

            //Flowers
            GL.PushMatrix();

            setBlending(true);
            GL.Color4(1f, 1f, 1f, 1f);

            for(int i = 0; i < NUM_OF_FLOWERS; i++) {
                GL.PushMatrix();
                GL.Translate(rFlowers[i, 0], 0f, rFlowers[i, 1]);
                GL.Scale(rFlowers[i, 2], rFlowers[i, 2], rFlowers[i, 2]);
                drawFlower();
                GL.PopMatrix();
            }
            setBlending(false);
            GL.PopMatrix();
        }

        /// <summary>
        /// Draws flower which is always facing view.
        /// </summary>
        private void drawFlower() {
            GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_FLOWER_OFFSET + filter]);
            GL.Rotate(+yrot, 0f, 1f, 0f);
            GL.Scale(1.5f, 1.5f, 0f);
            drawTriangles(16, 18);
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
            GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_BDOOR_OFFSET + filter]);
            drawTriangles(2, 4);

            //Back, Left, Right side, Attic
            GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_BRICK_OFFSET + filter]);
            drawTriangles(4, 14);

            //Roof
            GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_ROOF_OFFSET + filter]);
            drawTriangles(14, 16);

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
        /// Creates floor by given parameters.
        /// Example:
        ///     layers = 0 -> 1 tile
        ///     layers = 1 -> 9 tiles
        /// </summary>
        /// <param name="offsetx">width of floor tile</param>
        /// <param name="offsetz">height of floor tile</param>
        /// <param name="layers">number of layers around starting tile</param>
        private void drawTileFloor(float offsetx, float offsetz, int layers) {
            SimpleStructures.Triangle item;

            //Two triangle for one quad
            for(int i = 0; i < 2; i++) {
                item = sector1.list[i];

                GL.BindTexture(TextureTarget.Texture2D, texID[TEXTURE_GRASS_OFFSET + filter]);
                GL.Begin(PrimitiveType.Triangles);
                {
                    for(int iz = -layers; iz <= layers; iz++) {
                        for(int ix = -layers; ix <= layers; ix++) {
                            GL.Normal3(0f, 0f, 1f);
                            //First
                            GL.TexCoord2(
                                item.verts[0].u,
                                item.verts[0].v
                                );
                            GL.Vertex3(
                                item.verts[0].x + (offsetx * ix),
                                item.verts[0].y,
                                item.verts[0].z + (offsetz * iz)
                                );
                            //Second
                            GL.TexCoord2(
                                item.verts[1].u,
                                item.verts[1].v
                                );
                            GL.Vertex3(
                                item.verts[1].x + (offsetx * ix),
                                item.verts[1].y,
                                item.verts[1].z + (offsetz * iz)
                                );
                            //Third
                            GL.TexCoord2(
                                item.verts[2].u,
                                item.verts[2].v
                                );
                            GL.Vertex3(
                                item.verts[2].x + (offsetx * ix),
                                item.verts[2].y,
                                item.verts[2].z + (offsetz * iz)
                                );
                        }
                    }
                    
                }
                GL.End();
            }
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
    }
}
