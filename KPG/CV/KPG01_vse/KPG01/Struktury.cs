using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KPG01
{
    public struct Bunka
    {
        
            public bool visited;
            public bool left;
            public bool top;
            public bool right;
            public bool bottom;
            public int section;
            public bool cil;

            public Bunka(bool closedCell)
            {
                this.section = 0;
                this.visited = false;
                this.top = closedCell;
                this.left = closedCell;
                this.right = closedCell;
                this.bottom = closedCell;
                this.cil = false;
            }



    }
}

public struct Position
{
    public int x;
    public int y;

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}

public struct Matice {

    public double a00;
    public double a01;
    public double a02;
    public double a10;
    public double a11;
    public double a12;
    public double a20;
    public double a21;
    public double a22;

    public Matice(double a00, double a01, double a02, double a10, double a11, double a12, double a20, double a21, double a22)
    {
         this.a00= a00;
         this.a01=a01;
         this.a02=a02;
         this.a10=a10;
         this.a11=a11;
         this.a12=a12;
         this.a20=a20;
         this.a21=a21;
         this.a22=a22;
    }


}