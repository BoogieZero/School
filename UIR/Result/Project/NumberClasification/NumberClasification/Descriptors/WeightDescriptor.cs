using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Descriptor which creates description of image by averaging lines so image is divided into DESCRIPTION_LENGTH sections
    /// </summary>
    class WeightDescriptor : IDescriptor {
        /// <summary>
        /// Length of description
        /// </summary>
        public const int DESCRIPTION_LENGTH = 4;

        /// <summary>
        /// Returns description normalized to DESCRIPTION_LENGTH
        /// </summary>
        /// <param name="origDesc">original description</param>
        /// <param name="image">processed image</param>
        /// <returns>final description for given image</returns>
        private double[] getNormalizedDescription(double[] origDesc, BWImage image) {
            double[] result = new double[DESCRIPTION_LENGTH];

            int step = getStep(image);

            int k = 0;
            int i = 0;
            int stepCount = 0;
            
            while(i < result.Length - 1) {
                result[i] += origDesc[k];
                k++;
                stepCount++;
                if(stepCount >= step) {
                    result[i] /= stepCount;     //average
                    i++;
                    stepCount = 0;
                }
            }

            //last element could have lmore numbers than others
            while(k < origDesc.Length) {
                result[i] += origDesc[k];
                k++;
                stepCount++;
            }

            result[i] /= stepCount;

            return result;
        }

        /// <summary>
        /// Returns number of lines to be averaged according to DESCRIPTION_LENGTH and 
        /// original minMax rectangle length
        /// </summary>
        /// <param name="image">processed image</param>
        /// <returns></returns>
        public int getStep(BWImage image) {
            return (image.getEfectiveLengthY() / DESCRIPTION_LENGTH);
        }
        
        /// <summary>
        /// Returns description for given image
        /// </summary>
        /// <param name="image">processed image</param>
        /// <returns>description for given image</returns>
        public double[] getDescription(BWImage image) {

            int[,] bmp = image.getBitmap();
   
            int rowSum = 0;
            int maxY = image.getMaxY();
            int maxX = image.getMaxX();
            int descLengthY = image.getEfectiveLengthY();
            int descLengthX = image.getEfectiveLengthX();

            double[] descOrig = new Double[descLengthY];

            int k = 0;
            for(int i = image.getMinY(); i <= maxY; i++) {
                for(int j = image.getMinX(); j <= maxX; j++) {
                    rowSum += bmp[i, j];
                }
                descOrig[k] = rowSum / descLengthX;
                k++;
                rowSum = 0;
            }

            return getNormalizedDescription(descOrig, image);
        }

        /// <summary>
        /// Returns length of description
        /// </summary>
        /// <returns>length of description</returns>
        public int getDescriptionVectorLength() {
            return DESCRIPTION_LENGTH;
        }
    }
}
