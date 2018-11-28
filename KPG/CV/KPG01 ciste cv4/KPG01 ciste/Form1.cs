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
        public int timeSpeed = 10;
        float spiral = 1f;

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
            if (bmp == null) return;
            Graphics grp = panel1.CreateGraphics();


            //int whiteSpaceLeft = 0;
            //int whiteSpaceTop = 0;
            //double aspectRatioSrc = (double)bmp.Width / bmp.Height;
            //double aspectRatioDst = (double)panel1.Width / panel1.Height;
            //if (aspectRatioSrc > aspectRatioDst)
            //{
            //    whiteSpaceTop = panel1.Height - (int)(panel1.Width / aspectRatioSrc);
            //    whiteSpaceTop >>= 1;
            //}
            //else
            //{
            //    whiteSpaceLeft = panel1.Width - (int)(panel1.Height * aspectRatioSrc);
            //    whiteSpaceLeft >>= 1;
            //}
            //Rectangle rSource = new Rectangle(0, 0, bmp.Width, bmp.Height);
            //Rectangle rDestination = new Rectangle(whiteSpaceLeft, whiteSpaceTop, panel1.Width - (whiteSpaceLeft << 1), panel1.Height - (whiteSpaceTop << 1));
            // grp.DrawImage(bmp, rDestination, rSource, GraphicsUnit.Pixel);

            // grp.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
            grp.DrawImage(bmp, 0, 0, panel1.Width, panel1.Height);
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
            else if (e.Button == MouseButtons.Left && checkBox2.Checked) {
               
                kliknutoL = true;
                SolidBrush sb = new SolidBrush(Color.White);
                float offset = 1f;
                float tloustkaF = tloustka * offset;
                graphics.FillEllipse(sb, tecka.X - tloustkaF * 0.5f, tecka.Y - tloustkaF * 0.5f, tloustkaF, tloustkaF);
                vykresliDoPanelu();
            }


        }

        private void panel1_MouseMove(object sender, MouseEventArgs e)
        {
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

        private void button5_Click(object sender, EventArgs e) {
            prepinacAnimace = 2;
            mujTimer.Interval = timeSpeed;
            mujTimer.Start();
        }

        private void trackBar1_Scroll(object sender, EventArgs e) {
            timeSpeed = trackBar1.Value;
            mujTimer.Interval = timeSpeed;
        }

        private void button11_Click(object sender, EventArgs e) {
            Pen pen = new Pen(Color.Black);

            float rx = 50;
            float ry = 50;
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;

            for(double i = 0.0; i < 720.0; i += 0.1) {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + rx * (System.Math.Cos(angle) + System.Math.Sin(angle / 2)));
                int yY = (int)(posunY + ry * (System.Math.Sin(angle) + System.Math.Cos(angle / 2)));
                if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                    graphics.DrawEllipse(pen, xX, yY, 5, 5);
                }
            }

            vykresliDoPanelu();
        }

        private void button12_Click(object sender, EventArgs e) {
            Pen pen = new Pen(Color.Black);

            float rx = 50;
            float ry = 50;
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;

            for(double i = 0.0; i < 720.0; i += 0.1) {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + rx * (System.Math.Cos(angle) + System.Math.Sin(angle)));
                int yY = (int)(posunY + ry * (2 * System.Math.Sin(angle) + System.Math.Cos(angle)));
                if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                    graphics.DrawEllipse(pen, xX, yY, 5, 5);
                }
            }

            vykresliDoPanelu();
        }

        private void button13_Click(object sender, EventArgs e) {
            Pen pen = new Pen(Color.Black);

            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;
            float a = 50;

            for(double i = 0.0; i < 360.0; i += 0.1) {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + 2 * a * (1 + Math.Cos(angle)) * Math.Cos(angle));
                int yY = (int)(posunY + 2 * a * (1 + Math.Cos(angle)) * Math.Sin(angle));
                if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                    graphics.DrawEllipse(pen, xX, yY, 5, 5);
                }
            }

            vykresliDoPanelu();
        }

        private void button14_Click(object sender, EventArgs e) {
            Pen pen = new Pen(Color.Black);

            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;
            float a = 100;

            for(double i = 0.0; i < 360.0; i += 0.1) {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + a * Math.Pow(Math.Cos(angle), 3));
                int yY = (int)(posunY + a * Math.Pow(Math.Sin(angle), 3));
                if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                    graphics.DrawEllipse(pen, xX, yY, 5, 5);
                }
            }

            vykresliDoPanelu();
        }

        private void button15_Click(object sender, EventArgs e) {
            Pen pen = new Pen(Color.Black);

            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;
            float a = 5;

            for(double i = 0.0; i < 360.0 * 10; i += 0.1) {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + a * angle * Math.Cos(angle));
                int yY = (int)(posunY + a * angle * Math.Sin(angle));
                if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                    graphics.DrawEllipse(pen, xX, yY, 5, 5);
                }
            }

            vykresliDoPanelu();
        }

        private void button16_Click(object sender, EventArgs e) {
            Pen pen = new Pen(Color.Black);

            float add = 0;

            float rx = 100;
            float ry = 100;
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;

            for(int j = 0; j < 5; j++) {
                Console.Write("main\n");
                for(double i = 0.0; i < 720.0 * 1; i += 0.1) {
                    double angle = i * System.Math.PI / 180;

                    int xX = (int)(posunX + (rx - add) * (System.Math.Cos(angle) + System.Math.Sin(angle / 2)));
                    int yY = (int)(posunY + (ry - add) * (System.Math.Sin(angle) + System.Math.Cos(angle / 2)));
                    //add += 0.01f;
                    if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                        graphics.DrawEllipse(pen, xX, yY, 5, 5);
                    }
                }
            }
            vykresliDoPanelu();
        }

        private void button10_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();
            x = 0;
            y = 0;
            t = 0;
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
                float rx = 100;
                float ry = 100;
                //float spiralConst = 1f;
                float posunX = bmp.Width / 2;
                float posunY = bmp.Height / 2;
                vypln = new SolidBrush(Color.Black);
                if (t > 360*20 -90) return;

                double angle = t * System.Math.PI / 180;
                int xX = (int)( (posunX + (rx - spiral) * System.Math.Cos(angle)));
                int yY = (int)( (posunY + (ry - spiral) * System.Math.Sin(angle)));
                //bmp.SetPixel(xX, yY, Color.Red);
                graphics.FillRectangle(vypln, xX, yY, 10, 10);
                t++;
                spiral += 0.005f;
                //vypln = new SolidBrush(Color.Black, 10 * (1 - spiralConst));

            }

            vykresliDoPanelu();
            //drawsomething();
            return;
        }

    }
}
