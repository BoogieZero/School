			.h8300s

;	nektere konstanty		
			.equ	syscall,0x1FF00		; simulated IO area
			.equ	PUTS,0x0114			; kod PUTS
			.equ	GETS,0x0113			; kod GETS
			.equ	PTR_MASK,0x00000007	; maska pro pointery
			.equ	CMD_READ,'R'
			.equ	CMD_WRITE,'W'
			.equ	CMD_INIT,'I'
			.equ	CMD_EXIT,'X'
			
			.data

; parametricke bloky pro IO operace			
			.align	2
par_prompt:	.long	prompt
par_full:	.long	txt_full
par_empty:	.long	txt_empty
par_ok:		.long	txt_ok
par_out:	.long	out_buf
par_in:		.long	in_buf

; texty
prompt:		.asciz	">"
txt_full:	.asciz	"Full\n"
txt_empty:	.asciz	"Empty\n"
txt_ok:		.asciz	"OK\n"

; buffery pro vstup a vystup
out_buf:	.space	10			; pro vystup 1 bytu staci
in_buf:		.space	100			; pro jistotu radsi delsi

; promenne
			.align	2			; simulator vyzaduje zarovnani dat
buffer:		.space	8			; kruhovy buffer
rd_ptr:		.space	4			; pointer pro cteni
wr_ptr:		.space	4			; pointer pro zapis

			.align	2			; zarovnani adresy
			.space	100			; stack
stck:							; konec stacku + 1

			.text
			.global	_start

; ---------- main --------------------------------

_start:		mov.l	#stck,ER7

			jsr		@init_buffer

loop:		jsr		@read_cmd		; cteni radku s prikazem
			jsr		@decode_cmd		; dekodovani prikazu a parametru
									; prikaz je v R0L, parametr je v R0H
			
			; rozvetveni podle prikazu (je v R0L)
			cmp.b	#CMD_READ,R0L
			bne		lab2
			jsr		@read_buffer	; cteni
			jmp		@loop
			
lab2:		cmp.b	#CMD_WRITE,R0L
			bne		lab3
			jsr		@write_buffer	; zapis
			jmp		@loop
			
lab3:		cmp.b	#CMD_INIT,R0L
			bne		lab4
			jsr		@init_buffer	; inicializace
			jmp		@loop
			
lab4:		cmp.b	#CMD_EXIT,R0L
			beq		lab1
			
			jmp		@loop
							
lab1:		jmp	@lab1

; ---------- cteni radku s prikazem ------------
; vypise prompt a precte radku s prikazem

read_cmd:	mov.w	#PUTS,R0		; vypis prompt
			mov.l	#par_prompt,ER1
			jsr		@syscall
			
			mov.w	#GETS,R0		; cteni prikazu
			mov.l	#par_in,ER1
			jsr		@syscall
			rts

; ---------- dekodovani prikazu ----------------
; vraci v R0L kod prikazu, v R0H zadane 8bitove cislo

decode_cmd:	mov.b	@in_buf,R0L			; prvni znak = kod prikazu
			and.b	#0xDF,R0L			; prevod na velke (a -> A)			
			mov.l	#(in_buf + 1),ER1	; pointer na prvni znak zadaneho cisla
			jsr		@atob				; prevod ASCII na byte
			mov.b	R2H,R0H				; vysledek prevodu do R1L
			rts

; ---------- prevod retezce na 8bitovou hodnotu --
; pointer na retezec je v ER1, hodnotu vraci v R2H

atob:		xor.b	R2H,R2H				; nulovani R2H
			
lab14:		mov.b	@ER1,R2L			; cteni znaku
			cmp.b	#0x0A,R2L			; test na konec radku
			bne		lab13				; neni konec radku
			rts							; konec radku -> navrat
			
lab13:		cmp.b	#'9',R2L
			bls		lab15				; znak je '0'..'9'
			and.b	#0xDF,R2L			; prevod malych znaku na velke (a -> A)
			add.b	#(-'A'+0x0A),R2L	; prevod A..F	
			jmp		@lab12				; 
lab15:		add.b	#-'0',R2L			; prevod 0..9			
lab12:		shll.b	R2H					; sestaveni cisla v R2H
			shll.b	R2H
			shll.b	R2H
			shll.b	R2H
			or.b	R2L,R2H
			inc.l	#1,ER1				; posun pointeru
			jmp		@lab14				; na dalsi znak

; ---------- inicializace bufferu ----------------
; nastavi rd_ptr a wr_ptr na 0
; nema zadne parametry

init_buffer:	
			xor.l	ER0,ER0
			mov.l	ER0,@rd_ptr
			mov.l	ER0,@wr_ptr
			rts
				
; ---------- cteni z bufferu ---------------------
; vyjme a zobrazi data z bufferu
; nema zadne parametry

read_buffer:	
			mov.l	@rd_ptr, ER0		; index pro cteni
			mov.l	@wr_ptr, ER1		; index pro zapis
			cmp.l	ER0,ER1				; porovnani
			beq		b_empty				; wr_ptr == rd_ptr -> prazdny buffer

			mov.b	@(buffer,ER0),R1L	; cteni z bufferu
			inc.l	#1,ER0				; posun rd_ptr
			and.l	#PTR_MASK,ER0		; uprave pointeru (modulo n)
			mov.l	ER0,@rd_ptr			; zapis pointeru do pameti
			
			;zobrazeni precteneho znaku (je v R1L)
			mov.l	#out_buf,ER0		; adresa vystupniho bufferu do ER0
			jsr		@btoa				; ulozi cislo v ASCII na adresu @ER0
			
			mov.w	#PUTS,R0			; priprava na vypis
			mov.l	#par_out,ER1
			jsr		@syscall			; vypis
			rts							; navrat
			
b_empty:	mov.w	#PUTS,R0			; pripad "buffer empty"
			mov.l	#par_empty,ER1
			jsr		@syscall
			rts							; navrat
			
; ---------- prevod byte na ASCII ----------------
; prevede byte z R1L na retezec na adrese @ER0

btoa:		mov.b	R1L,R1H
			shlr.b	R1H					; zpracovani levych 4 bitu (-> 1. ASCII znak)			
			shlr.b	R1H
			shlr.b	R1H
			shlr.b	R1H
			add.b	#'0',R1H			; prevod 0x0..0x9 na '0'..'9'
			cmp.b	#'9',R1H
			bls		lab20				; byl to pripad '0'..'9'
			add.b	#(-'0'+'A'-0x0A),R1H	; byl to pripad 'A'..'F'
lab20:		mov.b	R1H,@ER0			; ulozeni do bufferu

			inc.l	#1,ER0
			and.b	#0x0F,R1L			; zpracovani pravych 4 bitu (-> 2. ASCII znak)
			add.b	#'0',R1L			; prevod 0x0..0x9 na '0'..'9'
			cmp.b	#'9',R1L
			bls		lab21				; byl to pripad '0'..'9'
			add.b	#(-'0'+'A'-0x0A),R1L	; byl to pripad 'A'..'F'
lab21:		mov.b	R1L,@ER0			; ulozeni do bufferu

			inc.l	#1,ER0
			mov.b	#0x0A,R1L			; znak nove radky (LF)
			mov.b	R1L,@ER0
			inc.l	#1,ER0
			xor.b	R1L,R1L				; 0 -> R1L
			mov.b	R1L,@ER0			; zakonceni retezce
			
			rts
			
; ---------- zapis do bufferu --------------------
; zapise do bufferu data z R0H

write_buffer:	
			mov.l	@rd_ptr, ER1		; index pro cteni
			mov.l	@wr_ptr, ER2		; index pro zapis
			mov.l	ER2,ER3
			
			inc.l	#1,ER3				; posun pointeru
			and.l	#PTR_MASK,ER3		; uprava pointeru (modulo n)
			
			cmp.l	ER3,ER1				; porovnani
			beq		b_full				; wr_ptr == rd_ptr -> plny buffer

			mov.b	R0H,@(buffer,ER2)	; zapis dat podle puvodniho wr_ptr
			mov.l	ER3,@wr_ptr			; zapis upraveneho wr_ptr
			
			mov.w	#PUTS,R0			; priprava vypsani "OK"
			mov.l	#par_ok,ER1
			jsr		@syscall
			rts
			
b_full:		mov.w	#PUTS,R0			; priprava vypsani "Full"
			mov.l	#par_full,ER1
			jsr		@syscall
			rts
			
; -----------------------------------------------------

			
			