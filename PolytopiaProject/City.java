import javafx.scene.image.Image;
import javafx.scene.canvas.*;
import java.util.ArrayList;
import javafx.scene.paint.*;

/**
 * City buildings
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class City extends Tile
{
    private Player player;
    private int population;
    private int level;
    private final int x;
    private final int y;
    
    /**
     * Constructor for objects of class City
     */
    public City(int x, int y)
    {
        player = null;
        level = 1;
        population = 0;
        this.x=x;
        this.y=y;
        
        updateImage();
    }
    
    public void setPlayer (Player newPlayer, Tile[][] map)
    {
        if (player == null)
        {
            claimTiles(map);
        }
        else
            player.removeCity(this);
        
        newPlayer.addCity(this);
        
        player = newPlayer;
    }
    
    public void claimTiles(Tile[][] map)
    {
        ArrayList<Integer> xValues = new ArrayList<Integer>();
        ArrayList<Integer> yValues = new ArrayList<Integer>();
        int size = map.length;
        
        xValues.add(x);
        if (x != 0)
            xValues.add(x-1);
        if (x != size-1)
            xValues.add(x+1);
        
        yValues.add(y);
        if (y != 0)
            yValues.add(y-1);
        if (y != size-1)
            yValues.add(y+1);
            
        for (int a : xValues)
        {
            for (int b : yValues)
            {
                if (map[a][b].getCity() == null)
                    map[a][b].setCity(this);
            }
            }
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void incPopulation(int num)
    {
        population += num;
        if (population > level)
        {
            level++;
            population = population%level;
        }
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void drawTile(GraphicsContext gc)
    {
        if (player == null)
            gc.drawImage(new Image("images\\village.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else if (player.getPlayerNum() == 0) // player 1 city
            gc.drawImage(new Image("images\\city.png"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else // player 2 city
            gc.drawImage(new Image("images\\city.png"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            
        if (player != null)
            drawPopulation(gc, x, y);
    }
    
    public void updateImage()
    {
        if (player == null)
            setImage(new Image("images\\village.jpg"));
        else if (player.getPlayerNum() == 0) // player 1 city
            setImage(new Image("images\\city.png"));
        else // player 2 city
            setImage(new Image("images\\city.png"));
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        drawTile(gc);
    }
    
    protected void drawPopulation(GraphicsContext gc, int x, int y)
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(x*TILE_SIZE+5, y*TILE_SIZE+40, 40, 5);
        gc.setFill(Color.TAN);
        gc.fillRect(x*TILE_SIZE+5, y*TILE_SIZE+40, 40.0*population/(level+1), 5);
        gc.setFill(Color.BLACK);
        for (int i = 1; i <= level; i++)
        {
            gc.fillRect(x*TILE_SIZE+5+40.0*i/(level+1)-0.5, y*TILE_SIZE+40, 1, 5);
        }
    }
    
    public void drawPopulation(GraphicsContext gc, double w)
    {
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(w+50, 75, 100, 13, 6, 5);
        gc.setFill(Color.TAN);
        gc.fillRoundRect(w+50, 75, 100.0*population/(level+1), 13, 6, 5);
        if (population > 0)
            gc.fillRect(w+56, 75, 100.0*population/(level+1)-6, 13);
        gc.setFill(Color.BLACK);
        for (int i = 1; i <= level; i++)
        {
            gc.fillRect(w+50+100.0*i/(level+1)-1, 75, 2, 13);
        }
    }
    
    public String getInfo()
    {
        if (player == null)
            return "village";
        return "city lvl "+level;
    }
    
    public void trainTroop(Troop type)
    {
        Player.troopMap[x][y] = type;
    }
    
    public String toString()
    {
        if (player == null)
            return "v("+x+","+y+")";
        
        String output = "c"+"("+x+","+y+")"+player.getPlayerNum()+".";
        output += population+"/"+level;
            
        return output;
    }
    
    public static Tile loadTile(String save)
    {
        int x = Integer.parseInt(save.substring(save.indexOf('(')+1, save.indexOf(',')));
        int y = Integer.parseInt(save.substring(save.indexOf(',')+1, save.indexOf(')')));
        if (save.charAt(0)=='v')
            return new City(x,y);
        
        City c = new City(x,y);
        c.player = Display.getPlayer(Integer.parseInt(save.substring(save.indexOf(')')+1,save.indexOf('.'))));
        Display.getPlayer(Integer.parseInt(save.substring(save.indexOf(')')+1,save.indexOf('.')))).addCity(c);
        c.population = Integer.parseInt(save.substring(save.indexOf('.')+1,save.indexOf('/')));
        c.level = Integer.parseInt(save.substring(save.indexOf('/')+1));
        return c;
    }
}
