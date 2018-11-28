#include <stdlib.h>
#include <stdio.h>
#include "frequencies.h"
#include "my_malloc.h"
#define malloc(t_size) my_malloc(t_size)

/*
Adds new frequency created by given parametters and last added frequency
for first frequency last = NULL
returns last added frequency (newly created one)
*/
frequencies *frequencies_add_frequency(frequencies *last, int id, int value){
	frequency *newfreq;
	frequencies *newfreqs;
	
	/* creating frequency */
	newfreq = malloc(sizeof(frequency));
	newfreq->id = id;
	newfreq->value = value;
	
	/* creating new element for list of the frequencies */
	newfreqs = malloc(sizeof(frequencies));
	newfreqs->frequency = newfreq;
	newfreqs->next = NULL;
	
	if(!last){
		/* first frequency in list of the frequencies */
		return newfreqs;
	}
	
	last->next = newfreqs;
	return newfreqs;
}

/*
Frees memory allocated to available frequencies
*/
void frequencies_free_all(frequencies *head){
	frequencies *tmp;
	frequencies *pom;
	
	pom = head;
	while(pom != NULL){
		tmp = pom;
		pom = pom->next;
		free(tmp->frequency);
		free(tmp);
	}
	
	head = NULL;
	/*
	printf("%s", "frequencies cleared\n");
	*/
}

/*
Prints given frequency to console
*/
void frequencies_print_frequency(frequency *freq){
	printf("id: %d value: %d\n", freq->id, freq->value);
}

/*
Prints all available frequencies to console
*/
void frequencies_print_all(frequencies *head){
	frequencies *pom = head;
	printf("%s", "Available frequencies:\n");
	
	while(pom != NULL){
		frequencies_print_frequency(pom->frequency);
		pom = pom->next;
	}
}
