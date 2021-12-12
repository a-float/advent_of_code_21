fs = require('fs')

const run = (cb) => {
    fs.readFile('puzzle_data.txt', 'ascii', (err, data) => {
        if (err) return console.error(err)
        else cb(data)
    })
}

const countObviousDigits = (data) => {
    let outputs = data.split('\r\n').map(l => l.split('|')[1].trim().split(' ')).flat(1)
    let count = outputs.filter(o => [2, 4, 3, 7].includes(o.length)).length
    console.log(count)
}

String.prototype.intersection = function (other) {
    return this.split('').filter(letter => other.includes(letter)).join('')
}

String.prototype.sort = function () {
    return this.split('').sort().join('')
}

const decipherWiring = (input) => {
    const nums = new Array(10)
    nums[8] = 'abcdefg'
    nums[1] = input.find(s => s.length == 2).sort()
    nums[7] = input.find(s => s.length == 3).sort()
    nums[4] = input.find(s => s.length == 4).sort()

    segBD = nums[4].split('').filter(s => !nums[1].includes(s)).join('')

    nums[0] = input.find(s =>
        s.length == 6 &&
        s.intersection(nums[7]).length == 3 &&
        s.intersection(segBD).length == 1
    ).sort()

    nums[3] = input.find(s =>
        s.length == 5 &&
        s.intersection(nums[1]).length == 2
    ).sort()

    nums[6] = input.find(s =>
        s.length == 6 &&
        s.intersection(nums[1]).length == 1
    ).sort()

    nums[5] = input.find(s =>
        s.length == 5 &&
        s.intersection(nums[6]).length == 5
    ).sort()

    nums[2] = input.find(s =>
        s.length == 5 &&
        s.sort() != nums[5] &&
        s.sort() != nums[3]
    ).sort()

    nums[9] = input.find(s =>
        s.length == 6 &&
        s.intersection(nums[0]).length == 5 &&
        s.intersection(nums[1]).length == 2
    ).sort()
    return nums
}

const decode = (string, nums) => {
    if (string.length == 2) return '1'
    else if (string.length == 3) return '7'
    else if (string.length == 4) return '4'
    else if (string.length == 7) return '8'
    else {
        for (let val of [0, 2, 3, 5, 6, 9]){
            if (string.sort() == nums[val]) return val.toString()
        }
    }
    throw new Error("Invalid input string. Could not find a match.")
}

const decipher = (data) => {
    const segments = data.split('\r\n').map(line => line.split('|').map(half => half.trim().split(' ')))
    let sum = 0
    for (const segment of segments) {
        const [input, output] = segment
        const nums = decipherWiring(input)
        const res = parseInt(output.map(o => decode(o, nums)).join(''))
        sum += res
    }
    console.log(sum);
}


// run(countObviousDigits) // puzzle 1

run(decipher) // puzzle 2