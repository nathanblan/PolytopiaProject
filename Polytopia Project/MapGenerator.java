
/**
 * Generate a 2D array map using fractals
 *
 * grid_ contains the 2D array object
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.Random;

public class MapGenerator {
    /** Source of entropy */
    private Random rand_;

    /** 
     * Amount of roughness 
     * Adjust to change smoothness of map.
     */
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
    // load grid_ with values
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
     * Dump out as a CSV
     * Not needed. Good for testing.
     */
    public void printAsCSV() {
        for(int i = 0;i < grid_.length;i++) {
            for(int j = 0;j < grid_[0].length;j++) {
                System.out.print(grid_[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }
    
    /**
     * Dump out as a CSV
     * Not needed. Good for testing.
     */
    public void printAsStringCSV(char[][] map) {
        for(int i = 0;i < map.length;i++) {
            for(int j = 0;j < map[0].length;j++) {
                System.out.print(map[i][j]);
                System.out.print(" ");
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
    public static void main(String[] args) {
        MapGenerator n = new MapGenerator(null, 4.0f, 20, 20);
        int i=5;
        while(i >= 1)
        {
            n.init();
            //n.printAsCSV();
            char[][] a = n.createTerrain(20);
            n.printAsStringCSV(a);
            System.out.println();
            i--;
        }
        
    }
    
    /**
     * Make a map with land and water
     */
    public char[][] createTerrain(int size) {
        char[][] map = new char[size][size];
        for(int i = 0;i < map.length;i++) {
            for(int j = 0;j < map[0].length;j++) {
                if(grid_[i][j]+0.5 < -0.75)
                {
                    map[i][j] = '=';//deep water
                }
                else if(grid_[i][j] <= 0)
                {
                    map[i][j] = '~';//shallow water
                }
                else if(grid_[i][j]+0.5 >=5.5)
                {
                    map[i][j] = 'A'; //mountain
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
        
        for(int i = 2;i < map.length-2;i++) {
            for(int j = 2;j < map[0].length-2;j++) {
                //mountain conditions
                if(map[i][j] == 'A' && map[i][j-2] == 'A')
                {
                    map[i][j-1] = '-';//so shallow water is next to land
                }
                if(map[i][j] == 'A' && map[i][j+2] == 'A')
                {
                    map[i][j+1] = '-';//so shallow water is next to land
                }
                if(map[i][j] == 'A' && map[i][j-2] == 'A')
                {
                    map[i-1][j] = '-';//so shallow water is next to land
                }
                if(map[i][j] == 'A' && map[i][j+2] == 'A')
                {
                    map[i+1][j] = '-';//so shallow water is next to land
                }
                
                //deep water conditions
                /*
                if(map[i][j].equals("=") && map[i][j-1].equals("=") && map[i][j-2].equals("="))
                {
                    map[i][j-2] = "—";//so shallow water is next to land
                }
                if(map[i][j].equals("=") && map[i][j+1].equals("=") && map[i][j+2].equals("="))
                {
                    map[i][j+2] = "—";//so shallow water is next to land
                }
                if(map[i][j].equals("=") && map[i-1][j].equals("=") && map[i-2][j].equals("="))
                {
                    map[i-2][j] = "—";//so shallow water is next to land
                }
                if(map[i][j].equals("=") && map[i+1][j].equals("=") && map[i+2][j].equals("="))
                {
                    map[i+2][j] = "—";//so shallow water is next to land
                } */
            }
        }
        
        for(int i = 1;i < map.length-1;i++) {
            for(int j = 1;j < map[0].length-1;j++) {
                //shallow water conditions
                if(map[i][j] == '-' && map[i][j-1] == '=')
                {
                    map[i][j-1] = '~';//so shallow water is next to land
                }
                if(map[i][j] == '-' && map[i][j+1] == '=')
                {
                    map[i][j+1] = '~';//so shallow water is next to land
                }
                if(map[i][j] == '-' && map[i-1][j] == '=')
                {
                    map[i-1][j] = '~';//so shallow water is next to land
                }
                if(map[i][j] == '-' && map[i+1][j] == '=')
                {
                    map[i+1][j] = '~';//so shallow water is next to land
                }
            }
        }
        
        for(int i = 0;i < map.length;i++) {
            for(int j = 0;j < map[0].length;j++) {
                if(map[i][j] == '-')
                {
                    if((Math.random()*10) < 2.5)
                    {
                        map[i][j] = '+';
                    }
                    else if((Math.random()*10) < 5)
                    {
                        map[i][j] = ',';
                    }
                }
            }
        }
        return map;
    }
}