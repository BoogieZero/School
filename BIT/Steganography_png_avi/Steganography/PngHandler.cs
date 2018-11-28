using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Steganography {
    /// <summary>
    /// Provides functionality for hiding message to .png files based on given arguments. 
    /// With single argument -png uses default settings (input, output, message)
    /// </summary>
    class PngHandler {
        #region bit masks
        //masks are meant for use on char (16 bits)
        private const int MASK_ZERO =   char.MaxValue - 1;  //...11110
        private const int MASK_ONE =    1;                  //...00001
        #endregion

        #region PNG
        /// <summary>
        /// Char size in bits 16 for Unicode UTF-16.
        /// </summary>
        private const int CHAR_SIZE =           16;
        /// <summary>
        /// Bytes per char.
        /// </summary>
        private const int BYTES_PER_CHAR =      CHAR_SIZE / 8;
        /// <summary>
        /// Sign marking end of message.
        /// </summary>
        private static char END_SIGN =          char.MaxValue;            //End of message sign
        #endregion

        #region default
        /// <summary>
        /// Default source file.
        /// </summary>
        private const String DEFAULT_SOURCE =   "source.png";
        /// <summary>
        /// Default message file.
        /// </summary>
        private const String DEFAULT_MSG =      "msg.txt";
        /// <summary>
        /// Default output file
        /// </summary>
        private const String DEFAULT_OUTPUT =   "output.png";

        /// <summary>
        /// Calls pngInsert() with default settings
        /// </summary>
        private static void pngDefaultInsert() {
            Console.WriteLine("Default insert message into PNG");
            Console.WriteLine(
                        " Settings: \n" +
                        "   source file: " + DEFAULT_SOURCE + "\n" +
                        "   output file: " + DEFAULT_OUTPUT + "\n" +
                        "   message:     " + DEFAULT_MSG
                        );

            pngInsert(DEFAULT_SOURCE, DEFAULT_OUTPUT, MsgLoader.loadMessage(DEFAULT_MSG));
        }

        /// <summary>
        /// Calls pngExtract() with default settings
        /// </summary>
        private static void pngDefaultExtract() {
            Console.WriteLine("Default extract message from PNG");
            Console.WriteLine(
                        " Settings: \n" +
                        "   source file: " + DEFAULT_OUTPUT + "\n" +
                        "   message:     " + DEFAULT_MSG
                        );

            pngExtract(DEFAULT_OUTPUT);
        }

        #endregion

        /// <summary>
        /// Calls specific functions based on given settings or shows help.
        /// </summary>
        /// <param name="args">settings for png steganograpy</param>
        public static void pngMod(string[] args) {
            switch(args.Length) {
                case 1:
                case 2:
                    pngDefaultInsert();
                    pngDefaultExtract();
                    break;
                case 3:
                    pngExtract(args[2]);
                    break;
                case 5:
                    pngInsert(args[2], args[3], MsgLoader.loadMessage(args[4]));
                    break;

                default:
                    Console.WriteLine("Wrong number of arguments");
                    Console.WriteLine(
                            "Input format: " +
                            "Steganography.exe -png [mod] [\"input\"] [\"output\"] [\"msg\"]" + "\n" +
                            "   [mod]   -i  insert message into png" + "\n" +
                            "               require: [\"input\"] [\"output\"] [\"msg\"] " + "\n" +
                            "           -e  extract message from png" + "\n" +
                            "               require: [\"input\"]" + "\n" +
                            "   \"input\"   filename for input" + "\n" +
                            "   \"output\"  filename for generated file" + "\n" +
                            "   \"msg\"     file with message to be encrypted into input file" + "\n"
                            );
                    break;
            }
        }

        /// <summary>
        /// Hides given message into image source and result is saved as output
        /// </summary>
        /// <param name="source">source file</param>
        /// <param name="output">output file</param>
        /// <param name="msg">message to hide</param>
        private static void pngInsert(string source, string output, string msg) {
            Bitmap bmp;
            bmp = bitmapFromFile(source);
            Console.WriteLine("Inserting message...");
            insertMessage(bmp, msg);
            Console.WriteLine("Saving bitmap...");
            saveBitmapAsPng(bmp, output);
            Console.WriteLine("Done");
            bmp.Dispose();
        }

        /// <summary>
        /// Extracts hidden message from given image
        /// </summary>
        /// <param name="input">source file</param>
        private static void pngExtract(string input) {
            Bitmap bmp;
            bmp = bitmapFromFile(input);
            String msg;
            Console.WriteLine("Exctracting message...");
            msg = extractMessage(bmp);
            Console.WriteLine("Exctracted message: \n" + msg);
            bmp.Dispose();
            Console.WriteLine("Done");
            bmp.Dispose();
        }

        /// <summary>
        /// Gets bitmap from given image file
        /// </summary>
        /// <param name="file">source file</param>
        /// <returns>bitmap from file</returns>
        private static Bitmap bitmapFromFile(String file) {
            Bitmap bmp = null;
            try {
                bmp = new Bitmap(file);
            } catch(Exception e) {
                Console.WriteLine("File not found! " + file);
                Environment.Exit(-1);
            }
            return bmp;
        }

        /// <summary>
        /// Iterates through bitmp bmp and hides given message in LSB of color components for each pixel.
        /// Method checks if message isn't too long with appropriate output to console and terminates applcation if so.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <param name="text">message to hide in the bitmap</param>
        private static void insertMessage(Bitmap bmp, String text) {
            text += END_SIGN;      //add end of message

            byte[] textByte = Encoding.Unicode.GetBytes(text);
            BitArray bArr = new BitArray(textByte);

            //Message length test
            int available = (bmp.Height * bmp.Width * 3)/CHAR_SIZE -1; //bits

            if(text.Length > available) {//////reverse
                Console.WriteLine(
                    "Message is too long. " +
                    "\n Requested:      " + (text.Length + 1) + " characters" +
                    "\n Availble space: " + available + " characters."
                    );
                Environment.Exit(-1);
            }

            int bArrIdx = 0;
            int[] argb = new int[4];

            //image
            for(int i = 0; i < bmp.Height; i++) {       //row
                for(int j = 0; j < bmp.Width; j++) {    //column
                    Color px = bmp.GetPixel(j, i);
                    argb[0] = px.A;
                    argb[1] = px.R;
                    argb[2] = px.G;
                    argb[3] = px.B;

                    //Each color
                    for(int k = 1; k < argb.Length; k++) {
                        argb[k] = embedLastBit(argb[k], bArr, bArrIdx);
                        bArrIdx++;  //next bit

                        if(bArrIdx >= bArr.Count)
                            break;
                    }

                    bmp.SetPixel(j, i, Color.FromArgb(argb[0], argb[1], argb[2], argb[3]));
                    if(bArrIdx >= bArr.Count) {
                        //End of message
                        return;
                    }
                }
            }
            //End of image
            Console.WriteLine("Image full");
        }

        /// <summary>
        /// Iterates through bitmap end extracts hiden message from LSB of color components in each pixel.
        /// </summary>
        /// <param name="bmp">source bitmap</param>
        /// <returns>message extracted from bitmap</returns>
        private static string extractMessage(Bitmap bmp) {
            String src = "";
            String srcByte = "";
            char ch = '0';

            int[] argb = new int[4];
            int bitCnt = 0;

            for(int i = 0; i < bmp.Height; i++) {       //row
                for(int j = 0; j < bmp.Width; j++) {    //column
                    Color px = bmp.GetPixel(j, i);
                    argb[1] = px.R;
                    argb[2] = px.G;
                    argb[3] = px.B;

                    for(int k = 1; k < argb.Length; k++) {
                        srcByte += getLastBit(argb[k]);
                        bitCnt++;
                        if(bitCnt >= CHAR_SIZE) {
                            ch = convertToText(srcByte);
                            if(ch == END_SIGN) {
                                //End of message
                                return src;
                            }
                            //Add char to result
                            src += ch;
                            bitCnt = 0;
                            srcByte = "";
                        }
                    }
                }
            }
            Console.WriteLine("End of image");
            return src;
        }

        /// <summary>
        /// Saves given bitmap with name specified by output
        /// </summary>
        /// <param name="bmp"></param>
        /// <param name="output"></param>
        private static void saveBitmapAsPng(Bitmap bmp, String output) {
            bmp.Save(output);
        }

        /// <summary>
        /// Sets last bit in source to 1 or 0 based on boolean value in BitArray bArr on index index.
        /// </summary>
        /// <param name="source">source for changing</param>
        /// <param name="bArr">bit array (boolean)</param>
        /// <param name="index">index in bit array</param>
        /// <returns>source with modified last bit</returns>
        private static int embedLastBit(int source, BitArray bArr, int index) {
            if(bArr[index]) {
                //1
                source = source | MASK_ONE;
            } else {
                //0
                source = source & MASK_ZERO;
            }
            
            return source;
        }

        /// <summary>
        /// Returns last bit (LSB) in source
        /// </summary>
        /// <param name="source">source</param>
        /// <returns>LSB in source</returns>
        private static int getLastBit(int source) {
            if((source & MASK_ONE) == 1) {
                return 1;
            } else {
                return 0;
            }
        }

        /// <summary>
        /// Converts bits represented by 1 and 0 in string srcByte to unicode char.
        /// Bits in srcByte are in reversed order.
        /// </summary>
        /// <param name="srcByte"></param>
        /// <returns></returns>
        private static char convertToText(string srcByte) {
            int value = 0;
            for(int i = srcByte.Length - 1; i >= 0; i--) {
                if(srcByte[i] == '1') {
                    value = value << 1;
                    value |= 1;
                } else {
                    value = value << 1;
                }
            }

            return (char)value;
        }
    }
}
