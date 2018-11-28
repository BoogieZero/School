using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ZPG_1_10 {
    static class SimpleStructures {
        public struct Vertex {
            //Point coordinates
            public float x;
            public float y;
            public float z;

            //Texture coordinates
            public float u;
            public float v;

            public Vertex(float u, float v, float x, float y, float z) {
                this.u = u;
                this.v = v;
                this.x = x;
                this.y = y;
                this.z = z;
            }
        }

        public struct Triangle {
            public Vertex[] verts;

            public Triangle(Vertex a, Vertex b, Vertex c) {
                verts = new Vertex[3];
                verts[0] = a;
                verts[1] = b;
                verts[2] = c;
            }
        }

        public struct Sector {
            public List<Triangle> list;
        }

    }
}
