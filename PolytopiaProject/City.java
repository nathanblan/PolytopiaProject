
/**
 * City buildings
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class City extends Tile
{
    private int player;
    private int population;
    private int level;
    private int levelInc; // incrementor for population until next level
    /**
     * Constructor for objects of class City
     */
    public City()
    {
        player = 1;
        level = 1;
        levelInc = 2;
        population = 0;
    }
    
    public City(int player)
    {
        this.player = player;
        level = 1;
        levelInc = 2;
        population = 0;
    }
    
    public void setPlayer (int newPlayer)
    {
        player = newPlayer;
    }
    
    public int getPlayer()
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
}
