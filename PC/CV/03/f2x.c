#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    float val;
    int *pval;
    
    if (argc < 2) {
             printf("Argument missing!\n");
             return 1;
    }
    
    val = atof(argv[1]);
    pval = &val;
    printf("%f -> 0x%X\n", val, *pval);
    
    return 0;
}
