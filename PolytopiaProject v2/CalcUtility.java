import java.util.ArrayList;

/**
 * Holds helper methods
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CalcUtility
{
    private static final int w = 800;
    private static final int h = 800;
    
    public static int sgn(int num)
    {
        if (num > 0)
            return 1;
        if (num < 0)
            return -1;
        return 0;
    }
    
    public static int getDistance (int x1, int y1, int x2, int y2)
    {
        return max(abs(x1-x2), abs(y1-y2));
    }
    
    public static void wait(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private static int abs(int num)
    {
        if (num > 0)
            return num;
        return num*-1;
    }
    
    private static int max (int a, int b)
    {
        if (a > b)
            return a;
        return b;
    }
    
    public static ArrayList<Coord> getAttackableTiles(int x, int y)
    {
        ArrayList<Integer> Xs = new ArrayList<Integer>();
        ArrayList<Integer> Ys = new ArrayList<Integer>();
        
        Troop t = Player.troopMap[x][y];
        int r = t.getRange();
        int size = Player.troopMap.length;
        
        Xs.add(x);
        if (x != 0)
        {
            Xs.add(x-1);
            if (x != 1 && r > 1)
            {
                Xs.add(x-2);
                if (x != 2 && r > 2)
                    Xs.add(x-3);
            }
        }
        if (x != size-1)
        {
            Xs.add(x+1);
            if (x != size-2 && r > 1)
            {
                Xs.add(x+2);
                if (x != size-3 && r > 2)
                    Xs.add(x+3);
            }
        }
                            
        Ys.add(y);
        if (y != 0)
        {
            Ys.add(y-1);
            if (y != 1 && r > 1)
            {
                Ys.add(y-2);
                if (y != 2 && r > 2)
                    Ys.add(y-3);
            }
        }
        if (y != size-1)
        {
            Ys.add(y+1);
            if (y != size-2 && r > 1)
            {
                Ys.add(y+2);
                if (y != size-3 && r > 2)
                    Ys.add(y+3);
            }
        }
        
        Player p = t.getPlayer();
        ArrayList<Coord> output = new ArrayList<Coord>();
        
        for (int a : Xs)
        {
            for (int b : Ys)
            {
                Troop other = Player.troopMap[a][b];
                if ((a != x || b != y) && other != null && other.getPlayer() != p)
                {
                    output.add(new Coord(a, b));
                }
            }
        }
        
        return output;
    }
    
    public static ArrayList<Coord> getMovableTiles(Tile[][] map, int x, int y)
    {
        Troop t = Player.troopMap[x][y];
        int r = t.getMovement();
        Player p = t.getPlayer();
        int size = map.length;
        ArrayList<Coord> check = new Coord(x,y).getSurounding();
        
        ArrayList<Coord> output = new ArrayList<Coord>();
        
        while (check.size() > 0)
        {
            Coord c = check.remove(0);
            
            if (Player.troopMap[c.x][c.y] == null && c.distance <= r)
            {
                boolean movable = false;
                // check terrain
                String type = map[c.x][c.y].getInfo();
                if (type.equals("mountain"))
                {
                    if (p.getTree().getClimbing())
                        movable = true;
                }
                else if (type.equals("water"))
                {
                    if ((((Water)map[c.x][c.y]).hasPort() || t.getShipLevel() > 0) && t.getPlayer().getTree().getSailing())
                        movable = true;
                }
                else if (type.equals("deep water"))
                {
                    if (t.getShipLevel() > 0 && t.getPlayer().getTree().getNavigation())
                        movable = true;
                }
                else
                    movable = true;
                    
                if (movable)
                {
                    // check if dup
                    boolean dup = false;
                    for (Coord o : output)
                    {
                        if (o.equals(c))
                            dup = true;
                    }
                    if (!dup)
                    {
                        output.add(c);
                        c.addSurounding(check);
                    }
                }
            }
        }
        
        return output;
    }
    
    
    /**
     * Returns the index of the button clicked, -1 if not clicking a button
     */
    public static int getButtonIndex(int x, int y)
    {
        if (x < w || getConfirmButton(x, y) == 1)
            return -1;
            
        x -= w+100;
        y -= 80;
        
        int index = y/200;
        
        y %= 200;
        y -= 80;
        
        if (x*x + y*y > 80*80)
            return -1;
        
        return index;
    }
    
    /**
     * Returns -1 if neither confirm button pressed, 
     * 1 if confirm button pressed, 
     * 0 if cancel button pressed
     */
    public static int getConfirmButton(double x, double y)
    {
        if (x < w+20 || x > w+180)
            return -1;
        if (y < h-80)
            return -1;
        
        x -= w+20;
        y -= h-50;
        
        int output = (int)(x/100);
        
        x %= 100;
        x -= 30;
        
        if (x*x + y*y < 30*30)
            return output;
        
        return -1;
    }
    
    public static Tile[][] getTileMap(int size)
    {
        char[][] charMap = MapGenerator.createTerrain(size);
        Tile[][] map = new Tile[size][size];

        for (int r = 0; r < size; r++)
        {
            for (int c = 0; c < size; c++)
            {
                if (charMap[r][c] == 'A')
                    map[r][c] = new Mountain();
                else if (charMap[r][c] == '=')
                    map[r][c] = new DeepWater();
                else if (charMap[r][c] == '~')
                    map[r][c] = new Water();
                else if (charMap[r][c] == '+')
                    map[r][c] = new Forest();
                else if (charMap[r][c] == ',')
                    map[r][c] = new Grass();
                else if (charMap[r][c] == '-')
                    map[r][c] = new Field();
                else if (charMap[r][c] == 'c')
                    map[r][c] = new City(r,c);
            }
        }

        return map;
    }
    
    public static int sgn(double num)
    {
        if (num > 0)
            return 1;
        if (num < 0)
            return -1;
        return 0;
    }
}
