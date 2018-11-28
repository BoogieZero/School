#ifndef _FUNCTION_H
#define _FUNCTION_H
#include "transmitters.h"

/*
Finds collisions by distance between each transmitters and creates neighbours for colliding ones.
head is pointer to first transmitters element in transmitters list.
*/
void function_find_collisions(transmitters *head);

/*
Tries to assign non-coliding frequencies to transmitters.
trans_head is pointer to first transmitters element in transmitters list.
freqs_head is pointer to first frequencies element in available frequencies list.
*/
int function_assing_frequencies(transmitters *trans_head, frequencies *freqs_head, transmitters **m_stack);


#endif
