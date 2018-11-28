#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    int val, i, j;
    char buf[sizeof(int) * 8];
    
    if (argc < 2) {
             printf("Argument missing!\n");
             return 1;
    }
    
    val = atoi(argv[1]);
    printf("%d -> ", val);
    
    i = 0;
    while (val > 0) {
          buf[i] = val % 2 + 48;
          val /= 2;
          i++;
    }
    
    for (j = i - 1; j >= 0; j--)
          printf("%c", buf[j]);
    
    return 0;
}
