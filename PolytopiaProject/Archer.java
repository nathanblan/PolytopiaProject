
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
        p.decStars(3);
    }
    
    public String getType()
    {
        return "archer";
    }
}
