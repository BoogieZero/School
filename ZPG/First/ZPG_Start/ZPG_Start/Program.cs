using OpenTK;
using System.Collections.Generic;
using System;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZPG_Start {
    class Program {
        static void Main(string[] args) {
            Console.WriteLine("Start");
            Console.WriteLine(  "Esc        - Quit application \n"+
                                "F1         - toggle fullscreen \n"+
                                "F          - change filter \n"+
                                "L          - toggle lights \n"+
                                "B          - toggle blending \n"+
                                "Arrows     - movement \n"+
                                "PageUp     - move up \n"+
                                "PageDown   - move down");
            Console.WriteLine();
            using(MyGameWindow window = new MyGameWindow()) {
                window.Run();
            }
        }
    }
}
