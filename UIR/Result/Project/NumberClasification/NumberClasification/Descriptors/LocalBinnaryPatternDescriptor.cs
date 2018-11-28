using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    static class ByteExtension {
        public static byte RotateLeft(
                                this byte value,
                                int count) {
           
            count &= 0x07;
            return (byte)((value << count) | (value >> (8 - count)));
        }
    }

    class LocalBinnaryPatternDescriptor : IDescriptor {
        public const int EXAMINED_ROWS = 20;
        public const int EXAMINED_COLUMNS = 20;
        public const int DESCRIPTION_LENGTH = 36;   //precalculated value

        private int[] rotations = new int[DESCRIPTION_LENGTH];
        Dictionary<int, int> spectrumIndexes;
        

        public double[] getDescription(BWImage image) {
            int[,] bmp = image.getBitmap();
            double[] result = new double[DESCRIPTION_LENGTH];
            int maxY = image.getMaxY();
            int maxX = image.getMaxX();

            int value;
            int indexSpec;
            double[] desc = new double[DESCRIPTION_LENGTH];

            for(int i = image.getMinY() + 1; i < maxY - 1; i+=2)
                for(int j = image.getMinX() + 1; j < maxX - 1; j+=2) {

                    value = getLBPSample(image.getBitmap(), i, j);
                    if(!spectrumIndexes.TryGetValue(value, out indexSpec)) {
                        Console.WriteLine("INDEX(" + value + ") DOES NOT EXIST IN SPECTRUM DICTIONARY");
                        Environment.Exit(0);
                    }

                    desc[indexSpec]++;

                }

            return desc;
        }

        /*
        public double[] getDescription(BWImage image) {
            int[,] bmp = image.getBitmap();
            double[] result = new double[DESCRIPTION_LENGTH];

            int maxY = image.getMaxY();
            int maxX = image.getMaxX();

            int[] examRows, examCols;
            getRowColSamples(image, out examRows, out examCols);

            int efectiveLengthX = image.getEfectiveLengthX();

            //Dictionary<int, int> spectrum = new Dictionary<int, int>();
            double[] desc = new double[DESCRIPTION_LENGTH];
            int value;
            int indexSpec;

            for(int i = 0; i < examRows.Length; i++)
                for(int j = 0; j < examCols.Length; j++) {
                    value = getLBPSample(image.getBitmap(), examRows[i], examCols[j]);
                    if(!spectrumIndexes.TryGetValue(value, out indexSpec)) {
                        Console.WriteLine("INDEX("+value+") DOES NOT EXIST IN SPECTRUM DICTIONARY");
                        Environment.Exit(0);
                    }

                    desc[indexSpec]++;
                }
            
            return desc;
        }
        */

        private int getLBPSample(int[,] bmp, int y, int x) {
            //int lbp = 0;
            byte lbp = 0;
            
            for(int i = 0; i < 3; i++)
                for(int j = 0; j < 3; j++) {

                    if(x == x-1+j && y == y-1+i) continue;

                    if(bmp[y-1+i, x-1+j] > bmp[y, x]) {
                        //examined value is greater than middle
                        lbp <<= 1;
                        lbp += 1;
                    } else {
                        lbp <<= 1;
                    }
                }

            return getMinByRotation(lbp);
        }

        private int getMinByRotation(byte lbp) {
            //all rotation posibilities
            int min = lbp;  //value of min number from rotations
            for(int i = 0; i < 8; i++) {
                lbp = ByteExtension.RotateLeft(lbp, 1);
                if(lbp < min) min = lbp;
            }
            if(min >= 255) return 0;
            return min;
        }

        public void getRowColSamples(BWImage image, out int[] rows, out int[] cols) {

            int descLengthY = image.getEfectiveLengthY();
            int descLengthX = image.getEfectiveLengthX();

            rows = new int[EXAMINED_ROWS];
            cols = new int[EXAMINED_COLUMNS];

            double exRowStep = (double)descLengthY / (double)EXAMINED_ROWS;
            double exColStep = (double)descLengthX / (double)EXAMINED_COLUMNS;

            for(int i = 0; i < rows.Length; i++) {
                rows[i] = (int)(i * exRowStep + exRowStep / 2 + image.getMinY());
                //Console.WriteLine("\trow" + i + " " + rows[i]);
            }

            for(int i = 0; i < cols.Length; i++) {
                cols[i] = (int)(i * exColStep + exColStep / 2 + image.getMinX());
                //Console.WriteLine("\tcol" + i + " " + cols[i]);
            }

            rows[0]++;
            cols[0]++;
            rows[rows.Length - 1]++;
            cols[cols.Length - 1]++;
        }

        public int getDescriptionVectorLength() {
            return DESCRIPTION_LENGTH;
        }

        private void fillRotations() {
            spectrumIndexes = new Dictionary<int, int>();
            int value;
            int index = 0;
            for(byte i = 0; i < byte.MaxValue; i++) {
                value = getMinByRotation(i);
                if(!spectrumIndexes.ContainsKey(value)) {
                    //add new key value to the spectrum
                    spectrumIndexes.Add(value, index);
                    index++;
                }
            }
            /*
            foreach(KeyValuePair<int, int> entry in spectrumIndexes) {
                Console.WriteLine(entry.Key + " : " + entry.Value);
            }
            Console.WriteLine("Size: "+spectrumIndexes.Count);
            */
        }


        public LocalBinnaryPatternDescriptor() {
            fillRotations();
        }
    }
}
