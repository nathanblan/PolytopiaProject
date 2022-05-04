
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
        if (isFarm)
            return -1;
        isFarm = true;
        
        city.incPopulation(2);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
