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
public class Display extends Application
{
    private double sceneWidth = 800;
    private double sceneHeight = 800;
    private static final int SIZE = 16;
    
    private Tile[][] map = getTileMap();

    private int curX = 0;
    private int curY = 0;

    private int curSelectedX = -1;
    private int curSelectedY = -1;
    private int curLayer = 0; // troops are 1, tiles are 2
    private boolean isConfirmScreen = false;
    private ActionButton curButton;
    
    private Player[] players;
    private int curPlayer = 0;
    private final int NUM_PLAYERS = 2;
    private int curTurn = 0;

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Polytopia");
        Group root = new Group();
        
        Canvas mapLayer = new Canvas(sceneWidth+200, sceneHeight);
        GraphicsContext gc = mapLayer.getGraphicsContext2D();

        players = new Player[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++)
        {
            players[i] = new Player(i);
        }
        setStartingCity();
        DisplayUtility.drawTileMap(gc, map);
        
        Canvas transition = new Canvas(sceneWidth+200, sceneHeight);
        gc = transition.getGraphicsContext2D();
        gc.drawImage(new Image("images\\startscreen.png"), 0, 0, sceneWidth+200, sceneHeight);
        
        Canvas troopLayer = new Canvas(sceneWidth+200, sceneHeight);
        DisplayUtility.drawTroops(Player.troopMap, troopLayer.getGraphicsContext2D());
        
        Canvas treeLayer = new Canvas(sceneWidth+200, sceneHeight);

        root.getChildren().add(mapLayer);
        root.getChildren().add(transition);
        root.getChildren().add(troopLayer);
        root.getChildren().add(treeLayer);
        stage.setScene(new Scene(root));
        stage.show();
        
        mapLayer.toFront();
        troopLayer.toFront();
        transition.toFront();
        takeUserInput(mapLayer, troopLayer, treeLayer, transition);
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
                if (map[j][i].getInfo().equals("village"))
                {
                    firstX = j;
                    firstY = i;
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
        ((City)map[firstX][firstY]).setPlayer(players[0], map, firstX, firstY);
        Player.troopMap[firstX][firstY] = new Warrior(players[0]);
        ((City)map[secondX][secondY]).setPlayer(players[1], map, secondX, secondY);
        Player.troopMap[secondX][secondY] = new Warrior(players[1]);
    }
    
    private void takeUserInput(Canvas mapLayer, Canvas troopLayer, Canvas treeLayer, Canvas transition)
    {
        troopLayer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                int x = (int)(e.getX()/Tile.TILE_SIZE);
                int y = (int)(e.getY()/Tile.TILE_SIZE);
                
                GraphicsContext mapGC = mapLayer.getGraphicsContext2D();
                GraphicsContext troopGC = troopLayer.getGraphicsContext2D();
                
                // clicked in the map
                if (x < SIZE && y < SIZE)
                {
                    isConfirmScreen = false;
                    
                    // clear side panel and selected tile
                    if (curSelectedX != -1 && curSelectedY != -1)
                    {
                        DisplayUtility.fillSide(mapGC, players[curPlayer].getStars());
                        if (curLayer == 1)
                        {
                            DisplayUtility.clearMovableTiles(troopGC, map, curSelectedX, curSelectedY);
                            DisplayUtility.clearTile(troopGC, curSelectedX, curSelectedY);
                            Player.troopMap[curSelectedX][curSelectedY].drawTroop(troopGC, curSelectedX, curSelectedY);
                        }
                        else if (curLayer == 2)
                        {
                            map[curSelectedX][curSelectedY].drawTile(mapGC, curSelectedX, curSelectedY);
                        }
                    }
                    
                    boolean actionDone = false;
                    
                    // selected same tile
                    if (x == curSelectedX && y == curSelectedY)
                    {
                        curLayer++;
                        curLayer %= 3;
                        
                        if (curLayer == 0)
                        {
                            DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
                            curSelectedX = -1;
                            curSelectedY = -1;
                        }
                    }
                    else
                    {
                        if (curSelectedX != -1 && curSelectedY != -1)
                            actionDone = selectTroopMovement(mapGC, troopGC, x, y);
                        
                        // did not select a troop movement/attack tile
                        if (!actionDone)
                        {
                            curSelectedX = x;
                            curSelectedY = y;
                            
                            if (Player.troopMap[x][y] != null)
                                curLayer = 1;
                            else
                                curLayer = 2;
                        }
                    }

                    if (!actionDone)
                    {
                        if (curLayer == 2)
                            selectedTile(mapGC, x, y);
                        else if (curLayer == 1)
                            selectedTroop(mapGC, troopGC, x, y);
                    }
                }
                // clicked on side panel
                else
                {
                    x = (int)e.getX();
                    y = (int)e.getY();
                    
                    if (isConfirmScreen)
                        selectConfirmAction(mapGC, x, y);
                    else if (curLayer == 2)
                        selectTileAction(mapGC, x, y);
                    else if (curLayer == 1)
                        selectTroopAction(mapGC, troopGC, x, y);
                    // regular actions
                    else if (curLayer == 0)
                    {
                        int temp = CalcUtility.getConfirmButton(x, y);
                        
                        if (temp == 1)
                            endTurn(transition);
                        else if (temp == 0)
                            showTree(treeLayer);
                    }
                }
            }
        });
        
        transition.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                transition.toBack();
                DisplayUtility.drawRegularScreen(mapLayer.getGraphicsContext2D(), players[curPlayer].getStars());
            }
        });
        
        treeLayer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                double x = e.getX();
                double y = e.getY();
                TechTree t = players[curPlayer].getTree();
                GraphicsContext gc = treeLayer.getGraphicsContext2D();
                
                ActionButton btn = t.getActionButton(x, y);
                if (btn != null)
                {
                    t.showTechTree(gc);
                    DisplayUtility.showStars(gc, players[curPlayer].getStars());
                    
                    curButton = btn;
                    btn.displayInfo(gc, sceneWidth);
                    if (btn.canDoAction(players[curPlayer]))
                        gc.drawImage(DisplayUtility.CHECK_BTN, sceneWidth+120, sceneHeight-80, 60, 60);
                    gc.drawImage(DisplayUtility.X_BTN, sceneWidth+20, sceneHeight-80, 60, 60);
                    
                    isConfirmScreen = true;
                }
                else if (isConfirmScreen)
                {
                    int temp = CalcUtility.getConfirmButton(x, y);
                    if (temp == 1 && curButton.canDoAction(players[curPlayer]))
                    {
                        curButton.doAction(players[curPlayer]);
                        t.showTechTree(gc);
                        DisplayUtility.showStars(gc, players[curPlayer].getStars());
                        DisplayUtility.showXBtn(gc);
                        isConfirmScreen = false;
                    }
                    else if (temp == 0)
                    {
                        t.showTechTree(gc);
                        DisplayUtility.showStars(gc, players[curPlayer].getStars());
                        DisplayUtility.showXBtn(gc);
                        isConfirmScreen = false;
                    }
                }
                else
                {
                    if (CalcUtility.getConfirmButton(x, y) == 1)
                    {
                        treeLayer.toBack();
                        DisplayUtility.drawRegularScreen(mapLayer.getGraphicsContext2D(), players[curPlayer].getStars());
                    }
                }
            }
        });
    }
    
    private boolean selectTroopMovement(GraphicsContext mapGC, GraphicsContext troopGC, int x, int y)
    {
        Troop t = Player.troopMap[curSelectedX][curSelectedY];
        if (curLayer == 1 && t.getLastMoveTurn() < curTurn && t.getPlayer() == players[curPlayer])
        {
            // check if selected troop movement
            ArrayList<Coord> movable = CalcUtility.getMovableTiles(map, curSelectedX, curSelectedY);
            for (Coord c : movable)
            {
                if (c.x == x && c.y == y)
                {
                    DisplayUtility.clearTile(troopGC, curSelectedX, curSelectedY);
                    t.drawTroop(troopGC, x, y);
                    
                    Player.troopMap[curSelectedX][curSelectedY] = null;
                    Player.troopMap[x][y] = t;
                    
                    t.updateLastMoveTurn(curTurn);
                    if (!t.canDash())
                        t.updateLastAttackTurn(curTurn);
                    
                    ArrayList<Coord> attackable = CalcUtility.getAttackableTiles(x, y);
                    // nothing can be attacked, deselect troop
                    if (attackable.size() == 0 && t.getLastAttackTurn() < curTurn)
                    {
                        curSelectedX = -1;
                        curSelectedY = -1;
                        curLayer = 0;
                        DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
                    }
                    else
                    {
                        curSelectedX = x;
                        curSelectedY = y;
                        DisplayUtility.fillSide(mapGC, players[curPlayer].getStars());
                        DisplayUtility.showType(mapGC, t);
                        DisplayUtility.showAttackableTiles(troopGC, x, y);
                    }
                    
                    return true;
                }
            }
        }
        
        // check if attacked troop
        if (curLayer == 1 && t.getLastAttackTurn() < curTurn && t.getPlayer() == players[curPlayer])
        {
            ArrayList<Coord> attackable = CalcUtility.getAttackableTiles(curSelectedX, curSelectedY);
            for (Coord c : attackable)
            {
                if (c.x == x && c.y == y)
                {
                    Troop other = Player.troopMap[x][y];
                    
                    boolean temp = t.attack(other, CalcUtility.getDistance(x, y, curSelectedX, curSelectedY));
                    
                    if (!temp || t.getRange() > 1)
                        t.updateLastAttackTurn(curTurn);
                    t.updateLastMoveTurn(curTurn);
                    
                    if (temp && t.getRange() == 1)
                    {
                        // take other troop's location
                        DisplayUtility.clearTile(troopGC, curSelectedX, curSelectedY);
                        t.drawTroop(troopGC, x, y);
                        
                        Player.troopMap[curSelectedX][curSelectedY] = null;
                        Player.troopMap[x][y] = t;
                        
                        attackable = CalcUtility.getAttackableTiles(x, y);
                        // nothing can be attacked, deselect troop
                        if (attackable.size() == 0 && t.getLastAttackTurn() < curTurn)
                        {
                            curSelectedX = -1;
                            curSelectedY = -1;
                            curLayer = 0;
                            DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
                        }
                        else
                        {
                            curSelectedX = x;
                            curSelectedY = y;
                            DisplayUtility.fillSide(mapGC, players[curPlayer].getStars());
                            DisplayUtility.showType(mapGC, t);
                            DisplayUtility.showAttackableTiles(troopGC, x, y);
                            DisplayUtility.showSelectedTroop(troopGC, x, y);
                        }
                    }
                    else
                    {
                        curSelectedX = -1;
                        curSelectedY = -1;
                        curLayer = 0;
                        DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
                    }
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void selectConfirmAction(GraphicsContext mapGC, int x, int y)
    {
        int temp = CalcUtility.getConfirmButton(x, y);
        if (temp == 1)
        {
            // do action
            Tile t = map[curSelectedX][curSelectedY];
            curButton.doAction(t);
            
            // update tile graphics + clear side panel
            t.drawTile(mapGC, curSelectedX, curSelectedY);
            DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
            
            // clear variables
            curSelectedX = -1;
            curSelectedY = -1;
            curLayer = 0;
            isConfirmScreen = false;
        }
        else if (temp == 0)
        {
            map[curSelectedX][curSelectedY].drawTile(mapGC, curSelectedX, curSelectedY);
            DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
            
            // clear variables
            curSelectedX = -1;
            curSelectedY = -1;
            curLayer = 0;
            isConfirmScreen = false;
        }
    }
    
    private void selectTroopAction(GraphicsContext mapGC, GraphicsContext troopGC, int x, int y)
    {
        if (CalcUtility.getConfirmButton(x, y) == 1)
        {
            // exit side panel
            DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
            
            DisplayUtility.clearMovableTiles(troopGC, map, curSelectedX, curSelectedY);
            DisplayUtility.clearTile(troopGC, curSelectedX, curSelectedY);
            Player.troopMap[curSelectedX][curSelectedY].drawTroop(troopGC, curSelectedX, curSelectedY);
    
            curLayer = 0;
            curSelectedX = -1;
            curSelectedY = -1;
        }
        else
        {
            Troop t = Player.troopMap[curSelectedX][curSelectedY];
            ArrayList<ActionButton> actions = getActionButtons(t,curSelectedX, curSelectedY);
            int index = CalcUtility.getButtonIndex(x, y);
            if (index < actions.size() && index != -1)
            {
                if (actions.get(index).doAction(t))
                {
                    ((City)map[curSelectedX][curSelectedY]).setPlayer(players[curPlayer], map, curSelectedX, curSelectedY);
                    map[curSelectedX][curSelectedY].drawTile(mapGC, curSelectedX, curSelectedY);
                }
                t.updateLastMoveTurn(curTurn);
                t.updateLastAttackTurn(curTurn);
                
                DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
                DisplayUtility.clearTile(troopGC, curSelectedX, curSelectedY);
                DisplayUtility.clearMovableTiles(troopGC, map, curSelectedX, curSelectedY);
                t.drawTroop(troopGC, curSelectedX, curSelectedY);
        
                curLayer = 0;
                curSelectedX = -1;
                curSelectedY = -1;
            }
        }
    }
    
    private void selectTileAction(GraphicsContext mapGC, int x, int y)
    {
        if (CalcUtility.getConfirmButton(x, y) == 1)
        {
            // exit side panel
            map[curSelectedX][curSelectedY].drawTile(mapGC, curSelectedX, curSelectedY);
            DisplayUtility.drawRegularScreen(mapGC, players[curPlayer].getStars());
    
            curLayer = 0;
            curSelectedX = -1;
            curSelectedY = -1;
        }
        else
        {
            Tile t = map[curSelectedX][curSelectedY];
            ArrayList<ActionButton> actions = getActionButtons(t);
            int index = CalcUtility.getButtonIndex(x, y);
            if (index < actions.size() && index != -1)
            {
                isConfirmScreen = true;
                curButton = actions.get(index);
                
                DisplayUtility.drawConfirmScreen(mapGC, players[curPlayer].getStars());
                DisplayUtility.showType(mapGC, t);
                
                curButton.displayInfo(mapGC, sceneWidth);
            }
        }
    }
    
    private void selectedTroop(GraphicsContext mapGC, GraphicsContext troopGC, int x, int y)
    {
        Troop t = Player.troopMap[x][y];
        DisplayUtility.fillSide(mapGC, players[curPlayer].getStars());
        DisplayUtility.showType(mapGC, t);
        DisplayUtility.showXBtn(mapGC);
        
        DisplayUtility.showSelectedTroop(troopGC, x, y);
        
        if (t.getPlayer() == players[curPlayer])
        {
            // show movable locations
            if (t.getLastMoveTurn() < curTurn)
            {
                DisplayUtility.showMovableTiles(troopGC, map, x, y);
                DisplayUtility.drawActionButtons(mapGC, getActionButtons(t, x, y));
            }
            if (t.getLastAttackTurn() < curTurn)
            {
                DisplayUtility.showAttackableTiles(troopGC, x, y);
            }
        }
    }
    
    private void selectedTile(GraphicsContext mapGC, int x, int y)
    {
        Tile t = map[x][y];
        ArrayList<ActionButton> actions = getActionButtons(t);

        // display tile type
        DisplayUtility.fillSide(mapGC, players[curPlayer].getStars());
        DisplayUtility.showType(mapGC, t);
        if (t.getInfo().substring(0, 4).equals("city") && t.getPlayer() == players[curPlayer])
            ((City)t).drawPopulation(mapGC, sceneWidth);
        
        // show selected tile
        if (actions.size() == 0)
            DisplayUtility.showSelectedTile(mapGC, Color.GAINSBORO, x, y);
        else
            DisplayUtility.showSelectedTile(mapGC, Color.LIGHTBLUE, x, y);
        
        // draw action buttons
        DisplayUtility.drawActionButtons(mapGC, actions);
        
        DisplayUtility.showXBtn(mapGC);
    }
    
    private void endTurn(Canvas transition)
    {
        players[curPlayer].startTurn();
        curPlayer++;
        curPlayer %= NUM_PLAYERS;
        curTurn++;
        
        transition.toFront();
        GraphicsContext gc = transition.getGraphicsContext2D();
        
        gc.drawImage(new Image("images\\starbackground.jfif"), 0, 0, 1000, 800);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(100));
        gc.setFill(Color.LIGHTGREY);
        gc.fillText("Player "+(curPlayer+1)+" Turn", sceneWidth/2+100, 400);
        gc.setFont(new Font(35));
        gc.fillText("click screen to continue", sceneWidth/2+100, 480);
    }
    
    private void showTree(Canvas treeLayer)
    {
        treeLayer.toFront();
        GraphicsContext gc = treeLayer.getGraphicsContext2D();
        TechTree t = players[curPlayer].getTree();
        t.showTechTree(gc);
        DisplayUtility.showStars(gc, players[curPlayer].getStars());
        DisplayUtility.showXBtn(gc);
    }
    
    private ArrayList<ActionButton> getActionButtons(Troop t, int x, int y)
    {
        ArrayList<ActionButton> actions = new ArrayList<ActionButton>();
        
        if (t.canHeal())
            actions.add(ActionButton.heal);
        if (t.getLastMoveTurn() < curTurn && t.getLastAttackTurn() < curTurn &&
            (map[x][y].getInfo().equals("village") || (map[x][y].getInfo().substring(0, 4).equals("city")
                && ((City)map[x][y]).getPlayer() != t.getPlayer())))
            actions.add(ActionButton.claimCity);
        
        return actions;
    }
    
    private ArrayList<ActionButton> getActionButtons(Tile t)
    {
        if (t.getPlayer() != players[curPlayer])
            return new ArrayList<ActionButton>();
            
        ArrayList<ActionButton> actions = new ArrayList<ActionButton>();
        TechTree tree = players[curPlayer].getTree();
        String type = t.getInfo();
        if (type.equals("mountain"))
        {
            if (((Mountain)t).canGrabGold() && tree.getClimbing())
                actions.add(ActionButton.pickGold);
            if (((Mountain)t).canBuildMine() && tree.getMining())
                actions.add(ActionButton.buildMine);
        }
        else if (type.equals("field"))
        {
            if (((Field)t).canHarvestFruit())
                actions.add(ActionButton.pickBerry);
        }
        else if (type.equals("forest"))
        {
            if (((Forest)t).canHunt() && tree.getHunting())
                actions.add(ActionButton.hunt);
            if (((Forest)t).canBuildHut() && tree.getForestry())
                actions.add(ActionButton.buildHut);
        }
        else if (type.equals("water"))
        {
            if (((Water)t).canFish() && tree.getFishing())
                actions.add(ActionButton.fish);
            if (((Water)t).canBuildPort() && tree.getSailing())
                type = "water"; // placeholder
        }
        else if (type.equals("grass"))
        {
            if (((Grass)t).canBuildFarm() && tree.getFarming())
                actions.add(ActionButton.buildFarm);
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