/**
 * I DONT KNOW HOW THIS WORKS. ACTUALLY IT DOESNT. PLEASE MAKE THIS WORK LOL.
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

    private double sceneWidth = 1000;
    private double sceneHeight = 1000;
    private final int SIZE = 10;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Polytopia");
        Group root = new Group();
        Canvas canvas = new Canvas(sceneWidth, sceneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        MapGenerator gen = new MapGenerator(null, 4.0f, SIZE, SIZE);
        String[][] stringMap = gen.createTerrain(SIZE);
        Tile[][] map = new Tile[10][10];
        
        for (int r = 0; r < SIZE; r++)
        {
            for (int c = 0; c < SIZE; c++)
            {
                if (stringMap[r][c].equals
            }
        }
        
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }
}