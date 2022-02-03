string[] input = System.IO.File.ReadAllText(@"./data.txt").Split(",");
long[] ages = Array.ConvertAll(input, s => long.Parse(s));
Dictionary<long, ulong> dict = new Dictionary<long, ulong>();
for (long i = 0; i < 9; i++)
{
    dict[i] = 0;
}
foreach (var a in ages)
{
    dict[a] = dict.ContainsKey(a) ? dict[a] + 1 : 1;
}

ulong GetLanternfishCount(Dictionary<long, ulong> dict)
{
    ulong sum = 0;
    foreach (var pair in dict)
    {
        sum += pair.Value;
    }
    return sum;
}

for (int i = 0; i < 256; i++)
{
    ulong tmp = dict[0];
    for (int j = 1; j <= 8; j++)
    {
        dict[j - 1] = dict[j];
    }
    dict[8] = tmp;
    dict[6] += tmp;

    // Console.WriteLine("Round no. {0}", i+1);
    // foreach (var pair in dict)
    // {
    //     Console.WriteLine("Key = {0}, Value = {1}", pair.Key, pair.Value);
    // }
}
Console.WriteLine("Total number of lanterfish: {0}", GetLanternfishCount(dict));
