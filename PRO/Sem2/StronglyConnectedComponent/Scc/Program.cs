using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Scc {
    class Program {
        /// <summary>
        /// Show neighbor matrix in console
        /// </summary>
        private static bool showMatrix = false;

        /// <summary>
        /// Show elements of single components in console
        /// </summary>
        private static bool showComponent = false;

        /// <summary>
        /// Number of a selected method for computation.
        /// </summary>
        private static int method = 0;

        /// <summary>
        /// Graph parameters
        /// </summary>
        #region
        private static int nodes = 10;
        private static int edges = 10;
        /// <summary>
        /// True for directed graph
        /// </summary>
        private static bool dir = false;
        /// <summary>
        /// True for use of recusive method
        /// </summary>
        private static bool rec = false;
        /// <summary>
        /// Seed for random generation
        /// </summary>
        private static int seed;
        #endregion


        /// <summary>
        /// Entry point of this program.
        /// Sets all required parameters from given arguments.
        /// </summary>
        /// <param name="args"></param>
        static void Main(string[] args) {
            try {
                //Arguments parsing
                if(!int.TryParse(args[0], out nodes)) {
                    nodes = 10;
                }
                if(!int.TryParse(args[1], out edges)) {
                    edges = 10;
                }
                if(edges > nodes * nodes) edges = nodes;

                int len = args.Length;

                for(int i = 2; i < args.Length; i++) {
                    if(args[i].Contains("-s")) {
                        if(args.Length > (i + 1))
                            int.TryParse(args[i + 1], out seed);
                    }
                    if(args[i].Contains("-m"))
                        showMatrix = true;
                    if(args[i].Contains("-c"))
                        showComponent = true;
                    if(args[i].Contains("-d"))
                        dir = true;
                    if(args[i].Contains("-r"))
                        rec = true;
                    if(args[i].Contains("-k"))
                        method = 1;

                }
            } catch {
                Console.WriteLine("Invalid arguments");
                Environment.Exit(-1);
            }

            

            //neighbor matrix
            int[,]neigh = null;
            try {
                if(seed != 0) {
                    neigh = GraphGen.getMatrix(nodes, edges, seed, dir);
                } else {
                    neigh = GraphGen.getMatrix(nodes, edges, dir);
                }
            } catch(OutOfMemoryException ex) {
                Console.WriteLine("Too large set was selected. (Out of memory for generating graph)");
                Environment.Exit(-2);
            }

            //neigh = InitNeigh();///////////

            //solution
            SCCI impl = null;

                //Tarjan's
            if(method == 0) {
                if(rec) {
                    impl = new Tarjan(neigh);
                } else {
                    impl = new TarjanNonRec(neigh);
                }
            }

                //Korasaraju's
            if(method == 1) {
                impl = new Korasaraju(neigh);
            }
            

            if(showMatrix)
                printMatrix(neigh);

            //Run the algorithm
            Stopwatch stopWatch = new Stopwatch();
            stopWatch.Start();
            try {
                if(showComponent) {
                    impl.WriteComponents(true);
                } else {
                    impl.WriteComponents(false);
                }
            }catch(StackOverflowException ex) {
                Console.WriteLine("Too large set was selected. Method reached limit of recursion nesting("+Tarjan.REC_LIMIT+"). Program was terminated due to imminent danger of StackOverflow exception.");
                Environment.Exit(-2);
            }
            stopWatch.Stop();
            string time = stopWatch.ElapsedMilliseconds.ToString();

            //Report
            Console.WriteLine("");
            Console.WriteLine(GraphGen.getReport());
            Console.WriteLine("Elapsed time: \t"+time+"ms");
        }

        /// <summary>
        /// Method for testing
        /// </summary>
        /// <returns>neighbor matrix</returns>
        private static int[,] InitNeigh() {
            int [,] neigh = new int[5, 5];
            neigh[0, 1] = 1;
            neigh[1, 0] = 1;
            neigh[1, 2] = 1;
            neigh[2, 0] = 1;
            neigh[1, 3] = 1;
            neigh[3, 4] = 1;
            neigh[4, 3] = 1;
            return neigh;
        }

        /// <summary>
        /// Prints given matrix to console
        /// </summary>
        /// <param name="arr"></param>
        private static void printMatrix(int[,] arr) {
            var rowCount = arr.GetLength(0);
            var colCount = arr.GetLength(1);
            for(int row = 0; row < rowCount; row++) {
                for(int col = 0; col < colCount; col++)
                    Console.Write(String.Format("{0} ", arr[row, col]));
                Console.WriteLine();
            }
        }
    }
}
