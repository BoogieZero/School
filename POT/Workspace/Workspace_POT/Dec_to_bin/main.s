;
; Prevod cisla z desitkove do hex. soustavy
;

		.h8300s

		.section	.vects,"a",@progbits
rs:		.long		_start		

; ----------- symboly ------------- 

		.equ	syscall,0x1FF00	; simulated IO area
		.equ	PUTS,0x0114		; kod PUTS
		.equ	GETS,0x0113		; kod GETS
		
; ----------- data ---------------- 

		.data
		.align	2			; zarovnani par. bloku
par_i:	.long	vstup		; parametricky blok pro vstup
par_o:	.long	text1		; parametricky blok pro vystup
par_p:	.long	prn			; parametricky blok pro vystup

prn:	.space	20			; vystupni buffer
vstup:	.space	20			; vstupni buffer
text1:	.asciz	"Vstupni hodnota (max. 2^16 - 1): "

		.align	2			; zarovnani adresy
		.space	100			; stack
stck:						; konec stacku + 1

; ----------- program ------------- 

		.text
		.global	_start

_start:
		mov.l	#stck,ER7
		
; ---   prompt		
		mov.w	#PUTS,R0	; 24bitovy PUTS
		mov.l	#par_o,ER1	; adr. param. bloku do ER1
		jsr		@syscall
		
; ---   cteni cisla		
		mov.w	#GETS,R0	; 24bitovy GETS
		mov.l	#par_i,ER1	; adr. param. bloku do ER1
		jsr		@syscall

; ---	volani funkce
		mov.l	#vstup,ER6	; adresa retezce do ER6
		jsr		@prevod		; volani funkce prevod
		jsr		@print_hex	; vystup hex. cisla
		
lab1:	jmp	@lab1			; dynamicky stop

; ---	funkce prevod  ----------
prevod:
		push.l	ER1			; ulozeni registru
		xor.w	R0,R0
		xor.w	R1,R1
		mov.w	#10,E1
		
lab2:	mov.b	@ER6,R1L
		cmp.b	#0x0A,R1L	; test na konec (CR)
		beq		lab3
		add.b	#-'0',R1L
		mulxu.w	E1,ER0
		add.w	R1,R0
		inc.l	#1,ER6
		jmp		@lab2
		
lab3:	pop.l	ER1			; obnoveni registru
		rts					; navrat

; ---	vystup  ----------
print_hex:
		mov.l	#prn,ER1	; adresa vyst. bufferu
		mov.b	#4,R2H		;pocet hex. cislic
		
lab5:	rotl.w	#2,R0		; nejvyssi hex. cislo doprava
		rotl.w	#2,R0
		
		mov.b	#0x0F,R2L	; maska pro 1 hex. cislo
		and.b	R0L,R2L		; v R2L bude jen 1 hex. cislice
		add.b	#'0',R2L	; prevod pro 0..9
		cmp.b	#'9',R2L
		ble		lab4		; je to 0..9
		add.b	#(-'0'+'A'-0x0A),R2L	; oprava pro A..F
lab4:	mov.b	R2L,@ER1	; ulozeni do bufferu
		inc.l	#1,ER1		; posun pointeru
		dec.b	R2H
		bne		lab5
		
		mov.b	#0x0A,R2L	; 'LF'
		mov.b	R2L,@ER1	; zakonceni textu
		
		mov.w	#PUTS,R0
		mov.l	#par_p,ER1
		jsr		@syscall	
		
		rts

; ---	------- ----------
		 
		.end

; ---	------  ----------
