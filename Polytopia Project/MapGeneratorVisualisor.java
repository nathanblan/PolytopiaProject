/**
 * 
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
    private final int SIZE = 14;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Polytopia");
        Group root = new Group();
        Canvas canvas = new Canvas(sceneWidth, sceneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
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
                    
                map[r][c].drawTile(gc, r, c);
                System.out.print(charMap[r][c]+" ");
            }
            System.out.println();
        }
        
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }
}