
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
    /**
     * Constructor for objects of class Land
     */
    public Land()
    {
    }
    
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        gc.setFill(Color.PALEGREEN);
        gc.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
