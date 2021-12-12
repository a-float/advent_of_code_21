x = 0
y = 0
with open('puzzle_data.txt', 'r') as f:
    for line in f:
        comm, val = line.split()
        val = int(val)
        if comm == 'forward':
            x += val
        elif comm == 'down':
            y += val
        elif comm == 'up':
            y -= val
print(f'{x=} {y=} {x*y=}')