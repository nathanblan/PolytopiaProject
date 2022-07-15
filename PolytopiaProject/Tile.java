
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.*;

/**
 * A tile of the board
 *
 * @author (your name)
 * @version (a version number or a date)
 */
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
    
    public void drawTile(GraphicsContext gc, int x, int y, int scale)
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
    
    public boolean canAfford()
    {
        return false;
    }
    
    public boolean canAfford(int select)
    {
        return false;
    }
    
    public static Tile loadTile(String save)
    {
        char type = save.charAt(0);
        
        if (type == 't')
            return Forest.loadTile(save.substring(1));
        if (type == 'w')
            return Water.loadTile(save.substring(1));
        if (type == 'm')
            return Mountain.loadTile(save.substring(1));
        if (type == 'd')
            return DeepWater.loadTile(save.substring(1));
        if (type == 'f')
            return Field.loadTile(save.substring(1));
        if (type == 'g')
            return Grass.loadTile(save.substring(1));
        if (type == 'c' || type == 'v')
            return City.loadTile(save);
        
        return null;
    }
}
