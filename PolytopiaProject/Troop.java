import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.application.Platform;

/**
 * Main class for Troops
 * 
 */
public class Troop extends ImageView
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
    private int lastActionTurn;
    
    private Player player;
    
    // if 0, not a ship
    // if 1, sailboat
    // if 2, cruiser
    // if 3, battleship
    protected int shipLevel = 0;
    
    private final int baseRange;
    private final int baseMovement;
    protected int range;
    protected int movement;
    protected boolean waterMovement = false;
    
    protected boolean canDash;
    
    private final TranslateTransition animation;
    
    private int curX;
    private int curY;
    
    /**
     * Constructor for objects of class Troop
     */
    public Troop(Player p, int h, double a, double d, int r, int m)
    {
        player = p;
        
        maxHealth = h;
        health = maxHealth;
        attack = a;
        defense = d;
        range = r;
        movement = m;
        baseAttack = a;
        baseDefense = d;
        baseRange = r;
        baseMovement = m;
        
        lastMoveTurn = -1;
        lastAttackTurn = -1;
        lastActionTurn = -1;
        
        super.setFitHeight(Tile.TILE_SIZE);
        super.setFitWidth(Tile.TILE_SIZE);
        
        animation = new TranslateTransition();
        animation.setNode(this);
        updateImage();
        animation.setAutoReverse(true);
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
    
    public void updateLastActionTurn(int turn)
    {
        lastActionTurn = turn;
    }
    
    public int updateLastActionTurn()
    {
        return lastActionTurn;
    }
    
    /**
     * @return true if kills the other troop, false otherwise
     */
    public void attack(Troop other)
    {
        int distance = CalcUtility.getDistance(getXCoord(), getYCoord(), other.getXCoord(), other.getYCoord());
        
        double attackForce = attack * ((double)health/maxHealth);
        double defenseForce = other.defense * (other.health / other.maxHealth) * 1;
        double totalDamage = attackForce + defenseForce;
        int attackResult = round((attackForce / totalDamage) * attack * 4.5);
        int defenseResult = round((defenseForce / totalDamage) * other.defense * 4.5);
        
        other.health -= attackResult;
        if (other.health <= 0)
            other.destroyTroop();
        else if (other.range <= distance)
        {
            health -= defenseResult;
            if (health <= 0)
                destroyTroop();
        }
    }
    
    public int getMovement()
    {
        return movement;
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
            movement = 2;
            range = 2;
            attack = 1;
            defense = 1;
        }
        else if (shipLevel == 2)
        {
            movement = 3;
            range = 2;
            attack = 2;
            defense = 2;
        }
        else if (shipLevel == 3)
        {
            movement = 3;
            range = 2;
            attack = 4;
            defense = 3;
        }
        
        updateImage();
    }
    
    public boolean canUpgradeShip()
    {
        return (shipLevel==1 || (shipLevel==2 && player.getTree().getNavigation())) && player.getStars() >= 10;
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
        range = baseRange;
        movement = baseMovement;
        
        updateImage();
    }
    private int round (double num)
    {
        return (int)(num+0.5);
    }
    
    public void setXY(int x, int y)
    {
        curX = x;
        curY = y;
    }
    
    public void moveTo(int x, int y)
    {
        moveTo(x, y, 0);
    }
    
    public void moveTo(int x, int y, int millis)
    {
        setX(curX*Tile.TILE_SIZE);
        setY(curY*Tile.TILE_SIZE);
        
        animation.setDelay(Duration.millis(millis));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX(x*Tile.TILE_SIZE-getX());
        animation.setToY(y*Tile.TILE_SIZE-getY());
        
        animation.setCycleCount(1);
        animation.play();
        
        setXY(x, y);
    }
    
    public void destroyTroop()
    {
        Player.troopMap[getXCoord()][getYCoord()] = null;
        setImage(null);
    }
    
    public void animateAttack(int x, int y)
    {
        if (range == 1)
            meleeAttack(x, y);
        else
            rangeAttack(x, y);
    }
    
    public void meleeAttack(int x, int y)
    {
        setX(curX*Tile.TILE_SIZE);
        setY(curY*Tile.TILE_SIZE);
        
        animation.setDelay(Duration.millis(0));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX(x*Tile.TILE_SIZE-getX());
        animation.setToY(y*Tile.TILE_SIZE-getY());
        
        animation.setCycleCount(2);
        animation.play();
    }
    
    public void rangeAttack(int x, int y)
    {}
    
    public int getXCoord()
    {
        return (int)getX()/Tile.TILE_SIZE;
    }
    public int getYCoord()
    {
        return (int)getY()/Tile.TILE_SIZE;
    }
    
    public void updateImage()
    {
        int n = player.getPlayerNum()+1;
        if (shipLevel == 1)
            super.setImage(new Image("troops\\boat"+n+".png"));
        else if (shipLevel == 2)
            super.setImage(new Image("troops\\ship"+n+".png"));
        else
            super.setImage(new Image("troops\\battleship"+n+".png"));
    }
    
    public String getInfo()
    {
        if (shipLevel == 1)
            return "boat";
        if (shipLevel == 2)
            return "ship";
        if (shipLevel == 3)
            return "battleship";
        return "basic";
    }
    
    private void wait(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
}
