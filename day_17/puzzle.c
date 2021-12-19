#include <stdio.h>
#include <stdlib.h>

typedef struct Vector {
    int x;
    int y;
} Vector;

void updateVelocity(Vector *vel) {
    vel->y -= 1;
    if (vel->x > 0)
        vel->x--;
    else if (vel->x < 0)
        vel->x++;
}

Vector add(Vector a, Vector b) {
    return (Vector){a.x + b.x, a.y + b.y};
}

int simulate(Vector pos, Vector vel, Vector targetStart, Vector targetEnd) {
    while (pos.y > targetEnd.y) {
        pos = add(pos, vel);
        if (pos.x >= targetStart.x && pos.x <= targetEnd.x && pos.y <= targetStart.y && pos.y >= targetEnd.y) {
            return 1;
        }
        updateVelocity(&vel);
    }
    return 0;
}

int main(int argc, char** argv) {
    int x1, x2, y1, y2;
    if(argc < 5){
        x1 = 20; x2 = 30; y1 = -10; y2 = -5;
    }
    else {
        x1 = atoi(argv[1]); x2= atoi(argv[2]); y1 = atoi(argv[3]); y2 = atoi(argv[4]);
    }
    Vector targetStart = {x1, y2};
    Vector targetEnd = {x2, y1};
    Vector startPos = {0, 0};
    int bestY = 0;
    int possibleShots = 0;
    // printf("%d", simulate(startPos, (Vector){6, 9}, targetStart, targetEnd));
    for (int y = targetEnd.y; y < 1000; y++) {
        for (int x = 1; x <= targetEnd.x; x++) {
            // printf("Simulating for %d %d\n", x, y);
            if (simulate(startPos, (Vector){x, y}, targetStart, targetEnd)) {
                printf("New shot with top height %d for vel=[%d, %d]\n", y * (y+1) / 2, x, y);
                bestY = y;
                possibleShots++;
                // break;
            }
        }
    }
    printf("Solution:\n");
    printf("Highest y position is %d\n", bestY * (bestY + 1) / 2);
    printf("There are %d possible shots\n", possibleShots);
    return 0;
}