using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace Steganography_Console {
    class Program {

        private const int MSG_LENGTH = 500 * 8;       //in bits
        private const int BMP_HEADER_OFFSET = 54;

        private static void findMessage(String file) {
            byte[] arr = new byte[MSG_LENGTH];

            String msg = "";

            if(!File.Exists(file)) {
                Console.WriteLine("File not found! ("+file+")");
            }

            try {
                using(BinaryReader rd = new BinaryReader(new FileStream(file, FileMode.Open))) {
                    rd.BaseStream.Seek(BMP_HEADER_OFFSET, SeekOrigin.Begin);
                    rd.Read(arr, 0, MSG_LENGTH);
                    rd.Close();
                }
            } catch {
                Console.WriteLine("Unable to read message from file!");
                Environment.Exit(0);
            }
            
            int ch = 0;
            int chCnt = 0;
            char c;
            for(int i = 0; i < arr.Length; i++) {
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
            Console.Write("Message: \n " + msg);
        }

        static void Main(string[] args) {
            if(args.Length != 1) {
                Console.WriteLine("Wrong number of arguments!");
                Console.WriteLine("Example: Steganography_CLI.exe [image.bmp]");
                Environment.Exit(0);
            }
            findMessage(args[0]);
        }
    }
}
