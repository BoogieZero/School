#ifndef _FREQUENCIES_H
#define _FREQUENCIES_H

/*
Sctructure represent frequency and it's id.
*/
typedef struct thefrequency{
	int id;
  	int value;
}frequency;

/*
Structure represents element in linked list of frequencies.
*/
typedef struct thefrequencies{
	frequency *frequency;
	struct thefrequencies *next;	/* pointer to next frequency in the list */
}frequencies;

/*
Creates frequency from id, value and link it to list of frequencies behind last.
Returns last added element in frequencies list.
*/
frequencies *frequencies_add_frequency(frequencies *last, int id, int value);

/*
Frees all memory allocated to list of frequencies,
head is pointer to first element in this list.
*/
void frequencies_free_all(frequencies *head);

/*
Prints given frequency to console.
*/
void frequencies_print_frequency(frequency *freq);

/*
Prints all frequencies in given list of frequencies,
head is pointer to first element in this list.
*/
void frequencies_print_all(frequencies *head);

#endif
