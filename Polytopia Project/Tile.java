
/**
 * A tile of the board
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Tile
{
    protected City city;
    
    /**
     * Constructor for objects of class Tile
     */
    public Tile()
    {}
    
    public void setOwner (int newOwner)
    {
        if (city != null)
            city.setOwner(newOwner);
    }
    public int getOwner()
    {
        if (city == null)
            return 0;
        return city.getOwner();
    }
    
    public City getCity()
    {
        return city;
    }
}
