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
import javafx.scene.Group;

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
    public static final ActionButton buildFarm = new ActionButton("farm");
    public static final ActionButton buildCity = new ActionButton("build city");
    public static final ActionButton buildPort = new ActionButton("build port");
    public static final ActionButton destroyMountain = new ActionButton("destroy mtn");
    
    public static final ActionButton claimCity = new ActionButton("claimcity");
    public static final ActionButton heal = new ActionButton("heal");
    public static final ActionButton upgrade = new ActionButton("upgrade");
    
    public static final ActionButton trainWarrior = new ActionButton("warrior");
    public static final ActionButton trainArcher = new ActionButton("archer");
    public static final ActionButton trainShield = new ActionButton("shield");
    public static final ActionButton trainRider = new ActionButton("rider");
    
    public static final ActionButton techOrganization = new ActionButton("organization");
    public static final ActionButton techFarming = new ActionButton("farming");
    public static final ActionButton techShields = new ActionButton("shields");
    public static final ActionButton techClimbing = new ActionButton("climbing");
    public static final ActionButton techMining = new ActionButton("mining");
    public static final ActionButton techCanalDigger = new ActionButton("dig canal");
    public static final ActionButton techFishing = new ActionButton("fishing");
    public static final ActionButton techSailing = new ActionButton("sailing");
    public static final ActionButton techNavigation = new ActionButton("navigation");
    public static final ActionButton techHunting = new ActionButton("hunting");
    public static final ActionButton techRiding = new ActionButton("riding");
    public static final ActionButton techForestry = new ActionButton("forestry");
    public static final ActionButton techArchery = new ActionButton("archery");
    public static final ActionButton techCityBuilding = new ActionButton("city building");
    public static final ActionButton techMountainDestroyer = new ActionButton("destroy mountain");
    
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
        else if (type.equals("farm"))
            info = "Builds a farm. Costs 5 stars, gains 2 population";
        else if (type.equals("fish"))
            info = "Fish some fish. Costs 2 stars, gains 1 population.";
        else if (type.equals("gold"))
            info = "Grabs gold off the mountain. Gain 5 stars.";
        else if (type.equals("mine"))
            info = "Builds a mine on the mountain. Costs 5 stars, gains 2 population.";
        else if (type.equals("lumberhut"))
            info = "Builds a lumberhut in the forest. Costs 2 stars, gains 1 population.";
        else if (type.equals("build port"))
            info = "Builds a port in shallow water. Costs 5 stars, gains 2 population.";
        else if (type.equals("build city"))
            info = "Builds a city. Costs 20 stars.";
        else if (type.equals("destroy mtn"))
            info = "Destroys a mountain. Costs 15 stars.";
            
        else if (type.equals("warrior"))
            info = "Train a  warrior.";
        else if (type.equals("archer"))
            info = "Train an archer";
        else if (type.equals("rider"))
            info = "Train rider";
        else if (type.equals("shield"))
            info = "Train shield";
            
        else if (type.equals("organization"))
            info = "Enables organization.";
        else if (type.equals("farming"))
            info = "Enables the construction of farms.";
        else if (type.equals("shields"))
            info = "Enables the training of shield troops.";
        else if (type.equals("climbing"))
            info = "Enables troops to move onto mountain tiles. Enables picking up gold from mountains.";
        else if (type.equals("dig canal"))
            info = "Enables the creation of canals.";
        else if (type.equals("mining"))
            info = "Enables the construction of mines on mountains.";
        else if (type.equals("fishing"))
            info = "Enables fishing.";
        else if (type.equals("sailing"))
            info = "Enables the constructions of ports and movement in shallow water.";
        else if (type.equals("navigation"))
            info = "Enables movement in deep water. Cruisers can be upgraded into battleships.";
        else if (type.equals("forestry"))
            info = "Enables the construction of lumberhuts in forests.";
        else if (type.equals("hunting"))
            info = "Enables hunting.";
        else if (type.equals("riding"))
            info = "Enables the training of rider troops.";
        else if (type.equals("archery"))
            info = "Enables the training of archer troops.";
        else if (type.equals("city building"))
            info = "Enables city building.";
        else if (type.equals("destroy mountain"))
            info = "Enables the destruction of mountains";
    }
    
    public Image getButton(Player p)
    {   
        if (canDoAction(p))
            return new Image("ActionButtonImages\\"+type+"_button.png");
        return new Image("ActionButtonImages\\"+type+"_button_L.png");
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
        else if (type.equals("upgrade"))
            t.upgradeShip();
        return false;
    }
    
    public int doAction(Tile t, int x, int y, int turn)
    {
        Troop w = null;
        
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
        else if (type.equals("farm"))
            ((Grass)t).buildFarm();
        else if (type.equals("build port"))
            ((Water)t).buildPort();
        else if (type.equals("destroy mtn"))
            return 1;
        else if (type.equals("build city"))
            return 2;
        
        else if (type.equals("warrior"))
            w = new Warrior(t.getPlayer(), turn, x, y);
        else if (type.equals("archer"))
            w = new Archer(t.getPlayer(), turn, x, y);
        else if (type.equals("rider"))
            w = new Rider(t.getPlayer(), turn, x, y);
        else if (type.equals("shield"))
            w = new Shield(t.getPlayer(), turn, x, y);
            
        if (w != null)
        {
            ((City)t).trainTroop(w);
            return 3;
        }
        return 0;
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
        else if (type.equals("dig canal"))
            t.unlockCanalDigger(p);
    }
    
    public boolean canDoAction(Player p)
    {
        TechTree t = p.getTree();
        if (type.equals("organization"))
            return false;
        if (type.equals("farming") && p.getStars() >= 10)
            return !t.getFarming();
        if (type.equals("shields") && p.getStars() >= 10)
            return !t.getShields();
        if (type.equals("climbing") && p.getStars() >= 5)
            return !t.getClimbing();
        if (type.equals("mining") && p.getStars() >= 10)
        {
            if (t.getClimbing())
                return !t.getMining();
            return false;
        }
        if (type.equals("fishing") && p.getStars() >= 5)
            return !t.getFishing();
        if (type.equals("sailing") && p.getStars() >= 10)
        {
            if (t.getFishing())
                return !t.getSailing();
            return false;
        }
        if (type.equals("navigation") && p.getStars() >= 20)
        {
            if (t.getSailing())
                return !t.getNavigation();
            return false;
        }
        if (type.equals("forestry") && p.getStars() >= 10)
        {
            if (t.getHunting())
                return !t.getForestry();
            return false;
        }
        if (type.equals("hunting") && p.getStars() >= 5)
            return !t.getHunting();
        if (type.equals("riding") && p.getStars() >= 5)
            return !t.getRiding();
        if (type.equals("archery") && p.getStars() >= 10)
        {
            if (t.getHunting())
                return !t.getArchery();
            return false;
        }
        if (type.equals("city building") && p.getStars() >= 10)
        {
            if (t.getRiding())
                return !t.getCityBuilding();
            return false;
        }
        if (type.equals("destroy mountain") && p.getStars() >= 20)
        {
            if (t.getCityBuilding())
                return !t.getMountainDestroyer();
            return false;
        }
        if (type.equals("dig canal") && p.getStars() >= 20)
        {
            if (t.getMining())
                return !t.getCanalDigger();
            return false;
        }
        
        if (type.equals("warrior") && p.getStars() >= 2)
            return true;
        if (type.equals("archer") && p.getStars() >= 3)
            return t.getArchery();
        if (type.equals("shield") && p.getStars() >= 3)
            return t.getShields();
        if (type.equals("rider") && p.getStars() >= 3)
            return t.getRiding();
            
        if (type.equals("animal"))
            return p.getStars() >= 2;
        else if (type.equals("berry"))
            return p.getStars() >= 2;
        else if (type.equals("fish"))
            return p.getStars() >= 2;
        else if (type.equals("gold"))
            return true;
        else if (type.equals("mine"))
            return p.getStars() >= 5;
        else if (type.equals("lumberhut"))
            return p.getStars() >= 2;
        else if (type.equals("farm"))
            return p.getStars() >= 5;
        else if (type.equals("build port"))
            return p.getStars() >= 5;
        else if (type.equals("destroy mtn"))
            return p.getStars() >= 20;
        else if (type.equals("build city"))
            return p.getStars() >= 20;
        
        return true;
    }
    
    public String toString()
    {
        return type+": "+info;
    }
}
