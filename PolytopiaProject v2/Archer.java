
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
        setXY(x, y);
    }
    
    public Archer(Player p, int turn, int x, int y)
    {
        super(p, 10, 2, 1, 2, 1);
        
        canDash = true;
        
        updateLastAttackTurn(turn);
        updateLastMoveTurn(turn);
        updateLastActionTurn(turn);
        setXY(x, y);
        
        p.decStars(3);
    }
    
    public String getType()
    {
        return "archer";
    }
}
