import java.util.ArrayList;

/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    private static Troop[][] troopMap = new Troop[16][16];
    private ArrayList<City> cities;
    private TechTree tree;
    private int stars;
    
    public Player()
    {
        cities = new ArrayList<City>();
        tree = new TechTree();
        stars = 5;
    }
    
    public void incStars(int num)
    {
        stars += num;
    }
    
    public void startTurn()
    {
        for (City c : cities)
        {
            stars += c.getLevel();
        }
    }
    
    public int getStars()
    {
        return stars;
    }
    
    public void decStars(int num)
    {
        stars -= num;
    }
}
