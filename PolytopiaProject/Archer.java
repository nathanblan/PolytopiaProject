
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
    public Archer(Player p, int x, int y)
    {
        super(p, 10, 2, 1, 2, 1);
        
        canDash = true;
        super.setXY(x, y);
        setX(x*Tile.TILE_SIZE);
        setY(y*Tile.TILE_SIZE);
    }
    
    public Archer(Player p, int turn, int x, int y)
    {
        super(p, 10, 2, 1, 2, 1);
        
        canDash = true;
        
        super.updateLastAttackTurn(turn);
        super.updateLastMoveTurn(turn);
        super.updateLastActionTurn(turn);
        super.setXY(x, y);
        setX(x*Tile.TILE_SIZE);
        setY(y*Tile.TILE_SIZE);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "archer";
    }
    
    public void updateImage()
    {
        int n = getPlayer().getPlayerNum()+1;
        if (super.getShipLevel() > 0)
            super.updateImage();
        else
            super.setImage(new Image("troops\\archer"+n+".png"));
    }
}
