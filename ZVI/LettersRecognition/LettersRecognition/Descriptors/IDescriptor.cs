using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LettersRecognition {
    /// <summary>
    /// Interface for descriptors.
    /// </summary>
    public interface IDescriptor {
        /// <summary>
        /// Returns description for fragment with given index and it's parrent image.
        /// </summary>
        /// <param name="image">source image</param>
        /// <param name="index">index of fragment</param>
        /// <returns>description vector</returns>
        double[] getDescription(Image image, int index);

        /// <summary>
        /// Sets attributes for given fragment.
        /// </summary>
        /// <param name="fragment">source fragment</param>
        /// <param name="image">parrent image for the fragment</param>
        void setAttributes(Image.Fragment fragment, Image image);

        /// <summary>
        /// Returns name of a descriptor.
        /// </summary>
        /// <returns>name of a descriptor</returns>
        string getName();

        /// <summary>
        /// Returns length of description vector.
        /// </summary>
        /// <returns>length of description vector</returns>
        int getDescriptionVectorLength();
    }
}
