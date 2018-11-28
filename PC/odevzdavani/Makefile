# Project: Vysilace

CC       = gcc
OBJ      = freq.o frequencies.o transmitters.o function.o transmitters_stack.o my_malloc.o
BIN      = freq.exe
RM       = rm -f

$(BIN): $(OBJ)
	$(CC) $(OBJ) -o $(BIN)

freq.o: freq.c
	$(CC) -c freq.c -o freq.o

frequencies.o: frequencies.c
	$(CC) -c frequencies.c -o frequencies.o

transmitters.o: transmitters.c
	$(CC) -c transmitters.c -o transmitters.o

function.o: function.c
	$(CC) -c function.c -o function.o

transmitters_stack.o: transmitters_stack.c
	$(CC) -c transmitters_stack.c -o transmitters_stack.o

my_malloc.o: my_malloc.c
	$(CC) -c my_malloc.c -o my_malloc.o

#deletes all .o files
clean:
	${RM} $(OBJ) $(BIN)

#rebuilds project
rebuild: clean $(BIN)
