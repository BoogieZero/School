#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[]) {
     int val = 0, i, j, one;
     
     if (argc < 2) {
              printf("Argument missing.");
              return 0;
              }
              
     val = atoi(argv[1]);

     one = 0;
     for (i = sizeof(val) * 8 - 1; i >=0; i--) {
         j = (val >> i) & 1;
         if (j) one = 1;
         if (one) printf("%c", j + 48);
     }     
     
     getch();
     return 0;
}
