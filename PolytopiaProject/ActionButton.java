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
    
    public static final ActionButton claimCity = new ActionButton("claimcity");
    public static final ActionButton heal = new ActionButton("heal");
    
    public static final ActionButton techFarming = new ActionButton("farming");
    public static final ActionButton techShields = new ActionButton("shields");
    public static final ActionButton techClimbing = new ActionButton("climbing");
    public static final ActionButton techMining = new ActionButton("mining");
    public static final ActionButton techFishing = new ActionButton("fishing");
    public static final ActionButton techSailing = new ActionButton("sailing");
    public static final ActionButton techHunting = new ActionButton("hunting");
    
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
        else if (type.equals("farming"))
            info = "Allows the construction of farms.";
        else if (type.equals("shields"))
            info = "Allows the training of shield troops.";
        else if (type.equals("climbing"))
            info = "Allows troops to move onto mountain tiles. Can now pick up gold from mountains.";
        else if (type.equals("mining"))
            info = "Allows the construction of mines on mountains.";
        else if (type.equals("fishing"))
            info = "Allows the player to fish.";
        else if (type.equals("sailing"))
            info = "Allows the player to build ports and sail in shallow water.";
        else if (type.equals("navigation"))
            info = "Allows the player to sail in deep water. Cruisers can be upgraded into battleships.";
        else if (type.equals("forestry"))
            info = "Allows the player to build lumberhuts in forests.";
        else if (type.equals("hunting"))
            info = "Allows the player to hunt animals in forests.";
        else if (type.equals("riding"))
            info = "Allows the player to train rider troops.";
        else if (type.equals("archery"))
            info = "Allows the player to train archer troops.";
        else if (type.equals("city building"))
            info = "Allows the player to build cities.";
        else if (type.equals("destroy mountain"))
            info = "Allows the player to destroy mountains";
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
    
    public boolean doAction(Troop t)
    {
        if (type.equals("heal"))
            t.heal(4);
        else if (type.equals("claimcity"))
            return true;
        return false;
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
    
    public void doAction(Player p)
    {
        TechTree t = p.getTree();
        if (type.equals("farming"))
            t.unlockFarming(p);
        else if (type.equals("shields"))
            t.unlockShields(p);
        else if (type.equals("climbing"))
            t.unlockClimbing(p);
        else if (type.equals("mining"))
            t.unlockMining(p);
        else if (type.equals("fishing"))
            t.unlockFishing(p);
        else if (type.equals("sailing"))
            t.unlockSailing(p);
        else if (type.equals("navigation"))
            t.unlockNavigation(p);
        else if (type.equals("forestry"))
            t.unlockForestry(p);
        else if (type.equals("hunting"))
            t.unlockHunting(p);
        else if (type.equals("riding"))
            t.unlockRiding(p);
        else if (type.equals("archery"))
            t.unlockArchery(p);
        else if (type.equals("city building"))
            t.unlockCityBuilding(p);
        else if (type.equals("destroy mountain"))
            t.unlockMountainDestroyer(p);
    }
}
