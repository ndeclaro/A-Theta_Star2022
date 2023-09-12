import subprocess
import os
import sys


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

#loading textfile
input = sys.argv[1]
load(input)

#compiling necessary algorithm files
os.system("javac -d ../out MinHeap.java")
print("MinHeap.java compiled")
os.system("javac -d ../out HeapNode.java")
print("HeapNode.java compiled")
os.system("javac -d ../out DriverA.java")
print("DriverA.java compiled")
os.system("javac -d ../out DriverTheta.java")
print("DriverTheta.java compiled")
print("Running DriverA")
os.system("java -cp ../out DriverTheta test_grid.txt test_solution.txt")
print("DriverTheta running")
print("GUI displayed")

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
os.system("python GUI.py test_grid.txt")
