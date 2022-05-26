
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
    public Rider(Player p, int x, int y)
    {
        super(p, 10, 2, 1, 1, 2);
        
        canDash = true;
        super.setXY(x, y);
    }
    
    public Rider(Player p, int turn, int x, int y)
    {
        super(p, 10, 2, 1, 1, 2);
        
        canDash = true;
        
        super.updateLastAttackTurn(turn);
        super.updateLastMoveTurn(turn);
        super.updateLastActionTurn(turn);
        
        super.setXY(x, y);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "rider";
    }
    
    public void updateImage()
    {
        int n = getPlayer().getPlayerNum()+1;
        if (super.getShipLevel() > 0)
            super.updateImage();
        else
            super.setImage(new Image("troops\\rider"+n+".png"));
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
