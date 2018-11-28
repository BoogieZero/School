using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Class represents container for simplyfied grayscale bitmap with tools for loading
    /// and getting additional information like indees of min/max black pixels in bitmap in both axis
    /// </summary>
    public class BWImage {
        /// <summary>
        /// Treshold for pixel to be considered black
        /// </summary>
        private const int BLACK_PIXEL_TRESHOLD = 140;   //140

        /// <summary>
        /// Minimal width of minMax rectangle
        /// </summary>
        private const int MIN_EFECT_LENGTH_X = 10;

        /// <summary>
        /// Minimal height of minMax rectangle
        /// </summary>
        private const int MIN_EFECT_LENGTH_Y = 20;

        /// <summary>
        /// Actual bitmap of image
        /// </summary>
        private int[,] bmp = new int[128, 128];

        /// <summary>
        /// Index of most left black pixel
        /// </summary>
        private int minX = int.MaxValue;

        /// <summary>
        /// Index of most roght black pixel
        /// </summary>
        private int maxX = int.MinValue;

        /// <summary>
        /// Index of most top black pixel
        /// </summary>
        private int minY = int.MaxValue;

        /// <summary>
        /// Index of most bottom black pixel
        /// </summary>
        private int maxY = int.MinValue;

        /// <summary>
        /// Returns bitmap of this image
        /// </summary>
        /// <returns>bitmap of this image</returns>
        public int[,] getBitmap() {
            return bmp;
        }

        /// <summary>
        /// Returns proper bitmap for drawing
        /// </summary>
        /// <returns>ARGB bitmap for drawing</returns>
        public Bitmap getDrawbaleBitmap() {
            Bitmap bmpGray = new Bitmap(128, 128);

            int value;
            for(int i = 0; i < 128; i++)
                for(int j = 0; j < 128; j++) {
                    value = bmp[i, j];
                    bmpGray.SetPixel(j, i, Color.FromArgb(value, value, value));
                }

            return bmpGray;
        }

        public int getMinX() {
            return minX;
        }

        public int getMaxX() {
            return maxX;
        }

        public int getMinY() {
            return minY;
        }

        public int getMaxY() {
            return maxY;
        }

        /// <summary>
        /// Returns width of minMax rectangle
        /// </summary>
        /// <returns>width of minMax rectangle</returns>
        public int getEfectiveLengthX() {
            return maxX - minX + 1;
        }

        /// <summary>
        /// Returns height of minMax rectangle
        /// </summary>
        /// <returns></returns>
        public int getEfectiveLengthY() {
            return maxY - minY + 1;
        }

        /// <summary>
        /// Returns if minMax rectangle is too small in any axis
        /// </summary>
        /// <returns>true for too small minMax rectangle of this image</returns>
        private Boolean isTooSmall() {

            if(getEfectiveLengthX() < MIN_EFECT_LENGTH_X ||
                getEfectiveLengthY() < MIN_EFECT_LENGTH_Y) {

                Console.WriteLine("Content is too small!");
                return true;
            }

            return false;
        }

        /// <summary>
        /// Sets given value into bitmap at i, j position
        /// </summary>
        /// <param name="img">image to set value to</param>
        /// <param name="value">value to be set</param>
        /// <param name="i">Y position in bitmap</param>
        /// <param name="j">X position in bitmap</param>
        private static void setValue(BWImage img, int value, int i, int j) {

            if(value < BLACK_PIXEL_TRESHOLD) {
                if(img.minX > j) img.minX = j;
                if(img.maxX < j) img.maxX = j;
                if(img.minY > i) img.minY = i;
                if(img.maxY < i) img.maxY = i;
                
                value = 0;
            }
            img.bmp[i, j] = value;
        }

        /// <summary>
        /// Creates instance of BWImage from file if possible otherwise returns null
        /// </summary>
        /// <param name="inputFile">fileName</param>
        /// <returns>instance of BWImage or null if instance could not be created</returns>
        public static BWImage createInstanceFromFile(String inputFile) {
            BWImage img = new BWImage();

            String line;
            StreamReader file = new StreamReader(@"" + inputFile);

            for(int i = 0; i < 4; i++)
                file.ReadLine();

            int value;
            String[] sLine;

            //over every pixel in image (128x128)
            for(int i = 0; i < 128; i++)
                for(int j = 0; j < 128; j++) {
                    if((line = file.ReadLine()) == null) {
                        //EOF
                        i = 128;
                        break;
                    };   // return;

                    if(!int.TryParse(line, out value)) {
                        //Line could not be parsed into one integer -> there is probably line of numbers

                        sLine = line.TrimEnd(' ').Split(' ');
                        //over every number in line
                        j--;    // first j++ would start at j = 1 instead of 0 -> j-- to 0
                        for(int k = 0; k < sLine.Length; k++) {
                            j++;
                            if(j >= 128) {
                                //next row in image
                                i++;
                                j = 0;
                            }
                            int.TryParse(sLine[k], out value);
                            setValue(img, value, i, j);
                        }
                    } else {
                        //Line could be parsed into one integer
                        setValue(img, value, i, j);
                    }
                    
                }

            if(img.isTooSmall()) return null;

            return img;
        }

        /// <summary>
        /// Creates instance of BWImage from bitmap if possible otherwise returns null
        /// </summary>
        /// <param name="bitmap"></param>
        /// <returns>instance of BWImage or null if instance could not be created</returns>
        public static BWImage createInstanceFromBitmap(Bitmap bitmap) {
            BWImage img = new BWImage();
            
            Boolean empty = true;
            int value;

            for(int i = 0; i < 128; i++)
                for(int j = 0; j < 128; j++) {
                    value = bitmap.GetPixel(j, i).R;
                    setValue(img, value, i, j);

                    if(value < BLACK_PIXEL_TRESHOLD) {
                        empty = false;
                    }
                }

            if(empty || img.isTooSmall()) {
                return null;
            }

            return img;
        }

        /// <summary>
        /// Empty constructor for factory methods t oconstruct instance
        /// </summary>
        private BWImage() {
            
        }
    }
}
