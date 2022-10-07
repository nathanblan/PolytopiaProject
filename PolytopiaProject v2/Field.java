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
        drawTile(gc, x, y, TILE_SIZE);
    }
    
    public void drawTile(GraphicsContext gc, int x, int y, int scale)
    {
        if (hasFruit)
            gc.drawImage(new Image("images\\berries.jpg"), x*scale, y*scale, scale, scale);
        else
            gc.drawImage(new Image("images\\land.jpg"), x*scale, y*scale, scale, scale);
    }
    
    public String getInfo()
    {
        return "field";
    }
    
    public boolean canHarvestFruit()
    {
        return hasFruit;
    }
    
    public String toString()
    {
        String output = "f";
        if (hasFruit)
            output += "1";
        else
            output += "0";
            
        return output;
    }
    
    public static Field loadTile(String save)
    {   
        return new Field(save.charAt(0)=='1');
    }
}
