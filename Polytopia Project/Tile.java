
/**
 * A tile of the board
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Tile
{
    private int owner;

    /**
     * Constructor for objects of class Tile
     */
    public Tile()
    {
        owner = 0;
    }
    
    public void setOwner (int newOwner)
    {
        owner = newOwner;
    }
    public int getOwner()
    {
        return owner;
    }
}
