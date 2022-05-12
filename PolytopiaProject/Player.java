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
    
    public Player()
    {
        cities = new ArrayList<City>();
        tree = new TechTree();
    }
}
