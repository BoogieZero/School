#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "stack.h"

#define TOK_UNKNOWN 0
#define TOK_PLUS 1
#define TOK_MINUS 2
#define TOK_ASTER 3
#define TOK_SLASH 4

#define TOK_NUMBER 100

int currpos = 0;
char allowed[] = "+-*/0123456789.Ee";

char getnext(char *text) {
	char c = text[currpos];

	if (c == 0) return 0;
	else {
		currpos++;
		return c;
	}
}

int scan(char *text, double *value) {
	int i;
	char c;
	char temp[256];
	
	*value = 0.0;

	do {
		c = getnext(text);
	} while (strchr(allowed, (int) c) == NULL || c != 0);

	if (c == 0) return TOK_UNKNOWN;

	switch (c) {
		case '+': return TOK_PLUS;
		case '-': return TOK_MINUS;
		case '*': return TOK_ASTER;
		case '/': return TOK_SLASH;
		default: {
					 i = 0;
					 while (strchr(allowed + 4, (int) c)) {
					 	temp[i] = c;
					 	c = getnext(text);
						if (c == 0) break;
						i++;
					 }
					 temp[i] = 0;

					 *value = atof(temp);
					 return TOK_NUMBER;
				 }
	}

	return TOK_UNKNOWN;
}

int main() {
	int run = 1;
	char inp[] = "2 3 + 5 5 + *";
	stack *s;
	double x, y;

	s = createstack(128, sizeof(double));

	do {
		switch (scan(inp, &x)) {
			case TOK_PLUS: pop(s, &x);
					  pop(s, &y);
					  x = x + y;
					  push(s, &x);
					  break;
			case TOK_ASTER: pop(s, &x);
					   pop(s, &y);
					   x = x * y;
					   push(s, &x);
					   break;
			case TOK_NUMBER: push(s, &x);
						break;
            case TOK_UNKNOWN: run = 0;
						 break;
		}
	} while (run);

	pop(s, &x);
	printf("Result: %lf\n", x);

	freestack(&s);

	return 0;
}
