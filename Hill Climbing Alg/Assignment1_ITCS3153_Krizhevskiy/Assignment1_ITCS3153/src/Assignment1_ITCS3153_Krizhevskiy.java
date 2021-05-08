

/**
 * Randomly generates 8x8 board with 8 queens and uses hill climbing 
 * to move the queens to locations where they do not conflict with one
 * another
 * 
 * @author Edward Krizhevskiy
 */
public class Assignment1_ITCS3153_Krizhevskiy {

    static int[] location; //stores the locations of the 8 queens
    static int H; //heuristic value of state
    static int stateChanges, restarts; //stores number of state change and board restarts

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        location = newBoard(); //creates inital state
        stateChanges = 0;
        restarts = 0;

        int lowerH = -1; //store number of neighbors with lower states
        while (true) {

            H = checkTotalHeuristic(location); //Checks heturistic of current state
            // if true then current state is the solution
            if (H == 0) {
                System.out.println("Current State");
                System.out.println(printBoard(location));
                System.out.println("Solution Found!"
                        + "\nState changes: " + stateChanges
                        + "\nRestarts: " + restarts);
                break;
            }
            System.out.println("\nCurrent h: " + H);
            System.out.println("Current State");
            System.out.println(printBoard(location));

            lowerH = hillClimbing(location); //executes hill climbing algorithm 

            System.out.println("Neighbors found with lower h: " + lowerH);
            // If a better state was not found, random restart
            if (lowerH == 0) {
                System.out.println("RESTART");
                restarts++;
                location = newBoard();
            } else {
                System.out.println("Setting new current state\n");
            }

        }//end while 

    }//end main

    /**
     * Creates a new board with 8 queens in random locations. The locations of
     * the queens are stored in a array where the index of the array is the
     * column of the queen (each queen has its own column) and the number stored
     * in that index is the row of the queen (like a x, y coordinate system)
     *
     * @return int [] with locations of queens
     */
    public static int[] newBoard() {

        int[] result = new int[8];

        for (int i = 0; i < result.length; i++) {
            result[i] = (int) (Math.random() * 8);
        }

        return result;
    }

    /**
     *  Takes a 2D array and prints out the board
     * 
     * @param board 2D board array
     * @return String of board
     */
    public static String printBoard(int board[][]) {
        String result = "";
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                result += board[row][col] + " , ";
            }
            result += "\n";
        }
        return result;
    }

    /**
     * Takes an array of locations and prints out the 2d board the 0 represents
     * a blank space and 1 represents a queen
     *
     * @param board Location array to print
     * @return String of board
     */
    public static String printBoard(int board[]) {
        String result = "";

        for (int i = 0; i < board.length * board.length; i++) {
            int row = i / board.length;
            int col = i % board.length;

            // prints queen when correct col and row are reached
            if (board[col] == row) {
                result += "1";
            } else {
                result += "0";
            }
            //format of board
            if (col == board.length - 1 && row != board.length - 1) {
                result += "\n";
            } else {
                result += ",";
            }
        }

        return result;
    }

    /**
     *  Takes the array containing locations of the 8 queens and
     * finds the Heuristic value of that state
     *
     * @param location Location array of queens
     * @return int Heuristic value of state
     */
    public static int checkTotalHeuristic(int[] location) {
        int count = 0;
        //get value of every queen
        for (int i = 0; i < location.length; i++) {
            count += checkHeuristic(location[i], i, location);

        }

        return count / 2;
    }

    /**
     * This checks the Heuristic value of one specific queen
     * 
     * @param row row of queen
     * @param col col of queen
     * @param location Location array of queens
     * @return int Heuristic value of queen
     */
    public static int checkHeuristic(int row, int col, int[] location) {
        int count = 0, y = -1, x = -1;
        //loop through other locations and compare
        for (int i = 0; i < location.length; i++) {
            if (i != col) {//only check queens in other columns
                x = (col - i);// find difference of columns and rows
                y = (location[i] - row);
                //if aboslute value of x and y are equal the 2 queens conflict
                //diagonally. If x is 0 the conflit in same column and y is
                //conflict in same row
                if (Math.abs(x) == Math.abs(y) || x == 0 || y == 0) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     *  evaluates heuristic value all of the possible neighbor states
     * and selects the lowest heuristic value than the current state
     * that state becomes the current state
     *
     * @param location Location array of queens
     * @return number of Neighbors found with lower H value
     */
    public static int hillClimbing(int[] location) {
        int count = 0, min = H, x = -1, y = -1, h = -1;
        int[][] heuristic2D = new int[8][8];//stores all neighbor heuristic values of current state
        int[] loc = location.clone(); //creat a copy of current state

        for (int i = 0; i < location.length * location.length; i++) {
            int row = i / location.length;
            int col = i % location.length;

            loc[col] = row;  //move the queen to neighbor location in the loc array
            h = checkTotalHeuristic(loc); //get Heuristic of the neighbor state
            heuristic2D[row][col] = h;//store H value 
            loc = location.clone(); //reset loc to current state

            if (h < min) {//if Heuristic of neighbor is lower than current
                count++;
                min = h;//set new minima  
                x = col;//save col and row position new queen location
                y = row;
            }
        }
        //set new current state if lower is found
        if (x != -1 || y != -1) {
            location[x] = y;
            stateChanges++;
        }
        
        //Uncomment this block of code if only if you want to see the H values of the current state
        //System.out.println(printBoard(heuristic2D));
        
        return count;
    }

}
