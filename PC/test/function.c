#include <stdlib.h>
#include <stdio.h>
#include "function.h"
#include "transmitters_stack.h"

/*
Minimal distance between two transmitters so they don't colide
*/
static int power_radius = NULL;

/*
Top of stack which is used in assigning frequencies for neighbours of actually 
processed transmitter
*/
static transmitters *stack;

/*
Frequency used to mark encountered transmitters
*/
static frequency *f_encountered;

/*
	Creates pointer to pointer to NULL so
	f_encountered haven't NULL value but is not usable structure frequency
	this pointer is used to mark encountered transmiters which haven't assigned frequency yet
	but are in a stack and frequency will be assigned shortly after
	meaning of this mark is to prevent duplicate transmitters in the stack
*/
void create_encountered_freq(){
	frequency *f = NULL;
	f_encountered = (frequency*)&f;
}

/*
Finds collisions between each transmitter. Every collision is represented by 
list of neighbours (which are colliding) of each individual transmitter
*/
void function_find_collisions(transmitters *head){
	transmitters *p_main, *p_minor;
	p_main = head;
	power_radius = 2 * transmitters_get_radius();
	power_radius = power_radius * power_radius;
	
	/* for each transmitter*/
	while(p_main != NULL){
		p_minor = p_main->next;
		/* for each left transmitter*/
		while(p_minor != NULL){
			
			/*for each left transmiter calculate if distance is enough for avoiding collision*/
			if(calculate_collision(p_main->transmitter, p_minor->transmitter)){
				/*p_main and p_minor are too close together*/
				transmitters_add_neighbour(p_main->transmitter, p_minor->transmitter);
				transmitters_add_neighbour(p_minor->transmitter, p_main->transmitter);
			}
			p_minor = p_minor->next;
		}
		p_main = p_main->next;
	}
}

/*
Calculates distance between transmitter t1 and t2,
if the distance is less than collision radius returns 1 otherwise returns 0
*/
int calculate_collision(transmitter *t1, transmitter *t2){
	double x1, x2, y1, y2, length;
	
	x1 = t1->coordinates->x;
	y1 = t1->coordinates->y;
	x2 = t2->coordinates->x;
	y2 = t2->coordinates->y;
	
	/* distance btween t1 and t2 by power of 2 */
	length = (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);
	
	if(length <= power_radius){
		/* collision */
		return 1;
	}
	return 0;
}

/*
Checks if given transmitter have assigned frequency which does not collide 
with his neighbours, if so returns 0 otherwise returns 1, and
adds non-encountered neighbours into stack
*/
int is_possible_to_assign(transmitter *tran){
	int id;
	
	id = tran->frequency->id;
	neighbours *neigh = tran->neighbours;
	
	if(neigh == NULL){
		/* collision isn't possible for this transmitter */
		return 0;
	}
	
	/* for all neighbour of tran */
	while(neigh != NULL){
		
		/* non-encountered transmitter */
		if(neigh->transmitter->frequency == NULL){
			/* adding non-encountered transmiter to stack*/
			neigh->transmitter->frequency = f_encountered;
			t_stack_push(&stack, neigh->transmitter);
			
			neigh = neigh->next;
			continue;
		}
		
		/* encountered transmitter without assigned frequency */
		if(neigh->transmitter->frequency == f_encountered){
			
			neigh = neigh->next;
			continue;
		}
		
		/* transmitter with assigned frequency */
		if(	neigh->transmitter->frequency != NULL
			&& id == neigh->transmitter->frequency->id){
			/* there is already this frequency assigned */
			return 1;
		}
		neigh = neigh->next;
		
	}
	/* assigned frequency is valid */
	return 0;
}

/*
Assigning to transmitter tran available frequencies until given transmitter does not collide with others
if frequency was successfully assigned returns 0 otherwise returns 1
*/
int assign_frequency(transmitter *tran, frequencies *freqs_head){
	frequencies *freqs = freqs_head;
	
	/* until valid frequency si assigned to transmitter tran */
	do{
		if(freqs == NULL){
			/* there is not enough available frequencies */
			return 1;
		}
		
		tran->frequency = freqs->frequency;
		freqs = freqs->next;
	}while(is_possible_to_assign(tran) == 1);
	
	/* frequency was succesfully assigned */
	return 0;
}

/*
Tryes to assign available frequencies to all transmitters, 
if assigning was successfull returns 0 otherwise returns 1
*/
int function_assing_frequencies(transmitters *trans_head, frequencies *freqs_head, transmitters **m_stack){
	transmitters *t_main = trans_head;
	transmitter *t_minor;
	
	stack = t_stack_create_stack();
	*m_stack = stack;
	create_encountered_freq();
	
	/* for all transmitters */
	while(t_main != NULL){
		
		if(t_main->transmitter->frequency == NULL){
			/* push non-encountered transmitter to stack */
			t_stack_push(&stack, t_main->transmitter);
		}else{
			/* transmitter already have assigned frequency */
			t_main = t_main->next;
			continue;
		}
		
		/* until stack is empty */
		while(1){
			if(t_stack_pop(&stack, &t_minor)) break;	/* break if stack is empty */
			
			/* tries to assign frequency */
			if(assign_frequency(t_minor, freqs_head)){
				t_stack_free_stack(&stack);
				return 1;	/* assigning failed */
			}
		}
		t_main = t_main->next;
	}
	return 0;
}
