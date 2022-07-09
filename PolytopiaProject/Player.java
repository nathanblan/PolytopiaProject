import java.util.ArrayList;

/**
 * Store all player information in objects from this class.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    public static Troop[][] troopMap = new Troop[16][16];
    public boolean[][] fogMap = new boolean[16][16];
    private ArrayList<City> cities;
    private TechTree tree;
    private int playerNum;
    private int stars;
    
    public Player(int playerNum)
    {
        this.playerNum = playerNum;
        cities = new ArrayList<City>();
        tree = new TechTree();
        stars = 5;
    }
    
    public Player(int num, TechTree techTree, int numStars)
    {
        playerNum = num;
        tree = techTree;
        stars = numStars;
        cities = new ArrayList<City>();
    }
    
    public void incStars(int num)
    {
        stars += num;
    }
    
    public int getPlayerNum()
    {
        return playerNum;
    }
    
    public void startTurn()
    {
        stars += getWorth();
    }
    
    public int getWorth()
    {
        int worth = 1;
        for (City c : cities)
        {
            worth += c.getLevel();
        }
        return worth;
    }
    
    public int getStars()
    {
        return stars;
    }
    
    public void decStars(int num)
    {
        stars -= num;
    }
    
    public TechTree getTree()
    {
        return tree;
    }
    
    public void addCity(City c)
    {
        for (City o : cities)
        {
            if (o == c)
                return;
        }
        
        cities.add(c);
    }
    
    public void removeCity(City c)
    {
        for (int i = 0; i < cities.size(); i++)
        {
            if (cities.get(i) == c)
            {
                cities.remove(i);
                return;
            }
        }
    }
}
