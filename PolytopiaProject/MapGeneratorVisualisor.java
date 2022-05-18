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
public class MapGeneratorVisualisor extends Application
{
    private double sceneWidth = 800;
    private double sceneHeight = 800;
    private static final int SIZE = 16;
    
    public static Tile[][] map = getTileMap();

    private int curX = 0;
    private int curY = 0;

    private int curSelectedX = -1;
    private int curSelectedY = -1;
    private int curLayer = 0; // troops are 1, tiles are 2
    private boolean isConfirmScreen = false;
    private ActionButton curButton;
    
    private Player[] players;
    private int curPlayer;
    private final int NUM_PLAYERS = 2;

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Polytopia");
        Group root = new Group();
        
        Canvas canvas = new Canvas(sceneWidth+200, sceneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        DisplayUtility.drawTileMap(gc, map);
        
        players = new Player[NUM_PLAYERS];
        
        curPlayer = 0;
        
        Canvas transition = new Canvas(sceneWidth+200, sceneHeight);
        gc = transition.getGraphicsContext2D();
        gc.drawImage(new Image("images\\startscreen.png"), 0, 0, sceneWidth+200, sceneHeight);

        root.getChildren().add(canvas);
        root.getChildren().add(transition);
        stage.setScene(new Scene(root));
        stage.show();
        
        
        transition.toFront();
        takeUserInput(canvas, map, transition);
        
    }

    private void setStartingCity()
    {
        
    }
    
    private void takeUserInput(Canvas canvas, Tile[][] map, Canvas transition)
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
                    isConfirmScreen = false;
                    
                    // clear side panel and selected tile
                    if (curSelectedX != -1 && curSelectedY != -1)
                    {
                        map[curSelectedX][curSelectedY].drawTile(gc, curSelectedX, curSelectedY);
                        DisplayUtility.fillSide(gc);
                    }

                    // selected same tile
                    if (x == curSelectedX && y == curSelectedY)
                    {
                        curLayer++;
                        curLayer %= 3;
                        
                        if (curLayer == 0)
                        {
                            DisplayUtility.drawRegularScreen(gc);
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
                        Tile t = map[x][y];
                        ArrayList<ActionButton> actions = getActionButtons(t);

                        // display tile type
                        DisplayUtility.showType(gc, t);
                        
                        // show selected tile
                        if (actions.size() == 0)
                            DisplayUtility.showSelectedTile(gc, Color.GAINSBORO, x, y);
                        else
                            DisplayUtility.showSelectedTile(gc, Color.LIGHTBLUE, x, y);
                        
                        // draw action buttons
                        for (int i = 0; i < actions.size(); i++)
                        {
                            gc.drawImage(actions.get(i).getButton(), sceneWidth+20, 80+i*200, 160, 160);
                        }
                        
                        gc.drawImage(DisplayUtility.X_BTN, sceneWidth+120, sceneHeight-80, 60, 60);
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
                    
                    // confirmation screen
                    if (isConfirmScreen)
                    {
                        int temp = CalcUtility.getConfirmButton(x, y);
                        if (temp == 1)
                        {
                            // do action
                            Tile t = map[curSelectedX][curSelectedY];
                            curButton.doAction(t);
                            
                            // update tile graphics + clear side panel
                            t.drawTile(gc, curSelectedX, curSelectedY);
                            DisplayUtility.drawRegularScreen(gc);
                            
                            // clear variables
                            curSelectedX = -1;
                            curSelectedY = -1;
                            curLayer = 0;
                            isConfirmScreen = false;
                        }
                        else if (temp == 0)
                        {
                            map[curSelectedX][curSelectedY].drawTile(gc, curSelectedX, curSelectedY);
                            DisplayUtility.drawRegularScreen(gc);
                            
                            // clear variables
                            curSelectedX = -1;
                            curSelectedY = -1;
                            curLayer = 0;
                            isConfirmScreen = false;
                        }
                    }
                    // tile actions
                    else if (curLayer == 2)
                    {
                        Tile t = map[curSelectedX][curSelectedY];
                        ArrayList<ActionButton> actions = getActionButtons(t);
                        int index = CalcUtility.getButtonIndex(x, y);
                        
                        // clicked on of the buttons
                        if (index < actions.size() && index != -1)
                        {
                            isConfirmScreen = true;
                            curButton = actions.get(index);
                            
                            DisplayUtility.drawConfirmScreen(gc);
                            DisplayUtility.showType(gc, t);
                            
                            curButton.displayInfo(gc, sceneWidth);
                        }
                        else
                        {
                            index = CalcUtility.getConfirmButton(x, y);
                            if (index == 1)
                            {
                                // exit side panel
                                map[curSelectedX][curSelectedY].drawTile(gc, curSelectedX, curSelectedY);
                                DisplayUtility.drawRegularScreen(gc);
                        
                                curLayer = 0;
                                curSelectedX = -1;
                                curSelectedY = -1;
                            }
                        }
                    }
                    // troop actions
                    else if (curLayer == 1)
                    {
                        
                    }
                    // regular actions
                    else if (curLayer == 0)
                    {
                        int temp = CalcUtility.getConfirmButton(x, y);
                        
                        // end turn
                        if (temp == 1)
                        {
                            curPlayer++;
                            curPlayer %= NUM_PLAYERS;
                            
                            transition.toFront();
                            gc = transition.getGraphicsContext2D();
                            
                            gc.drawImage(new Image("images\\starbackground.jfif"), 0, 0, 1000, 800);
                            gc.setTextAlign(TextAlignment.CENTER);
                            gc.setFont(new Font(100));
                            gc.setFill(Color.LIGHTGREY);
                            gc.fillText("Player "+(curPlayer+1)+" Turn", sceneWidth/2+100, 400);
                            gc.setFont(new Font(35));
                            gc.fillText("click screen to continue", sceneWidth/2+100, 480);
                        }
                    }
                }
            }
        });
        
        transition.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                canvas.toFront();
            }
        });
    }
    
    private ArrayList<ActionButton> getActionButtons(Tile t)
    {
        if (t.getPlayer() != players[curPlayer])
            return new ArrayList<ActionButton>();
        
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

    private static Tile[][] getTileMap()
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
}