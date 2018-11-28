using System;

namespace Steganography {
    /// <summary>
    /// Main class which calls specific functions for avi and png steganography based on given arguments
    /// </summary>
    class Program {

        static void Main(string[] args) {
            switch(args.Length) {
                case 0:
                    //Help
                    Console.WriteLine("No arguments used");
                    Console.WriteLine(
                        "Input format: " +
                        "Steganography.exe [mod] " + "\n" +
                        "   [mod]   -png    steganography for png" + "\n" +
                        "           -avi    steganograpy for avi" + "\n"
                        );
                    break;
                default:
                    //PNG/AVI
                    switch(args[0]) {
                        case "-png":
                            PngHandler.pngMod(args);
                            break;
                        case "-avi":
                            AviHandler.aviMod(args);
                            break;

                        default:
                            Console.WriteLine("Incorrect [mod] argument \n" +
                                "for help type: Steganography.exe"
                                );
                            break;
                    }
                    break;
            }
            //END
            //Console.ReadKey();
        }
    }
}
