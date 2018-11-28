#include <stdlib.h>
#include "stack.h"

stack *create_stack(int size, int itemlen) {
	stack *temp = (stack *) malloc(sizeof(stack));
	if (!temp) return NULL;

	temp->size = size;
	temp->itemlen = itemlen;
	temp->sp = -1;
	temp->data = malloc(size * itemlen);
	if (!temp->data) {
		free(temp);
		return NULL;
	}

	return temp;
}

void free_stack(stack **s) {
	if (!*s) return;
	free((*s)->data);
	free(*s);
	*s = NULL;
}

int push(stack *s, void *d) {
	int i;
	if (!s || !d) return 0;
	if (s->sp >= s->size - 1) return 0;

	s->sp++;
	for (i = 0; i < s->itemlen; i++)
		((char *) s->data)[s->sp * s->itemlen + i] = ((char *) d)[i];

	return 1;
}

int pop(stack *s, void *d) {
	int i;
	if (!s || !d) return 0;
	if (s->sp < 0) return 0;

	for (i = 0; i < s->itemlen; i++)
		((char *) d)[i] = ((char *) s->data)[s->sp * s->itemlen + i];
	s->sp--;

	return 1;
}

