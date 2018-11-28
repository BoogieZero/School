using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Descriptor which creates description based on histogram of oriented gradients
    /// </summary>
    class HOGDescriptor : IDescriptor {
        /// <summary>
        /// Number of buckets in column
        /// </summary>
        public const int BUCKET_ROWS = 5;

        /// <summary>
        /// Number of buckets in row
        /// </summary>
        public const int BUCKET_COLUMNS = 3;

        /// <summary>
        /// Nuber of discretized angles from 180°
        /// </summary>
        public const int HISTOGRAM_ANGLES = 8;

        /// <summary>
        /// Constant for converting radians to degrees
        /// </summary>
        public const double RAD_TO_DEG = 180 / Math.PI;

        /// <summary>
        /// Constant for converting degrees to radians
        /// </summary>
        public const double DEG_TO_RAD = Math.PI / 180;

        /// <summary>
        /// Step in angle for dividing 180°°into HISTOGRAM_ANGLES sections
        /// </summary>
        public const double ANGLE_STEP = Math.PI / HISTOGRAM_ANGLES;

        /// <summary>
        /// Number of bucket over all in image
        /// </summary>
        public const int NUMBER_OF_BUCKETS = BUCKET_ROWS * BUCKET_COLUMNS;

        /// <summary>
        /// Description length
        /// </summary>
        private const int DESCRIPTION_LENGTH = NUMBER_OF_BUCKETS * HISTOGRAM_ANGLES;

        /// <summary>
        /// Buckets for actually processed image
        /// </summary>
        private Bucket[] buckets;

        /// <summary>
        /// Number of lines to step over to next row of buckets
        /// </summary>
        private double rowStep;

        /// <summary>
        /// Number of lines to step over to next column of buckets
        /// </summary>
        private double colStep;
        
        /// <summary>
        /// Creates description from given image by calculating histogram of gradienst for each bucket
        /// and by serializing those histograms gets description
        /// </summary>
        /// <param name="image">processed image</param>
        /// <returns>description for given image</returns>
        public double[] getDescription(BWImage image) {
            int[,] bmp = image.getBitmap();
            
            int maxX = image.getMaxX();
            int minX = image.getMinX();
            int maxY = image.getMaxY();
            int minY = image.getMinY();

            setSteps(image);

            buckets = new Bucket[NUMBER_OF_BUCKETS];
            for(int i = 0; i < buckets.Length; i++)
                buckets[i] = new Bucket();
            
            int bucketIndex;
            for(int i = image.getMinY() + 1; i <= maxY - 1; i++) {
                for(int j = image.getMinX() + 1; j <= maxX - 1; j++) {
                    //every pixel in MinMax rectangle (except borders)
                    bucketIndex = getIndexOfBucket(image, i, j);
                    contributeToBucket(image.getBitmap(), i, j, buckets[bucketIndex]);
                }
            }

            return getDescriptionFromBuckets(buckets);
        }

        /// <summary>
        /// Calculates gradient in pixel at x, y coordinates and contributes to two nearest angles by weighted magnitude
        /// </summary>
        /// <param name="bmp">processed image</param>
        /// <param name="y">Y coordinates for pixel in bitmap</param>
        /// <param name="x">X coordinates for pixel in bitmap</param>
        /// <param name="bucket">actual bucket</param>
        private void contributeToBucket(int[,] bmp, int y, int x, Bucket bucket) {
            double dx = bmp[y, x + 1] - bmp[y, x - 1];
            double dy = bmp[y + 1, x] - bmp[y - 1, x];

            double magnitude = Math.Sqrt(dx * dx + dy * dy);
            double orientation = Math.Abs(Math.Atan2(dy, dx));

            double indexOfBucket = orientation / ANGLE_STEP;
            if(indexOfBucket == HISTOGRAM_ANGLES) indexOfBucket = HISTOGRAM_ANGLES-1; 

            int index = (int)indexOfBucket;
            double fraction = indexOfBucket - index;

            if(index != bucket.histogram.Length - 1) {
                bucket.histogram[index] += (1 - fraction) * magnitude;
                bucket.histogram[index + 1] += fraction * magnitude;
            } else {
                //last index
                bucket.histogram[index] += magnitude;
            }
            bucket.contributed++;
        }

        /// <summary>
        /// Fills final description by histograms which are normalized by number of contributions to coresponding bucket
        /// </summary>
        /// <param name="buckets"></param>
        /// <returns></returns>
        private double[] getDescriptionFromBuckets(Bucket[] buckets) {
            double[] result = new double[DESCRIPTION_LENGTH];
            int i = 0;
            foreach(Bucket bucket in buckets) {
                foreach(double value in bucket.histogram) {
                    result[i] = value/bucket.contributed;
                    i++;
                }
            }
            return result;
        }

        /// <summary>
        /// Returns index of coresponding bucket to pixel given by x, y coordinates
        /// </summary>
        /// <param name="img">processed image</param>
        /// <param name="y">Y coordinates for pixel in bitmap</param>
        /// <param name="x">X coordinates for pixel in bitmap</param>
        /// <returns>index of coresponding bucket</returns>
        public int getIndexOfBucket(BWImage img, int y, int x) {
            int bucketY = (int)((y - img.getMinY()) / rowStep);
            int bucketX = (int)((x - img.getMinX()) / colStep);
            return bucketY * BUCKET_COLUMNS + bucketX;
        }

        /// <summary>
        /// Calculates steps for dividing image into buckets
        /// </summary>
        /// <param name="image">processed image</param>
        public void setSteps(BWImage image) {

            int descLengthY = image.getEfectiveLengthY();
            int descLengthX = image.getEfectiveLengthX();

            rowStep = (double)descLengthY / (double)BUCKET_ROWS;
            colStep = (double)descLengthX / (double)BUCKET_COLUMNS;
        }

        /// <summary>
        /// Calculates steps for dividing image into buckets
        /// </summary>
        /// <param name="image">processed image</param>
        /// <param name="rowStep">step in rows</param>
        /// <param name="colStep">step in columns</param>
        public void setSteps(BWImage image, out double rowStep, out double colStep) {
            int descLengthY = image.getEfectiveLengthY();
            int descLengthX = image.getEfectiveLengthX();

            rowStep = (double)descLengthY / (double)BUCKET_ROWS;
            colStep = (double)descLengthX / (double)BUCKET_COLUMNS;
        }

        /// <summary>
        /// Returns description for given image
        /// </summary>
        /// <returns>length of description</returns>
        public int getDescriptionVectorLength() {
            return DESCRIPTION_LENGTH;
        }

        /// <summary>
        /// Represent bucket for histograms in image
        /// </summary>
        class Bucket {
            /// <summary>
            /// Number used for counting contributions to this bucket
            /// </summary>
            public int contributed = 0;

            /// <summary>
            /// Histogram for this bucket
            /// </summary>
            public double[] histogram = new double[HISTOGRAM_ANGLES];
        }
    }
}
