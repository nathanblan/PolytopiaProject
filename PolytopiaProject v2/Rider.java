
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
        setXY(x, y);
    }
    
    public Rider(Player p, int turn, int x, int y)
    {
        super(p, 10, 2, 1, 1, 2);
        
        canDash = true;
        
        updateLastAttackTurn(turn);
        updateLastMoveTurn(turn);
        updateLastActionTurn(turn);
        setXY(x, y);
        
        p.decStars(3);
    }
    
    public String getType()
    {
        return "rider";
    }
}
