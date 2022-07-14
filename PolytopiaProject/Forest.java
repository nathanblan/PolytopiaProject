/**
 * A forest tile. Can hunt (30% chance to have animal) and build lumber hut.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Forest extends Tile
{
    private boolean hasAnimal;
    private boolean hasLumberHut;
    
    /**
     * Constructor for objects of class Forest
     */
    public Forest()
    {
        hasLumberHut = false;
        hasAnimal = Math.random() < 0.3; // 30% chance to have animal
        
        updateImage();
    }
    
    public Forest(boolean animal)
    {
        hasLumberHut = false;
        hasAnimal = animal;
    }
    
    public Forest(boolean animal, boolean hut)
    {
        hasAnimal = animal;
        hasLumberHut = hut;
    }
    
    public int hunt()
    {
        if (!canHunt() || city == null)
            return -1;
        hasAnimal = false;
        
        city.incPopulation(1);
        getPlayer().decStars(2);
        
        return 1;
    }
    
    public int buildHut()
    {
        if (!canBuildHut() || city == null)
            return -1;
        hasLumberHut = true;
        hasAnimal = false;
        
        city.incPopulation(1);
        city.getPlayer().decStars(2);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        if (hasLumberHut)
            gc.drawImage(new Image("images\\lumber_hut.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else if (hasAnimal)
            gc.drawImage(new Image("images\\forest_animal.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else
            gc.drawImage(new Image("images\\forest.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
    
    private void updateImage()
    {
        if (hasLumberHut)
            setImage(new Image("images\\lumber_hut.jpg"));
        else if (hasAnimal)
            setImage(new Image("images\\forest_animal.jpg"));
        else
            setImage(new Image("images\\forest.jpg"));
    }
    
    public String getInfo()
    {
        return "forest";
    }
    
    public boolean canHunt()
    {
        return hasAnimal;
    }
    
    public boolean canBuildHut()
    {
        return !hasLumberHut;
    }
    
    public String toString()
    {
        String output = "t";
        if (hasAnimal)
            output += "1";
        else
            output += "0";
        if (hasLumberHut)
            output += "1";
        else
            output += "0";
            
        return output;
    }
    
    public static Forest loadTile(String save)
    {   
        return new Forest(save.charAt(0)=='1',save.charAt(1)=='1');
    }
}
