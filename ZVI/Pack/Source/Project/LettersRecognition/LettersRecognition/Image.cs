using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace LettersRecognition {
    /// <summary>
    /// Represent single loaded image with it's fragments and descriptions of them.
    /// </summary>
    public class Image {
        /// <summary>
        /// Treshold for pixel to be considered as part of a object.
        /// </summary>
        public static int treshold = 150;

        /// <summary>
        /// Bitmap of this image.
        /// </summary>
        public Bitmap bmp;

        /// <summary>
        /// Used descriptor
        /// </summary>
        public IDescriptor descriptor;

        /// <summary>
        /// Segmented objects from this image.
        /// </summary>
        public List<Fragment> fragments;

        /// <summary>
        /// Creates new instance from given bitmap and descriptor
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="des">descriptor</param>
        public Image(Bitmap bmp, IDescriptor des) {
            this.descriptor = des;
            this.bmp = bmp;
            fragments = new List<Fragment>();
            process();
            fillDescriptions();
        }

        /// <summary>
        /// Fills descriptions for each fragment.
        /// </summary>
        private void fillDescriptions() {
            foreach(Fragment frag in fragments) {
                frag.setDescription(descriptor, this);
            }
        }

        /// <summary>
        /// Creates new instance from given bitmap and descriptor
        /// </summary>
        /// <param name="path">file from which bitmap will be extracted</param>
        /// <param name="des">descriptor</param>
        public Image(String path, IDescriptor des) : this(getBitmap(path), des) {
            //Empty
        }

        /// <summary>
        /// Gets bitmap from file
        /// </summary>
        /// <param name="fileName">source file</param>
        /// <returns>bitmap from file</returns>
        private static Bitmap getBitmap(String fileName) {
            if(!File.Exists(fileName)) {
                Console.WriteLine("File doesn't exist.");
                Environment.Exit(0);
            }

            System.Drawing.Image img = System.Drawing.Image.FromFile(fileName);

            return (Bitmap)img;
        }

        #region current image raw data

        /// <summary>
        /// Raw data from currently processed bitmap
        /// </summary>
        private static byte[]pixels;

        /// <summary>
        /// Stride for currently processed bitmap data
        /// </summary>
        private static int stride;

        /// <summary>
        /// Containts information if pixel has been hit
        /// </summary>
        private static Boolean[,] hits;

        #endregion current image data

        /// <summary>
        /// Processes bitmap to get single objects.
        /// </summary>
        private void process() {
            hits = new bool[bmp.Width, bmp.Height];

            //lock
            BitmapData bmpData = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadWrite,
                bmp.PixelFormat
                );

            //Copy
            int byteCount = bmpData.Stride * bmpData.Height;        //Stride accounting for Alignment of bitmap
            pixels = new byte[byteCount];
            stride = bmpData.Stride;
            IntPtr ptrFirstPx = bmpData.Scan0;                  //pointer to first pixel
            Marshal.Copy(ptrFirstPx, pixels, 0, pixels.Length);

            //Iterate through pixels
            int heightInPxls = bmp.Height;
            int currentLine = 0;
            int widthInBytes = bmpData.Width * 1;   //1 byte per pixel

            //Clear borders (Black on edges of bitmap)
            clearBorders(heightInPxls, widthInBytes);

            for(int y = 0; y < heightInPxls; y++) {     //lines
                currentLine = y * bmpData.Stride;
                for(int x = 0; x < widthInBytes; x++) {   //rows

                    if(isObject(pixels[currentLine + x])) {
                        //found pixel of object
                        if(hits[x, y] == false)   //px without hit
                            processByFreeman(x, y);
                    }
                }
                //end of row
            }
            //end of bitmap 

            //Unlock
            bmp.UnlockBits(bmpData);
        }

        /// <summary>
        /// Clears borders of bitmap.
        /// Single pixel wide border is made black (value 0).
        /// </summary>
        /// <param name="heightInPxls">height of the bitmap in pixels</param>
        /// <param name="widthInBytes">height of the bitmap in bytes</param>
        private void clearBorders(int heightInPxls, int widthInBytes) {
            //top & bottom
            int currentLine = 0;
            for(int x = 0; x < widthInBytes; x++) {
                pixels[currentLine + x] = 0;
            }

            currentLine = (heightInPxls - 1) * stride;
            for(int x = 0; x < widthInBytes; x++) {
                pixels[currentLine + x] = 0;
            }

            //left & right
            currentLine = 0;
            for(int y = 0; y < heightInPxls; y++) {
                currentLine = y * stride;
                pixels[currentLine + 0] = 0;
                //end of row
            }
            currentLine = 0;
            for(int y = 0; y < heightInPxls; y++) {
                currentLine = y * stride;
                pixels[currentLine + (widthInBytes - 1)] = 0;
                //end of row
            }

        }

        /// <summary>
        /// Returns true if given pixel value is over the treshold for it to be considered as object.
        /// </summary>
        /// <param name="value">examined value</param>
        /// <returns>true for object</returns>
        public static Boolean isObject(int value) {
            if(value > treshold) {
                return true;
            } else {
                return false;
            }
        }

        /// <summary>
        /// Returns true if given pixels value is over the treshold for it to be considered as object.
        /// </summary>
        /// <param name="x">x-coordinate of pixel</param>
        /// <param name="y">y-coordinate of pixel</param>
        /// <returns>true for object</returns>
        private static Boolean isObject(int x, int y) {
            int index = y * stride + x;
            return isObject(pixels[index]);
        }

        /// <summary>
        /// Processes pixels in freeman algorithm. Discovered fragments in this way are added to the image instance.
        /// Marks hits for pixels encountered.
        /// Algorithm ignores single pixel objects.
        /// </summary>
        /// <param name="x">x-coordinate of start pixel</param>
        /// <param name="y">x-coordinate of start pixel</param>
        private void processByFreeman(int x, int y) {
            Boolean validFragment = true;
            Fragment frag = new Fragment(x, y);
            Point current = new Point(x, y);
            Point start = current;

            //while freeman

            int dir = 3;    //default direction
            while(true) {
                frag.addEdgePoint(current);
                if(!nextByFreeman(ref current, ref dir)) {
                    //Premature end of border line
                    validFragment = false;
                    break;
                }
                if(current.X == start.X && current.Y == start.Y) break;
            }

            //finish
            if(!validFragment) {
                //Single dot fragment -> ignore
                hits[frag.edge[0].X, frag.edge[0].Y] = true;
            } else {
                fillVolume(frag);
                //frag.setMinRectangleAngle();
                //frag.cloneFromBitmap(bmp);
                fragments.Add(frag);

            }
        }

        /// <summary>
        /// Marks all pixels of the given fragment as hit.
        /// </summary>
        /// <param name="frag">source fragment</param>
        private void fillVolume(Fragment frag) {
            //foreach row in frag.edge map
            foreach(KeyValuePair<int, Point> item in frag.minMaxRLC) {  //row
                for(int i = item.Value.X; i <= item.Value.Y; i++) {     //col
                    //pixels in a row from min to max
                    if(isObject(i, item.Key)) {
                        //element is object -> hit
                        hits[i, item.Key] = true;   //hit
                        frag.allPoints.Add(new Point(i, item.Key)); //add point to all points
                    }
                }
            }
            foreach(Point p in frag.allPoints) {
                if(!isObject(p.X, p.Y)) {
                    Console.WriteLine();
                }
            }
        }

        /// <summary>
        /// Changes current pixel to next one in freeman algorithm. Direction of discovering this pixel is updated.
        /// Returns false for single pixel object.
        /// </summary>
        /// <param name="current">currently processed pixel</param>
        /// <param name="direction">direction of encounter</param>
        /// <returns>false for single pixel object, true otherwise</returns>
        private Boolean nextByFreeman(ref Point current, ref int direction) {
            int index = current.Y * stride + current.X;
            direction = (direction + 3) % 4;
            Point nextPoint = new Point();
            int value = -1;
            for(int i = 0; i < 4; i++) {
                try {
                    //for each direction
                    switch(direction) {
                        case 0: //right
                            value = pixels[index + 1];
                            break;
                        case 1: //up
                            value = pixels[index - stride];
                            break;
                        case 2: //left
                            value = pixels[index - 1];
                            break;
                        case 3: //down
                            value = pixels[index + stride];
                            break;
                        default:
                            //Unreachable
                            Console.WriteLine("Wrong direction!");
                            throw new Exception("Wrong direction");
                    }
                } catch(Exception e) {
                    Console.WriteLine("Object is on the edge. (Clear the edge of the bitmap)");
                    Environment.Exit(0);
                    throw e;
                }

                if(isObject(value)) {
                    switch(direction) {
                        case 0: //right
                            nextPoint.X = current.X + 1;
                            nextPoint.Y = current.Y;
                            break;
                        case 1: //up
                            nextPoint.X = current.X;
                            nextPoint.Y = current.Y - 1;
                            break;
                        case 2: //left
                            nextPoint.X = current.X - 1;
                            nextPoint.Y = current.Y;
                            break;
                        case 3: //down
                            nextPoint.X = current.X;
                            nextPoint.Y = current.Y + 1;
                            break;
                        default:
                            Console.WriteLine("Wrong direction!");
                            Environment.Exit(0);
                            break;
                    }
                    current = nextPoint;
                    return true;
                }

                direction = (direction + 1) % 4;
            }

            //Single dot border
            return false;
        }

        /// <summary>
        /// Prepares bitmap for direct acces to it's raw data by byte array.
        /// Data are locked and it's necessary to unlock them afterwards by use of closeBitmap() or saveCloseBitmap() methods.
        /// Method is meant for bitmap with pixel format 24bppRgb.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="stride">stride for given bitmap</param>
        /// <param name="pixels">raw data of bitmap</param>
        /// <param name="bmpData">bitmap data wrapper</param>
        public static void openBitmap24bpp(Bitmap bmp, out int stride, out byte[] pixels, out BitmapData bmpData) {
            openBitmap(bmp, out stride, out pixels, out bmpData, 3);
        }

        /// <summary>
        /// Prepares bitmap for direct acces to it's raw data by byte array.
        /// Data are locked and it's necessary to unlock them afterwards by use of closeBitmap() or saveCloseBitmap() methods.
        /// Method is meant for bitmap with pixel format 8bppIndexed.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="stride">stride for given bitmap</param>
        /// <param name="pixels">raw data of bitmap</param>
        /// <param name="bmpData">bitmap data wrapper</param>
        public static void openBitmap8bi(Bitmap bmp, out int stride, out byte[] pixels, out BitmapData bmpData) {
            openBitmap(bmp, out stride, out pixels, out bmpData, 1);
        }

        /// <summary>
        /// Prepares bitmap for direct acces to it's raw data by byte array.
        /// Data are locked and it's necessary to unlock them afterwards by use of closeBitmap() or saveCloseBitmap() methods.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="stride">stride for given bitmap</param>
        /// <param name="pixels">raw data of bitmap</param>
        /// <param name="bmpData">bitmap data wrapper</param>
        /// <param name="bytesPerPx">bytes per pixel (pixel format dependent)</param>
        private static void openBitmap(Bitmap bmp, out int stride, out byte[] pixels, out BitmapData bmpData, int bytesPerPx) {
            //lock
            bmpData = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadWrite,
                bmp.PixelFormat
                );

            //Variables
            stride = bmpData.Stride;
            //int byteCount = bmpData.Stride * bmp.Height * bytesPerPx;        //Stride accounting for Alignment of bitmap
            int byteCount = bmpData.Stride * bmpData.Height;
            pixels = new byte[byteCount];

            //Copy
            IntPtr ptrFirstPx = bmpData.Scan0;                  //pointer to first pixel
            Marshal.Copy(ptrFirstPx, pixels, 0, pixels.Length);
        }

        /// <summary>
        /// Unlocks previously openned bitmap by methods using openBitmap().
        /// Data are saved copied into given bitmap from byte array pixels.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="pixels">raw data of bitmap</param>
        /// <param name="bmpData">bitmap data wrapper</param>
        public static void saveCloseBitmap(Bitmap bmp, BitmapData bmpData, byte[] pixels) {
            IntPtr ptrFirstPx = bmpData.Scan0;
            Marshal.Copy(pixels, 0, ptrFirstPx, pixels.Length);
            bmp.UnlockBits(bmpData);
        }

        /// <summary>
        /// Unlocks previously openned bitmap by methods using openBitmap().
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="pixels">raw data of bitmap</param>
        /// <param name="bmpData">bitmap data wrapper</param>
        public static void closeBitmap(Bitmap bmp, BitmapData bmpData) {
            bmp.UnlockBits(bmpData);
        }

        /// <summary>
        /// Creates dilatation on given bitmap by D-mask (4 surrounding pixels).
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <returns>dilatated bitmap</returns>
        public static Bitmap dilatation(Bitmap bmp) {

            //Src bitmap
            int stride;
            BitmapData bmpData;
            byte[]oldPixels;
            openBitmap8bi(bmp, out stride, out oldPixels, out bmpData);

            //Output array
            byte[]newPixels = new byte[oldPixels.Length];
            Array.Copy(oldPixels, newPixels, oldPixels.Length);

            //Iterate pixels
            int value;
            int currentLine;
            int index;
            for(int y = 1; y < bmp.Height - 1; y++) {     //lines
                currentLine = y * stride;
                for(int x = 1; x < bmp.Width - 1; x++) {   //cols
                    index = currentLine + x;
                    value = oldPixels[index];
                    if(value < oldPixels[index - 1]) value = oldPixels[index - 1];
                    if(value < oldPixels[index + 1]) value = oldPixels[index + 1];
                    if(value < oldPixels[index - stride]) value = oldPixels[index - stride];
                    if(value < oldPixels[index + stride]) value = oldPixels[index + stride];
                    newPixels[index] = (byte)value;
                }
                //end of row
            }

            //Save
            saveCloseBitmap(bmp, bmpData, newPixels);
            return bmp;
        }

        /// <summary>
        /// Creates erosion on given bitmap by D-mask (4 surrounding pixels).
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <returns>eroded bitmap</returns>
        public static Bitmap erosion(Bitmap bmp) {
            //Src bitmap
            int stride;
            BitmapData bmpData;
            byte[]oldPixels;
            openBitmap8bi(bmp, out stride, out oldPixels, out bmpData);

            //Output array
            byte[]newPixels = new byte[oldPixels.Length];
            Array.Copy(oldPixels, newPixels, oldPixels.Length);

            //Iterate pixels
            int value;
            int currentLine;
            int index;
            for(int y = 1; y < bmp.Height - 1; y++) {     //lines
                currentLine = y * stride;
                for(int x = 1; x < bmp.Width - 1; x++) {   //cols
                    index = currentLine + x;
                    value = oldPixels[index];
                    if(value > oldPixels[index - 1]) value = oldPixels[index - 1];
                    if(value > oldPixels[index + 1]) value = oldPixels[index + 1];
                    if(value > oldPixels[index - stride]) value = oldPixels[index - stride];
                    if(value > oldPixels[index + stride]) value = oldPixels[index + stride];
                    newPixels[index] = (byte)value;
                }
                //end of row
            }

            //Save
            saveCloseBitmap(bmp, bmpData, newPixels);
            return bmp;
        }

        /// <summary>
        /// Aproximates individual pixels by 5x5 mask evenly.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <returns>blurred bitmap</returns>
        public static Bitmap blur(Bitmap bmp) {
            //Src bitmap

            BitmapData bmpData;
            openBitmap8bi(bmp, out stride, out pixels, out bmpData);

            //Output array
            byte[]newPixels = new byte[pixels.Length];
            Array.Copy(pixels, newPixels, pixels.Length);

            //Iterate pixels
            int currentLine;
            int index;
            for(int y = 2; y < bmp.Height - 2; y++) {     //lines
                currentLine = y * stride;
                for(int x = 2; x < bmp.Width - 2; x++) {   //cols
                    index = currentLine + x;
                    newPixels[index] = (byte)getAverageFive(index);
                }
                //end of row
            }

            //Save
            saveCloseBitmap(bmp, bmpData, newPixels);
            return bmp;
        }

        /// <summary>
        /// Returns average value of given pixel index in pixels byte array using 5x5 mask.
        /// </summary>
        /// <param name="index">index of pixel in byte array</param>
        /// <returns>average value for the pixel</returns>
        private static int getAverageFive(int index) {
            int value = 0;
            for(int i = index - (2 * stride); i <= index + (2 * stride); i += stride) {
                for(int j = -2; j <= 2; j++) {
                    value += pixels[i + j];
                }
            }
            value /= 25;
            return value;
        }

        /// <summary>
        /// Class represents single object in image with it's attributes.
        /// </summary>
        public class Fragment {
            /// <summary>
            /// Upper left corner of rectangle containing detected object.
            /// </summary>
            public Point origin;

            /// <summary>
            /// List of points on the edge.
            /// </summary>
            public List<Point> edge;

            /// <summary>
            /// List of all points belonging to this object.
            /// </summary>
            public List<Point> allPoints;

            /// <summary>
            /// Description of this object by used descriptor.
            /// </summary>
            public Description description;

            /// <summary>
            /// Min/Max values of coordinates for rectangle surrounding this object.
            /// </summary>
            #region minMax for Detail
            public int minXdet;
            public int maxXdet;
            public int minYdet;
            public int maxYdet;
            #endregion minMax for Detail



            /// <summary>
            /// Min and Max values for each row.
            /// p.X = MIN
            /// p.Y = MAX
            /// </summary>
            public Dictionary<int,Point> minMaxRLC;

            /// <summary>
            /// Creates new instance of this fragment.
            /// </summary>
            /// <param name="x">x-coordinate of first detected pixel</param>
            /// <param name="y">y-coordinate of first detected pixel</param>
            public Fragment(int x, int y) {
                origin = new Point(x, y);
                minMaxRLC = new Dictionary<int, Point>();
                edge = new List<Point>();
                allPoints = new List<Point>();
                description = new Description();
                minXdet = int.MaxValue;
                minYdet = int.MaxValue;
                maxXdet = int.MinValue;
                maxYdet = int.MinValue;
                //this.bmp = bmp.Clone(new Rectangle(x, y, width, height), System.Drawing.Imaging.PixelFormat.Format8bppIndexed);
            }

            /// <summary>
            /// Adds new edge point to edge.
            /// Updates min and max values for coordinates.
            /// Updates RLC (Bounds) representation.
            /// </summary>
            /// <param name="p"></param>
            public void addEdgePoint(Point p) {
                //min max
                if(p.X < minXdet) minXdet = p.X;
                if(p.Y < minYdet) minYdet = p.Y;
                if(p.X > maxXdet) maxXdet = p.X;
                if(p.Y > maxYdet) maxYdet = p.Y;

                edge.Add(p);
                updateRlc(p.Y, p.X);
            }

            /// <summary>
            /// Updates array with min or max value for given row
            /// </summary>
            /// <param name="row">row</param>
            /// <param name="value">column</param>
            private void updateRlc(int row, int value) {
                if(!minMaxRLC.ContainsKey(row)) {
                    //new row
                    minMaxRLC[row] = new Point(value, value);
                } else {
                    //update existing row
                    //Point p = minMaxRLC[row];

                    if(value < minMaxRLC[row].X)    //new min
                        minMaxRLC[row] = new Point(value, minMaxRLC[row].Y);

                    if(value > minMaxRLC[row].Y)    //new max
                        minMaxRLC[row] = new Point(minMaxRLC[row].X, value);

                }
            }

            /// <summary>
            /// Sets most of attributes for this fragment using given descriptor and it's parrent image.
            /// </summary>
            /// <param name="des">descriptor for description</param>
            /// <param name="img">it's parrent image</param>
            public void setDescription(IDescriptor des, Image img) {
                des.setAttributes(this, img);
            }

            /// <summary>
            /// String containing detected result and coordinates of 
            /// upper left corner of rectangle containing detected object.
            /// </summary>
            /// <returns></returns>
            public override string ToString() {
                String str = description.result+"\t["+minXdet+","+minYdet+"]";
                return str;
            }
        }


    }
}
