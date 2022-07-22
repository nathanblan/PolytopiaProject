import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.event.*;

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
    
    private final int SCALE_MIN;
    private final int SCALE_MAX;

    /**
     * Constructor for objects of class CanvasMap
     */
    public Map(int size)
    {
        super(100*size, 100*size);
        
        scale = 100;
        SIZE = size;
        
        SCALE_MAX = DISPLAY_SIZE/5;
        SCALE_MIN = DISPLAY_SIZE/SIZE;
        
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
            
            curX -= xDiff;
            curY -= yDiff;
            
            if (curX < 0)
                curX = 0;
            if (curY < 0)
                curY = 0;
            if (curX > SIZE*scale-DISPLAY_SIZE)
                curX = SIZE*scale-DISPLAY_SIZE;
            if (curY > SIZE*scale-DISPLAY_SIZE)
                curY = SIZE*scale-DISPLAY_SIZE;
            
            relocate();
        }
        lastX = e.getX();
        lastY = e.getY();
    }
    
    public void liftClick()
    {
        lastX = -1;
        lastY = -1;
    }
    
    public void handleZoom(ScrollEvent e)
    {
        if (e.getDeltaX() == 0)
        {
            double change = e.getDeltaY() * 5 / 16;
            
            scale += change;
            if (scale < SCALE_MIN)
                scale = SCALE_MIN;
            if (scale > SCALE_MAX)
                scale = SCALE_MAX;
            
            System.out.println(scale);    
            
            setScaleX(scale/100.0);
            setScaleY(scale/100.0);
        }
    }
    
    private void relocate()
    {
        //relocate(scale*SIZE-curX, scale*SIZE-curY);
        relocate(-curX, -curY);
    }
    
    public Canvas controls()
    {
        Canvas canvas = new Canvas(DISPLAY_SIZE, DISPLAY_SIZE);
        
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                handleDrag(e);
            }
        });
        
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                liftClick();
            }
        });
        
        /*canvas.addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>()
        {
           @Override
           public void handle(ScrollEvent e)
           {
               handleZoom(e);
           }
        });*/
        
        return canvas;
    }
}
