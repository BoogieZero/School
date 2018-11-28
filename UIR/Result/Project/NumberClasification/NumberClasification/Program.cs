using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;

namespace NumberClasification {
    /// <summary>
    /// Main class operetaing function of program
    /// </summary>
    static class Program {

        /// <summary>
        /// Attachment of console to windows forms apllication
        /// </summary>
        /// <param name="dwProcessId"></param>
        /// <returns></returns>
        [DllImport("kernel32.dll")]
        static extern bool AttachConsole(int dwProcessId);
        private const int ATTACH_PARENT_PROCESS = -1;

        /// <summary>
        /// Instance of clasifier that will be used for clasification
        /// </summary>
        static IClassifier classifier;

        /// <summary>
        /// Intance of descriptor that will be used for description of given image
        /// </summary>
        static IDescriptor descriptor;

        /// <summary>
        /// Actual instance of imgae (used for clasification from bitmap)
        /// </summary>
        static BWImage bw;

        /// <summary>
        /// Instance of form from GUI to be able to display processed image
        /// </summary>
        static Form1 form;


        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(String []args) {
            AttachConsole(ATTACH_PARENT_PROCESS);

            Console.WriteLine();
            Console.WriteLine("<NumberClasification>");
            
             if(args.Length == 5) {
                //console start
                setDescriptor(args);
                setClassifier(args);
                processInput(args);
                classifier.saveModel(args[4]);

            } else if(args.Length == 1) {
                //gui start
                loadFromModel(args);
                descriptor = classifier.getDescriptor();
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                form = new Form1();
                Application.Run(form);

            } else {
                //help otherwise
                Console.WriteLine("Wrong number of arguments!");
                Console.WriteLine("There are two options for start: ");
                Console.WriteLine("\t 1. NumberClasification.exe [arg1]");
                Console.WriteLine("\t 2. NumberClasification.exe [arg1] [arg2] [arg3] [arg4] [arg5]");
                Console.WriteLine(" 1.");
                Console.WriteLine("\t arg1 = model filename");
                Console.WriteLine(" 2.");
                Console.WriteLine("\t arg1 = training set foldername");
                Console.WriteLine("\t arg2 = testing set foldername");
                Console.WriteLine("\t arg3 = descriptor name");
                Console.WriteLine("\t arg4 = classifier name");
                Console.WriteLine("\t arg5 = model filename for saving");
                Console.WriteLine();
                Console.WriteLine("Available descriptors:");
                Console.WriteLine("\t'WEIGHT'   (Averaging rows)");
                Console.WriteLine("\t'SAMPLE'   (Sampling rows and columns)");
                Console.WriteLine("\t'HOG'      (Histogram of Oriented Gradients)");

                Console.WriteLine("Available clasifiers:");
                Console.WriteLine("\t'DISTANCE' (Nearest distance to ethalon)");
                Console.WriteLine("\t'KNN'      (K Nearest Neighbors)");
                
            }
            
             //successfull end of program
            Console.WriteLine("<\\NumberClasification>");
        }

        /// <summary>
        /// Sets proper clasifier by saved model
        /// </summary>
        /// <param name="args">name of the saved model</param>
        private static void loadFromModel(string[] args) {
            string fileName = args[0];

            if(!File.Exists(fileName)) {
                Console.WriteLine("File (" + fileName + ") doesn't exist!");
                Environment.Exit(0);
            }

            using(XmlReader xrr = XmlReader.Create(fileName)) {

                while(xrr.Read()) {
                    if(xrr.IsStartElement()) {
                        if(xrr.Name.Equals("classifier")) {
                            switch(xrr["name"].ToUpper()) {
                                case "KNN":
                                    classifier = new NearestNeighborsClasifier();
                                    break;
                                case "DISTANCE":
                                    classifier = new DistanceClasifier();
                                    break;
                                default:
                                    Console.WriteLine("No such classifier!");
                                    break;
                            }

                            xrr.Close();
                            classifier.loadModel(fileName);
                            return;
                        }
                    }
                }
            }
        }

        /// <summary>
        /// Sets descriptor according to given arguments
        /// </summary>
        /// <param name="args">descriptor mark</param>
        private static void setDescriptor(string[] args) {
            descriptor = DescriptorNamesExtension.getNewDescriptor(args[2]);
        }

        /// <summary>
        /// Sets classifier according to given arguments
        /// </summary>
        /// <param name="args">clasification mark</param>
        private static void setClassifier(string[] args) {
            switch(args[3].ToUpper()) {
                case "KNN":
                    classifier = new NearestNeighborsClasifier(descriptor);
                    break;
                case "DISTANCE":
                    classifier = new DistanceClasifier(descriptor);
                    break;
                default:
                    Console.WriteLine("No such classifier found!");
                    Environment.Exit(0);
                    break;
            }
        }

        /// <summary>
        /// Evaluates given image and result is set in coresponding elements in GUI via form
        /// </summary>
        /// <param name="bmp">bitmap of image for evaluation</param>
        public static void evaluateBitmap(Bitmap bmp) {
            bw = BWImage.createInstanceFromBitmap(bmp);
            if(bw == null) return;
            form.setUpBitmapToDraw(bw, descriptor);
            int result = classifier.evaluate(bw);
            form.writeResult(result);
            form.writeInfo(getReportFrom(classifier.getReportLast()));
            //Console.WriteLine("RESULT: "+result);
        }

        /// <summary>
        /// Returns number coresponding to case follder in given path
        /// </summary>
        /// <param name="path">path to image</param>
        /// <returns>number of folder for given image</returns>
        static int getFolderInt(String path) {
            int folderI;
            string folder = (path.Split('\\'))[1];    //folder name is after one '\'
            if(!int.TryParse(folder, out folderI)) {
                Console.WriteLine("Invalid folder name!");
                Environment.Exit(0);
            }
            return folderI;
        }

        /// <summary>
        /// Loads training data and tests testing data
        /// </summary>
        /// <param name="args">arguments from starting the program</param>
        private static void processInput(String[] args) {
            loadTrainingSet(args);
            loadTestingSet(args);
        }

        /// <summary>
        /// Loads training data and feeds classifier with their description
        /// </summary>
        /// <param name="args">starting arguments</param>
        private static void loadTrainingSet(string[] args) {
            double[] dsc;
            string[] trainingSet = Directory.GetFiles(args[0], "*.pgm", SearchOption.AllDirectories);
            Console.WriteLine("Loading training set...");

            BWImage img;
            for(int i = 0; i < trainingSet.Length; i++) {
                img = BWImage.createInstanceFromFile(trainingSet[i]);

                if(img == null) {
                    Console.WriteLine("Could not load image {" + trainingSet[i] + "}");
                    continue;
                }

                dsc = descriptor.getDescription(img);
                classifier.addTrainCase(getFolderInt(trainingSet[i]), dsc);

            }
            Console.WriteLine("Training set loaded (" + trainingSet.Length + " files)");
        } 

        /// <summary>
        /// Loads testing data and evaluates them by classifier
        /// Results are written into console
        /// </summary>
        /// <param name="args">starting arguments</param>
        private static void loadTestingSet(string[] args) {
            string[] testingSet = Directory.GetFiles(args[1], "*.pgm", SearchOption.AllDirectories);
            Console.WriteLine("Loading testing set...");

            BWImage img;
            int folderI;
            int counter = 0;    //number of corect evaluations
            for(int i = 0; i < testingSet.Length; i++) {

                folderI = getFolderInt(testingSet[i]);

                img = BWImage.createInstanceFromFile(testingSet[i]);
                if(img == null) {
                    Console.WriteLine("Could not load image {" + testingSet[i] + "}");
                    continue;
                }
                int vysledek = classifier.evaluate(img);

                if(folderI == vysledek) {
                    counter++;
                }
            }

            Console.WriteLine("Correct " + counter + " / " + testingSet.Length);
            Console.WriteLine("\t Success rate: {0:00.00}%", ((double)counter / testingSet.Length) * 100.0);
            Console.WriteLine("Testing set loaded (" + testingSet.Length + " files)");
        }

        /// <summary>
        /// Gets string of distances from list sorted by distance to image which is evauated
        /// </summary>
        /// <param name="report">list of distances to individual cases</param>
        /// <returns>formatted report from given distances</returns>
        private static string getReportFrom(List<KeyValuePair<int, double>> report) {
            String result = "";
            result += "Class : Distance \r\n";
            foreach(KeyValuePair<int, double> val in report) {
                result += String.Format("         {0:0} : {1:###.0} \r\n", val.Key, val.Value);
            }
            
            return result;
        }

        /// <summary>
        /// Writes report from evaluation to console
        /// </summary>
        /// <param name="report">list of distances to individual cases</param>
        private static void writeReport(List<KeyValuePair<int, double>> report) {
            Console.WriteLine("\t----Report----");
            foreach(KeyValuePair<int, double> val in report) {
                Console.WriteLine("\t\t"+val.Key + " : " + val.Value);
            }
            Console.WriteLine("\t--------------");
        }
    }
}
