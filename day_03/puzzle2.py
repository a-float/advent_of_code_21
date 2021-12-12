lines = []
with open('puzzle_data.txt', 'r') as f:
    lines = list(map(lambda x: x.strip(), f.readlines()))

def get_score(is_maxing, lines, idx=0):
    # print(lines)
    if len(set(lines)) == 1:
        return lines[0]
    else:
        count = 0
        for line in lines:
            if line[idx] == '1':
                count += 1
        next_digit = '1' if count >= len(lines)/2 else '0'
        if not is_maxing:
            next_digit = str((1+ int(next_digit)) % 2)
        lines = list(filter(lambda x: x[idx] == next_digit, lines))
        return get_score(is_maxing, lines, idx+1)

OX = get_score(True, lines)
CO2 = get_score(False, lines)
# print(OX, CO2, int(OX, 2), int(CO2, 2))
print(f"Solution is {int(OX, 2) * int(CO2, 2)}")