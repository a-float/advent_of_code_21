HashSet<string> marks = new HashSet<string>();
List<string> folds = new List<string>();
Boolean atFolds = false;
foreach (string line in System.IO.File.ReadLines("./puzzle_data.txt"))
{
    if (line.Length == 0)
    {
        atFolds = true;
    }
    else if (!atFolds)
    {
        marks.Add(line.Trim());
    }
    else
    {
        folds.Add(line.Split(' ')[2]);
    }

}
long[] borders = { 999999, 99999 };
foreach (string fold in folds)
{
    string[] parts = fold.Split('=');
    long crease = long.Parse(parts[1]);
    int foldDir = parts[0] == "x" ? 0 : 1;
    borders[foldDir] = crease;
    List<string> toAdd = new List<string>();
    foreach (string mark in marks)
    {
        string[] coords = mark.Split(',');
        long coord = long.Parse(coords[foldDir]);
        if (coord > crease)
        {
            coords[foldDir] = (2 * crease - coord).ToString();
            toAdd.Add(String.Join(',', coords));
        }
    }
    marks.RemoveWhere(s => long.Parse(s.Split(',')[0]) >= borders[0] || long.Parse(s.Split(',')[1]) > borders[1]);
    marks.UnionWith(toAdd);
    // break;
}
Console.WriteLine("Number of marks is " + marks.Count);
for (int y = 0; y < borders[1]; y++)
{
    for (int x = 0; x < borders[0]; x++)
    {
        Char c = marks.Contains(x + "," + y) ? '#' : '.'; 
        Console.Write(c);
    }
    Console.WriteLine();
}