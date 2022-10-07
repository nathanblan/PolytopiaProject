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
    
    public Mountain(boolean gold)
    {
        hasMine = false;
        hasGold = gold;
    }
    
    public Mountain(boolean gold, boolean mine)
    {
        hasGold = gold;
        hasMine = mine;
    }
    
    public int buildMine()
    {
        if (!canBuildMine() || city == null)
        {
            return -1;
        }
        hasMine = true; 
        
        city.incPopulation(2);
        getPlayer().decStars(5);
        
        return 1;
    }
    
    public int grabGold()
    {
        if (!canGrabGold() || city == null)
            return -1;
        
        hasGold = false;
        getPlayer().incStars(5);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        drawTile(gc, x, y, TILE_SIZE);
    }
    
    public void drawTile(GraphicsContext gc, int x, int y, int scale)
    {
        if (hasMine)
            gc.drawImage(new Image("images\\mine.jpg"), x*scale, y*scale, scale, scale);
        else if (hasGold)
            gc.drawImage(new Image("images\\mountain_with_gold.jpg"), x*scale, y*scale, scale, scale);
        else
            gc.drawImage(new Image("images\\mountain.jpg"), x*scale, y*scale, scale, scale);
    }
    
    public String getInfo()
    {
        return "mountain";
    }
    
    public boolean canBuildMine()
    {
        return !hasMine;
    }
    
    public boolean canGrabGold()
    {
        return hasGold;
    }
    
    public String toString()
    {
        String output = "m";
        if (hasGold)
            output += "1";
        else
            output += "0";
        if (hasMine)
            output += "1";
        else
            output += "0";
            
        return output;
    }
    
    public static Mountain loadTile(String save)
    {   
        return new Mountain(save.charAt(0)=='1',save.charAt(1)=='1');
    }
}
