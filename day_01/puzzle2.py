with open('puzzle_data.txt', 'r') as f:
    nums = list(map(int, f.readlines()))

rises = 0
for i in range(3, len(nums)):
    if nums[i-3] < nums[i]:
        rises += 1

print(rises)
