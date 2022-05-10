import javafx.scene.image.Image;

/**
 * Write a description of class Mountain here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Mountain extends Tile
{
    private boolean hasGold;
    private boolean hasMine;

    /**
     * Constructor for objects of class Mountain
     */
    public Mountain()
    {
        hasMine = false;
        hasGold = Math.random() < 0.3; // 30% chance
    }
    
    public int buildMine()
    {
        if (!hasGold || hasMine)
            return -1;
        hasMine = true;
        
        city.incPopulation(2);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        if (hasMine)
            hasMine = hasMine; // placeholder
        else if (hasGold)
            gc.drawImage(new Image("images\\mountain_with_gold.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else
            gc.drawImage(new Image("images\\mountain.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
