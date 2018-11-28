using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZPG_1_10 {
    class Program {
        static void Main(string[] args) {
            Console.WriteLine("Start");
            Console.WriteLine(  "Esc        - Quit application \n" +
                                "F1         - toggle fullscreen \n" +
                                "F          - change filter \n" +
                                "L          - toggle lights \n" +
                                "B          - toggle blending \n" +
                                "Arrows     - movement \n"
                                );
            Console.WriteLine();
            Console.WriteLine("Info: ");
            using(MainGWindow window = new MainGWindow()) {
                window.Run();
            }
        }
    }
}
