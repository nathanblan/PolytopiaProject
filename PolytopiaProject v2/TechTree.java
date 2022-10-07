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
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * Write a description of class TechTree here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TechTree
{
    private boolean hasHunting;
    private boolean hasOrganization;
    private boolean hasFishing;
    private boolean hasClimbing;
    private boolean hasSailing;
    private boolean hasNavigation;
    private boolean hasArchery;
    private boolean hasForestry;
    private boolean hasFarming;
    private boolean hasShields;
    private boolean hasMining;
    private boolean hasRiding;
    private boolean hasCityBuilding;
    private boolean hasMountainDestroyer;
    private boolean hasCanalDigger;
    public TechTree()
    {
        hasOrganization = true;
        hasFarming = false;
        hasShields = false;
        
        hasClimbing = false;
        hasMining = false;
        hasCanalDigger = false;
        
        hasFishing = false;
        hasSailing = false;
        hasNavigation = false;
        
        hasHunting = false;
        hasForestry = false;
        hasArchery = false;
        
        hasRiding = false;
        hasCityBuilding = false;
        hasMountainDestroyer = false;
    }
    
    public TechTree(String save)
    {
        hasOrganization = true;
        hasFarming = save.charAt(1)=='1';
        hasShields = save.charAt(2)=='1';
        
        hasClimbing = save.charAt(3)=='1';
        hasMining = save.charAt(4)=='1';
        
        hasFishing = save.charAt(5)=='1';
        hasSailing = save.charAt(6)=='1';
        hasNavigation = save.charAt(7)=='1';
        
        hasHunting = save.charAt(8)=='1';
        hasForestry = save.charAt(9)=='1';
        hasArchery = save.charAt(10)=='1';
        
        hasRiding = save.charAt(11)=='1';
        hasCityBuilding = save.charAt(12)=='1';
        hasMountainDestroyer = save.charAt(13)=='1';
    }
    
    public void showTechTree(GraphicsContext gc)
    {
        gc.drawImage(new Image("images\\techtreebackground.png"), 0, 0, 1000, 800);
        gc.drawImage(new Image("techtree_images\\tech tree.png"), 350, 350, 100, 100);
        
        gc.drawImage(new Image("techtree_images\\organization_U.png"), 475, 320, 100, 100);
        gc.drawImage(button(hasClimbing, "climbing"), 430, 455, 100, 100);
        gc.drawImage(button(hasFishing, "fishing"), 270, 455, 100, 100);
        gc.drawImage(button(hasHunting, "hunting"), 225, 320, 100, 100);
        gc.drawImage(button(hasRiding, "riding"), 350, 225, 100, 100);
        
        gc.drawImage(button(hasForestry, "forestry"), 125, 225, 100, 100);
        gc.drawImage(button(hasArchery, "archery"), 100, 350, 100, 100);
        
        gc.drawImage(button(hasFarming, "farming"), 575, 225, 100, 100);
        gc.drawImage(button(hasShields, "shields"), 600, 350, 100, 100);
        
        gc.drawImage(button(hasCityBuilding, "citybuilding"), 450, 130, 100, 100);
        gc.drawImage(button(hasMountainDestroyer, "mtndestroyer"), 325, 75, 100, 100);
        //gc.drawImage(button(hasCanalDigger, "cnldigger"), 400, 75, 100, 100);
        
        gc.drawImage(button(hasSailing, "sailing"), 150, 510, 100, 100);
        gc.drawImage(button(hasNavigation, "navigation"), 250, 600, 100, 100);
        
        gc.drawImage(button(hasMining, "mining"), 500, 575, 100, 100);
    }
    
    public Image button(boolean unlocked, String type)
    {
        if(unlocked)
            return new Image("techtree_images\\"+type+"_U.png");
        else
            return new Image("techtree_images\\"+type+"_L.png");
    }
    
    //All the gets!
    public boolean getFarming()
    {
        return hasFarming;
    }
    
    public boolean getShields()
    {
        return hasShields;
    }
    
    public boolean getClimbing()
    {
        return hasClimbing;
    }
    
    public boolean getMining()
    {
        return hasMining;
    }
    
    public boolean getFishing()
    {
        return hasFishing;
    }
    
    public boolean getSailing()
    {
        return hasSailing;
    }
    
    public boolean getNavigation()
    {
        return hasNavigation;
    }
    
    public boolean getHunting()
    {
        return hasHunting;
    }
    
    public boolean getArchery()
    {
        return hasArchery;
    }
    
    public boolean getForestry()
    {
        return hasForestry;
    }
    
    public boolean getRiding()
    {
        return hasRiding;
    }
    
    public boolean getCityBuilding()
    {
        return hasCityBuilding;
    }
    
    public boolean getMountainDestroyer()
    {
        return hasMountainDestroyer;
    }
    
    public boolean getCanalDigger()
    {
        return hasCanalDigger;
    }
    
    //Unlocking!!! Lvl 1 = 5 Stars, Lvl 2 = 10 Stars, Lvl 3 = 20 Stars
    
    //Lvl 1s
    public void unlockClimbing(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasClimbing = true;
            person.decStars(5);
        }
    }
    
    public void unlockFishing(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasFishing = true;
            person.decStars(5);
        }
    }
    
    public void unlockHunting(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasHunting = true;
            person.decStars(5);
        }
    }
    
    public void unlockRiding(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasRiding = true;
            person.decStars(5);
        }
    }
    
    //Lvl 2s
    public void unlockFarming(Player person)
    {
        if(person.getStars() >= 10)
        {
            hasFarming = true;
            person.decStars(10);
        }
    }
    
    public void unlockShields(Player person)
    {
        if(person.getStars() >= 10)
        {
            hasShields = true;
            person.decStars(10);
        }
    }
    
    public void unlockSailing(Player person)
    {
        if(person.getStars() >= 10 && hasFishing)
        {
            hasSailing = true;
            person.decStars(10);
        }
    }
    
    public void unlockMining(Player person)
    {
        if(person.getStars() >= 10 && hasClimbing)
        {
            hasMining = true;
            person.decStars(10);
        }
    }
    
    public void unlockArchery(Player person)
    {
        if(person.getStars() >= 10 && hasHunting)
        {
            hasArchery = true;
            person.decStars(10);
        }
    }
    
    public void unlockForestry(Player person)
    {
        if(person.getStars() >= 10 && hasHunting)
        {
            hasForestry = true;
            person.decStars(10);
        }
    }
    
    public void unlockCityBuilding(Player person)
    {
        if(person.getStars() >= 10 && hasRiding)
        {
            hasCityBuilding = true;
            person.decStars(10);
        }
    }
    
    //Lvl 3
    public void unlockNavigation(Player person)
    {
        if(person.getStars() >= 20 && hasSailing)
        {
            hasNavigation = true;
            person.decStars(20);
        }
    }
    
    public void unlockMountainDestroyer(Player person)
    {
        if(person.getStars() >= 20 && hasCityBuilding)
        {
            hasMountainDestroyer = true;
            person.decStars(20);
        }
    }
    
    public void unlockCanalDigger(Player person)
    {
        if(person.getStars() >= 20 && hasMining)
        {
            hasCanalDigger = true;
            person.decStars(20);
        }
    }
    
    public ActionButton getActionButton(double x, double y)
    {
        x -= 50;
        y -= 50;
        
        if (square(x-475) + square(y-320) < square(50))
            return ActionButton.techOrganization;
        if (square(x-430) + square(y-455) < square(50))
            return ActionButton.techClimbing;
        if (square(x-270) + square(y-455) < square(50))
            return ActionButton.techFishing;
        if (square(x-225) + square(y-320) < square(50))
            return ActionButton.techHunting;
        if (square(x-350) + square(y-225) < square(50))
            return ActionButton.techRiding;
        if (square(x-125) + square(y-225) < square(50))
            return ActionButton.techForestry;
        if (square(x-100) + square(y-350) < square(50))
            return ActionButton.techArchery;
        if (square(x-575) + square(y-225) < square(50))
            return ActionButton.techFarming;
        if (square(x-600) + square(y-350) < square(50))
            return ActionButton.techShields;
        if (square(x-450) + square(y-130) < square(50))
            return ActionButton.techCityBuilding;
        if (square(x-325) + square(y-75) < square(50))
            return ActionButton.techMountainDestroyer;
        if (square(x-150) + square(y-510) < square(50))
            return ActionButton.techSailing;
        if (square(x-250) + square(y-600) < square(50))
            return ActionButton.techNavigation;
        if (square(x-500) + square(y-575) < square(50))
            return ActionButton.techMining;
        
        return null;
    }
    
    private double square(double num)
    {
        return num*num;
    }
    
    public String toString()
    {
        String output = "1";
        
        if (hasFarming)
            output += "1";
        else
            output += "0";
            
        if (hasShields)
            output += "1";
        else
            output += "0";
        
        if (hasClimbing)
            output += "1";
        else
            output += "0";
            
        if (hasMining)
            output += "1";
        else
            output += "0";
            
        if (hasFishing)
            output += "1";
        else
            output += "0";
            
        if (hasSailing)
            output += "1";
        else
            output += "0";
            
        if (hasNavigation)
            output += "1";
        else
            output += "0";        
            
        if (hasHunting)
            output += "1";
        else
            output += "0";
            
        if (hasForestry)
            output += "1";
        else
            output += "0";
            
        if (hasArchery)
            output += "1";
        else
            output += "0";

        if (hasRiding)
            output += "1";
        else
            output += "0";
            
        if (hasCityBuilding)
            output += "1";
        else
            output += "0";
            
        if (hasMountainDestroyer)
            output += "1";
        else
            output += "0";
        
        return output;
    }
}
