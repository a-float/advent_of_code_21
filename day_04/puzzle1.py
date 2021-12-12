class Board:
    def __init__(self, board):
        self.board = board
        self.marked = [[False] * 5 for i in range(5)]
    
    def mark(self, num):
        for i in range(5):
            for j in range(5):
                if self.board[i][j] == num:
                    self.marked[i][j] = True
    
    def check_complete(self):
        for i in range(5):
            if all(marked == True for marked in [self.marked[i][j] for j in range(5)]):
                return True
            elif all(marked == True for marked in [self.marked[j][i] for j in range(5)]):
                return True
            elif all(marked == True for marked in self.board[i][i]):
                return True
            elif all(marked == True for marked in self.board[i][4-i]):
                return True
        return False

    def get_score(self):
        s = 0
        for i in range(5):
            for j in range(5):
                if not self.marked[i][j]:
                    s += int(self.board[i][j])
        return s
    
    def __repr__(self):
        return 'Board:\n' + '\n'.join(str(row) for row in self.board) + '\nMarked:\n' + '\n'.join(str(row) for row in self.marked) + '\n'

with open('puzzle_data.txt', 'r') as f:
    lines = f.readlines()

picks = lines[0].strip().split(',')

boards = []
for idx in range(1, len(lines), 6):
    board = []
    for i in range(5):
        board.append(lines[idx+i+1].split())
    boards.append(Board(board))

# for board in boards:
#     print(board)

for pick in picks:
    for board in boards:
        board.mark(pick)
        if board.check_complete():
            print("Board complete")
            print(board)
            board_score = board.get_score()
            print(pick, board_score)
            print(int(pick) * board_score)
            exit()
