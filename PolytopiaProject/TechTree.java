
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
    public TechTree()
    {
        hasOrganization = true;
        hasFarming = false;
        hasShields = false;
        
        hasClimbing = false;
        hasMining = false;
        
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
    
    //Unlocking!!! Lvl 1 = 5 Stars, Lvl 2 = 10 Stars, Lvl 3 = 20 Stars
    
    //Lvl 1s
    public void unlockClimbing(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasClimbing = true;
        }
        person.decStars(5);
    }
    
    public void unlockFishing(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasFishing = true;
        }
        person.decStars(5);
    }
    
    public void unlockHunting(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasHunting = true;
        }
        person.decStars(5);
    }
    
    public void unlockRiding(Player person)
    {
        if(person.getStars() >= 5)
        {
            hasRiding = true;
        }
        person.decStars(5);
    }
    
    //Lvl 2s
    public void unlockFarming(Player person)
    {
        if(person.getStars() >= 10)
        {
            hasFarming = true;
        }
        person.decStars(10);
    }
    
    public void unlockShields(Player person)
    {
        if(person.getStars() >= 10)
        {
            hasShields = true;
        }
        person.decStars(10);
    }
    
    public void unlockSailing(Player person)
    {
        if(person.getStars() >= 10 && hasFishing)
        {
            hasSailing = true;
        }
        person.decStars(10);
    }
    
    public void unlockMining(Player person)
    {
        if(person.getStars() >= 10 && hasClimbing)
        {
            hasMining = true;
        }
        person.decStars(10);
    }
    
    public void unlockArchery(Player person)
    {
        if(person.getStars() >= 10 && hasHunting)
        {
            hasArchery = true;
        }
        person.decStars(10);
    }
    
    public void unlockForestry(Player person)
    {
        if(person.getStars() >= 10 && hasHunting)
        {
            hasForestry = true;
        }
        person.decStars(10);
    }
    
    public void unlockCityBuilding(Player person)
    {
        if(person.getStars() >= 10 && hasRiding)
        {
            hasCityBuilding = true;
        }
        person.decStars(10);
    }
    
    //Lvl 3
    public void unlockNavigation(Player person)
    {
        if(person.getStars() >= 20 && hasSailing)
        {
            hasNavigation = true;
        }
        person.decStars(20);
    }
    
    public void unlockMountainDestroyer(Player person)
    {
        if(person.getStars() >= 20 && hasCityBuilding)
        {
            hasMountainDestroyer = true;
        }
        person.decStars(20);
    }
}
