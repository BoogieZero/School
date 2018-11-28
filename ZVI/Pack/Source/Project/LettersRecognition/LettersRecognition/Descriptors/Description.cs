using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LettersRecognition {
    /// <summary>
    /// Encapsulates description of a single fragment.
    /// </summary>
    public class Description {
        /// <summary>
        /// Description vector.
        /// </summary>
        public double[] vektor;

        /// <summary>
        /// Classified result.
        /// </summary>
        public String result;

        /// <summary>
        /// Attributes of minimal rectangle surrounding a fragment.
        /// </summary>
        #region minMax with rotation
        public PointF rotOrigin;
        public float angle;
        public float minX;
        public float maxX;
        public float minY;
        public float maxY;
        public float width;
        public float height;
        #endregion minMax with rotation

        /// <summary>
        /// Volume
        /// </summary>
        public double volume;

        /// <summary>
        /// Center of gravity point.
        /// </summary>
        public PointF balancePoint;

        /// <summary>
        /// Lognitude
        /// </summary>
        public double longitude;

        /// <summary>
        /// x-coordinate for center of gravity
        /// </summary>
        public double minRecCoGX;

        /// <summary>
        /// y-coordinate for center of gravity
        /// </summary>
        public double minRecCoGY;

        /// <summary>
        /// Number of detected holes.
        /// </summary>
        public int numOfHoles;

        /// <summary>
        /// Report of distances to other results from cassifier.
        /// </summary>
        public string distanceReport;
    }
}
