using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Interface for all descriptors
    /// Provides common methods for manipulatin with descriptors
    /// </summary>
    public interface IDescriptor {
        /// <summary>
        /// Creates description for given image
        /// </summary>
        /// <param name="image">processed image</param>
        /// <returns>description for given image</returns>
        double[] getDescription(BWImage image);

        /// <summary>
        /// Returns length of description
        /// </summary>
        /// <returns></returns>
        int getDescriptionVectorLength();
    }
}
