using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Descriptor which creates description based on konstant number of sample in rows and columns
    /// </summary>
    class SamplesDescriptor : IDescriptor {
        /// <summary>
        /// Number of samples in rows
        /// </summary>
        public const int EXAMINED_ROWS = 15;

        /// <summary>
        /// Number of samples in columns
        /// </summary>
        public const int EXAMINED_COLUMNS = 10;

        /// <summary>
        /// Description length
        /// </summary>
        private int DESCRIPTION_LENGTH;

        /// <summary>
        /// Creates description for given image by averaging in sample rows and columns
        /// from each sample is made one value which is placed into final description vector
        /// </summary>
        /// <param name="image">processed image</param>
        /// <returns>description for given image</returns>
        public double[] getDescription(BWImage image) {
            int[,] bmp = image.getBitmap();
            double[] result = new double[DESCRIPTION_LENGTH];
                //average for rows : result[] from 0 to number of examined rows
                //average for cols : result[] from "number of examined rows" to number of examined columns

            int maxY = image.getMaxY();
            int maxX = image.getMaxX();

            int[] examinedRows, examinedCols;
            getSamples(image, out examinedRows, out examinedCols);

            int k = 0;
            //rows
            for(int i = 0; i < examinedRows.Length; i++) {
                for(int j = image.getMinX(); j < maxX; j++) {
                    result[k] += bmp[examinedRows[i], j];
                }
                result[k] /= maxX - image.getMinX();      //average
                k++;
            }

            //cols
            for(int i = 0; i < examinedCols.Length; i++) {
                for(int j = image.getMinY(); j < maxY; j++) {
                    result[k] += bmp[j, examinedCols[i]];
                }
                result[k] /= maxY - image.getMinY();      //average
                k++;
            }

            return result;
        }

        /// <summary>
        /// Fills given arrays with values coresponding to original image rows and oclumns indexes at which
        /// will be commited sampling
        /// </summary>
        /// <param name="image">processed image</param>
        /// <param name="rows">array of row indexes for sampling</param>
        /// <param name="cols">array of columns indexes for sampling</param>
        public void getSamples(BWImage image, out int[] rows, out int[] cols) {

            int descLengthY = image.getEfectiveLengthY();
            int descLengthX = image.getEfectiveLengthX();

            rows = new int[EXAMINED_ROWS];
            cols = new int[EXAMINED_COLUMNS];

            double exRowStep = (double)descLengthY / (double)EXAMINED_ROWS;
            double exColStep = (double)descLengthX / (double)EXAMINED_COLUMNS;

            for(int i = 0; i < rows.Length; i++) {
                rows[i] = (int)(i * exRowStep + exRowStep/2 + image.getMinY());
            }

            for(int i = 0; i < cols.Length; i++) {
                cols[i] = (int)(i * exColStep + exColStep/2 + image.getMinX());
            }
        }

        /// <summary>
        /// Returns length of description
        /// </summary>
        /// <returns>length of description</returns>
        public int getDescriptionVectorLength() {
            return DESCRIPTION_LENGTH;
        }

        /// <summary>
        /// Creates new descriptor and calculates length of description
        /// </summary>
        public SamplesDescriptor() {
            DESCRIPTION_LENGTH = EXAMINED_COLUMNS + EXAMINED_ROWS;
        }
    }
}
