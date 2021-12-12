#include <algorithm>
#include <fstream>
#include <iostream>
#include <map>
#include <string>
#include <vector>

using namespace std;

map<char, int> error_bracket_value{{')', 3}, {']', 57}, {'}', 1197}, {'>', 25137}};
map<char, long> autocomplete_bracket_value{{')', 1}, {']', 2}, {'}', 3}, {'>', 4}};
map<char, char> closing_brackets{{'(', ')'}, {'[', ']'}, {'{', '}'}, {'<', '>'}};

int main() {
    string line;
    vector<char> stack;
    int syntax_score = 0;
    vector<long long> autocomplete_scores;
    ifstream file("puzzle_data.txt");

    while (getline(file, line)) {
        for (auto bracket : line) {
            if (bracket == '(' || bracket == '{' || bracket == '[' || bracket == '<') {
                stack.push_back(closing_brackets[bracket]);
            } else if (bracket != stack.back()) {  // part one of the puzzle
                syntax_score += error_bracket_value[bracket];
                stack.clear();
                break;
            } else if (bracket == stack.back()) {
                stack.pop_back();
            } else {
                cout << "Invalid character";
                exit(1);
            }
        }
        if (!stack.empty()) {  // part two of the puzzle
            long long score = 0;
            for (auto it = stack.rbegin(); it != stack.rend(); ++it) {
                score *= 5;
                score += autocomplete_bracket_value[*it];
            }
            stack.clear();
            // cout << "adding score: " << score << endl;
            autocomplete_scores.push_back(score);
        }
    }
    file.close();
    // finding the middle valued autocomplete score
    sort(autocomplete_scores.begin(), autocomplete_scores.end());
    int autocomplete_score = autocomplete_scores.at(autocomplete_scores.size() / 2);
    cout << "Total syntax error score is " << syntax_score << endl;
    cout << "Total autocomplete score is " << autocomplete_score << endl;

    return 0;
}