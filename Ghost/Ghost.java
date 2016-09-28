package Ghost;

import Map.*;
import pacman.PacMan;
import pathfinding.*;


public class Ghost extends AllGhost {
    
    private boolean foundASpot;

    public Ghost(Cell [][] board,Map amap, PacMan pMan) {
	ghostType = '1';
	a_Map = amap;
	this.board = board;
	this.aSTAR = new A_Star(board, a_Map.whichMap(), pMan);
	this.pMan = pMan;
	isInPosition = false;
	bumpIntoPacman = false;
	foundASpot = false;
	if (a_Map.whichMap() == 1) {
	    xPos = 22;
	    yPos = 16;
	    fixedPosX = 2;
	    fixedPosY = 5;
	    x_stepToMaze = 2;
	    y_stepToMaze = 2;
	    x_block_FROM = 2;
	    x_block_TO = 22;
	    y_block_FROM = 1;
	    y_block_TO = 14;
	}
	else if (a_Map.whichMap() == 2) {
            xPos = 30;
            yPos = 3;
            fixedPosX = 30;
            fixedPosY = 12;
            x_stepToMaze = 3;  //Ghost starts moving after pacman walked 3 steps
            y_stepToMaze = 0;
            x_block_FROM = 18;
            x_block_TO = 42;
            y_block_FROM = 5;
            y_block_TO = 14;
        }
    
    }
    @Override
    public void resetGhost() {
	// Ghost is not eaten by pacman, so the cell that Ghost stood 
	// has to reset to its previous state
        if (board[yPos][xPos].getDisplay() != 'C') {
            board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
        }
	
	isInPosition = false;
	bumpIntoPacman = false;
	foundASpot = false;
        a_path.clearEverything();
        chasingPacman.clearEverything();
	
	if (a_Map.whichMap() == 1) {
	    xPos = 22;
	    yPos = 16;
	    x_stepToMaze = 2;
	    y_stepToMaze = 2;
	}
	else if (a_Map.whichMap() == 2) {
	    xPos = 30;
	    yPos = 3;
	    x_stepToMaze = 3;
	}
        
	board[yPos][xPos].changeDisplay(ghostType);
    }
    
    private void checkSpot(int x, int y) {
	// if it is a wall / a gap / a portal gate / pacman who have eaten pill
	if (board[y][x].isWall() || board[y][x].getPrevState() == 0
	    || board[y][x].getDisplay() == '+' || board[y][x].getDisplay() == 'C') {
	    return ; 
	}
	
	if (board[y][x].isValidMove() && !board[y][x].isGhostHere()) {
	    foundASpot = true;
	    board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
	    xPos = x;
	    yPos = y;
	    board[yPos][xPos].changeDisplay(ghostType);
	}
    }
        
    private void smartMove() {
	
	int diffX = xPos - pMan.getX();
	int diffY = yPos - pMan.getY();
	int sqr =  (int) Math.sqrt(diffX * diffX + diffY *diffY);
	int left_or_right;
	int left_or_right2;
       
	if (sqr <= 5) {   //Pacman is within radius 5...AVOID!!!!!
	    if (xPos < pMan.getX()) {
		left_or_right = xPos - 2;
		left_or_right2 = xPos + 2;
	    }
	    else {
		left_or_right = xPos + 2;
		left_or_right2 = xPos - 2;
	    }
	    
	    if (yPos < pMan.getY()) {
		checkSpot(left_or_right, yPos); 
		if (!foundASpot) checkSpot(xPos, yPos-1); //top
		if (!foundASpot) checkSpot(left_or_right2, yPos); 
		if (!foundASpot) checkSpot(xPos, yPos+1); //bottom
	    }
	    else if (yPos > pMan.getY()) {
		checkSpot(left_or_right, yPos);
		if (!foundASpot) checkSpot(xPos, yPos+1); //bottom
		if (!foundASpot) checkSpot(left_or_right2, yPos); 
		if (!foundASpot) checkSpot(xPos, yPos-1); //top
	    }
	    else {
		checkSpot(left_or_right, yPos);
		if (!foundASpot) checkSpot(xPos, yPos-1); //top
		if (!foundASpot) checkSpot(xPos, yPos+1); //bottom
		if (!foundASpot) checkSpot(left_or_right2, yPos); 
	    }
	}
	else moveRandomly();
	foundASpot = false;
    } //smartMove
    
    @Override
    protected void ghostPanicking() {
	if (board[yPos][xPos].getDisplay() == 'C') {
	    pMan.eatAGhost();
	    resetGhost();
	}
	else {
	    smartMove(); 
	    // The Ghost moves around smartly and it 
	    // still bumps into Pacman
	    if (bumpIntoPacman) {
		pMan.eatAGhost();
		resetGhost();
	    }
	}
    }

    public void move() throws InterruptedException {
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
		else moveToLocation();
	    }
	}
	else if (pMan.atePill()) {
	    ghostPanicking();
	}
	else {
	    movingInBlock();
	}
	
    } //move
} //Ghost