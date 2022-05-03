
/**
 * Write a description of class Mountain here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Mountain extends Tile
{
    private boolean hasGold;
    private boolean hasMine;

    /**
     * Constructor for objects of class Mountain
     */
    public Mountain()
    {
        hasMine = false;
        hasGold = Math.random() < 0.3; // 30% chance
    }
    
    public int buildMine()
    {
        if (!hasGold || hasMine)
            return -1;
        hasMine = true;
        
        city.incPopulation(2);
        
        return 1;
    }
}
