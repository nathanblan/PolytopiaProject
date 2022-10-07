import java.util.ArrayList;

/**
 * Write a description of class Coords here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Coord
{
    // instance variables - replace the example below with your own
    public final int x;
    public final int y;
    public final int distance;

    /**
     * Constructor for objects of class Coords
     */
    public Coord(int x, int y)
    {
        this.x = x;
        this.y = y;
        distance = 0;
    }
    
    public Coord(int x, int y, Coord last)
    {
        this.x = x;
        this.y = y;
        distance = last.distance + 1;
    }
    
    public void addSurounding(ArrayList<Coord> list)
    {
        ArrayList<Coord> added = getSurounding();
        for (Coord c : added)
        {
            boolean dup = false;
            for (Coord o : list)
            {
                if (o.equals(c))
                    dup = true;
            }
            if (!dup)
                list.add(c);
        }
    }

    public ArrayList<Coord> getSurounding()
    {
        ArrayList<Integer> Xs = new ArrayList<Integer>();
        ArrayList<Integer> Ys = new ArrayList<Integer>();
            
        int size = 16;
        
        Xs.add(x);
        if (x != 0)
            Xs.add(x-1);
        if (x != size-1)
            Xs.add(x+1);
                            
        Ys.add(y);
        if (y != 0)
            Ys.add(y-1);
        if (y != size-1)
            Ys.add(y+1);
        
        ArrayList<Coord> output = new ArrayList<Coord>();
        for (int a : Xs)
        {
            for (int b : Ys)
            {
                if (a != x || b != y)
                    output.add(new Coord(a, b, this));
            }
        }
        
        return output;
    }
    
    public boolean equals(Coord other)
    {
        return other.x == x && other.y == y;
    }
}
