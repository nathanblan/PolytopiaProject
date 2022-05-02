
/**
 * Write a description of class WaterTile here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class WaterTile extends Tile
{
    private boolean hasPort;
    private boolean hasFish;
    
    /**
     * Constructor for objects of class WaterTile
     */
    public WaterTile()
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
        return 1;
    }
}
