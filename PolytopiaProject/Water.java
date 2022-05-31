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
        if (hasPort)
            gc.drawImage(new Image("images\\port.png"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else if (hasFish)
            gc.drawImage(new Image("images\\water_with_fish.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else
            gc.drawImage(new Image("images\\shallow water.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
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
}
