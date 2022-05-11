/**
 * Use to visualize the map made by MapGenerator.
 */

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

public class MapGeneratorVisualisor extends Application {

    private double sceneWidth = 700;
    private double sceneHeight = 700;
    private final int SIZE = 16;
    
    private int curX = 0;
    private int curY = 0;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Polytopia");
        Group root = new Group();
        Canvas canvas = new Canvas(sceneWidth, sceneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Tile[][] map = getTileMap();
        drawTileMap(map, gc, 0, 0, 14, 14);
        
        canvas.setOnKeyPressed(e -> {
            checkKeyPress(e, map, gc);
        });
        
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
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
                    map[r][c] = new Land();
                else if (charMap[r][c] == '+')
                    map[r][c] = new Forest();
                else if (charMap[r][c] == ',')
                    map[r][c] = new Grass();
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
    }
}