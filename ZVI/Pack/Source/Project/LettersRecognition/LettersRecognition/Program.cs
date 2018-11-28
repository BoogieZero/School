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

namespace LettersRecognition {
    /// <summary>
    /// Main class operetaing function of program
    /// </summary>
    static class Program {
        #region Console Attachment
        /// <summary>
        /// Attachment of console to windows forms apllication
        /// </summary>
        /// <param name="dwProcessId"></param>
        /// <returns></returns>
        [DllImport("kernel32.dll")]
        static extern bool AttachConsole(int dwProcessId);
        private const int ATTACH_PARENT_PROCESS = -1;

        #endregion Console Attachment

        /// <summary>
        /// Names for files needed.
        /// </summary>
        #region file names
        private const String TRAINING_DIR_DEFAULT   = "TrainingData";
        private const String TESTING_DIR_DEFAULT    = "TestingData";
        /// <summary>
        /// Name for created model from training data
        /// </summary>
        private const String CLASSIFIER_MODEL_NAME  = "Model.data";
        #endregion file names

        /// <summary>
        /// Instance of clasifier that will be used for clasification
        /// </summary>
        private static DistanceClasifier classifier;

        /// <summary>
        /// Intance of descriptor that will be used for description of given image
        /// </summary>
        private static IDescriptor descriptor;

        /// <summary>
        /// Instance of form from GUI to be able to display processed image
        /// </summary>
        public static Form1 form;

        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(String[] args) {
            AttachConsole(ATTACH_PARENT_PROCESS);
            descriptor = new DefaultDescriptor();
            //GUI start
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            form = new Form1();
            Application.Run(form);

        }

        /// <summary>
        /// Evaluates given bitmap.
        /// From bitmap instnce of Image is created.
        /// All fragments in the image are processed and results are commited to GUI.
        /// </summary>
        /// <param name="bmp">bitmap for evaluation</param>
        public static void evaluateBitmap(Bitmap bmp) {
            //segment
            Image im = new Image(bmp, descriptor);

            foreach(Image.Fragment frag in im.fragments) {
                frag.description.result = classifier.evaluate(frag);
            }
            
            form.fillListBox(im);
        }

        /// <summary>
        /// Sets proper clasifier by saved model
        /// </summary>
        /// <param name="args">name of the saved model</param>
        private static void loadFromModel(String fileName) {

            if(!File.Exists(fileName)) {
                Console.WriteLine("File (" + fileName + ") doesn't exist!");
                Environment.Exit(0);
            }

            using(XmlReader xrr = XmlReader.Create(fileName)) {

                while(xrr.Read()) {
                    if(xrr.IsStartElement()) {
                        if(xrr.Name.Equals("classifier")) {
                            switch(xrr["name"].ToUpper()) {
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
        /// Returns name coresponding to name of the folder in the given path.
        /// Folder name represents correct output.
        /// </summary>
        /// <param name="path">path to image</param>
        /// <returns>expected output of the recognition</returns>
        static String getFolderName(String path) {
            string folder = (path.Split('\\'))[1];    //folder name is after one '\'
            return folder;
        }

        /// <summary>
        /// Trains clasifier from TrainingData folder. Folder names are considered as name of the desired result for
        /// images it contains.
        /// </summary>
        public static void trainClassifier() {
            classifier = new DistanceClasifier(descriptor);
            string[] trainingSet = Directory.GetFiles(TRAINING_DIR_DEFAULT, "*.bmp", SearchOption.AllDirectories);
            
            double[] dsc;
            Image img;
            String result;  //folder name
            for(int i = 0; i < trainingSet.Length; i++) {
                img = new Image(trainingSet[i], descriptor);
                result = getFolderName(trainingSet[i]);

                dsc = descriptor.getDescription(img,0);
                classifier.addTrainCase(result, dsc);
            }
            classifier.compileReadings();
            classifier.saveModel(CLASSIFIER_MODEL_NAME);
        }
        
        /// <summary>
        /// Loads trained classifier model from file.
        /// </summary>
        public static void loadClassifier() {
            loadFromModel(CLASSIFIER_MODEL_NAME);
        }
        
    }
}
