import subprocess
import time
import os
import sys


#loads file from grids folder
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

#compile all ncessary files
os.system("javac -d ../out MinHeap.java")
print("MinHeap.java compiled")
os.system("javac -d ../out HeapNode.java")
print("HeapNode.java compiled")
os.system("javac -d ../out DriverA.java")
print("DriverA.java compiled")
os.system("javac -d ../out DriverTheta.java")
print("DriverTheta.java compiled")

#structure to hold running time
A_times = list()
Theta_times = list()

#A* average runtime over grids 1 - 50
for i in range(50):
    file_no = i + 1
    file_name = "grid" + str(file_no) + ".txt"
    load(file_name)
    print("")
    print("Loaded:" + file_name)
    print("")
    start = time.perf_counter()
    os.system("java -cp ../out DriverA test_grid.txt test_solution.txt")
    end = time.perf_counter()
    A_times.append(end - start)
A_sum = sum(A_times)
A_average = A_sum/len(A_times)

#Theta* average runtime over grids 1 - 50
for i in range(50):
    file_no = i + 1
    file_name = "grid" + str(file_no) + ".txt"
    load(file_name)
    print("")
    print("Loaded:" + file_name)
    print("")
    start = time.perf_counter()
    os.system("java -cp ../out DriverTheta test_grid.txt test_solution.txt")
    end = time.perf_counter()
    Theta_times.append(end - start)
T_sum = sum(Theta_times)
T_average = T_sum/len(Theta_times)


#results
print("")
print("**Results**")
print("  A* average runtime over 50 grids: " + str(A_average))
print("  Theta* average runtime over 50 grids: " + str(T_average))
