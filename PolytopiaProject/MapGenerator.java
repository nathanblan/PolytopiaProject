/**
 * Generate a 2D array representing a map
 * using Perlin Noise and a look-up table
 *
 * @author Nathan Lan
 * @version 5/3/2022
 */

import java.util.Random;

public class MapGenerator {
    /** Source of entropy */
    private Random rand_;

    /** Amount of roughness */
    float roughness_;

    /** Plasma fractal grid */
    private float[][] grid_;

    /** Generate a noise source based upon the midpoint displacement fractal.
     * 
     * @param rand The random number generator
     * @param roughness a roughness parameter
     * @param width the width of the grid
     * @param height the height of the grid
     */
    public MapGenerator(Random rand, float roughness, int width, int height) {
        roughness_ = roughness / width;
        grid_ = new float[width][height];
        rand_ = (rand == null) ? new Random() : rand;
    }

    public void init() {
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
    private float roughen(float v, int l, int h) {
        return v + roughness_ * (float) (rand_.nextGaussian() * (h - l));
    }

    // generate the fractal
    private void generate(int xl, int yl, int xh, int yh) {
        int xm = (xl + xh) / 2;
        int ym = (yl + yh) / 2;
        if ((xl == xm) && (yl == ym)) return;

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

    /**
     * Dump out as a CSV [DONT NEED THIS]
     */
    public void printAsCSV() {
        for(int i = 0;i < grid_.length;i++) {
            for(int j = 0;j < grid_[0].length;j++) {
                System.out.print(grid_[i][j]);
                //System.out.print(",");
            }
            System.out.println();
        }
    }

    /**
     * Convert to a Boolean array
     * @return the boolean array
     */
    public boolean[][] toBooleans() {
        int w = grid_.length;
        int h = grid_[0].length;
        boolean[][] ret = new boolean[w][h];
        for(int i = 0;i < w;i++) {
            for(int j = 0;j < h;j++) {
                ret[i][j] = grid_[i][j] < 0;
            }
        }
        return ret;
    }

    /** For testing */
    public static void test(String[] args) {
        MapGenerator n = new MapGenerator(null, 4.0f, 20, 20);
        int i=5;
        while(i >= 1)
        {
            n.init();
            //n.printAsCSV();
            char[][] a = n.createTerrain(20);
            //n.printAsCSV(a);
            System.out.println();
            i--;
        }

    }

    /**
     * Make a map with land and water
     */
    public char[][] createTerrain(int size) 
    {
        char[][] map = new char[size][size];
        for(int i = 0;i < map.length;i++) 
        {
            for(int j = 0;j < map[0].length;j++) 
            {
                if(grid_[i][j]+0.5 < -0.75)
                {
                    map[i][j] = '=';//deep water
                }
                else if(grid_[i][j]+0.5 >=3.75)
                {
                    map[i][j] = 'A'; //mountain
                }
                else
                {
                    map[i][j] = '-';//land
                }
            }
        }

        for(int i = 2;i < map.length-2;i++) 
        {
            for(int j = 2;j < map[0].length-2;j++) 
            {
                //mountain conditions
                if(map[i][j] == 'A' && map[i][j-2] == 'A')
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

        for (int i = 0; i < map.length; i++) 
        {
            for (int j = 0; j < map[0].length;j++) 
            {
                //shallow water conditions
                
                // if land or mountain
                if (map[i][j] == '-' || map[i][j] == 'A')
                {
                    // top left corner
                    if (i==0 && j==0)
                    {
                        if (map[i+1][j] == '=')
                        {
                            map[i+1][j] = '~';
                        }
                        if (map[i][j+1] == '=')
                        {
                            map[i][j+1] = '~';
                        }
                        if (map[i+1][j+1] == '=')
                        {
                            map[i+1][j+1] = '~';
                        }
                    }
                    // bottom left corner
                    else if(i==map.length-1 && j==0)
                    {
                        if (map[i-1][j] == '=')
                        {
                            map[i-1][j] = '~';
                        }
                        if (map[i][j+1] == '=')
                        {
                            map[i][j+1] = '~';
                        }
                        if (map[i-1][j+1] == '=')
                        {
                            map[i-1][j+1] = '~';
                        }
                    }
                    // top right corner
                    else if (i == 0 && j == map[0].length-1)
                    {
                        if (map[i+1][j] == '=')
                        {
                            map[i+1][j] = '~';
                        }
                        if (map[i][j-1] == '=')
                        {
                            map[i][j-1] = '~';
                        }
                        if (map[i+1][j-1] == '=')
                        {
                            map[i+1][j-1] = '~';
                        }
                    }
                    // bottom right corner
                    else if (i == map.length-1 && j == map[0].length-1)
                    {
                        if (map[i-1][j] == '=')
                        {
                            map[i-1][j] = '~';
                        }
                        if (map[i][j-1] == '=')
                        {
                            map[i][j-1] = '~';
                        }
                        if (map[i-1][j-1] == '=')
                        {
                            map[i-1][j-1] = '~';
                        }
                    }
                    // top row, not corners
                    else if (i == 0)
                    {
                        if (map[i+1][j] == '=')
                        {
                            map[i+1][j] = '~';
                        }
                        if (map[i][j+1] == '=')
                        {
                            map[i][j+1] = '~';
                        }
                        if (map[i][j-1] == '=')
                        {
                            map[i][j-1] = '~';
                        }
                        if (map[i+1][j-1] == '=')
                        {
                            map[i+1][j-1] = '~';
                        }
                        if (map[i+1][j+1] == '=')
                        {
                            map[i+1][j+1] = '~';
                        }
                    }
                    // bottom row, not corners
                    else if (i == map.length-1)
                    {
                        if (map[i-1][j] == '=')
                        {
                            map[i-1][j] = '~';
                        }
                        if (map[i][j+1] == '=')
                        {
                            map[i][j+1] = '~';
                        }
                        if (map[i][j-1] == '=')
                        {
                            map[i][j-1] = '~';
                        }
                        if (map[i-1][j-1] == '=')
                        {
                            map[i-1][j-1] = '~';
                        }
                        if (map[i-1][j+1] == '=')
                        {
                            map[i-1][j+1] = '~';
                        }
                    }
                    // left column, not corners
                    else if (j == 0)
                    {
                        if (map[i-1][j] == '=')
                        {
                            map[i-1][j] = '~';
                        }
                        if (map[i][j+1] == '=')
                        {
                            map[i][j+1] = '~';
                        }
                        if (map[i+1][j] == '=')
                        {
                            map[i+1][j] = '~';
                        }
                        if (map[i+1][j+1] == '=')
                        {
                            map[i+1][j+1] = '~';
                        }
                        if (map[i-1][j+1] == '=')
                        {
                            map[i-1][j+1] = '~';
                        }
                    }
                    // right column, not corners
                    else if (j == map[0].length-1)
                    {
                        if (map[i-1][j] == '=')
                        {
                            map[i-1][j] = '~';
                        }
                        if (map[i][j-1] == '=')
                        {
                            map[i][j-1] = '~';
                        }
                        if(map[i+1][j] == '=')
                        {
                            map[i+1][j] = '~';
                        }
                        if(map[i+1][j-1] == '=')
                        {
                            map[i+1][j-1] = '~';
                        }
                        if (map[i-1][j-1] == '=')
                        {
                            map[i-1][j-1] = '~';
                        }
                    }
                    // literally any other tile
                    else
                    {
                        if (map[i-1][j-1] == '=')
                        {
                            map[i-1][j-1] = '~';
                        }
                        if (map[i-1][j] == '=')
                        {
                            map[i-1][j] = '~';
                        }
                        if (map[i-1][j+1] == '=')
                        {
                            map[i-1][j+1] = '~';
                        }
                        if (map[i][j-1] == '=')
                        {
                            map[i][j-1] = '~';
                        }
                        if (map[i][j+1] == '=')
                        {
                            map[i][j+1] = '~';
                        }
                        if (map[i+1][j-1] == '=')
                        {
                            map[i+1][j-1] = '~';
                        }
                        if (map[i+1][j] == '=')
                        {
                            map[i+1][j] = '~';
                        }
                        if (map[i+1][j+1] == '=')
                        {
                            map[i+1][j+1] = '~';
                        }
                    }
                }
            }
        }

        for(int i = 0;i < map.length;i++) 
        {
            for(int j = 0;j < map[0].length;j++) 
            {
                if(map[i][j] == '-')
                {
                    double foliageVal = (Math.random()*10);
                    if(foliageVal < 2.5)
                    {
                        map[i][j] = '+'; // forrest
                    }
                    else if(foliageVal < 3.75)
                    {
                        map[i][j] = ','; // grass
                    }
                }
            }
        }
        
        for(int i = 0;i < map.length;i++) 
        {
            for(int j = 0;j < map[0].length;j++) 
            {
                if(map[i][j] == '-')
                {
                    double foliageVal = (Math.random()*10);
                    if(foliageVal < 2.5)
                    {
                        map[i][j] = '+'; // forrest
                    }
                    else if(foliageVal < 3.75)
                    {
                        map[i][j] = ','; // grass
                    }
                }
            }
        }
    
        return map;
    }
}
