using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lesson_12 {
    class Program {
        static void Main(string[] args) {
            Console.WriteLine("Controlls: ");
            Console.WriteLine("Esc        - Quit application \n" +
                                "F1         - toggle fullscreen \n" +
                                "F          - change filter \n" +
                                "L          - toggle lights \n" +
                                "Arrows     - movement \n"+
                                "Shift + Up/Down    - tilt foward/backward"
                                );
            Console.WriteLine();
            Console.WriteLine("Info: ");

            using(OpenTKWindow window = new OpenTKWindow(500, 500)) {
                window.Run();
            }
            Console.WriteLine("Session closed.");
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }
    }
}
