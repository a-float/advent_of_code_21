#include <fstream>
#include <iostream>
#include <set>
#include <sstream>
#include <string>
#include <vector>
using namespace std;

int rotations[24][3] = {
    {1, 2, 3},  // right
    {1, 3, -2},
    {1, -2, -3},
    {1, -3, 2},
    {3, 2, -1},  // front
    {3, -1, -2},
    {3, -2, 1},
    {3, 1, 2},
    {-1, 2, -3},  // left
    {-1, -3, -2},
    {-1, -2, 3},
    {-1, 3, 2},
    {-3, 2, 1},  // back
    {-3, 1, -2},
    {-3, -2, -1},
    {-3, -1, 2},
    {2, -3, -1},  // up
    {2, 1, -3},
    {2, 3, 1},
    {2, -1, 3},
    {-2, -1, -3},  // down
    {-2, -3, 1},
    {-2, 1, 3},
    {-2, 3, -1},
};

struct Vector3 {
    int x, y, z;
    Vector3(int _x, int _y, int _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    Vector3 rotated(int id) {
        int* rot = rotations[id];
        return Vector3(decode(rot[0]), decode(rot[1]), decode(rot[2]));
    }
    Vector3 add(Vector3 other) {
        return Vector3(x + other.x, y + other.y, z + other.z);
    }
    Vector3 sub(Vector3 other) {
        return Vector3(x - other.x, y - other.y, z - other.z);
    }
    long manhattanDist(Vector3 other) {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z);
    }

    string toString() {
        ostringstream oss;
        oss << "[" << x << ", " << y << ", " << z << "]";
        return oss.str();
    }
    bool equals(Vector3 other) {
        return x == other.x && y == other.y && z == other.z;
    }

   private:
    int decode(int d) {
        int coord;
        if (abs(d) == 1)
            coord = x;
        else if (abs(d) == 2)
            coord = y;
        else
            coord = z;
        if (d < 0) return coord * -1;
        return coord;
    }
};

struct Scanner {
    vector<Vector3> points;
    Vector3 pos = Vector3(0, 0, 0);
    int rotation = -1;  // not -1 if the scanner position has been set

    bool tryMatch(Scanner* other, int* correctRotationNo, Vector3* otherPosition) {
        if (rotation == -1 || other->rotation != -1) return false;
        for (int rot = 0; rot < 24; rot++) {
            for (int i = 0; i < this->points.size() - 12; i++) {
                Vector3 vec1 = this->points.at(i);
                for (auto& vec2 : other->points) {  // first match from the other scanner
                    int matches = 0;
                    Vector3 otherPos = pos.add(vec1.rotated(this->rotation).sub(vec2.rotated(rot)));
                    for (auto& vec3 : other->points) {
                        for (auto& vec4 : this->points) {
                            if (pos.add(vec4.rotated(this->rotation)).equals(otherPos.add(vec3.rotated(rot)))) {
                                matches++;
                                if (matches == 12) {
                                    *correctRotationNo = rot;
                                    *otherPosition = otherPos;
                                    return true;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
};

vector<Scanner*> scanners;
string line;

int main() {
    ifstream file("data.txt");
    Scanner* scanner;
    while (getline(file, line, '\n')) {
        if (line.size() == 0)
            continue;
        else if (line[0] == '-' && line[1] == '-') {  // new scannner start
            scanner = new Scanner();
            scanners.push_back(scanner);
            continue;
        } else {
            for (int i = 0; i < line.size(); i++) {
                if (line[i] == ',') line[i] = ' ';
            }
            istringstream is(line);
            int x, y, z;
            is >> x >> y >> z;
            scanner->points.push_back(Vector3(x, y, z));
        }
    }
    vector<int> stack;
    scanners.at(0)->rotation = 0;
    stack.push_back(0);
    while (!stack.empty()) {
        int startId = stack.back();
        stack.pop_back();
        for (int j = 0; j < scanners.size(); j++) {
            if (startId == j) continue;
            int rot;
            Vector3 otherPos = Vector3(0, 0, 0);
            if (scanners.at(startId)->tryMatch(scanners.at(j), &rot, &otherPos)) {
                cout << "Match between scanners " << startId << " and " << j << " with rot " << rot << " at " << otherPos.toString() << endl;
                scanners.at(j)->rotation = rot;
                scanners.at(j)->pos = otherPos;
                stack.push_back(j);
            }
        }
    }

    // counting the number of probes
    set<string> beacons;
    for (auto& scan : scanners) {
        for (auto& vec : scan->points) {
            beacons.insert(scan->pos.add(vec.rotated(scan->rotation)).toString());
        }
    }
    for (auto& beacon : beacons) {
        cout << beacon << endl;
    }

    long maxDist = 0;
    for (auto& scan1 : scanners) {
        for (auto& scan2 : scanners) {
            maxDist = max(maxDist, scan1->pos.manhattanDist(scan2->pos));
        }
    }

    cout << "There are " << beacons.size() << " probes" << endl;
    cout << "The largest Manhattan distance between any two scanners is " << maxDist << endl;

    return 0;
}