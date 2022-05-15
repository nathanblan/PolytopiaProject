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
 * Use to visualize the map made by MapGenerator.
 */
public class MapGeneratorVisualisor extends Application {

    private double sceneWidth = 800;
    private double sceneHeight = 800;
    private final int SIZE = 16;

    private int curX = 0;
    private int curY = 0;

    private int curSelectedX = -1;
    private int curSelectedY = -1;
    private int curLayer = 0; // troops are 1, tiles are 2
    
    private Player[] players;
    private int curPlayer;
    private final int NUM_PLAYERS = 2;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Polytopia");
        Group root = new Group();
        Canvas canvas = new Canvas(sceneWidth+200, sceneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Tile[][] map = getTileMap();
        drawTileMap(map, gc, 0, 0, 16, 16);
        
        players = new Player[NUM_PLAYERS];
        curPlayer = 0;

        takeUserInput(canvas, map);

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void takeUserInput(Canvas canvas, Tile[][] map)
    {
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                int x = (int)(e.getX()/Tile.TILE_SIZE);
                int y = (int)(e.getY()/Tile.TILE_SIZE);
                
                GraphicsContext gc = canvas.getGraphicsContext2D();
                
                // clicked in the map
                if (x < SIZE && y < SIZE)
                {
                    // clear side panel and selected tile
                    if (curSelectedX != -1 && curSelectedY != -1)
                    {
                        map[curSelectedX][curSelectedY].drawTile(gc, curSelectedX, curSelectedY);
                        fillSide(gc);
                    }

                    // selected same tile
                    if (x == curSelectedX && y == curSelectedY)
                    {
                        curLayer++;
                        curLayer %= 3;
                        
                        if (curLayer == 0)
                        {
                            curSelectedX = -1;
                            curSelectedY = -1;
                        }
                    }
                    else
                    {
                        curSelectedX = x;
                        curSelectedY = y;
                        
                        if (Player.troopMap[x][y] != null)
                            curLayer = 1;
                        else
                            curLayer = 2;
                    }

                    // if selected a tile
                    if (curLayer == 2)
                    {

                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.setFont(new Font(35));
                        gc.setFill(Color.LIGHTGREY);
                        
                        Tile t = map[x][y];
                        String type = t.getInfo();
                        ArrayList<ActionButton> actions = getActionButtons(t);

                        gc.fillText(type, sceneWidth+100, 50);
                        
                        if (actions.size() == 0)
                            showSelectedTile(gc, Color.GAINSBORO, x, y);
                        else
                            showSelectedTile(gc, Color.LIGHTBLUE, x, y);
                        
                        // draw action buttons
                        for (int i = 0; i < actions.size(); i++)
                        {
                            gc.drawImage(actions.get(i).getButton(), sceneWidth+20, 80+i*200, 160, 160);
                        }
                    }
                    // if selected a troop
                    else if (curLayer == 1)
                    {
                        
                    }
                }
                // clicked on side panel
                else
                {
                    x = (int)e.getX();
                    y = (int)e.getY();
                    // tile actions
                    if (curLayer == 2)
                    {
                        Tile t = map[curSelectedX][curSelectedY];
                        ArrayList<ActionButton> actions = getActionButtons(t);
                        int index = getButtonIndex(x, y);
                        
                        // clicked on of the buttons
                        if (index < actions.size() && index != -1)
                        {
                            // clear side panel
                            actions.get(index).doAction(t);
                            
                            t.drawTile(gc, curSelectedX, curSelectedY);
                            fillSide(gc);
                            
                            curSelectedX = -1;
                            curSelectedY = -1;
                            curLayer = 0;
                        }
                    }
                    // troop actions
                    else if (curLayer == 1)
                    {
                        
                    }
                    // regular actions
                    else if (curLayer == 0)
                    {
                        
                    }
                }
            }
        });
    }
    
    /**
     * Returns the index of the button clicked, -1 if not clicking a button
     */
    private int getButtonIndex(int x, int y)
    {
        if (x < sceneWidth)
            return -1;
            
        x -= sceneWidth+100;
        y -= 160;
        
        int index = y/200;
        
        y %= 200;
        if (x*x + y*y > 80*80)
            return -1;
        
        return index;
    }
    
    private void showSelectedTile(GraphicsContext gc, Color color, int x, int y)
    {
        gc.setFill(Color.web(color.toString(), 0.75));
        gc.fillRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, 5);
        gc.fillRect(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE+5, 5, Tile.TILE_SIZE-5);
        gc.fillRect(x*Tile.TILE_SIZE+5, y*Tile.TILE_SIZE+Tile.TILE_SIZE-5, Tile.TILE_SIZE-5, 5);
        gc.fillRect(x*Tile.TILE_SIZE+Tile.TILE_SIZE-5, y*Tile.TILE_SIZE+5, 5, Tile.TILE_SIZE-5);
    }
    
    private ArrayList<ActionButton> getActionButtons(Tile t)
    {
        ArrayList<ActionButton> actions = new ArrayList<ActionButton>();
        String type = t.getInfo();
        if (type.equals("mountain"))
        {
            if (((Mountain)t).canGrabGold())
                actions.add(ActionButton.pickGold);
            if (((Mountain)t).canBuildMine())
                actions.add(ActionButton.buildMine);
        }
        else if (type.equals("field"))
        {
            if (((Field)t).canHarvestFruit())
                actions.add(ActionButton.pickBerry);
        }
        else if (type.equals("forest"))
        {
            if (((Forest)t).canHunt())
                actions.add(ActionButton.hunt);
            if (((Forest)t).canBuildHut())
                actions.add(ActionButton.buildHut);
        }
        else if (type.equals("water"))
        {
            if (((Water)t).canFish())
                actions.add(ActionButton.fish);
            if (((Water)t).canBuildPort())
                type = "water"; // placeholder
        }
        else if (type.equals("grass"))
        {
            if (((Grass)t).canBuildFarm())
                type = "grass"; // placeholder
        }
        
        return actions;
    }

    private Tile[][] getTileMap()
    {
        char[][] charMap = MapGenerator.createTerrain(SIZE);
        Tile[][] map = new Tile[SIZE][SIZE];

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
                    map[r][c] = new City();
            }
        }

        return map;
    }

    private void drawTileMap(Tile[][] map, GraphicsContext gc, int startX, int startY, int width, int height)
    {
        for (int r = startX; r < width; r++)
        {
            for (int c = startY; c < height; c++)
            {
                map[r][c].drawTile(gc, r-startX, c-startY);
            }
        }

        fillSide(gc);
    }
    
    private void fillSide(GraphicsContext gc)
    {
        gc.drawImage(new Image("images\\starbackground.png"), sceneWidth, 0, 200, sceneHeight);
    }
}