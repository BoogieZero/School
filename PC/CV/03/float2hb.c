#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[]) {
	float val = 0.0;
	int *pval;

	if (argc < 2) {
		printf("Argument missing.\n");
		return 0;
	}

	val = atof(argv[1]);
    pval = &val;

    printf("%f -> 0x%X\n", val, *pval);

    getch();
	return 0;
}
