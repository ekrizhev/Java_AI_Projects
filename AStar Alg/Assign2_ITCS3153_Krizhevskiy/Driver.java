
/**
 * Uses the java GUI to display the 15x15 environment and allows the
 * used to click the start and goal node the shows if the path is found
 * or not found using an A* search
 * 
 * 
 * @author Edward Krizhevskiy
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Driver extends JFrame {

    private int spacing = 3;
    private Node[][] enviroment;//holds nodes
    private int[][] locations;//holds locations of traverseable and non-traverseable nodes
    private Node start, goal;
    private Stack<Node> path;
    private boolean searched;
    private int count = 0;

    private int mouseX;
    private int mouseY;

    /**
     * sets up environment and runs the search and animation 
     * 
     * @throws InterruptedException
     */
    public Driver() throws InterruptedException {
        //sets up display 
        this.setSize(1000, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        
        //sets up board and mouse
        Board board = new Board();
        this.setContentPane(board);
        Click click = new Click();
        this.addMouseListener(click);
        
        //initalize feilds
        searched = false;
        locations = new int[15][15];
        enviroment = new Node[15][15];
        start = new Node(-1, -1, -1);
        goal = new Node(-1, -1, -1);
        
        //fill locations array
        fillEnviroment(locations);
        //uses locations array to fill enviroment
        fillEnviromentNode(enviroment, locations);
        
        while (true) {//loop until user quits
            if (start.getType() == -1 || goal.getType() == -1) {
                this.repaint();
            } else {
                runSearch();//run search after start and goal are selected
            }
        }
    }

    /**
     *  
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new Driver();
    }
    
    //preform the A* search
    private void runSearch() throws InterruptedException {

        AStar searchAStar = new AStar(start, goal, enviroment);
        searchAStar.search();//begin search
        searched = true;
        path = searchAStar.getPath();//get path
        if (path != null) {//if there is a path animate it
            while (!path.isEmpty()) {
                this.repaint();
                start = path.pop();
                TimeUnit.MILLISECONDS.sleep(250);
                System.out.println(start);
            }
        }else{//if no path found (message will display)
            this.repaint();
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        //reset variable for next search
        fillEnviromentNode(enviroment, locations);
        count = 0;
        start = new Node(-1, -1, -1);
        goal = new Node(-1, -1, -1);
        searched = false;
    }//end search
    
    //randomly generate locations of non-transverable nodes
    //if the array contains a 1 then that location is non-transverable
    private void fillEnviroment(int[][] x) {
        //loop through array
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                //random number between 0 and 99
                int rand = (int) (Math.random() * 100);
                //10% chance node is non-transverable
                if (rand < 10) {
                    x[i][j] = 1;
                    
                } else {
                    x[i][j] = 0;
                    
                }
            }
        }
    }//end fill

    //uses the locations array to fill the node array
    private void fillEnviromentNode(Node[][] x, int[][]locations) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                if (locations[i][j] == 1) {
                    x[i][j] = new Node(i, j, 1);//non-transverable
                } else {
                    x[i][j] = new Node(i, j, 0);//transverable
                }
            }
        }
    }//end fill

    /**
     *  class that determines what is displayed by GUI
     */
    public class Board extends JPanel {

        /**
         *  //paints the GUI
         * @param g
         */
        public void paint(Graphics g) {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, 1250, 850);
            g.setColor(Color.WHITE);
            //draws a grid of squares with various color determining purpose
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (enviroment[j][i].getType() == 1) {//if non-transverable
                        g.setColor(Color.BLACK);
                    } else if (start.getRow() == j && start.getCol() == i) {//if start node
                        g.setColor(Color.green);
                    } else if (goal.getRow() == j && goal.getCol() == i) {//if goal node
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    //determines where the square is drawn and how big
                    g.fillRect(spacing + i * 40, spacing + j * 40, 40 - 2 * spacing, 40 - 2 * spacing);

                }
            }
            //user instructions
            g.setColor(Color.white);
            g.setFont(new Font("Tahoma", Font.PLAIN, 25));
            if (count == 0) {
                g.drawString("Click tile to set START point", 650, 150);
            } else if (count == 1) {
                g.drawString("Click tile to set GOAL point", 650, 150);
            }
            //displays result
            if (searched) {
                if (path != null) {
                    g.drawString("Path found", 700, 150);
                } else {
                    g.drawString("No path could be found", 650, 150);
                }
            }
        }//end paint
    }//end board

    /**
     *  Class allows user to click GUI
     */
    public class Click implements MouseListener {

        /**
         * Clicks determine start and goal node
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            //gets position of mouse
            mouseX = e.getX();
            mouseY = e.getY();
            System.out.println("Mouse click " + mouseX + " " + mouseY);
            
            //determine which square the mouse clicks
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                        if (mouseX >= spacing + i * 40 + 7 && mouseX < spacing + i * 40 + 40
                                && mouseY >= spacing + j * 40 + 30 && mouseY < spacing + j * 40 + 40 + 26) {
                            if (enviroment[j][i].getType() == 0) {//if  transverable
                                if(count == 0){//first click
                                    start = new Node(j, i, 0);//start node
                                }
                                else if(count==1){//second click
                                     goal = new Node(j, i, 0);//goal node
                                }
                            }

                        }
                }//end loop
            }//end loop
            
            //determines when to increase click count so
            //invaild clicks dont count
            if (start.getType() > -1 && count == 0) {
                count++;
            } else if (goal.getType() > -1 && count == 1) {
                count++;
            }

        }
        
        //unused methods from interface
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}

    }//end mouse class

}//end display driver class
