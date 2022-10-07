
/**
 * Write a description of class MindBender here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MindBender extends Troop
{
    /**
     * Constructor for objects of class MindBender
     */
    public MindBender(Player p)
    {
        super(p, 10, 0, 1, 1, 1);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "mind bender";
    }
}
