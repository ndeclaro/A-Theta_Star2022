import random

# making 50 files
for i in range(5):
    file_no = i + 1
    file_name = "grid" + str(file_no) + ".txt"

    # choose 500 random blocked cells (10% of 5000)
    blocked = set()
    for j in range(500):
        cell = random.randint(1, 5000)
        if blocked.__contains__(cell):
            j -= 1
        else:
            blocked.add(cell)

    # new file
    with open(file_name, 'w') as f:
        # random start and goal coordinates
        start = str(random.randint(1, 100)) + " " + str(random.randint(1, 50))
        goal = str(random.randint(1, 100)) + " " + str(random.randint(1, 50))
        f.write(start + '\n' + goal + '\n' + "100 50\n")

        cell_count = 1
        # populating grid
        for x in range(100):
            for y in range(50):
                f.write(str(x+1) + " " + str(y+1) + " ")
                if blocked.__contains__(cell_count):
                    f.write("1\n")
                else:
                    f.write("0\n")
                cell_count += 1
