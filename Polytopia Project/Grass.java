
/**
 * A grass tile. Can build farms on this tile.
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
        
        city.incPopulation(2);
        
        return 1;
    }
}
