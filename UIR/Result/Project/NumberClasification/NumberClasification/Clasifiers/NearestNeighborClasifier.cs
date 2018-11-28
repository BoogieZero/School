using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace NumberClasification {
    /// <summary>
    /// K-Nearest neighbor classifier which decides about given description by calculating distances to all train cases and
    /// case with most frequency in K nearest is chosen where K is KNN_FACTOR
    /// </summary>
    class NearestNeighborsClasifier : IClassifier{
        /// <summary>
        /// K factor of classifier
        /// </summary>
        private const int KNN_FACTOR = 5;

        /// <summary>
        /// Number of classified classes
        /// </summary>
        private const int NUMBER_OF_CLASSES = 10;

        /// <summary>
        /// Used descriptor
        /// </summary>
        private IDescriptor descriptor;

        /// <summary>
        /// Dictionary of all added train cases where key is correct class of that train case
        /// </summary>
        private Dictionary<int, List<Double[]>> loadedTrainCases = new Dictionary<int, List<Double[]>>();

        /// <summary>
        /// List of distances to individual ethalons used for sorting and reporting results
        /// </summary>
        private List<KeyValuePair<int, double>> distances;

        /// <summary>
        /// Adds new train case
        /// </summary>
        /// <param name="name">correct class of pattern</param>
        /// <param name="description">description of that pattern</param>
        public void addTrainCase(int name, double[] description) {

            List<double[]> tc;
            if(loadedTrainCases.TryGetValue(name, out tc)) {
                //list is already created
                tc.Add(description);
            } else {
                //no such list found
                tc = new List<double[]>();
                tc.Add(description);
                loadedTrainCases.Add(name, tc);
            }
        }

        /// <summary>
        /// Evaluates given image and returns result.
        /// </summary>
        /// <param name="image">image for evaluation</param>
        /// <returns>number of class which has been assigned to this image</returns>
        public int evaluate(BWImage image) {

            double[] imgDesc = descriptor.getDescription(image);

            distances = new List<KeyValuePair<int, double>>();

            //distance between every loaded case and examined description -> added to distances
            foreach(KeyValuePair<int, List<Double[]>> bucket in loadedTrainCases) {
                //for every bucket (with cases that belongs to same number)
                foreach(double[] pattern in bucket.Value) {
                    //for every case in bucket
                    distances.Add(
                        new KeyValuePair<int, double>(bucket.Key, distance(
                            pattern, imgDesc
                            ))
                        );  
                }
            }

            distances.Sort(
                delegate (KeyValuePair<int, double> a, KeyValuePair<int, double> b) {
                    return a.Value.CompareTo(b.Value);
                }
            );     //sort by distance

            int winnerKey = -1;
            int winnerValue = int.MinValue;

            int[] found = new int[NUMBER_OF_CLASSES];   //index = class ; value = number of found examples

            int keyFound;
            //frequency in distances
            for(int i = 0; i < KNN_FACTOR; i++) {
                keyFound = distances[i].Key;
                found[keyFound]++;
                if(found[keyFound] > winnerValue) {
                    winnerKey = keyFound;
                    winnerValue = found[keyFound];
                }
            }

            return winnerKey;
        }

        /// <summary>
        /// Calculates Euclidean distance between given patterns
        /// </summary>
        /// <param name="pattern">pattern A</param>
        /// <param name="target">patterns B</param>
        /// <returns>Eclidean distance between A and B</returns>
        private double distance(double[] pattern, double[] target) {
            double sum = 0;

            for(int i = 0; i < pattern.Length; i++) {
                sum += (pattern[i] - target[i]) * (pattern[i] - target[i]);
            }

            return Math.Sqrt(sum);
        }

        /// <summary>
        /// Returns field of distance from last evaluation
        /// </summary>
        /// <returns>field of sorted distances from last evaluation</returns>
        public List<KeyValuePair<int, double>> getReportLast() {
            return distances;
        }

        /// <summary>
        /// Saves all important data to xml structure for later use.
        /// </summary>
        /// <param name="fileName">file name for saving</param>
        public void saveModel(string fileName) {
            Console.WriteLine("Saving model...");
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Indent = true;

            using(XmlWriter xwr = XmlWriter.Create(fileName, settings)) {
                xwr.WriteStartDocument();
                xwr.WriteStartElement("root");

                //classifier
                xwr.WriteStartElement("classifier");
                xwr.WriteAttributeString("name", "KNN");
                xwr.WriteEndElement();  //classifier

                //descriptor
                xwr.WriteStartElement("descriptor");
                xwr.WriteAttributeString("name", "" + DescriptorNamesExtension.getName(descriptor));
                xwr.WriteEndElement();

                //cases
                xwr.WriteStartElement("cases");

                string field;
                foreach(KeyValuePair<int, List<double[]>> caseFile in loadedTrainCases) {
                    //case
                    xwr.WriteStartElement("case");
                    xwr.WriteAttributeString("key", caseFile.Key + "");

                    //individual patterns

                    foreach(double[] pattern in caseFile.Value) {
                        xwr.WriteStartElement("pattern");

                        field = "";
                        foreach(double val in pattern) {
                            field += val + " ";
                        }
                        xwr.WriteString(field);

                        xwr.WriteEndElement();  //pattern
                    }
                    

                    xwr.WriteEndElement();  //case
                }

                xwr.WriteEndElement();  //cases

                xwr.WriteEndDocument();
                xwr.Close();
                Console.WriteLine("Model saved");
            }
        }

        /// <summary>
        /// Loads data from saved model.
        /// </summary>
        /// <param name="fileName">file name to load</param>
        public void loadModel(string fileName) {
            Console.WriteLine("Loading model...");
            String descriptorName = "";

            if(!File.Exists(fileName)) {
                Console.WriteLine("File (" + fileName + ") doesn't exist!");
                Environment.Exit(0);
            }

            using(XmlReader xrr = XmlReader.Create(fileName)) {

                string key = "";
                string field = "";

                while(xrr.Read()) {
                    if(xrr.IsStartElement()) {
                        switch(xrr.Name) {
                            case "descriptor":
                                descriptorName = xrr["name"];
                                break;
                            case "case":
                                key = xrr["key"];
                                break;
                            case "pattern":
                                xrr.Read();
                                field = xrr.Value.Trim();
                                addPattern(key, field);
                                break;
                        }
                    }
                }

                descriptor = DescriptorNamesExtension.getNewDescriptor(descriptorName);
                xrr.Close();
                Console.WriteLine("Model loaded");
            }
        }

        /// <summary>
        /// Creates train case from loaded data and place it in loadedTrainCases
        /// </summary>
        /// <param name="key">coresponding class of the pattern</param>
        /// <param name="field">description of the pattern</param>
        private void addPattern(string key, string field) {
            int keyI = int.Parse(key);
            string[] fieldS = field.Split(' ');
            double[] result = new double[fieldS.Length];

            int k = 0;
            foreach(string str in fieldS) {
                result[k] = double.Parse(str);
                k++;
            }
            addTrainCase(keyI, result);
        }

        /// <summary>
        /// Returns used descriptor
        /// </summary>
        /// <returns>used descriptor</returns>
        public IDescriptor getDescriptor() {
            return descriptor;
        }

        /// <summary>
        /// Creates new classifier by given descriptor
        /// </summary>
        /// <param name="descriptor"></param>
        public NearestNeighborsClasifier(IDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        /// <summary>
        /// Empty constructor used for loading from saved file
        /// </summary>
        public NearestNeighborsClasifier() {

        }

    }
}
