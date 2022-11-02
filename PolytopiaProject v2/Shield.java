
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
        
        setXY(x, y);
    }
    
    public Shield(Player p, int turn, int x, int y)
    {
        super(p, 10, 1, 3, 1, 1);
        
        updateLastAttackTurn(turn);
        updateLastMoveTurn(turn);
        updateLastActionTurn(turn);
        setXY(x, y);
        
        p.decStars(3);
    }
    
    public String getType()
    {
        return "shield";
    }
}
