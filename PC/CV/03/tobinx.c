#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    int val, i, j, one = 0;
    
    if (argc < 2) {
             printf("Argument missing!\n");
             return 1;
    }
    
    val = atoi(argv[1]);
    printf("%d -> ", val);
    
    for (i = sizeof(val) * 8 - 1; i >= 0; i--) {
        j = (val >> i) & 1;
        if (j) one = 1;
        if (one) printf("%c", j + 48);
    }
    
    return 0;
}
