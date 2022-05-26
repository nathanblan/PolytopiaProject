
/**
 * A tile of the board
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;

public class Tile
{
    protected City city = null;
    public static final int TILE_SIZE = 50;
    
    /**
     * Constructor for objects of class Tile
     */
    public Tile()
    {}
    
    public Player getPlayer()
    {
        if (city == null)
            return null;
        return city.getPlayer();
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {}
    
    public City getCity()
    {
        return city;
    }
    
    public void setCity(City c)
    {
        city = c;
    }
    
    public boolean isWater()
    {
        return false;
    }
    
    public String getInfo()
    {
        return "basic";
    }
}
