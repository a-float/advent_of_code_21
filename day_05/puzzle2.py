from collections import defaultdict

def cmp(x, y):
   return (x > y) - (x < y)

class Board:
    def __init__(self):
        self.tiles = defaultdict(lambda: 0)
    
    def parse_any_line(self, start, end):
        x1, y1 = map(int, start.split(','))
        x2, y2 = map(int, end.split(','))
        if x2 < x1:
            (x1, y1), (x2, y2) = (x2, y2), (x1, y1)
        elif x1 == x2:  # vertical lines
            for y in range(min(y1,y2), max(y1,y2) + 1):
                self.mark_point(x1, y)
            return
        slope = (y2 - y1) / (x2 - x1) # dire
        for x in range(x2-x1+1):
            self.mark_point(x1+x, int(y1 + x * slope))

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
        board.parse_any_line(line[0], line[1])

print(board.get_danger_count())