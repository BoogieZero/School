using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Scc {
    /// <summary>
    /// This class implements Korasaraju's algorythm without using recursive method.
    /// Recursion is substituted by additional stack.
    /// </summary>
    class Korasaraju : SCCI {
        /// <summary>
        /// Graph node
        /// </summary>
        private class Node {
            /// <summary>
            /// Is discovered
            /// </summary>
            public bool dsc = false;
        }

        /// <summary>
        /// Neighbor matrix
        /// </summary>
        private int [,] neigh;

        /// <summary>
        /// Graph nodes
        /// </summary>
        private Node[] nodes;

        /// <summary>
        /// Component counter
        /// </summary>
        private int component;

        /// <summary>
        /// true - writes all nodes assigned to each component to console
        /// </summary>
        private static bool showComp;

        /// <summary>
        /// Number of components
        /// </summary>
        private int numOfComp;

        /// <summary>
        /// Stack forward
        /// </summary>
        private Stack<int> fwdStck;

        /// <summary>
        /// Stack backward
        /// </summary>
        private Stack<int> bcwStck;

        /// <summary>
        /// Creates new instance of this implementation
        /// </summary>
        /// <param name="neighborMatrix">neighbour matrix</param>
        public Korasaraju(int [,] neighborMatrix) {
            neigh = neighborMatrix;
            nodes = new Node[neigh.GetLength(0)];
            for(int i = 0; i < nodes.Length; i++) {
                nodes[i] = new Node();
            }
            fwdStck = new Stack<int>(neigh.GetLength(0));
            bcwStck = new Stack<int>(neigh.GetLength(0));
            component = 0;
        }

        /// <summary>
        /// Starts substituted recursive algorythm
        /// </summary>
        /// <param name="showComp">true - writes all nodes for each component into console</param>
        public void WriteComponents(bool showComp) {
            Korasaraju.showComp = showComp;

            //forward
            //for all nodes
            for(int i = 0; i < neigh.GetLength(0); i++) {
                if(nodes[i].dsc == false) { //new node
                    nodes[i].dsc = true;
                    fwdStck.Push(i);
                    Forward();
                }
            }

            //clear discover time
            for(int i = 0; i < nodes.Length; i++) {
                nodes[i].dsc = false;
            }

            //backward
            component = 0;
            int v;
            while(bcwStck.Count != 0) {
                v = bcwStck.Pop();
                if(nodes[v].dsc)
                    continue;
                Backward(v);
            }

            Console.WriteLine("----Korasaraju's Algorythm (Non-recursive implementation)----");
        }

        /// <summary>
        /// Single backward traverse of the graph from start node
        /// with output to console.
        /// Outputs single component to console.
        /// </summary>
        /// <param name="v">starting node</param>
        private void Backward(int v) {
            component++;
            numOfComp = 0;
            string result = "";
            fwdStck.Push(v);
            nodes[v].dsc = true;

            while(fwdStck.Count != 0) {
                v = fwdStck.Pop();
                if(Korasaraju.showComp) {
                    result += v + ", ";
                } else {
                    numOfComp++;
                }
                //for each neighbor of v
                for(int n = 0; n < nodes.GetLength(0); n++) {
                    if(neigh[n, v] == 0) continue;  //not a neighbor
                    if(nodes[n].dsc == false) {     //new node
                        nodes[n].dsc = true;
                        fwdStck.Push(n);
                    }
                }
            }

            //DFS ended
            if(Korasaraju.showComp) {
                result = result.Substring(0, result.Length - 2); //remove last ", "
                Console.WriteLine("Component č." + component + ":  \t" + result);
            } else {
                Console.WriteLine("Component č." + component + ":  \t" + numOfComp);
            }

        }

        /// <summary>
        /// Forward traverse of the graph.
        /// Uses forward stack.
        /// Fills backward stack.
        /// </summary>
        private void Forward() {
            int v;
            bool isLeaf;
            while(fwdStck.Count != 0) {
                isLeaf = true;
                v = fwdStck.Peek();
                //for each neighbor of v
                for(int n = 0; n < nodes.GetLength(0); n++) {
                    if(neigh[v, n] == 0) continue;  //not a neighbor
                    if(nodes[n].dsc == false) {         //new node
                        isLeaf = false;
                        nodes[n].dsc = true;
                        fwdStck.Push(n);
                    }
                }

                if(isLeaf) {
                    //node is a leaf
                    //transfer to backward stack
                    fwdStck.Pop();
                    bcwStck.Push(v);
                }
            }
        }
    }
}
