#include <stdio.h>
#include "matrix.h"

int main() {
  matrix *a;
  
  a = create_matrix(5, 5, 1.0);
  print_matrix(a);
  free_matrix(&a);
  
  getchar();
  return 0;
}
