#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[]) {
     int val = 0, i, j;
     char str[sizeof(int) * 8];
     
     if (argc < 2) {
              printf("Argument missing.");
              return 0;
              }
              
     val = atoi(argv[1]);
     
     i = 0;
     while (val > 0) {
           str[i] = (val % 2) + 48;
           val /= 2;
           i++;
           }
           
     for (j = i - 1; j >= 0; j--) printf("%c", str[j]);
     printf("\n");
     
     return 0;
}
