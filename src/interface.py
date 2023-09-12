import subprocess
import time
import os
import sys

#loads file
def load(filename):
    grid_write = os.path.dirname('test_grid.txt')
    path = '..\\grids\\'
    grid_read = os.path.relpath(path + filename, grid_write)
    with open('grids/' + filename, 'r') as file_read:
        with open('test_grid.txt', 'w') as file_write:
            for line in file_read:
                if line == "\n":
                    continue
                file_write.write(line)
        file_write.close()
    file_read.close()
    print("loaded: " + filename)

#load display
def runDisplay(filename):
    print("Displayed: " + filename)
    #display start and goal node
    with open('test_grid.txt', 'r') as points:
        #start
        line = points.readline()
        print("Start: " + line)
        #goal
        line = points.readline()
        print("Goal: " + line)
    points.close()
    #display
    print("Graph displayed. Close to load another graph")
    os.system("python GUI.py test_grid.txt")

#runs and displays A* display
def commandLA(user_input):
    args = user_input.split()
    load(args[1])
    os.system("javac -d ../out MinHeap.java")
    print("MinHeap.java compiled")
    os.system("javac -d ../out HeapNode.java")
    print("HeapNode.java compiled")
    os.system("javac -d ../out DriverA.java")
    print("DriverA.java compiled")
    print("Running DriverA")
    start = time.perf_counter()
    os.system("java -cp ../out DriverA test_grid.txt test_solution.txt")
    end = time.perf_counter()
    print("")
    print("A* runtime: " + str(end-start))
    ToDisplay = True
    with open('test_solution.txt', 'r') as file:
        line = file.readline()
        if(line == "NO VALID PATH"):
            ToDisplay = False
    file.close()
    if(ToDisplay):
        runDisplay(args[1])
    else:
        print("")
        print("NO VALID PATH")
        print("")

#runs and displays Theta* display
def commandLT(user_input):
    args = user_input.split()
    load(args[1])
    os.system("javac -d ../out MinHeap.java")
    print("MinHeap.java compiled")
    os.system("javac -d ../out HeapNode.java")
    print("HeapNode.java compiled")
    os.system("javac -d ../out DriverTheta.java")
    print("DriverTheta.java compiled")
    print("Running DriverTheta")
    start = time.perf_counter()
    os.system("java -cp ../out DriverTheta test_grid.txt test_solution.txt")
    end = time.perf_counter()
    print("")
    print("Theta* runtime: " + str(end-start))
    ToDisplay = True
    with open('test_solution.txt', 'r') as file:
        line = file.readline()
        if(line == "NO VALID PATH"):
            ToDisplay = False
    file.close()
    if(ToDisplay):
        runDisplay(args[1])
    else:
        print("")
        print("NO VALID PATH")
        print("")


running = True
while running:
    print("** list of commands **")
    print("")
    print("  'LA filename.txt' - loads file and runs A*  ")
    print("  'LT filename.txt' - loads file and runs Theta*  ")
    print("")
    print("  To display F, G, and H values:")
    print("  Set the S_Vertex at the top of the display  ")
    print("  input another vertex to calculate values for A* or Theta*  ")
    print("  Example input in display '20 39'  ")
    print("")
    user_input = input("** Q to exit program **\n")
    if(user_input == 'Q'):
        break
    elif(user_input[0] == 'L' and user_input[1] == 'A'):
        commandLA(user_input)
    elif(user_input[0] == 'L' and user_input[1] == 'T'):
        commandLT(user_input)
    else:
        print("**invalid command**")
