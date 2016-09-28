package pacman;

import Map.*;

class Portal {
    private boolean portal1, portal2;
    private boolean cheatPort;
    private String portal1Exit, portal2Exit;
    private int portal1_xPos, portal1_yPos;
    private int portal2_xPos, portal2_yPos;
    private int portal1Num, portal2Num; // the usage amount for Portal 1 and Portal 2 that is available
    private char port1PrevState, port2PrevState;
    private Cell [] [] board;
    

    public Portal(Cell [][] board) {
	portal1 = false;
	portal2 = false;
	cheatPort = false;
	portal1Num = 3;
	portal2Num = 3;
	this.board = board;

    }

    public void resetPortal() {
	if (portal1){
	    resetPortalDisplay(1);
	    portal1 = false;
	}
	if (portal2) {
	    resetPortalDisplay(2);
	    portal2 = false;
	}
	portal1_xPos = portal1_yPos = portal2_xPos = portal2_yPos = 0;
    }

    public void resetPortalDisplay(int portalNO) {
	if (portalNO == 1) {
	    board[portal1_yPos][portal1_xPos].resetMove();
	    board[portal1_yPos][portal1_xPos].changeDisplay(port1PrevState);
	}

	else if (portalNO == 2) {
	    board[portal2_yPos][portal2_xPos].resetMove();
	    board[portal2_yPos][portal2_xPos].changeDisplay(port2PrevState);
	    
	}
    }

    public void setPortal(int x, int y, int portNo, String exit) {
	if (portNo == 1) {
	    portal1 = true;
	    portal1_xPos = x;
	    portal1_yPos = y;
	    portal1Exit = exit;
	    port1PrevState = board[y][x].getDisplay();
	    if (!cheatPort) portal1Num--;
	} else {
	    portal2 = true;
	    portal2_xPos = x;
	    portal2_yPos = y;
	    portal2Exit = exit;
	    port2PrevState = board[y][x].getDisplay();
	    if (!cheatPort) portal2Num--;
	}
    }

    public void setPortalOff(int x, int y) {
	if (x == portal1_xPos && y == portal1_yPos) {
	    setP1_OFF();
	} else if (x == portal2_xPos && y == portal2_yPos) {
	    setP2_OFF();
	}
    }

    public boolean checkPortal(int x, int y) {
	return ((x == portal1_xPos && y == portal1_yPos)
		|| (x == portal2_xPos && y == portal2_yPos));
    }

    public boolean getP1() {
	return portal1;
    }
    
    public boolean getP2() {
	return portal2;
    }
    
    public int getP1_X() {
	return portal1_xPos;
    }

    public int getP1_Y() {
	return portal1_yPos;
    }

    public int getP2_X() {
	return portal2_xPos;
    }

    public int getP2_Y() {
	return portal2_yPos;
    }

    public void setP1_ON() {
	portal1 = true;
    }
    
    public void setP1_OFF() {
	portal1 = false;
    }

    public void setP2_ON() {
	portal2 = true;
    }
    
    public void setP2_OFF() {
	portal2 = false;
    }

    public String getP1_Exit() {
	return portal1Exit;
    }

    public String getP2_Exit() {
	return portal2Exit;
    }

    public int getP1_Num() {
	return portal1Num;
    }
    
    public int getP2_Num() {
	return portal2Num;
    }

    public void setCheatCode() {
	cheatPort = true;
    }

} //Portal