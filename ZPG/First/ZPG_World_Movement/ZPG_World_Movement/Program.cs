using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZPG_World_Movement {
    class Program {
        static void Main(string[] args) {

            using(MainGWindow window = new MainGWindow()) {
                window.Run();
            }
        }
    }
}
