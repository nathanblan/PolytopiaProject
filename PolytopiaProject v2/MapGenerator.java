/**
 * Generate a 2D array representing a map
 * using Perlin Noise and a look-up table
 *
 * @author Nathan Lan
 * @version 5/3/2022
 */

import java.util.Random;
import java.util.ArrayList;

public class MapGenerator {
    /** Source of entropy */
    private static Random rand_;

    /** Amount of roughness */
    private static float roughness_;

    /** Plasma fractal grid */
    private static float[][] grid_;

    public static void init(Random rand, float roughness, int width, int height) {
        roughness_ = roughness / width;
        grid_ = new float[width][height];
        rand_ = (rand == null) ? new Random() : rand;
        
        int xh = grid_.length - 1;
        int yh = grid_[0].length - 1;

        // set the corner points
        grid_[0][0] = rand_.nextFloat() - 0.5f;
        grid_[0][yh] = rand_.nextFloat() - 0.5f;
        grid_[xh][0] = rand_.nextFloat() - 0.5f;
        grid_[xh][yh] = rand_.nextFloat() - 0.5f;

        // generate the fractal
        generate(0, 0, xh, yh);
    }

    // Add a suitable amount of random displacement to a point
    private static float roughen(float v, int l, int h) {
        return v + roughness_ * (float) (rand_.nextGaussian() * (h - l));
    }

    // generate the fractal
    private static void generate(int xl, int yl, int xh, int yh) {
        int xm = (xl + xh) / 2;
        int ym = (yl + yh) / 2;
        if ((xl == xm) && (yl == ym))
            return;

        grid_[xm][yl] = 0.5f * (grid_[xl][yl] + grid_[xh][yl]);
        grid_[xm][yh] = 0.5f * (grid_[xl][yh] + grid_[xh][yh]);
        grid_[xl][ym] = 0.5f * (grid_[xl][yl] + grid_[xl][yh]);
        grid_[xh][ym] = 0.5f * (grid_[xh][yl] + grid_[xh][yh]);

        float v = roughen(0.5f * (grid_[xm][yl] + grid_[xm][yh]), xl + yl, yh
                + xh);
        grid_[xm][ym] = v;
        grid_[xm][yl] = roughen(grid_[xm][yl], xl, xh);
        grid_[xm][yh] = roughen(grid_[xm][yh], xl, xh);
        grid_[xl][ym] = roughen(grid_[xl][ym], yl, yh);
        grid_[xh][ym] = roughen(grid_[xh][ym], yl, yh);

        generate(xl, yl, xm, ym);
        generate(xm, yl, xh, ym);
        generate(xl, ym, xm, yh);
        generate(xm, ym, xh, yh);
    }
    
    private static char[][] getBasicTerrain (double shift, int size)
    {
        char[][] map = new char[size][size];
        int waterCounter = 0;
        int landCounter = 0;
        int cityCounter =0;
        
        for(int i = 0;i < map.length;i++) 
        {
            for(int j = 0;j < map[0].length;j++) 
            {
                if(grid_[i][j]+shift < -1.25)
                {
                    map[i][j] = '=';//deep water
                    waterCounter++;
                }
                else if(grid_[i][j]+shift >= 3.25)
                {
                    map[i][j] = 'A'; //mountain
                    landCounter++;
                }
                else
                {
                    map[i][j] = '-';//land
                    landCounter++;
                }
            }
        }
        
        double total = size*size;
        
        // if too much water
        if (waterCounter/total > 0.6)
            return getBasicTerrain(shift+0.1, size);
        // if too much land
        else if (landCounter/total > 0.75)
            return getBasicTerrain(shift-0.1, size);
            
        return map;
    }

    public static void addCities(char[][] other, int size)
    {
        int[] coordinates = new int[2];
        for(int i = 0; i < other.length; i++)
        {
            for(int j = 0; j < other.length-1; j++)
            {
                if (other[i][j] == '-' || other[i][j] == 'A')
                {
                    if (Math.random() < 0.25)
                    {
                        other[i][j] = 'c';
                    }
                }
            }
        }
        
        for (int i = 0; i < other.length; i++) 
        {
            for (int j = 0; j < other[0].length;j++) 
            {
                // 
                if (other[i][j] == 'c')
                {
                    ArrayList<Integer> x = new ArrayList<Integer>();
                    ArrayList<Integer> y = new ArrayList<Integer>();
                    
                    x.add(i);
                    if (i != 0)
                    {
                        x.add(i-1);
                        if (i != 1)
                            x.add(i-2);
                    }
                    
                    if (i != other.length-1)
                    {
                        x.add(i+1);
                        if (i != other.length-2)
                            x.add(i+2);
                    }
                                        
                    y.add(j);
                    if (j != 0)
                    {
                        y.add(j-1);
                        if (j != 1)
                            y.add(j-2);
                    }
                    
                    if (j != other[0].length-1)
                    {
                        y.add(j+1);
                        if (j != other[0].length-2)
                            y.add(j+2);
                    }
                                            
                    for (int a : x)
                    {
                        for (int b : y)
                        {
                            if ((a != i || b != j) && other[a][b] == 'c')
                            {
                                other[a][b] = '-';
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Make a map with land and water
     */
    public static char[][] createTerrain(int size) 
    {
        init(null, 4.0f, 20, 20);
        
        char map[][] = getBasicTerrain(0, size);

        for(int i = 2; i < map.length-2;i++) 
        {
            for(int j = 2; j < map[0].length-2;j++) 
            {
                // mountain conditions
                if (map[i][j] == 'A' && map[i][j-2] == 'A')
                {
                    map[i][j-1] = '-';//so there are not huge amounts of mountains
                }
                if(map[i][j] == 'A' && map[i][j+2] == 'A')
                {
                    map[i][j+1] = '-';
                }
                if(map[i][j] == 'A' && map[i][j-2] == 'A')
                {
                    map[i-1][j] = '-';
                }
                if(map[i][j] == 'A' && map[i][j+2] == 'A')
                {
                    map[i+1][j] = '-';
                }
            }
        }
        
        //shallow water conditions
        for (int i = 0; i < map.length; i++) 
        {
            for (int j = 0; j < map[0].length;j++) 
            {
                // if land or mountain
                if (map[i][j] == '-' || map[i][j] == 'A')
                {
                    ArrayList<Integer> x = new ArrayList<Integer>();
                    ArrayList<Integer> y = new ArrayList<Integer>();
                    
                    x.add(i);
                    if (i != 0)
                        x.add(i-1);
                    if (i != map.length-1)
                        x.add(i+1);
                    
                    y.add(j);
                    if (j != 0)
                        y.add(j-1);
                    if (j != map[0].length-1)
                        y.add(j+1);
                        
                    for (int a : x)
                    {
                        for (int b : y)
                        {
                            if ((a != i || b != j) && map[a][b] == '=')
                                map[a][b] = '~';
                        }
                    }
                }
            }
        }
        
        addCities(map, map.length);
        int cityCount = 0;
        
        //populate with grass and forrests
        for(int i = 0;i < map.length;i++) 
        {
            for(int j = 0;j < map[0].length;j++) 
            {
                if(map[i][j] == '-')
                {
                    double foliageVal = (Math.random());
                    if(foliageVal < 0.2)
                    {
                        map[i][j] = '+'; // forrest
                    }
                    else if(foliageVal < 0.3)
                    {
                        map[i][j] = ','; // grass
                    }
                }
                
                if(map[i][j] == 'c')
                {
                    cityCount++;
                }
            }
        }
        
        if (cityCount <= 4)
        {
            addCities(map, map.length);
        }
        
        return map;
    }
}
