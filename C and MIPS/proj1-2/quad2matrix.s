.text

# Decodes a quadtree to the original matrix
#
# Arguments:
#     quadtree (qNode*)
#     matrix (void*)
#     matrix_width (int)
#
# Recall that quadtree representation uses the following format:
#     struct qNode {
#         int leaf;
#         int size;
#         int x;
#         int y;
#         int gray_value;
#         qNode *child_NW, *child_NE, *child_SE, *child_SW;
#     }

quad2matrix:

        # YOUR CODE HERE #
        lw $t8 0($a0)
        beq $t8 $0 morepieces 
        
fillcolorloop: #base case, fills matrix with gray_value
	
	lw $t0 8($a0) #loads int x from quadtree struct 
	lw $t1 12($a0) #loads int y from quadtree struct 
	lw $t2 4($a0) #loads size from quadtree struct
	li $t3 0 #initialize i to 0

	forloop1:
		slt $t5 $t3 $t2 #checks if i < size, stores 1 in $t5 if true, stores 0 o/w
		beq $t5 $0 return
		li $t4 0 #initilaize j to 0
		
	forloop2:
		slt $t6 $t4 $t2 #checks to see if j < size, stores 1 in $t6 if true, o/w stores 0
		beq $t6 $0 increment #goes to increment i if j is finished looping through
		addu $t7 $t1 $t3 # (x+j) + image_width*(y+i) gets index
		multu $t7 $a2 #multiplies image_width with (y+i)
		mflo $t7 #get the lower 32 bits and load it into $t7
		addu $t7 $t7 $t0 #adds x to image_width*(y+i)
		addu $t7 $t7 $t4 #adds j to x + image)width*(y+i)
		lw $t9 16($a0) #loads gray_value into $t9
		addu $t7 $t7 $a1 #moves matrix to the correct index position 
		sb $t9 0($t7) #stores the gray value at the correct position in the matrix 
		addiu $t4 $t4 1 #increments j 
		j forloop2 #jumps back to forloop2
		
	increment:
		addiu $t3 $t3 1 #increments i
		j forloop1 #jumps to loop through forloop1

morepieces: #recursive call 
	addiu $sp $sp -8
	sw $ra 0($sp) 
	sw $a0 4($sp)
	lw $a0 20($a0)
	jal quad2matrix
	lw $a0 24($a0)
	jal quad2matrix
	lw $a0 28($a0)
	jal quad2matrix
	lw $a0 32($a0)
	jal quad2matrix
	
	lw $ra 0($sp)
	addiu $sp $sp 8
	
return:
	lw $a0 4($sp)
        jr $ra
        
        
