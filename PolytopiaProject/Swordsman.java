
/**
 * Write a description of class Swordsman here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Swordsman extends Troop
{
    /**
     * Constructor for objects of class Swordsman
     */
    public Swordsman(Player p)
    {
        super(p, 15, 3, 3, 1, 1);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "swordsman";
    }
}
