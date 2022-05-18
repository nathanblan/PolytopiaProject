
/**
 * Write a description of class Archer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Archer extends Troop
{
    /**
     * Constructor for objects of class Archer
     */
    public Archer(Player p)
    {
        super(p, 10, 2, 1);
        movement = 1;
        range = 2;
    }
}
