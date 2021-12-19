"use strict";
let MULT = 5;
const fs = require("fs");

class MinHeap {
    constructor(comparator) {
        this.arr = [-1] // dummy element for easier indexing
        this.comparator = comparator
    }
    swap(i, j) {
        [this.arr[i], this.arr[j]] = [this.arr[j], this.arr[i]]
    }
    isEmpty() {
        return this.length == 1
    }
    insert(x) {
        this.arr.push(x)
        let curr = this.arr.length - 1
        while (curr != 1 && this.comparator(x, this.arr[Math.floor(curr / 2)]) > 0) {
            this.swap(curr, Math.floor(curr / 2))
            curr = Math.floor(curr / 2)
        }
    }
    extract() {
        if (this.arr.length == 1) {
            return null
        }
        const x = this.arr[1]
        this.arr[1] = this.arr.pop()
        let curr = 1
        let smallChild = null
        while (true) {
            if (this.arr.length > curr * 2 + 1) {
                smallChild = this.comparator(this.arr[2 * curr], this.arr[2 * curr + 1]) > 0 ? 2 * curr : 2 * curr + 1
            } else if (this.arr.length > curr * 2) {
                smallChild = curr * 2
            } else break;
            if (this.comparator(this.arr[smallChild], this.arr[curr]) > 0) {
                this.swap(curr, smallChild)
                curr = smallChild
            } else {
                break
            }
        }
        return x
    }
}

const file = fs.readFileSync("./puzzle_data.txt", "utf-8");
const lines = file.trim().split("\r\n").map(line => line.split("").map(char => parseInt(char)))
const [width, height] = [lines[0].length, lines.length]
let dist, visited, prev

const getValFrom = (x, y, arr) => {
    if (arr != lines) {
        return arr[y][x];
    }
    let xDiff = Math.floor(x / width);
    let yDiff = Math.floor(y / height);
    return ((arr[y % height][x % width] + xDiff + yDiff) - 1) % 9 + 1;
}
const setValTo = (x, y, arr, val) => { arr[y][x] = val };

const getNeighbours = (x, y) => {
    return [[-1, 0], [1, 0], [0, 1], [0, -1]]
        .map(d => [d[0] + x, d[1] + y])
        .filter(p => p[0] >= 0 && p[0] < width * MULT && p[1] >= 0 && p[1] < height * MULT)
}


const findBestPath = (mult) => {
    MULT = mult // bad global variable :c
    dist = Array.from({ length: height * MULT }, () => Array.from({ length: width * MULT }, () => Infinity))
    visited = Array.from({ length: height * MULT }, () => Array.from({ length: width * MULT }, () => false))
    prev = Array.from({ length: height * MULT }, () => Array.from({ length: width * MULT }, () => undefined))
    const heap = new MinHeap((a, b) => getValFrom(...b, dist) - getValFrom(...a, dist))

    setValTo(0, 0, dist, 0)
    heap.insert([0, 0])
    while (!heap.isEmpty()) {
        let node = heap.extract()
        if (getValFrom(...node, visited)) continue;

        for (const neigh of getNeighbours(...node)) {
            if (getValFrom(...neigh, visited)) continue;
            const alt = getValFrom(...node, dist) + getValFrom(...neigh, lines)
            if (getValFrom(...neigh, dist) > alt) {
                setValTo(...neigh, dist, alt)
                setValTo(...neigh, prev, node)
            }
            heap.insert(neigh)
        }
        setValTo(...node, visited, true)
        if (node[0] == width * MULT - 1 && node[1] == height * MULT - 1) {
            break;
        }
    }
    let totalDist = getValFrom(...[width * MULT - 1, height * MULT - 1], dist)
    console.log("Total distance for MULT=" + MULT + " is " + totalDist);
}

findBestPath(1)
findBestPath(5)