/**
 * Write a description of class ActionButton here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;

public class ActionButton
{
    private String type;
    private String info;
    
    
    /**
     * Constructor for objects of class ActionButton
     */
    public ActionButton(String t)
    {
        type = t;
        
        // set info
        if (type == "animal")
            info = "Hunts an animal. Costs 2 stars, gains 1 population.";
        else if (type == "berry")
            info = "Picks berries. Costs 2 stars, gains 1 population.";
        else if (type == "fish")
            info = "Fish some fish. Costs 2 stars, gains 1 population.";
        
    }
    
    public Image getButton()
    {
        return new Image("images\\"+type+"_button.jpg");
    }
    
    public void displayInfo(GraphicsContext gc, int width)
    {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(20));
        gc.setFill(Color.LIGHTGREY);
        
        gc.fillText(info, width+100, 100, 180);
    }
}
