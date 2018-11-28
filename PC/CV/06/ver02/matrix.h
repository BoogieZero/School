#ifndef _MATRIX_H
#define _MATRIX_H

typedef struct {
  int rows, cols;
  double **items;
} matrix;

matrix *create_matrix(int rows, int cols, double val);
void free_matrix(matrix **m);
void print_matrix(matrix *m);

#endif
