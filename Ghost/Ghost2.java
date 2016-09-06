package Ghost;

import Map.*;
import pacman.PacMan;
import pathfinding.*;

public class Ghost2 extends AllGhost {

    public Ghost2(Cell [][] board, Map amap, PacMan pMan) {
	ghostType = '2';
	a_Map = amap;
	this.board = board;
	this.aSTAR = new A_Star(board, a_Map.whichMap(), pMan);
	this.pMan = pMan;
	isInPosition = false;
	bumpIntoPacman = false;
	if (a_Map.whichMap() == 1) {
	    xPos = 21;
	    yPos = 16;
	    fixedPosX = 46;
	    fixedPosY = 5;
	    x_stepToMaze = 3;
	    y_stepToMaze = 2;
	    x_block_FROM = 24;
	    x_block_TO = 46;
	    y_block_FROM = 1;
	    y_block_TO = 14;
	}
	else if (a_Map.whichMap() == 2) {
	    xPos = 6;
	    yPos = 15;
	    fixedPosX = 20;
	    fixedPosY = 11;
	    x_stepToMaze = 3;  //Ghost2 starts moving after pacman walked 3 steps
	    y_stepToMaze = 0;
	    x_block_FROM = 10;
	    x_block_TO = 30;
	    y_block_FROM = 11;
	    y_block_TO = 21;
	}
    }
    @Override
    public void resetGhost() {
	// Ghost2 is not eaten by pacman, so the cell that Ghost2 stood 
	// has to reset to its previous state
        if (board[yPos][xPos].getDisplay() != 'C') {
            board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
        }
	
	isInPosition = false;
	bumpIntoPacman = false;
        a_path.clearEverything();
        chasingPacman.clearEverything();
	
	if (a_Map.whichMap() == 1) {
	    xPos = 21;
	    yPos = 16;
	    x_stepToMaze = 3;
	    y_stepToMaze = 2;
        }
	else if (a_Map.whichMap() == 2) {
	    xPos = 6;
	    yPos = 15;
	    x_stepToMaze = 3;
	}
	
	board[yPos][xPos].changeDisplay(ghostType);
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
		if (!chaseMode) moveToLocation();
		else {
		    if (!a_path.isPathEmpty()) a_path.clearEverything();
		    chasePacman(pMan.getX(),pMan.getY());
		    isInPosition = true;
		}
	    }
	} // !isInPosition
	else if (pMan.atePill()) {
	    if (!a_path.isPathEmpty()) a_path.clearEverything();
	    ghostPanicking();
	}
	else if (chaseMode 
		 || (a_Map.whichMap() == 1 && 28 < pMan.getX() && pMan.getX() < 44 
		     && 2 < pMan.getY() && pMan.getY() < 13)
		 || (a_Map.whichMap() == 2 && 16 < pMan.getX() && pMan.getX() < 28
		     && 12 < pMan.getY() && pMan.getY() < 19)) {
	    if (!a_path.isPathEmpty())a_path.clearEverything();
	    chasePacman(pMan.getX(), pMan.getY());
	}
	else {
	    if (!checkPacman()) movingInBlock();
	}	
    } //move

} //Ghost2