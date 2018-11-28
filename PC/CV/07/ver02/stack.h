typedef struct {
	int sp;
	int size;
	int itemlen;
	char *data;
	} stack;

stack *createstack(int size, int itemlen);
void freestack(stack **s);
int push(stack *s, void *item);
int pop(stack *s, void *item);
