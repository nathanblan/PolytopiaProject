
/**
 * A water tile. Can fish (30% chance to have fish) and build ports
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Water extends Tile
{
    private boolean hasPort;
    private boolean hasFish;
    
    /**
     * Constructor for objects of class WaterTile
     */
    public Water()
    {
        hasPort = false;
        hasFish = Math.random() < 0.3; // 30% chance of having fish
    }
    
    /**
     * Builds a port on the tile of water
     * @return -1 if unsuccessful, 1 if successful
     */
    public int buildPort()
    {
        if (hasPort)
            return -1;
        hasPort = true;
        hasFish = false;
        
        city.incPopulation(2);
        
        return 1;
    }
    
    /**
     * Fishes in the tile of water
     * @return -1 if unsuccessful, 1 if successful
     */
    public int fish()
    {
        if (!hasFish)
            return -1;
        hasFish = false;
        
        city.incPopulation(1);
        
        return 1;
    }
}
