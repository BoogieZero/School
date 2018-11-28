#include <stdlib.h>
#include "stack.h"

stack *createstack(int size, int itemlen) {
	stack *temp = malloc(sizeof(stack));

	temp->data = (char *) malloc(size * itemlen);
	temp->sp = -1;
	temp->size = size;
	temp->itemlen = itemlen;

	return temp;
}

void freestack(stack **s) {
	free((*s)->data);
	free(*s);
	*s = NULL;
}

int push(stack *s, void *item) {
	int i;

	if (s->sp < s->size) {
		s->sp += 1;
		for (i = 0; i < s->itemlen; i++)
			s->data[s->sp * s->itemlen + i] = ((char *) item)[i];

		return 1;
	}
		return 0;
}

int pop(stack *s, void *item) {
	int i;

	if (s->sp >= 0) {
		for (i = 0; i < s->itemlen; i++)
			((char *) item)[i] = s->data[s->sp * s->itemlen + i];
		s->sp -= 1;

		return 1;
	}
		return 0;
}
