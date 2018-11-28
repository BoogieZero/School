#include <stdlib.h>
#include <stdio.h>
#include "tree.h"

int add_node(node **root, int key) {
  node *walker = *root;
  node *newnode = (node *) malloc(sizeof(node));
  if (!newnode) return 0;

  newnode->key = key;
  newnode->left = newnode->right = NULL;
  
  if (!*root) *root = newnode;
  else {
    while (walker) {
      if (key < walker->key) {
        if (walker->left) walker = walker->left;
        else {
          walker->left = newnode;
          break;
        }
      }
      else {
        if (walker->right) walker = walker->right;
        else {
          walker->right = newnode;
          break;
        }
      }
    }
  }
  
  return 1;
}

void print_node(node *root) {
  if (!root) {
    printf(" NULL");
    return;
  }
  
  printf("%d (", root->key);
  print_node(root->left);
  print_node(root->right);
  printf(") ");
}

void delete_node(node **root) {
  if (!*root) return;
  
  delete_node(&(*root)->left);
  delete_node(&(*root)->right);
  free(*root);
  *root = NULL;
}
