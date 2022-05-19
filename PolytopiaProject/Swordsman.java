
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
        super(p, 15, 3, 3);
        movement = 1;
        range = 1;
    }
    
    public String getInfo()
    {
        return "swordsman";
    }
}
