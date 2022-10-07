
/**
 * Write a description of class Catapult here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Catapult extends Troop
{
    /**
     * Constructor for objects of class Catapult
     */
    public Catapult(Player p)
    {
        super(p, 10, 4, 0, 3, 1);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "catapult";
    }
}
