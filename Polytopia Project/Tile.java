
/**
 * A tile of the board
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;

public class Tile
{
    protected City city;
    public static final int TILE_SIZE = 50;
    
    /**
     * Constructor for objects of class Tile
     */
    public Tile()
    {}
    
    public void setOwner (int newOwner)
    {
        if (city != null)
            city.setOwner(newOwner);
    }
    public int getOwner()
    {
        if (city == null)
            return 0;
        return city.getOwner();
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {}
    
    public City getCity()
    {
        return city;
    }
}
