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
    
    public static final ActionButton pickBerry = new ActionButton("berry");
    public static final ActionButton fish = new ActionButton("fish");
    public static final ActionButton hunt = new ActionButton("animal");
    public static final ActionButton pickGold = new ActionButton("gold");
    public static final ActionButton buildMine = new ActionButton("mine");
    public static final ActionButton buildHut = new ActionButton("lumberhut");
    
    /**
     * Constructor for objects of class ActionButton
     */
    private ActionButton(String t)
    {
        type = t;
        
        // set info
        if (type.equals("animal"))
            info = "Hunts an animal. Costs 2 stars, gains 1 population.";
        else if (type.equals("berry"))
            info = "Picks berries. Costs 2 stars, gains 1 population.";
        else if (type.equals("fish"))
            info = "Fish some fish. Costs 2 stars, gains 1 population.";
        else if (type.equals("gold"))
            info = "Grabs gold off the mountain. Gain 5 stars.";
        else if (type.equals("mine"))
            info = "Builds a mine on the mountain. Costs 5 stars, gains 2 population.";
        else if (type.equals("lumberhut"))
            info = "Builds a lumberhut in the forest. Costs 2 stars, gains 1 population.";
        
    }
    
    public Image getButton()
    {
        return new Image("images\\"+type+"_button.png");
    }
    
    /**
     * Displays the text onto the side panel, formats it somewhat nicely
     */
    public void displayInfo(GraphicsContext gc, double width)
    {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(20));
        gc.setFill(Color.LIGHTGREY);
        
        int i = 0;
        int y = 100;
        while (i < info.length())
        {
            int temp = info.indexOf(" ", i);
            while (temp != -1 && info.indexOf(" ", temp+1)-i <= 20)
            {
                temp = info.indexOf(" ", temp+1);
            }
            
            if (temp == -1)
            {
                if (info.length()-i <= 20)
                    temp = info.length();
                else
                    temp = info.lastIndexOf(" ");
            }
            
            gc.fillText(info.substring(i, temp), width+100, y);
            
            i = temp+1;
            y += 30;
        }
    }
    
    public void doAction(Tile t)
    {
        if (type.equals("animal"))
            ((Forest)t).hunt();
        else if (type.equals("berry"))
            ((Field)t).harvestFruit();
        else if (type.equals("fish"))
            ((Water)t).fish();
        else if (type.equals("gold"))
            ((Mountain)t).grabGold();
        else if (type.equals("mine"))
            ((Mountain)t).buildMine();
        else if (type.equals("lumberhut"))
            ((Forest)t).buildHut();
    }
}
