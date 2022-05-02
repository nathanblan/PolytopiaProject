
/**
 * Write a description of class Grass here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Grass extends Tile
{
    private boolean isFarm;
    
    /**
     * Constructor for objects of class Grass
     */
    public Grass()
    {
        isFarm = false;
    }
    
    public int buildFarm()
    {
        if (isFarm)
            return -1;
        isFarm = true;
        return 1;
    }
}
