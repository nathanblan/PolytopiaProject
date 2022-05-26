
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
        super(p, 10, 2, 1, 1, 2);
        
        canDash = true;
    }
    
    public Rider(Player p, int turn)
    {
        super(p, 10, 2, 1, 1, 2);
        
        canDash = true;
        
        super.updateLastAttackTurn(turn);
        super.updateLastMoveTurn(turn);
        super.updateLastActionTurn(turn);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "rider";
    }
    
    public void drawTroop(GraphicsContext gc, int x, int y)
    {
        int playerNum = getPlayer().getPlayerNum()+1;
        if (shipLevel > 0)
            super.drawTroop(gc, x, y);
        else
            gc.drawImage(new Image("troops\\rider"+playerNum+".png"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
}
