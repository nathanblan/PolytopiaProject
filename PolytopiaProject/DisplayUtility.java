import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * Holds helper methods
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DisplayUtility
{
    private static final int w = 800;
    private static final int h = 800;
    
    public static final Image X_BTN = new Image("images\\X_button.png");
    public static final Image CHECK_BTN = new Image("images\\check_button.png");
    public static final Image END_TURN_BTN = new Image("images\\endturn_button.png");
    
    public static void drawTileMap(GraphicsContext gc, Tile[][] map)
    {
        int size = map.length;
        for (int r = 0; r < size; r++)
        {
            for (int c = 0; c < size; c++)
            {
                map[r][c].drawTile(gc, r, c);
            }
        }

        drawRegularScreen(gc);
    }
    
    public static void fillSide(GraphicsContext gc)
    {
        gc.drawImage(new Image("images\\starbackground.png"), w, 0, 200, h);
    }
    
    public static void drawConfirmScreen(GraphicsContext gc)
    {
        fillSide(gc);
        gc.drawImage(X_BTN, w+20, h-80, 60, 60);
        gc.drawImage(CHECK_BTN, w+120, h-80, 60, 60);
    }
    
    public static void drawRegularScreen(GraphicsContext gc)
    {
        fillSide(gc);
        //gc.drawImage(X_BTN, w+20, h-80, 60, 60);
        gc.drawImage(END_TURN_BTN, w+120, h-80, 60, 60);
    }
    
    public static void showSelectedTile(GraphicsContext gc, Color color, int x, int y)
    {
        gc.setFill(Color.web(color.toString(), 0.75));
        gc.fillRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, 5);
        gc.fillRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE+5, 5, Tile.TILE_SIZE-5);
        gc.fillRect(x*Tile.TILE_SIZE+5, y*Tile.TILE_SIZE+Tile.TILE_SIZE-5, Tile.TILE_SIZE-5, 5);
        gc.fillRect(x*Tile.TILE_SIZE+Tile.TILE_SIZE-5, y*Tile.TILE_SIZE+5, 5, Tile.TILE_SIZE-5);
    }
    
    public static void clearMovableTiles(GraphicsContext gc, Tile[][] map, int x, int y)
    {
        ArrayList<Coord> movable = CalcUtility.getMovableTiles(map, x, y);
        for (Coord c : movable)
        {
            clearTile(gc, c.x, c.y);
        }
    }
    
    public static void clearTile(GraphicsContext gc, int x, int y)
    {
        gc.clearRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
    
    public static void showMovableTiles(GraphicsContext gc, Tile[][] map, int x, int y)
    {
        ArrayList<Coord> movable = CalcUtility.getMovableTiles(map, x, y);
        for (Coord c : movable)
        {
            showMovableTile(gc, Color.LIGHTBLUE, c.x, c.y);
        }
    }
    
    private static void showMovableTile(GraphicsContext gc, Color c, int x, int y)
    {
        x *= Tile.TILE_SIZE;
        y *= Tile.TILE_SIZE;
        
        gc.setFill(Color.web(c.toString(), 0.75));
        gc.setStroke(Color.web(c.toString(), 0.75));
        gc.setLineWidth(8);
        
        gc.strokeOval(x+8, y+8, 34, 34);
        gc.setStroke(Color.web(Color.WHITE.toString(), 0.75));
        gc.strokeOval(x+16, y+16, 18, 18);
        gc.fillOval(x+20, y+20, 10, 10);
    }
    
    public static void showType(GraphicsContext gc, Tile t)
    {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(35));
        gc.setFill(Color.LIGHTGREY);
        gc.fillText(t.getInfo(), w+100, 50);
    }
    
    public static void drawTroops(Troop[][] map, GraphicsContext gc)
    {
        for (int x = 0; x < map.length; x++)
        {
            for (int y = 0; y < map[0].length; y++)
            {
                Troop t = map[x][y];
                if (t != null)
                    t.drawTroop(gc, x, y);
            }
        }
    }
}
