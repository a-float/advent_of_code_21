"use strict"
const fs = require("fs")
const file = fs.readFileSync("./puzzle_data.txt", "utf-8");
const lines = file.trim().split("\r\n").map(line => line.split("").map(char => parseInt(char)))
const [width, height] = [lines[0].length, lines.length]
const dist = Array.from({ length: height }, () => Array.from({ length: width }, () => Infinity))
const visited = Array.from({ length: height }, () => Array.from({ length: width }, () => false))
const prev = Array.from({ length: height }, () => Array.from({ length: width }, () => undefined))

const getValFrom = (x, y, arr) => arr[y][x];
const setValTo = (x, y, arr, val) => { arr[y][x] = val };

const get_neighbours = (x, y) => {
    const res = []
    if (x > 0) res.push([x - 1, y]);
    if (x < width - 1) res.push([x + 1, y]);
    if (y > 0) res.push([x, y - 1]);
    if (y < height - 1) res.push([x, y + 1]);
    return res;
}

const getMin = (q) => {
    q.sort((a, b) => getValFrom(...a, dist) - getValFrom(...b, dist))   // slooow
    return q.shift()
}

setValTo(0, 0, dist, 0)

const queue = []
queue.push([0, 0])
while (queue.length > 0) {
    let node = getMin(queue)
    if (getValFrom(...node, visited)) continue;

    for (const neigh of get_neighbours(...node)) {
        if (getValFrom(...neigh, visited)) continue;
        const alt = getValFrom(...node, dist) + getValFrom(...neigh, lines)
        if (getValFrom(...neigh, dist) > alt) {
            setValTo(...neigh, dist, alt)
            setValTo(...neigh, prev, node)
        }
        queue.push(neigh)
    }
    setValTo(...node, visited, true)
    if (node[0] == 99 && node[1] == 99) {
    }
}

let totalDist = getValFrom(...[width-1, height-1], dist)
console.log(totalDist);


