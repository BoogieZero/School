#include <stdlib.h>
#include "matrix.h"

matrix *create_matrix(int rows, int cols, double val) {
  int i;
  matrix *temp = (matrix *) malloc(sizeof(matrix));
  if (!temp) return NULL;
  
  temp->rows = rows;
  temp->cols = cols;
  temp->items = (double *) malloc(rows * cols * sizeof(double));
  if (!temp->items) {
    free(temp);
    return NULL;
  }
  
  for (i = 0; i < rows * cols; i++) 
    ((double *) temp->items)[i] = val;
  
  return temp;
}

void free_matrix(matrix **m) {
  if (!*m) return;
  
  free((*m)->items);
  free(*m);
  *m = NULL;
}

double get_item(matrix *m, int row, int col) {
  if (!m) return 0.0;
  if (row >= m->rows || row < 0) return 0.0;
  if (col >= m->cols || col < 0) return 0.0;
  
  return m->items[row * m->cols + col];
}

void set_item(matrix *m, int row, int col, double val) {
  if (!m) return;
  if (row >= m->rows || row < 0) return; 
  if (col >= m->cols || col < 0) return;
  
  m->items[row * m->cols + col] = val;
}


void print_matrix(matrix *m) {
  int i, j;
  if (!m) return;
  
  for (i = 0; i < m->rows; i++) {
    printf("| ");    
    for (j = 0; j < m->cols; j++)
      printf("%lf ", get_item(m, i, j)); 
    printf("|\n");
  }
}
