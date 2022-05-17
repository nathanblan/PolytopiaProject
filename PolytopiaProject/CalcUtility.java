
/**
 * Holds helper methods
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CalcUtility
{
    private static final int w = 800;
    private static final int h = 800;
    
    /**
     * Returns the index of the button clicked, -1 if not clicking a button
     */
    public static int getButtonIndex(int x, int y)
    {
        if (x < w)
            return -1;
            
        x -= w+100;
        y -= 80;
        
        int index = y/200;
        
        y %= 200;
        y -= 80;
        
        if (x*x + y*y > 80*80)
            return -1;
        
        return index;
    }
    
    /**
     * Returns -1 if neither confirm button pressed
     * Returns 1 if confirm button pressed
     * Returns 0 if cancel button pressed
     */
    public static int getConfirmButton(int x, int y)
    {
        if (x < w+20 || x > w+180)
            return -1;
        if (y < h-80)
            return -1;
        
        x -= w+20;
        y -= h-50;
        
        int output = x/100;
        
        x %= 100;
        x -= 30;
        
        if (x*x + y*y < 30*30)
            return output;
        
        return -1;
    }
}
