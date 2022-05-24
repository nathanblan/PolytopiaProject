import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
/**
 * Main class for Troops
 * 
 */
public class Troop
{
    // instance variables
    private int health;
    private int maxHealth;
    
    private final double baseAttack;
    private final double baseDefense;
    private double attack;
    private double defense;
    
    private int lastMoveTurn;
    private int lastAttackTurn;
    
    private Player player;
    
    // if 0, not a ship
    // if 1, sailboat
    // if 2, cruiser
    // if 3, battleship
    protected int shipLevel = 0;
    
    protected int range;
    protected int movement;
    protected boolean waterMovement = false;
    
    protected boolean canDash;
    
    /**
     * Constructor for objects of class Troop
     */
    public Troop(Player p, int h, double a, double d)
    {
        player = p;
        
        maxHealth = h;
        health = maxHealth;
        attack = a;
        defense = d;
        baseAttack = a;
        baseDefense = d;
        
        lastMoveTurn = -1;
        lastAttackTurn = -1;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void heal(int h)
    {
        health += h;
        if (health > maxHealth)
            health = maxHealth;
    }
    
    public int getHealth()
    {
        return health;
    }
    
    public boolean canHeal()
    {
        return health < maxHealth;
    }
    
    public boolean canDash()
    {
        if (shipLevel > 0)
            return false;
        return canDash;
    }
    
    public void updateLastMoveTurn(int turn)
    {
        lastMoveTurn = turn;
    }
    
    public int getLastMoveTurn()
    {
        return lastMoveTurn;
    }
    
    public void updateLastAttackTurn(int turn)
    {
        lastAttackTurn = turn;
    }
    
    public int getLastAttackTurn()
    {
        return lastAttackTurn;
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
        if (shipLevel == 1)
        {
            waterMovement = true;
            movement = 1;
        }
        if (shipLevel == 2)
        {
            movement = 3;
        }
        if (shipLevel == 3)
        {
            attack = 10;
        }
    }
    
    public int getShipLevel()
    {
        return shipLevel;
    }
    
    public void destroyShip() // when you turn a waterborne troop back into a land troop
    {
        shipLevel = 0;
        waterMovement = false;
        attack = baseAttack;
        defense = baseDefense;
    }
    private int round (double num)
    {
        return (int)(num+0.5);
    }
    
    public void claimCity(Player player)
    {
        
    }
    
    public void drawTroop(GraphicsContext gc, int x, int y)
    {
        if(shipLevel == 1) //draw sailboat
        {
            gc.drawImage(new Image("troops\\boat.jpg"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        else if(shipLevel == 2) //draw cruiser
        {
            gc.drawImage(new Image("troops\\sailboat.jpg"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        else //draw battleship
        {
            gc.drawImage(new Image("troops\\battleship.jpg"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        
    }
    
    public String getInfo()
    {
        return "basic";
    }
}
