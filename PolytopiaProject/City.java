import javafx.scene.image.Image;
import javafx.scene.canvas.*;

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
    
    public void setPlayer (Player newPlayer)
    {
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
        
    }
    
    public String getInfo()
    {
        if (player == null)
            return "village";
        return "city lvl "+level;
    }
}
