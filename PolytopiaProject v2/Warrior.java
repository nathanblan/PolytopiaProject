
/**
 * Write a description of class Warrior here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.*;

public class Warrior extends Troop
{
    /**
     * Constructor for objects of class Warrior
     */
    public Warrior(Player p, int x, int y)
    {
        super(p, 10, 2, 2, 1, 1);
        
        canDash = true;
        setXY(x, y);
    }
    
    public Warrior(Player p, int turn, int x, int y)
    {
        super(p, 10, 2, 2, 1, 1);
        
        canDash = true;
        
        updateLastAttackTurn(turn);
        updateLastMoveTurn(turn);
        updateLastActionTurn(turn);
        setXY(x, y);
        
        p.decStars(2);
    }
    
    public String getType()
    {
        return "warrior";
    }
}
