/**
 * Write a description of class City here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class City
{
    private int owner;
    private int population;
    private int level;
    /**
     * Constructor for objects of class City
     */
    public City()
    {
        owner = 0;
        level = 0;
        population = 0;
    }
    
    public void setOwner (int newOwner)
    {
        owner = newOwner;
    }
    public int getOwner()
    {
        return owner;
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
