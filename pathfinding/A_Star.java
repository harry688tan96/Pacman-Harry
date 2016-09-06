package pathfinding;

import java.util.Collections;
import java.util.ArrayList;
import Map.Cell;
import pacman.PacMan;

public class A_Star {

    private ArrayList <Cell> open;
    private ArrayList <Cell> close;
    private PacMan pMan;
    private Cell [] [] board;
    private int mapNo;

    public A_Star(Cell [][] board, int mapNo, PacMan pMan) {
	this.board = board;
	this.mapNo = mapNo;
	this.pMan = pMan;
	open = new ArrayList <Cell>();
	close = new ArrayList <Cell>();
    }

    private void resetAllCosts() {
	if (mapNo == 1) {
	    for (int row=0; row<33; row++) {
		for (int col=0; col<50; col++) {
		    board[row][col].reset_A_Star();
		}
	    }
	}
	else if (mapNo == 2) {
	    for (int row=0; row<31; row++) {
		for (int col=0; col<62; col++) {
		    board[row][col].reset_A_Star();
		}
	    }
	}
    }


    public Path findPath (int xStart, int yStart, int xGoal, int yGoal, 
			  boolean findPortal) {
	// Initial state for A*: 
	//      Only the starting tile is in the open list. 
	//      The close list is empty. All cells' costs are zero.
	resetAllCosts();
	close.clear();
	open.clear();
	open.add(board[yStart][xStart]);
	Collections.sort(open);
	
	while (open.size() != 0 ) {
	    Cell current =  open.get(0);
	    if (current.getX() == xGoal && current.getY() == yGoal){
		break;
	    }

	    open.remove(current);
	    close.add(current);
	    
	    int xPos;
	    int yPos;
	    
	    for (int i=0; i<15; i++) {
		int x = (i%5)-2;  //explain it later, it is in page 4 in the book
		int y = (i/5)-1;
		
		// it is the current tile
		if (x==0 && y==0) continue;
		
		//no diagonal movement is allowed in the maze
		if (x!=0 && y!=0) continue;

		//evaluating the location of the neighbour
		xPos = x + current.getX();
		yPos = y + current.getY();
				
		if (board[yPos][xPos].isValidMove()) {
		    if (board[yPos][xPos].getPrevState() == 0) continue;
		    
		    // Scenario:The Ghost(in chase mode) is searching for Pacman 
		    //     When the Ghost detects a portal gate,which is a 
		    //     validMove, it will ignore that portal gate since 
		    //     Ghost could not travel into the Portal. Hence, it 
		    //     continues to search for Pacman with another path
		    if (!findPortal) {
			if (pMan.isPortal(xPos, yPos)) continue;
		    }
		    
		    // the cost to get to this node is the current cost 
		    // plus the movement cost to reach this node. 
		    // Note that the heursitic value is only used in the 
		    // sorted open list
		    float nextStepCost = current.getCost() + get_G_Cost();
		    Cell neighbour = board[yPos][xPos];
		    
		    // if the new cost we've determined for this node 
		    // is lower than it has been previously, make sure 
		    // the node hasn't determined that there might have been 
		    // a better path to get to this node so it needs to be 
		    // re-evaluated
		    if (nextStepCost < neighbour.getCost()) {
			if (open.contains(neighbour)) {
			    open.remove(neighbour);
			}
			if (close.contains(neighbour)) {
			    close.remove(neighbour);
			}
		    }

		    // if the node hasn't already been processed and discarded, 
		    // then reset its cost to our current cost. Add it as a 
		    // next possible step to the open list
		    
		    if (!open.contains(neighbour) && !close.contains(neighbour)){
			neighbour.setCost(nextStepCost);
			float heuristic_cost = get_H_Cost(xPos,yPos,xGoal,yGoal);
			neighbour.setHeuristic(heuristic_cost);
			neighbour.setParent(current);
			open.add(neighbour);
			Collections.sort(open);
			// The following can be used to check how
			// the A* algorithm works
			//if (findPortal) board[yPos][xPos].changeDisplay('x');
		    }
		} //if
	    } //for
	} //while
	
	// since we've run out of search 
	// there was no path. Just return null
	if (board[yGoal][xGoal].getParent() == null) {
	    return null;
	}
	
	// At this point we've definitely found a path so we can use the parent
	// references of the nodes to find out way from the target location back
	// to the start recording the nodes on the way.
	Path path = new Path();
	Cell target = board[yGoal][xGoal];
	while (target.getParent() != null) {
	    path.prependSteps(target.getX(), target.getY());
	    target = target.getParent();
	}
	// we have our path 
       	open.clear();
	close.clear();
	
       	return path;
    
    } //findPath


    // Movement Cost
    // Note: Only horizontal and vertical movements are allowed, so +1.
    public int get_G_Cost() {
	return 1;
    }

    //Heuristic Cost
    public float get_H_Cost(int xCurrent, int yCurrent, int xGoal, int yGoal){
	// (xGoal - xCurrent) is divided by 2 becuz don't forget there is a 
	// gap between every two points in x-axis
	float dx = Math.abs((xGoal - xCurrent)/2);
	float dy = Math.abs(yGoal - yCurrent);
	
	return dx+dy;  //MUST use Manhattan distance!!!
    }

} //A_star