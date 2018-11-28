using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KPG01_ciste
{
    struct ColorF {
        public float a;
        public float r;
        public float g;
        public float b;

        public ColorF(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = 1.0f;
        }

        public static ColorF operator +(ColorF a, ColorF b) {
            return new ColorF(a.r + b.r, a.g + b.g, a.b + b.b);
        }

        public static ColorF operator -(ColorF a, ColorF b) {
            return new ColorF(a.r - b.r, a.g - b.g, a.b - b.b);
        }

        public static ColorF operator *(ColorF a, float coef) {
            return new ColorF(a.r * coef, a.g * coef, a.b * coef);
        }
    }
}
