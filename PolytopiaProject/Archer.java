
/**
 * Write a description of class Archer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Archer extends Troop
{
    /**
     * Constructor for objects of class Archer
     */
    public Archer(Player p)
    {
        super(p, 10, 2, 1);
        movement = 1;
        range = 2;
        
        canDash = true;
    }
    
    public String getInfo()
    {
        return "archer";
    }
    
    public void drawTroop(GraphicsContext gc, int x, int y, int playerNum)
    {
        if (shipLevel > 0)
            super.drawTroop(gc, x, y, playerNum);
        else
            gc.drawImage(new Image("troops\\archer"+playerNum+".png"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
}
