#include <stdio.h>
#include <stdlib.h>
#define min(a, b) (a < b) ? a : b
#define max(a, b) (a < b) ? b : a

typedef struct DeterministicDie {
    int curr;
    int max;
    int throws;
} DDie;

typedef struct Player {
    long pos;
    long score;
} Player;

int throwDie(DDie* die) {
    die->throws++;
    return (die->curr++) % (die->max) + 1;
}

long partOne(int p1, int p2) {
    DDie die = {0, 100, 0};
    int score1 = 0;
    int score2 = 0;
    int turn = 1;
    while (score1 < 1000 && score2 < 1000) {
        int throw = throwDie(&die) + throwDie(&die) + throwDie(&die);
        if (turn == 1) {
            p1 = (p1 + throw) % 10;
            score1 += p1 + 1;
        } else if (turn == 0) {
            p2 = (p2 + throw) % 10;
            score2 += p2 + 1;
        }
        turn = (turn + 1) % 2;
    }
    long result = min(score1, score2) * die.throws;
    printf("Loser score * throw count = %ld\n", result);
    return result;
}

// idx                    0  1  2  3  4  5  6  7  8  9
long possiblePaths[10] = {0, 0, 0, 1, 3, 6, 7, 6, 3, 1};
void recu(Player p1, Player p2, int turn, long long mult, long long* wins1, long long* wins2) {
    if (p1.score >= 21) {
        *wins1 += mult;
        return;
    }
    if (p2.score >= 21) {
        *wins2 += mult;
        return;
    }
    if (turn % 2 == 0) {  // player one
        for (int i = 3; i <= 9; i++) {
            Player newP1;
            newP1.pos = (p1.pos + i) % 10;
            newP1.score = p1.score + newP1.pos + 1;
            recu(newP1, p2, turn + 1, mult * possiblePaths[i], wins1, wins2);
        }
    } else {  // player two
        for (int i = 3; i <= 9; i++) {
            Player newP2;
            newP2.pos = (p2.pos + i) % 10;
            newP2.score = p2.score + newP2.pos + 1;
            recu(p1, newP2, turn + 1, mult * possiblePaths[i], wins1, wins2);
        }
    }
}

// The trick:
// it doesn't matter what numbers we got while throwing the dice three times. All that matters
// is in how many ways it could have happened
long partTwo(int pos1, int pos2) {
    long long wins1 = 0;
    long long wins2 = 0;
    Player p1 = {pos1, 0};
    Player p2 = {pos2, 0};
    recu(p1, p2, 0, 1, &wins1, &wins2);
    printf("Player      %% of total wins            wins\n");
    printf("1             %.7g%%           %lld\n", (100 * wins1) / (double)(wins1 + wins2), wins1);
    printf("2             %.7g%%           %lld\n", (100 * wins2) / (double)(wins1 + wins2), wins2);
    printf("The ultimate winner is %s with %lld wins\n", (max(wins1, wins2) == wins1) ? "player 1" : "player 2", max(wins1, wins2));
    return max(wins1, wins2);
}

int main(int argc, char** argv) {
    int p1 = 0;
    int p2 = 0;
    if (argc > 2) {
        p1 = atoi(argv[1]) - 1;
        p2 = atoi(argv[2]) - 1;
    } else {
        printf("Usage: %s [player 1 start] [player 2 start]", argv[0]);
        return 0;
    }
    partOne(p1, p2);
    partTwo(p1, p2);
    return 0;
}