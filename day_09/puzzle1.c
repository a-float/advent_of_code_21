#include <stdio.h>
#define MAX_LINE 1000

int map[1000000];
char line[1001];
int width = 0;
int height = 1;

int getFromMap(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height)
        return 999;
    else
        return map[y * width + x];
}

int findMinimaScore() {
    int score = 0;
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            int thisVal = getFromMap(j, i);
            if (thisVal < getFromMap(j - 1, i) &&
                thisVal < getFromMap(j + 1, i) &&
                thisVal < getFromMap(j, i + 1) &&
                thisVal < getFromMap(j, i - 1)){
                    // printf("Minimum at %d %d with val %d\n", j, i, thisVal);
                    score += thisVal + 1;
                } 
        }
    }
    return score;
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