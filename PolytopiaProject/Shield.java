
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
    public Shield(Player p, int x, int y)
    {
        super(p, 10, 1, 3, 1, 1);
        super.setXY(x, y);
        setX(x*Tile.TILE_SIZE);
        setY(y*Tile.TILE_SIZE);
    }
    
    public Shield(Player p, int turn, int x, int y)
    {
        super(p, 10, 1, 3, 1, 1);
        
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
        return "shield";
    }
    
    public void updateImage()
    {
        int n = getPlayer().getPlayerNum()+1;
        if (super.getShipLevel() > 0)
            super.updateImage();
        else
            super.setImage(new Image("troops\\shield"+n+".png"));
    }
}
