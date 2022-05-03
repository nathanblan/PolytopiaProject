/**
 * I DONT KNOW HOW THIS WORKS. ACTUALLY IT DOESNT. PLEASE MAKE THIS WORK LOL.
 * Use to visualize the map made by MapGenerator.
 */


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MapGeneratorVisualisor extends Application {

    private double sceneWidth = 1024;
    private double sceneHeight = 768;

    private int n = 10;
    private int m = 10;

    double gridWidth = sceneWidth / n;
    double gridHeight = sceneHeight / m;

    MyNode[][] playfield = new MyNode[n][m];

    @Override
    public void start(Stage primaryStage) {


        Group root = new Group();

        // initialize playfield
        for( int i=0; i < n; i++) {
            for( int j=0; j < m; j++) {

                // create node
                MyNode node = new MyNode( "Item " + i + "/" + j, i * gridWidth, j * gridHeight, gridWidth, gridHeight);

                // add node to group
                root.getChildren().add( node);

                // add to playfield for further reference using an array
                playfield[i][j] = node;

            }
        }


        Scene scene = new Scene( root, sceneWidth, sceneHeight);

        primaryStage.setScene( scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class MyNode extends StackPane {

        public MyNode( String name, double x, double y, double width, double height) {

            // create rectangle
            Rectangle rectangle = new Rectangle( width, height);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.LIGHTBLUE);

            // create label
            Label label = new Label( name);

            // set position
            setTranslateX( x);
            setTranslateY( y);

            getChildren().addAll( rectangle, label);

        }

    }

}