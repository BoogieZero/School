using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Transformations {
    public partial class Form1 : Form {

        Bitmap bmp;
        Graphics graphics;
        Pen penB = new Pen(new SolidBrush(Color.Blue), 3);
        Pen penR = new Pen(new SolidBrush(Color.Red), 2);

        Pen axis = new Pen(new SolidBrush(Color.Green));
        Matrix matrixT;
        PointF[] points;
        double degToRad;

        public Form1() {
            InitializeComponent();
            bmp = new Bitmap(drawBoard.Width, drawBoard.Height);
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(Color.White);
            matrixT = new Matrix(2, 0, 0, 2, 0, 0);
            degToRad = Math.PI / 180;
            
            //sets origin for coordinates to middle of drawingBoard
            graphics.TranslateTransform(drawBoard.Width / 2, drawBoard.Height / 2);
            initializePoints();
            rbTranslation_CheckedChanged(null, null);
            
        }

        /// <summary>
        /// Sets points to their default locations.
        /// </summary>
        private void initializePoints() {
            points = new PointF[4];
            points[0].X = 0;
            points[0].Y = 0;

            points[1].X = 50;
            points[1].Y = 0;

            points[2].X = 50;
            points[2].Y = 50;

            points[3].X = 0;
            points[3].Y = 50;
        }

        /// <summary>
        /// Prepares axis lines and resets points to their default positions if stacking transformations isn't alowed.
        /// </summary>
        private void resetBoard() {
            graphics.DrawLine(axis, -100, 0, +100, 0);
            graphics.DrawLine(axis, 0, -100, 0, +100);

            if(!chbStackTransform.Checked) {
                initializePoints();
            }
        }

        /// <summary>
        /// Draws bitmap into odrawboard
        /// </summary>
        private void draw() {
            drawBoard_Paint(this, null);
        }

        /// <summary>
        /// Clears drawing board.
        /// </summary>
        private void clear() {
            graphics.Clear(Color.White);
        }

        /// <summary>
        /// Draws bitmap into drawBoard
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void drawBoard_Paint(object sender, PaintEventArgs e) {
            if(bmp == null) return;
            Graphics grp = drawBoard.CreateGraphics();
            int whiteSpaceTop;
            int whiteSpaceLeft;

            double aspectRatioSrc = (double)bmp.Width / bmp.Height;
            double aspectRatioDst = (double)drawBoard.Width / drawBoard.Height;
            if(aspectRatioSrc > aspectRatioDst) {
                whiteSpaceTop = drawBoard.Height - (int)(drawBoard.Width / aspectRatioSrc);
                whiteSpaceTop >>= 1;
                whiteSpaceLeft = 0;
            } else {
                whiteSpaceLeft = drawBoard.Width - (int)(drawBoard.Height * aspectRatioSrc);
                whiteSpaceLeft >>= 1;
                whiteSpaceTop = 0;
            }
            Rectangle rSource = new Rectangle(0, 0, bmp.Width, bmp.Height);
            Rectangle rDestination = new Rectangle(whiteSpaceLeft, whiteSpaceTop, drawBoard.Width - (whiteSpaceLeft << 1), drawBoard.Height - (whiteSpaceTop << 1));
            grp.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
            grp.DrawImage(bmp, rDestination, rSource, GraphicsUnit.Pixel);
            grp.Dispose();
        }

        /// <summary>
        /// Redraw image after change of scale
        /// </summary>
        /// <param name="sender"></param>
        private void drawBoard_Resize(object sender, EventArgs e) {
            drawBoard.CreateGraphics().Clear(Color.White);
            drawBoard_Paint(null, null);
        }

        /// <summary>
        /// Draws square given by points.
        /// </summary>
        /// <param name="pen">pen for drawing lines</param>
        private void drawPoints(Pen pen) {
            graphics.DrawLine(pen, points[0], points[1]);
            graphics.DrawLine(pen, points[1], points[2]);
            graphics.DrawLine(pen, points[2], points[3]);
            graphics.DrawLine(pen, points[3], points[0]);
            graphics.DrawLine(pen, points[0], points[2]);   //diagonal
        }

        /// <summary>
        /// Loads matrix from GUI in text form and parse it into float matrix.
        /// </summary>
        /// <returns>true if parsing was succesfull</returns>
        private Boolean loadMatrixT() {
            float   m11,    m12,
                    m21,    m22,
                    dx,     dy;

            if(!float.TryParse(textBox1.Text, out m11)) return false;
            if(!float.TryParse(textBox2.Text, out m12)) return false;
            if(!float.TryParse(textBox4.Text, out m21)) return false;
            if(!float.TryParse(textBox5.Text, out m22)) return false;
            if(!float.TryParse(textBox7.Text, out dx)) return false;
            if(!float.TryParse(textBox8.Text, out dy)) return false;

            matrixT = new Matrix(m11, m12, m21, m22, dx, dy);
            return true;
        }

        /// <summary>
        /// Done  affine transformations on points by loaded matrixT.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btTransform_Click(object sender, EventArgs e) {
            clear();
            resetBoard();
            if(!loadMatrixT()) return;

            if(chbManual.Checked) {
                manual();
            } else {
                drawByMatrix();
            }
            
        }

        /// <summary>
        /// Draws original points and points after tranformation by matrixT
        /// </summary>
        private void drawByMatrix() {
            drawPoints(penB);
            matrixT.TransformPoints(points);
            drawPoints(penR);
            draw();
        }

        /// <summary>
        /// Locks unnecesary elements in gui and alows change of the relevant ones.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void rbScale_CheckedChanged(object sender, EventArgs e) {
            if(!rbScale.Checked) return;

            textBox1.Text = "1";
            textBox1.Enabled = true;
            textBox2.Text = "0";
            textBox2.Enabled = false;
            textBox4.Text = "0";
            textBox4.Enabled = false;
            textBox5.Text = "1";
            textBox5.Enabled = true;
            textBox7.Text = "0";
            textBox7.Enabled = false;
            textBox8.Text = "0";
            textBox8.Enabled = false;
            btConvert.Enabled = false;
        }

        /// <summary>
        /// Locks unnecesary elements in gui and alows change of the relevant ones.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void rbTranslation_CheckedChanged(object sender, EventArgs e) {
            if(!rbTranslation.Checked) return;

            textBox1.Text = "1";
            textBox1.Enabled = false;
            textBox2.Text = "0";
            textBox2.Enabled = false;
            textBox4.Text = "0";
            textBox4.Enabled = false;
            textBox5.Text = "1";
            textBox5.Enabled = false;
            textBox7.Text = "0";
            textBox7.Enabled = true;
            textBox8.Text = "0";
            textBox8.Enabled = true;
            btConvert.Enabled = false;
        }

        /// <summary>
        /// Locks unnecesary elements in gui and alows change of the relevant ones.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void rbMirror_CheckedChanged(object sender, EventArgs e) {
            if(!rbMirror.Checked) return;

            textBox1.Text = "1";
            textBox1.Enabled = true;
            textBox2.Text = "0";
            textBox2.Enabled = false;
            textBox4.Text = "0";
            textBox4.Enabled = false;
            textBox5.Text = "-1";
            textBox5.Enabled = true;
            textBox7.Text = "0";
            textBox7.Enabled = false;
            textBox8.Text = "0";
            textBox8.Enabled = false;
            btConvert.Enabled = false;
        }

        /// <summary>
        /// Locks unnecesary elements in gui and alows change of the relevant ones.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void rbBevel_CheckedChanged(object sender, EventArgs e) {
            if(!rbBevel.Checked) return;

            textBox1.Text = "1";
            textBox1.Enabled = false;
            textBox2.Text = "2";
            textBox2.Enabled = true;
            textBox4.Text = "0";
            textBox4.Enabled = true;
            textBox5.Text = "1";
            textBox5.Enabled = false;
            textBox7.Text = "0";
            textBox7.Enabled = false;
            textBox8.Text = "0";
            textBox8.Enabled = false;
            btConvert.Enabled = false;
        }

        /// <summary>
        /// Locks unnecesary elements in gui and alows change of the relevant ones.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void rbRotation_CheckedChanged(object sender, EventArgs e) {
            if(!rbRotation.Checked) return;

            textBox1.Text = "1";
            textBox1.Enabled = true;
            textBox2.Text = "0";
            textBox2.Enabled = true;
            textBox4.Text = "0";
            textBox4.Enabled = true;
            textBox5.Text = "1";
            textBox5.Enabled = true;
            textBox7.Text = "0";
            textBox7.Enabled = false;
            textBox8.Text = "0";
            textBox8.Enabled = false;
            btConvert.Enabled = true;
        }

        /// <summary>
        /// Converts input in degrees into radians and calculates sin and cos value.
        /// If Rotation mode is selected apropriate values will be filled into matrix.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btConvert_Click(object sender, EventArgs e) {
            double dg;
            if(!double.TryParse(txDegree.Text, out dg)) return;
            dg = dg * degToRad;
            
            double sin = Math.Sin(dg);
            double cos = Math.Cos(dg);

            txSin.Text = "Sin = " + sin;
            txCos.Text = "Cos = " + cos;
            if(rbRotation.Checked) {
                textBox1.Text = "" + cos;
                textBox2.Text = "" + sin;
                textBox4.Text = "" + (-sin);
                textBox5.Text = "" + cos;
            }
        }

        /// <summary>
        /// Runs methods for manually computed values for points.
        /// </summary>
        private void manual() {
            if(rbScale.Checked)         drawScaleManual();
            if(rbTranslation.Checked)   drawTranslateManual();
            if(rbMirror.Checked)        drawScaleManual();             //scale can be used for mirroring by negative input values
            if(rbBevel.Checked)         drawBevelManual();
            if(rbRotation.Checked)      drawRotationManual();
        }

        /// <summary>
        /// Calculates scaled positions for points. Method ignores Values in matrix which are irelevant for simple scale function.
        /// X = X * M11 + dx
        /// Y = Y * M22 + dy
        /// dx = 0
        /// dy = 0
        /// </summary>
        private void drawScaleManual() {
            drawPoints(penB);
            for(int i = 0; i < points.Length; i++) {
                points[i].X = points[i].X * matrixT.Elements[0];
                points[i].Y = points[i].Y * matrixT.Elements[3];
            }
            drawPoints(penR);
            draw();
        }

        /// <summary>
        /// Calculates translated positions for points. Method ignores Values in matrix which are irelevant for simple translate function.
        /// X = X + (dx * z)
        /// Y = Y + (dy * z)
        ///     z = 1 (M33)
        ///     dx = M13 , before transposition
        ///     dy = M23 , before transposition
        /// </summary>
        private void drawTranslateManual() {
            drawPoints(penB);
            for(int i = 0; i < points.Length; i++) {
                points[i].X = points[i].X + matrixT.Elements[0];
                points[i].Y = points[i].Y + matrixT.Elements[3];
            }
            drawPoints(penR);
            draw();
        }

        /// <summary>
        /// Calculates beveled positions for points. Method ignores Values in matrix which are irelevant for simple bevel function.
        /// X = X + (M12 * Y) + (dx * z), before transposition
        /// Y = Y + (M21 * X) + (dy * z), before transposition
        /// dx = 0
        /// dy = 0
        /// </summary>
        private void drawBevelManual() {
            drawPoints(penB);
            float x;
            float y;

            for(int i = 0; i < points.Length; i++) {
                x = points[i].X;
                y = points[i].Y;
                points[i].X = x + matrixT.Elements[2] * y;
                points[i].Y = y + matrixT.Elements[1] * x;
            }

            drawPoints(penR);
            draw();
        }

        /// <summary>
        /// Calculates rotated positions for points. Method ignores Values in matrix which are irelevant for simple rotation function.
        /// X = ((Cos a) * X) + ((-Sin a) * Y) + (dx * z), before transposition
        /// Y = ((Sin a) * X) + (( Cos a) * Y) + (dy * z), before transposition
        /// a = desiredd rotation angle
        /// dx = 0
        /// dy = 0
        /// 
        /// Functions are already calculated which means:
        /// X = M11 * X + M12 * Y
        /// Y = M21 * X + M22 * Y
        /// </summary>
        private void drawRotationManual() {
            drawPoints(penB);
            float x;
            float y;

            for(int i = 0; i < points.Length; i++) {
                x = points[i].X;
                y = points[i].Y;
                points[i].X = matrixT.Elements[0] * x + matrixT.Elements[2] * y;
                points[i].Y = matrixT.Elements[1] * x + matrixT.Elements[3] * y;
            }

            drawPoints(penR);
            draw();
        }
    }
}
