using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
//using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

using OpenTK;
using OpenTK.Graphics;
using OpenTK.Graphics.OpenGL;
using System.Windows.Forms.Integration;
using System.Windows.Forms;
using System.Diagnostics;
using System.IO;
using System.Drawing;
using System.Timers;
using System.Windows.Media;

namespace WpfApplication1_openTK
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        GLControl glControl;
        /// <summary>
        /// ID of our program on the graphics card
        /// </summary>
        int pgmID;

        /// <summary>
        /// Address of the vertex shader
        /// </summary>
        int vsID;

        /// <summary>
        /// Address of the fragment shader
        /// </summary>
        int fsID;

        /// <summary>
        /// Address of the color parameter
        /// </summary>
        int attribute_vcol;

        /// <summary>
        /// Address of the position parameter
        /// </summary>
        int attribute_vpos;

        /// <summary>
        /// Address of the modelview matrix uniform
        /// </summary>
        int uniform_mview;

        /// <summary>
        /// Address of the Vertex Buffer Object for our position parameter
        /// </summary>
        int vbo_position;

        /// <summary>
        /// Address of the Vertex Buffer Object for our color parameter
        /// </summary>
        int vbo_color;

        /// <summary>
        /// Address of the Vertex Buffer Object for our modelview matrix
        /// </summary>
        int vbo_mview;

        /// <summary>
        /// Index Buffer Object
        /// </summary>
        int ibo_elements;

        /// <summary>
        /// Array of our vertex positions
        /// </summary>
        Vector3[] vertdata;

        /// <summary>
        /// Array of our vertex colors
        /// </summary>
        Vector3[] coldata;

        /// <summary>
        /// Array of our modelview matrices
        /// </summary>
        Matrix4[] mviewdata;

        /// <summary>
        /// Array of our indices
        /// </summary>
        int[] indicedata;

        /// <summary>
        /// Current time, for animation
        /// </summary>
        float time = 0.0f;
        Stopwatch sw = new Stopwatch(); // available to all event handlers
        System.Timers.Timer timer;
        float rotation = 0;
        int ticks = 0;
        int lastTime = 0;


        public MainWindow()
        {
            InitializeComponent();
            CompositionTarget.Rendering += CompositionTarget_Rendering;
        }

        private void WindowsFormsHost_Initialized(object sender, EventArgs e)
        {
            var flags = GraphicsContextFlags.Default;
            //flags = GraphicsContextFlags.Embedded;
            glControl = new GLControl(new GraphicsMode(32, 24), 2, 0, flags);
           
            //render();
            glControl.MakeCurrent();
            glControl.Paint += GLControl_Paint;
            glControl.Dock = DockStyle.Fill;
            (sender as WindowsFormsHost).Child = glControl;
            Load();
          //  System.Windows.Forms.Application.Idle += Application_Idle;
            //timer = new System.Timers.Timer(10.0); 
            //timer.Elapsed += TimerElapsed;
            //timer.Start();
        }

        void CompositionTarget_Rendering(object sender, EventArgs e)
        {
            ticks = Environment.TickCount;
            double milliseconds = ComputeTimeSlice();
            Accumulate(milliseconds);
            Animate(milliseconds);
            //if (ticks > lastTime + 16)
            //{

            glControl.Invalidate();
        }

        private void TimerElapsed(object sender, ElapsedEventArgs e)
        {
            ticks = Environment.TickCount;
            double milliseconds = ComputeTimeSlice();
            Accumulate(milliseconds);
            Animate(milliseconds);
            //if (ticks > lastTime + 16)
            //{

            glControl.Invalidate();
        }

        void Application_Idle(object sender, EventArgs e)
        {
            ticks = Environment.TickCount;
            double milliseconds = ComputeTimeSlice();
            Accumulate(milliseconds);
            Animate(milliseconds);
            //if (ticks > lastTime + 16)
            //{

            glControl.Invalidate();
            //lastTime = ticks;
            //}

        }

        private void Animate(double milliseconds)
        {
            float deltaRotation = (float)milliseconds / 200.0f;
            rotation += deltaRotation;

            render();
            // glControl1.Invalidate();




        }

        double accumulator = 0;
        int idleCounter = 0;
        private void Accumulate(double milliseconds)
        {
            idleCounter++;
            accumulator += milliseconds;
            int konst = 1000;
            if (accumulator > konst)
            {
                label1.Content = "FPS: "+idleCounter.ToString();
                accumulator -= konst;
                idleCounter = 0; // don't forget to reset the counter!
            }
        }

        private double ComputeTimeSlice()
        {
            sw.Stop();
            double timeslice = sw.Elapsed.TotalMilliseconds;
            sw.Reset();
            sw.Start();
            return timeslice;
        }

        private void Load()
        {
            

            initProgram();

           // Application.Idle += Application_Idle; // press TAB twice after +=
            sw.Start(); // start at application boot


            vertdata = new Vector3[] { new Vector3(-0.8f, -0.8f,  -0.8f),
                new Vector3(0.8f, -0.8f,  -0.8f),
                new Vector3(0.8f, 0.8f,  -0.8f),
                new Vector3(-0.8f, 0.8f,  -0.8f),
                new Vector3(-0.8f, -0.8f,  0.8f),
                new Vector3(0.8f, -0.8f,  0.8f),
                new Vector3(0.8f, 0.8f,  0.8f),
                new Vector3(-0.8f, 0.8f,  0.8f),
            };

            indicedata = new int[]{
                //left
                0, 2, 1,
                0, 3, 2,
                //back
                1, 2, 6,
                6, 5, 1,
                //right
                4, 5, 6,
                6, 7, 4,
                //top
                2, 3, 6,
                6, 3, 7,
                //front
                0, 7, 3,
                0, 4, 7,
                //bottom
                0, 1, 5,
                0, 5, 4
            };


            coldata = new Vector3[] { new Vector3(1f, 0f, 0f),
                new Vector3( 0f, 0f, 1f),
                new Vector3( 0f,  1f, 0f),new Vector3(1f, 0f, 0f),
                new Vector3( 0f, 0f, 1f),
                new Vector3( 0f,  1f, 0f),new Vector3(1f, 0f, 0f),
                new Vector3( 0f, 0f, 1f)};

            mviewdata = new Matrix4[]{
                Matrix4.Identity
            };

            GL.ClearColor(System.Drawing.Color.CornflowerBlue);
            GL.PointSize(5f);


        }

        void initProgram()
        {
            /** In this function, we'll start with a call to the GL.CreateProgram() function,
             * which returns the ID for a new program object, which we'll store in pgmID. */
            pgmID = GL.CreateProgram();

            loadShader("vs.glsl", ShaderType.VertexShader, pgmID, out vsID);
            loadShader("fs.glsl", ShaderType.FragmentShader, pgmID, out fsID);

            /** Now that the shaders are added, the program needs to be linked.
             * Like C code, the code is first compiled, then linked, so that it goes
             * from human-readable code to the machine language needed. */
            GL.LinkProgram(pgmID);
            Console.WriteLine(GL.GetProgramInfoLog(pgmID));

            /** We have multiple inputs on our vertex shader, so we need to get
            * their addresses to give the shader position and color information for our vertices.
            * 
            * To get the addresses for each variable, we use the 
            * GL.GetAttribLocation and GL.GetUniformLocation functions.
            * Each takes the program's ID and the name of the variable in the shader. */
            attribute_vpos = GL.GetAttribLocation(pgmID, "vPosition");
            attribute_vcol = GL.GetAttribLocation(pgmID, "vColor");
            uniform_mview = GL.GetUniformLocation(pgmID, "modelview");

            /** Now our shaders and program are set up, but we need to give them something to draw.
             * To do this, we'll be using a Vertex Buffer Object (VBO).
             * When you use a VBO, first you need to have the graphics card create
             * one, then bind to it and send your information. 
             * Then, when the DrawArrays function is called, the information in
             * the buffers will be sent to the shaders and drawn to the screen. */
            GL.GenBuffers(1, out vbo_position);
            GL.GenBuffers(1, out vbo_color);
            GL.GenBuffers(1, out vbo_mview);

            /** We'll need to get another buffer object to put our indice data into.  */
            GL.GenBuffers(1, out ibo_elements);
        }

        void loadShader(String filename, ShaderType type, int program, out int address)
        {

            address = GL.CreateShader(type);
            using (StreamReader sr = new StreamReader(filename))
            {
                GL.ShaderSource(address, sr.ReadToEnd());
            }
            GL.CompileShader(address);
            GL.AttachShader(program, address);
            Console.WriteLine(GL.GetShaderInfoLog(address));
        }

        void render()
        {


            GL.BindBuffer(BufferTarget.ArrayBuffer, vbo_position);
            GL.BufferData<Vector3>(BufferTarget.ArrayBuffer, (IntPtr)(vertdata.Length * Vector3.SizeInBytes), vertdata, BufferUsageHint.StaticDraw);
            GL.VertexAttribPointer(attribute_vpos, 3, VertexAttribPointerType.Float, false, 0, 0);

            GL.BindBuffer(BufferTarget.ArrayBuffer, vbo_color);
            GL.BufferData<Vector3>(BufferTarget.ArrayBuffer, (IntPtr)(coldata.Length * Vector3.SizeInBytes), coldata, BufferUsageHint.StaticDraw);
            GL.VertexAttribPointer(attribute_vcol, 3, VertexAttribPointerType.Float, true, 0, 0);

            // time += (float)timet;

            mviewdata[0] = Matrix4.CreateRotationY(0.55f * rotation) * Matrix4.CreateRotationX(0.15f * rotation) * Matrix4.CreateTranslation(0.0f, 0.0f, -3.0f) * Matrix4.CreatePerspectiveFieldOfView(1.3f, glControl.ClientSize.Width / (float)glControl.ClientSize.Height, 1.0f, 40.0f);

            GL.UniformMatrix4(uniform_mview, false, ref mviewdata[0]);

            GL.UseProgram(pgmID);

            GL.BindBuffer(BufferTarget.ArrayBuffer, 0);


            GL.BindBuffer(BufferTarget.ElementArrayBuffer, ibo_elements);
            GL.BufferData(BufferTarget.ElementArrayBuffer, (IntPtr)(indicedata.Length * sizeof(int)), indicedata, BufferUsageHint.StaticDraw);


            /////////////
            GL.Viewport(0, 0, glControl.Width, glControl.Height);
            GL.Clear(ClearBufferMask.ColorBufferBit | ClearBufferMask.DepthBufferBit);
            GL.Enable(EnableCap.DepthTest);

            GL.EnableVertexAttribArray(attribute_vpos);
            GL.EnableVertexAttribArray(attribute_vcol);

            GL.DrawElements(BeginMode.Triangles, indicedata.Length, DrawElementsType.UnsignedInt, 0);

            GL.DisableVertexAttribArray(attribute_vpos);
            GL.DisableVertexAttribArray(attribute_vcol);

            GL.Flush();
            glControl.SwapBuffers();


        }


        private void GLControl_Paint(object sender, PaintEventArgs e)
        {
            
        }



    }
}
