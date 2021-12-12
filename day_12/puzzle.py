from collections import defaultdict
neigh = defaultdict(list)
paths1 = 0
paths2 = 0

with open('puzzle_data.txt', 'r') as f:
    for line in f:
        ends = line.strip().split('-')
        neigh[ends[0]].append(ends[1])
        neigh[ends[1]].append(ends[0])


def recu_search1(curr, target, visited=[]):
    '''part 1'''
    if curr == target:
        global paths1
        paths1 += 1
        return
    for n in neigh[curr]:
        if n.isupper() or n not in visited:
            recu_search1(n, target, visited + [curr])


def recu_search2(curr, target, visited=[], double_small=False):
    '''part 2'''
    if curr == target:
        global paths2
        paths2 += 1
        return
    for n in neigh[curr]:
        if n.isupper() or n not in visited:
            recu_search2(n, target, visited + [curr], double_small)
        elif n not in ['start', 'end'] and double_small == False:
            recu_search2(n, target, visited + [curr], True)


recu_search1('start', 'end')
print(f'Part one solution {paths1}')
recu_search2('start', 'end')
print(f'Part two solution {paths2}')