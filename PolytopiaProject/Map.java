import javafx.scene.Group;
import javafx.scene.input.*;

/**
 * Write a description of class Map here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Map
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
    
    public Map (int size, Group root)
    {
        scale = 100;
        SIZE = size;
        char[][] charMap = MapGenerator.createTerrain(SIZE);
        map = new Tile[SIZE][SIZE];

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
                    
                map[r][c].setX(r*scale);
                map[r][c].setY(c*scale);
                map[r][c].setScale(scale);
                
                root.getChildren().add(map[r][c]);
                //break;
            }
            //break;
        }
        
        curX = 0;
        curY = 0;
        lastX = -1;
        lastY = -1;
    }
    
    public Tile getTile(int x, int y)
    {
        return map[x][y];
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
                /*
                if (Math.abs(xDiff) > scale/2)
                    xDiff = scale/2 * CalcUtility.sgn(xDiff);
                if (Math.abs(yDiff) > scale/2)
                    yDiff = scale/2 * CalcUtility.sgn(yDiff);
                */
                curX -= xDiff;
                curY -= yDiff;
                
                // only moves tiles on screen w/ a buffer of 1? since each drag is very small
                for (int x = max(0, (int)(curX/scale)-1); x < min(SIZE, (int)((curX+DISPLAY_SIZE)/scale)+1); x++)
                {
                    for (int y = max(0, (int)(curY/scale)-1); y < min(SIZE, (int)((curY+DISPLAY_SIZE)/scale)+1); y++)
                    {
                        map[x][y].setX(x*scale-curX);
                        map[x][y].setY(y*scale-curY);
                        map[x][y].toFront();
                    }
                }
                
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
    
    private int max(int a, int b)
    {
        if (a > b)
            return a;
        return b;
    }
    
    private int min(int a, int b)
    {
        if (a < b)
            return a;
        return b;
    }
}
