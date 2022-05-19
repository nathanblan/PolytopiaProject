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
    
    private double attack;
    private double defense;
    private int lastTurn = 0;
    private Player player;
    
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
    public Troop(Player p, int h, double a, double d)
    {
        player = p;
        
        maxHealth = h;
        health = maxHealth;
        attack = a;
        defense = d;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void move()
    {
        lastTurn = player.getTurn();
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
    
    public int getShipLevel()
    {
        return shipLevel;
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
    
    public void claimCity(Player player)
    {
        
    }
    
    public void drawTroop(GraphicsContext gc, int x, int y)
    {
        if(shipLevel == 0) //non aquatic version of troop
        {
            gc.drawImage(new Image("images\\claimcity_button.png"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        else if(shipLevel == 1) //draw sailboat
        {
            gc.drawImage(new Image("images\\grass.jpg"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        else if(shipLevel == 2) //draw cruiser
        {
            gc.drawImage(new Image("images\\grass.jpg"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        else //draw battleship
        {
            gc.drawImage(new Image("images\\grass.jpg"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
        
    }
}
