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
    
    public int p1turn = 0;
    public int p2turn = 0;

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Polytopia");
        Group root = new Group();
        
        Canvas mapLayer = new Canvas(sceneWidth+200, sceneHeight);
        GraphicsContext gc = mapLayer.getGraphicsContext2D();

        players = new Player[NUM_PLAYERS];
        curPlayer = 0;
        for (int i = 0; i < NUM_PLAYERS; i++)
        {
            players[i] = new Player(i);
        }
        setStartingCity();
        DisplayUtility.drawTileMap(gc, map);
        //Player.troopMap[3][3] = new Knight(players[0]);
        
        Canvas transition = new Canvas(sceneWidth+200, sceneHeight);
        gc = transition.getGraphicsContext2D();
        gc.drawImage(new Image("images\\startscreen.png"), 0, 0, sceneWidth+200, sceneHeight);
        
        Canvas troopLayer = new Canvas(sceneWidth+200, sceneHeight);
        DisplayUtility.drawTroops(Player.troopMap, troopLayer.getGraphicsContext2D());

        root.getChildren().add(mapLayer);
        root.getChildren().add(transition);
        root.getChildren().add(troopLayer);
        stage.setScene(new Scene(root));
        stage.show();
        
        troopLayer.toFront();
        transition.toFront();
        takeUserInput(map, mapLayer, troopLayer, transition);
        
    }

    private void setStartingCity()
    {
        int firstX = 0;
        int firstY = 0;
        int secondX = 0;
        int secondY = 0;
        
        for(int i = 0; i<map.length; i++)
        {
            for(int j =0; j<map.length; j++)
            {
                if (map[i][j].getInfo().equals("village"))
                {
                    firstX = i;
                    firstY = j;
                    i=map.length;
                    j=map.length;
                }
            }
        }
        for(int i = map.length-1; i>0; i--)
        {
            for(int j = map.length-1; j>0; j--)
            {
                if (map[j][i].getInfo().equals("village"))
                {
                    secondX = j;
                    secondY = i;
                    i=0;
                    j=0;
                }
            }
        }
        map[firstX][firstY] = new City(players[0]);
        Player.troopMap[firstX][firstY] = new Knight(players[0]);
        map[secondX][secondY] = new City(players[1]);
        Player.troopMap[secondX][secondY] = new Knight(players[1]);
    }
    
    private void takeUserInput(Tile[][] map, Canvas mapLayer, Canvas troopLayer, Canvas transition)
    {
        troopLayer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                int x = (int)(e.getX()/Tile.TILE_SIZE);
                int y = (int)(e.getY()/Tile.TILE_SIZE);
                
                GraphicsContext gc = mapLayer.getGraphicsContext2D();
                
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
                    
                    if (curLayer == 1)
                        DisplayUtility.clearMovableTiles(troopLayer.getGraphicsContext2D(), map, curSelectedX, curSelectedY);

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
                        // check if selected troop movement
                        if (curLayer == 1)
                        {
                            System.out.println("selected troop "+x+","+y);
                            ArrayList<Coord> movable = CalcUtility.getMovableTiles(map, curSelectedX, curSelectedY);
                            
                            for (Coord c : movable)
                            {
                                if (c.x == x && c.y == y)
                                {
                                    gc = troopLayer.getGraphicsContext2D();
                                    Troop t = Player.troopMap[curSelectedX][curSelectedY];
                                    
                                    DisplayUtility.clearTile(gc, curSelectedX, curSelectedY);
                                    t.drawTroop(gc, x, y);
                                    
                                    Player.troopMap[curSelectedX][curSelectedY] = null;
                                    Player.troopMap[x][y] = t;
                                    
                                    Player.troopMap[x][y].move();
                                    
                                    curSelectedX = -1;
                                    curSelectedY = -1;
                                    x = -1;
                                    y = -1;
                                    curLayer = 0;
                                    
                                    DisplayUtility.drawRegularScreen(mapLayer.getGraphicsContext2D());
                                }
                            }
                        }
                        
                        if (x != -1 && y != -1)
                        {
                            curSelectedX = x;
                            curSelectedY = y;
                            
                            if (Player.troopMap[x][y] != null)
                                curLayer = 1;
                            else
                                curLayer = 2;
                        }
                    }

                    // if selected a tile
                    if (curLayer == 2)
                    {
                        System.out.println("selected tile "+x+","+y);
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
                        Troop t = Player.troopMap[x][y];
                        if (t.getPlayer() == players[curPlayer])
                        {
                            // show movable locations
                            gc = troopLayer.getGraphicsContext2D();
                            DisplayUtility.showMovableTiles(gc, map, x, y);
                            
                            // get actions
                            
                            
                        }
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
                            players[curPlayer].endTurn(); // adds one to turn count of the current player
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
                mapLayer.toFront();
                troopLayer.toFront();
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