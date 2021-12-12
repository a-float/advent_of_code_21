x = 0
y = 0
aim = 0
with open('puzzle_data.txt', 'r') as f:
    for line in f:
        comm, val = line.split()
        val = int(val)
        if comm == 'forward':
            x += val
            y += val*aim
        elif comm == 'down':
            aim += val
        elif comm == 'up':
            aim -= val
print(f'{x=} {y=} {x*y=}')