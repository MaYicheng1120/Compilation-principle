
# Code generated from MiniCCompiler at 
# input file:C:\Users\GJLDR\Desktop\byyl\bit-minic-compiler-master-1\bit-minic-compiler-master\input\test.ic2.xml
# Please do not change this file! 

	.data
	.text
__init:
	# setup the stack
	lui $sp, 0x8000
	addi $sp, $sp, 0x0000
	addiu $sp, $sp, -1024

	# redirect to main function
	jal __main

	# make system call to terminate the program
	li $v0, 10
	syscall


__MARS_SCANF_I:
	li $v0, 5
	syscall
	move $t0, $v0
	jr $ra


__MARS_PRINTF_I:
	li $v0, 1
	syscall
	jr $ra

__main:

	# allocate stack frame for the callee
	addiu $fp, $sp, 0
	addiu $sp, $sp, -136
	sw $ra, 0($fp)

	sw $a0, 8($fp)
	sw $a1, 12($fp)
	sw $a2, 16($fp)
	sw $a3, 20($fp)


	jal __MARS_SCANF_I

	lw $ra, 0($fp)

	lw $a0, 8($sp)
	lw $a1, 12($sp)
	lw $a2, 16($sp)
	lw $a3, 20($sp)

	move $t9, $v0
	sw $ra, 0($fp)

	sw $a0, 8($fp)
	sw $a1, 12($fp)
	sw $a2, 16($fp)
	sw $a3, 20($fp)


	jal __MARS_SCANF_I

	lw $ra, 0($fp)

	lw $a0, 8($sp)
	lw $a1, 12($sp)
	lw $a2, 16($sp)
	lw $a3, 20($sp)

	move $t8, $v0
	bgt $t8,100,iftrue1
	ble $t8,100,iffalse1

iftrue1:
	li $t0,1
	move $t8,$t0

iffalse1:
	li $t0,1
	move $s7,$t0

for11:
	ble $s7,3,for21
	bgt $s7,3,for31

for41:
	addi $s7,$s7,1
	j   for11

for21:
	li $t0,1
	move $s6,$t0

for12:
	ble $s6,3,for22
	bgt $s6,3,for32

for42:
	addi $s6,$s6,1
	j   for12

for22:
	li $t0,1
	move $s5,$t0

for13:
	ble $s5,2,for23
	bgt $s5,2,for33

for43:
	addi $s5,$s5,1
	j   for13

for23:
	add $t9, $t9, $t8
	j for43

for33:
	j for42

for32:
	j for41

for31:
	bgt $t9,50,iftrue12
	ble $t9,50,iffalse12

iftrue12:
	li $t0,0
	move $t9,$t0

iffalse12:
	sw $ra, 0($fp)

	sw $a0, 8($fp)
	sw $a1, 12($fp)
	sw $a2, 16($fp)
	sw $a3, 20($fp)

	move $a0, $25

	jal __MARS_PRINTF_I

	lw $ra, 0($fp)

	lw $a0, 8($sp)
	lw $a1, 12($sp)
	lw $a2, 16($sp)
	lw $a3, 20($sp)

	addi $v0, $v0, 0
	addiu $sp, $fp, 0
	addiu $fp, $fp, -136
	jr $ra
	
	
