#include <stdlib.h>
#include <stdio.h>
#include "stack.h"

int main() {
	stack *s = NULL;
	double x;
	int i;

	s = create_stack(100, sizeof(double));

	for (i = 0; i < 10; i++) {
		x = rand();
		push(s, &x);
		printf("%d push %lf\n", i, x);
	}

	printf("\n");

	while (pop(s, &x)) {
		printf("%d pop %lf\n", --i, x);
	}

	free_stack(&s);

	return 0;
}
