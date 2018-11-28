#ifndef _TREE_H
#define _TREE_H

typedef struct thenode {
  int key;
  struct thenode *left, *right;
} node;

int add_node(node **root, int key);
void print_node(node *root);
void delete_node(node **root);

#endif
