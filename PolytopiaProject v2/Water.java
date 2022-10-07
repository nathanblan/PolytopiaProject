/**
 * A water tile. Can fish (30% chance to have fish) and build ports
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Water extends Tile
{
    private boolean hasPort;
    private boolean hasFish;
    
    /**
     * Constructor for objects of class WaterTile
     */
    public Water()
    {
        hasPort = false;
        hasFish = Math.random() < 0.2; // 20% chance of having fish
    }
    
    public Water(boolean fish)
    {
        hasPort = false;
        hasFish = fish;
    }
    
    public Water(boolean fish, boolean port)
    {
        hasFish = fish;
        hasPort = port;
    }
    
    /**
     * Builds a port on the tile of water
     * @return -1 if unsuccessful, 1 if successful
     */
    public int buildPort()
    {
        if (!canBuildPort() || city == null)
            return -1;
        hasPort = true;
        hasFish = false;
        
        city.incPopulation(2);
        getPlayer().decStars(5);
        
        return 1;
    }
    
    /**
     * Fishes in the tile of water
     * @return -1 if unsuccessful, 1 if successful
     */
    public int fish()
    {
        if (!canFish() || city == null)
            return -1;
        hasFish = false;
        
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
        if (hasPort)
            gc.drawImage(new Image("images\\port.png"), x*scale, y*scale, scale, scale);
        else if (hasFish)
            gc.drawImage(new Image("images\\water_with_fish.jpg"), x*scale, y*scale, scale, scale);
        else
            gc.drawImage(new Image("images\\shallow water.jpg"), x*scale, y*scale, scale, scale);
    }
    
    public String getInfo()
    {
        return "water";
    }
    
    public boolean canFish()
    {
        return hasFish;
    }
    
    public boolean canBuildPort()
    {
        return !hasPort;
    }
    
    public boolean hasPort()
    {
        return hasPort;
    }
    
    public boolean isWater()
    {
        return true;
    }
    
    public String toString()
    {
        String output = "w";
        if (hasFish)
            output += "1";
        else
            output += "0";
        if (hasPort)
            output += "1";
        else
            output += "0";
            
        return output;
    }
    
    public static Water loadTile(String save)
    {   
        return new Water(save.charAt(0)=='1',save.charAt(1)=='1');
    }
}
