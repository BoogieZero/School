using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Scc {
    /// <summary>
    /// This class implements Tarjan's algorythm using original recursive method.
    /// </summary>
    class Tarjan : SCCI {
        /// <summary>
        /// Graph node
        /// </summary>
        private class Node {
            /// <summary>
            /// Discover time
            /// </summary>
            public int dsc = 0;
            /// <summary>
            /// Lowest accesible descendant 
            /// </summary>
            public int low;
            /// <summary>
            /// In processing
            /// </summary>
            public bool prg = false;
        }

        /// <summary>
        /// Recursion counter 
        /// </summary>
        private static int recCount = 0;

        /// <summary>
        /// Limit for recursion nesting
        /// </summary>
        public static readonly int REC_LIMIT = 3000;

        /// <summary>
        /// Neighbor matrix
        /// </summary>
        private int [,] neigh;
        /// <summary>
        /// Graph nodes
        /// </summary>
        private Node[] nodes;
        /// <summary>
        /// Stack
        /// </summary>
        private Stack<int> stck;
        /// <summary>
        /// Time counter
        /// </summary>
        private int time;

        /// <summary>
        /// Component counter
        /// </summary>
        private int component;

        /// <summary>
        /// Number of components
        /// </summary>
        private int numOfComp;

        /// <summary>
        /// true - writes all nodes assigned to each component to console
        /// </summary>
        private static bool showComp;

        /// <summary>
        /// Creates new instance of this implementation
        /// </summary>
        /// <param name="neighborMatrix">neighbour matrix</param>
        public Tarjan(int[,] neighborMatrix) {
            neigh = neighborMatrix;
            nodes = new Node[neigh.GetLength(0)];
            for(int i = 0; i < nodes.Length; i++) {
                nodes[i] = new Node();
            }
            stck = new Stack<int>(neigh.GetLength(0));
            time = 1;
            component = 0;
        }

        /// <summary>
        /// Starts recursive algorythm
        /// </summary>
        /// <param name="showComp">true - writes all nodes for each component into console</param>
        public void WriteComponents(bool showComp) {
            Tarjan.showComp = showComp;
            Tarjan.recCount = 0;
            for(int i = 0; i < neigh.GetLength(0); i++) {
                if(nodes[i].dsc == 0) { //new node
                    recCount = 0;
                    Scc(i);
                }
            }

            Console.WriteLine("----Tarjan's Algorythm (Recursive implementation)----");
        }

        /// <summary>
        /// Recursive method for iteration over the graph
        /// </summary>
        /// <param name="v">start node</param>
        private void Scc(int v) {
            Tarjan.recCount++;
            if(Tarjan.recCount >= REC_LIMIT) {  //recursion overflow prevention
                throw new StackOverflowException();
            }
            nodes[v].dsc = time;    //discover time
            nodes[v].low = time;    //oldest descendant
            time++;
            stck.Push(v);
            nodes[v].prg = true;    //v in progress

            //for each neighbour
            for(int n = 0; n < nodes.GetLength(0); n++) {
                if(neigh[v, n] == 0) continue;  //not a neighbor
                if(nodes[n].dsc == 0) {         //new node
                    Scc(n); //recursion
                    //set older descendant
                    nodes[v].low = Math.Min(nodes[v].low, nodes[n].low);
                } else if(nodes[n].prg) {    //neighbour in processing
                    //new lowest descendant if better
                    nodes[v].low = Math.Min(nodes[v].low, nodes[n].dsc);
                }
            }

            //return through ancestors to origin of this component
            if(nodes[v].low == nodes[v].dsc) {
                //origin
                component++;
                int x;
                numOfComp = 0;
                string result = "";
                do {
                    x = stck.Pop();
                    nodes[x].prg = false;
                    if(Tarjan.showComp) {
                        result += x + ", ";
                    } else {
                        numOfComp++;
                    }
                } while(x != v);
                if(Tarjan.showComp) {
                    result = result.Substring(0, result.Length - 2); //remove last ", "
                    Console.WriteLine("Component č." + component + ":  \t" + result);
                } else {
                    Console.WriteLine("Component č." + component + ":  \t" + numOfComp);
                }
            }
        }

    }
}
