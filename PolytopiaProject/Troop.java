
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
    private int maxHealth;
    
    private double attack;
    private double defense;
    
    // if 0, not a ship
    // if 1, sailboat
    // if 2, cruiser
    // if 3, battleship
    private int shipLevel = 0;
    
    protected int range;
    protected int movement;
    protected boolean waterMovement = false;
    
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
    
    public boolean getWaterMovement()
    {
        return waterMovement;
    }
    
    public int getRange()
    {
        return range;
    }
    
    public void upgradeShip() // upgrading ships
    {
        shipLevel++;
        if(shipLevel == 1)
        {
            waterMovement = true;
            movement = 1;
        }
        if(shipLevel == 2)
        {
            movement = 3;
        }
        if(shipLevel == 3)
        {
            attack = 10;
        }
    }
    
    public void destroyShip() // when you turn a waterborne troop back into a land troop
    {
        shipLevel = 0;
        waterMovement = false;
    }
    private int round (double num)
    {
        return (int)(num+0.5);
    }
}
