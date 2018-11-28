#ifndef _STACK_H
#define _STACK_H

typedef struct {
	int size;		/* the overall size of the stack */
	int itemlen;	/* the length of the item */
	int sp;			/* stack pointer */
	void* data;
} stack;

stack *create_stack(int size, int itemlen);
void free_stack(stack **s);

int push(stack *s, void *d);
int pop(stack *s, void *d);

#endif
