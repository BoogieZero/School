
/**----------------------------------
**  Typedef for the function pointer
**-----------------------------------*/

extern void start (void);     /* Startup code (in start.s)  */

typedef void (*fp) (void);
#define VECT_SECT          __attribute__ ((section (".vects")))

const fp HardwareVectors[] VECT_SECT = {
  start,                    /* 0  Power-on reset, Program counter (PC) */
};
