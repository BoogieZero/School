using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SomeCurves {
    public partial class Form1 : Form {

        public Graphics graphics;
        public Bitmap bmp;
        public Timer myTimer;

        Pen pen = new Pen(Color.Black);
        Brush brush = new SolidBrush(Color.Black);
        float rxM = 80;
        float ryM = 30;
        float rx = 5;
        float ry = 5;
        int cloudShift = 150;

        float posunX;
        float posunY;
        double shiftI = 0;
        double i = 0.0;
        double isn = 0.0;
        double eI = 0.0;
        double j = 0.0;
        float tl = 20f;
        int xXL = 0;
        int yYL = 0;
        int xXStem = 0;
        int yYStem = 0;
        int xXSn = 0;
        int yYSn = 0;

        private void initializeTimer() {
            myTimer = new Timer();
            myTimer.Enabled = false;
            myTimer.Interval = 1;
            myTimer.Tick += new EventHandler(drawPicture);
        }

        private void drawPicture(object sender, EventArgs e) {
            int xX = 0;
            int yY = 0;
            //int xXM = 0;
            //int yYM = 0;
            float posunXCloud = bmp.Width / 7;
            float posunYCloud = bmp.Height / 4;

            float posunXSun = bmp.Width / 5;
            float posunYSun = -bmp.Height / 10;

            //Clouds + Sun
            if(i < 360) {
                //Clouds
                double angleM = i * System.Math.PI / 180;
                double angle = j * System.Math.PI / 180;

                int xXM = (int)(posunXCloud + rxM * System.Math.Cos(angleM));
                int yYM = (int)(posunYCloud + ryM * System.Math.Sin(angleM));
                if(xXM < bmp.Width && xXM > 0 && yYM < bmp.Height && yYM > 0) {
                    //movement line
                    //graphics.FillEllipse(brush, xXM, yYM, 5, 5);
                }

                xX = (int)(xXM + rx * System.Math.Cos(angle));
                yY = (int)(yYM + ry * System.Math.Sin(angle));
                if(xX < bmp.Width && xX > 0 && yY < bmp.Height && yY > 0) {
                    graphics.FillEllipse(new SolidBrush(Color.Blue), xX, yY, 5, 5);
                    graphics.FillEllipse(new SolidBrush(Color.Blue), xX + cloudShift, yY + cloudShift/2, 5, 5);
                    graphics.FillEllipse(new SolidBrush(Color.Blue), xX + cloudShift*2, yY - cloudShift/2 / 2, 5, 5);
                }

                //Sun
                float r1 = 60f;
                float r2 = 5f;
                double angleS = i * System.Math.PI / 180;

                //Inner ring
                int xXSI = (int)(posunXSun + 100+ (r1-15) * System.Math.Cos(angleM));
                int yYSI = (int)(posunYSun + 100+ (r1-15) * System.Math.Sin(angleM));

                //Outer ring
                int xXS = (int)(posunXSun + 100 + (r1 - r2) * Math.Cos(angleS) + r2 * Math.Cos(((r1 - r2) / r2) * angleS) );
                int yYS = (int)(posunYSun + 100 + (r1 - r2) * Math.Sin(angleS) - r2 * Math.Sin(((r1 - r2) / r2) * angleS) );
                if(xXS < bmp.Width && xXS > 0 && yYS < bmp.Height && yYS > 0) {
                    graphics.FillEllipse(new SolidBrush(Color.Yellow), xXS, yYS, 5, 5);
                    graphics.FillEllipse(new SolidBrush(Color.Yellow), xXSI, yYSI, 5, 5);
                }

                float posunXLeaf = bmp.Width*3 / 4;
                float posunYLeaf = bmp.Height*2 / 3;
                //leafs
                if(i < 360) {
                    double i2 = i - 18;
                    double angleL = 4*i2 * System.Math.PI / 180;
                    xXL = (int)(posunXLeaf + rx *5* (System.Math.Cos(angleL) + System.Math.Sin(angleL / 4)));
                    yYL = (int)(posunYLeaf + ry *5* (System.Math.Sin(angleL) + System.Math.Cos(angleL / 4)));
                    if(xXL < bmp.Width && xXL > 0 && yYL < bmp.Height && yYL > 0) {
                        graphics.FillEllipse(new SolidBrush(Color.Green), xXL, yYL, 5, 5);
                    }
                    //draw();
                    //i += 2.0;
                    tl -= 20f / 200f;

                }
                //graphics.DrawRectangle(pen, xX, yY, 20, 20);

                draw();
                i += 1.0;
                j += 10.0;

            } else if(i>=360 && i<720) {
                //graphics.FillRectangle(new SolidBrush(Color.Black), xXL, yYL, 20, 20);
                double angleStem = i * System.Math.PI / 180;
                
                if(i < 540) {
                    xXStem = -100+(int)(xXL +100* System.Math.Cos(angleStem));
                    yYStem = (int)(yYL +200* System.Math.Sin(angleStem));
                    if(xXStem < bmp.Width && xXStem > 0 && yYStem < bmp.Height && yYStem > 0) {
                        graphics.FillEllipse(new SolidBrush(Color.Green), xXStem, yYStem, 5, 5);
                    }
                } else {
                    double i2 = i + 395;
                    double angleL = 6 * i2 * System.Math.PI / 180;
                    xXL = (int)(xXStem + rx * 5 * (System.Math.Cos(angleL) + System.Math.Sin(angleL / 3)));
                    yYL = (int)(yYStem + ry * 5 * (System.Math.Sin(angleL) + System.Math.Cos(angleL / 3)));
                    
                    if(xXL < bmp.Width && xXL > 0 && yYL < bmp.Height && yYL > 0) {
                        graphics.FillEllipse(new SolidBrush(Color.Green), xXL, yYL, 5, 5);
                    }
                    
                    
                }
                
                //snail
                float posunXSn = bmp.Width  / 5;
                float posunYSn = bmp.Height * 8 / 9;

                double angleSn = 2*i * System.Math.PI / 180;
                xXSn = (int)(posunXSn + (3 + isn) * Math.Cos(angleSn));
                yYSn = (int)(posunYSn + (3 + isn) * Math.Sin(angleSn));
                if(xXSn < bmp.Width && xXSn > 0 && yYSn < bmp.Height && yYSn > 0) {
                    graphics.FillEllipse(new SolidBrush(Color.Black), xXSn, yYSn, 5, 5);
                }
                isn += 0.1 + isn/300;

               

                i += 1.0;
                draw();
            } else if(i >= 720 && i < 1020) {
                double i2 = i - 70;
                double angleSn = i2 * System.Math.PI / 180;
                int xXB = -40+(int)(xXSn + 125 * System.Math.Cos(angleSn));
                int yYB = 22+(int)(yYSn + 22 * System.Math.Sin(angleSn));
                if(xXB < bmp.Width && xXB > 0 && yYB < bmp.Height && yYB > 0) {
                    graphics.FillEllipse(new SolidBrush(Color.Black), xXB, yYB, 5, 5);
                }
                i += 1.0;
                draw();
            } else if(i >= 1020 && i < 1175) {
                float posunXEye = bmp.Width / 5;
                float posunYEye = bmp.Height * 8 / 9;

                double i2 = i + 200;
                double angleE = i2 * System.Math.PI / 180;
                
                int xXE = (int)(posunXEye+150 + (50- eI) * System.Math.Cos(angleE));
                int yYE = -40+(int)(posunYEye-eI/3 + (80- eI) * System.Math.Sin(angleE));

                if(xXE < bmp.Width && xXE > 0 && yYE < bmp.Height && yYE > 0) {
                    graphics.FillEllipse(new SolidBrush(Color.Black), xXE, yYE, 5, 5);
                    graphics.FillEllipse(new SolidBrush(Color.Black), xXE+15, yYE+5, 5, 5);
                }
                eI += 0.5 + eI/300;
                i += 1.0;
                draw();
            } else {
                i = 0.0;
                j = 0.0;
                tl = 0f;
                myTimer.Stop();
            }

            
            

            
            
        }

        public Form1() {
            InitializeComponent();
            bmp = new Bitmap(drawBoard.Width, drawBoard.Height);
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(Color.White);
            posunX = bmp.Width / 3;
            posunY = bmp.Height / 3;
            i = shiftI;
            initializeTimer();
        }

        private void Clear() {
            graphics.Clear(Color.White);
        }

        private void draw() {
            drawBoard_Paint(this, null);
        }

        private void drawBoard_Paint(object sender, PaintEventArgs e) {
            if(bmp == null) return;
            Graphics grp = drawBoard.CreateGraphics();
            grp.DrawImage(bmp, 0, 0, drawBoard.Width, drawBoard.Height);
            grp.Dispose();
        }

        private void btDraw_Click(object sender, EventArgs e) {
            myTimer.Start();
        }

        private void btClear_Click(object sender, EventArgs e) {
            Clear();
            draw();
        }
    }
}
