#include <fstream>
#include <iostream>
#include <string>
using namespace std;
#define OWIDTH 10
#define OHEIGHT 10

string line;
int octos[OWIDTH * OHEIGHT];

int *get_octo(int x, int y, int arr[]) { return arr + (y * OWIDTH + x); }

void flash(int x, int y, int arr[]) {
    (*get_octo(x, y, arr)) = -1000;
    if (y > 0) {
        (*get_octo(x, y - 1, arr))++;
        if (x > 0) (*get_octo(x - 1, y - 1, arr))++;
        if (x < OWIDTH - 1) (*get_octo(x + 1, y - 1, arr))++;
    }
    if (y < OHEIGHT - 1) {
        (*get_octo(x, y + 1, arr))++;
        if (x > 0) (*get_octo(x - 1, y + 1, arr))++;
        if (x < OWIDTH - 1) (*get_octo(x + 1, y + 1, arr))++;
    }
    if (x > 0) (*get_octo(x - 1, y, arr))++;
    if (x < OWIDTH - 1) (*get_octo(x + 1, y, arr))++;
}

// returns the number of flashes
int do_step(int arr[]) {
    int all_flashes = 0;

    for (int y = 0; y < OHEIGHT; y++) {
        for (int x = 0; x < OWIDTH; x++) {
            int *octo = get_octo(x, y, arr);
            (*octo)++;
            if (*octo > 9) {
                all_flashes++;
                flash(x, y, arr);
            }
        }
    }
    int flashes = 1;
    while (flashes > 0) {
        flashes = 0;
        for (int y = 0; y < OHEIGHT; y++) {
            for (int x = 0; x < OWIDTH; x++) {
                if ((*get_octo(x, y, arr)) > 9) {
                    all_flashes++;
                    flashes++;
                    flash(x, y, arr);
                }
            }
        }
    }
    for (int y = 0; y < OHEIGHT; y++) {
        for (int x = 0; x < OWIDTH; x++) {
            if ((*get_octo(x, y, arr)) < 0) {
                (*get_octo(x, y, arr)) = 0;
            }
        }
    }
    return all_flashes;
}

int main() {
    ifstream file("puzzle_data.txt");
    int h = 0;
    while (getline(file, line)) {
        for (int i = 0; i < line.size(); i++) {
            octos[h * OWIDTH + i] = line[i] - '0';
        }
        h++;
    }
    // part 1
    // long all_flashes = 0;
    // for (int i = 0; i < 100; i++) {
    //     all_flashes += do_step(octos);
    // }
    // cout << "No. of flashes is " << all_flashes << endl;

    // part 2
    for (int i = 0; i < 1000; i++) {
        if (do_step(octos) == OWIDTH * OHEIGHT) {
            cout << "Octopuses all flash at the sime time after step " << i + 1 << endl;
            return 0;
        };
    }

    return 0;
}