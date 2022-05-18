
/**
 * Write a description of class Rider here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Rider extends Troop
{
    /**
     * Constructor for objects of class Rider
     */
    public Rider(Player p)
    {
        super(p, 10, 2, 1);
        movement = 2;
        range = 1;
    }
}
