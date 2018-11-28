			.h8300s
			
;KONSTANTY-----------------------------------------
			.equ	syscall,0x1FF00		; simulated IO area
			.equ	PUTS,0x0114			; kod PUTS
			.equ	GETS,0x0113			; kod GETS
			
			.data
			
;PARAMETRY PRO IO----------------------------------
prompt:		.asciz	">"

			.align 	2		
par_prompt:	.long	prompt

par_out:	.long	out_buf		;vystupni buffer
par_in:		.long 	in_buf		;vstupni buffer



; buffery pro vstup a vystup
			.align	2
out_buf:	.space	100
in_buf:		.space	100
PIN:		.long				;pointer na vstupni buffer
			.space	4	
POUT:		.long				;pointer na vystupni buffer
			.space	4		
num_count:	.byte
			.space	1
;--------------------------------------------------

;MATICE--------------------------------------------
			.align	2
matice:		.space	245

PM:			.long				;pointer na matici
			.space	4
P_ROW:		.long				;pointer na radky matice pro transformaci
			.space	4
P_COL:		.long				;pointer na sloupce matice pro transformaci
			.space	4
size:		.byte				;rad matice
			.space	1


;ZASOBNIK------------------------------------------
			.align	2			; zarovnani adresy
			.space	100			; stack
stck:							; konec stacku + 1
			
			.text
			.global _start
;--------------------------------------------------


;MAIN----------------------------------------------
_start:		mov.l	#stck,ER7	;nastaveni zasobniku 
			jsr		@init
			
			;	nacteni velikosti matice
			jsr		@read_cmd
			mov.b	@in_buf,R0L
			add.b	#-0x30,R0L
			mov.b	R0L,@size
			
			;	nacteni vlastni matice
			;mulxu.b	R0L,R0		;pocet hodnot pro naplneni matice
			
loopm:		push.w	R0

			jsr	@read_line

			pop.w	R0
			dec.b	R0L
			bne		loopm	;pokud zbyvaji radky matice pokracuje nacitani
			
			mov.l	#matice,ER0
			mov.l	ER0,@PM
			;	matice je nactena
			
			;transformovani matice
			jsr		@transform
			jsr		@clr_reg
			
			;vystup do konzole
			jsr		@raw_write_out
			
lab1:		jmp 	@lab1

;/MAIN---------------------------------------------

;METODY--------------------------------------------

; inicializace-----------------------
init:		
		xor.l	ER0,ER0
		mov.l	ER0,ER1
		mov.l	ER0,ER2
		mov.l	ER0,ER3
		;nastaveni ukazatele na vstupni buffer
		mov.l	#in_buf,ER1
		mov.l	ER1,@PIN
		;nastaveni ukazatele na vystupni buffer
		mov.l	#out_buf,ER1
		mov.l	ER1,@POUT
		;nastaveni ukazatele na matici
		mov.l	#matice,ER1
		mov.l	ER1,@PM
		;nulovani citace poctu vstupnich znaku
		mov.b	R0L,@num_count
		rts

; vynulovani registru----------------
;	vynuluje pouzite registry pro dalsi praci mimo ER0

clr_reg:
		xor.l	ER1,ER1
		mov.l	ER1,ER2
		mov.l	ER1,ER3
		mov.l	ER1,ER4
		mov.l	ER1,ER5
		mov.l	ER1,ER6
		rts

; cteni vstupu-----------------------
;	vypise prompt a precte radku konzole

read_cmd:	
		mov.w	#PUTS,R0		; vypis prompt
		mov.l	#par_prompt,ER1
		jsr		@syscall
		
		mov.w	#GETS,R0		; cteni prikazu
		mov.l	#par_in,ER1
		jsr		@syscall
		rts

; nacte radek matice------------------

read_line:
		jsr		@read_cmd	;cte 1 radek
		mov.b	@size,R0L
convert_line:	
		push.w	R0	
		jsr		@convert	;konvertuje jedno cislo
		jsr		@insert_mat	;vlozi zkonvertovane cislo do matice
		
		pop.w	R0
		dec.b	R0L
		bne		convert_line	;pokud zbyvaji cisla v radku konvertuje dalsi
		rts

; prevod na cislo---------------------
;	prevede data ze vstupniho bufferu na cislo a odstrani
;	ukoncovaci znak "0A"
;	maximalni hodnota je 5ti mistne cislo (int16)
;	vysledek po prevodu je v registru ER0


convert:
		jsr		@last
		xor.l	ER1,ER1
		mov.l	#0x1,ER3		;nasobici konstanta 1,10,100,1000,...
		mov.l	@PIN,ER0		;ER0 je ukazatel na rad cisla ze vstupu
		mov.l	#0xA,ER4		;velikost dalsiho radu
		mov.b	@num_count,R5L	;pocet cislic vstupu
loopc:	
		mov.b	@ER0,R1L
		add.b	#-0x30,R1L		;odecteni konstanty pro prevod z ASCII
		
		mulxu.w	R3,ER1			;nasobeni radem
		add.w	R1,R2			;pricteni prevodu k dosavadnimu vysledku v R2L
		
		dec.l	#1,ER0			;posun na vyssi rad
		mulxu.w	R4,ER3			;zvetseni nasobitele radu
		xor.l	ER1,ER1			;nulovani ER1
		dec.b	R5L
		bne		loopc
		mov.l	ER2,ER0			;prevedena hodnota je v ER0
		
		
		xor.l	ER2,ER2		;vynuluje ER2
		mov.l	ER2,ER3		;vynuluje ER3
		
		mov.l	@PIN,ER2
		inc.l	#1,ER2		;posune ukazatel na dalsi znak
		mov.b	@ER2,R3L
		
		cmp.b	#0x0A,R3L	;pokud je konec radky resetuje ukazatel na vstupni buffer
		beq		endline
		cmp.b	#0x20,R3L	;pokud je dalsi znak mezera posune ukazatel za mezeru
		beq		space
		
endline:				;	resetovani ukazatele na vstupni buffer
		mov.l	#in_buf,ER1
		mov.l	ER1,@PIN
		
		jsr		@clr_reg		;vynulovani registru
		mov.b	R1L,@num_count	;vynulovani citace poctu znaku na vstupu
		rts

space:	inc.l	#1,ER2
		mov.l	ER2,@PIN	;posunuti ukazatele za mezeru
		jsr		@clr_reg
		mov.b	R1L,@num_count	;vynulovani poctu znaku na vstupu
		rts
		
; nalezeni konce vstupu------------------
;	nastavi pointer vstupu na posledni cislo

last:	
		mov.l	@PIN,ER0
		mov.b	@num_count,R2L
loopl:
		mov.b	@ER0,R1L
		
		cmp.b	#0x0A,R1L	;pokud neni konec bufferu pokracuje
		beq		endl
		cmp.b	#0x20,R1L	;pokud nenajde mezeru pokracuje
		beq		endl
		
		inc.l	#1,ER0		;posunuti ukazatele na dalsi prvek
		inc.b	R2L			;zvyseni poctu znaku na vstupu
		jmp		@loopl
endl:
		dec.l	#1,ER0
		mov.l	ER0,@PIN	;nastavi ukazatel PIN na posledni znak (pred "0A")
		
		mov.b	R2L,@num_count	;ulozi pocet znaku vstupniho cisla do num_count
		xor.l	ER2,ER2			;nulovani R2L kvuli dalsim operacim
		
		;mov.l	@PIN,ER0
		;mov.b	@ER0,R0L
		rts

; vloz do matice--------------------------
;	vlozi cislo int16 z R0 na prislusne poradi v matici
insert_mat:
			mov.l	@PM,ER1
			mov.w	R0,@ER1
			;	posun ukazatele v matici
			inc.l	#2,ER1
			mov.l	ER1,@PM
			rts
			
; transformuje matici---------------------
;	vytvori matici transformovanou z nactene matice (od "matice:")

transform:	
			;mov.l	@PM,ER0			;ukazatel na matici
			mov.b	@size,R6L		;rad matice
			add.b	R6L,R6L
			mov.b	R6L,R2H			;R2H-rad (2x) 
			;mov.b	R2H,R2L
			mov.b	#0x2,R6H		;aktualni posun
			;sub.b	R6H,R2L			;R2L- rozdil radu a aktualniho posunu (2x)
			
			;mov.l	ER0,ER4			;hfbdfsfbs
			;mov.l	ER0,ER5			;fkbsdfkjdng
			
loopt:		
			mov.b	R2H,R2L
			sub.b	R6H,R2L			;R2L- rozdil radu a aktualniho posunu (2x)
			mov.l	@PM,ER0			;ukazatel na matici
			mov.l	ER0,ER4
			mov.l	ER0,ER5
			
			
inloopt:
			jsr		@shift_row
			;mov.w	@ER4,R0
			
			jsr		@shift_col
			;mov.w	@ER5,R0
			
			jsr		@swap
			
			dec.b	R2L
			dec.b	R2L
			bne		inloopt		;dokud neni hotovy radek/sloupec skace na inloopt
			
			inc.b	R6H			;zvyseni aktualniho posunu
			inc.b	R6H
			
			cmp.b	R6L,R6H		;pokud aktualni posun = rad matice -> konec
			beq		endt
			jsr		@shift_dia
			jmp		loopt
		
endt:		;posledni prvek diagonaly -> hotovo
			mov.l	#matice,ER0
			mov.l	ER0,@PM		;nastaveni ukazatele na zacatek matice
			rts

; posun po prvcich radku------------------

shift_row:
		push.l	ER0
		xor.l	ER0,ER0
		;mov.b	R6H,R0L
		add.l	#0x2,ER4
		pop.l	ER0
		rts
		
; posun po prvcich sloupce----------------

shift_col:
		push.l	ER0
		push.l	ER1
		xor.l	ER0,ER0
		mov.l	ER0,ER1
		
		mov.b	#0x2,R0L
		mov.b	@size,R1L
		mulxu.b	R1L,R0
		add.l	ER0,ER5
		
		pop.l	ER1
		pop.l	ER0
		rts

; posune PM po diagonale------------------
;	matice	podle aktualniho posunu R6H

shift_dia:
		push.l	ER4
		push.l	ER5
		
		mov.l	@PM,ER5
		jsr		@shift_col
		mov.l	ER5,ER4
		jsr		shift_row
		mov.l	ER4,@PM
		
		
		pop.l	ER5
		pop.l	ER4
		rts

; prohodi dva prvky-----------------------
;	podle adresy v ER4 a ER5

swap:		
		push.l	ER0
		push.l	ER1
		mov.w	@ER4,R0
		mov.w	@ER5,R1
		mov.w	R0,@ER5
		mov.w	R1,@ER4
		pop.l	ER1
		pop.l	ER0

		rts
	
; vypis ulozene matice do konzole---------

raw_write_out:
		mov.l	#matice,ER1
		mov.l	ER1,@PM
		mov.b	@size,R3L

loop_line:				;volani raw_out_line pro kazdy radek matice
		jsr 	raw_out_line
		dec.b	R3L
		bne		loop_line
		rts

; vypis radku matice do konzole

raw_out_line:
		mov.b	@size,R3H
		
		mov.l	@PM,ER1
		mov.l	@POUT,ER2
		

loop_col:
		mov.w	@ER1,R4
		
		xor.l	ER0,ER0
		mov.b	R4H,R0L
		shlr.b	#2,R0L
		shlr.b	#2,R0L
		jsr		@controll
		mov.b	R0L,@ER2		;prvni znak
		inc.l	#1,ER2		;posun za prvni znak
		
		xor.l	ER0,ER0
		mov.b	R4H,R0L
		and.b	#0xF,R0L
		jsr		@controll
		mov.b	R0L,@ER2		;druhy znak
		inc.l	#1,ER2		;posun za druhy znak
		
		xor.l	ER0,ER0
		mov.b	R4L,R0L
		shlr.b	#2,R0L
		shlr.b	#2,R0L
		jsr		@controll
		mov.b	R0L,@ER2		;treti znak
		inc.l	#1,ER2		;posun za treti znak
		
		xor.l	ER0,ER0
		mov.b	R4L,R0L
		and.b	#0xF,R0L
		jsr		@controll
		mov.b	R0L,@ER2		;ctvrty znak
		inc.l	#1,ER2		;posun za ctvrty znak
		
		mov.w	#0x20,R4
		mov.b	R4L,@ER2		;pridani mezery
		inc.l	#1,ER2		;posun za mezeru
	
		inc.l	#2,ER1		;posun na dalsi cislo matice
		mov.l	ER1,@PM
		
		
		dec.b	R3H
		bne	loop_col
		;hotovy radek
		
		mov.b	#0xA,R0L		;konec radku
		mov.b	R0L,@ER2
		
		;mov.l	#out_buf,ER0	;nastaveni ukazatele na zacatek vystupniho bufferu
		;mov.l	ER0,@POUT
		
		mov.w	#PUTS,R0		; vypis radku
		mov.l	#par_out,ER1
		jsr		@syscall
		
		rts

; kontrola znaku----------------------

controll:
		mov.b	#0x9,R5L
		sub.b	R0L,R5L
		bcc		f_add	
		add.w	#0x7,R0
f_add:	add.w	#0x30,R0
		rts

;/METODY-------------------------------------------			
		.end
