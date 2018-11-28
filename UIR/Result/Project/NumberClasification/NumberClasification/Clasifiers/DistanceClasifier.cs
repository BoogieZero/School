using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Serialization;

namespace NumberClasification {
    /// <summary>
    /// Simple classifier using average of given train cases to creates ethalons for each class
    /// and computes Euclidean distance from those ethalons to evaluated image
    /// </summary>
    class DistanceClasifier : IClassifier {
        /// <summary>
        /// Used descriptor
        /// </summary>
        IDescriptor descriptor;
        
        /// <summary>
        /// Dictionary of all train cases where key is their correct class
        /// </summary>
        Dictionary<int, List<Double[]>> loadedTrainCases = new Dictionary<int, List<Double[]>>();

        /// <summary>
        /// List of ethalons created from train cases
        /// </summary>
        List<KeyValuePair<int, double[]>> finalPatterns;

        /// <summary>
        /// List of distances to individual ethalons used for sorting and reporting results
        /// </summary>
        List<KeyValuePair<int, double>> distances;

        /// <summary>
        /// Saves all important data to xml structure for later use.
        /// </summary>
        /// <param name="fileName">file name for saving</param>
        public void saveModel(String fileName) {
            Console.WriteLine("Saving model...");
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Indent = true;

            using(XmlWriter xwr = XmlWriter.Create(fileName, settings)) {
                xwr.WriteStartDocument();
                xwr.WriteStartElement("root");

                //classifier
                xwr.WriteStartElement("classifier");
                xwr.WriteAttributeString("name", "DISTANCE");
                xwr.WriteEndElement();  //classifier


                //descriptor
                xwr.WriteStartElement("descriptor");
                xwr.WriteAttributeString("name", "" + DescriptorNamesExtension.getName(descriptor));
                xwr.WriteEndElement();

                //patterns
                xwr.WriteStartElement("patterns");

                string field;
                foreach(KeyValuePair<int, double[]> pattern in finalPatterns) {
                    //pattern
                    xwr.WriteStartElement("pattern");
                    xwr.WriteAttributeString("key", pattern.Key + "");
                    field = "";
                    
                    foreach(double val in pattern.Value) {
                        field += val + " ";
                    }
                    xwr.WriteString(field);
                    xwr.WriteEndElement();
                }
                xwr.WriteEndElement();

                xwr.WriteEndDocument();
                xwr.Close();
            }
            Console.WriteLine("Model saved");
        }

        /// <summary>
        /// Loads data from saved model.
        /// </summary>
        /// <param name="fileName">file name to load</param>
        public void loadModel(String fileName) {
            Console.WriteLine("Loading model...");
            finalPatterns = new List<KeyValuePair<int, double[]>>();

            String descriptorName = "";

            if(!File.Exists(fileName)) {
                Console.WriteLine("File (" + fileName + ") doesn't exist!");
                Environment.Exit(0);
            }

            using(XmlReader xrr = XmlReader.Create(fileName)) {

                string key, field;
                while(xrr.Read()) {
                    if(xrr.IsStartElement()) {
                        switch(xrr.Name) {
                            case "descriptor":
                                descriptorName = xrr["name"];
                                break;
                            case "pattern":
                                key = xrr["key"];
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
        /// Creates pattern (ethalon) from loaded data.
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

            finalPatterns.Add(
                new KeyValuePair<int, double[]>(keyI, result)
                );
        }

        /// <summary>
        /// Returns field of distance from last evaluation
        /// </summary>
        /// <returns>field of sorted distances from last evaluation</returns>
        public List<KeyValuePair<int, double>> getReportLast() {
            return distances;
        }

        /// <summary>
        /// Adds new train case
        /// </summary>
        /// <param name="name">correct class of pattern</param>
        /// <param name="description">description of pattern</param>
        public void addTrainCase(int name, double[] description) {
            finalPatterns = null;   //ensure that evaulating will be commited with this added train example
            List<double[]> tc;

            if( loadedTrainCases.TryGetValue(name, out tc) ) {
                //list is already created
                tc.Add(description);
                //Console.WriteLine("Added to existing list");
            } else {
                //no such list found
                tc = new List<double[]>();
                tc.Add(description);
                loadedTrainCases.Add(name, tc);
                //Console.WriteLine("New list");
            }
        }
        
        /// <summary>
        /// Creates ethalons by averaging from stored train cases
        /// </summary>
        public void compileReadings() {
            finalPatterns = new List<KeyValuePair<int, double[]>>();

            foreach(KeyValuePair<int, List<Double[]>> entry in loadedTrainCases) {
                //for all classes
                finalPatterns.Add(
                    new KeyValuePair<int, double[]> (
                        entry.Key, getAverageInClass(entry.Value)
                        )
                    );
            }
        }

        /// <summary>
        /// Gets ethalon by averagin in one class
        /// </summary>
        /// <param name="cases">list of patterns in same class</param>
        /// <returns>constructed ethalon</returns>
        private double[] getAverageInClass(List<Double[]> cases) {
            double[] fPattern = new double[descriptor.getDescriptionVectorLength()];

            foreach(double[] cs in cases) {
                //for all cases in actual class
                //sum over all cases
                for(int i = 0; i < fPattern.Length; i++) {
                    //for all components in case
                    fPattern[i] += cs[i];
                }
            }

            //average
            for(int i = 0; i < fPattern.Length; i++) {
                //for all components in case
                fPattern[i] /= cases.Count;
            }

            return fPattern;
        }

        /// <summary>
        /// Evaluates given image and returns result.
        /// </summary>
        /// <param name="image">image for evaluation</param>
        /// <returns>number of class which has been assigned to this image</returns>
        public int evaluate(BWImage image) {
            if(finalPatterns == null) {
                //preprocess loaded training data if they are not
                compileReadings();
            }

            double[] imgDesc = descriptor.getDescription(image);

            distances = new List<KeyValuePair<int, double>>();

            foreach(KeyValuePair<int, double[]> pattern in finalPatterns) {
                //distance between pattern and image
                distances.Add(
                    new KeyValuePair<int, double>(pattern.Key, distance(
                        pattern.Value, imgDesc
                        ))
                    );                          //key = pattern name , value = distance to that pattern
            }

            distances.Sort(
                delegate (KeyValuePair<int, double> a, KeyValuePair<int, double> b) {
                    return a.Value.CompareTo(b.Value);
                }
            );     //sort by distance

            return distances[0].Key;
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
        /// Returns used descriptor
        /// </summary>
        /// <returns></returns>
        public IDescriptor getDescriptor() {
            return descriptor;
        }

        /// <summary>
        /// Creates new classifier by given descriptor
        /// </summary>
        /// <param name="descriptor"></param>
        public DistanceClasifier(IDescriptor descriptor) {
            this.descriptor = descriptor;
        }
        
        /// <summary>
        /// Empty constructor used for loading from saved file
        /// </summary>
        public DistanceClasifier() {

        }
    }
}
