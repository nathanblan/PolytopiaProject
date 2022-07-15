import javafx.scene.image.Image;

/**
 * A tile of deep water.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class DeepWater extends Tile
{
    /**
     * Constructor for objects of class DeepWater
     */
    public DeepWater()
    {}
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        drawTile(gc, x, y, TILE_SIZE);
    }
    
    public void drawTile(GraphicsContext gc, int x, int y, int scale)
    {
        gc.drawImage(new Image("images\\deep water.jpg"), x*scale, y*scale, scale, scale);
    }
    
    public String getInfo()
    {
        return "deep water";
    }
    
    public boolean isWater()
    {
        return true;
    }
    
    public String toString()
    {
        return "d";
    }
    
    public static DeepWater loadTile(String save)
    {
        return new DeepWater();
    }
}
