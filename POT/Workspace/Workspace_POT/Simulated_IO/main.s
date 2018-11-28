		.h8300s

		.equ	syscall,0x1FF00	; simulated IO area
		.equ	PUTS,0x0114		; kod PUTS
		.equ	GETS,0x0113		; kod GETS
		
; ------ datovy segment ----------------------------

		.data
txt1:	.asciz	"Simulovany vystup\n\r"
txt2:	.asciz	"Seru na tebe\n\r"
buffer:	.space	100			; vstupni buffer

; parametricke bloky musi byt zarovnane na dword
		.align	2			; zarovnani adresy
par1:	.long	txt1		; parametricky blok 1
par2:	.long	buffer		; parametricky blok 2
par3:	.long	txt2

; stack musi byt zarovnany na word
		.align	1			; zarovnani adresy
		.space	100			; stack
stck:						; konec stacku + 1

; ------ kodovy segment ----------------------------

		.text
		.global	_start

_start:	mov.l	#stck,ER7

		mov.w	#PUTS,R0	; 24bitovy PUTS
		mov.l	#par1,ER1	; adr. param. bloku do ER1
		jsr		@syscall
		
		mov.w	#PUTS,R0	; 24bitovy PUTS					;
		mov.l	#par3,ER1	; adr. param. bloku do ER1		;
		jsr		@syscall									;
		
		mov.w	#GETS,R0	; 24bitovy GETS
		mov.l	#par2,ER1	; adr. param. bloku do ER1
		jsr		@syscall
			
lab1:	jmp	@lab1			; konec vypoctu

		.end

