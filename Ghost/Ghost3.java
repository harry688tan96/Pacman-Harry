package Ghost;

import Map.*;
import pacman.PacMan;
import pathfinding.*;

public class Ghost3 extends AllGhost {

    private Path chasingPacman2 = new Path();
    private Path chasingPacman3 = new Path();
    private int pMan_prevX, pMan_prevY;
    private boolean lazer;

        public Ghost3(Cell [][] board, Map amap, PacMan pMan) {
	ghostType = '3';
	a_Map = amap;
	this.board = board;
	this.aSTAR = new A_Star(board, a_Map.whichMap(), pMan);
	this.pMan = pMan;
	isInPosition = false;
	bumpIntoPacman = false;
	pMan_prevX = pMan.getX();
	pMan_prevY = pMan.getY();
	lazer=false;
	if (a_Map.whichMap() == 1) {
	    xPos = 19;
	    yPos = 16;
	    fixedPosX = 2;
	    fixedPosY = 27;
	    x_stepToMaze = 5;
	    y_stepToMaze = 2;
	    x_block_FROM = 2;
	    x_block_TO = 24;
	    y_block_FROM = 18;
	    y_block_TO = 31;
	}
	else if (a_Map.whichMap() == 2) {
	    xPos = 54;
	    yPos = 15;
	    fixedPosX = 40;
	    fixedPosY = 17;
	    x_stepToMaze = 3; //Ghost3 starts moving after pacman has walked 3 steps
	    y_stepToMaze = 0;
	    x_block_FROM = 30;
	    x_block_TO = 50;
	    y_block_FROM = 11;
	    y_block_TO = 21;	    
	}
    }
    @Override
    public void resetGhost() {
	// Ghost3 is not eaten by pacman, so the cell that Ghost 3 stood 
	// has to reset to its previous state
        if (board[yPos][xPos].getDisplay() != 'C') {
            board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
	}
	if (lazer) resetCellDisplay();
	
	isInPosition = false;
	bumpIntoPacman = false;
	lazer = false;
	a_path.clearEverything();
        chasingPacman.clearEverything();
	chasingPacman2.clearEverything();
	chasingPacman3.clearEverything();
	
	if (a_Map.whichMap() == 1) {
	    xPos = 19;
	    yPos = 16;
	    x_stepToMaze = 5;
	    y_stepToMaze = 2;
	}
	else if (a_Map.whichMap() == 2) {
	    xPos = 54;
	    yPos = 15;
	    x_stepToMaze = 3;
	}
	
	board[yPos][xPos].changeDisplay(ghostType);
        
    }
	
    public void resetCellDisplay() {
	
	lazer = false;
	int i;
	int j;
	if (this.xPos < pMan_prevX) {
	    i = this.xPos+1;
	    j = pMan_prevX;
	}
	else if (pMan_prevX < this.xPos) {
	    i = pMan_prevX+1;
	    j = this.xPos;
	}
	else { // Ghost 3 touches Pacman, causing Pacman to lose a life
	    if (board[pMan_prevY][this.xPos-1].getDisplay() == '-') {
		board[pMan_prevY][this.xPos-1].setDisplay(' ');
	    }
	    else {
		board[pMan_prevY][this.xPos+1].setDisplay(' ');
	    }
	    return;
	}
	
	for ( ; i < j; i++) {
	    if (board[pMan_prevY][i].getPrevState() == 0) {
		board[pMan_prevY][i].setDisplay(' '); //must use setDisplay
	    }
	    else if (board[pMan_prevY][i].isGhostHere()) continue;
	    
	    // if the cell is used to be portal, change it back to '+'
	    else if (pMan.isPortal(i,pMan_prevY)) {
		board[pMan_prevY][i].changeDisplay('+');
	    }
	    else if (pMan.getY() == pMan_prevY && pMan.getX() == i) {
		board[pMan_prevY][i].changeDisplay('c');
	    }
	    else {
		char c = board[pMan_prevY][i].getPrevState();
		board[pMan_prevY][i].changeDisplay(c);
	    }
	} //for
    } //resetCellDisplay()
    
    private boolean ghostBlock() {
	AllGhost ghost1 = a_Map.getGhost(1);
	AllGhost ghost2 = a_Map.getGhost(2);
	AllGhost ghost4 = a_Map.getGhost(4);

	if (ghost1.getY() == yPos) {
	    // check whether Ghost 1 is in between Ghost 3 and pacman
	    if ((xPos < ghost1.getX() && ghost1.getX() < pMan.getX())
		|| (pMan.getX() < ghost1.getX() && ghost1.getX() < xPos))
		return true;
	}

	if (ghost2.getY() == yPos) {
	    // check whether Ghost 2 is in between Ghost 3 and pacman
	    if ((xPos < ghost2.getX() && ghost2.getX() < pMan.getX())
		|| (pMan.getX() < ghost2.getX() && ghost2.getX() < xPos))
		return true;
	}

	if (ghost4.getY() == yPos) {
	    // check whether Ghost 4 is in between Ghost 3 and pacman
	    if ((xPos < ghost4.getX() && ghost4.getX() < pMan.getX())
		|| (pMan.getX() < ghost4.getX() && ghost4.getX() < xPos))
		return true;
	}
	    

	return false;
    }

    private void moveToPacman() {
	if (yPos != pMan.getY()) {
	    int j = pMan.getY();
	    int k = this.xPos;
	    int a=k;
	    int b=k;
	    boolean sameDir = false;
	    
	    if (lazer && !pMan.getFreeze()) {
		resetCellDisplay();
	    }
	    
	    chasingPacman2.clearEverything();
	    chasingPacman3.clearEverything();
	    
	    // pacman moves into portal, return
	    if(pMan.isInPortal()) return;
	    
	    
	    if (board[j][k].isValidMove() && board[j][k].getPrevState()!=0) {
		chasingPacman2 = aSTAR.findPath(xPos, yPos, k, j,false);
		chasingPacman3 = chasingPacman2; //so no null pointer occurs
	    }
	    
	    else {
		// LoR stands for "Left or Right"
		for (int LoR=0, i=2; LoR<12; LoR++) {
		    if (a_Map.whichMap() == 1) {
			if (LoR%2==0) k=this.xPos-i;
			else {
			    k=this.xPos+i;
			    i+=2;
			}
		    }
		    else if (a_Map.whichMap() == 2) {
			if (pMan.getX()<this.xPos) k=this.xPos-i;
			else k=this.xPos+i;
			i+=2;
		    }
		    
		    
		    if (a_Map.whichMap() == 1) {
			//SCENARIO: imagine pacman at (42,16) 
			//          and Ghost3 at (2,27)
			if (k<=0 || k>=48) continue; 
		    }
		    else if (a_Map.whichMap() == 2) {
			if (k<=4 || k>=56) continue;
		    }
		    
		    if (board[j][k].isValidMove()
			&& board[j][k].getPrevState() != 0) {
			if (chasingPacman2.isPathEmpty()) {
			    if (k<this.xPos) sameDir = true;
			    chasingPacman2 = aSTAR.findPath(xPos, yPos, 
							    k, j,false);
			    a = k;
			    // just in case only one path is found
			    chasingPacman3 = chasingPacman2;
			    b = k;
			}
			else {
			    if ((k<this.xPos && sameDir)|| 
				(k>this.xPos && !sameDir)) continue;
			    else {
				chasingPacman3.clearEverything();
				chasingPacman3 = aSTAR.findPath(xPos, yPos, 
								k, j,false);
				b = k;
				break;
			    }
			}
		    } //if validMove()
		} //for
	    } //else
	    
	    if (chasingPacman2.getSize() <= chasingPacman3.getSize()) {
		chasePacman(a,j);
	    }
	    else chasePacman(b,j);
	    
	    // the following occurs if the Ghost moves a step
	    // and it falls on the same y-axis as the Pacman now
	    if (yPos == pMan.getY()) {
		if (!(pMan.isInPortal() || ghostBlock())) {
		    shootLazer(); 
		}
	    } 
	} //yPos != pMan.getY()
	
	//when Ghost3 falls on the same y-axis as the Pacman
	else  {   
	    // If portal coincidentally falls on the same y-axis,
	    // need to reset the Lazer
	    if (pMan.isInPortal()) {
		resetCellDisplay();
	    }
	    else if (ghostBlock()) {
		if (lazer) {
		    resetCellDisplay();
		    pMan.resetFreeze();
		}
	    }
	    else {
		shootLazer();
	    }
	} //else
    } //moveToPacman

    private void shootLazer() {
	// if pacman wants to continue to move in the same direction
	// (UP,DOWN, LEFT, RIGHT), the pacman will slow by 1 loop.
	// In order words, it will freeze for one more time,
	// then in the second loop, it only starts moving
	if (pMan.getFreeze()) {
	    pMan.resetFreeze();
	}
	
	else {
	    pMan.setFreeze();
	}
	
	//pacman moves to left or right
	if (pMan_prevY == pMan.getY()) {
	    if (pMan.getX() < pMan_prevX) { //pacman moves to left
		//reset the gap from '-' to ' '
		board[pMan_prevY][pMan_prevX-1].setDisplay(' ');
	    }
	    else if (pMan.getX() > pMan_prevX) { //pacman moves to right
		board[pMan_prevY][pMan_prevX+1].setDisplay(' ');
	    }
	}
	
	if (xPos < pMan.getX()) {
	    for (int i=xPos+1;i<pMan.getX(); i++) {
		board[yPos][i].changeDisplay('-');
	    }
	}
	else {
	    for (int i=pMan.getX()+1; i<xPos; i++) {
		board[yPos][i].changeDisplay('-');
	    }
	}
	
	pMan_prevX = pMan.getX();
	pMan_prevY = pMan.getY();
	lazer = true;
    } //shootLazer

    public boolean isLazerShot() {
	return lazer;
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
	} //isInPosition

	else if (pMan.atePill()) {
	    if (lazer) {
		resetCellDisplay();
		board[pMan.getY()][pMan.getX()].changeDisplay('C');
	    }
	    if (!a_path.isPathEmpty()) a_path.clearEverything();
	    ghostPanicking();
	}

	else if (chaseMode) {
	    if (!a_path.isPathEmpty()) a_path.clearEverything();
	    
	    if (checkPacman()) return;
	    
	    if (pMan.isEaten()) {
		resetCellDisplay();
		return;
	    }
	    moveToPacman();
	} //chaseMode
	
	else {
	    if (lazer) resetCellDisplay();
	    if (!checkPacman()) movingInBlock();
	}
    } //move

}//Ghost3