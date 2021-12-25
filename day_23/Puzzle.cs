using System.Text;

/* 
 I am, not proud of the code I've written below.
 It's a mess of ifs, fors, global variables and spontanous function returns.
 The only good thing about it is that it works well enough.

 Using recursion I simulate all possible pods movements leading to them ending 
 up in the correct rooms, or blocking themselves along the way.
 Rules of amphipod movement does not allow for the burrow to return to a state it has been in
 before.

 In each move:
    - pod in the corridor can try to go to its room if the path is not blocked and the room does
    not contain a pod of different type

    - pod in a room can stays in it if it's the correct one and it no pod below it gets blocked,
    otherwise pod leaves the room and picks an available spot in the corridor

    - pod in a room with no way out of it stays inside

State consists of:
    - 11 (corridorLen) characters symbolizing the corridor
    - maxDepth * 4 characters for the contents of each room

State for the example burrow
#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########

is "...........BCBDADCA" (11 dots for the empty corridor, then room contents row by row)
*/

// a lot of global variables
const int corridorLen = 11;
int[] entrances = { 2, 4, 6, 8 };
// size of the rooms
int maxDepth;
// used for abandoning paths that can't yield better results
// the time difference is very slight, but not nonexistent
long bestSoFar = long.MaxValue;
Dictionary<string, long> cache = new Dictionary<string, long>();

int IntPow(int x, int pow)
{
    int ret = 1;
    while (pow != 0)
    {
        if ((pow & 1) == 1)
            ret *= x;
        x *= x;
        pow >>= 1;
    }
    return ret;
}

// mainly for debug
string StateToString(StringBuilder state)
{
    string res = state.ToString().Substring(0, corridorLen) + "\n";
    for (int i = 0; i < maxDepth; i++)
    {
        int idx = corridorLen + 4 * i;
        res += "  " + state[idx] + "|" + state[idx + 1] + "|" + state[idx + 2] + "|" + state[idx + 3] + "\n";
    }
    return res;
}

// returns a list of tuples (from idx, move cost) for the i'ts spot in the burrow
// For each move (v1, v2) it reads as: Pod at pos i can move to pos v1 with a cost of v2.
List<Tuple<int, int>> getMoves(string state, int idx)
{
    List<Tuple<int, int>> res = new List<Tuple<int, int>>();
    if (state[idx] == '.') return res;
    int type = state[idx] - 'A';
    if (idx >= corridorLen) // letter is in a room
    {
        int posDiff = idx - corridorLen;
        int room = posDiff % 4;
        bool needsToGetOut = false;
        int depth = posDiff / 4;
        if (depth > 0)
        {
            for (int d = depth - 1; d >= 0; d--)
            {
                if (state[corridorLen + d * 4 + room] != '.')
                {
                    return res;
                } // cant get out, is blocked
            }
        }
        if (type == room) // correct room, check if all below are same type
        {
            for (int d = depth + 1; d < maxDepth; d++)
            {
                if (state[corridorLen + d * 4 + room] != state[idx]) // wrong pod below will need to get out
                {
                    needsToGetOut = true;
                    break;
                }
            }
            if (!needsToGetOut) return res; // empty list, already waiting on its place in its room
        }
        else
        {
            needsToGetOut = true;
        }

        if (needsToGetOut)
        {  // needs to get out of its room
            for (int i = entrances[room]; i >= 0; i--)  // try going left
            {
                if (state[i] == '.' && !entrances.Contains(i))
                {
                    res.Add(Tuple.Create(i, (1 + depth + entrances[room] - i) * IntPow(10, type)));
                }
                else if (state[i] != '.')
                {   // the path is blocked, can't go left any further
                    break;
                }
            }
            for (int i = entrances[room]; i < corridorLen; i++)  // try going left
            {
                if (state[i] == '.' && !entrances.Contains(i))
                {
                    res.Add(Tuple.Create(i, (1 + depth + i - entrances[room]) * IntPow(10, type)));
                }
                else if (state[i] != '.')
                {   // the path is blocked, can't go left any further
                    break;
                }
            }
        }
        return res;
    }
    else // pod is in the corridor
    {
        for (int d = maxDepth - 1; d >= 0; d--)
        {
            int roomSpotIdx = corridorLen + d * 4 + type;
            // no point in going back to its room and blocking a different pod underneath
            if (state[roomSpotIdx] != state[idx] && state[roomSpotIdx] != '.') return res;
            if (state[roomSpotIdx] == '.') // available space in the room, try to get there
            {
                for (int i = Math.Min(idx, entrances[type]); i <= Math.Max(idx, entrances[type]); i++)
                {
                    if (i == idx) continue;
                    if (state[i] != '.') // the corridor path is blocked
                    {
                        return res;
                    }
                }
                for (int i = 0; i < d; i++)
                {
                    if (state[corridorLen + d * 4 + type] != '.') // path to room spot is blocked
                    {
                        return res;
                    }
                }
                res.Add(Tuple.Create(roomSpotIdx, (Math.Abs(idx - entrances[type]) + d + 1) * IntPow(10, type)));
                break;
            }
        }
    }
    return res;
}
long recu(StringBuilder state, long costSoFar, string targetState)
{
    if (costSoFar >= bestSoFar)
    {
        return long.MaxValue; // no point in going deeper
    }
    // Console.WriteLine(String.Format("State:\n{0}costSoFar: {1}", StateToString(state), costSoFar));
    if (state.ToString().Equals(targetState))
    {
        Console.WriteLine("Solution with " + costSoFar.ToString() + " energy cost");
        bestSoFar = Math.Min(bestSoFar, costSoFar);
        return costSoFar;
    }
    if (cache.ContainsKey(state.ToString() + costSoFar.ToString()))
    {
        return cache[state.ToString() + costSoFar.ToString()];
    }
    // pretty much reduntant after adding the global variable bestSoFar
    long minCost = long.MaxValue;
    for (int i = 0; i < state.Length; i++)
    {
        foreach (var move in getMoves(state.ToString(), i))
        {
            // Console.WriteLine(String.Format("Can move {0} from {1} to {2} with a cost {3}", state[i], i, move.Item1, move.Item2));
            state[move.Item1] = state[i];
            state[i] = '.';
            long res = recu(state, costSoFar + move.Item2, targetState);
            minCost = Math.Min(res, minCost);
            state[i] = state[move.Item1];
            state[move.Item1] = '.';
        }
    }
    cache.Add(state.ToString() + costSoFar.ToString(), minCost);
    return minCost;
}

String input = File.ReadAllText("data.txt");
List<Char> startPods = input.ToList().FindAll(c => c >= 'A' && c <= 'D');
maxDepth = startPods.Count() / 4;
StringBuilder state = new StringBuilder(new string('.', 11) + String.Join("", startPods));
string targetState = "..........." + String.Concat(Enumerable.Repeat("ABCD", maxDepth));
Console.WriteLine("Starting state:\n" + StateToString(state));
Console.WriteLine("Target state: \n" + StateToString(new StringBuilder(targetState)));

long minCost = recu(state, 0, targetState);

Console.WriteLine("Minimum energy cost is " + minCost.ToString());