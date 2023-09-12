import sys
import math
from tkinter import *
from tkinter import ttk

#button to set s vertex
def submit_s():
    input = entry.get().split()
    print("S-vertex: " + entry.get())
    global Sx, Sy
    Sx = float(input[0])
    Sy = float(input[1])
    if entry.get() not in Gtrack:
        print("Invalid Vertex")

#button to display values ofr A*
def Avalues():
    input = entry.get().split()
    print("Input Vertex: "+ entry.get())
    global PTx, PTy
    PTx = float(input[0])
    PTy = float(input[1])
    h = h_value()
    g = g_value()
    f = h + g
    print("A* values:")
    print("H value: " + str(round(h, 2)))
    print("G value: " + str(round(g, 2)))
    print("F value: " + str(round(f, 2)))

#button to dispaly values of Theta*
def Tvalues():
    input = entry.get().split()
    print("Input Vertex: "+ entry.get())
    global PTx, PTy
    PTx = float(input[0])
    PTy = float(input[1])
    h = t_value()
    g = g_value()
    f = h + g
    print("Theta* Values: ")
    print("H value: " + str(round(h, 2)))
    print("G value: " + str(round(g, 2)))
    print("F value: " + str(round(f, 2)))

#calculates value
def g_value():
    distance = math.hypot(Sx - PTx, Sy - PTy)
    format = str(int(Sx)) + " " + str(int(Sy))
    return distance + Gtrack[format]

#calculates heuristic for A*
def h_value():
    xdiff = abs(PTx - goalx)
    ydiff = abs(PTy - goaly)
    hmin = min(xdiff, ydiff)
    hmax = max(xdiff, ydiff)
    heuristic = math.sqrt(2) * hmin + hmax - hmin
    return heuristic

#calculates hueristic for Theta*
def t_value():
    straightline = math.hypot(PTx - goalx, PTy - goaly)
    return straightline

#coordinates to pixel coordinates on configured gui
def toPixel(x, y):
    x1 = x - 1
    y1 = y - 1
    x1 = 20 + (x1 * 100)
    y1 = 20 + (y1 * 100)
    return x1, y1

#reading grid.txt and inputs
with open(sys.argv[1], 'r') as file:
    #start
    line = file.readline()
    arg = line.split()
    startx = int(arg[0])
    starty = int(arg[1])
    #goal
    line = file.readline()
    arg = line.split()
    goalx = int(arg[0])
    goaly = int(arg[1])
    #dimensions
    line = file.readline()
    arg = line.split()
    columns = int(arg[0])
    rows = int(arg[1])
    #array for grid blocking
    grid_close = [[0 for i in range(columns)] for j in range(rows)]
    for line in file:
        arg = line.split()
        row = int(arg[1]) - 1
        col = int(arg[0]) - 1
        blocked = int(arg[2])
        grid_close[row][col] = blocked
file.close()

#building dictonary for g value tracking
Gtrack = {}
Xprevious = startx
Yprevious = starty
distance = 0
for line in reversed(list(open("test_solution.txt"))):
    format = line.rstrip()
    format = format.replace(",", " ")
    #buidling sum for gvalue
    arg = format.split()
    Xcurr = int(arg[0])
    Ycurr = int(arg[1])
    distance = math.hypot(Xcurr - Xprevious, Ycurr - Yprevious) + distance
    Gtrack[format] = distance
    Xprevious = Xcurr
    Yprevious = Ycurr

#making display
root = Tk()
root.geometry("800x800")

#data entry

Sx = 0
Sy = 0
PTx = 0
PTy = 0

S_VERTEX = Button(root, text = "S_VERTEX", command = submit_s)
S_VERTEX.pack()
A_values = Button(root, text = "A* values", command = Avalues)
A_values.pack()
T_values = Button(root, text = "Theta* values", command = Tvalues)
T_values.pack()

entry = Entry(root, width = 20)
entry.pack()
entry.insert(0, "")

#graph related
main_frame = Frame(root)
main_frame.pack(fill = BOTH, expand = 1)

display = Canvas(main_frame)
display.pack(fill = BOTH, expand = 1)

yscrollbar = ttk.Scrollbar(main_frame, orient = VERTICAL, command = display.yview)
yscrollbar.pack(side = RIGHT, fill = Y)
display.configure(yscrollcommand = yscrollbar.set)

xscrollbar = ttk.Scrollbar(main_frame, orient = HORIZONTAL, command = display.xview)
xscrollbar.pack(side = BOTTOM, fill = X)
display.configure(xscrollcommand = xscrollbar.set)

display.bind('<Configure>', lambda e: display.configure(scrollregion = display.bbox("all")))


#grid end dimensions
gridx = (columns*100) + 20
gridy = (rows*100) + 20

#blocking out grid
for i in range(columns):
    for j in range(rows):
        if(grid_close[j][i] == 1):
            blockx, blocky = toPixel(i + 1, j + 1)
            display.create_rectangle(blockx, blocky, blockx + 100, blocky + 100, fill = "gray")
#row drawing
for i in range(20, gridy + 100 , 100):
    display.create_line(20, i, gridx, i, fill = "black")
    num = (i - 20)/100
    if(num < row + 2):
        display.create_text(10, 10 + i, text = int(num) + 1)
#column drawing
for i in range(20, gridx + 100 , 100):
    display.create_line(i, 20, i, gridy, fill = "black")
    num = (i - 20)/100
    if(num < columns + 1):
            display.create_text(10 + i, 10, text = int(num) + 1)

#y coordinates
for i in range (100, gridy, 100):
    num = i/100 + 1
    display.create_text(130, i + 10, text = int(num))
    for j in range(100, gridx, 100):
        display.create_text(j + 30, i + 10, text = int(num))

#x coordinates
for i in range(100, gridx, 100):
    num = i/100 + 1
    display.create_text(i + 10, 110, text = int(num))
    for j in range(100, gridy, 100):
        display.create_text(i + 10, j + 10, text = int(num))

#tracing solution
with open('test_solution.txt', 'r') as file:
    line = file.readline()
    arg = line.split(',')
    xstart, ystart = toPixel(int(arg[0]), int(arg[1]))
    for line in file:
        arg = line.split(',')
        xend, yend = toPixel(int(arg[0]), int(arg[1]))
        display.create_line(xstart, ystart, xend, yend, fill = "red")
        xstart = xend
        ystart = yend
file.close()

#start plot input
start_x1, start_y1 = toPixel(startx, starty)
display.create_oval(start_x1 - 5, start_y1 - 5, start_x1 + 5, start_y1 + 5, fill = "green")
display.create_text(start_x1 + 20, start_y1 + 10, text = 'start')

#goal plot input
goal_x1, goal_y1 = toPixel(goalx, goaly)
display.create_oval(goal_x1 - 5, goal_y1 - 5, goal_x1 + 5, goal_y1 + 5, fill = "red")
display.create_text(goal_x1 + 15, goal_y1 + 10, text = 'goal')



root.mainloop()
