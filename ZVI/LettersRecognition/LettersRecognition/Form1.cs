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
    /// Main GUI window.
    /// </summary>
    public partial class Form1 : Form {

        /// <summary>
        /// Graphic context build above bitmap in GUI
        /// </summary>
        public Graphics graphics;

        /// <summary>
        /// Back up bitmap used for step back function.
        /// </summary>
        public Bitmap backBmp;

        /// <summary>
        /// Bitmap of the source image
        /// </summary>
        public Bitmap srcBmp;
        
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
        Pen penG = new Pen(Color.Green, 1f);

        /// <summary>
        /// Blue pen
        /// </summary>
        Pen penB = new Pen(Color.Blue, 0.5f);

        Pen penR = new Pen(Color.DarkRed, 2.0f);

        /// <summary>
        /// Orange pen
        /// </summary>
        Pen penO = new Pen(Color.Orange, 1.0f);

        #endregion Pens

        /// <summary>
        /// Constructor of GUI
        /// </summary>
        public Form1() {
            InitializeComponent();

            bmp = new Bitmap(128, 128);
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(Color.White);
            draw();
        }

        /// <summary>
        /// Fills list box with discovered fragments from given image.
        /// </summary>
        /// <param name="im">source image</param>
        public void fillListBox(Image im) {
            foreach(Image.Fragment item in im.fragments) {
                lbxObjects.Items.Add(item);
            }
        }

        /// <summary>
        /// Draws content of bitmap into panel 
        /// bitmap works like buffer
        /// </summary>
        private void draw() {
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
            panel1.CreateGraphics().Clear(Color.White); //artifact around the image
            panel1_Paint(null, null);
        }

        /// <summary>
        /// Clears bitmap
        /// </summary>
        private void clear() {
            graphics.Clear(Color.White);
        }

        /// <summary>
        /// Sends bitmap for evaluation
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btEvaluate_Click(object sender, EventArgs e) {
            btEvaluate.Enabled = false;
            btDetail.Enabled = false;
            lbxObjects.Items.Clear();
            Program.evaluateBitmap(srcBmp);
            //clear();
        }

        /// <summary>
        /// Draws rectangle surrounding given fragment.
        /// </summary>
        /// <param name="fragment">source fragment</param>
        private void drawMinMaxRectangle(Image.Fragment fragment) {
            Point a = new Point(fragment.minXdet, fragment.minYdet);
            Point b = new Point(fragment.maxXdet, fragment.minYdet);
            Point c = new Point(fragment.maxXdet, fragment.maxYdet);
            Point d = new Point(fragment.minXdet, fragment.maxYdet);
            graphics.DrawLine(penR, a, b);
            graphics.DrawLine(penR, b, c);
            graphics.DrawLine(penR, c, d);
            graphics.DrawLine(penR, d, a);
            draw();
        }

        /// <summary>
        /// Colors all pixels coresponding to given fragment to blue color.
        /// </summary>
        /// <param name="fragment">source fragment</param>
        private void drawVolume(Image.Fragment fragment) {
            
            int stride;
            byte[]pixels;
            BitmapData bmpData;
            Image.openBitmap24bpp(bmp, out stride, out pixels, out bmpData);

            int index;
            foreach(Point p in fragment.allPoints) {
                index = p.Y * stride + p.X * 3;
                pixels[index] = 200;
                pixels[index + 1] = 50;
                pixels[index + 2] = 50;
            }

            Image.saveCloseBitmap(bmp, bmpData, pixels);
            draw();
            
        }

        /// <summary>
        /// Loads image from directory and draws it into panel.
        /// Loaded image is saved as bitmap srcBmp.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btOpenImg_Click(object sender, EventArgs e) {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.FileName = string.Empty;
            ofd.Filter = "Supported image formats (bmp, gif, jpeg, png, tiff)|*.bmp;*.gif;*.jpg;*.jpeg;*.png;*.tif;*.tiff";
            ofd.Multiselect = false;
            ofd.CheckFileExists = true;

            //Show dialog
            DialogResult dr = ofd.ShowDialog(this);

            if(dr != System.Windows.Forms.DialogResult.OK) {
                return;
            }

            System.Drawing.Image importedImage = null;
            string errMsg = "Unable to load file.";
            try {
                importedImage = System.Drawing.Image.FromFile(ofd.FileName);
            } catch(Exception exc) {
                importedImage = null;
                errMsg = exc.Message;
            }
            if(importedImage == null) {
                MessageBox.Show(errMsg, "Error occurred while reading file.", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            graphics.Dispose();
            bmp.Dispose();
            bmp = (Bitmap)importedImage;
            Console.WriteLine("Source image pixel format: "+bmp.PixelFormat);
            if(bmp.PixelFormat != PixelFormat.Format8bppIndexed) {
                Console.WriteLine("Wrong pixel format! Converted to 8bppIndexed");
                bmp = bmp.Clone(new Rectangle(0, 0, bmp.Width, bmp.Height), System.Drawing.Imaging.PixelFormat.Format8bppIndexed);
            }
            srcBmp = (Bitmap)(importedImage.Clone());
            backBmp = (Bitmap)(importedImage.Clone());
            graphics = Graphics.FromImage(
                bmp.Clone(new Rectangle(0, 0, bmp.Width, bmp.Height), System.Drawing.Imaging.PixelFormat.Format24bppRgb)
                );

            panel1.CreateGraphics().Clear(Color.White);     //Artifacts around the image
            draw();
            lbxObjects.Items.Clear();
            btEvaluate.Enabled = true;
            btDetail.Enabled = false;
            btBack.Enabled = true;
            btErosion.Enabled = true;
            btDilattion.Enabled = true;
            btBlur.Enabled = true;
        }

        /// <summary>
        /// Draws given bitmap into main panel.
        /// If pixel format is 8bppIndexed bitmap is converted into 24bppRgb
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        public void drawBitmap(Bitmap bmp) {
            if(bmp.PixelFormat == PixelFormat.Format8bppIndexed) {
                this.bmp = bmp.Clone(
                    new Rectangle(0, 0, bmp.Width, bmp.Height),
                    System.Drawing.Imaging.PixelFormat.Format24bppRgb
                    );
            }else {
                this.bmp = bmp;
            }
            graphics = Graphics.FromImage(this.bmp);
            draw();
        }

        /// <summary>
        /// Trains classifier from default train folders.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btTrain_Click(object sender, EventArgs e) {
            Program.trainClassifier();
            btTrain.Enabled = false;
            btOpenImg.Enabled = true;
            btLoad.Enabled = false;
        }

        /// <summary>
        /// Loads model for classifier.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btLoad_Click(object sender, EventArgs e) {
            Program.loadClassifier();
            btLoad.Enabled = false;
            btOpenImg.Enabled = true;
            btTrain.Enabled = false;
        }

        /// <summary>
        /// New selection of fragment.
        /// Selected fragment is higligted by red rectangle and it's pixels are colored to blue.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void lbxObjects_SelectedIndexChanged(object sender, EventArgs e) {
            bmp = srcBmp.Clone(new Rectangle(0, 0, bmp.Width, bmp.Height), PixelFormat.Format24bppRgb);
            graphics = Graphics.FromImage(bmp);
            drawMinMaxRectangle((Image.Fragment)lbxObjects.SelectedItem);
            drawVolume((Image.Fragment)lbxObjects.SelectedItem);
            btDetail.Enabled = true;
        }

        /// <summary>
        /// Shows new dialog window with details regarding selected fragment.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btDetail_Click(object sender, EventArgs e) {
            FragmentDetailForm fdf = new FragmentDetailForm((Image.Fragment)lbxObjects.SelectedItem);
            fdf.Show(this);
        }

        /// <summary>
        /// Dilates currently displayed image.
        /// Respectivelly it's source bitmap (srcBitmap)
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btDilattion_Click(object sender, EventArgs e) {
            graphics.Dispose();
            bmp.Dispose();
            backBmp = (Bitmap)srcBmp.Clone();
            srcBmp = Image.dilatation(srcBmp);
            drawBitmap(srcBmp);
            lbxObjects.Items.Clear();
            btEvaluate.Enabled = true;
            btDetail.Enabled = false;
        }

        /// <summary>
        /// Erodes currently displayed image.
        /// Respectivelly it's source bitmap (srcBitmap)
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btErosion_Click(object sender, EventArgs e) {
            graphics.Dispose();
            bmp.Dispose();
            backBmp = (Bitmap)srcBmp.Clone();
            srcBmp = Image.erosion(srcBmp);
            drawBitmap(srcBmp);
            lbxObjects.Items.Clear();
            btEvaluate.Enabled = true;
            btDetail.Enabled = false;
        }

        /// <summary>
        /// Blurs currently displayed image.
        /// Respectivelly it's source bitmap (srcBitmap)
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btBlur_Click(object sender, EventArgs e) {
            graphics.Dispose();
            bmp.Dispose();
            backBmp = (Bitmap)srcBmp.Clone();
            srcBmp = Image.blur(srcBmp);
            drawBitmap(srcBmp);
            lbxObjects.Items.Clear();
            btEvaluate.Enabled = true;
            btDetail.Enabled = false;
        }

        /// <summary>
        /// Changes current bitmap to backup. It's used as one step back function.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btBack_Click(object sender, EventArgs e) {
            srcBmp = (Bitmap)backBmp.Clone();
            drawBitmap(srcBmp);
        }
    }
}
