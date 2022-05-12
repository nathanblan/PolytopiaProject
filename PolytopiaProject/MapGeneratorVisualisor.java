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

    @Override
    public void start(Stage stage) {
        stage.setTitle("Polytopia");
        Group root = new Group();
        Canvas canvas = new Canvas(sceneWidth+200, sceneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Tile[][] map = getTileMap();
        drawTileMap(map, gc, 0, 0, 16, 16);
        
        /*canvas.setOnKeyPressed(e -> {
            checkKeyPress(e, map, gc);
        });*/
        
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
                
                System.out.println("Pressed tile ("+x+", "+y+") "+map[x][y].getInfo());
                ArrayList<String> actions = new ArrayList<String>();
                
                Tile t = map[x][y];
                String type = t.getInfo();
                if (type.equals("mountain"))
                {
                    if (((Mountain)t).canBuildMine())
                        actions.add("build mine");
                }
                else if (type.equals("field"))
                {
                    if (((Field)t).canHarvestFruit())
                        actions.add("harvest fruit");
                }
                else if (type.equals("forest"))
                {
                    if (((Forest)t).canHunt())
                        actions.add("hunt");
                    if (((Forest)t).canBuildHut())
                        actions.add("build hut");
                }
                else if (type.equals("water"))
                {
                    if (((Water)t).canFish())
                        actions.add("fish");
                    if (((Water)t).canBuildPort())
                        actions.add("build port");
                }
                else if (type.equals("grass"))
                {
                    if (((Grass)t).canBuildFarm())
                        actions.add("build farm");
                }
                
                for (String s : actions)
                {
                    System.out.print(s+" ");
                }
                System.out.println();
                
                // selected same tile
                if (x == curSelectedX && y == curSelectedY)
                {
                    // unselect tile
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    drawTileMap(map, gc, 0, 0, 16, 16);
                }
                else
                {
                    // show selected tile
                }
            }
        });
    }
    
    private void checkKeyPress(KeyEvent e, Tile[][] map, GraphicsContext gc)
    {
        if (e.getCode() == KeyCode.A)
        {
            if (curX > 0)
            {
                curX--;
                drawTileMap(map, gc, curX, curY, 14, 14);
            }
        }
        else if (e.getCode() == KeyCode.D)
        {
            if (curX < SIZE - sceneWidth/Tile.TILE_SIZE)
            {
                curX++;
                drawTileMap(map, gc, curX, curY, 14, 14);
            }
        }
        else if (e.getCode() == KeyCode.W)
        {
            if (curY > 0)
            {
                curY--;
                drawTileMap(map, gc, curX, curY, 14, 14);
            }
        }
        else if (e.getCode() == KeyCode.S)
        {
            if (curY < SIZE - sceneHeight/Tile.TILE_SIZE)
            {
                curY++;
                drawTileMap(map, gc, curX, curY, 14, 14);
            }
        }
    }
    
    private Tile[][] getTileMap()
    {
        MapGenerator gen = new MapGenerator(null, 4.0f, SIZE, SIZE);
        gen.init();
        char[][] charMap = gen.createTerrain(SIZE);
        Tile[][] map = new Tile[SIZE][SIZE];
        
        int numWater = 0;
        int numLand = 0;
        
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
                else if (charMap[r][c] == '-')
                    map[r][c] = new Field();
                else if (charMap[r][c] == '+')
                    map[r][c] = new Forest();
                else if (charMap[r][c] == ',')
                    map[r][c] = new Grass();
                    
                if (charMap[r][c] == '~' || charMap[r][c] == '=')
                    numWater++;
                else
                    numLand++;
            }
        }
        
        // make sure it isn't too much water or land
        double total = SIZE*SIZE;
        if (numWater/total < 0.25 || numLand/total < 0.25)
            return getTileMap();
        
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
        
        gc.setFill(Color.GREY);
        gc.fillRect(sceneWidth, 0, 200, sceneHeight);
    }
}