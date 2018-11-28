using System;
using System.IO;


namespace Steganography {
    /// <summary>
    /// Provides functionality to reading source message files.
    /// </summary>
    class MsgLoader {
        /// <summary>
        /// Returns one string containing whole message from given file.
        /// </summary>
        /// <param name="file">file to read</param>
        /// <returns>content of the file</returns>
        public static String loadMessage(String file) {
            if(!File.Exists(file)) {
                Console.WriteLine("Message file not found: "+file);
                Environment.Exit(-1);
            }
            string contents = File.ReadAllText(file);
            return contents;
        }
    }
}
