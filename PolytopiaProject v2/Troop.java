import javafx.scene.canvas.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;

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
    
    private char direction;
    
    /**
     * Constructor for objects of class Troop
     */
    protected Troop(Player p, int h, double a, double d, int r, int m)
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
        
        setFitHeight(Tile.TILE_SIZE);
        setFitWidth(Tile.TILE_SIZE);
        
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
    public void attack(Troop other, Group root)
    {
        int distance = CalcUtility.getDistance(curX, curY, other.curX, other.curY);
        
        double attackForce = attack * ((double)health/maxHealth);
        double defenseForce = other.defense * (other.health / other.maxHealth) * 1;
        double totalDamage = attackForce + defenseForce;
        int attackResult = round((attackForce / totalDamage) * attack * 4.5);
        int defenseResult = round((defenseForce / totalDamage) * other.defense * 4.5);
        
        other.health -= attackResult;
        if (other.health <= 0)
        {
            // killed other troop
            other.destroyTroop();
            moveTo(other.curX, other.curY);
        }
        else
        {
            animateAttack(other.curX, other.curY, root);
            
            if (other.range <= distance)
            {
                // counterattack
                health -= defenseResult;
                    
                new Thread(() -> {
                    CalcUtility.wait(1000);
                    Platform.runLater(() -> other.animateAttack(curX, curY, root));
                    if (health <= 0)
                    {
                        // troop die :P
                        CalcUtility.wait(500);
                        destroyTroop();
                    }
                }).start();
            }
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
            
            player.decStars(5);
        }
        else if (shipLevel == 3)
        {
            movement = 3;
            range = 2;
            attack = 4;
            defense = 3;
            
            player.decStars(15);
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
        updateImage(getDirection(x));
        setX(curX*Tile.TILE_SIZE);
        setY(curY*Tile.TILE_SIZE);
        
        animation.setDelay(Duration.millis(millis));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX(x*Tile.TILE_SIZE-getX());
        animation.setToY(y*Tile.TILE_SIZE-getY());
        
        animation.setCycleCount(1);
        animation.play();
        
        Player.troopMap[getXCoord()][getYCoord()] = null;
        Player.troopMap[x][y] = this;
        
        setXY(x, y);
    }
    
    public void destroyTroop()
    {
        Player.troopMap[curX][curY] = null;
        setImage(null);
    }
    
    private void animateAttack(int x, int y, Group root)
    {
        updateImage(getDirection(x));
        
        if (range == 1)
            meleeAttack(x, y);
        else
            rangeAttack(x, y, root);
    }
    
    private void meleeAttack(int x, int y)
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
    
    private void rangeAttack(int x, int y, Group root)
    {
        Circle c = new Circle(curX*Tile.TILE_SIZE+Tile.TILE_SIZE/2, curY*Tile.TILE_SIZE+Tile.TILE_SIZE/2, 2.5, Color.BLACK);
        root.getChildren().add(c);
        animation.setNode(c);
        
        animation.setDelay(Duration.millis(0));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX((x-curX+1)*Tile.TILE_SIZE/2);
        animation.setToY((y-curY+1)*Tile.TILE_SIZE/2);
        
        animation.setCycleCount(1);
        animation.play();
        
        animation.setNode(this);
        new Thread(() -> {
            CalcUtility.wait(500);
            c.setFill(Color.TRANSPARENT);
        }).start();
    }
    
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
        updateImage('r');
    }
    
    public void updateImage(char direction)
    {
        int n = player.getPlayerNum()+1;
        setImage(new Image("troops\\"+getInfo()+n+direction+".png"));
        
        this.direction = direction;
    }
    
    public String getInfo()
    {
        if (shipLevel == 1)
            return "boat";
        if (shipLevel == 2)
            return "ship";
        if (shipLevel == 3)
            return "battleship";
        return getType();
    }
    
    public String getType()
    {
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
    
    public String toString()
    {
        String output = getType();
        
        output += " "+curX+" "+curY+" "+player.getPlayerNum();
        output += " "+shipLevel+" "+health+" "+direction;
        output += " "+lastMoveTurn+" "+lastAttackTurn+" "+lastActionTurn;
        
        return output;
    }
    
    private char getDirection(Troop other)
    {
        return getDirection(other.curX);
    }
    
    private char getDirection(int other)
    {
        if (other < curX)
            return 'l';
        if (other > curX)
            return 'r';
        return direction;
    }
    
    public static Troop loadTroop(String save)
    {
        int space = save.indexOf(" ");
        String type = save.substring(0, space);
        int x = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        int y = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        int playerNum = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        
        Troop t = null;
        
        if (type.equals("archer"))
            t = new Archer(Display.getPlayer(playerNum), x, y);
        else if (type.equals("warrior"))
            t = new Warrior(Display.getPlayer(playerNum), x, y);
        else if (type.equals("rider"))
            t = new Rider(Display.getPlayer(playerNum), x, y);
        else if (type.equals("shield"))
            t = new Shield(Display.getPlayer(playerNum), x, y);
            
        t.shipLevel = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        t.health = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        t.direction = save.charAt(space+1);
        space += 2;
        
        t.lastMoveTurn = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        t.lastAttackTurn = nextInt(save, space);
        space = save.indexOf(" ", space+1);
        t.lastActionTurn = Integer.parseInt(save.substring(space+1));
        
        Player.troopMap[x][y] = t;
        
        return t;
    }
    
    private static int nextInt(String save, int space)
    {
        return Integer.parseInt(save.substring(space+1, save.indexOf(" ", space+1)));
    }
}
