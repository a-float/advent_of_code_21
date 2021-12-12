from collections import defaultdict

def cmp(x, y):
   return (x > y) - (x < y)

class Board:
    def __init__(self):
        self.tiles = defaultdict(lambda: 0)
    
    def parse_orthogonal_line(self, start, end):
        x1, y1 = map(int, start.split(','))
        x2, y2 = map(int, end.split(','))
        if x1 == x2:
            while y1 != y2:
                self.mark_point(x1, y1)
                y1 -= cmp(y1,y2)
            self.mark_point(x1, y1)
        elif y1 == y2:
            while x1 != x2:
                self.mark_point(x1, y1)
                x1 -= cmp(x1,x2)
            self.mark_point(x1, y1)
    
    def mark_point(self, x, y):
        self.tiles[f"{x},{y}"] += 1

    def get_danger_count(self):
        return len([1 for val in self.tiles.values() if val > 1])

    def show_tiles(self):
        for item in self.tiles.items():
            print(item)

board = Board()
with open('puzzle_data.txt', 'r') as f:
    for line in f:
        line = line.strip().split(' -> ')
        # print("line is :", line)
        board.parse_orthogonal_line(line[0], line[1])

print(board.get_danger_count())