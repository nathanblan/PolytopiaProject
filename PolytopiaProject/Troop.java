
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
    
    private final double attack;
    private final double defense;
    
    protected int range;
    protected int movement;
    
    /**
     * Constructor for objects of class Troop
     */
    public Troop(int h, double a, double d)
    {
        maxHealth = h;
        health = maxHealth;
        attack = a;
        defense = d;
    }
    
    public void heal(int h)
    {
        health += h;
        if (health > maxHealth)
            health = maxHealth;
    }
    
    /**
     * @return true if kills the other troop, false otherwise
     */
    public boolean attack(Troop other, int distance)
    {
        double attackForce = attack * ((double)health/maxHealth);
        double defenseForce = other.defense * (other.health / other.maxHealth) * 1;
        double totalDamage = attackForce + defenseForce;
        int attackResult = round((attackForce / totalDamage) * attack * 4.5);
        int defenseResult = round((defenseForce / totalDamage) * other.defense * 4.5);
        
        other.health -= attackResult;
        if (other.health <= 0)
            return true;
        
        if (other.range <= distance)
            health -= defenseResult;
        return false;
    }
    
    public int getMovement()
    {
        return movement;
    }
    
    public int getRange()
    {
        return range;
    }
    
    private int round (double num)
    {
        return (int)(num+0.5);
    }
}
