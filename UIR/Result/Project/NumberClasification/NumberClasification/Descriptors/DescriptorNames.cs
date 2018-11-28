using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NumberClasification {
    /// <summary>
    /// Represents marks for descriptors
    /// </summary>
    public enum DescriptorNames {
        HOG, SAMPLE, WEIGHT, LBP, NONE
    }
    
    
    public static class DescriptorNamesExtension {
        /// <summary>
        /// Returns coresponding mark to given descriptor
        /// </summary>
        public static DescriptorNames getName(IDescriptor descriptor) {
            if(descriptor is HOGDescriptor)                 return DescriptorNames.HOG;
            if(descriptor is LocalBinnaryPatternDescriptor) return DescriptorNames.LBP;
            if(descriptor is SamplesDescriptor)             return DescriptorNames.SAMPLE;
            if(descriptor is WeightDescriptor)              return DescriptorNames.WEIGHT;
            Console.WriteLine("No such descriptor found!");
            Environment.Exit(0);
            return DescriptorNames.NONE;
        }

        /// <summary>
        /// Returns new descriptor by given name.
        /// </summary>
        /// <param name="name">mark of descriptor in string form</param>
        /// <returns>new instance of descriptor by given name</returns>
        public static IDescriptor getNewDescriptor(String name) {
            
            DescriptorNames enDesc = (DescriptorNames)Enum.Parse(typeof(DescriptorNames), name.ToUpper(), true);

            IDescriptor descriptor = null;

            switch(enDesc) {
                case DescriptorNames.HOG:
                    descriptor = new HOGDescriptor();
                    break;
                case DescriptorNames.LBP:
                    descriptor = new LocalBinnaryPatternDescriptor();
                    break;
                case DescriptorNames.SAMPLE:
                    descriptor = new SamplesDescriptor();
                    break;
                case DescriptorNames.WEIGHT:
                    descriptor = new WeightDescriptor();
                    break;
                default:
                    Console.WriteLine("No such descriptor found!");
                    Environment.Exit(0);
                    break;
            }

            return descriptor;
        }
    }
}
