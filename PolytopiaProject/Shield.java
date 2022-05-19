
/**
 * Write a description of class Shield here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Shield extends Troop
{
    /**
     * Constructor for objects of class Shield
     */
    public Shield(Player p)
    {
        super(p, 10, 1, 3);
        movement = 1;
        range = 1;
    }
    
    public String getInfo()
    {
        return "shield";
    }
    
    public void drawTroop(GraphicsContext gc, int x, int y)
    {
        gc.drawImage(new Image("troops\\shield.png"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
}
