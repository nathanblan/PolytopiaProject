
/**
 * Write a description of class Warrior here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Warrior extends Troop
{
    /**
     * Constructor for objects of class Warrior
     */
    public Warrior(Player p)
    {
        super(p, 10, 2, 2);
        movement = 1;
        range = 1;
    }
}
