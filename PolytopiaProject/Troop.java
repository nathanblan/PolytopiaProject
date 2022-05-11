
/**
 * Main class for Troops
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Troop
{
    // instance variables - replace the example below with your own
    private int health;
    private final int maxHealth;
    
    /**
     * Constructor for objects of class Troop
     */
    public Troop(int h)
    {
        maxHealth = h;
        health = maxHealth;
    }
    
    public void takeDamage(int d)
    {
        health -= d;
    }
    
    public void heal(int h)
    {
        health += h;
        if (health > maxHealth)
            health = maxHealth;
    }
    
    public void attack(Troop other)
    {}
}
