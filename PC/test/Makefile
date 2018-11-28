# Project: Vysilace

CC       = gcc.exe
OBJ      = freq.o frequencies.o transmitters.o function.o transmitters_stack.o my_malloc.o
LINKOBJ  = freq.o frequencies.o transmitters.o function.o transmitters_stack.o my_malloc.o
BIN      = freq.exe
RM       = rm -f

$(BIN): $(OBJ)
	$(CC) $(LINKOBJ) -o $(BIN) $(LIBS)

freq.o: freq.c
	$(CC) -c freq.c -o freq.o $(CFLAGS)

frequencies.o: frequencies.c
	$(CC) -c frequencies.c -o frequencies.o $(CFLAGS)

transmitters.o: transmitters.c
	$(CC) -c transmitters.c -o transmitters.o $(CFLAGS)

function.o: function.c
	$(CC) -c function.c -o function.o $(CFLAGS)

transmitters_stack.o: transmitters_stack.c
	$(CC) -c transmitters_stack.c -o transmitters_stack.o $(CFLAGS)

my_malloc.o: my_malloc.c
	$(CC) -c my_malloc.c -o my_malloc.o $(CFLAGS)

#deletes all .o files
clean:
	${RM} $(OBJ) $(BIN)

#rebuilds project
rebuild: clean $(BIN)
