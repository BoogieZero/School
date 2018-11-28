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

namespace NumberClasification {
    public partial class Form1 : Form {
        private const Boolean SHOW_DETAIL = true;

        /// <summary>
        /// Size of pen used for drawing to bitmap in GUI
        /// </summary>
        private const float PEN_SIZE = 4f;

        /// <summary>
        /// Graphic context build above bitmap in GUI
        /// </summary>
        public Graphics graphics;

        /// <summary>
        /// Bitmap used for displaying and drawing
        /// Bitmap is created above panel_1
        /// </summary>
        public Bitmap bmp;

        /// <summary>
        /// Instance of procesed image used for displaying features of that image
        /// </summary>
        public BWImage bwImg;

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

        /// <summary>
        /// Green pen
        /// </summary>
        Pen penG = new Pen(Color.Green, 2f);

        /// <summary>
        /// Blue pen
        /// </summary>
        Pen penB = new Pen(Color.Blue, 0.5f);

        /// <summary>
        /// Orange pen
        /// </summary>
        Pen penO = new Pen(Color.Orange, 0.5f);

        /// <summary>
        /// Black pen used for drawing in panel
        /// </summary>
        Pen penWrite = new Pen(Color.Black, PEN_SIZE);
        
        /// <summary>
        /// White pen used for clearing in panel by mouse rightclick
        /// </summary>
        Pen penWriteDel = new Pen(Color.White, PEN_SIZE*1.5f);

        /// <summary>
        /// Is true of mouse left button is pushed down
        /// </summary>
        Boolean mouseDownL = false;

        /// <summary>
        /// Is true of mouse right button is pushed down
        /// </summary>
        Boolean mouseDownR = false;

        /// <summary>
        /// Location of previous point in line while drawing
        /// </summary>
        PointF drawPoint;

        /// <summary>
        /// Stores relative mouse possition in panel to actual picture (bitmap)
        /// </summary>
        PointF relativePosition;
        
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
        /// Draws MinMax rectangle around detected digit in processed image
        /// </summary>
        /// <param name="bwImage">processed image</param>
        private void drawMinMaxRectangle(BWImage bwImage) {

            //Horizontal lines
            graphics.DrawLine(penG, bwImage.getMinX(), bwImage.getMinY(), bwImage.getMaxX(), bwImage.getMinY());
            graphics.DrawLine(penG, bwImage.getMinX(), bwImage.getMaxY(), bwImage.getMaxX(), bwImage.getMaxY());

            //Vertical lines
            graphics.DrawLine(penG, bwImage.getMinX(), bwImage.getMinY(), bwImage.getMinX(), bwImage.getMaxY());
            graphics.DrawLine(penG, bwImage.getMaxX(), bwImage.getMinY(), bwImage.getMaxX(), bwImage.getMaxY());
            draw();
        }

        /// <summary>
        /// Draws lines coresponding to WeightDescriptor averaging
        /// </summary>
        /// <param name="bwImage">processed image</param>
        /// <param name="descriptor">descriptor used</param>
        private void drawAverageLines(BWImage bwImage, WeightDescriptor descriptor) {
            int step = descriptor.getStep(bwImage);
            int lengthY = bwImage.getEfectiveLengthY();
            //row lines
            for(int i = 0; i < lengthY; i += step) {
                graphics.DrawLine(penB, bwImage.getMinX(), i + bwImage.getMinY(),
                                        bwImage.getMaxX(), i + bwImage.getMinY());
            }
            draw();
        }

        /// <summary>
        /// Draws lines coresponding to SampleDescriptor sapmles
        /// </summary>
        /// <param name="bwImage">processed image</param>
        /// <param name="descriptor">descriptor used</param>
        private void drawSampleLines(BWImage bwImage, SamplesDescriptor descriptor) {
            int[] rows, cols;
            descriptor.getSamples(bwImage, out rows, out cols);

            //row lines
            for(int i = 0; i < rows.Length; i++) {
                graphics.DrawLine(penB, bwImage.getMinX(), rows[i], 
                                        bwImage.getMaxX(), rows[i]);
            }

            //column lines
            for(int i = 0; i < cols.Length; i++) {
                graphics.DrawLine(penB, cols[i], bwImage.getMinY(),
                                        cols[i], bwImage.getMaxY());
            }
            draw();
        }

        /// <summary>
        /// Draws lines separating buckets used by HOGDescriptor
        /// Draws rotated lines in the middle of those buckets rotated by angle coresponding to most frequent angle in bucket
        /// </summary>
        /// <param name="bwImage">processed image</param>
        /// <param name="descriptor">descriptor used</param>
        private void drawBuckets(BWImage bwImage, HOGDescriptor descriptor) {
            double rowStep, colStep;
            descriptor.setSteps(bwImage, out rowStep, out colStep);
            int rowSteps = HOGDescriptor.BUCKET_ROWS;
            int colSteps = HOGDescriptor.BUCKET_COLUMNS;

            double shift;
            if(colStep < rowStep) {
                shift = colStep/2;
            } else {
                shift = rowStep/2;
            }

            //row lines
            for(int i = 1; i < rowSteps; i++) {
                graphics.DrawLine(penB, bwImage.getMinX(), bwImage.getMinY() + (int)(i * rowStep),
                                        bwImage.getMaxX(), bwImage.getMinY() + (int)(i * rowStep));
            }

            //row gradients
            Point[] points = new Point[2];
            Point c = new Point();

            Matrix rotate;
            double[] angles = getAnglesFromHOG(bwImage, descriptor);

            int k = 0;
            for(int i = 1; i < rowSteps + 1; i++) {
                for(int j = 0; j < colSteps; j++) {

                    //center of rotation
                    c.X = bwImage.getMinX() + (int)(j * colStep + colStep / 2);
                    c.Y = bwImage.getMinY() + (int)(i * rowStep - rowStep / 2);

                    //left point
                    points[0].X = (int)(c.X - shift);
                    points[0].Y = c.Y;

                    //right point
                    points[1].X = (int)(c.X + shift);
                    points[1].Y = c.Y;
                    
                    //rotation
                    rotate = new Matrix();
                    rotate.RotateAt((float)angles[k], c);
                    k++;

                    rotate.TransformPoints(points);

                    graphics.DrawLine(penO, points[0], points[1]);
                }
            }

            //column lines
            for(int i = 1; i < colSteps; i++) {
                graphics.DrawLine(penB, bwImage.getMinX() + (int)(i * colStep), bwImage.getMinY(),
                                        bwImage.getMinX() + (int)(i * colStep), bwImage.getMaxY());
            }
            
            draw();


        }
        
        /// <summary>
        /// Returns array of angles where index is coresponding to bucket number used by HOGDescriptor
        /// </summary>
        /// <param name="bwImage">processed image</param>
        /// <param name="descriptor">descriptor used</param>
        /// <returns>array of most frequent angles for every bucket</returns>
        private double[] getAnglesFromHOG(BWImage bwImage, HOGDescriptor descriptor) {
            double[] result = descriptor.getDescription(bwImage);
            double[] angles = new double[HOGDescriptor.NUMBER_OF_BUCKETS];

            int bucketCounter = HOGDescriptor.HISTOGRAM_ANGLES;
            double max =  double.MinValue;
            int maxIndex = -1;
            int k = 0;
            for(int i = 0; i < result.Length; i++) {

                if(result[i] > max) {
                    max = result[i];
                    maxIndex = i;
                }
                
                bucketCounter--;
                if(bucketCounter <= 0) {
                    angles[k] = -(maxIndex % HOGDescriptor.HISTOGRAM_ANGLES) * (HOGDescriptor.ANGLE_STEP * HOGDescriptor.RAD_TO_DEG);
                    k++;
                    max = 0;
                    maxIndex = 0;
                    bucketCounter = HOGDescriptor.HISTOGRAM_ANGLES;
                }
            }
            return angles;
        }

        /// <summary>
        /// Draws given image with all detected features (by given descriptor) to bitmap
        /// </summary>
        /// <param name="bwImage">procesed image</param>
        /// <param name="descriptor">descriptor used</param>
        public void setUpBitmapToDraw(BWImage bwImage, IDescriptor descriptor) {
            bmp = bwImage.getDrawbaleBitmap();
            graphics = Graphics.FromImage(bmp);

            if(descriptor is WeightDescriptor) {
                drawMinMaxRectangle(bwImage);
                drawAverageLines(bwImage, (WeightDescriptor)descriptor);

            } else if(descriptor is SamplesDescriptor) {
                drawMinMaxRectangle(bwImage);
                drawSampleLines(bwImage, (SamplesDescriptor)descriptor);

            } else if(descriptor is HOGDescriptor) {
                drawMinMaxRectangle(bwImage);
                drawBuckets(bwImage, (HOGDescriptor)descriptor);
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
            panel1.CreateGraphics().Clear(Color.White);
            panel1_Paint(null, null);
        }

        /// <summary>
        /// Sets relative position according to actual possition of mouse
        /// </summary>
        /// <param name="e">mouse event for actual possition of mouse</param>
        private void setRelativeMousePosition(MouseEventArgs e) {
            relativePosition.X = (float)(e.X - whiteSpaceLeft) / (panel1.Width - 2 * whiteSpaceLeft);
            relativePosition.Y = (float)(e.Y - whiteSpaceTop) / (panel1.Height - 2 * whiteSpaceTop);

            relativePosition.X = relativePosition.X * bmp.Width;
            relativePosition.Y = relativePosition.Y * bmp.Height;
        }

        /// <summary>
        /// Draws point and prepares for drawing by mouse move event
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void panel1_MouseDown(object sender, MouseEventArgs e) {

            setRelativeMousePosition(e);

            if(e.Button == MouseButtons.Left) {
                mouseDownL = true;
                drawPoint = relativePosition;

                graphics.FillEllipse(Brushes.Black, relativePosition.X - PEN_SIZE * 0.5f, relativePosition.Y - PEN_SIZE * 0.5f, PEN_SIZE, PEN_SIZE);
                draw();

            } else if(e.Button == MouseButtons.Right) {
                mouseDownR = true;
                drawPoint = relativePosition;

                graphics.FillEllipse(Brushes.White, relativePosition.X - (PEN_SIZE*1.5f) * 0.5f, relativePosition.Y - (PEN_SIZE*1.5f) * 0.5f, (PEN_SIZE*1.5f), (PEN_SIZE*1.5f));
                draw();
            }
        }

        /// <summary>
        /// Draws line between previous location and actual if mouse was down
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void panel1_MouseMove(object sender, MouseEventArgs e) {

            setRelativeMousePosition(e);

            if(mouseDownL) {
                graphics.FillEllipse(Brushes.Black, relativePosition.X - PEN_SIZE * 0.5f, relativePosition.Y - PEN_SIZE * 0.5f, PEN_SIZE, PEN_SIZE);
                graphics.DrawLine(penWrite, drawPoint, relativePosition);
                drawPoint = relativePosition;
                draw();

            } else if(mouseDownR) {
                graphics.FillEllipse(Brushes.White, relativePosition.X - (PEN_SIZE * 1.5f) * 0.5f, relativePosition.Y - (PEN_SIZE * 1.5f) * 0.5f, (PEN_SIZE * 1.5f), (PEN_SIZE * 1.5f));
                graphics.DrawLine(penWriteDel, drawPoint, relativePosition);
                drawPoint = relativePosition;
                draw();
            }

        }

        /// <summary>
        /// Stops drawing
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void panel1_MouseUp(object sender, MouseEventArgs e) {
            if(e.Button == MouseButtons.Left) {
                mouseDownL = false;
            } else if(e.Button == MouseButtons.Right) {
                mouseDownR = false;
            }
        }


        /// <summary>
        /// Clears bitmap
        /// </summary>
        private void clear() {
            graphics.Clear(Color.White);
        }

        /// <summary>
        /// Button for clearing panel
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btClear_Click(object sender, EventArgs e) {
            clear();
            draw();
        }

        /// <summary>
        /// Sends bitmap for evaluation
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btEvaluate_Click(object sender, EventArgs e) {
            Program.evaluateBitmap(bmp);
            clear();
        }

        /// <summary>
        /// Writes given string into information panel
        /// </summary>
        /// <param name="info"></param>
        public void writeInfo(String info) {
            txInfo.Text = info;
        }

        /// <summary>
        /// Writes given number into result panel
        /// </summary>
        /// <param name="result"></param>
        public void writeResult(int result) {
            lbResult.Text = "" + result;
        }

    }
}
