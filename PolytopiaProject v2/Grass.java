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
    
    public Grass(boolean farm)
    {
        isFarm = farm;
    }
    
    public int buildFarm()
    {
        if (!canBuildFarm() || city == null)
            return -1;
        isFarm = true;
        
        city.incPopulation(2);
        getPlayer().decStars(5);
        
        return 1;
    }
    
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        drawTile(gc, x, y, TILE_SIZE);
    }
    
    public void drawTile(GraphicsContext gc, int x, int y, int scale)
    {
        if (isFarm)
            gc.drawImage(new Image("images\\farm.png"), x*scale, y*scale, scale, scale);
        else  
            gc.drawImage(new Image("images\\grass.jpg"), x*scale, y*scale, scale, scale);
    }
    
    public String getInfo()
    {
        return "grass";
    }
    
    public boolean canBuildFarm()
    {
        return !isFarm;
    }
    
    public String toString()
    {
        String output = "g";
        if (isFarm)
            output += "1";
        else
            output += "0";
            
        return output;
    }
    
    public static Grass loadTile(String save)
    {   
        return new Grass(save.charAt(0)=='1');
    }
}
