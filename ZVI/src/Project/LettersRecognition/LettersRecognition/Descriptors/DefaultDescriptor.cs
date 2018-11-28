using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LettersRecognition {
    /// <summary>
    /// Descriptor which creates description of image by averaging lines so image is divided into DESCRIPTION_LENGTH sections
    /// </summary>
    public class DefaultDescriptor : IDescriptor {
        /// <summary>
        /// Length of description
        /// </summary>
        public const int DESCRIPTION_LENGTH = 8;

        /// <summary>
        /// Weights used for multiplying final description vector.
        /// </summary>
        #region weight
        private double [] weigth = {
            1,          //volume
            2,          //longitude
            2, 2,       //Center of gravity
            0.5, 0.5,   //LongLine
            2.5,        //Form factor
            3.5         //Holes
        };
        #endregion weight

        /// <summary>
        /// Currently processed image instnce.
        /// </summary>
        private Image img;

        /// <summary>
        /// Variables for recursive spread function.
        /// Variables are here to not have to pass them directly (relieving stack a bit)
        /// </summary>
        #region Spread values
        bool spreadRes = true;
        bool[,] hits;
        int stride;
        byte[] pixels;
        int maxX;
        int maxY;
        #endregion Spred values

        /// <summary>
        /// Returns description for given image.
        /// </summary>
        /// <param name="image">processed image</param>
        /// <returns>description for given image</returns>
        public double[] getDescription(Image image, int index) {
            return getDescription(image.fragments[index], image);
        }

        /// <summary>
        /// Returns description for given fragment.
        /// Appropriate values in description of the fragment are also updated.
        /// </summary>
        /// <param name="fragment">source fragment</param>
        /// <param name="img">parrent image of the fragment</param>
        /// <returns>description vector</returns>
        private double[] getDescription(Image.Fragment fragment, Image img) {
            this.img = img;
            double small;
            double big;
            double longLine;

            double[]dsc = new double[DESCRIPTION_LENGTH];
            dsc[0] = setVolumeDsc(fragment);
            dsc[1] = setLongitude(fragment, out small, out big);
            setCoGCoordintes(fragment, out dsc[2], out dsc[3]);

            longLine = getLongLine(fragment);
            dsc[4] = longLine / small;
            dsc[5] = longLine / big;
            dsc[6] = getCicrle(fragment, longLine);
            dsc[7] = getNumberOfHoles(fragment);

            dsc = useWeight(dsc);

            return dsc;
        }

        /// <summary>
        /// Returns number of holes in given fragment.
        /// Recursive method is used and it's not advised to use it on larger data.
        /// Number of holes attribute in description is updated.
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <returns>number of holes</returns>
        private double getNumberOfHoles(Image.Fragment frg) {
            Bitmap bmp = img.bmp.Clone(
                    new Rectangle(
                        frg.minXdet, frg.minYdet,     //origin
                        frg.maxXdet - frg.minXdet + 1,    //width
                        frg.maxYdet - frg.minYdet + 1),   //height
                    PixelFormat.Format8bppIndexed
                    );
            bmp.Palette = img.bmp.Palette;

            hits = new Boolean[bmp.Width, bmp.Height];
            maxX = bmp.Width - 1;
            maxY = bmp.Height - 1;

            BitmapData bmpData;
            Image.openBitmap8bi(bmp, out stride, out pixels, out bmpData);

            int counter = 0;    //counter for holes
            int value;
            int index;
            int currentLine;
            for(int y = 1; y < bmp.Height - 1; y++) {     //lines
                currentLine = y * stride;
                for(int x = 1; x < bmp.Width - 1; x++) {   //cols
                    index = currentLine + x;
                    value = pixels[index];
                    if(hits[x, y] == false && !Image.isObject(value)) {
                        //no hit && no object
                        spreadRes = true;
                        spread(x, y);
                        if(spreadRes) {
                            //Positive hole
                            counter++;
                        }
                    }

                }
                //end of row
            }

            frg.description.numOfHoles = counter;
            Image.closeBitmap(bmp, bmpData);
            return counter;
        }

        /// <summary>
        /// Returns area of the given fragment over suare of it's circumference all multiplied by 4*Pi.
        /// It's form factor of the object.
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <param name="longLine">diameter of the circle</param>
        /// <returns>area of circle over minimal rectangle</returns>
        private double getCicrle(Image.Fragment frg, double longLine) {
            double val = 4*Math.PI * (frg.allPoints.Count / (double)(frg.edge.Count * frg.edge.Count));
            return val;
        }

        /// <summary>
        /// Finds the lagest distance between two points on the edge of given fragment.
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <returns>lagest distance between two points on the edge</returns>
        private double getLongLine(Image.Fragment frg) {
            double max = 0;
            double val;
            foreach(Point p1 in frg.edge) {
                foreach(Point p2 in frg.edge) {
                    val = (p2.X - p1.X) * (p2.X - p1.X) + (p2.Y - p1.Y) * (p2.Y - p1.Y);
                    if(val > max) max = val;
                }
            }
            return Math.Sqrt(max);
        }

        /// <summary>
        /// Recursive function used to find holes in single fragment. 
        /// Area is not considered as hole if it touches edge of the bitmap (of the examined fragment).
        /// Encountered pixels are marked as hit in the hits array attribute.
        /// If given pixel is on the edge resSpread attribute is set to false.
        /// </summary>
        /// <param name="x">x-coordinate of examined pixel</param>
        /// <param name="y">y-coordinate of examined pixel</param>
        private void spread(int x, int y) {
            if(x == 1 || x == maxX || y == 1 || y == maxY) {
                spreadRes = false;
                hits[x, y] = true;
                return;
            }

            if(hits[x, y]) return;

            hits[x, y] = true;
            int value = pixels[y*stride+x];
            if(Image.isObject(value)) return;

            //check surroundings
            spread(x + 1, y);
            spread(x - 1, y);
            spread(x, y + 1);
            spread(x, y - 1);
        }

        /// <summary>
        /// Multiplies values of description vektor by weights.
        /// </summary>
        /// <param name="dsc">description vector</param>
        /// <returns>multiplyed description vector</returns>
        private double[] useWeight(double[] dsc) {
            for(int i = 0; i < dsc.Length; i++) {
                dsc[i] = dsc[i] * weigth[i];
            }
            return dsc;
        }

        /// <summary>
        /// Finds center of gravity of given fragement.
        /// Values relative to the minimal rectangle for given fragment are updated
        /// (minRecCoGX, minRecCoGY) in description.
        /// Relative coordinates are generated as follows:
        ///     sCoor = {x-coordinate (relative to left lower corner of the rectngle) / Min{width, width (of min rectangle)} * }
        ///     bCoor = {x-coordinate (relative to left lower corner of the rectngle) / Max{width, height (of min rectangle)} * }
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <param name="sCoor">smaller relative coordinate value</param>
        /// <param name="bCoor">bigger relative coordinate value</param>
        private void setCoGCoordintes(Image.Fragment frg, out double sCoor, out double bCoor) {

            Matrix m = new Matrix();
            m.RotateAt(frg.description.angle, frg.description.rotOrigin);
            PointF bp = new PointF(frg.description.balancePoint.X, frg.description.balancePoint.Y);
            PointF[]pts = {
                bp
            };
            m.TransformPoints(pts);
            bp = pts[0];

            PointF lCorner = new PointF(frg.description.minX, frg.description.minY);

            float xr = bp.X - lCorner.X;
            float yr = bp.Y - lCorner.Y;

            frg.description.minRecCoGX = xr / frg.description.width;
            frg.description.minRecCoGY = yr / frg.description.height;

            /*
            sCoor = frg.description.minRecCoGX;
            bCoor = frg.description.minRecCoGY;
            */

            if(frg.description.width < frg.description.height) {
                //width is smaller
                sCoor = frg.description.minRecCoGX;
                bCoor = frg.description.minRecCoGY;
            } else {
                //height is smaller
                bCoor = frg.description.minRecCoGX;
                sCoor = frg.description.minRecCoGY;
            }

        }

        /// <summary>
        /// Returns value calculated as bigger dimension divided by the smaller one.
        /// (Dimensions of the minimal rectangle).
        /// Longitude attribute in description is updated.
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <param name="small">smaller dimension value</param>
        /// <param name="big">bigger dimension value</param>
        /// <returns>longitude of given fragment</returns>
        private double setLongitude(Image.Fragment frg, out double small, out double big) {
            if(frg.description.width > frg.description.height) {
                big = frg.description.width;
                small = frg.description.height;
            } else {
                small = frg.description.width;
                big = frg.description.height;
            }
            double longitude = small/big;
            frg.description.longitude = longitude;
            return longitude;
        }

        /// <summary>
        /// Returns volume of given fragment over volume of it's minimal rectangle.
        /// Volume attribute in description is updated.
        /// Coordinates of Center of Gravity are updated in description.
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <returns>relative volume</returns>
        private double setVolumeDsc(Image.Fragment frg) {
            int stride;
            byte[]pixels;
            BitmapData bmpData;
            Image.openBitmap8bi(img.bmp, out stride, out pixels, out bmpData);

            int index;
            double vol = 0;
            double val;
            double bpX = 0;
            double bpY = 0;

            foreach(Point p in frg.allPoints) {
                index = p.Y * stride + p.X;
                val = pixels[index] / 255.0;
                vol += val;
                bpX += p.X;// * val;
                bpY += p.Y;// * val;
            }

            Image.closeBitmap(img.bmp, bmpData);
            frg.description.volume = vol;   //volume
            vol = vol / (frg.description.width * frg.description.height);   //per min max rectangle volume

            //avg coordinates
            bpX /= frg.allPoints.Count;
            bpY /= frg.allPoints.Count;
            frg.description.balancePoint = new PointF((float)bpX, (float)bpY);

            return vol;
        }

        /// <summary>
        /// Finds agle for minimal rectangle surrounding given fragment by rotation of it's edge points.
        /// Description is updates with values for the rectangle:
        ///     minimal and maximal indexes
        ///     width, height
        ///     angle
        /// </summary>
        /// <param name="frg">source fragment</param>
        /// <param name="image">parrent image of the fragment</param>
        public void setAttributes(Image.Fragment frg, Image image) {
            Description dsc = frg.description;
            float[] mmRec = new float[4];

            dsc.rotOrigin = new Point(
                    (frg.minXdet + frg.maxXdet) / 2,
                    (frg.minYdet + frg.maxYdet) / 2
                    );

            PointF[]pts = new PointF[frg.edge.Count];
            List<PointF> edgeF = frg.edge.ConvertAll<PointF>(
                    element => element
                    );

            float min = float.MaxValue;
            float val;
            int ang = 0;
            Matrix matrix;
            for(int i = 0; i <= 90; i++) {
                edgeF.CopyTo(pts);
                matrix = new Matrix();
                matrix.RotateAt(i, dsc.rotOrigin);
                matrix.TransformPoints(pts);
                val = findMinVolume(pts, mmRec);
                if(val < min) {
                    min = val;
                    ang = i;
                    //update values in dsc
                    dsc.minX = mmRec[0];
                    dsc.maxX = mmRec[1];
                    dsc.minY = mmRec[2];
                    dsc.maxY = mmRec[3];
                }
            }

            dsc.angle = ang;

            //sizes
            dsc.width = dsc.maxX - dsc.minX + 1;
            dsc.height = dsc.maxY - dsc.minY + 1;

            dsc.vektor = getDescription(frg, image);
        }

        /// <summary>
        /// Calculates volume of minimal rectangle containing edge points
        /// </summary>
        /// <param name="pts">Points on edge</param>
        /// <param name="mmRec">Min Max points in x and y axis</param>
        /// <returns>volume of the minimal rectangle</returns>
        private float findMinVolume(PointF[] pts, float[] mmRec) {
            mmRec[0] = float.MaxValue;  //minX
            mmRec[1] = float.MinValue;  //maxX
            mmRec[2] = float.MaxValue;  //MinY
            mmRec[3] = float.MinValue;  //MaxY

            foreach(PointF p in pts) {
                if(p.X < mmRec[0]) mmRec[0] = p.X;
                if(p.X > mmRec[1]) mmRec[1] = p.X;
                if(p.Y < mmRec[2]) mmRec[2] = p.Y;
                if(p.Y > mmRec[3]) mmRec[3] = p.Y;
            }
            return (mmRec[1] - mmRec[0]) * (mmRec[3] - mmRec[2]);
        }

        /// <summary>
        /// Returns length of description
        /// </summary>
        /// <returns>length of description</returns>
        public int getDescriptionVectorLength() {
            return DESCRIPTION_LENGTH;
        }

        /// <summary>
        /// Returns name of this descriptor.
        /// </summary>
        /// <returns>name of this descriptor</returns>
        public string getName() {
            return "DefaultDescriptor";
        }
    }
}
