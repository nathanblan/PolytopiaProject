import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.scene.canvas.*;

/**
 * Write a description of JavaFX class TestMapMovement here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TestMapMovement extends Application
{
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Map Tester");
        stage.setMaxHeight(800);
        stage.setMaxWidth(800);
        stage.setResizable(false);
        
        Group root = new Group();
        
        Map map = new Map(16);
        root.getChildren().add(map);
        root.getChildren().add(map.controls());
        
        //Canvas canvas = new Canvas(800, 800);
        //root.getChildren().add(canvas);
        
        

        // Show the Stage (window)
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }
}
