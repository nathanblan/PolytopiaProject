
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
        super(p, 10, 1, 3, 1, 1);
    }
    
    public Shield(Player p, int turn)
    {
        super(p, 10, 1, 3, 1, 1);
        
        super.updateLastAttackTurn(turn);
        super.updateLastMoveTurn(turn);
        super.updateLastActionTurn(turn);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "shield";
    }
}
