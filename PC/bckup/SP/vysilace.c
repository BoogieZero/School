#include <stdio.h>
#include <stdlib.h>
#include "frequencies.h"
#include "transmitters.h"
#include "function.h"

/*
String constants for input file
*/
const char s_available[] = "Available frequencies:\n";
const char s_radius[] = "Transmission radius:\n";
const char s_transmitters[] = "Transmitters:\n";
const char s_help[] = 	"Input file name argument is incorrect.\n"
						"Please run the program in the following form:\n"
						"freq.exe -\"input_file_name.txt\"\n";
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
   		//printf("%s\n", "Found Available frequencies");
   		return 1;
	}
	if(strncmp(line, s_radius, sizeof s_radius) == 0){
		//printf("%s\n", "Found Transmission radius");
		return 2;
	}
	if(strncmp(line, s_transmitters, sizeof s_transmitters) == 0){
		//printf("%s\n", "Found Transmitters");
		return 3;
	}
	return 0;
}

/*
Reads available frequencies from file
*/
int load_available_frequencies(FILE *fr, char line[]){
	printf("%s\n", "Available frequencies");
	unsigned int id, value;
	fgets(line, input_buffer_size, fr);

   	if(get_block_number(line) != 1){
   		printf("%s\n", "Wrong format of ""Available frequencies"" ");
   		return -1;
	}
   	
	while(fgets(line, input_buffer_size, fr) != NULL){		
   		if(sscanf(line, "%d %d", &id, &value) != 2){
   			break;
		}
		//printf("%d %d\n", id, value);
		frequencies_add_frequency(id, value);
	}
	return 0;
}

/*
Reads transmission radius from file
*/
int load_radius(FILE *fr, char line[]){
	printf("%s\n", "Transmission radius");
	unsigned int radius;
	
	if(get_block_number(line) != 2){
   		printf("%s\n", "Wrong format of ""Transmission radius"" ");
   		return -2;
	}
	
	fgets(line, input_buffer_size, fr);
	
	sscanf(line, "%d", &radius);
	transmitters_set_radius(radius);
	//printf("%d\n",a);
	fgets(line, input_buffer_size, fr);
	return 0;
}

/*
Reads transmitters from file
*/
int load_transmitters(FILE *fr, char line[]){
	printf("%s\n", "Transmitters");
	unsigned int id;
	double x,y;
	
	if(get_block_number(line) != 3){
   		printf("%s\n", "Wrong format of ""Transmitters"" ");
   		return -3;
	}
	
	while(fgets(line, input_buffer_size, fr) != NULL){
		if(sscanf(line, "%d %lf %lf", &id, &x, &y) != 3){
   			break;
		}
		//printf("%d %f %f\n", id, x, y);
		transmitters_add_transmitter(id, x, y);
	}
	return 0;
}

/*
Loads input file
*/
int load_file(char *argv[]){
	FILE *fr; 
	char line[input_buffer_size];
	
   	fr = fopen (argv[1], "r");
   	if(fr == NULL){
   		printf("%s", "ERR#1: Missing argument!\n");
   		printf("%s", s_help);
   		return -1;
	}
   	printf("%s", "Loading file\n");
   	
	load_available_frequencies(fr, line);
	load_radius(fr, line);
	load_transmitters(fr, line);
	
	printf("%s\n", "File loaded\n");
	fclose(fr);
	return 0;
}

/*
Clears alocated memory
*/
void clear_memory(){
	frequencies_free_all();
	transmitters_free_all();
}

/*
Main function
*/
int main(int argc, char *argv[]) {
	if(load_file(argv) != 0){
		return 1;
	}
	
	/*
	int i = 0;
	transmitters *pom;
	transmitter *pom0, *pom2, *pom3;
	transmitters_add_transmitter(21, 10.f, 11.f);
	transmitters_add_transmitter(22, 11.f, 12.f);
	transmitters_add_transmitter(23, 12.f, 13.f);
	
	
	pom = transmitters_get_head();
	
	//transmitters_print_all();
	printf("%s\n", "---");
	for(i = 0; i < 25; i++){
		pom = pom->next;
	}
	pom0 = pom->transmitter;
	pom = pom->next;
	pom2 = pom->transmitter;
	pom = pom->next;
	pom3 = pom->transmitter;
	
	transmitters_add_neighbour(pom0, pom2);
	transmitters_add_neighbour(pom0, pom3);
	
	printf("%s\n", "-neigh:-");
	transmitters_print_neighbour(pom0);
	printf("%s\n", "---");
	pom = transmitters_get_head();
	*/
	
	//frequencies_print_all();
	function_find_collisions(transmitters_get_head());
	//transmitters_print_all();
	//double d = 115.698096;
	//printf("%.6f\n",d);
	
	clear_memory();
	return 0;
}
