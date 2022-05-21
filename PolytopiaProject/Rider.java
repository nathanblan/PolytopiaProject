
/**
 * Write a description of class Rider here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Rider extends Troop
{
    /**
     * Constructor for objects of class Rider
     */
    public Rider(Player p)
    {
        super(p, 10, 2, 1);
        movement = 2;
        range = 1;
        
        canDash = true;
    }
    
    public String getInfo()
    {
        return "rider";
    }
    
    public void drawTroop(GraphicsContext gc, int x, int y)
    {
        gc.drawImage(new Image("troops\\rider.png"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
}
