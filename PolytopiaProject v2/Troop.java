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
public class Troop
{
    private static final int SCALE = 100;
    
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
    
    private static final TranslateTransition animation = new TranslateTransition();
    private static ImageView movingObject = new ImageView();
    
    private int curX;
    private int curY;
    
    private char direction;
    
    private static GraphicsContext troopGC;
    
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
        
        //setFitHeight(Tile.TILE_SIZE);
        //setFitWidth(Tile.TILE_SIZE);
        updateImage();
    }
    
    public static void setGraphics(GraphicsContext gc, Group root)
    {
        troopGC = gc;
        troopGC.setFill(Color.TRANSPARENT);
        root.getChildren().add(movingObject);
        
        animation.setNode(movingObject);
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
        int distance = CalcUtility.getDistance(curX, curY, other.curX, other.curY);
        
        double attackForce = attack * ((double)health/maxHealth);
        double defenseForce = other.defense * (other.health / other.maxHealth) * 1;
        double totalDamage = attackForce + defenseForce;
        int attackResult = round((attackForce / totalDamage) * attack * 4.5);
        int defenseResult = round((defenseForce / totalDamage) * other.defense * 4.5);
        
        other.health -= attackResult;
        if (other.health <= 0)
        {
            // Killed other troop
            other.destroyTroop();
            moveTo(other.curX, other.curY);
        }
        else
        {
            animateAttack(other.curX, other.curY);
            
            if (other.range <= distance)
            {
                // Counterattack
                health -= defenseResult;
                    
                new Thread(() -> {
                    CalcUtility.wait(1000);
                    Platform.runLater(() -> other.animateAttack(curX, curY));
                    if (health <= 0)
                    {
                        // Troop dies :P
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
        //updateImage(getDirection(x));
        troopGC.fillRect(curX*SCALE, curY*SCALE, SCALE, SCALE);
        
        movingObject.setImage(getImage(getDirection(x)));
        movingObject.setX(curX*SCALE);
        movingObject.setY(curY*SCALE);
        
        animation.setDelay(Duration.millis(millis));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX((x-curX)*Tile.TILE_SIZE);
        animation.setToY((y-curY)*Tile.TILE_SIZE);
        
        animation.setCycleCount(1);
        animation.play();
        
        Player.troopMap[getXCoord()][getYCoord()] = null;
        Player.troopMap[x][y] = this;
        
        setXY(x, y);
        movingObject.setImage(null);
    }
    
    public void destroyTroop()
    {
        Player.troopMap[curX][curY] = null;
        //(null);
    }
    
    public void animateAttack(int x, int y)
    {
        updateImage(getDirection(x));
        
        if (range == 1)
            meleeAttack(x, y);
        else
            rangeAttack(x, y);
    }
    
    private void meleeAttack(int x, int y)
    {
        troopGC.fillRect(curX*SCALE, curY*SCALE, SCALE, SCALE);
        
        movingObject.setImage(getImage(getDirection(x)));
        animation.setDelay(Duration.millis(0));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX((x-curX)*Tile.TILE_SIZE);
        animation.setToY((y-curY)*Tile.TILE_SIZE);
        
        animation.setCycleCount(2);
        animation.play();
        
        movingObject.setImage(null);
    }
    
    private void rangeAttack(int x, int y)
    {
        //movingObject = new Circle(curX*Tile.TILE_SIZE+Tile.TILE_SIZE/2, curY*Tile.TILE_SIZE+Tile.TILE_SIZE/2, 2.5, Color.BLACK);
        
        animation.setDelay(Duration.millis(0));
        animation.setFromX(0);
        animation.setFromY(0);
        animation.setToX((x-curX+1)*Tile.TILE_SIZE/2);
        animation.setToY((y-curY+1)*Tile.TILE_SIZE/2);
        
        animation.setCycleCount(1);
        animation.play();
        
        movingObject.setImage(null);
    }
    
    public int getXCoord()
    {
        return curX;
    }
    public int getYCoord()
    {
        return curY;
    }
    
    public void updateImage()
    {
        updateImage('r');
    }
    
    public void updateImage(char direction)
    {
        //setImage(new Image("troops\\"+getInfo()+n+direction+".png"));
        troopGC.fillRect(curX*SCALE, curY*SCALE, SCALE, SCALE);
        troopGC.drawImage(getImage(direction), curX*SCALE, curY*SCALE, SCALE, SCALE);
        
        this.direction = direction;
    }
    
    private Image getImage()
    {
        int n = player.getPlayerNum()+1;
        return new Image("troops\\"+getInfo()+n+"r.png");
    }
    
    private Image getImage(char direction)
    {
        int n = player.getPlayerNum()+1;
        return new Image("troops\\"+getInfo()+n+direction+".png");
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
