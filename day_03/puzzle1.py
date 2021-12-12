counts = None
total = 0
with open('puzzle_data.txt', 'r') as f:
    for line in f:
        total += 1
        line = line.strip()
        if counts is None:
            counts = [0]*len(line)
        for i, b in enumerate(line):
            counts[i] += int(b)
gamma = ''.join(['1' if count > total / 2 else '0' for count in counts])
gamma_dec = int(gamma, 2)
epsilon = 2**(len(gamma)) - gamma_dec - 1
print(f"Solution is {gamma_dec * epsilon}")

