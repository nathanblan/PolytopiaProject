import javafx.scene.image.Image;

/**
 * A basic land tile.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Land extends Tile
{
    private boolean hasFruit;
    
    /**
     * Constructor for objects of class Land
     */
    public Land()
    {
        hasFruit = Math.random() < 0.2;
    }
    
    public int harvestFruit()
    {
        if (!hasFruit)
        {
            return -1;
        }
        hasFruit = false;
        
        city.incPopulation(1);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        gc.drawImage(new Image("images\\land.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
