using System;
using System.Collections;
using System.Drawing;
using System.Text;
using AForge.Video.FFMPEG;




namespace BIT_Steganography_png {
    class Program {
        #region PNG
        //PNG
        private const String DEFAULT_SOURCE     = "source.png";
        private const String DEFAULT_TEXT       = "Testing steganography text. Hello world!";
        private const String DEFAULT_OUTPUT     = "output.png";
        private const int CHAR_SIZE             = 16;                       //Unicode UTF-16
        private const int BYTES_PER_CHAR        = CHAR_SIZE / 8;
        private static char END_SIGN            = char.MaxValue;            //End of message sign
        #endregion

        #region AVI
        private const String DEFAULT_SOURCE_AVI = "drop.avi";
        private const String DEFAULT_OUTPUT_AVI = "out.avi";



        #endregion

        static void Main(string[] args) {
            switch(args.Length) {
                case 0:
                    Console.WriteLine("No arguments used");
                    Console.WriteLine(
                        "Input format: " +
                        "BIT_Steganography.exe [mod] " + "\n" +
                        "   [mod]   -png    steganography for png" + "\n" +
                        "           -avi    steganograpy for avi" + "\n"
                        );
                    break;
                case 1:
                    switch(args[0]) {
                        case "-png":
                            pngMod(args); break;
                        case "-avi":
                            aviMod(args); break;

                        default:
                            Console.WriteLine("Incorrect [mod] argument \n" +
                                "for help type: BIT_Steganography.exe"
                                ); break;
                    }
                    break;
            }

            //END
            Console.ReadKey();
        }

        #region PngActions
        private static void pngDefaultInsert() {
            Console.WriteLine("Default insert message PNG");
            Console.WriteLine(
                        " Settings: \n" +
                        "   source file: " + DEFAULT_SOURCE + "\n" +
                        "   output file: " + DEFAULT_OUTPUT + "\n" +
                        "   message:     " + DEFAULT_TEXT
                        );

            pngInsert(DEFAULT_SOURCE, DEFAULT_OUTPUT, DEFAULT_TEXT);
        }

        private static void pngDefaultExtract() {
            Console.WriteLine("Default extract message PNG");
            Console.WriteLine(
                        " Settings: \n" +
                        "   source file: " + DEFAULT_OUTPUT + "\n" +
                        "   message:     " + "Testing steganography text. Hello world!"
                        );

            pngExtract(DEFAULT_SOURCE);
        }

        

        private static void pngMod(string[] args) {
            switch(args.Length) {
                case 1:
                    pngDefaultInsert();
                    pngDefaultExtract();
                    break;
                case 3:
                    pngExtract(args[2]); break;
                case 5:
                    pngInsert(args[3], args[4], args[5]); break;

                default:
                    Console.WriteLine("Wrong number of arguments");
                    Console.WriteLine(
                            "Input format: " +
                            "BIT_Steganography.exe -png [mod] [\"input\"] [\"output\"] [\"msg\"]" + "\n" +
                            "   [mod]   -i  insert message into png" + "\n" +
                            "               require: [\"input\"] [\"output\"] [\"msg\"] " + "\n" +
                            "           -e  extract message from png" + "\n" +
                            "               require: [\"input\"]" + "\n" +
                            "   \"input\"   filename for input" + "\n" +
                            "   \"output\"  filename for generated file" + "\n" +
                            "   \"msg\"     message to be encrypted into input file" + "\n"
                            );
                    break;
            }
        }


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

        private static Bitmap bitmapFromFile(String file) {
            Bitmap bmp = null;
            try {
                bmp = new Bitmap(file);
            }catch(Exception e) {
                Console.WriteLine("File not found! " + file);
                Environment.Exit(-1);
            }
            return bmp;
        }

        private static void insertMessage(Bitmap bmp, String text) {
            text += END_SIGN;      //add end of message

            byte[] textByte = Encoding.Unicode.GetBytes(text);
            BitArray bArr = new BitArray(textByte);

            int bArrIdx = 0;
            int[] argb = new int[4];
            int maskZero = 255 - 1;                //...11110
            int maskOne = 1;                       //...00001

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
                        if(bArr[bArrIdx] == true) {
                            argb[k] = argb[k] | maskOne;
                        } else {
                            argb[k] = argb[k] & maskZero;
                        }
                        bArrIdx++;  //next bit
                        if(bArrIdx >= bArr.Count) break;
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

        private static string extractMessage(Bitmap bmp) {
            String src = "";
            String srcByte = "";
            char ch = '0';

            int[] argb = new int[4];
            int maskOne = 1;                       //...00001
            int bitCnt = 0;

            for(int i = 0; i < bmp.Height; i++) {       //row
                for(int j = 0; j < bmp.Width; j++) {    //column
                    Color px = bmp.GetPixel(j, i);
                    argb[1] = px.R;
                    argb[2] = px.G;
                    argb[3] = px.B;

                    for(int k = 1; k < argb.Length; k++) {
                        //Console.WriteLine("Binary r:  " + Convert.ToString(argb[k], 2).PadLeft(8, '0'));
                        if((argb[k] & maskOne) == 1) {
                            srcByte += "1";
                        }else {
                            srcByte += "0";
                        }
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

        private static char convertToText(string srcByte) {
            int value = 0;
            for(int i = srcByte.Length-1; i >= 0; i--) {
                if(srcByte[i] == '1') {
                    value = value << 1;
                    value |= 1;
                }else {
                    value = value << 1;
                }
            }

            return (char)value;
        }

        private static void saveBitmapAsPng(Bitmap bmp, String output) {
            bmp.Save(output);
        }

#endregion

        private static void aviMod(string[] args) {
            aviDefaultInsert();
            aviDefaultExtract();
        }

        private static void aviDefaultInsert() {
            Console.WriteLine("avi insert");
            aviInsertMessage(DEFAULT_SOURCE_AVI, DEFAULT_OUTPUT_AVI,DEFAULT_TEXT);
        }

        private static void aviDefaultExtract() {
            throw new NotImplementedException();
        }

        private static void aviInsertMessage(String input, String output, String msg) {
            
            VideoFileReader vfr = new VideoFileReader();
            vfr.Open(input);
            /*Console.WriteLine(
                "Video attributes: \n"+
                "   width:  "+vfr.Width+
                "   height: "+vfr.Height+
                "   codec:  "+vfr.CodecName+
                "   frames: "+vfr.FrameCount);
                */
            //vfr.Close();
            
        }
    }
}
