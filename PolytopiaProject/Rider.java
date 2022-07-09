
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
        setX(x*Tile.TILE_SIZE);
        setY(y*Tile.TILE_SIZE);
    }
    
    public Rider(Player p, int turn, int x, int y)
    {
        super(p, 10, 2, 1, 1, 2);
        
        canDash = true;
        
        super.updateLastAttackTurn(turn);
        super.updateLastMoveTurn(turn);
        super.updateLastActionTurn(turn);
        p.decStars(3);
        
        super.setXY(x, y);
        setX(x*Tile.TILE_SIZE);
        setY(y*Tile.TILE_SIZE);
    }
    
    public String getType()
    {
        return "rider";
    }
}
