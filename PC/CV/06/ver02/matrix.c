#include <stdlib.h>
#include <stdio.h>
#include "matrix.h"

matrix *create_matrix(int rows, int cols, double val) {
  int i, j;
  matrix *temp = (matrix *) malloc(sizeof(matrix));
  if (!temp) return NULL;
  
  temp->rows = rows;
  temp->cols = cols;
  temp->items = malloc(sizeof(double *) * rows);
  if (!temp->items) {
    free(temp);
    return NULL;
  }
  
  for (i = 0; i < rows; i++) {
    temp->items[i] = malloc(cols * sizeof(double));
    if (temp->items[i])
      for (j = 0; j < cols; j++) temp->items[i][j] = val;
  }
  
  return temp;
}

void free_matrix(matrix **m) {
  int i;
  
  if (!*m) return;
  
  for (i = 0; i < (*m)->rows; i++) free((*m)->items[i]);
  free((*m)->items);
  free(*m);
  *m = NULL;
}

void print_matrix(matrix *m) {
  int i, j;
  
  if (!m) return;
  
  for (i = 0; i < m->rows; i++) {
    printf("| ");
    for (j = 0; j < m->cols; j++)
      printf("%.2lf ", m->items[i][j]);
    printf("|\n");      
  }
}
