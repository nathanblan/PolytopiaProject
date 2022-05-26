import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
 
public class Animation extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Rider t = new Rider(new Player(0), 0, 0);   
        t.updateImage();
        Group group = new Group();
 
        t.moveTo(3, 2, 500);
 
        group.getChildren().add(t);
        primaryStage.setScene(new Scene(group, 600, 600));
        primaryStage.show();
    }
}