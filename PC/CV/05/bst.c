#include <stdio.h>
#include "tree.h"

int main() {
  node *root = NULL;
  
  add_node(&root, 10);
  add_node(&root, 5);
  add_node(&root, 20);
  add_node(&root, 1);
  
  print_node(root);     
  delete_node(&root);
  
  getchar();
  return 0;
}
