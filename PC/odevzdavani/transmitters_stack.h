#ifndef _STACK_H
#define _STACK_H
#include "transmitters.h"

/*
Creates new stack for transmitters and returns pointer to it's top.
*/
transmitters *t_stack_create_stack();

/*
Adds new transmitter value on top of stack **stack.
*/
void t_stack_push(transmitters **stack, transmitter *value);

/*
Removes top of stack and saves pointer to this transmitter to given *result.
If stack is empty returns 1 otherwise returns 0.
*/
int t_stack_pop(transmitters **stack, transmitter **result);

/*
Removes remaining values from stack and frees theirs allocated memory.
*/
void t_stack_free_stack(transmitters **stack);

#endif
