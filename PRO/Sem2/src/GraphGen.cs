using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Scc {
    static class GraphGen {
        /// <summary>
        /// Random number generator
        /// </summary>
        private static Random rnd;
        /// <summary>
        /// Seed for random number generator
        /// </summary>
        private static int seed;
        /// <summary>
        /// True for directed graph
        /// </summary>
        private static bool directed;

        public static int edges = 0;
        public static int nodes = 0;
        /// <summary>
        /// Edge density (percent of maximum available edges in graph)
        /// </summary>
        public static float density = 0f;

        /// <summary>
        /// Creates random generated neighbor matrix for graph defined by given parameters
        /// </summary>
        /// <param name="nodes">number of graph nodes</param>
        /// <param name="edges">maximum number of edges in the graph</param>
        /// <param name="dir">true for directed graph</param>
        /// <returns></returns>
        public static int[,] getMatrix(int nodes, int edges, bool dir) {
            GraphGen.seed = System.DateTime.Now.Millisecond;
            rnd = new Random(seed);
            return getMatrixP(nodes, edges, dir);
        }

        /// <summary>
        /// Creates random generated neighbor matrix for graph defined by given parameters
        /// </summary>
        /// <param name="nodes">number of graph nodes</param>
        /// <param name="edges">maximum number of edges in the graph</param>
        /// <param name="seed">seed for random generator</param>
        /// <param name="dir">true for directed graph</param>
        /// <returns></returns>
        public static int[,] getMatrix(int nodes, int edges, int seed, bool dir) {
            GraphGen.seed = seed;
            rnd = new Random(seed);
            return getMatrixP(nodes, edges, dir);
        }

        /// <summary>
        /// Creates random generated neighbor matrix for graph defined by given parameters
        /// this.seed is used
        /// </summary>
        /// <param name="nodes"></param>
        /// <param name="edges"></param>
        /// <param name="dir"></param>
        /// <returns></returns>
        private static int[,] getMatrixP(int nodes, int edges, bool dir) {
            int edg = 0;
            int [,] neigh = new int[nodes, nodes];
            if(edges > (nodes * nodes)) edges = nodes;
            int x,y;

            for(int i = 0; i < edges; i++) {
                if(!dir) {
                    x = i % nodes;
                } else {
                    x = rnd.Next(nodes);
                }
                y = rnd.Next(nodes);
                if(neigh[x, y] != 1) {
                    edg += 1;
                } else {
                    continue;
                }
                neigh[x, y] = 1;
                if(!dir)
                    neigh[y, x] = 1;
            }
            if(!dir)
                edg *= 2;

            GraphGen.density = (float)edg / (nodes * nodes);
            GraphGen.edges = edg;
            GraphGen.nodes = nodes;
            GraphGen.directed = dir;
            return neigh;
        }

        /// <summary>
        /// Creates report for generated graph as string
        /// </summary>
        /// <returns>report for last generated graph</returns>
        public static string getReport() {
            string result;
            if(GraphGen.directed) {
                result = "Directed graph";
            } else {
                result = "Undirected graph";
            }
            result +=
                " (seed: " + seed + ")\n" +
                "   nodes:      " + nodes + "\n" +
                "   edges:      " + edges + "\n" +
                "   density:    " + (int)(density * 100f) + "%\n";
            return result;
        }
    }
}
