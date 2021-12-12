with open('puzzle_data.txt', 'r') as f:
    prev_num = None
    inverses = 0
    for line in f:
        num = int(line)
        if prev_num and num > prev_num:
            inverses += 1
        prev_num = num
print(inverses)