#include <stdio.h>
#include <stdlib.h>
#include "function.h"
#include "transmitters_stack.h"

/*
String constants for input file
*/
const char s_available[] = "Available frequencies:\n";
const char s_radius[] = "Transmission radius:\n";
const char s_transmitters[] = "Transmitters:\n";

/*
Help text
*/
const char s_help[] = 	"Input file name argument is incorrect.\n"
						"Please run the program in the following form:\n"
						"freq.exe -\"input_file_name.txt\"\n";

/* pointer to first frequency in list of available frequencies */
static frequencies *freqs_head = NULL;	
	
/* pointer to first transmitter in list of all transmitters */
static transmitters *trans_head = NULL;
	
/* pointer to top of stack */
static transmitters *m_stack = NULL;

/*
Maximum length of row in input file
*/
const int input_buffer_size = 30;

/*
Returns coresponding number of given line otherwise return 0
	"Available frequencies:\n"	- 1
	"Transmission radius:\n"	- 2
	"Transmitters:\n"			- 3
	otherwise					- 0
*/
int get_block_number(char line[]){
	if(strncmp(line, s_available, sizeof s_available) == 0){
   		return 1;
	}
	if(strncmp(line, s_radius, sizeof s_radius) == 0){
		return 2;
	}
	if(strncmp(line, s_transmitters, sizeof s_transmitters) == 0){
		return 3;
	}
	return 0;
}

/*
Reads next available frequency from inpput file.
If reading was succesfull returns 0 otherwise 1.
*/
int read_frequency(FILE *fr, char line[], int *id, int *value){
	if(		fgets(line, input_buffer_size, fr) != NULL
			&&sscanf(line, "%d %d", id, value) == 2){
		return 0;
	}
	return 1;
}

/*
Reads available frequencies from file.
If reading was succesfull returns 0 otherwise 1.
*/
int load_available_frequencies(FILE *fr, char line[], frequencies **freqs_head){
	frequencies *last;
	unsigned int id, value;
	
	fgets(line, input_buffer_size, fr);

   	if(get_block_number(line) != 1){
   		printf("%s\n", "ERR#4: Wrong format of ""Available frequencies"" !");
   		return 1;
	}
   	
   	read_frequency(fr, line, &id, &value);
   	*freqs_head = frequencies_add_frequency(NULL, id, value);
   	last = *freqs_head;	/* head for list of available frequencies */
   	
	while(read_frequency(fr, line, &id, &value) == 0){		
		last = frequencies_add_frequency(last, id, value);
	}
	return 0;
}

/*
Reads transmission radius from file.
If reading was succesfull returns 0 otherwise 2.
*/
int load_radius(FILE *fr, char line[]){
	unsigned int radius;
	
	if(get_block_number(line) != 2){
   		printf("%s\n", "ERR#5: Wrong format of ""Transmission radius"" !");
   		return 2;
	}
	
	fgets(line, input_buffer_size, fr);
	sscanf(line, "%d", &radius);
	
	transmitters_set_radius(radius);

	fgets(line, input_buffer_size, fr);	/* next line */
	return 0;
}

/*
Reads next transmitter from inpput file.
If reading was succesfull returns 0 otherwise 1.
*/
int read_transmitter(FILE *fr, char line[], int *id, double *x, double *y){
	if(		fgets(line, input_buffer_size, fr) != NULL
			&&sscanf(line, "%d %lf %lf", id, x, y) == 3){
		return 0;
	}
	return 1;
}

/*
Reads transmitters from file.
If reading was succesfull returns 0 otherwise 3.
*/
int load_transmitters(FILE *fr, char line[], transmitters **trans_head){
	unsigned int id;
	double x,y;
	transmitters *last;
	
	if(get_block_number(line) != 3){
   		printf("%s\n", "ERR#5: Wrong format of ""Transmitters"" !");
   		return 3;
	}
	
	read_transmitter(fr, line, &id, &x, &y);
	*trans_head = transmitters_add_transmitter(NULL, id, x, y);
	last = *trans_head;	/* head for list of transmitters */
	
	while(read_transmitter(fr, line, &id, &x, &y) == 0){
		last = transmitters_add_transmitter(last, id, x, y);
	}
	return 0;
}

/*
Loads input file.
If load and reading was succesfull returns 0 
otherwise 3, 4 or 5 according to issue.
*/
int load_file(char *argv[], transmitters **trans_head, 
							frequencies **freqs_head){
	FILE *fr; 
	char line[input_buffer_size];
	
   	fr = fopen (argv[1], "r");
   	if(fr == NULL){
   		printf("%s\n", "ERR#1: Missing argument!");
   		printf("%s", s_help);
   		return 1;
	}
	/*
   	printf("%s", "Loading file\n");
   	*/
   	
	if(load_available_frequencies(fr, line, freqs_head)) 	return 4;
	if(load_radius(fr, line)) 								return 5;
	if(load_transmitters(fr, line, trans_head)) 			return 6;
	
	/*
	printf("%s\n", "File loaded\n");
	*/
	fclose(fr);
	return 0;
}

/*
Clears all allocated memory.
*/
void clear(){
	t_stack_free_stack(&m_stack);
	transmitters_free_all(trans_head);
	frequencies_free_all(freqs_head);
}

/*
Main function
*/
int main(int argc, char *argv[]) {
	int err;
	
	if(argc != 2){
		/* there is not exactly one non-default argument */
		printf("%s\n", "ERR#1: Missing argument!");
		printf("%s", s_help);
		return 1;
	}
	
	/* load input file */
	if(err = load_file(argv, &trans_head, &freqs_head)){
		return err;
	}
	
	/* creating collision graph */
	function_find_collisions(trans_head);
	
	/* trying to assing non-colliding frequencies */
	if(function_assing_frequencies(trans_head, freqs_head, &m_stack)){
		/* assigning was not possible */
		printf("ERR#3: Non-existent solution!\n");
		
		clear();
		return 4;
	}
	
	transmitters_print_all(trans_head);
	
	clear();
	return 0;
}
