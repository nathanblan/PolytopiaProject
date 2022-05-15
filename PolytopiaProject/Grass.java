import javafx.scene.image.Image;

/**
 * A grass tile. Can build farms on this tile.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

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
        if (!canBuildFarm() || city == null)
            return -1;
        isFarm = true;
        
        city.incPopulation(2);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        gc.drawImage(new Image("images\\grass.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
    
    public String getInfo()
    {
        return "grass";
    }
    
    public boolean canBuildFarm()
    {
        return !isFarm;
    }
}
