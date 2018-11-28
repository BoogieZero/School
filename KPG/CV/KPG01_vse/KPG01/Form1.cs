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

namespace KPG01
{
    public partial class Form1 : Form
    {

        public Graphics graphics;
        public Pen pero;
        public Brush vypln;
        public SolidBrush stetec;
        public Bitmap bmp;
        public bool kliknutoL = false;
        public bool kliknutoP = false;
        public Timer mujTimer;
        public Point pocatecniBod;
        public int prepinacAnimace;
        int x, y,t;
        Color barvaKresleni;
        public float tloustka = 5;
        public Point pozice;
        public Size velikost;
        public Bludiste bludiste;
        public Position poziceHrace;
        public Position cil;
        bool vyhra;

        public Form1()
        {
            InitializeComponent();
            bmp = new Bitmap(panel1.Width,panel1.Height);
            //graphics = panel1.CreateGraphics();
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(Color.White);
            inicializaceCasovace();
            barvaKresleni = Color.Red;
            pero = new Pen(Color.Black);
            definujBarvy();
            pozice = new Point(0, 0);
            velikost = new Size(15, 15);
            this.KeyPreview = true;
            vyhra = false;
        }

        public void inicializaceCasovace() {
            mujTimer = new Timer();
            mujTimer.Enabled = false;
            mujTimer.Tick += new EventHandler(animace);        
        }

        
        private void gumujPanelDoBela() {
            graphics.Clear(Color.White);        
        }

        private void vykresliDoPanelu()
        {
            panel1_Paint(this, null);
           //panel1.BackgroundImage = bmp;            
            //panel1.Invalidate();
            //panel1.Refresh();
            //panel1.Invalidate();
        }

        private void kresliObdelnik(Color c, float x, float y, float width, float height){
            vypln = new SolidBrush(c);
            graphics.FillRectangle(vypln, x, y, width, height);        
        }

        private void kresliCaru(Color c, float tloustka, Point a, Point b) {
            pero = new Pen(c, tloustka);
            graphics.DrawLine(pero, a, b);
            vykresliDoPanelu();
        }
        

        //kresleni kruznice
        private void button11_Click(object sender, EventArgs e)
        {
            float rx = 200;
            float ry = 200;
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;

            for (double i = 0.0; i < 360.0; i += 0.1)
            {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + rx * System.Math.Cos(angle));
                int yY = (int)(posunY + ry * System.Math.Sin(angle));
                bmp.SetPixel(xX, yY, Color.Red);
            }

            vykresliDoPanelu();

        }

               

        //guma
        private void button2_Click(object sender, EventArgs e)
        {
            gumujPanelDoBela();
            vykresliDoPanelu();
        }


        //kresli obdelnik
        private void button3_Click(object sender, EventArgs e)
        {
            kresliObdelnik(Color.Red, 10, 10, 100, 100);
            vykresliDoPanelu();
        }

        //uklada obrazek
        private void button4_Click(object sender, EventArgs e)
        {

            //Rectangle rec = new Rectangle(0, 0, panel1.Width, panel1.Height);
            //panel1.DrawToBitmap(bmp, rec);
           

            //panel1.BackColor = Color.Black;
            //panel1.BackgroundImage = bmp;

            SaveFileDialog sfd = new SaveFileDialog();
            sfd.FileName = string.Empty;
            sfd.Filter = "Portable Network Graphics *.png|*.png|Windows Bitmap *.bmp|*.bmp|Jpeg *.jpg|*.jpg";
            if (sfd.ShowDialog() != System.Windows.Forms.DialogResult.OK)
            {
                return;
            }

            try
            {
                bmp.Save(sfd.FileName);
            }
            catch (Exception exc)
            {
                MessageBox.Show(exc.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
                       
        }

        //kresli caru
        private void button5_Click(object sender, EventArgs e)
        {
            kresliCaru(Color.Blue,10, new Point(200, 200), new Point(100, 300));
        }


        //generuje sedadla
        private void button1_Click(object sender, EventArgs e)
        {
            float pocatekX = 5;
            float pocatekY = 5;
            float velikost = 20;
            float distanc = 2;
            float x, y;

            for (int i = 0; i < 10; i++)
            {
                for (int j = 0; j < 5; j++)
                {
                    x = pocatekX + i * (distanc + velikost);
                    y = pocatekY + j * (distanc + velikost);

                    kresliObdelnik(Color.Green, x, y, velikost, velikost);

                }
            }
            vykresliDoPanelu();
        }

        //kresli ohraniceni kina
        private void button6_Click(object sender, EventArgs e)
        {
            Point A = new Point(1, 1);
            Point B = new Point(226, 1);
            Point C = new Point(226, 130);
            Point D = new Point(1, 130);
            
            kresliCaru(Color.Black, 3, A, B);
            kresliCaru(Color.Black, 3, B, C);
            kresliCaru(Color.Black, 3, C, D);
            kresliCaru(Color.Black, 3, D, A);

            kresliCaru(Color.Red, 5, new Point(113-30, 125), new Point(113+30, 125));
        }

        private void button7_Click(object sender, EventArgs e)
        {
            Point pocatek = new Point(0, 130);
            graphics.DrawString("Kinosál - plátko je červené.", new Font("Arial", 15), Brushes.Blue,pocatek);
            vykresliDoPanelu();
        }




        private void Form1_Resize(object sender, EventArgs e)
        {
            if (panel1.Width < 50 || panel1.Height < 50) return;

            Bitmap tmp = new Bitmap(panel1.Width, panel1.Height);
            Graphics g = Graphics.FromImage((Image)tmp);

            g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;
            g.DrawImage(bmp, 0, 0, panel1.Width, panel1.Height);

            bmp = tmp;
            graphics = Graphics.FromImage(bmp);
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
        }

        private void panel1_MouseMove(object sender, MouseEventArgs e)
        {
            if (checkBox1.Checked&&kliknutoL) {             //kresleni stetcem

                
                           
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
            if (e.Button == MouseButtons.Left)
            {
                kliknutoL = false;
            }else if (e.Button == MouseButtons.Right)
            {
                kliknutoP = false;
            }
        }

                  


       
        /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
       

        private void animace(object sender, EventArgs e)
        {
            if(prepinacAnimace==1){
            x += 10;
            y += 10;                          

            kresliCaru(Color.Coral, 5, new Point(x, y), new Point(x + 2, y + 2));
            }else if(prepinacAnimace==2){
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


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //start - jednoducha animace 1
        private void button8_Click(object sender, EventArgs e)
        {
            prepinacAnimace = 1;
            mujTimer.Start();
        }

        //pauza
        private void button9_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();
        }

        //stop
        private void button10_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();
            x = 0;
            y = 0;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        

        private void button12_Click(object sender, EventArgs e)
        {
            prepinacAnimace = 2;
            vypln = Brushes.Fuchsia;
            mujTimer.Start(); 
        }

        private void button14_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();

        }

        private void button13_Click(object sender, EventArgs e)
        {
            mujTimer.Stop();
            t = 0;
        }

        private void numericUpDown1_ValueChanged(object sender, EventArgs e)
        {
            mujTimer.Interval = (int)numericUpDown1.Value;
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //krivka
        private void button15_Click(object sender, EventArgs e)
        {
            float rx = 50;
            float ry = 50;
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;

            for (double i = 0.0; i < 720.0; i += 0.1)
            {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + rx * (System.Math.Cos(angle) + System.Math.Sin(angle/2)));
                int yY = (int)(posunY + ry * (System.Math.Sin(angle) + System.Math.Cos(angle/2)));
                if (xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0)
                {
                    bmp.SetPixel(xX, yY, Color.Red);
                }
            }

            vykresliDoPanelu();
        }

        private void button16_Click(object sender, EventArgs e)
        {
            float rx = 50;
            float ry = 50;
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;

            for (double i = 0.0; i < 720.0; i += 0.1)
            {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + rx * (System.Math.Cos(angle) + System.Math.Sin(angle)));
                int yY = (int)(posunY + ry * (2*System.Math.Sin(angle) + System.Math.Cos(angle)));
                if (xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0)
                {
                    bmp.SetPixel(xX, yY, Color.Red);
                }
            }

            vykresliDoPanelu();
        }

        private void button17_Click(object sender, EventArgs e)
        {
           
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;
            float a = 50;

            for (double i = 0.0; i < 360.0; i += 0.1)
            {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + 2*a*(1+Math.Cos(angle))*Math.Cos(angle));
                int yY = (int)(posunY + 2*a*(1+Math.Cos(angle))*Math.Sin(angle));
                if (xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0)
                {
                    bmp.SetPixel(xX, yY, Color.Red);
                }
            }

            vykresliDoPanelu();
        }

        private void button18_Click(object sender, EventArgs e)
        {
         
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;
            float a = 100;

            for (double i = 0.0; i < 360.0; i += 0.1)
            {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + a*Math.Pow(Math.Cos(angle),3));
                int yY = (int)(posunY + a * Math.Pow(Math.Sin(angle), 3));
                if (xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0)
                {
                    bmp.SetPixel(xX, yY, Color.Red);
                }
            }

            vykresliDoPanelu();
        }

        private void button19_Click(object sender, EventArgs e)
        {
            float posunX = bmp.Width / 2;
            float posunY = bmp.Height / 2;
            float a = 5;

            for (double i = 0.0; i < 360.0*10; i += 0.1)
            {
                double angle = i * System.Math.PI / 180;
                int xX = (int)(posunX + a*angle * Math.Cos(angle));
                int yY = (int)(posunY + a *angle* Math.Sin(angle));
                if (xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0)
                {
                    bmp.SetPixel(xX, yY, Color.Red);
                }
            }

            vykresliDoPanelu();
        }


        //vykreslovani do panelu
        private void panel1_Paint(object sender, PaintEventArgs e)
        {
            if (bmp == null) return;
            Graphics grp = panel1.CreateGraphics();
            int whiteSpaceLeft = 0;
            int whiteSpaceTop = 0;
            double aspectRatioSrc = (double)bmp.Width / bmp.Height;
            double aspectRatioDst = (double)panel1.Width / panel1.Height;
            if (aspectRatioSrc > aspectRatioDst)
            {
                whiteSpaceTop = panel1.Height - (int)(panel1.Width / aspectRatioSrc);
                whiteSpaceTop >>= 1;
            }
            else
            {
                whiteSpaceLeft = panel1.Width - (int)(panel1.Height * aspectRatioSrc);
                whiteSpaceLeft >>= 1;
            }
            Rectangle rSource = new Rectangle(0, 0, bmp.Width, bmp.Height);
            Rectangle rDestination = new Rectangle(whiteSpaceLeft, whiteSpaceTop, panel1.Width - (whiteSpaceLeft << 1), panel1.Height - (whiteSpaceTop << 1));
            grp.DrawImage(bmp, rDestination, rSource, GraphicsUnit.Pixel);

            //grp.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
            //grp.DrawImage(bmp, 0, 0, panel1.Width, panel1.Height);
            grp.Dispose();

        }


        //uklid
        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            bmp.Dispose();
            graphics.Dispose();
            mujTimer.Stop();
            mujTimer.Dispose();
        }


        //nacteni obrazku
        private void button20_Click(object sender, EventArgs e)
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
            panel1.CreateGraphics().Clear(Color.White);
            vykresliDoPanelu();
        }


        //zmena barvy
        private void button21_Click(object sender, EventArgs e)
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


        private void definujBarvy() {
           
            panel2.BackColor = barvaKresleni;
            stetec = new SolidBrush(barvaKresleni);
            pero = new Pen(barvaKresleni, tloustka);    
        }

        private void numericUpDown2_ValueChanged(object sender, EventArgs e)
        {            
            tloustka = (float)numericUpDown2.Value;
            definujBarvy();
        }

        private void Form1_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (checkBox2.Checked)
            {

                switch (e.KeyChar)
                {
                    case 'w':
                        {
                            if (pozice.Y - velikost.Height < 0) return;
                            kresliElypsu(pozice, Color.White);
                            pozice.Y = pozice.Y - velikost.Height;
                            kresliElypsu(pozice, Color.Red);
                        } break;
                    case 's':
                        {
                            if (pozice.Y + velikost.Height > panel1.Height - velikost.Height) return;
                            kresliElypsu(pozice, Color.White);
                            pozice.Y = pozice.Y + velikost.Height;
                            kresliElypsu(pozice, Color.Red);
                        } break;
                    case 'd':
                        {
                            if (pozice.X + velikost.Width > panel1.Width - velikost.Width) return;
                            kresliElypsu(pozice, Color.White);
                            pozice.X = pozice.X + velikost.Width;
                            kresliElypsu(pozice, Color.Red);
                        } break;
                    case 'a':
                        {
                            if (pozice.X - velikost.Width < 0) return;
                            kresliElypsu(pozice, Color.White);
                            pozice.X = pozice.X - velikost.Width;
                            kresliElypsu(pozice, Color.Red);
                        } break;
                }
            }
            else if (checkBox3.Checked&&!vyhra) {
                switch (e.KeyChar)
                {
                    case 'w':
                        {
                            if (poziceHrace.y < bludiste.height - 1)
                            {
                                if (!bludiste.bunky[poziceHrace.y, poziceHrace.x].top)
                                {
                                    poziceHrace.y += 1;
                                }
                            }
                        } break;
                    case 's':
                        {
                            if (poziceHrace.y > 0)
                            {
                                if (!bludiste.bunky[poziceHrace.y, poziceHrace.x].bottom)
                                {
                                    poziceHrace.y -= 1;
                                }
                            }
                        } break;
                    case 'd':
                        {
                            if (poziceHrace.x < bludiste.width - 1)
                            {
                                if (!bludiste.bunky[poziceHrace.y, poziceHrace.x].right)
                                {
                                    poziceHrace.x += 1;
                                }
                            }
                        } break;
                    case 'a':
                        {
                            if (poziceHrace.x > 0)
                            {
                                if (!bludiste.bunky[poziceHrace.y, poziceHrace.x].left)
                                {
                                    poziceHrace.x -= 1;
                                }
                            }
                        } break;
                    
                }
                bludiste.drawOnto(graphics, new Rectangle(0, 0, bmp.Width, bmp.Height), poziceHrace, cil);
                vykresliDoPanelu();
                if (poziceHrace.x == cil.x && poziceHrace.y == cil.y && !vyhra)
                {
                    MessageBox.Show("vyhráli jste", "Výhra", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    vyhra = true;
                }
            
            }
            
        }

        public void kresliElypsu(Point poz, Color c) {
            Brush bru = new SolidBrush(c);
            graphics.FillEllipse(bru, poz.X,poz.Y, velikost.Width,velikost.Height);
        }


        //generuj bludiste
        private void button22_Click(object sender, EventArgs e)
        {
            int sirka = 30;
            int vyska = 30;
            poziceHrace = new Position(0, 0);
            bludiste = new Bludiste(sirka, vyska);
            bludiste.build();
            cil = new Position(bludiste.rng.Next(0, sirka), bludiste.rng.Next(0, vyska));
            bludiste.drawOnto(graphics, new Rectangle(0, 0, bmp.Width, bmp.Height), poziceHrace,cil);
            vykresliDoPanelu();    

        }

        private void button23_Click(object sender, EventArgs e)
        {
            gumujPanelDoBela();
            int sirka = 80;
            Point pocatek = new Point(0, 0);
            Point stred = new Point(panel1.Width / 2 - sirka / 2, panel1.Height / 2 - sirka / 2);
            Matrix mStred = new Matrix(1, 0, 0, 1, stred.X, stred.Y);

            Point A = new Point(pocatek.X, pocatek.Y);
            Point B = new Point(pocatek.X, pocatek.Y + sirka);
            Point C = new Point(pocatek.X + sirka, pocatek.Y + sirka);
            Point D = new Point(pocatek.X + sirka, pocatek.Y);
            Point[] pole = new Point[4] { A, B, C, D };
            Point[] pole2 = new Point[4] { A, B, C, D }; 
            pero = new Pen(Color.Blue);
            
            mStred.TransformPoints(pole2);
            graphics.DrawLine(pero, pole2[0], pole2[2]);
            graphics.DrawPolygon(pero, pole2);
           

            float a00 = (float)Convert.ToDouble(textBox1.Text);
            float a01 = (float)Convert.ToDouble(textBox2.Text);
            float a10 = (float)Convert.ToDouble(textBox4.Text);
            float a11 = (float)Convert.ToDouble(textBox5.Text);
            float a20 = (float)Convert.ToDouble(textBox7.Text);
            float a21 = (float)Convert.ToDouble(textBox8.Text);

                       

            Matrix m = new Matrix(a00, a01, a10, a11, a20, a21);

            

            m.TransformPoints(pole);
            mStred.TransformPoints(pole);
            pero = new Pen(Color.Red);
            graphics.DrawLine(pero, pole[0], pole[2]);
            graphics.DrawPolygon(pero, pole);
            vykresliDoPanelu();

        }


        private double DegreeToRadian(double angle)
        {
            return  Math.PI * angle / 180.0;
        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e)
        {
            textBox1.Text = "1";
            textBox2.Text = "0";
            textBox4.Text = "0";
            textBox5.Text = "1";
            textBox7.Text = "1";
            textBox8.Text = "1";
            
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e)
        {
            textBox1.Text = "1";
            textBox2.Text = "1";
            textBox4.Text = "1";
            textBox5.Text = "1";
            textBox7.Text = "0";
            textBox8.Text = "0";
        }

        private void radioButton3_CheckedChanged(object sender, EventArgs e)
        {
            textBox1.Text = "2";
            textBox2.Text = "0";
            textBox4.Text = "0";
            textBox5.Text = "2";
            textBox7.Text = "0";
            textBox8.Text = "0";
        }

        private void radioButton4_CheckedChanged(object sender, EventArgs e)
        {
            textBox1.Text = "1";
            textBox2.Text = "0";
            textBox4.Text = "0";
            textBox5.Text = "-1";
            textBox7.Text = "0";
            textBox8.Text = "0";
        }

        private void radioButton5_CheckedChanged(object sender, EventArgs e)
        {
            textBox1.Text = "1";
            textBox2.Text = "2";
            textBox4.Text = "0";
            textBox5.Text = "1";
            textBox7.Text = "0";
            textBox8.Text = "0";
        }

        private void button24_Click(object sender, EventArgs e)
        {
            radioButton2.Checked=true;
            float tmp = (float)Convert.ToDouble(textBox10.Text);
            float kk = (float)DegreeToRadian(tmp);
            float sin = (float)Math.Sin(kk);
            float cos = (float)Math.Cos(kk);
            textBox11.Text = "sin = " + sin.ToString();
            textBox12.Text = "cos = " + cos.ToString();

            textBox1.Text = cos.ToString();
            textBox2.Text = sin.ToString();
            textBox4.Text = (-sin).ToString();
            textBox5.Text = cos.ToString();
            textBox7.Text = "0";
            textBox8.Text = "0";

        }

        private void button25_Click(object sender, EventArgs e)
        {
            graphics.Clear(Color.White);
            pero = new Pen(Color.Red, 1);
            drawKoch((int)numericUpDown3.Value, new PointF((float)bmp.Width, bmp.Height * 3.0f / 4.0f), new PointF(0.0f, bmp.Height * 3.0f / 4.0f));
            //trojuh
            //drawKoch((int)numericUpDown3.Value, new PointF((float)bmp.Width / 2, bmp.Height * 1.0f / 4.0f), new PointF(bmp.Width * 1.0f / 4.0f, bmp.Height * 3.0f / 4.0f));
            //drawKoch((int)numericUpDown3.Value, new PointF(bmp.Width * 1.0f / 4.0f, bmp.Height * 3.0f / 4.0f), new PointF(bmp.Width * 3.0f / 4.0f, bmp.Height * 3.0f / 4.0f));
            //drawKoch((int)numericUpDown3.Value, new PointF(bmp.Width * 3.0f / 4.0f, bmp.Height * 3.0f / 4.0f), new PointF((float)bmp.Width / 2, bmp.Height * 1.0f / 4.0f));
            //ctverec
            //drawKoch((int)numericUpDown3.Value, new PointF((float)bmp.Width * 1 / 4, bmp.Height * 1.0f / 4.0f), new PointF((float)bmp.Width * 3 / 4, bmp.Height * 1.0f / 4.0f));
            //drawKoch((int)numericUpDown3.Value, new PointF((float)bmp.Width * 3 / 4, bmp.Height * 1.0f / 4.0f), new PointF((float)bmp.Width * 3 / 4, bmp.Height * 3.0f / 4.0f));
            //drawKoch((int)numericUpDown3.Value, new PointF((float)bmp.Width * 3 / 4, bmp.Height * 3.0f / 4.0f), new PointF((float)bmp.Width * 1 / 4, bmp.Height * 3.0f / 4.0f));
            //drawKoch((int)numericUpDown3.Value, new PointF((float)bmp.Width * 1 / 4, bmp.Height * 3.0f / 4.0f), new PointF((float)bmp.Width * 1 / 4, bmp.Height * 1.0f / 4.0f));

            vykresliDoPanelu();
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void drawKoch(int level, PointF a, PointF b)
        {
            PointF na = a;
            PointF nb = lerp(a, b, (1.0f / 3.0f));
            PointF ov = new PointF(-nb.Y + na.Y, nb.X - na.X); //kolmy vektor
            PointF nc = lerp(a, b, 0.5f);
            nc.X += ov.X * (float)Math.Sqrt(3.0) * 0.5f;
            nc.Y += ov.Y * (float)Math.Sqrt(3.0) * 0.5f;
            PointF nd = lerp(a, b, (2.0f / 3.0f));
            PointF ne = b;
            if (level <= 0)
            {
                graphics.DrawLine(pero, na, nb);
                graphics.DrawLine(pero, nb, nc);
                graphics.DrawLine(pero, nc, nd);
                graphics.DrawLine(pero, nd, ne);
                return;
            }
            drawKoch(level - 1, na, nb);
            drawKoch(level - 1, nb, nc);
            drawKoch(level - 1, nc, nd);
            drawKoch(level - 1, nd, ne);
        }

        PointF lerp(PointF a, PointF b, float coef)
        {
            return new PointF(a.X + coef * (b.X - a.X), a.Y + coef * (b.Y - a.Y));
        }

       
        
        


    }
}
