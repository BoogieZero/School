using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing.Drawing2D;

namespace KPG_9
{
    /// <summary>
    /// Particle description
    /// </summary>
    public class Particle
    {
        public PointF position; //particle position
        public PointF center;   //center of the particle texture
        public int bitmapIndex; //index of used texture
        public double rotation; //rotation of the particle
        public double rotationIncrement; //rotation speed
        public float size;      //particle scale
        public int life;        //lifetime of the particle
        public int lifeEnd;     //time of death
    }
}
