import javafx.scene.canvas.*;
import javafx.scene.input.*;

/**
 * Write a description of class CanvasMap here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Map extends Canvas
{
    // instance variables - replace the example below with your own
    private final int SIZE;
    private Tile[][] map;
    private final int DISPLAY_SIZE = 800;
    
    // variables for zooming in/out and moving around the map
    private int scale;
    private double curX;
    private double curY;
    
    private double lastX;
    private double lastY;

    /**
     * Constructor for objects of class CanvasMap
     */
    public Map(int size)
    {
        super(100*size, 100*size);
        
        scale = 100;
        SIZE = size;
        char[][] charMap = MapGenerator.createTerrain(SIZE);
        map = new Tile[SIZE][SIZE];
        GraphicsContext gc = getGraphicsContext2D();

        for (int r = 0; r < SIZE; r++)
        {
            for (int c = 0; c < SIZE; c++)
            {
                if (charMap[r][c] == 'A')
                    map[r][c] = new Mountain();
                else if (charMap[r][c] == '=')
                    map[r][c] = new DeepWater();
                else if (charMap[r][c] == '~')
                    map[r][c] = new Water();
                else if (charMap[r][c] == '+')
                    map[r][c] = new Forest();
                else if (charMap[r][c] == ',')
                    map[r][c] = new Grass();
                else if (charMap[r][c] == '-')
                    map[r][c] = new Field();
                else if (charMap[r][c] == 'c')
                    map[r][c] = new City(r,c);
                    
                map[r][c].drawTile(gc, r, c, scale);
            }
        }
        
        curX = 0;
        curY = 0;
        lastX = -1;
        lastY = -1;
    }
    
    public void handleDrag(MouseEvent e)
    {
        if (lastX != -1 && lastY != -1)
        {
            double xDiff = e.getX()-lastX;
            double yDiff = e.getY()-lastY;
            
            xDiff *= 1;
            yDiff *= 1;
            if (Math.abs(xDiff) > 10 || Math.abs(yDiff) > 10)
            {
                if (xDiff > curX)
                    xDiff = curX;
                if (yDiff > curY)
                    yDiff = curY;
                if (xDiff < curX-SIZE*scale+DISPLAY_SIZE)
                    xDiff = curX-SIZE*scale+DISPLAY_SIZE;
                if (yDiff < curY-SIZE*scale+DISPLAY_SIZE)
                    yDiff = curY-SIZE*scale+DISPLAY_SIZE;
                    
                curX -= xDiff;
                curY -= yDiff;
                
                relocate(-curX, -curY);
                
                lastX = e.getX();
                lastY = e.getY();
            }
        }
        else
        {
            lastX = e.getX();
            lastY = e.getY();
        }
    }
    
    public void liftClick()
    {
        lastX = -1;
        lastY = -1;
    }
}
