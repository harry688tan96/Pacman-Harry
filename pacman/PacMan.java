package pacman;

import java.util.concurrent.TimeUnit;
import Map.*;

public class PacMan {
    private int xPos, yPos;
    private int lives, scores, rounds;
    private int number_pellets;
    private char player;
    private char ghostNo;  // the Ghost that ate PacMan
    private String currDirection;
    private boolean pill;   // pill is set to TRUE if PacMan eats the Power Pellet
    private boolean freeze; // freeze is set to TRUE if PacMan is froze by Ghost 3's Lazer
    private boolean loseALife;
    private boolean trap;  // trap is set to TRUE if PacMan is trapped in the Portal
    private boolean cheatPill, cheatLife;  //cheat codes available for PacMan
    private Portal a_Portal;
    private Cell [] [] board;
    private Map a_Map;
    private long powerPellet_start;  // to keep track of the time interval right after PacMan ate Power Pellet
    
    public PacMan(Cell [] [] board, Map amap) {
	lives = 2;
	scores = 0;
	rounds = 1;
	player = 'c';
	pill = false;
	freeze = false;
	loseALife = false;
	trap = false;
	cheatPill = false;
	cheatLife = false;
	ghostNo = 0;
	this.board = board;
	a_Map = amap;
	a_Portal = new Portal(board);
	
	if (a_Map.whichMap() == 1) {
	    currDirection = "w";
	    this.xPos = 22;
	    this.yPos = 25;
	    number_pellets = 374; 
	}
	else if (a_Map.whichMap() == 2) {
	    currDirection = "a";
	    this.xPos = 30;
	    this.yPos = 15;
	    number_pellets = 187;
	}
    }

    public void resetPlayer() {
	player = 'c';
	board[yPos][xPos].changeDisplay(player);
    }
    
    public void resetPacman() {
	board[yPos][xPos].changeDisplay(board[yPos][xPos].getPrevState());
	if (!cheatLife) lives--;
	if (!cheatPill) {
	    pill = false;
	    player = 'c';
	}
	freeze = false;
	loseALife = false;
	trap = false;
	ghostNo = 0;
	a_Portal.resetPortal();
	
	if (a_Map.whichMap() == 1) {
	    xPos = 22;
	    yPos = 25;
	    currDirection = "w";
	}
	else if (a_Map.whichMap() == 2) {
	    xPos = 30;
	    yPos = 15;
	    currDirection = "a";
	}
	board[yPos][xPos].changeDisplay(player);
    }
    
    public boolean getFreeze() {
	return freeze;
    }
    
    public void setFreeze() {
	freeze = true;
    }

    public void resetFreeze() {
	freeze = false;
    }
    
    public boolean isEaten() {
	return loseALife;
    }
    
    public void setEaten(char ghostNo) {
	this.ghostNo = ghostNo;
	loseALife = true;
    }

    public void eatAGhost() {
	scores+=1000;
    }
    
    public char eatenByGhost() {
	return ghostNo;
    }

    public boolean isTrapped() {
	return trap;
    }

    public void setTrapped() {
	trap = true;
    }    
    
    public int getLives() {
	return lives;
    }
    
    public void lose_A_Live() {
	lives--;
    }
    
    public int getScores() {
	return scores;
    }
    
    public int getRounds() {
	return rounds;
    }

    public void setRounds() {
	rounds++;
    }

    public int getX() {
	return xPos;
    }
    
    public int getY() {
	return yPos;
    }
    
    public String getDirection() {
	return currDirection;
    }
    
    public boolean pelletsAllEaten() {
	return number_pellets == 0;
    }

    public boolean atePill() {
	return pill;
    }
    
    public void setPillOn() {
	powerPellet_start = System.currentTimeMillis();
	pill = true;
    }
    
    private boolean checkPowerTime() {
	if (pill && !cheatPill) {
	    long powerPellet_end = System.currentTimeMillis();
	    long diff = powerPellet_end - powerPellet_start;
	    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
	    if (a_Map.whichMap() == 1) {
		if (seconds >= 30) return true;
	    }
	    else if (a_Map.whichMap() == 2) {
		if (seconds >= 15) return true;
	    }
	}
	return false;
    }

    public boolean usePortal1() {
	return a_Portal.getP1();
    }
    
    public boolean usePortal2() {
	return a_Portal.getP2();
    }
    
    public int portal1xPos() {
	return a_Portal.getP1_X();
    }

    public int portal1yPos() {
	return a_Portal.getP1_Y();
    }

    public int portal2xPos() {
	return a_Portal.getP2_X();
    }

    public int portal2yPos() {
	return a_Portal.getP2_Y();
    }
    
    public void setPortal1On() {
	a_Portal.setP1_ON();
    }
    
    public void setPortal1Off() {
	a_Portal.setP1_OFF();
    }

    public void setPortal2On() {
	a_Portal.setP2_ON();
    }
    
    public void setPortal2Off() {
	a_Portal.setP2_OFF();
    }
    
    public void portalOff(int x, int y) {
	a_Portal.setPortalOff(x,y);
    }

    public int getPortal1Num() {
	return a_Portal.getP1_Num();
    }
    
    public int getPortal2Num() {
	return a_Portal.getP2_Num();
    }
    
    public boolean isPortal(int x, int y) {
	return ((x == a_Portal.getP1_X() && y == a_Portal.getP1_Y())
		|| (x == a_Portal.getP2_X() && y == a_Portal.getP2_Y()));
    }
    
    public boolean isInPortal() {
	// returns true if pacman is currently inside the portal
	return a_Portal.checkPortal(xPos, yPos); 
    }

    private void usePortal(String portNo) {
	
	int x = xPos;
	int y = yPos;

	if (portNo.equals("p1")) {
	    if (a_Portal.getP1_Num() == 0) return ;
	}
	
	else if (portNo.equals("p2")) {
	    if (a_Portal.getP2_Num() == 0) return ;
	}
	
	for ( ;; ) {
	    if (currDirection.equals("w")) y--;
	    else if (currDirection.equals("s")) y++;
	    else if (currDirection.equals("a")) x--;
	    else if (currDirection.equals("d")) x++;

	    if (board[y][x].getDisplay() == '+') break;
	    
	    // A Ghost blocks the way when pacman wants to shoot the portal
	    if (board[y][x].isGhostHere()) break;

	    if (board[y][x].isWall()) {
		if (a_Map.whichMap() == 1) {
		    //the "PACMAN" heading in Map 1
		    if (x>=8 && x<=40 && y>=15 && y<=17) break; 
		}
		
		if (a_Map.whichMap() == 2) {
		    // Pacman tries to shoot Portal into Ghost 1 or Ghost 4's cage
		    if (x == 30) {  
			if (y == 2 || y == 28) break;
		    }
		    // Pacman tries to shoot Portal into Ghost 2 or Ghost 3's cage
		    if (y == 15) {
			if (x == 4 || x == 56) break;
		    }
		}
		
		if (currDirection.equals("w") || currDirection.equals("s")) {
		    if (board[y][x].getDisplay() == '|') break;
		}
		if (currDirection.equals("a") || currDirection.equals("d")) {
		    if (board[y][x].getDisplay() == '_') break;
		}		
		
		if (portNo.equals("p1")) {
		    //if portal 1 has already been initiated
		    if (a_Portal.getP1()) {
			a_Portal.resetPortalDisplay(1);
		    }
		    a_Portal.setPortal(x, y, 1, currDirection);
		} 
		else {
		    //if portal 2 has already been initiated 
		    if (a_Portal.getP2()) {   
			a_Portal.resetPortalDisplay(2);
		    }
		    a_Portal.setPortal(x, y, 2, currDirection);
		}
		board[y][x].changeDisplay('+');
		board[y][x].setMove();
		break;
	    } 
	} //for
    } //usePortal


    private void move(String dir) {
	int x = xPos;
	int y = yPos;

	if (freeze) {
	    if (!a_Map.getAttackMode()) freeze = false;
	    else {
		// if pacman wants to continue to move in the same direction 
		// (UP,DOWN, LEFT, RIGHT), the pacman will slow by 1 loop. 
		// In order words, it will freeze for one more time, 
		// then in the second loop, it only starts moving
		currDirection = dir;
		return;
	    }
	} //freeze

	for (int i=0; i<3; i++) {
	    if (dir.equals("w")) y--;
	    else if (dir.equals("s")) y++;
	    else if (dir.equals("a")) x--;
	    else if (dir.equals("d")) x++;

	    if (checkPowerTime()) {
		pill = false;
		resetPlayer();
	    }
	    	    
	    //it is a portal gate. 
	    if (a_Portal.checkPortal(x,y)) {
		// portal1 and portal2 has been initiated
		if (a_Portal.getP1() && a_Portal.getP2()) {
		    board[yPos][xPos].changeDisplay(' ');
		    if (x == a_Portal.getP1_X() && y == a_Portal.getP1_Y()) {
			xPos = a_Portal.getP2_X();
			yPos = a_Portal.getP2_Y();
			if (a_Portal.getP2_Exit().equals("w")) {
			    currDirection = "s";
			} else if (a_Portal.getP2_Exit().equals("s")) {
			    currDirection = "w";
			} else if (a_Portal.getP2_Exit().equals("a")) {
			    currDirection = "d";
			} else {
			    currDirection = "a";
			}
		    } else {
			xPos = a_Portal.getP1_X();
			yPos = a_Portal.getP1_Y();
			if (a_Portal.getP1_Exit().equals("w")) {
			    currDirection = "s";
			} else if (a_Portal.getP1_Exit().equals("s")) {
			    currDirection = "w";
			} else if (a_Portal.getP1_Exit().equals("a")) {
			    currDirection = "d";
			} else {
			    currDirection = "a";
			}
		    }
		} //if
		else {   //only one portal is initiated, so it stops moving
		    currDirection = dir;
		}
		break;
	    } //if

	    // test for walls
	    else if (!board[y][x].isValidMove()) {
		currDirection = dir;
		break;
	    }
	    
	    else if (board[y][x].getDisplay() == 'c' 
		|| board[y][x].getDisplay() == 'C') {
		dir = currDirection;
	    }
	    
	    else if (board[y][x].getPrevState() == 'O') {
		if (board[yPos][xPos].getDisplay() != '+') {
		    board[yPos][xPos].changeDisplay(' ');
		}
		scores += 100;
		xPos = x;
		yPos = y;
		currDirection = dir;
		setPillOn();
		player ='C';
		board[yPos][xPos].changeDisplay(player);
		number_pellets--;
		break;
	    } //else if
	    
	    else if (!(board[y][x].getPrevState() == 0 )) {
		if (board[y][x].getDisplay() == '.' ) {
		    scores += 100;
		    number_pellets--;
		}
		if (board[yPos][xPos].getDisplay() != '+') {
		    board[yPos][xPos].changeDisplay(' ');
		}
		xPos=x;
		yPos=y;
	 	currDirection = dir;
		board[yPos][xPos].changeDisplay(player);
		break;
	    } //else if
	   	    
	} //for
    } //move

    private void cheatcode(String cc) {
	if (cc.equals("infinite pope")) {
	    pill = true;
	    cheatPill = true;
	    freeze = false;
	    player = 'C';
	    board[yPos][xPos].changeDisplay(player);
	}
	else if (cc.equals("infinite life")) {
	    cheatLife = true;
	}
	else if (cc.equals("infinite port")) {
	    a_Portal.setCheatCode();
	}
    }

    public void action(String act) {
	if (act.equals("w") || act.equals("a") || act.equals("s") || act.equals("d"))
	    move(act);
	else if (act.equals("p1") || act.equals("p2")) usePortal(act);
	else if (act.equals("infinite pope") || act.equals("infinite life") 
		 || act.equals("infinite port")) cheatcode(act); 
	else move(currDirection);
    }

}