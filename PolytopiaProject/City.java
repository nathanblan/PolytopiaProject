
/**
 * City buildings
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class City
{
    private int player;
    private int population;
    private int level;
    /**
     * Constructor for objects of class City
     */
    public City()
    {
        player = 0;
        level = 0;
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
        if (population > level)
        {
            level++;
            population %= level;
        }
    }
}
