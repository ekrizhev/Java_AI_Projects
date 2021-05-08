/**
 * This class preforms a A* search from a start node to a goal node
 * in a 2D environment containing nodes. 
 * 
 * @author Edward Krizhevskiy
 */
import java.util.*;

public class AStar {

    private Node goal; //Stores goal node
    private Node current;//Stores current node being accessed
    private Node[][] envi;//2D environment of nodes to search through
    private PriorityQueue<Node> open;//Stores all discovered node but not accessed
    private Hashtable<Integer, Node> close;//Stores all the nodes the search accessed 
    private Stack<Node> path; //Stores path of node

    /**
     * Stores the goal node and node environment int proper 
     * fields. Places the start node in the open list
     * 
     * @param start Start point of search
     * @param goal end point of search
     * @param envi environment to be searched
     */
    public AStar(Node start, Node goal, Node[][] envi) {
        this.goal = goal;
        this.envi = envi;
        open = new PriorityQueue<>();
        close = new Hashtable<>();
        open.add(start);
        path = null;
    }

    /**
     *  Method run the A* search to find the path from the 
     *  start node to the goal node
     */
    public void search() {
        //search until path found or open list is empty
        while (open.size() > 0) {
            current = open.poll();//gets the node with the lowest f cost

            if (current.equals(goal)) {//check if goal is found
                break;
            }
            //generates/updates neighbors of current node
            generateNeighbors(current);
            //places current node in the close list
            close.put(current.getF(), current);

        }
        //after search complete clear open and close list
        close.clear();
        open.clear();
        
        //if path was found places a the node from the goal
        //to the start point in a stack
        if (current.equals(goal)) {
            path = new Stack<>();
            while (current != null) {
                path.push(current);
                current = current.getParent();//gets next node in path
            }
        } 

    }
    
    //finds all valid neighbors and add/updates openlist
    private void generateNeighbors(Node current) {
        //loop through all 8 possible neighbors
        for (int i = current.getRow() - 1; i <= current.getRow() + 1; i++) {
            for (int j = current.getCol() - 1; j <= current.getCol() + 1; j++) {
               //checks if current neighbor is vaild
                if (checkVaild(i, j)) {
                    int g;//Stores cost to move to this node from parent
                    //calcualtes g cost
                    if (i == current.getRow() || j == current.getCol()) {
                        g = (current.getG() + 10);
                    } else {
                        g = (current.getG() + 14);
                    }
                    //if current neighbor's G is 0 that means it has not been discover before
                    //if current neighbor's G is greater than the G just calcuated that means
                    //its in the openlist but has better G, so that node is updated
                    if (envi[i][j].getG() == 0 || g < envi[i][j].getG()) {
                        envi[i][j].setG(g);//set new G
                        envi[i][j].setH((Math.abs(goal.getCol() - envi[i][j].getCol()) //use Manhattan method to get Heuristic
                                + Math.abs(goal.getRow() - envi[i][j].getRow())) * 10);
                        envi[i][j].setF();//set new F
                        envi[i][j].setParent(current);//sets new parent
                        
                        //only adds to open list if it has not been discover before
                        //else it just updates what is currnetly in the openlist
                        if (!open.contains(envi[i][j])) {
                            open.add(envi[i][j]);
                        }
                    }
                }

            }//endloop
        }//end loop
    }//end generateNeighbors
    
    /*
    *   checks if neighbors id vaild using the row and column
    */
    private boolean checkVaild(int row, int col) {
        //checks if neighbor is outside of the environment
        if (col < 0 || row < 0 || col >= envi.length || row >= envi.length) {
            return false;
        } else if (envi[row][col].getType() == 1) { //checks if traverseable
            return false;
        } else if (envi[row][col].equals(current)) {// checks if its the current node
            return false;
        } else if (close.contains(envi[row][col])) {//checks if its in the closed list
            return false;
        }
        //if all checks passed then neighbors is vaild
        return true;
    }//end checkVaild
    
    /**
     *  returns the path from the start to the goal node
     * @return stack path
     */
    public Stack getPath(){
        return path;
    }//end Stack

}
