using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Bludiste {
    /// <summary>
    /// Class represents stack for Cells wraped in Item class and provides basic functions expected from stack.
    /// </summary>
    class Stack {
        /// <summary>
        /// Top item in stack
        /// null if stack is empty
        /// </summary>
        private Item top;


        /// <summary>
        /// Default constructor for stack
        /// </summary>
        public Stack() {
            top = null;
           
        }

        /// <summary>
        /// Checks if stack is empty.
        /// </summary>
        /// <returns> true if stack is empty</returns>
        public Boolean isEmpty() {
            return top == null;
        }

        /// <summary>
        /// Adds new Cell into stack.
        /// </summary>
        /// <param name="item"> cell which is added to stack </param>
        public void push(Map.Cell item) {
            Item newItem = new Item(item);
            if(isEmpty()) {
                top = newItem;
                return;
            }
            newItem.next = top;
            top = newItem;
           
        }

        /// <summary>
        /// Returns top of the stack.
        /// </summary>
        /// <returns> top of the stack </returns>
        public Map.Cell pop() {
            Map.Cell poped = top.value;
            if(top.next == null) {
                top = null;
            } else {
                top = top.next;
            }
   
            return poped;
        }

        /// <summary>
        /// Wrapper class for Cell to be inserted in the stack
        /// </summary>
        class Item {
            public Map.Cell value;
            public Item next;

            public Item(Map.Cell value) {
                this.value = value;
                next = null;
            }
        }
    }
}
