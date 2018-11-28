#include <stdio.h>
#include <stdlib.h>

const char available[] = "Available frequencies:\n";
const char radius[] = "Transmission radius:\n";
const char transmitters[] = "Transmitters:\n";
const int input_buffer_size = 25;

/*
Returns coresponding number of given line otherwise return 0
	"Available frequencies:\n"	- 1
	"Transmission radius:\n"	- 2
	"Transmitters:\n"			- 3
	otherwise					- 0
*/
int get_block_number(char line[]){
	if(strncmp(line, available, sizeof available) == 0){
   		//printf("%s\n", "Found Available frequencies");
   		return 1;
	}
	if(strncmp(line, radius, sizeof radius) == 0){
		//printf("%s\n", "Found Transmission radius");
		return 2;
	}
	if(strncmp(line, transmitters, sizeof transmitters) == 0){
		//printf("%s\n", "Found Transmitters");
		return 3;
	}
	return 0;
}

int load_available_frequencies(FILE *fr, char line[]){
	printf("\n%s\n", "Available frequencies");
	unsigned int a,b;
	fgets(line, input_buffer_size, fr);
	printf("%s", line);
	
   	if(get_block_number(line) != 1){
   		printf("%s\n", "Wrong format of ""Available frequencies"" ");
   		return -1;
	}
   	
   	while(	fgets(line, input_buffer_size, fr) != NULL
	   		&& get_block_number(line) == 0){
	   			
   		sscanf(line, "%d %d", &a, &b);
		printf("%d %d\n", a, b);
	}
	return 0;
}

int main(int argc, char *argv[]) {
	FILE *fr; 
	unsigned int a, b;
	float c, d;
	char line[25];
	
	printf("%s\n", "Loading file ");
	
   	fr = fopen ("vysilace-25.txt", "r");
   	
	load_available_frequencies(fr, line);
	
	printf("%s\n", "File loaded ");
	fclose(fr);
   	
	return 0;
}
