#ifndef _MATRIX_H
#define _MATRIX_H

typedef struct {
  int rows, cols;
  double *items;
} matrix;

matrix *create_matrix(int rows, int cols, double val);
void free_matrix(matrix **m);

double get_item(matrix *m, int row, int col);
void set_item(matrix *m, int row, int col, double val);

void print_matrix(matrix *m);

#endif
