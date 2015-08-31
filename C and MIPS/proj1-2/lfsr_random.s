.data

lfsr:
        .align 4
        .half
        0x1

.text

# Implements a 16-bit lfsr
#
# Arguments: None
lfsr_random:

        la $t0 lfsr
        lhu $v0 0($t0) #v0 is reg

        # YOUR CODE HERE #
        addiu $t4 $0 16 # $t4 = 16
        addiu $t5 $0 65535 # $t5 = 0b0000 0000 0000 0000 1111 1111 1111 1111
For:

		srl $t1 $v0 2 # $t1 = (reg >> 2)
		srl $t2 $v0 3 # $t2 = (reg >> 3)
		srl $t3 $v0 5 # $t3 = (reg >> 5)

		xor $t1 $v0 $t1 # $t1 = (reg >> 0) ^ (reg >> 2)
		xor $t1 $t1 $t2 # $t1 = (reg >> 0) ^ (reg >> 2) ^ (reg >> 3)
		xor $t1 $t1 $t3 # $t1 =  highest = (reg >> 0) ^ (reg >> 2) ^ (reg >> 3) ^ (reg >> 5)

		srl $t2 $v0 1 # $t2 = (reg >> 1)
		sll $t3 $t1 15 # $t3 = (highest << 15)
		
		or $v0 $t2 $t3 # $v0 = reg = (reg >> 1) | (highest << 15)
		and $v0 $v0 $t5


		
		addiu $t4 $t4 -1 # $t4 = $t4 - 1
		
		bne $t4 $0 For # if ($t4 != 0), go to For

Return:

        la $t0 lfsr
        sh $v0 0($t0)
        jr $ra
