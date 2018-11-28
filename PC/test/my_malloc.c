#include <stdio.h>
#include <stdlib.h>
#include "my_malloc.h"

/*
Overrided malloc.
*/
void *my_malloc(size_t size) {
 	void *p = malloc(size);
  	if(p == NULL){
  		printf("%s\n", "ERR#2: Out of memory!");
  		clear();
  		exit(2);
  	}
  	return p;
}
