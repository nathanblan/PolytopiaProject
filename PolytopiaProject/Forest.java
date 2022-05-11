import javafx.scene.image.Image;

/**
 * A forest tile. Can hunt (30% chance to have animal) and build lumber hut.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

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
    }
    
    public int hunt()
    {
        if (!hasAnimal)
            return -1;
        hasAnimal = false;
        
        city.incPopulation(1);
        
        return 1;
    }
    
    public int buildLumberHut()
    {
        if (hasLumberHut)
            return -1;
        hasLumberHut = true;
        hasAnimal = false;
        
        city.incPopulation(1);
        
        return 1;
    }
    
    public void drawTile(GraphicsContext gc, int x, int y)
    {
        if (hasLumberHut)
            hasLumberHut = true; // placeholder
        else if (hasAnimal)
            gc.drawImage(new Image("images\\forest_animal.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        else
            gc.drawImage(new Image("images\\forest.jpg"), x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
