import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.*;
 
public class Animation extends Application
{
    @Override
    public void start (Stage primaryStage) throws Exception
    {
        Canvas c = new Canvas(600, 600);
        Group group = new Group();
 
        Troop.setGraphics(c.getGraphicsContext2D(), group);
        
        //c.getGraphicsContext2D().setFill(Color.LIME);
        Warrior t = new Warrior(new Player(0), 1, 1);
        primaryStage.setScene(new Scene(group, 600, 600));
        primaryStage.show();
        
        group.getChildren().add(c);
        c.toBack();
        c.getGraphicsContext2D().fillRect(0, 0, 600, 600);
        
        
        c.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent e)
            {
                t.animateAttack(2, 2);
            }
        });
    }
    
    
}