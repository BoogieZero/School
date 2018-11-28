using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Interface for all classifiers
    /// Provides common methods for manipulatin with classifiers
    /// </summary>
    interface IClassifier {
        /// <summary>
        /// Feeds classifier with training cases
        /// </summary>
        /// <param name="name">correct class for this training case</param>
        /// <param name="description">description of this training case</param>
        void addTrainCase(int name, double[] description);

        /// <summary>
        /// Evaluates given image and returns result
        /// </summary>
        /// <param name="image">image for evaluation</param>
        /// <returns>number of class which has been assigned to this image</returns>
        int evaluate(BWImage image);

        /// <summary>
        /// Saves learned model
        /// </summary>
        /// <param name="fileName">name of file for saving</param>
        void saveModel(String fileName);

        /// <summary>
        /// Loads saved model from given name
        /// </summary>
        /// <param name="fileName">name of file to load</param>
        void loadModel(String fileName);

        /// <summary>
        /// Returns descriptor assigned to this classifier
        /// </summary>
        /// <returns>assigned descriptor</returns>
        IDescriptor getDescriptor();

        /// <summary>
        /// Returns report of decisions in evaluation
        /// specifically distances to all classes
        /// </summary>
        /// <returns></returns>
        List<KeyValuePair<int, double>> getReportLast();
    }
}
