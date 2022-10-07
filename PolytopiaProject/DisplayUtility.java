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
    private static final int w = 1600;
    private static final int h = 800;
    private static final int size = 16;
    
    public static final Image X_BTN = new Image("images\\X_button.png");
    public static final Image CHECK_BTN = new Image("images\\check_button.png");
    public static final Image END_TURN_BTN = new Image("images\\endturn_button.png");
    
    public static void clearTroopFog(Canvas fog, int x, int y, int r, Player p)
    {
        ArrayList<Integer> Xs = new ArrayList<Integer>();
        ArrayList<Integer> Ys = new ArrayList<Integer>();
        
        Xs.add(x);
        if (x != 0)
        {
            Xs.add(x-1);
            if (x != 1 && r > 1)
            {
                Xs.add(x-2);
                if (x != 2 && r > 2)
                    Xs.add(x-3);
            }
        }
        if (x != size-1)
        {
            Xs.add(x+1);
            if (x != size-2 && r > 1)
            {
                Xs.add(x+2);
                if (x != size-3 && r > 2)
                    Xs.add(x+3);
            }
        }
                            
        Ys.add(y);
        if (y != 0)
        {
            Ys.add(y-1);
            if (y != 1 && r > 1)
            {
                Ys.add(y-2);
                if (y != 2 && r > 2)
                    Ys.add(y-3);
            }
        }
        if (y != size-1)
        {
            Ys.add(y+1);
            if (y != size-2 && r > 1)
            {
                Ys.add(y+2);
                if (y != size-3 && r > 2)
                    Ys.add(y+3);
            }
        }
        
        for (int a : Xs)
        {
            for (int b : Ys)
            {
                clearFog(fog, a, b);
                p.fogMap[a][b] = true;
            }
        }
    }
    
    public static void clearStartingFog(Canvas fog, int x, int y, Player p)
    {
        ArrayList<Integer> Xs = new ArrayList<Integer>();
        ArrayList<Integer> Ys = new ArrayList<Integer>();
        
        Xs.add(x);
        if (x != 0)
        {
            Xs.add(x-1);
            if (x != 1)
                Xs.add(x-2);
        }
        if (x != size-1)
        {
            Xs.add(x+1);
            if (x != size-2)
                Xs.add(x+2);
        }
                            
        Ys.add(y);
        if (y != 0)
        {
            Ys.add(y-1);
            if (y != 1)
                Ys.add(y-2);
        }
        if (y != size-1)
        {
            Ys.add(y+1);
            if (y != size-2)
                Ys.add(y+2);
        }
        
        
        for (int a : Xs)
        {
            for (int b : Ys)
            {
                clearFog(fog, a, b);
                p.fogMap[a][b] = true;
            }
        }
    }
    
    public static void clearFog(Canvas fog, int x, int y)
    {
        clearTile(fog.getGraphicsContext2D(), x, y);
    }

    public static void fillFog(Canvas fog)
    {
        GraphicsContext gc = fog.getGraphicsContext2D();
        gc.drawImage(new Image("images\\fog.png"), 0, 0, w, h);
    }
    
    public static void drawActionButtons(GraphicsContext gc, ArrayList<ActionButton> actions, Player p)
    {
        for (int i = 0; i < actions.size(); i++)
        {
            gc.drawImage(actions.get(i).getButton(p), w+20, 100+i*180, 160, 160);
        }
    }
    
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
    public static void clearSide(GraphicsContext gc, Player p)
    {
        gc.clearRect(w, 0, 200, h);
        showStars(gc, p);
    }
    
    public static void showStars(GraphicsContext gc, Player p)
    {
        gc.drawImage(new Image("images\\star.png"), w+90, 5, 20, 20);
        
        gc.setFill(Color.LIGHTGREY);
        gc.setFont(new Font(20));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(p.getStars()), w+70, 22.5);
        gc.fillText("(+"+p.getWorth()+")", w+140, 22.5);
    }
    
    public static void drawConfirmScreen(GraphicsContext gc, Player p, boolean canDoAction)
    {
        clearSide(gc, p);
        gc.drawImage(X_BTN, w+20, h-80, 60, 60);
        if (canDoAction)
            gc.drawImage(CHECK_BTN, w+120, h-80, 60, 60);
    }
    
    public static void drawRegularScreen(GraphicsContext gc, Player p)
    {
        clearSide(gc, p);
        gc.drawImage(new Image("techtree_images\\tech tree.png"), w+20, h-80, 60, 60);
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
        ArrayList<Coord> attackable = CalcUtility.getAttackableTiles(x, y);
        for (Coord c : attackable)
        {
            clearTile(gc, c.x, c.y);
        }
        clearTile(gc, x, y);
    }
    
    public static void clearTile(GraphicsContext gc, int x, int y)
    {
        gc.clearRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }
    
    public static void showSelectedTroop(GraphicsContext gc, int x, int y)
    {
        x *= Tile.TILE_SIZE;
        y *= Tile.TILE_SIZE;
        gc.setStroke(Color.web(Color.GAINSBORO.toString(), 0.75));
        gc.setLineWidth(5);
        gc.strokeOval(x+10, y+10, 30, 30);
    }
    
    public static void showMovableTiles(GraphicsContext gc, Tile[][] map, int x, int y)
    {
        ArrayList<Coord> movable = CalcUtility.getMovableTiles(map, x, y);
        for (Coord c : movable)
        {
            showMovableTile(gc, Color.LIGHTBLUE, c.x, c.y);
        }
    }
    
    public static void showAttackableTiles(GraphicsContext gc, int x, int y)
    {
        ArrayList<Coord> attackable = CalcUtility.getAttackableTiles(x, y);
        for (Coord c : attackable)
        {
            showMovableTile(gc, Color.RED, c.x, c.y);
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
    
    public static void showXBtn(GraphicsContext gc)
    {
        gc.drawImage(DisplayUtility.X_BTN, w+120, h-80, 60, 60);
    }
    
    public static void showType(GraphicsContext gc, Tile t)
    {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(35));
        gc.setFill(Color.LIGHTGREY);
        gc.fillText(t.getInfo(), w+100, 60);
    }
    
    public static void showType(GraphicsContext gc, Troop t)
    {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(35));
        gc.setFill(Color.LIGHTGREY);
        gc.fillText(t.getInfo(), w+100, 60);
        gc.setFont(new Font(20));
        gc.fillText(t.getHealth()+" health", w+100, 85);
    }
    
    public static void fillActionScreen(GraphicsContext gc)
    {
        gc.drawImage(new Image("images\\starbackground.png"), 0, 0, w, 200);
    }
}
