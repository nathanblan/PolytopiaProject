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
    public static boolean[][] fogMap = new boolean[16][16];
    private ArrayList<City> cities;
    private TechTree tree;
    private int playerNum;
    private int stars;
    private int turn = 0;
    
    public Player(int playerNum)
    {
        this.playerNum = playerNum;
        cities = new ArrayList<City>();
        tree = new TechTree();
        stars = 5;
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
        for (City c : cities)
        {
            stars += c.getLevel();
        }
    }
    
    public void endTurn()
    {
        turn++;
    }
    
    public int  getTurn()
    {
        return turn;
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
}
