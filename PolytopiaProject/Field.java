import javafx.scene.image.Image;

/**
 * A basic land tile.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

public class Field extends Tile
{
    private boolean hasFruit;
    
    /**
     * Constructor for objects of class Land
     */
    public Field()
    {
        hasFruit = Math.random() < 0.4;
    }
    
    public Field(boolean fruit)
    {
        hasFruit = fruit;
    }
    
    public int harvestFruit()
    {
        if (!canHarvestFruit() || city == null)
        {
            return -1;
        }
        hasFruit = false;
        
        city.incPopulation(1);
        getPlayer().decStars(2);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        if (hasFruit)
            gc.drawImage(new Image("images\\berries.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else
            gc.drawImage(new Image("images\\land.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
    
    public String getInfo()
    {
        return "field";
    }
    
    public boolean canHarvestFruit()
    {
        return hasFruit && getPlayer().getStars() >= 2;
    }
    
    public boolean canAfford(int x)
    {
        return getPlayer().getStars() >= 2;
    }
}
