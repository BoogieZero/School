using AForge.Video.FFMPEG;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace Steganography {

    /// <summary>
    /// Provides functionality for hiding message to .avi files based on given arguments. 
    /// With single argument -avi uses default settings (input, output, message) 
    /// </summary>
    class AviHandler {
        #region bit masks
        //masks are meant for use on char (16 bits)
        private const int MASK_ZERO =   char.MaxValue - 1;  //...11110
        private const int MASK_ONE =    1;                  //...00001
        #endregion

        #region AVI
        /// <summary>
        /// Char size in bits 16 for Unicode UTF-16.
        /// </summary>
        private const int CHAR_SIZE =               16;
        /// <summary>
        /// Bytes per pixel (r,g,b)
        /// </summary>
        private const int BYTES_PER_PX =            3;
        /// <summary>
        /// First pixel contains length of hidden message so 3 bits are unavalible for writing message.
        /// </summary>
        private const int UNAVALIBLE_BITS =         3;
        #endregion

        #region default
        /// <summary>
        /// Default source file.
        /// </summary>
        private const String DEFAULT_SOURCE =       "drop.avi";
        /// <summary>
        /// Default message file.
        /// </summary>
        private const String DEFAULT_MSG =          "msg.txt";
        /// <summary>
        /// Default output file.
        /// </summary>
        private const String DEFAULT_OUTPUT =       "out.avi";

        /// <summary>
        /// Calls aviInsert() with default settings.
        /// </summary>
        private static void aviDefaultInsert() {
            Console.WriteLine("Default insert message into AVI");
            Console.WriteLine(
                        " Settings: \n" +
                        "   source file: " + DEFAULT_SOURCE + "\n" +
                        "   output file: " + DEFAULT_OUTPUT + "\n" +
                        "   message:     " + DEFAULT_MSG
                        );
            aviInsert(DEFAULT_SOURCE, DEFAULT_OUTPUT, MsgLoader.loadMessage(DEFAULT_MSG));
        }

        /// <summary>
        /// Calls aviExtract() with default settings.
        /// </summary>
        private static void aviDefaultExtract() {
            Console.WriteLine("Default extract message from AVI");
            Console.WriteLine(
                        " Settings: \n" +
                        "   source file: " + DEFAULT_OUTPUT + "\n" +
                        "   message:     " + DEFAULT_MSG
                        );
            aviExtract(DEFAULT_OUTPUT);
        }

        #endregion

        /// <summary>
        /// Calls specific functions based on given settings or shows help.
        /// </summary>
        /// <param name="args">settings for avi steganography</param>
        public static void aviMod(string[] args) {
            switch(args.Length) {
                case 1:
                case 2:
                    aviDefaultInsert();
                    aviDefaultExtract();
                    break;
                case 3:
                    aviExtract(args[2]);
                    break;
                case 5:
                    aviInsert(args[2], args[3], MsgLoader.loadMessage(args[4]));
                    break;

                default:
                    Console.WriteLine("Wrong number of arguments");
                    Console.WriteLine(
                            "Input format: " +
                            "Steganography.exe -avi [mod] [\"input\"] [\"output\"] [\"msg\"]" + "\n" +
                            "   [mod]   -i  insert message into avi" + "\n" +
                            "               require: [\"input\"] [\"output\"] [\"msg\"] " + "\n" +
                            "           -e  extract message from avi" + "\n" +
                            "               require: [\"input\"]" + "\n" +
                            "   \"input\"   filename for input" + "\n" +
                            "   \"output\"  filename for generated file" + "\n" +
                            "   \"msg\"     file with message to be encrypted into input file" + "\n"
                            );
                    break;
            }
        }

        /// <summary>
        /// Converts given message to bits, hides it into video source and result is saved as output.
        /// </summary>
        /// <param name="input">source video file</param>
        /// <param name="output">name for the output video file</param>
        /// <param name="msg">message to hide</param>
        private static void aviInsert(String input, String output, String msg) {
            BitArray bArr;
            bArr = messageToBits(msg);

            //Reader
            VideoFileReader vfr = new VideoFileReader();
            try {
                vfr.Open(input);
            } catch(Exception e) {
                Console.WriteLine("Could not open file: " + input);
                Environment.Exit(-1);
            }

            //Info
            Console.WriteLine(
                "Video attributes: " +
                "\n width:  " + vfr.Width +
                "\n height: " + vfr.Height +
                "\n codec:  " + vfr.CodecName +
                "\n frames: " + vfr.FrameCount +
                "\n fps:    " + vfr.FrameRate
                );
            
            //Message size check
            long pxls = vfr.Width * vfr.Height;
            long availableSize = pxls * BYTES_PER_PX * vfr.FrameCount - UNAVALIBLE_BITS;

            if(bArr.Count >= availableSize) {
                Console.WriteLine(
                    "Message is too long." +
                    "\n Available bits: " + availableSize + " (" + availableSize / 16 + " chars)" +
                    "\n Requested bits: " + bArr.Count + " (" + bArr.Count / 16 + " chars)"
                    );
                vfr.Close();
                Environment.Exit(-1);
                return;
            }

            //Writer
            VideoFileWriter vfw = new VideoFileWriter();
            try {
                vfw.Open(output, vfr.Width, vfr.Height, vfr.FrameRate, VideoCodec.Raw);
            } catch(Exception e) {
                Console.WriteLine("Could not create file: " + output);
                Environment.Exit(-1);
            }

            
            Console.WriteLine("Inserting message...");
            writeMessage(vfr, vfw, bArr);
            Console.WriteLine("Done");

            vfr.Close();
            vfw.Close();
        }

        /// <summary>
        /// Extracts hidden message from given video file
        /// </summary>
        /// <param name="input">source video file</param>
        private static void aviExtract(string input) {
            //Reader
            VideoFileReader vfr = new VideoFileReader();
            try {
                vfr.Open(input);
            } catch(Exception e) {
                Console.WriteLine("Could not open file: " + input);
                Environment.Exit(-1);
            }

            //Info
            Console.WriteLine(
                "Video attributes: " +
                "\n width:  " + vfr.Width +
                "\n height: " + vfr.Height +
                "\n codec:  " + vfr.CodecName +
                "\n frames: " + vfr.FrameCount +
                "\n fps:    " + vfr.FrameRate
                );

            Console.WriteLine("Message: \n" + readMessage(vfr));

            vfr.Close();
            Console.WriteLine("Done");
        }

        /// <summary>
        /// Iterates through frames of video given by VideoFileReader.
        /// Gets size of the hidden message from first pixel in first frame and checks it's size.
        /// Applcation is terminated in case size of the message is beyond capacity of the given video.
        /// Extracts message fragments from each frame and returns whole message.
        /// </summary>
        /// <param name="vfr">video from which message should be extracted</param>
        /// <returns></returns>
        private static String readMessage(VideoFileReader vfr) {
            Bitmap bmp;
            BitmapData bmpData;
            IntPtr ptrFirstPx;
            byte[] pixels;
            int heightInPxls;
            int widthInBytes;

            //first bmp (with message length)
            bmp = vfr.ReadVideoFrame();

            //message length
            int msgLength = getMsgLength(bmp); //pixel[0, 0] 
            Console.WriteLine("Message length: " + msgLength);

            //Message size check
            long pxls = vfr.Width * vfr.Height;
            long availableSize = pxls * BYTES_PER_PX * vfr.FrameCount - UNAVALIBLE_BITS;

            if(msgLength * CHAR_SIZE >= availableSize) {
                Console.WriteLine(
                    "Message is too long. File is probably corrupted!" +
                    "\n Available bits: " + availableSize + " (" + availableSize / 16 + " chars)" +
                    "\n Requested bits: " + msgLength * CHAR_SIZE + " (" + msgLength + " chars)"
                    );
                vfr.Close();
                bmp.Dispose();
                Environment. Exit(-1);
            }

            //lock
            bmpData = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadWrite,
                bmp.PixelFormat
                );

            int byteCount = bmpData.Stride * bmp.Height;     //Stride accounting for Alignment of bitmap

            //Copy
            pixels = new byte[byteCount];
            ptrFirstPx = bmpData.Scan0;  //pointer to first pixel
            Marshal.Copy(ptrFirstPx, pixels, 0, pixels.Length);

            //Dimensions
            heightInPxls = bmpData.Height;
            widthInBytes = bmpData.Width * BYTES_PER_PX;

            //Read
            Console.WriteLine("Extracting message...");
            long msgLenBit = msgLength * CHAR_SIZE;

            int resultIdx = 0;
            char[] result = new char[msgLength];
            int srcByte = 0;
            srcByte = readFromBmpData(srcByte, bmpData, heightInPxls, widthInBytes, pixels, ref msgLenBit, ref result, ref resultIdx, true);

            //Unlock
            bmp.UnlockBits(bmpData);

            int percent = (int)(vfr.FrameCount / 10);
            int frame = 0;

            //Rest of the message (other frames)
            while(msgLenBit > 0) {
                bmp = vfr.ReadVideoFrame();
                
                //lock
                bmpData = bmp.LockBits(
                    new Rectangle(0, 0, bmp.Width, bmp.Height),
                    ImageLockMode.ReadWrite,
                    bmp.PixelFormat
                    );

                //Copy
                pixels = new byte[byteCount];
                ptrFirstPx = bmpData.Scan0;  //pointer to first pixel
                Marshal.Copy(ptrFirstPx, pixels, 0, pixels.Length);

                //Read
                srcByte = readFromBmpData(srcByte, bmpData, heightInPxls, widthInBytes, pixels, ref msgLenBit, ref result, ref resultIdx, false);

                //Unlock
                bmp.UnlockBits(bmpData);

                //Dispose bitmap
                bmp.Dispose();

                frame++;
                if(frame % percent == 0) {
                    Console.Write(".");
                }
            }
            Console.Write("\n");
            return new string(result);
        }

        /// <summary>
        /// Iterates through frames of video given by VideoFileReader.
        /// Writes size of the message into first pixel in the first frame.
        /// Hides fragments of the message in individual frames and those modified frames are added to output video stream.
        /// </summary>
        /// <param name="vfr">source video</param>
        /// <param name="vfw">output video</param>
        /// <param name="bArr">bit array containing message</param>
        private static void writeMessage(VideoFileReader vfr, VideoFileWriter vfw, BitArray bArr) {
            Bitmap bmp;
            BitmapData bmpData;
            IntPtr ptrFirstPx;
            byte[] pixels;
            int heightInPxls;
            int widthInBytes;
            //first bmp (with message length)
            bmp = vfr.ReadVideoFrame();

            //message length
            int msgLength = bArr.Count / CHAR_SIZE;
            setMsgLength(bmp, msgLength); //pixel[0, 0] 

            //lock
            bmpData = bmp.LockBits(
                new Rectangle(0, 0, bmp.Width, bmp.Height),
                ImageLockMode.ReadWrite,
                bmp.PixelFormat
                );

            int byteCount = bmpData.Stride * bmp.Height;     //Stride accounting for Alignment of bitmap

            //Copy
            pixels = new byte[byteCount];
            ptrFirstPx = bmpData.Scan0;  //pointer to first pixel
            Marshal.Copy(ptrFirstPx, pixels, 0, pixels.Length);

            //Dimensions
            heightInPxls = bmpData.Height;
            widthInBytes = bmpData.Width * BYTES_PER_PX;

            //Write
            int bArrIdx = 0;    //index in message
            writeToBmpData(bmpData, heightInPxls, widthInBytes, pixels, bArr, ref bArrIdx, true);

            //Apply copy to original
            Marshal.Copy(pixels, 0, ptrFirstPx, pixels.Length);

            //Unlock
            bmp.UnlockBits(bmpData);

            //Write to new video
            vfw.WriteVideoFrame(bmp);

            //Dispose bitmap
            bmp.Dispose();

            int percent = (int)(vfr.FrameCount / 10);

            //rest of the message (to other frames)
            int frames = 1;
            while(bArrIdx < bArr.Count) {
                bmp = vfr.ReadVideoFrame();

                //lock
                bmpData = bmp.LockBits(
                    new Rectangle(0, 0, bmp.Width, bmp.Height),
                    ImageLockMode.ReadWrite,
                    bmp.PixelFormat
                    );

                //Copy
                pixels = new byte[byteCount];
                ptrFirstPx = bmpData.Scan0;  //pointer to first pixel
                Marshal.Copy(ptrFirstPx, pixels, 0, pixels.Length);

                //Write
                writeToBmpData(bmpData, heightInPxls, widthInBytes, pixels, bArr, ref bArrIdx, false);  //not the first bitmap

                //Apply copy to original
                Marshal.Copy(pixels, 0, ptrFirstPx, pixels.Length);
                bmp.UnlockBits(bmpData);

                //Write to new video
                vfw.WriteVideoFrame(bmp);

                //Dispose bitmap
                bmp.Dispose();

                frames++;
                if(frames % percent == 0) {
                    Console.Write(".");
                }
            }
            Console.Write("\n");
            Console.WriteLine("Used frames: " + frames);

            //Copy rest
            Console.WriteLine("Copying rest of the frames...");

            while((bmp = vfr.ReadVideoFrame()) != null) {
                //Write to new video
                vfw.WriteVideoFrame(bmp);

                //Dispose bitmap
                bmp.Dispose();

                frames++;
                if(frames % percent == 0) {
                    Console.Write(".");
                }
            }
            Console.Write("\n");
        }

        /// <summary>
        /// Iterates through pixel in given bitmap and extract LSB from color components in individual pixels. Those bits are converted
        /// to Unicode char which is added to result char array.
        /// </summary>
        /// <param name="srcByte">Remaining bits from previous frame that were not converted into char yet</param>
        /// <param name="bmpData">Locked bitmap data</param>
        /// <param name="heightInPxls">BitmapData height in pixels</param>
        /// <param name="widthInBytes">BitmapData width in bytes</param>
        /// <param name="pixels">raw pixel array from bitmap</param>
        /// <param name="msgLenBit">remaining length of expected message</param>
        /// <param name="result">array of chars representing extracted message</param>
        /// <param name="resultIdx">index of last char added into result</param>
        /// <param name="first">true for a first frame to skip firts pixel in the first frame (message length is saved there)</param>
        /// <returns>Remaining bits which were not converted into char</returns>
        private static int readFromBmpData(int srcByte, BitmapData bmpData, int heightInPxls, int widthInBytes, byte[] pixels, ref long msgLenBit, ref char[] result, ref int resultIdx, bool first) {
            int currentLine;
            int comp;
            int x = 0;
            if(first) {
                x += BYTES_PER_PX;      //skip first pixel in first bitmap
            }

            ArrayList arrCh = new ArrayList();

            for(int y = 0; y < heightInPxls; y++) {     //lines
                currentLine = y * bmpData.Stride;
                for(; x < widthInBytes; x += BYTES_PER_PX) {   //rows
                    for(int c = 0; c < BYTES_PER_PX; c++) {    //3 colors per px (components)
                        comp = pixels[currentLine + x + c];
                        srcByte = srcByte << 1;
                        srcByte += getLastBit(comp);        //add one bit
                        msgLenBit--;

                        if(msgLenBit % CHAR_SIZE == 0) {
                            //One char complete
                            result[resultIdx] = (char)convertToText(srcByte);
                            resultIdx++;
                            srcByte = 0;
                        }

                        if(msgLenBit <= 0) {
                            //End of message
                            return srcByte;
                        }
                    }
                    //end pixel
                }
                //end of row
                x = 0;
            }
            //end of bitmap
            return srcByte;
        }

        /// <summary>
        /// Iterates through pixel in given bitmap and overwrites LSB from color components in individual pixels. By bits in BitArray representing message.
        /// </summary>
        /// <param name="bmpData">Locked bitmap data</param>
        /// <param name="heightInPxls">BitmapData height in pixels</param>
        /// <param name="widthInBytes">BitmapData width in bytes</param>
        /// <param name="pixels">raw pixel array from bitmap</param>
        /// <param name="bArr">message converted into bits</param>
        /// <param name="bArrIdx">index of last bit embedded into bitmap</param>
        /// <param name="first">true for a first frame to skip firts pixel in the first frame (message length is saved there)</param>
        private static void writeToBmpData(BitmapData bmpData, int heightInPxls, int widthInBytes, byte[] pixels, BitArray bArr, ref int bArrIdx, bool first) {
            int currentLine;
            int comp;
            int x = 0;
            if(first) {
                x += BYTES_PER_PX;      //skip first pixel in first bitmap
            }

            for(int y = 0; y < heightInPxls; y++) {     //lines
                currentLine = y * bmpData.Stride;
                for(; x < widthInBytes; x += BYTES_PER_PX) {   //rows
                    for(int c = 0; c < BYTES_PER_PX; c++) {    //3 colors per px (components)
                        comp = pixels[currentLine + x + c];
                        comp = embedLastBit(comp, bArr, bArrIdx);
                        pixels[currentLine + x + c] = (byte)comp;
                        bArrIdx++;

                        if(bArrIdx >= bArr.Count) {
                            //End of message
                            return;
                        }
                    }
                    //end pixel
                }
                //end of row
                x = 0;
            }
            //end of bitmap
        }

        /// <summary>
        /// Overwrites LSB in source by 1 or 0 based on boolean value in BitArray bArr on index index.
        /// </summary>
        /// <param name="source"></param>
        /// <param name="bArr"></param>
        /// <param name="index"></param>
        /// <returns></returns>
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
        /// <returns></returns>
        private static int getLastBit(int source) {
            if((source & MASK_ONE) == 1) {
                return 1;
            } else {
                return 0;
            }
        }

        /// <summary>
        /// Sets value val into first pixel of given bitmap over 3 bytes (3colors)
        /// </summary>
        /// <param name="bmp">bitmap from the first frame</param>
        /// <param name="val">value to hide</param>
        private static void setMsgLength(Bitmap bmp, int val) {
            Color px = bmp.GetPixel(0, 0);

            int r = val >> 16;      //remove 16 LSBs and leave red color
            val -= r << 16;         //remove red part
            int g = val >> 8;       //remove 8 LSBs and leave green color
            int b = val - (g << 8); //remove grean part
                                    //rest is blue part
            Color pixelColor = Color.FromArgb(r, g, b);
            bmp.SetPixel(0, 0, pixelColor);
        }

        /// <summary>
        /// Gets message length from first pixel in given bitmap
        /// </summary>
        /// <param name="bmp">bitmap from the first frame</param>
        /// <returns>extracted value</returns>
        private static int getMsgLength(Bitmap bmp) {
            Color px = bmp.GetPixel(0, 0);
            return (px.R << 16) + (px.G << 8) + px.B;
        }

        /// <summary>
        /// Converts string to BitArray
        /// </summary>
        /// <param name="msg">source string</param>
        /// <returns>bit array from string</returns>
        private static BitArray messageToBits(string msg) {
            byte[] textByte = Encoding.Unicode.GetBytes(msg);   //bytes
            return new BitArray(textByte);
        }

        /// <summary>
        /// Converts int value to Unicode char.
        /// The integer value have reverse order of bits than the character it is representing.
        /// </summary>
        /// <param name="srcByte">reverse int value of unicode char</param>
        /// <returns>Unicode char</returns>
        private static char convertToText(int srcByte) {
            int value = 0;
            for(int i = 0; i < CHAR_SIZE; i++) {
                value = value << 1;
                value |= srcByte & MASK_ONE;
                srcByte = srcByte >> 1;
            }
            return (char)value;
        }
    }
}
