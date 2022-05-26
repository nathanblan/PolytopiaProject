
/**
 * Write a description of class Warrior here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Warrior extends Troop
{
    /**
     * Constructor for objects of class Warrior
     */
    public Warrior(Player p)
    {
        super(p, 10, 2, 2, 1, 1);
        
        canDash = true;
    }
    
    public Warrior(Player p, int turn)
    {
        super(p, 10, 2, 2, 1, 1);
        
        canDash = true;
        
        super.updateLastAttackTurn(turn);
        super.updateLastMoveTurn(turn);
    }
    
    public String getInfo()
    {
        if (shipLevel > 0)
            return super.getInfo();
        return "warrior";
    }
    
    
    public void drawTroop(GraphicsContext gc, int x, int y)
    {
        int playerNum = getPlayer().getPlayerNum()+1;
        if (shipLevel > 0)
            super.drawTroop(gc, x, y);
        else
            gc.drawImage(new Image("troops\\warrior"+playerNum+".png"), x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
}
