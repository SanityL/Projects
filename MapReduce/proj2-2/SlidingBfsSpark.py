from pyspark import SparkContext
import Sliding, argparse

def bfs_flat_map(value):
    """ YOUR CODE HERE """
    re = []
    value = (value[0], value[1])
    re.append(value)
    if value[1] == (level-1): #check if its the previous level
        children = Sliding.children(WIDTH, HEIGHT, value[0]) #children is a list of children
        for each in children:
            re.append(tuple((each, level)))
    return re

def bfs_reduce(value1, value2):
    """ YOUR CODE HERE """
    #only choose the lowest level
    return min(value1, value2)

def solve_puzzle(master, output, height, width, slaves):
    global HEIGHT, WIDTH, level
    HEIGHT=height
    WIDTH=width
    level = 0

    sc = SparkContext(master, "python")

    sol = Sliding.solution(WIDTH, HEIGHT)
    """ YOUR CODE HERE """
    myRdd = sc.parallelize([(sol, level)]) # myRdd = [(('A', 'B', 'C', '-'), 0)]
    myRdd = myRdd.flatMap(bfs_flat_map).reduceByKey(bfs_reduce)
    prev_num = 0
    pos_num = myRdd.count()

    while prev_num != pos_num:
        level+=1
        prev_num = pos_num
        myRdd = myRdd.flatMap(bfs_flat_map)
        if level%4==0:
            myRdd = myRdd.partitionBy(16)
        myRdd = myRdd.reduceByKey(bfs_reduce)
        pos_num = myRdd.count()

    """ YOUR OUTPUT CODE HERE """
    myRdd = myRdd.map(lambda a: (a[1], a[0])).sortByKey().collect() # myRdd becomes a list
    for each in myRdd:
        output(str(each[0]) + " " + str(each[1]))

    sc.stop()



""" DO NOT EDIT PAST THIS LINE

You are welcome to read through the following code, but you
do not need to worry about understanding it.
"""

def main():
    """
    Parses command line arguments and runs the solver appropriately.
    If nothing is passed in, the default values are used.
    """
    parser = argparse.ArgumentParser(
            description="Returns back the entire solution graph.")
    parser.add_argument("-M", "--master", type=str, default="local[8]",
            help="url of the master for this job")
    parser.add_argument("-O", "--output", type=str, default="solution-out",
            help="name of the output file")
    parser.add_argument("-H", "--height", type=int, default=2,
            help="height of the puzzle")
    parser.add_argument("-W", "--width", type=int, default=2,
            help="width of the puzzle")
    parser.add_argument("-S", "--slaves", type=int, default=6,
            help="number of slaves executing the job")
    args = parser.parse_args()

    global PARTITION_COUNT
    PARTITION_COUNT = args.slaves * 16

    # call the puzzle solver
    solve_puzzle(args.master, args.output, args.height, args.width, args.slaves)

# begin execution if we are running this file directly
if __name__ == "__main__":
    main()
