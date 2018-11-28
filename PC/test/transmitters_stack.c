#include <stdlib.h>
#include <stdio.h>
#include "transmitters_stack.h"
#include "my_malloc.h"
#define malloc(t_size) my_malloc(t_size)

/*
Prepares new empty stack and returns pointer to it's top
*/
transmitters *t_stack_create_stack(){
	transmitters *stck = NULL;
	return stck;
}

/*
Adds transmitter value into stack given by "**stack" (pointer on top of the stack)
*/
void t_stack_push(transmitters **stack, transmitter *value){
	/* new element */
	transmitters *newtrans = malloc(sizeof(transmitters));
	newtrans->transmitter = value;
	
	if(*stack == NULL){
		/* stack is empty */
		*stack = newtrans;
		newtrans->next = NULL;
	}else{
		/* stack isn't empty */
		newtrans->next = *stack;
		*stack = newtrans;
	}
}

/*
Sets top of stack given by "**stack" (pointer on top of the stack) to pointer  *result
returns 1 if stack is empty otherwise returns 0
*/
int t_stack_pop(transmitters **stack, transmitter **result){
	transmitters *old;	/* pointer to actual top value */
	
	if(*stack == NULL){
		/* stack is empty */
		return 1;
	}
	
	*result = (*stack)->transmitter;	/* set *result to top value */
	old = *stack;
	
	if((*stack)->next != NULL){
		/* set top on next value and clear removed (old) value */
		*stack = (*stack)->next;
		free(old);
	}else{
		/* last value in stack */
		free(old);
		*stack = NULL;
	}
	return 0;
}

/*
Clear any remaining values from stack and frees it's allocated memory
*/
void t_stack_free_stack(transmitters **stack){
	transmitter *dummy;	/* pointer to result from t_stack_pop() */
	
	if(*stack == NULL){
		/* stack is empty */
		return;
	}
	
	while(!t_stack_pop(stack, &dummy));
}


