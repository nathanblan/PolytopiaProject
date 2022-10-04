import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.event.*;
import java.util.*;
import javafx.scene.*;

/**
 * Write a description of class CanvasMap here.
 *
 * @author Kaz
 * @version 9/23/2022
 */
public class Map extends Canvas
{
    // map variables
    private final int SIZE;
    private Tile[][] map;
    private final int DISPLAY_SIZE = 800;
    
    // variables for zooming in/out and moving around the map
    private int scale;
    private double curX;
    private double curY;
    
    private double lastX;
    private double lastY;
    
    private double clickX;
    private double clickY;
    
    private final int SCALE_MIN;
    private final int SCALE_MAX;
    
    // canvases that make up the map
    private final Canvas overlay;
    private final Canvas control;
    
    // all the nodes that are moved with the map, like fog
    private ArrayList<Node> dragged;
    
    // game-related variables
    private final int NUM_PLAYERS = 2;

    /**
     * Constructor for objects of class Map
     */
    public Map(int size)
    {
        super(100*size, 100*size);
        
        scale = 100;
        SIZE = size;
        
        // set constants
        SCALE_MAX = DISPLAY_SIZE/5;
        SCALE_MIN = DISPLAY_SIZE/SIZE;

        // get a map and draw it
        map = CalcUtility.getTileMap(SIZE);
        for (int r = 0; r < SIZE; r++)
        {
            for (int c = 0; c < SIZE; c++)
            {
                map[r][c].drawTile(getGraphicsContext2D(), r, c, 100);
            }
        }

        // set variables for dragging and location
        curX = 0;
        curY = 0;
        lastX = -1;
        lastY = -1;
        
        // add an overlay
        dragged = new ArrayList<Node>();
        overlay = new Canvas (100*size, 100*size);
        dragged.add(overlay);
        
        // set up top-most canvas for sensing drag
        control = new Canvas (DISPLAY_SIZE, DISPLAY_SIZE);
        
        control.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){public void handle(MouseEvent e){handleDrag(e);}});
        control.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){public void handle(MouseEvent e){handleLift(e);}});
        control.addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>(){public void handle(ScrollEvent e){handleZoom(e);}});
        control.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){public void handle(MouseEvent e){handlePress(e);}});
    }
    
    private void handlePress(MouseEvent e)
    {
        clickX = e.getX();
        clickY = e.getY();
    }
    
    private void handleDrag(MouseEvent e)
    {
        if (lastX != -1 && lastY != -1)
        {
            // find out how much mouse has moved
            double xDiff = e.getX()-lastX;
            double yDiff = e.getY()-lastY;
            
            // can control rate of drag
            xDiff *= 1;
            yDiff *= 1;
            
            curX -= xDiff;
            curY -= yDiff;
            
            relocate();
        }
        
        // record position from last drag
        lastX = e.getX();
        lastY = e.getY();
    }
    
    private void handleLift(MouseEvent e)
    {
        lastX = -1;
        lastY = -1;
        
        if (e.getX() == clickX && e.getY() == clickY)
        {
            int x = (int)(e.getX()+curX+(scale-100)*8)/scale;
            int y = (int)(e.getY()+curY+(scale-100)*8)/scale;
            
            System.out.println(x+" "+y);
        }
    }
    
    private void handleZoom(ScrollEvent e)
    {
        if (e.getDeltaX() == 0)
        {
            double change = 10 * CalcUtility.sgn(e.getDeltaY());
            
            scale += change;
            if (scale < SCALE_MIN)
                scale = SCALE_MIN;
            if (scale > SCALE_MAX)
                scale = SCALE_MAX;    
            
            setScaleX(scale/100.0);
            setScaleY(scale/100.0);
            
            relocate();
        }
    }
    
    /**
     * Moves the map, makes sure it stays within bounds
     */
    private void relocate()
    {
        if (curX < 0 - (scale-100)*8)
            curX = 0 - (scale-100)*8;
        if (curY < 0 - (scale-100)*8)
            curY = 0 - (scale-100)*8;
        if (curX > SIZE*scale-DISPLAY_SIZE - (scale-100)*8)
            curX = SIZE*scale-DISPLAY_SIZE - (scale-100)*8;
        if (curY > SIZE*scale-DISPLAY_SIZE - (scale-100)*8)
            curY = SIZE*scale-DISPLAY_SIZE - (scale-100)*8;
        
        relocate(-curX, -curY);
    }
    
    public Canvas controls()
    {
        return control;
    }
}
