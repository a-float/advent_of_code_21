#include <stdio.h>
#include <stdlib.h>
#define MAX_LINE 1000
#define MAP_SIZE 1000000
int map[MAP_SIZE];
int visited[MAP_SIZE];
char line[1001];
int width = 0;
int height = 1;

int getFromMap(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height)
        return 999;
    else
        return map[y * width + x];
}

int comp(const void *a, const void *b) {
    int *x = (int *)a;
    int *y = (int *)b;
    return *y - *x;
}

typedef struct Pos {
    int x;
    int y;
} Pos;

int findBasinSize(int x, int y) {
    Pos stack[5000];
    stack[0] = (Pos){.x = x, .y = y};
    int stackSize = 1;
    int basinSize = 0;
    while (stackSize != 0) {
        Pos pos = stack[--stackSize];
        if (getFromMap(pos.x, pos.y) < 9 && visited[pos.y * width + pos.x] == 0) {
            visited[pos.y * width + pos.x] = 1;
            basinSize++;
            stack[stackSize] = (Pos){pos.x - 1, pos.y};
            stack[stackSize + 1] = (Pos){pos.x + 1, pos.y};
            stack[stackSize + 2] = (Pos){pos.x, pos.y - 1};
            stack[stackSize + 3] = (Pos){pos.x, pos.y + 1};
            stackSize += 4;
        }
    }
    printf("Basin size for %d %d is %d\n", x, y, basinSize);
    return basinSize;
}

int findMinimaScore() {
    int topBasins[1000];
    for (int i = 0; i < 1000; i++) {
        topBasins[i] = 0;
    }
    int currentBasin = 0;
    for (int i = 0; i < width * height; i++) {
        visited[i] = 0;
    }
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            int thisVal = getFromMap(j, i);
            if (thisVal < getFromMap(j - 1, i) &&
                thisVal < getFromMap(j + 1, i) &&
                thisVal < getFromMap(j, i + 1) &&
                thisVal < getFromMap(j, i - 1)) {
                // printf("Minimum at %d %d with val %d\n", j, i, thisVal);
                topBasins[currentBasin++] = findBasinSize(j, i);
            }
        }
    }
    qsort(topBasins, sizeof(topBasins) / sizeof(*topBasins), sizeof(*topBasins), comp);
    return topBasins[0] * topBasins[1] * topBasins[2];
}

int main() {
    FILE *file = fopen("puzzle_data.txt", "r");
    if (file == NULL) {
        printf("Could not open the file");
        return 77;
    }
    fgets(line, MAX_LINE, file);
    for (int i = 0; i < MAX_LINE; i++) {
        if (line[i] >= '0' && line[i] <= '9') {
            map[i] = line[i] - '0';
            width++;
        } else
            break;
    }
    while (fgets(line, MAX_LINE, file) != NULL) {
        for (int i = 0; i < width; i++) {
            map[width * height + i] = line[i] - '0';
        }
        height++;
    }

    printf("\nSum of the risk levels is %d\n", findMinimaScore());

    fclose(file);
    return 0;
}