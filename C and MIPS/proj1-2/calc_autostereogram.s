.text

# Generates an autostereogram inside of buffer
#
# Arguments:
#     autostereogram (unsigned char*)
#     depth_map (unsigned char*)
#     width
#     height
#     strip_size
calc_autostereogram:

        # Allocate 5 spaces for $s0-$s5
        # (add more if necessary)
        addiu $sp $sp -32
        sw $s0 0($sp)
        sw $s1 4($sp)
        sw $s2 8($sp)
        sw $s3 12($sp)
        sw $s4 16($sp)
        sw $ra 20($sp) 
        sw $s5 24($sp) # i
        sw $s6 28($sp) # j

        # autostereogram
        lw $s0 32($sp)
        # depth_map
        lw $s1 36($sp)
        # width
        lw $s2 40($sp)
        # height
        lw $s3 44($sp)
        # strip_size
        lw $s4 48($sp)

        # YOUR CODE HERE #  
        addiu $s5 $0 0 # s5 = i = 0
        addiu $s6 $0 0 # s6 = j = 0 

For_i:
        # li $v0 1
        # add $a0 $s2 $0
        # Syscall
        beq $s5 $s2 Finish # if (i == width), go to Finish.        

For_j:
        
        beq $s6 $s3 Increment_i_and_go_to_For_i # if (j == height), go to Increment_i_and_go_to_For_i.

        sltu $t0 $s5 $s4 # if (i < S), t0 = 1.
        beq $t0 $0 Else # if (i >= S), go to Else.
        
        jal lfsr_random

        multu $s6 $s2 # j * width
        mflo $t1 # $t1 = LO = j * width
        addu $t1 $s5 $t1 # $t1 = i + j * width
        addu $t2 $s0 $t1 # $t2 = autostereogram + (i + j * width) -- pointer

        andi $v0 $v0 0xff # $v0 = lfsr_random() & 0xff
        sb $v0 0($t2)

        addiu $s6 $s6 1 # j++

        j For_j

Else:
        
        multu $s6 $s2 # j * width
        mflo $t1 # $t1 = LO = j * width
        addu $t1 $s5 $t1 # $t1 = i + j * width
        addu $t2 $s0 $t1 # $t2 = autostereogram + (i + j * width) -- pointer
        addu $t3 $s1 $t1 # $t3 = depth + (i + j * width) -- pointer

        lb $t4 0($t3) # load the value from depth(i, j)
        addu $t4 $t4 $s5 # $t4 = i + depth(i, j)
        subu $t4 $t4 $s4 # $t4 = i + depth(i, j) - S
        multu $s6 $s2 # j * width
        mflo $t1 # $t1 = LO = j * width
        addu $t4 $t4 $t1 # $t4 = i + depth(i, j) - S + j * width

        addu $t5 $s0 $t4
        # $t5 is pointer of autostereogram to (i + depth(i, j) - S + j * width)th element
        lb $t6 0($t5) # $t6 is the value of I((i + depth(i, j) - S), j)
        sb $t6 0($t2)

        addiu $s6 $s6 1 # j++
        j For_j
        
Increment_i_and_go_to_For_i:
        
        addiu $s6 $0 0 # j=0
        addiu $s5 $s5 1 # i++
        j For_i

Finish:

        lw $s0 0($sp)
        lw $s1 4($sp)
        lw $s2 8($sp)
        lw $s3 12($sp)
        lw $s4 16($sp)
        lw $ra 20($sp)
        lw $s5 24($sp)
        lw $s6 28($sp)
        addiu $sp $sp 32

        jr $ra
