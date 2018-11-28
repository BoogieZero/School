

namespace Scc {
    /// <summary>
    /// Interface for methods for finding strongly connected components in graphs.
    /// </summary>
    interface SCCI {
        /// <summary>
        /// Writes discovered strongly connected components into console
        /// </summary>
        /// <param name="showComp">true for detail component descriptions</param>
        void WriteComponents(bool showComp);
    }
}
