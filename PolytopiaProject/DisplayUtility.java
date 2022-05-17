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

        fillSide(gc);
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
    
    public static void showSelectedTile(GraphicsContext gc, Color color, int x, int y)
    {
        gc.setFill(Color.web(color.toString(), 0.75));
        gc.fillRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, 5);
        gc.fillRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE+5, 5, Tile.TILE_SIZE-5);
        gc.fillRect(x*Tile.TILE_SIZE+5, y*Tile.TILE_SIZE+Tile.TILE_SIZE-5, Tile.TILE_SIZE-5, 5);
        gc.fillRect(x*Tile.TILE_SIZE+Tile.TILE_SIZE-5, y*Tile.TILE_SIZE+5, 5, Tile.TILE_SIZE-5);
    }
    
    public static void showType(GraphicsContext gc, Tile t)
    {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(35));
        gc.setFill(Color.LIGHTGREY);
        gc.fillText(t.getInfo(), w+100, 50);
    }
}
