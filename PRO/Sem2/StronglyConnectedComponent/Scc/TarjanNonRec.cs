using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Scc {
    /// <summary>
    /// This class implements Tarjan's algorythm without using recursive method.
    /// Recursion is substituted by additional stack.
    /// </summary>
    class TarjanNonRec : SCCI {
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
        /// Stack for substitution of recursion
        /// </summary>
        private Stack<int> rNodeStck = new Stack<int>();
        //private Stack<int> rCycleStck = new Stack<int>();


        /// <summary>
        /// Creates new instance of this implementation
        /// </summary>
        /// <param name="neigbourMatrix">neighbour matrix</param>
        public TarjanNonRec(int[,] neigbourMatrix) {
            neigh = neigbourMatrix;
            nodes = new Node[neigh.GetLength(0)];
            for(int i = 0; i < nodes.Length; i++) {
                nodes[i] = new Node();
            }
            stck = new Stack<int>(neigh.GetLength(0));
            time = 1;
            component = 0;
        }

        /// <summary>
        /// Starts substituted recursive algorythm
        /// </summary>
        /// <param name="showComp">true - writes all nodes for each component into console</param>
        public void WriteComponents(bool showComp) {
            TarjanNonRec.showComp = showComp;
            for(int i = 0; i < neigh.GetLength(0); i++) {
                //iterate through all nodes
                if(nodes[i].dsc == 0) { //new node
                    int v = i;
                    bool rec = false;      //return from recursion
                    bool resolveSkip = false;   //skip resolve
                    while(true) {
                        int n;
                        if(rec) {
                            //returned from recursive call
                            rec = false;  //clear recursion flag
                            if(rNodeStck.Count == 0) {
                                break;
                            }
                            //store info onto stack
                            n = rNodeStck.Pop();
                            v = rNodeStck.Pop();
                            nodes[v].low = Math.Min(nodes[v].low, nodes[n].low);
                            //move onto next cycle
                            n++;
                        } else {
                            NewNode(v);
                            n = 0;
                        }

                        //for each neighbour of v
                        for(; n < nodes.GetLength(0); n++) {
                            if(neigh[v, n] == 0) continue;  //not a neighbour
                            if(nodes[n].dsc == 0) {         //new node
                                //store info for recursion
                                rNodeStck.Push(v);
                                rNodeStck.Push(n);
                                v = n;  //active node in this n
                                resolveSkip = true; //skip resolve
                                break;
                            } else if(nodes[n].prg) {    //neighbour in processing
                                //new lowest descendant
                                nodes[v].low = Math.Min(nodes[v].low, nodes[n].dsc);
                            }
                        }
                        if(resolveSkip) {
                            //skip resolve if new node was found
                            resolveSkip = false;
                            continue;
                        }

                        //no new nodes found
                        if(nodes[v].low == nodes[v].dsc) {
                            //resolve
                            //recursion for this component ended at the origin
                            //extraction of the component
                            retAncestor(v);
                            rec = true;
                        }else {
                            //return from recusive nesting
                            rec = true;
                        }
                    }
                }
            }

            Console.WriteLine("----Tarjan's Algorythm (Non-recursive implementation)----");
        }

        /// <summary>
        /// Exctract strongly connected component from stack and writes report to console
        /// </summary>
        /// <param name="v">origin of the component</param>
        private void retAncestor(int v) {
            //origin
            component++;
            int x;
            numOfComp = 0;
            string result = "";
            do {
                x = stck.Pop();
                nodes[x].prg = false;
                if(TarjanNonRec.showComp) {
                    result += x + ", ";
                } else {
                    numOfComp++;
                }
            } while(x != v);
            if(TarjanNonRec.showComp) {
                result = result.Substring(0, result.Length - 2); //remove last ", "
                Console.WriteLine("Component č." + component + ":  \t" + result);
            } else {
                Console.WriteLine("Component č." + component + ":  \t" + numOfComp);
            }
        }

        /// <summary>
        /// Setup for new discovered node
        /// </summary>
        /// <param name="v"></param>
        private void NewNode(int v) {
            nodes[v].dsc = time;    //discover time
            nodes[v].low = time;    //oldest descendant
            time++;
            stck.Push(v);
            nodes[v].prg = true;    //v in progress
        }

        private void Scc(int v) {
            nodes[v].dsc = time;    //discover time
            nodes[v].low = time;    //oldest descendant
            time++;
            stck.Push(v);
            nodes[v].prg = true;    //v in progress

            //for each neighbour
            for(int n = 0; n < nodes.GetLength(0); n++) {
                if(neigh[v, n] == 0) continue;  //not a neighbour
                if(nodes[n].dsc == 0) {         //new node
                    Scc(n); //recursion
                    //set older descendant
                    nodes[n].low = Math.Min(nodes[v].low, nodes[n].low);
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
                    if(TarjanNonRec.showComp) {
                        result += x + ", ";
                    } else {
                        numOfComp++;
                    }
                } while(x != v);
                if(TarjanNonRec.showComp) {
                    result = result.Substring(0, result.Length - 2); //remove last ", "
                    Console.WriteLine("Component č." + component + ":  \t" + result);
                } else {
                    Console.WriteLine("Component č." + component + ":  \t" + numOfComp);
                }
            }
        }
    }
}
