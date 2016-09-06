package Ghost;

import Map.*;
import pacman.PacMan;
import pathfinding.*;

public class Ghost4 extends AllGhost{
    
    private Path findPathToPortal_1 = new Path();
    private Path findPathToPortal_2 = new Path();
    private int portal1_X, portal1_Y, portal2_X, portal2_Y;

    private void portalLocation(int portalNo) {
	if (portalNo == 1) {
	    // SCENARIO 1: portal 1 was just created. It has not been found by Ghost 4 yet
	    // SCENARIO 2: portal 1 has changed its location
	    if (portal1_X != pMan.portal1xPos() 
		|| portal1_Y != pMan.portal1yPos()
		|| findPathToPortal_1.isPathEmpty()) {
		findPathToPortal_1.clearEverything();
		findPathToPortal_1 = aSTAR.findPath(xPos, yPos, pMan.portal1xPos(),
						    pMan.portal1yPos(),true);
		portal1_X = pMan.portal1xPos(); 
		portal1_Y = pMan.portal1yPos();
	    }
	}
	else if (portalNo == 2) {
	    // SCENARIO 1: portal 2 was just created. It has not been found by Ghost 4 yet
	    // SCENARIO 2: portal 2 has changed its location
	    if (portal2_X != pMan.portal2xPos() 
		|| portal2_Y != pMan.portal2yPos()
		|| findPathToPortal_2.isPathEmpty()) {
		findPathToPortal_2.clearEverything();
		findPathToPortal_2 = aSTAR.findPath(xPos, yPos, pMan.portal2xPos(),
						    pMan.portal2yPos(),true);
		portal2_X = pMan.portal2xPos();
		portal2_Y = pMan.portal2yPos();
	    }
	}

    } //portalLocation

    private void ghostApproachPortal() throws InterruptedException {
	if (pMan.usePortal1() && pMan.usePortal2()) {
	    portalLocation(1);
	    portalLocation(2);
	    
	    // portal 1 is closer compare to portal 2
	    if (findPathToPortal_1.getSize() <= findPathToPortal_2.getSize()) {
		int x = findPathToPortal_1.getFirstX();
		int y = findPathToPortal_1.getFirstY();
		ghostClosePortal(x,y,1);
	    }
	    
	    // portal 2 is closer
	    else {
		int x = findPathToPortal_2.getFirstX();
		int y = findPathToPortal_2.getFirstY();
		ghostClosePortal(x,y,2);
	    }
	} 	
	else if (pMan.usePortal1()) {
	    portalLocation(1);
	    int x = findPathToPortal_1.getFirstX();
	    int y = findPathToPortal_1.getFirstY();
	    ghostClosePortal(x,y,1);
	}
	else if (pMan.usePortal2()) {
	    portalLocation(2);
	    int x = findPathToPortal_2.getFirstX();
	    int y = findPathToPortal_2.getFirstY();
	    ghostClosePortal(x,y,2);
	}
    } //ghostApproachPortal
   
	    
    private void ghostClosePortal(int x, int y, int whichPortal) {
	if (board[y][x].getDisplay() == '+') { 
	    board[y][x].changeDisplay(board[y][x].getPrevState());
	    board[y][x].resetMove();
	    
	    if (pMan.isInPortal()) {
		pMan.setTrapped();  //Ghost 4 traps the pacman in the portal
		a_Map.gameStatus(true); //reset the game
	    }
	    
	    if (whichPortal == 1) {
		portal1_X = 0;
		portal1_Y = 0;
		pMan.setPortal1Off();
		findPathToPortal_1.clearEverything();
		if (pMan.usePortal2()) {        
		    findPathToPortal_2.clearEverything(); //first,reset the path to portal 2,then only find the path
		    findPathToPortal_2 = aSTAR.findPath(xPos, yPos, pMan.portal2xPos(), pMan.portal2yPos(),true);
		}
	    }
	    else if (whichPortal == 2) {
		portal2_X = 0;
		portal2_Y = 0;
		pMan.setPortal2Off();
		findPathToPortal_2.clearEverything();
		if (pMan.usePortal1()) {
		    findPathToPortal_1.clearEverything(); //first,reset the path to portal 1,then only find the path
		    findPathToPortal_1 = aSTAR.findPath(xPos, yPos, pMan.portal1xPos(), pMan.portal1yPos(),true);
		}
	    }
	} // board[y][x].getDisplay() == '+'
	else {
	    board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
	    xPos = x;
	    yPos = y;
	    board[yPos][xPos].changeDisplay(ghostType);
	}
	
    } //ghostClosePortal

    private boolean checkPortalAround() {
	int x;
	int y;
	for (int i=0; i<15; i++) {
	    if (i == 2 || i == 5 || i == 9 || i == 12) {
		x = xPos + (i%5 - 2);
		y = yPos + (i/5 - 1);
		
		//if pacman is not chasing the ghost, then the ghost can eat him
		if (board[y][x].getDisplay() == '+') { 
		    pMan.portalOff(x,y);
		    board[y][x].changeDisplay(board[y][x].getPrevState());
		    board[y][x].resetMove();
		    return true;
		}
	    } //if
	} //for
	return false;
    } //checkPortalAround
    
    @Override
    protected void ghostPanicking(){
	if (board[yPos][xPos].getDisplay() == 'C') {
	    pMan.eatAGhost();
	    resetGhost();
	}
	else if (!checkPortalAround()) {
	    moveRandomly();
	    // the Ghost randomly wanders around and it bumps into pacman
	    if (bumpIntoPacman) {
		pMan.eatAGhost();
		resetGhost();
	    }
	}
    }

    public Ghost4(Cell [][] board, Map amap, PacMan pMan) { 
	ghostType = '4';
	a_Map = amap;
	this.board = board;
       	this.aSTAR = new A_Star(board, a_Map.whichMap(), pMan);
	this.pMan = pMan;
	portal1_X = 0;
	portal1_Y = 0;
	portal2_X = 0;
	portal2_Y = 0;
	isInPosition = false;
	bumpIntoPacman = false;
	if (a_Map.whichMap() == 1) {
	    xPos = 20;
	    yPos = 16;
	    fixedPosX = 46;
	    fixedPosY = 27;
	    x_stepToMaze = 4;
	    y_stepToMaze = 2;
	    x_block_FROM = 26;
	    x_block_TO = 46;
	    y_block_FROM = 18;
	    y_block_TO = 31;
	}
	else if (a_Map.whichMap() == 2) {
	    xPos = 30;
	    yPos = 27;
	    fixedPosX = 30;
	    fixedPosY = 18;
	    x_stepToMaze = 3; //Ghost4 starts moving after pacman walked 3 steps
	    y_stepToMaze = 0;
	    x_block_FROM = 22;
	    x_block_TO = 40;
	    y_block_FROM = 18;
	    y_block_TO = 25;
	}
    }
    @Override
    public void resetGhost() {
	// Ghost4 is not eaten by pacman, so that cell that he stood 
	// has to reset to its previous state
        if (board[yPos][xPos].getDisplay() != 'C') {
            board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
        }
        
	isInPosition = false;
	bumpIntoPacman = false;
        a_path.clearEverything();
        chasingPacman.clearEverything();
	findPathToPortal_1.clearEverything();
	findPathToPortal_2.clearEverything();

	if (a_Map.whichMap() == 1) {
	    xPos = 20;
	    yPos = 16;
	    x_stepToMaze = 4;
	    y_stepToMaze = 2;
        }
	else if (a_Map.whichMap() == 2) {
	    xPos = 30;
	    yPos = 27;
	    x_stepToMaze = 3;
	}
	
	board[yPos][xPos].changeDisplay(ghostType);
	portal1_X = 0;
	portal1_Y = 0;
	portal2_X = 0;
	portal2_Y = 0;
    }


    public void move() throws InterruptedException{
	
	// pacman bumps into Ghost
        if (board[yPos][xPos].getDisplay() == 'c') {
            attackPacman(xPos, yPos);
	}

	
	else if (!isInPosition) {
	    if (moveToMaze()) {
		if (pMan.atePill()) {
		    isInPosition = true;
		    ghostPanicking();
		    return;
		}
		if (!chaseMode) moveToLocation();
		else {
		    if (!checkPortalAround()) {
			if (!a_path.isPathEmpty()) a_path.clearEverything();
			if (!findPathToPortal_1.isPathEmpty()) findPathToPortal_1.clearEverything();
			if (!findPathToPortal_2.isPathEmpty()) findPathToPortal_2.clearEverything();
			chasePacman(pMan.getX(),pMan.getY());
			isInPosition = true;
		    }
		}
	    }
	} // !isInPosition
	
	// Condition 1: When it is not in hostile mode and no portal(s) is generated, 
	// it randomly moves around the specified block
	// Condition 2: When it is in hostile mode, unless the portal is 
	// within its radius, it doesn't care at all in closing the portals
	// and it will start to panic and scatter around.
	
	else if (pMan.atePill()) {
	    if (!a_path.isPathEmpty()) a_path.clearEverything();
	    ghostPanicking();
	}
	
	else if (chaseMode) {
	    if (!a_path.isPathEmpty()) a_path.clearEverything();
	    if (!findPathToPortal_1.isPathEmpty()) findPathToPortal_1.clearEverything();
	    if (!findPathToPortal_2.isPathEmpty()) findPathToPortal_2.clearEverything();
	    if (!checkPortalAround()) {
		chasePacman(pMan.getX(), pMan.getY());
	    }
	}

	else if (!(pMan.usePortal1() || pMan.usePortal2())) {
	    if (!checkPacman()) {
		movingInBlock();
	    }
	}

	// The following method
	// foundPortal_1 is used to prevent this Ghost from calculating 
	// the same path everytime when it moves towards the portal. 
	else if (pMan.usePortal1() || pMan.usePortal2()) {
	    if (!checkPacman()) {
		ghostApproachPortal();
	    }
	}
    } //move
    
} //Ghost4

