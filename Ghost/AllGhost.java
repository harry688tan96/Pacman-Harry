package Ghost;

import Map.*;
import pathfinding.*;
import pacman.PacMan;
import java.util.Random;

public abstract class AllGhost {
    protected char ghostType;
    protected int xPos, yPos;
    protected int fixedPosX, fixedPosY;
    protected Cell [] [] board;
    protected Map a_Map;
    protected PacMan pMan;
    protected int x_stepToMaze, y_stepToMaze;     
    protected int x_block_FROM, x_block_TO, y_block_FROM, y_block_TO;
    protected boolean isInPosition; //Ghost moves to their specified position
    protected boolean chaseMode;
    protected boolean bumpIntoPacman;
    protected A_Star aSTAR;
    protected Path a_path = new Path();
    protected Path chasingPacman = new Path();
    private Random rand = new Random(); //random number generator
    

    private char checkForGhost() {
	// If the other Ghost moves first and takes over his current position,
	// return that Ghost's display instead of the standing cell's prevState
	if (board[yPos][xPos].getDisplay() != ghostType) {
	    return board[yPos][xPos].getDisplay();
	}
	return board[yPos][xPos].getPrevState();
    }
    
    private boolean canGhostMove(int x, int y, char aGhost, char prevDisplay) {
	if (ghostType == '3') return false; //this is becuz ghost 3 is the last one to move.

	if (ghostType == '4') {
	    if (aGhost == '3') {
		moveToHere(x,y,prevDisplay);
		return true;
	    }
	}
	
	if (ghostType < aGhost) {
	    moveToHere(x,y,prevDisplay);
	    return true;
	}
	return false;
    }
    
    private void moveToHere(int x, int y, char prevDisplay) {
	board[yPos][xPos].changeDisplay(prevDisplay);  //reset previous display
	this.xPos = x; //change its current xPos to new position x
	this.yPos = y; //change its current yPos to new position y
	board[yPos][xPos].changeDisplay(ghostType);
    }

    protected void chasePacman(int player_x, int player_y) {
	if (checkPacman()) return;
	
	// Scenario: Pacman enters into the Portal and its current location
	//           is inside the Portal
	// The Ghost will choose to randomly move around until 
	// Pacman reappears on the Maze
	if (pMan.isInPortal()) {
	    moveRandomly();
	    return;
	}

	chasingPacman.clearEverything();
	// it recalculates the path everytime because pacman might
	// goes through portals
	try {
	    chasingPacman = aSTAR.findPath(xPos, yPos, player_x, player_y,false);
	    
	    int x = chasingPacman.getFirstX();
	    int y = chasingPacman.getFirstY();
	    char prevDisplay = checkForGhost();

	    
	    if (board[y][x].isGhostHere()) {
		if (!canGhostMove(x, y, board[y][x].getDisplay(), prevDisplay)) {
		    return;
		}
	    }
	    
	    else {
		moveToHere(x, y, prevDisplay);
	    }
	} catch (Exception e) {
	    System.err.println("Error occurs in chasePacman() method!!!");
	    e.printStackTrace();
	}
    } //chasePacman
    
    protected boolean checkPacman() {
	int x;
	int y;
	for (int i=0; i<15; i++) {
	    if (i == 2 || i == 5 || i == 9 || i == 12) {
	    
		x = xPos + (i%5 - 2);
		y = yPos + (i/5 - 1);
		
		//if pacman is not chasing the ghost, then the ghost can eat him
		if (board[y][x].getDisplay() == 'c') { 
		    attackPacman(x,y);
		    return true;
		}
	    } //if
	} //for
	return false;
    }

    protected void attackPacman(int x, int y) {
	char prevDisplay = checkForGhost();
	moveToHere(x, y, prevDisplay);
	pMan.setEaten(ghostType);
	a_Map.gameStatus(true); //has to reset everything
    }
    
    protected void moveRandomly() {
      	int x;
	int y;
	char prevDisplay = checkForGhost();
	
	int randomInt = rand.nextInt(4);
	
	boolean left = false;
	boolean right = false;
	boolean top = false;
	boolean bottom = false;
	
	for ( ;; ) {
	    x = xPos;
	    y = yPos;
	    if (randomInt == 0 && !top) {
		y--;
		top = true;
	    }
	    else if (randomInt == 1 && !right) {
		x+=2;
		right = true;
	    }
	    else if (randomInt == 2 && !bottom) {
		y++;
		bottom = true;
	    }
	    else if (randomInt == 3 && !left) {
		x-=2;
		left = true;
	    }
	    else {
		// if all FOUR directions are not valid movement,
		// then don't move at all
		if (top && bottom && left && right) break; 
		else {
		    randomInt = rand.nextInt(4);
		    continue;
		}
	    }
	    // it might be a big space or invalid movement
	    if (board[y][x].getPrevState() == 0
		|| !board[y][x].isValidMove()) {
		randomInt = rand.nextInt(4);
		continue;
	    }
	       
	    //the Ghost randomly wanders around and it bumps into pacman
	    else if (board[y][x].getDisplay() == 'C') {
		bumpIntoPacman = true;
		break;
	    }

	    else if (board[y][x].isGhostHere()) {
		if (top && bottom && left && right) break;
	    }
	    // if the cell is a valid move and not a portal
	    else if (board[y][x].isValidMove() && board[y][x].getDisplay() != '+') {
		moveToHere(x,y,prevDisplay);
		break;
	    }
	}
    } //moveRandomly()
    
    public abstract void resetGhost();

    protected void ghostPanicking() {
	if (board[yPos][xPos].getDisplay() == 'C') {
	    pMan.eatAGhost();
	    resetGhost();
	}
	else {
	    moveRandomly();
	    //The Ghost randomly wanders around and it bumps into Pacman
	    if (bumpIntoPacman) {
		pMan.eatAGhost();
		resetGhost();
	    }
	}
    }

    protected boolean moveToMaze() {
        if (a_Map.whichMap() == 1) {
	    if (x_stepToMaze != 0) {
		int x_tmp = xPos+1;
		if (board[yPos][x_tmp].isGhostHere()) return false; //not moving
		else {
		    if (board[yPos][xPos].getDisplay() == ghostType) { //no other Ghost takes over his existing position
			board[yPos][xPos].changeDisplay(' ');
		    }
		    xPos = x_tmp;
		    x_stepToMaze--;
		}
		
		if (board[yPos][xPos].getDisplay() == 'C') {
		    pMan.eatAGhost();
		    resetGhost();
		}
		else board[yPos][xPos].changeDisplay(ghostType);
		
		return false;
	    }
	    else if (y_stepToMaze != 0) {
		if (board[yPos][xPos].getDisplay() == 'C') { //the Ghost is eaten by pacman while it's moving into maze
		    pMan.eatAGhost();
		    resetGhost();
		    return false;
		}
		int y_tmp=yPos;
		if (ghostType == '3' || ghostType == '4') y_tmp++;
		else y_tmp--;
		
		if (board[y_tmp][xPos].isGhostHere()) return false; //not moving
		else {
		    if (board[yPos][xPos].getDisplay() == ghostType) { //no other Ghost takes over his position
			board[yPos][xPos].changeDisplay(' ');
		    }
		    yPos = y_tmp;
		    y_stepToMaze--;
		}
		
		if (board[yPos][xPos].getDisplay() == 'C') {
		    pMan.eatAGhost();
		    resetGhost();
		}
		else board[yPos][xPos].changeDisplay(ghostType);
		
		return false;
	    }
	}
	else if (a_Map.whichMap() == 2) {
	    if (x_stepToMaze > 1) {
		x_stepToMaze--;
		return false;
	    }
	    // time for the Ghost to move to the maze
	    else if (x_stepToMaze == 1) {
		int x_tmp = xPos;
		int y_tmp = yPos;
		if (ghostType == '1') y_tmp++;
		else if (ghostType == '2') x_tmp+=2;
		else if (ghostType == '3') x_tmp-=2;
		else if (ghostType == '4') y_tmp--;
		
		if (board[y_tmp][x_tmp].isGhostHere()) return false; //not moving
		else {
		    if (board[yPos][xPos].getDisplay() == ghostType) { //no other Ghost takes over his position
			board[yPos][xPos].changeDisplay(' ');
		    }
		    xPos = x_tmp;
		    yPos = y_tmp;
		    x_stepToMaze--;
		}
		
		if (board[yPos][xPos].getDisplay() == 'C') {
		    pMan.eatAGhost();
		    resetGhost();
		}
		else board[yPos][xPos].changeDisplay(ghostType);
		
		return false;
	    }	    
	}	    
	
	// The Ghosts are in the maze now.
	return true;
	
    } //moveToMaze

    protected void moveToLocation() throws InterruptedException {
	// The Ghost is not in its specified position
	if (xPos != fixedPosX || yPos != fixedPosY) {
	    // a path to the specified location has not been found yet
	    if (a_path.isPathEmpty()) {
		a_path = aSTAR.findPath(xPos, yPos, fixedPosX, fixedPosY,false);
	    }
	    
	    int x = a_path.getFirstX();
	    int y = a_path.getFirstY();
	    char prevDisplay = checkForGhost();
	    
	    // pacman stops and blocks the way when the ghost is trying
	    // to move to its respective position
	    if (board[y][x].getDisplay() == 'c') {
		attackPacman(x, y);
	    }
	    else if (board[y][x].isGhostHere()) {
		if (!canGhostMove(x, y, board[y][x].getDisplay(), prevDisplay)) {
		    a_path.prependSteps(x,y);
		}
	    }
	    else {
		// validMovement
		moveToHere(x,y,prevDisplay);
	    }
	}
	
	// if the Ghost has reached its specified position, 
	// isInPosition is set to true
	// so the Ghost can start moving in block now
	if (xPos == fixedPosX && yPos == fixedPosY) {
	    a_path.clearEverything();
	    isInPosition = true;
	} 
    } //moveToLocation()

    public void movingInBlock() {
	try {
	    // a path to the specified location has not been found yet
	    if (a_path.isPathEmpty()) {
		
		int randomX;
		int randomY;
		
		do {
		    randomX = x_block_FROM 
			+ (int)(Math.random() * (x_block_TO - x_block_FROM + 1));
		    randomY = y_block_FROM
			+ (int)(Math.random() * (y_block_TO - y_block_FROM + 1));
		} while (!board[randomY][randomX].isValidMove()
			 || board[randomY][randomX].getDisplay() == '+'
			 || board[randomY][randomX].getPrevState() == 0);
		
		// if the generated randomX and randomY are the Ghost's current 
		// posX and posY respectively, the Ghost would choose 
		// not to move...
		if (randomX == xPos && randomY == yPos) return;
		else a_path = aSTAR.findPath(xPos, yPos, randomX, randomY,false);
	    }
	    
	    
	    int x = a_path.getFirstX();
	    int y = a_path.getFirstY();
	    char prevDisplay = checkForGhost();
	    
	    // pacman stops and blocks the way when the ghost is trying
	    // to move to its respective position
	    if (board[y][x].getDisplay() == 'c') {
		attackPacman(x, y);
	    }
	    
	    else if (board[y][x].isGhostHere()) {
		if (!canGhostMove(x, y, board[y][x].getDisplay(), prevDisplay)) {
		    a_path.prependSteps(x,y);
		}
	    }

	    else {
		// validMovement and there are no Ghosts blocking his way
		moveToHere(x,y,prevDisplay);
	    }
	    
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    } //movingInBlock()
    

    public boolean getChaseMode() {
	return chaseMode;
    }

    public void setAttack() {
	chaseMode = true;
    }

    public void resetAttack() {
	chaseMode = false;
    }

    public int getX() {
	return xPos;
    }

    public int getY() {
	return yPos;
    }
    
} //AllGhost