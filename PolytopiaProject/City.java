import javafx.scene.image.Image;
import javafx.scene.canvas.*;
import java.util.ArrayList;

/**
 * City buildings
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class City extends Tile
{
    private Player player;
    private int population;
    private int level;
    private int levelInc; // incrementor for population until next level
    
    /**
     * Constructor for objects of class City
     */
    public City()
    {
        player = null;
        level = 1;
        levelInc = 2;
        population = 0;
    }
    
    public City(Player player)
    {
        this.player = player;
        level = 1;
        levelInc = 2;
        population = 0;
    }
    
    public void setPlayer (Player newPlayer, Tile[][] map, int x, int y)
    {
        if (player == null)
        {
            ArrayList<Integer> xValues = new ArrayList<Integer>();
            ArrayList<Integer> yValues = new ArrayList<Integer>();
            int size = map.length;
            
            xValues.add(x);
            if (x != 0)
                xValues.add(x-1);
            if (x != size-1)
                xValues.add(x+1);
            
            yValues.add(y);
            if (y != 0)
                yValues.add(y-1);
            if (y != size-1)
                yValues.add(y+1);
                
            for (int a : xValues)
            {
                for (int b : yValues)
                {
                    if (map[a][b].getCity() == null)
                        map[a][b].setCity(this);
                }
            }
        }
        
        player = newPlayer;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void incPopulation(int num)
    {
        population += num;
        if (population >= levelInc)
        {
            level++;
            levelInc++;
            population = population%level;
        }
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        if (player == null)
            gc.drawImage(new Image("images\\village.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else if (player.getPlayerNum() == 0) // player 1 city
            gc.drawImage(new Image("images\\city.png"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else // player 2 city
            gc.drawImage(new Image("images\\city.png"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
    
    public String getInfo()
    {
        if (player == null)
            return "village";
        return "city lvl "+level;
    }
}
