const fs = require('fs')
const lines = fs.readFileSync('data.txt', 'utf-8').trim().split('\n')

class BT {
    constructor(obj, parent = null) {
        this.parent = parent
        this.isPair = true
        if (Number.isInteger(obj)) {
            this.value = obj
            this.isPair = false
        } else {
            this.left = new BT(obj[0], this)
            this.right = new BT(obj[1], this)
        }
    }

    next(dir) {
        if (dir != 'left' && dir != 'right') {
            console.log("Invalid direction");
            return;
        }
        let otherDir = (dir == 'left') ? 'right' : 'left'
        let n = this
        while (n.parent[dir] == n) {
            n = n.parent
            if (n.parent == null) {
                return null
            }
        }
        n = n.parent[dir]
        while (n.isPair) {
            n = n[otherDir]
        }
        return n
    }

    explode(depth = 1) {
        if (!this.isPair) return false
        if (depth == 5 && !this.left.isPair && !this.right.isPair) {
            // console.log("Explode " + this.toString())
            const [left, right] = [this.next('left'), this.next('right')]
            if (left) left.value += this.left.value
            if (right) right.value += this.right.value
            this.value = 0
            this.isPair = false
            return true
        }
        if (this.left.explode(depth + 1) || this.right.explode(depth + 1)) return true
        return false;
    }

    split() {
        if (!this.isPair) {
            if (this.value >= 10) {
                // console.log("Split " + this.value);
                this.isPair = true
                this.left = new BT(Math.floor(this.value / 2), this)
                this.right = new BT(Math.ceil(this.value / 2), this)
                return true
            }
            return false
        }
        if (this.left.split() || this.right.split()) return true
        return false;
    }

    reduce() {
        while (true) {
            // console.log(this.toString());
            if (this.explode()) continue;
            if (this.split()) continue;
            break;
        }
    }

    static add(a, b) {
        let bt = new BT(JSON.parse('[' + a + ',' + b + ']'))
        bt.reduce()
        return bt
    }

    toString() {
        if (!this.isPair) return this.value.toString();
        else return `[${this.left?.toString()},${this.right?.toString()}]`
    }

    magnitude() {
        if (this.isPair) {
            return 3 * this.left.magnitude() + 2 * this.right.magnitude()
        } else return this.value
    }
}

let res = new BT(JSON.parse(lines[0]))
for (let i = 1; i < lines.length; i++) {
    // console.log("Add " + res.toString() + " + " + lines[i]);
    res = BT.add(res.toString(), lines[i])
}
console.log("The result magnitude is " + res.magnitude());

let maxMagnitude = 0
for (let i = 0; i < lines.length; i++) {
    for (let j = 0; j < lines.length; j++) {
        if (i == j) continue
        maxMagnitude = Math.max(maxMagnitude, BT.add(lines[i], lines[j]).magnitude())
    }
}
console.log("Max possible sum magnitude is " + maxMagnitude);