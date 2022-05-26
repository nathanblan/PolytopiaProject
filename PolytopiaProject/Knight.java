
/**
 * Write a description of class Cavalry here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Knight extends Troop
{
    /**
     * Constructor for objects of class Knight
     */
    public Knight(Player p)
    {
        super(p, 10, 3.5, 1, 1, 3);
        
        canDash = true;
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "knight";
    }
}
