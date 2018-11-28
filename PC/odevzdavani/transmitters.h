#ifndef _TRANSMITTERS_H
#define _TRANSMITTERS_H
#include "frequencies.h"

/*
Structure represents coordinates of transmitter.
*/
typedef struct thecoordinates{
	double x;
	double y;
}coordinates; 

struct thetransmitter;

/*
Structure represents list of neighbouring transmitters.
*/
typedef struct theneigbours{
	struct thetransmitter *transmitter;
	struct theneigbours *next;
}neighbours;

/*
Structure represents transmitter.
*/
typedef struct thetransmitter{
	int id;
	coordinates *coordinates;	/* possition */
	neighbours *neighbours;		/* neighbouring transmitters */
	frequency *frequency;		/* assigned frequency */
}transmitter;

/*
Structure represents element in linked list of transmitters.
*/
typedef struct thetransmitters{
	transmitter *transmitter;
	struct thetransmitters *next;	/* pointer to next transmitter in the list */
}transmitters;

/*
Sets transmission radius of all transmitters.
*/
void transmitters_set_radius(int trans_radius);

/*
Returns transmission radius of all transmitters.
*/
int transmitters_get_radius();

/*
Creates transmitter from given parameters and link it to list of transmitters behind last.
Returns last added element in transmitters list.
*/
transmitters* transmitters_add_transmitter(transmitters *last, int id, double x, double y);

/*
Creates neighbour from given transmitter neigh and 
adds it to list of neighbours of transmitter tran.
*/
void transmitters_add_neighbour(transmitter *tran, transmitter *neigh);

/*
Frees all memory allocated to list of transmitters,
head is pointer to first element in this list.
*/
void transmitters_free_all(transmitters *head);

/*
Prints given transmitter to console.
*/
void transmitters_print_transmitter(transmitter *transmitter);

/*
Prints all neighbouring transmitters with given transmitter.
*/
void transmitters_print_neighbour(transmitter *transmitter);

/*
Prints all transmitters in given list of transmitters,
head is pointer to first element in this list.
*/
void transmitters_print_all(transmitters *head);

#endif
