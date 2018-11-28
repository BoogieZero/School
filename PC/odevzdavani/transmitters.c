#include <stdlib.h>
#include <stdio.h>
#include "transmitters.h"
#include "my_malloc.h"
#define malloc(t_size) my_malloc(t_size)

/*
Transmission radius for all transmitters
*/
static int radius;

/*
Sets transmission radius for all transmitters
*/
void transmitters_set_radius(int trans_radius){
	radius = trans_radius;
}

/*
Return transmission radius
*/
int transmitters_get_radius(){
	return radius;
}

/*
Creates and adds new transmitter to transmitters list
*/
transmitters* transmitters_add_transmitter(transmitters *last, int id, double x, double y){
	coordinates *newcoor;
	transmitter *newtran;
	transmitters *newtrans;
	
	/* creating coordinates */
	newcoor = malloc(sizeof(coordinates));
	newcoor->x = x;
	newcoor->y = y;
	
	/* creating transmitter */
	newtran = malloc(sizeof(transmitter));
	newtran->id = id;
	newtran->coordinates = newcoor;
	newtran->neighbours = NULL;
	newtran->frequency = NULL;
	
	/* creating transmitters (element of transmitters list) */
	newtrans = malloc(sizeof(transmitters));
	newtrans->transmitter = newtran;
	newtrans->next = NULL;
	
	if(!last){
		/* last is NULL which means this is first element of transmitters list */
		return newtrans;
	}
	
	last->next = newtrans;
	return newtrans;
}

/*
Creates and adds new neighbour created by given transmitter neigh 
to list of neighbours of transmitter tran
*/
void transmitters_add_neighbour(transmitter *tran, transmitter *neigh){
	neighbours *newneigh;
	/* creating neighbour */
	newneigh = malloc(sizeof(neighbours));
	newneigh->transmitter = neigh;
	
	if(!tran->neighbours){
		/* first neighbour */
		newneigh->next = NULL;
		tran->neighbours = newneigh;
	}else{
		/* transmitter already have neighbour */
		newneigh->next = tran->neighbours;
		tran->neighbours = newneigh;
	}
}

/*
Frees memory alocated to given transmitter
*/
void transmitters_free_transmitter(transmitter *tran){
	/* clear coordinates */
	free(tran->coordinates);
	
	/* clear neighbours */
	if(tran->neighbours != NULL){
		neighbours *tmp;
		neighbours *pom;
		pom = tran->neighbours;
		
		while(pom != NULL){
			tmp = pom;
			pom = pom->next;
			free(tmp);
		}
		tran->neighbours = NULL;
	}
	
	/* clear transmitter */
	free(tran);
}

/*
Frees memory alocated to all transmitters
head is pointer to first transmitter in transmitters list
*/
void transmitters_free_all(transmitters *head){
	transmitters *tmp;
	transmitters *pom;
	pom = head;
	
	while(pom != NULL){
		tmp = pom;
		pom = pom->next;
		transmitters_free_transmitter(tmp->transmitter);
		free(tmp);
	}
	head = NULL;
	/*
	printf("%s", "Transmitters cleared\n");
	*/
}

/*
Prints given transmitter to console
*/
void transmitters_print_transmitter(transmitter *tran){
	/*
	printf("id: %2d X: %10f Y: %f\n", tran->id,	tran->coordinates->x,
												tran->coordinates->y);
	*/
										
	printf("%d %d\n", tran->id, tran->frequency->value);
	
}

/*
Prints all neighbour of given transmitter
*/
void transmitters_print_neighbour(transmitter *transmitter){
	neighbours *pom = transmitter->neighbours;
	while(pom != NULL){
		transmitters_print_transmitter(pom->transmitter);
		pom = pom->next;
	}
}

/*
Prints all transmitters with transmittion radius to console
*/
void transmitters_print_all(transmitters *head){
	transmitters *pom = head;
	/*
	printf("%s %d\n", "Transmission radius:",radius);
	printf("%s", "Transmitters:\n");
	*/
	while(pom != NULL){
		transmitters_print_transmitter(pom->transmitter);
		
		/*
		printf("%s\n","neighbours:");
		transmitters_print_neighbour(pom->transmitter);
		printf("%s\n","----");
		*/
		
		pom = pom->next;
	}
}
