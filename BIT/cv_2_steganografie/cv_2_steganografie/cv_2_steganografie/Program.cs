using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace cv_2_steganografie {
    static class Program {

        private const string SRC_IMG = "obr2.bmp";
        private const int MSG_LENGTH = 500*8;       //in bits
        private const int BMP_HEADER_OFFSET = 54;

        private static Bitmap bmp;

        /// <summary>
        /// Attachment of console to windows forms apllication
        /// </summary>
        /// <param name="dwProcessId"></param>
        /// <returns></returns>
        [DllImport("kernel32.dll")]
        static extern bool AttachConsole(int dwProcessId);
        private const int ATTACH_PARENT_PROCESS = -1;

        private static void dumbWay() {
            byte[] arr = new byte[MSG_LENGTH];
            String msg = "";

            using(BinaryReader rd = new BinaryReader(new FileStream(SRC_IMG, FileMode.Open))) {
                rd.BaseStream.Seek(BMP_HEADER_OFFSET, SeekOrigin.Begin);
                rd.Read(arr, 0, MSG_LENGTH);
                rd.Close();
            }

            
            int ch = 0;
            int chCnt = 0;
            char c;
            for(int i=0; i < arr.Length; i++) {
                ch = ch << 1;
                ch += (arr[i] & 0x1);
                chCnt++;
                if(chCnt >= 8) {
                    c = Convert.ToChar(ch);
                    msg += c;
                    if(c == '\n') break;
                    ch = 0;
                    chCnt = 0;
                }
            }
            Console.WriteLine("Message: \n" + msg);
            
        }

        private static void processImage() {

            dumbWay();

            BitmapData bd = null;
            try {
                bd = bmp.LockBits(new Rectangle(0, 0, bmp.Width, bmp.Height), ImageLockMode.ReadOnly, PixelFormat.Format24bppRgb);
            } catch(Exception exc) {
                Console.WriteLine(exc.Message);
                Environment.Exit(0);
            }
            int bytes = bd.Stride * bmp.Height;

            /*
            byte sr = 255;
            byte ch = 0;
            ch = (byte)(sr & 0x002);
            */
            byte ch = 0;
            int bCnt = 0;
            String msg = "";

            unsafe
            {
                byte* ptr = null;
                byte* lnptr = (byte*)bd.Scan0;

                for(int i = 0; i < bd.Height; ++i) {    //row
                    ptr = lnptr;
                    for(int j = 0; j < bd.Width; ++j) { //column

                        for(int k = 0; k < 3; k++) {    //color in pixel (3 bytes)
                            ch = (byte)(ch << 1);
                            ch += (byte)(*ptr & 0x1);
                            //Console.WriteLine("char: " + ch);
                            ptr += 1;
                            bCnt++;
                            if(bCnt > 8) {
                                msg += Convert.ToChar(ch);
                                bCnt = 0;
                                ch = 0;
                                Console.WriteLine("msg: " + msg);
                            }

                            /*
                            Console.WriteLine(" src: "+ Convert.ToString(ch, 2));
                            Console.WriteLine(" dst: "+ Convert.ToString(*ptr, 2));
                            ch = ch << 1;
                            ch += (*ptr & 0x1);
                            chCnt++;
                            ptr += 1;
                            Console.WriteLine("res: " + String(ch));
                            */
                        }
                        //c.b = (float)(*ptr) / 255.0f;
                        //c.g = (float)(*(ptr + 1)) / 255.0f;
                        //c.r = (float)(*(ptr + 2)) / 255.0f;
                        //c.a = (float)(*(ptr + 3)) / 255.0f;

                        //rv[i, j] = c;
                        
                        //ptr += 4; //ARGB = 4 bytes
                    }
                    lnptr += bd.Stride;
                }

            }

            bmp.UnlockBits(bd);
            
        }

        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main() {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            Console.WriteLine("Start");
            dumbWay();
            //loadImage();
            //processImage();
            //Application.Run(new Form1());
        }

        private static void loadImage() {
            Image img = null;
            try {
                img = Image.FromFile(SRC_IMG, true);
            }catch(Exception e) {
                Console.WriteLine(SRC_IMG + " failed to load!");
                Environment.Exit(0);
            }
            Console.WriteLine("Image loaded");
            if(img.PixelFormat != PixelFormat.Format24bppRgb) {
                Console.WriteLine("Unsupported pixel format! (" + img.PixelFormat + ")");
                Environment.Exit(0);
            }else {
                Console.WriteLine("\tPixel format: " + img.PixelFormat);
            }
            bmp = (Bitmap)img;
        }
    }
}
