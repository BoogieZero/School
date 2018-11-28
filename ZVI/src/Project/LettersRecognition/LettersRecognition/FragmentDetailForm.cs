using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LettersRecognition {
    /// <summary>
    /// Window for detail informations about specific fragment.
    /// </summary>
    public partial class FragmentDetailForm : Form {
        /// <summary>
        /// Color for a edge.
        /// </summary>
        private Color EDGE_COLOR = Color.Red;

        /// <summary>
        /// Graphic context build above bitmap in GUI
        /// </summary>
        public Graphics graphics;

        /// <summary>
        /// Source fragment for which is window opened.
        /// </summary>
        private Image.Fragment src;

        /// <summary>
        /// Source bitmap for display.
        /// </summary>
        private Bitmap srcBmp;

        /// <summary>
        /// Bitmap used for displaying and drawing
        /// Bitmap is created above panel_1
        /// </summary>
        public Bitmap bmp;

        #region Aspect Rations

        /// <summary>
        /// White space from top of the window to displaying panel
        /// for purposes of preservation apect rations of image
        /// </summary>
        int whiteSpaceLeft = 0;

        /// <summary>
        /// White space from left of the window to displaying panel
        /// for purposes of preservation apect rations of image
        /// </summary>
        int whiteSpaceTop = 0;

        #endregion Aspect Rations

        #region Pens

        /// <summary>
        /// Green pen
        /// </summary>
        Pen penG = new Pen(Color.Green, 1.0f);

        /// <summary>
        /// Blue pen
        /// </summary>
        Pen penB = new Pen(Color.Blue, 0.5f);

        /// <summary>
        /// Orange pen
        /// </summary>
        Pen penO = new Pen(Color.Orange, 1.0f);

        #endregion Pens

        /// <summary>
        /// Creates new instance of this window for given fragment.
        /// </summary>
        /// <param name="fragment">source fragment</param>
        public FragmentDetailForm(Image.Fragment fragment) {
            InitializeComponent();

            src = fragment;
            bmp = getOriginalCut();
            srcBmp = getOriginalCut();

            fillInfo();

            graphics = Graphics.FromImage(bmp);
            draw();
        }

        /// <summary>
        /// Fills info box from description.
        /// </summary>
        private void fillInfo() {
            double volByMMRec = src.description.volume/(src.description.width*src.description.height);
            txInfo.Text =
                "Result: " + src.description.result +
                "\r\nCircumference: " + src.edge.Count +
                "\r\nVolume: " + String.Format("{0:0} ({1:0.0%})", src.description.volume, volByMMRec) +
                "\r\nLongitude: " + String.Format("{0:0.00}", src.description.longitude) +
                "\r\nOptimal rotation: " + String.Format("{0:0°}", src.description.angle) +
                "\r\nCenter of gravity: " + String.Format("{0:0%}X {1:0%}Y", src.description.minRecCoGX, src.description.minRecCoGY) +
                "\r\nHoles: " + src.description.numOfHoles +
                "\r\n" + src.description.distanceReport
                ;
        }

        /// <summary>
        /// Draws content of bitmap into panel 
        /// bitmap works like buffer
        /// </summary>
        private void draw() {
            panel1.CreateGraphics().Clear(Color.White);     //Artifacts around the image
            panel1_Paint(this, null);
        }

        /// <summary>
        /// Draws content of bitmap into panel 
        /// bitmap works like buffer
        /// whitespaces are reated around image for alowing scaling without loss of aspect ration
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void panel1_Paint(object sender, PaintEventArgs e) {
            if(bmp == null) return;
            Graphics grp = panel1.CreateGraphics();

            double aspectRatioSrc = (double)bmp.Width / bmp.Height;
            double aspectRatioDst = (double)panel1.Width / panel1.Height;
            if(aspectRatioSrc > aspectRatioDst) {
                whiteSpaceTop = panel1.Height - (int)(panel1.Width / aspectRatioSrc);
                whiteSpaceTop >>= 1;
                whiteSpaceLeft = 0;
            } else {
                whiteSpaceLeft = panel1.Width - (int)(panel1.Height * aspectRatioSrc);
                whiteSpaceLeft >>= 1;
                whiteSpaceTop = 0;
            }
            Rectangle rSource = new Rectangle(0, 0, bmp.Width, bmp.Height);
            Rectangle rDestination = new Rectangle(whiteSpaceLeft, whiteSpaceTop, panel1.Width - (whiteSpaceLeft << 1), panel1.Height - (whiteSpaceTop << 1));
            grp.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
            grp.DrawImage(bmp, rDestination, rSource, GraphicsUnit.Pixel);

            grp.Dispose();
        }

        /// <summary>
        /// Provides clearing of panel after resizing
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void panel1_Resize(object sender, EventArgs e) {
            panel1.CreateGraphics().Clear(Color.White);
            panel1_Paint(null, null);
        }

        /// <summary>
        /// Clears bitmap
        /// </summary>
        private void clear() {
            graphics.Clear(Color.White);
        }

        /// <summary>
        /// Draws given bitmap into panel.
        /// If the bitmap uses pixel format 8bppIndexed the bitmap is converted into 24bppRgb.
        /// </summary>
        /// <param name="bmp"></param>
        private void drawBitmap(Bitmap bmp) {

            if(bmp.PixelFormat == PixelFormat.Format8bppIndexed) {
                this.bmp = bmp.Clone(
                    new Rectangle(0, 0, bmp.Width, bmp.Height),
                    PixelFormat.Format24bppRgb
                    );
            } else {
                this.bmp = bmp;
            }
            graphics = Graphics.FromImage(this.bmp);
            draw();
        }

        /// <summary>
        /// Returns clone of original image with dimensions and possition by source fragment of this window. 
        /// </summary>
        /// <returns></returns>
        private Bitmap getOriginalCut() {
            return
                Program.form.srcBmp.Clone(
                    new Rectangle(
                        src.minXdet, src.minYdet,     //origin
                        src.maxXdet - src.minXdet + 1,    //width
                        src.maxYdet - src.minYdet + 1),   //height
                    PixelFormat.Format24bppRgb
                    );

        }

        /// <summary>
        /// Draws original bitmap for source fragment.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btOriginal_Click(object sender, EventArgs e) {
            drawBitmap(srcBmp);
            btDrawMinRectangle.Enabled = true;
        }

        /// <summary>
        /// Draws minimal rectangle of source fragment to panel.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btDrawMinRectangle_Click(object sender, EventArgs e) {
            drawMinRectangle();
        }

        /// <summary>
        /// Draws minimal rectangle of source fragment to panel.
        /// </summary>
        private void drawMinRectangle() {
            PointF[] pts = {
                new PointF(src.description.minX,src.description.minY),
                new PointF(src.description.maxX,src.description.minY),
                new PointF(src.description.maxX,src.description.maxY),
                new PointF(src.description.minX,src.description.maxY),
                new PointF(src.description.minX,src.description.minY)
            };


            Matrix m = new Matrix();
            m.RotateAt(-src.description.angle, src.description.rotOrigin);
            m.TransformPoints(pts);

            graphics.TranslateTransform(-src.minXdet, -src.minYdet);
            graphics.DrawLines(penO, pts);

            draw();
            graphics.ResetTransform();
        }

        /// <summary>
        /// Draws edge points from source fragment into panel.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btShowEdge_Click(object sender, EventArgs e) {
            foreach(Point p in src.edge) {
                bmp.SetPixel(p.X - src.minXdet, p.Y - src.minYdet, EDGE_COLOR);
            }
            draw();


        }

        /// <summary>
        /// Draws cross where center of gravity is for source fragment.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btAvgOrigin_Click(object sender, EventArgs e) {
            Matrix m = new Matrix();
            PointF p = new PointF(src.description.balancePoint.X, src.description.balancePoint.Y);

            graphics.DrawLine(
                penO,
                new PointF(
                    0,
                    p.Y - src.minYdet),
                new PointF(
                    bmp.Width,
                    p.Y - src.minYdet)
                );

            graphics.DrawLine(
                penO,
                new PointF(
                    p.X - src.minXdet,
                    0),
                new PointF(
                    p.X - src.minXdet,
                    bmp.Height)
                );

            draw();
        }
    }
}
