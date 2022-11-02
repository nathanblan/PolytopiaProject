import javafx.application.Application;
import javafx.application.Platform;
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
import java.io.*;
import java.util.*;

/**
 * Use to visualize the map made by MapGenerator.
 */
public class Display extends Application
{
    private static double sceneWidth = 800;
    private static double sceneHeight = 800;
    private static int SIZE = 16;

    private Tile[][] map;

    private int curX = 0;
    private int curY = 0;

    private int curSelectedX = -1;
    private int curSelectedY = -1;
    private int curLayer = 0; // troops are 1, tiles are 2
    private boolean isConfirmScreen = false;
    private ActionButton curButton;

    private static Player[] players;
    private static int curPlayer;
    private static int NUM_PLAYERS;
    private static int curTurn;

    private static Canvas[] fog;
    
    private static final Group root = new Group();

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Polytopia");

        Canvas mapLayer = new Canvas(sceneWidth+200, sceneHeight);
        GraphicsContext gc = mapLayer.getGraphicsContext2D();

        createNewGame();
        //loadSave(1);
        DisplayUtility.drawTileMap(gc, map);

        Canvas transition = new Canvas(sceneWidth+200, sceneHeight);
        gc = transition.getGraphicsContext2D();
        gc.drawImage(new Image("images\\startscreen.png"), 0, 0, sceneWidth+200, sceneHeight);

        Canvas overlay = new Canvas(sceneWidth+200, sceneHeight);
        Canvas treeLayer = new Canvas(sceneWidth+200, sceneHeight);

        root.getChildren().add(mapLayer);
        root.getChildren().add(transition);
        root.getChildren().add(overlay);
        root.getChildren().add(treeLayer);
        stage.setScene(new Scene(root));
        stage.show();

        overlay.toBack();
        mapLayer.toBack();
        fog[curPlayer].toFront();
        for (int i = 0; i < NUM_PLAYERS; i++)
        {
            if (i != curPlayer)
                fog[i].toBack();
        }
        transition.toFront();
        takeUserInput(mapLayer, overlay, treeLayer, transition);
    }

    private void takeUserInput(Canvas mapLayer, Canvas overlay, Canvas treeLayer, Canvas transition)
    {
        for (Canvas f : fog)
        {
            f.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent e)
                {
                    handleRegularActions(e, mapLayer, overlay, transition, treeLayer);
                }
            });
        }

        transition.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                transition.toBack();
                DisplayUtility.drawRegularScreen(overlay.getGraphicsContext2D(), players[curPlayer]);
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
                    DisplayUtility.showStars(gc, players[curPlayer]);

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
                        DisplayUtility.showStars(gc, players[curPlayer]);
                        DisplayUtility.showXBtn(gc);
                        isConfirmScreen = false;
                    }
                    else if (temp == 0)
                    {
                        t.showTechTree(gc);
                        DisplayUtility.showStars(gc, players[curPlayer]);
                        DisplayUtility.showXBtn(gc);
                        isConfirmScreen = false;
                    }
                }
                else
                {
                    if (CalcUtility.getConfirmButton(x, y) == 1)
                    {
                        treeLayer.toBack();
                        DisplayUtility.drawRegularScreen(overlay.getGraphicsContext2D(), players[curPlayer]);
                    }
                }
            }
        });
    }

    private void handleRegularActions(MouseEvent e, Canvas mapLayer, Canvas overlay, Canvas transition, Canvas treeLayer)
    {
        int x = (int)(e.getX()/Tile.TILE_SIZE);
        int y = (int)(e.getY()/Tile.TILE_SIZE);

        GraphicsContext mapGC = mapLayer.getGraphicsContext2D();
        GraphicsContext overlayGC = overlay.getGraphicsContext2D();

        // clicked in the map
        if (x < SIZE && y < SIZE)
        {
            if (players[curPlayer].fogMap[x][y])
            {
                isConfirmScreen = false;
    
                // clear side panel and selected tile
                if (curSelectedX != -1 && curSelectedY != -1)
                {
                    DisplayUtility.clearSide(overlayGC, players[curPlayer]);
                    if (curLayer == 1)
                    {
                        DisplayUtility.clearMovableTiles(overlayGC, map, curSelectedX, curSelectedY);
                        DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
                    }
                    else if (curLayer == 2)
                    {
                        map[curSelectedX][curSelectedY].drawTile(mapGC, curSelectedX, curSelectedY);
                        DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
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
                        DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
                        curSelectedX = -1;
                        curSelectedY = -1;
                    }
                }
                else
                {
                    if (curSelectedX != -1 && curSelectedY != -1)
                        actionDone = selectTroopMovement(mapGC, overlayGC, x, y);
    
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
                        selectedTile(overlayGC, x, y);
                    else if (curLayer == 1)
                        selectedTroop(overlayGC, x, y);
                }
            }
        }
        // clicked on side panel
        else
        {
            x = (int)e.getX();
            y = (int)e.getY();

            if (isConfirmScreen)
                selectConfirmAction(mapGC, overlay, x, y);
            else if (curLayer == 2)
                selectTileAction(mapGC, overlayGC, x, y);
            else if (curLayer == 1)
                selectTroopAction(mapGC, overlayGC, x, y);
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

    private boolean selectTroopMovement(GraphicsContext mapGC, GraphicsContext overlayGC, int x, int y)
    {
        Troop t = Player.troopMap[curSelectedX][curSelectedY];
        // check if attacked troop
        if (curLayer == 1 && t.getLastAttackTurn() < curTurn && t.getPlayer() == players[curPlayer])
        {
            ArrayList<Coord> attackable = CalcUtility.getAttackableTiles(curSelectedX, curSelectedY);
            for (Coord c : attackable)
            {
                if (c.x == x && c.y == y)
                {
                    Troop other = Player.troopMap[x][y];
                    t.attack(other);

                    if (other == Player.troopMap[x][y] || t.getRange() > 1)
                        t.updateLastAttackTurn(curTurn);
                    t.updateLastMoveTurn(curTurn);
                    
                    String s = map[x][y].getInfo();
                    
                    if (Player.troopMap[x][y] == t && t.getRange() == 1 && !map[x][y].isWater())
                    {
                        // take other troop's location
                        new Thread(() -> {
                            CalcUtility.wait(250);
                            if (map[x][y].getInfo().equals("mountain"))
                                Platform.runLater(() -> DisplayUtility.clearTroopFog(fog[curPlayer], x, y, 2, players[curPlayer]));
                            else
                                Platform.runLater(() -> DisplayUtility.clearTroopFog(fog[curPlayer], x, y, 1, players[curPlayer]));
                        }).start();

                        attackable = CalcUtility.getAttackableTiles(x, y);
                        // nothing can be attacked, deselect troop
                        if (attackable.size() == 0 && t.getLastAttackTurn() < curTurn)
                        {
                            curSelectedX = -1;
                            curSelectedY = -1;
                            curLayer = 0;
                            DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
                        }
                        else
                        {
                            curSelectedX = x;
                            curSelectedY = y;
                            DisplayUtility.clearSide(overlayGC, players[curPlayer]);
                            DisplayUtility.showType(overlayGC, t);
                            DisplayUtility.showAttackableTiles(overlayGC, x, y);
                            DisplayUtility.showSelectedTroop(overlayGC, x, y);
                        }
                    }
                    else
                    {
                        curSelectedX = -1;
                        curSelectedY = -1;
                        curLayer = 0;
                        DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
                    }

                    return true;
                }
            }
        }
        
        if (curLayer == 1 && t.getLastMoveTurn() < curTurn && t.getPlayer() == players[curPlayer])
        {
            // check if selected troop movement
            ArrayList<Coord> movable = CalcUtility.getMovableTiles(map, curSelectedX, curSelectedY);
            for (Coord c : movable)
            {
                if (c.x == x && c.y == y)
                {
                    t.moveTo(x, y);
                    
                    if (map[x][y].getInfo().equals("water") && ((Water)map[x][y]).hasPort() && t.getShipLevel() == 0)
                        t.upgradeShip();
                    else if (!map[x][y].isWater() && t.getShipLevel() > 0)
                        t.destroyShip();
                    
                    new Thread(() -> {
                        CalcUtility.wait(250);
                        
                        if (map[x][y].getInfo().equals("mountain"))
                            Platform.runLater(() -> DisplayUtility.clearTroopFog(fog[curPlayer], x, y, 2, players[curPlayer]));
                        else
                            Platform.runLater(() -> DisplayUtility.clearTroopFog(fog[curPlayer], x, y, 1, players[curPlayer]));
                    }).start();

                    t.updateLastMoveTurn(curTurn);
                    if (!t.canDash())
                        t.updateLastAttackTurn(curTurn);

                    ArrayList<Coord> attackable = CalcUtility.getAttackableTiles(x, y);
                    // nothing can be attacked, deselect troop
                    if (attackable.size() == 0 || t.getLastAttackTurn() == curTurn)
                    {
                        curSelectedX = -1;
                        curSelectedY = -1;
                        curLayer = 0;
                        DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
                    }
                    else
                    {
                        curSelectedX = x;
                        curSelectedY = y;
                        DisplayUtility.clearSide(overlayGC, players[curPlayer]);
                        DisplayUtility.showType(overlayGC, t);
                        DisplayUtility.showAttackableTiles(overlayGC, x, y);
                        DisplayUtility.showXBtn(overlayGC);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private void selectConfirmAction(GraphicsContext mapGC, Canvas overlay, int x, int y)
    {
        int temp = CalcUtility.getConfirmButton(x, y);
        GraphicsContext overlayGC = overlay.getGraphicsContext2D();
        if (temp == 1 && curButton.canDoAction(players[curPlayer]))
        {
            // do action
            Tile t = map[curSelectedX][curSelectedY];
            temp = curButton.doAction(t, curSelectedX, curSelectedY, curTurn);
            
            if (temp == 1)
            {
                players[curPlayer].decStars(20);
                map[curSelectedX][curSelectedY] = new Field(false);
                map[curSelectedX][curSelectedY].setCity(t.getCity());
                t = map[curSelectedX][curSelectedY];
            }
            else if (temp == 2)
            {
                players[curPlayer].decStars(20);
                map[curSelectedX][curSelectedY] = new City(curSelectedX, curSelectedY);
                ((City)map[curSelectedX][curSelectedY]).setPlayer(players[curPlayer], map);
                t = map[curSelectedX][curSelectedY];
            }
            else if (temp == 3)
            {
                //root.getChildren().add(Player.troopMap[curSelectedX][curSelectedY]);
                overlay.toFront();
                fog[curPlayer].toFront();
            }

            // update tile graphics + clear side panel
            t.drawTile(mapGC, curSelectedX, curSelectedY);
            DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
            DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
            
            if (t.getCity() != null)
                t.getCity().drawTile(mapGC, curSelectedX, curSelectedY);

            // clear variables
            curSelectedX = -1;
            curSelectedY = -1;
            curLayer = 0;
            isConfirmScreen = false;
        }
        else if (temp == 0)
        {
            DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
            DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);

            // clear variables
            curSelectedX = -1;
            curSelectedY = -1;
            curLayer = 0;
            isConfirmScreen = false;
        }
    }

    private void selectTroopAction(GraphicsContext mapGC, GraphicsContext overlayGC, int x, int y)
    {
        if (CalcUtility.getConfirmButton(x, y) == 1)
        {
            // exit side panel
            DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
            
            DisplayUtility.clearMovableTiles(overlayGC, map, curSelectedX, curSelectedY);
            DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
    
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
                    ((City)map[curSelectedX][curSelectedY]).setPlayer(players[curPlayer], map);
                    map[curSelectedX][curSelectedY].drawTile(mapGC, curSelectedX, curSelectedY);
                }
                t.updateLastMoveTurn(curTurn);
                t.updateLastAttackTurn(curTurn);

                DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);
                DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
                DisplayUtility.clearMovableTiles(overlayGC, map, curSelectedX, curSelectedY);
        
                curLayer = 0;
                curSelectedX = -1;
                curSelectedY = -1;
            }
        }
    }

    private void selectTileAction(GraphicsContext mapGC, GraphicsContext overlayGC, int x, int y)
    {
        if (CalcUtility.getConfirmButton(x, y) == 1)
        {
            // exit side panel
            DisplayUtility.clearTile(overlayGC, curSelectedX, curSelectedY);
            DisplayUtility.drawRegularScreen(overlayGC, players[curPlayer]);

            curLayer = 0;
            curSelectedX = -1;
            curSelectedY = -1;
        }
        else
        {
            Tile t = map[curSelectedX][curSelectedY];
            ArrayList<ActionButton> actions = getActionButtons(curSelectedX, curSelectedY);
            int index = CalcUtility.getButtonIndex(x, y);
            if (index < actions.size() && index != -1)
            {
                isConfirmScreen = true;
                curButton = actions.get(index);

                DisplayUtility.drawConfirmScreen(overlayGC, players[curPlayer], curButton.canDoAction(players[curPlayer]));
                DisplayUtility.showType(overlayGC, t);

                curButton.displayInfo(overlayGC, sceneWidth);
            }
        }
    }

    private void selectedTroop(GraphicsContext overlayGC, int x, int y)
    {
        Troop t = Player.troopMap[x][y];
        DisplayUtility.clearSide(overlayGC, players[curPlayer]);
        DisplayUtility.showType(overlayGC, t);
        DisplayUtility.showXBtn(overlayGC);

        DisplayUtility.showSelectedTroop(overlayGC, x, y);

        if (t.getPlayer() == players[curPlayer])
        {
            // show movable locations
            if (t.getLastMoveTurn() < curTurn)
            {
                DisplayUtility.showMovableTiles(overlayGC, map, x, y);
                DisplayUtility.drawActionButtons(overlayGC, getActionButtons(t, x, y), players[curPlayer]);
            }
            if (t.getLastAttackTurn() < curTurn)
            {
                DisplayUtility.showAttackableTiles(overlayGC, x, y);
            }
        }
    }

    private void selectedTile(GraphicsContext overlayGC, int x, int y)
    {
        Tile t = map[x][y];
        ArrayList<ActionButton> actions = getActionButtons(x, y);

        // display tile type
        DisplayUtility.clearSide(overlayGC, players[curPlayer]);
        DisplayUtility.showType(overlayGC, t);
        if (t.getInfo().substring(0, 4).equals("city") && t.getPlayer() == players[curPlayer])
            ((City)t).drawPopulation(overlayGC, sceneWidth);

        // show selected tile
        if (actions.size() == 0)
            DisplayUtility.showSelectedTile(overlayGC, Color.GAINSBORO, x, y);
        else
            DisplayUtility.showSelectedTile(overlayGC, Color.LIGHTBLUE, x, y);

        // draw action buttons
        DisplayUtility.drawActionButtons(overlayGC, actions, players[curPlayer]);

        DisplayUtility.showXBtn(overlayGC);
    }

    private void endTurn(Canvas transition)
    {
        players[curPlayer].startTurn();
        fog[curPlayer].toBack();
        curPlayer++;
        curPlayer %= NUM_PLAYERS;
        curTurn++;
        fog[curPlayer].toFront();

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
        DisplayUtility.showStars(gc, players[curPlayer]);
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
        if (t.canUpgradeShip())
            actions.add(ActionButton.upgrade);

        return actions;
    }

    private ArrayList<ActionButton> getActionButtons(int x, int y)
    {
        ArrayList<ActionButton> actions = new ArrayList<ActionButton>();
        
        Tile t = map[x][y];
        if (t.getPlayer() == null && Player.troopMap[x][y] != null &&
            Player.troopMap[x][y].getPlayer() == players[curPlayer] && players[curPlayer].getStars() >= 20
            && !t.getInfo().equals("water") && !t.getInfo().equals("mountain") && !t.getInfo().equals("deep water")
            && players[curPlayer].getTree().getCityBuilding())
        {
            actions.add(ActionButton.buildCity);
            return actions;
        }
        
        if (t.getPlayer() != players[curPlayer])
            return actions;

        TechTree tree = players[curPlayer].getTree();
        String type = t.getInfo();
        if (type.equals("mountain"))
        {
            if (((Mountain)t).canGrabGold() && tree.getClimbing())
                actions.add(ActionButton.pickGold);
            if (((Mountain)t).canBuildMine() && tree.getMining())
                actions.add(ActionButton.buildMine);
            if (tree.getMountainDestroyer() && players[curPlayer].getStars() >= 20)
                actions.add(ActionButton.destroyMountain);
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
                actions.add(ActionButton.buildPort);
        }
        else if (type.equals("grass"))
        {
            if (((Grass)t).canBuildFarm() && tree.getFarming())
                actions.add(ActionButton.buildFarm);
        }
        else if (type.substring(0,4).equals("city") && Player.troopMap[x][y] == null) 
        {
            actions.add(ActionButton.trainWarrior);
            if (tree.getRiding())
                actions.add(ActionButton.trainRider);
            if (tree.getArchery())
                actions.add(ActionButton.trainArcher);
            if (tree.getShields())
                actions.add(ActionButton.trainShield);
        }
        
        return actions;
    }

    
    
    public void saveGame(int saveNumber)
    {
        try
        {
            FileWriter saveFile = new FileWriter("saves\\saveFile"+saveNumber+".txt");

            // save game to file
            
            // save players
            saveFile.write(SIZE+"\n");
            saveFile.write(NUM_PLAYERS+"\n");
            saveFile.write(curPlayer+"\n");
            saveFile.write(curTurn+"\n");
            for (int i = 0; i < NUM_PLAYERS; i++)
            {
                Player p = players[i];
                
                // save tech tree
                saveFile.write(p.getTree()+"\n");
                
                // save stars
                saveFile.write(p.getStars()+"\n");
                
                // save fogMap
                for (int x = 0; x < SIZE; x++)
                {
                    for (int y = 0; y < SIZE; y++)
                    {
                        if (p.fogMap[x][y])
                            saveFile.write("1");
                        else
                            saveFile.write("0");
                    }
                    saveFile.write("\n");
                }
            }
            
            // save troops
            for (Troop[] troopCol : Player.troopMap)
            {
                for (Troop t : troopCol)
                {
                    if (t != null)
                        saveFile.write(t+"\n");
                }
            }
            
            saveFile.write("<t>\n");
            
            // save map
            for (int x = 0; x < SIZE; x++)
            {
                for (int y = 0; y < SIZE; y++)
                {
                    saveFile.write(map[x][y]+"\n");
                }
            }

            saveFile.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public static Player getPlayer(int i)
    {
        return players[i];
    }
    
    private void loadSave(int saveNumber)
    {
        try {
            Scanner saveFile = new Scanner(new File("saves\\saveFile"+saveNumber+".txt"));
            SIZE = Integer.valueOf(saveFile.nextLine());
            NUM_PLAYERS = Integer.valueOf(saveFile.nextLine());
            curPlayer = Integer.valueOf(saveFile.nextLine());
            curTurn = Integer.valueOf(saveFile.nextLine());
            
            players = new Player[NUM_PLAYERS];
            fog = new Canvas[NUM_PLAYERS];
            for (int n = 0; n < NUM_PLAYERS; n++)
            {
                players[n] = new Player(n, new TechTree(saveFile.nextLine()), Integer.parseInt(saveFile.nextLine()));
                
                fog[n] = new Canvas(sceneWidth+200, sceneHeight);
                DisplayUtility.fillFog(fog[n]);
                root.getChildren().add(fog[n]);
                for (int x = 0; x < SIZE; x++)
                {
                    String data = saveFile.nextLine();
                    for (int y = 0; y < SIZE; y++)
                    {
                        if (data.charAt(y)=='1')
                        {
                            DisplayUtility.clearFog(fog[n], x, y);
                            players[n].fogMap[x][y] = true;
                        }
                        else
                            players[n].fogMap[x][y] = false;
                    }
                }
            }
            
            String data = saveFile.nextLine();
            while (!data.equals("<t>"))
            {
                //.getChildren().add(Troop.loadTroop(data));
                data = saveFile.nextLine();
            }
            
            map = new Tile[SIZE][SIZE];
            int x = 0;
            int y = 0;
            ArrayList<City> cities = new ArrayList<City>();
            while (saveFile.hasNextLine())
            {
                data = saveFile.nextLine();
                Tile t = Tile.loadTile(data);
                map[x][y] = t;
                y++;
                if (y >= 16)
                {
                    y %= 16;
                    x++;
                }
                
                if (t.getInfo().charAt(0) == 'c')
                    cities.add((City)t);
            }

            saveFile.close();
            
            for (City c : cities)
            {
                c.claimTiles(map);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    private void createNewGame()
    {
        curTurn = 0;
        curPlayer = 0;
        NUM_PLAYERS = 2;
        players = new Player[NUM_PLAYERS];
        fog = new Canvas[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++)
        {
            players[i] = new Player(i);
            fog[i] = new Canvas(sceneWidth+200, sceneHeight);
            DisplayUtility.fillFog(fog[i]);
            root.getChildren().add(fog[i]);
        }
        
        map = CalcUtility.getTileMap(SIZE);
        
        // starting city
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
        
        Warrior w = new Warrior(players[0], firstX, firstY);
        //root.getChildren().add(w);
        ((City)map[firstX][firstY]).setPlayer(players[0], map);
        Player.troopMap[firstX][firstY] = w;
        DisplayUtility.clearStartingFog(fog[0], firstX, firstY, players[0]);
        
        ((City)map[secondX][secondY]).setPlayer(players[1], map);
        
        w = new Warrior(players[1], secondX, secondY);
        //root.getChildren().add(w);
        Player.troopMap[secondX][secondY] = w;
        DisplayUtility.clearStartingFog(fog[1], secondX, secondY, players[1]);
    }
}