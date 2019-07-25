// i guess i can write code or whatever
// board generator
// note that when playing, the first spot is never a mine
// randomly generate a specific number of coordinates for mines
import java.util.Random;
/**
 * a class that contains everything about a minefield,
 * including a constructor and a reveal/movement method.
 * Implemented naively using QuickFind arrays. Performance
 * should not be an issue given a reasonable size of the data.
 * @author Andrew Zhang
 * @author Ella Schwarz
 */
public class Minefield implements MineMap {
    private int [][] map;
    private boolean [][] displayed;

    public Minefield(int sizeX, int sizeY, int initialX, int initialY) {
        map = new int[sizeX][sizeY];
        map[initialX][initialY] = 0;
        Random rng = new Random();
        // Number of Mines is 1/10 of the area
        int numberOfMines  = sizeX * sizeY / 10;
        // Generate the position of the mines
        for (int i = 0; i < numberOfMines; i++) {
            int randomX = rng.nextInt(sizeX);
            int randomY = rng.nextInt(sizeY);
            while (randomX == initialX && randomY == initialY) {
                randomX = rng.nextInt(sizeX);
                randomY = rng.nextInt(sizeY);
            }
            map[randomX][randomY] = -1;
        }
        // Generate the number displayed
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (map[i][j] >= 0) {
                    int mineCounter = 0;
                    for (int a = -1; a < 2; a++) {
                        for (int b = -1; b < 2; b++) {
                            try {
                                if (map[i + a][j + b] < 0) {
                                    mineCounter++;
                                }
                            } catch (ArrayIndexOutOfBoundsException e) { }
                        }
                    }
                    map[i][j] = mineCounter;
                }
            }
        }
        //figure out what to display
        figureOutDisplayed(initialX, initialY);
    }

    /**
     * a recursive helper method that displays bordering areas
     * that are not covered by mines should be called every time
     * a coordinate is revealed.
     * changes displayed value to true if the value is not a mine.
     * @param x x coordinate of the point to be checked
     * @param y y coordinate of the point to be checked
     */
    private void figureOutDisplayed(int x, int y) {
        if (map[x][y] < 0) {
            return;
        }
        displayed[x][y] = true;
        if (map[x][y] > 0) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!displayed[i + x][j + y] && map[i + x][j + y] >= 0) {
                    try {
                        figureOutDisplayed(i + x, j + y);
                    } catch (ArrayIndexOutOfBoundsException e) { }
                }
            }
        }
    }

    /**
     * A function that calls on the private helper method
     * in order to determine if the move is legal and
     * calculate the repercussions.
     * @param x x coordinate of the value checked
     * @param y y coordinate of the value checked
     * @return whether the value is a mine
     */
    @Override
    public boolean move(int x, int y) {
        if (map[x][y] < 0) {
            return false;
        }
        figureOutDisplayed(x, y);
        return true;
    }

    /**
     * method that returns an array of the information to be displayed through
     * the combination of this.displayed and this.map.
     * @return a 2D array of the information that can be displayed
     */
    @Override
    public int[][] getDisplayedMap() {
        int[][] displayMap = new int[map.length][map[0].length];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (this.displayed[x][y]) {
                    displayMap[x][y] = map[x][y];
                } else {
                    displayMap[x][y] = 0;
                }
            }
        }
        return displayMap;
    }

    /**
     * loop through the whole entire array in order to determine whether any
     * non-displayed tile is not a mine, and returns false
     * if a single spot is not displayed.
     * @return boolean that specify whether the game is won or not
     */
    @Override
    public boolean isGameWon() {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (!displayed[x][y]) {
                    if (map[x][y] >= 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
