using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Filters {
    public partial class Form1 : Form {
        Bitmap bmp;
        Graphics graphics;
        private ColorF[,] myPixels;
        private ColorF[,] myPixelsOld;
        private ColorF[,] myPixelsOrig;

        public Form1() {
            InitializeComponent();
            bmp = new Bitmap(drawBoard.Width, drawBoard.Height);
            graphics = Graphics.FromImage(bmp);
            graphics.Clear(Color.White);
        }

        /// <summary>
        /// Load image by browse dialog and displays it into drawBoard
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btLoadImage_Click(object sender, EventArgs e) {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.FileName = string.Empty;
            ofd.Filter = "Podporovane obrazky (bmp, gif, jpeg, png, tiff)|*.bmp;*.gif;*.jpg;*.jpeg;*.png;*.tif;*.tiff";
            ofd.Multiselect = false;
            ofd.CheckFileExists = true;
            DialogResult dr = ofd.ShowDialog(this);
            if(dr != System.Windows.Forms.DialogResult.OK) {
                return;
            }
            Image importedImage = null;
            string errMsg = "Neznama chyba pri nahravani obrazku.";
            try {
                importedImage = Image.FromFile(ofd.FileName);
            } catch(Exception exc) {
                importedImage = null;
                errMsg = exc.Message;
            }
            if(importedImage == null) {
                MessageBox.Show(errMsg, "Chyba cteni souboru", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
            graphics.Dispose();
            bmp.Dispose();
            bmp = (Bitmap)importedImage;
            graphics = Graphics.FromImage(bmp);
            myPixels = bitmapToColorf(bmp);
            myPixelsOrig = bitmapToColorf(bmp);
            myPixelsOld = myPixels;
            drawBoard.CreateGraphics().Clear(Color.White);
            draw();
            btMess.Enabled = true;
            btEdgeVertical.Enabled = true;
            btExpo.Enabled = true;
            btGauss.Enabled = true;
            btGaussM.Enabled = true;
            btMotionM.Enabled = true;
            btSharp.Enabled = true;
            btSquare.Enabled = true;
            btSquareM.Enabled = true;
            btFilter1.Enabled = true;
            button1.Enabled = true;
            btBack.Enabled = true;
            btOrig.Enabled = true;
            btSave.Enabled = true;
        }

        /// <summary>
        /// Converts bitmap to field of ColorF
        /// </summary>
        /// <param name="souce"> source bitmap</param>
        /// <returns></returns>
        private ColorF[,] bitmapToColorf(Bitmap souce) {
            if(souce == null) return null;
            ColorF[,] rv = new ColorF[souce.Height, souce.Width];

            BitmapData bd = null;
            try {
                bd = bmp.LockBits(new Rectangle(0, 0, bmp.Width, bmp.Height), ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);
            } catch(Exception exc) {
                Console.WriteLine(exc.Message);
                return null;
            }
            ColorF c = new ColorF();
            unsafe
            {
                byte* ptr = null;
                byte* lnptr = (byte*)bd.Scan0;

                for(int i = 0; i < bd.Height; ++i) {
                    ptr = lnptr;
                    for(int j = 0; j < bd.Width; ++j) {
                        //*ptr, *ptr+1, *ptr+2, *ptr+3 = B, G, R, A
                        c.b = (float)(*ptr) / 255.0f;
                        c.g = (float)(*(ptr + 1)) / 255.0f;
                        c.r = (float)(*(ptr + 2)) / 255.0f;
                        c.a = (float)(*(ptr + 3)) / 255.0f;

                        rv[i, j] = c;
                        ptr += 4; //ARGB = 4 bytes
                    }
                    lnptr += bd.Stride;
                }

            }

            bmp.UnlockBits(bd);

            return rv;
        }

        /// <summary>
        /// Converts field of ColorF to bitmap
        /// </summary>
        /// <param name="source"> source field of ColorF</param>
        /// <returns></returns>
        private Bitmap colorfToBitmap(ColorF[,] source) {
            Bitmap rv = new Bitmap(source.GetLength(1), source.GetLength(0), PixelFormat.Format32bppArgb);

            BitmapData bd = null;
            try {
                bd = rv.LockBits(new Rectangle(0, 0, bmp.Width, bmp.Height), ImageLockMode.WriteOnly, PixelFormat.Format32bppArgb);
            } catch(Exception exc) {
                Console.WriteLine(exc.Message);
                return null;
            }
            unsafe
            {
                byte* ptr = null;
                byte* lnptr = (byte*)bd.Scan0;
                ColorF c;
                for(int i = 0; i < bd.Height; ++i) {
                    ptr = lnptr;
                    for(int j = 0; j < bd.Width; ++j) {
                        c = source[i, j];
                        //*ptr, *ptr+1, *ptr+2, *ptr+3 = B, G, R, A
                        *ptr = (byte)(c.b * 255.0f);
                        *(ptr + 1) = (byte)(c.g * 255.0f);
                        *(ptr + 2) = (byte)(c.r * 255.0f);
                        *(ptr + 3) = (byte)(c.a * 255.0f);

                        ptr += 4; //ARGB = 4 bytes
                    }
                    lnptr += bd.Stride;
                }

            }

            rv.UnlockBits(bd);

            return rv;
        }

        /// <summary>
        /// Draws bitmap into odrawboard
        /// </summary>
        private void draw() {
            drawBoard_Paint(this, null);
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
            // grp.DrawImage(bmp, 0, 0, panel1.Width, panel1.Height);
            grp.Dispose();
        }

        /// <summary>
        /// Redraw image after change of scale
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void drawBoard_Resize(object sender, EventArgs e) {
            drawBoard.CreateGraphics().Clear(Color.White);
            drawBoard_Paint(null, null);
        }

        /// <summary>
        /// Embos filter by 3x3 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btEmbos_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {-2, -1, 0},
                {-1, 0, 1},
                {0, 1, 2}
            };
            int weight = 8;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];
            
            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0)-1, i] = myPixels[myPixels.GetLength(0)-1, i];
            }
            
            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    newMyPixels[i, j] = new ColorF(RValue+128, GValue+128, BValue+128);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        /// <summary>
        /// Square blur by 3x3 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btSquare_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
            };
            int weight = 9;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        /// <summary>
        /// Gaussian blur by 3x3 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btGauss_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
            };
            int weight = 16;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
            
        }

        /// <summary>
        /// Square blur by 5x5 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btSquareM_Click(object sender, EventArgs e) {
            float[,] filter = new float[5, 5] {
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
            };
            int weight = 25;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
                newMyPixels[1, i] = myPixels[1, i];
                newMyPixels[myPixels.GetLength(0) - 2, i] = myPixels[myPixels.GetLength(0) - 2, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
                newMyPixels[i, 1] = myPixels[i, 1];
                newMyPixels[i, myPixels.GetLength(1) - 2] = myPixels[i, myPixels.GetLength(1) - 2];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 2; i < myPixels.GetLength(0) - 2; i++) {
                for(int j = 2; j < myPixels.GetLength(1) - 2; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 2), j + (fj - 2)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        /// <summary>
        /// Gaussian blur by 5x5 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btGaussM_Click(object sender, EventArgs e) {
            float[,] filter = new float[5, 5] {
                {2, 4, 5, 4, 2},
                {4, 9, 12, 9, 4},
                {5, 12, 15, 12, 5},
                {4, 9, 12, 9, 4},
                {2, 4, 5, 4, 2}
            };
            int weight = 159;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
                newMyPixels[1, i] = myPixels[1, i];
                newMyPixels[myPixels.GetLength(0) - 2, i] = myPixels[myPixels.GetLength(0) - 2, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
                newMyPixels[i, 1] = myPixels[i, 1];
                newMyPixels[i, myPixels.GetLength(1) - 2] = myPixels[i, myPixels.GetLength(1) - 2];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 2; i < myPixels.GetLength(0) - 2; i++) {
                for(int j = 2; j < myPixels.GetLength(1) - 2; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 2), j + (fj - 2)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        /// <summary>
        /// Motion blur by 5x5 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btMotionM_Click(object sender, EventArgs e) {
            float[,] filter = new float[5, 5] {
                {0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1},
                {3, 3, 3, 3, 3},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0}
            };
            int weight = 25;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
                newMyPixels[1, i] = myPixels[1, i];
                newMyPixels[myPixels.GetLength(0) - 2, i] = myPixels[myPixels.GetLength(0) - 2, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
                newMyPixels[i, 1] = myPixels[i, 1];
                newMyPixels[i, myPixels.GetLength(1) - 2] = myPixels[i, myPixels.GetLength(1) - 2];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 2; i < myPixels.GetLength(0) - 2; i++) {
                for(int j = 2; j < myPixels.GetLength(1) - 2; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 2), j + (fj - 2)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        /// <summary>
        /// Horizontal edge detect by 5x5 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btSharp_Click(object sender, EventArgs e) {
            /* Diagonal
            float[,] filter = new float[5, 5] {
                {1, 1, 1, 1, 0},
                {1, 2, 2, 0, -1},
                {1, 2, 0, -2, -1},
                {1, 0, -2, -2, -1},
                {0, -1, -1, -1, -1}
            };
            int weight = 1;
            */

            //Horizontal
            float[,] filter = new float[5, 5] {
                {2, 2, 2, 2, 2},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0},
                {-1, -1, -1, -1, -1},
                {-2, -2, -2, -2, -2}
            };
            int weight = 1;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
                newMyPixels[1, i] = myPixels[1, i];
                newMyPixels[myPixels.GetLength(0) - 2, i] = myPixels[myPixels.GetLength(0) - 2, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
                newMyPixels[i, 1] = myPixels[i, 1];
                newMyPixels[i, myPixels.GetLength(1) - 2] = myPixels[i, myPixels.GetLength(1) - 2];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 2; i < myPixels.GetLength(0) - 2; i++) {
                for(int j = 2; j < myPixels.GetLength(1) - 2; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 2), j + (fj - 2)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    if(RValue < 0) RValue = 0;
                    if(GValue < 0) GValue = 0;
                    if(BValue < 0) BValue = 0;

                    if(RValue > 1) RValue = 1;
                    if(GValue > 1) GValue = 1;
                    if(BValue > 1) BValue = 1;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;

        }

        /// <summary>
        /// Horizontal edge detect by 3x3 matric
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {1, 1, 1},
                {0, 0, 0},
                {-1, -1, -1}
            };
            int weight = 1;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                            
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    if(RValue < 0) RValue = 0;
                    if(GValue < 0) GValue = 0;
                    if(BValue < 0) BValue = 0;

                    if(RValue > 1) RValue = 1;
                    if(GValue > 1) GValue = 1;
                    if(BValue > 1) BValue = 1;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        /// <summary>
        /// Exposure effect by 3x3 matrix
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btExpo_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {1, 1, 1},
                {0, 0, 0},
                {1, 1, 1}
            };
            int weight = 4;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;
                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    if(RValue < 0) RValue = 0;
                    if(GValue < 0) GValue = 0;
                    if(BValue < 0) BValue = 0;

                    if(RValue > 1) RValue = 1;
                    if(GValue > 1) GValue = 1;
                    if(BValue > 1) BValue = 1;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        private void btBack_Click(object sender, EventArgs e) {
            myPixels = myPixelsOld;
            bmp = colorfToBitmap(myPixels);
            draw();
        }

        private void button2_Click(object sender, EventArgs e) {
            myPixels = myPixelsOrig;
            bmp = colorfToBitmap(myPixels);
            draw();
        }

        private void btSave_Click_1(object sender, EventArgs e) {
            SaveFileDialog sfd = new SaveFileDialog();
            sfd.Filter = "|*.png;";
            ImageFormat format = ImageFormat.Png;
            if(sfd.ShowDialog() == System.Windows.Forms.DialogResult.OK) {
                string ext = System.IO.Path.GetExtension(sfd.FileName);
                bmp.Save(sfd.FileName, format);
            }
        }

        private void btEdgeVertical_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {1, 0, -1},
                {1, 0, -1},
                {1, 0, -1}
            };
            int weight = 1;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;

                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    if(RValue < 0) RValue = 0;
                    if(GValue < 0) GValue = 0;
                    if(BValue < 0) BValue = 0;

                    if(RValue > 1) RValue = 1;
                    if(GValue > 1) GValue = 1;
                    if(BValue > 1) BValue = 1;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }

        private void btMess_Click(object sender, EventArgs e) {
            float[,] filter = new float[3, 3] {
                {0, 0, 0},
                {5, -10, 5},
                {0, 0, 0}
            };
            int weight = 1;

            ColorF[,] newMyPixels = new ColorF[myPixels.GetLength(0), myPixels.GetLength(1)];

            //copy unused border from original
            for(int i = 0; i < myPixels.GetLength(1); i++) {
                newMyPixels[0, i] = myPixels[0, i];
                newMyPixels[myPixels.GetLength(0) - 1, i] = myPixels[myPixels.GetLength(0) - 1, i];
            }

            for(int i = 0; i < myPixels.GetLength(0); i++) {
                newMyPixels[i, 0] = myPixels[i, 0];
                newMyPixels[i, myPixels.GetLength(1) - 1] = myPixels[i, myPixels.GetLength(1) - 1];
            }

            ColorF pixel;
            float RValue;
            float GValue;
            float BValue;

            for(int i = 1; i < myPixels.GetLength(0) - 1; i++) {
                for(int j = 1; j < myPixels.GetLength(1) - 1; j++) {
                    //run over all pixels in image
                    RValue = 0f;
                    GValue = 0f;
                    BValue = 0f;

                    for(int fi = 0; fi < filter.GetLength(0); fi++)
                        for(int fj = 0; fj < filter.GetLength(1); fj++) {
                            //run through filter
                            pixel = myPixels[i + (fi - 1), j + (fj - 1)];     // - 1 for filter [3, 3] - starts at first row and column

                            RValue += filter[fi, fj] * pixel.r;
                            GValue += filter[fi, fj] * pixel.g;
                            BValue += filter[fi, fj] * pixel.b;

                        }

                    RValue /= weight;
                    GValue /= weight;
                    BValue /= weight;

                    if(RValue < 0) RValue = 0;
                    if(GValue < 0) GValue = 0;
                    if(BValue < 0) BValue = 0;

                    if(RValue > 1) RValue = 1;
                    if(GValue > 1) GValue = 1;
                    if(BValue > 1) BValue = 1;

                    newMyPixels[i, j] = new ColorF(RValue, GValue, BValue);

                }
            }

            bmp = colorfToBitmap(newMyPixels);
            draw();
            myPixelsOld = myPixels;
            myPixels = newMyPixels;
        }
    }
}
