
/**
 * A forest tile. Can hunt (30% chance to have animal) and build lumber hut.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Forest extends Tile
{
    private boolean hasAnimal;
    private boolean hasLumberHut;
    
    /**
     * Constructor for objects of class Forest
     */
    public Forest()
    {
        hasLumberHut = false;
        hasAnimal = Math.random() < 0.3; // 30% chance to have animal
    }
    
    public int hunt()
    {
        if (!hasAnimal)
            return -1;
        hasAnimal = false;
        
        city.incPopulation(1);
        
        return 1;
    }
    
    public int buildLumberHut()
    {
        if (hasLumberHut)
            return -1;
        hasLumberHut = true;
        hasAnimal = false;
        
        city.incPopulation(1);
        
        return 1;
    }
}
