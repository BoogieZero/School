		.h8300s
		
		.data
var1:	.word	0x1234
var2:	.word	0xABCD
var3:	.space	2

		.align	2			; zarovnani adresy
		.space	100			; stack
stck:						; konec stacku + 1

		.text
		.global	_start

_start:	mov.l	#stck,ER7
		push.w	R0
		push.w	R1
		mov.w	@var1,R0
		mov.w	@var2,R1
		add.w	R1,R0
		mov.w	R0,@var3
		
		mov.l	#0x00005678,ER0
		mov.w	#0x0010,r1
		divxu.w	R1,ER0
							
lab1:	jmp	@lab1

		.end

