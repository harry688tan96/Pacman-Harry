package Map;

import java.util.Scanner;
import java.util.Formatter;
import java.lang.Thread;
import java.util.concurrent.TimeUnit;
import pacman.PacMan;
import Ghost.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Map {
    private String input = "";
    private boolean startGame = true;
    private Cell [][] board;
    private PacMan pMan;
    private Ghost ghost_1;
    private Ghost2 ghost_2;
    private Ghost3 ghost_3;
    private Ghost4 ghost_4;
    private int mapNo;
    
    private long timeToAttack;
    private long timeToAttack2;
    private long timeToFlipMap;
    private long timeToFlipMap2;
    private boolean attackMode = false;
    private boolean flipMode = false;
    private int stage = 1;    
    
    private void setInput(String input) {
	this.input = input;
    }

    private void createMap(int num_row, int num_col, int mapNo) {
	board = new Cell [num_row][num_col];
	
	for (int row=0; row<num_row; row++) {
	    for (int col=0; col<num_col; col++) {	
		board[row][col] = new Cell(col, row);  //col is xPos and row is yPos
	    }
	}
	ReadingFile.readfile(board, mapNo);
    }

    private String addColour(char achar) {
	String tmp = "";
	if (achar == 'c' || achar == 'C') {
	    tmp += "\u001B[33m"; // yellow
	}
	else if (achar == '1') {
	    tmp += "\u001B[31m"; //red
	}
	else if (achar == '2') {
	    tmp += "\u001B[35m"; //purple
	}
	else if (achar == '3') {
	    tmp += "\u001B[32m"; //green
	}
	else if (achar == '4') {
	    tmp += "\u001B[36m"; //cyan
	}
	else if (achar == '+') {
	    tmp += "\u001B[34m"; //blue
	}
	tmp += achar + "\u001B[0m";
	return tmp;
    }

    private String addColour(char achar, int lazerColour) {
	String tmp = "";
	tmp += "\u001B[" + lazerColour + "m" + achar + "\u001B[0m";
	return tmp;
    }
	    
    private void printMap() {	
	System.out.println("\033[2J"); // clear the existing output screen before
	                               // printing a new Map
	
	int lazerColour = 0;
	if (ghost_3.isLazerShot()) lazerColour = 31;
	
	if (mapNo == 1) {
	    	    
	    // Array of Strings are used because it can be printed out smoothly
	    String [] ss = new String[33];
	    
	    for (int row=0; row<33; row++) {
		ss[row]="";
		for (int col=0; col<50;col++) {
		    if (board[row][col].getDisplay() == '-') {
			if (lazerColour == 37) lazerColour = 31; // back to RED
			ss[row]+=addColour(board[row][col].getDisplay(),
					   lazerColour);
			lazerColour++;
		    }
		    else ss[row]+=addColour(board[row][col].getDisplay());
		}
	    }
	    
	    //Thread.sleep(1000);
	    System.out.println();
	    for (int row=0; row<33; row++) {
		System.out.print(ss[row]);
	    }
	    
	} // (mapNo == 1)
	
	else if (mapNo == 2) {
	    // Array of Strings are used because it can be printed out smoothly
	    String [] ss = new String[31];
	    
	    if (!flipMode) {
		for (int row=0; row<31; row++) {
		    ss[row]="";
		    for (int col=0; col<62;col++) {
			if (board[row][col].getDisplay() == '-') {
			    if (lazerColour == 37) lazerColour = 31; //back to RED
			    ss[row]+=addColour(board[row][col].getDisplay(),
					       lazerColour);
			    lazerColour++;
			}
			else ss[row]+=addColour(board[row][col].getDisplay());
		    }
		}
	    }
	    // if it is flipMode, reverse printing...
	    else if (flipMode) {
		for (int row=0; row<31; row++) {
		    ss[row]="";
		    for (int col=61; col>=0; col--) {
			char c = board[row][col].getDisplay();
			if (c == '\\')  ss[row] += '/';
			else if (c == '/')  ss[row] += '\\';
			else if (c == '-') {
			    if (lazerColour == 37) lazerColour = 31; // back to RED
			    ss[row]+=addColour(c, lazerColour);
			    lazerColour++;
			}
			else  ss[row] += addColour(c);
		    }
		}
	    }
	    //print out the Map    
	    System.out.println();
	    for (int row=0; row<31; row++) {
		System.out.print(ss[row]);
	    }
	    
	} // (mapNo == 2)
	
	String scores = "Scores: " + pMan.getScores();
	String lives = "Lives: " + pMan.getLives();
	String rounds = "Rounds: " + pMan.getRounds();
	String _input_ = "Input: " + input;
	String pPortal = "P1: " + pMan.getPortal1Num() + "  " 
	    + "P2: " + pMan.getPortal2Num();
	String ghostAttackMode;
	if (ghost_4.getChaseMode()) {
	    ghostAttackMode = "Ghost Chase Mode: ON!!!";
	}
	else ghostAttackMode = "";
	String firstRow = String.format("%1$-5s %2$-20s %3$-45s",
					"", scores, _input_);
	String secondRow = String.format("%1$-5s %2$-20s %3$-45s",
					 "", lives, pPortal);
	String thirdRow = String.format("%1$-5s %2$-20s %3$-45s",
					"", rounds, ghostAttackMode);
	
	System.out.println();
	System.out.println(firstRow);
	System.out.println(secondRow);
	System.out.println(thirdRow);
	System.out.println();

    } //printMap

    
    private void countingDown() throws InterruptedException {
	for (int countdown = 3; countdown != 0; countdown--) {
	    Thread.sleep(1000);
	    System.out.print(countdown);
	    if (countdown == 3) System.out.println("...");
	    else if (countdown == 2) System.out.println("..");
	    else System.out.println(".");
	}
	Thread.sleep(1000);
	printMap();
    }
    
    private void nextRound(int round) {
	if (round == 0) { //round 0 is the first round
	    System.out.println("Entering the 2nd round ...");
	}
	else if (round == 1) {
	    System.out.println("You Won!!!");
	    System.out.println("Thanks for playing.");
	}
    }
    
    private void gameStart() throws InterruptedException {
	//if pacman is eaten by Ghost or trapped in Portal,everything is reset
	if (pMan.isEaten() || pMan.isTrapped()) {
	    Thread.sleep(1500);
	    if (pMan.isTrapped()) {
		System.out.println("Ooops!!! You are trapped in the Portal!");
	    }
	    else {
		System.out.println("HAHAHA...You are eaten by Ghost "
				   + pMan.eatenByGhost());
	    }
	    // if the Ghosts are in still in chase mode..RESET ghosts' attack
	    if (ghost_4.getChaseMode()) {
		ghostResetAttack();
		attackMode = false;
		stage++;
	    }
	    ghost_1.resetGhost();
	    ghost_2.resetGhost();
	    ghost_3.resetGhost();
	    ghost_4.resetGhost();
	    pMan.resetPacman();
	    input = "";
	    
	    if (pMan.getLives() == -1) {
		Thread.sleep(1500);
		System.out.println("You Lost!!! Too bad...");
		System.exit(0);
	    }
	    
	    if (flipMode) {
		flipMode = false;
	    }
	    Thread.sleep(1500);
	    System.out.println("Restarting ....");
	    countingDown();
	}
	else {
	    Scanner scan = new Scanner(System.in);
	    String tmp;
	    do {
		printMap();
		Thread.sleep(800);
		System.out.println("Please adjust according to Map " + mapNo + "'s Dimension for the best gaming experience.");
		Thread.sleep(1000);
		System.out.print("Successfully adjusted the dimension? (y): ");
		tmp = scan.nextLine();
	    } while (!tmp.equals("y"));
	    Thread.sleep(1000);
	    System.out.println();
	    System.out.println("The game begins in ....");
	    countingDown();
	}
	if (mapNo == 2) {      //timer starts for map flipping
	    timeToFlipMap = System.currentTimeMillis();
	}
	timeToAttack=System.currentTimeMillis(); //timer starts for Ghost attack
	startGame = false;
    }
    
    private void ghostResetAttack() {
	ghost_1.resetAttack();
	ghost_2.resetAttack();
	ghost_3.resetAttack();
	ghost_4.resetAttack();
    }

    private void ghostSetAttack() {
	ghost_1.setAttack();
	ghost_2.setAttack();
	ghost_3.setAttack();
	ghost_4.setAttack();
    }

    private void checkTime() {
	timeToAttack2 = System.currentTimeMillis();
	long diff = timeToAttack2 - timeToAttack;
	long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
	
	if (mapNo == 2) {
	    timeToFlipMap2 = System.currentTimeMillis();
	    diff = timeToFlipMap2 - timeToFlipMap;
	    long seconds2 = TimeUnit.MILLISECONDS.toSeconds(diff);
    	    if (flipMode) {
		// flip the map every 20 seconds
		if (seconds2 >= 20) {
		    flipMode = false;
		    timeToFlipMap = System.currentTimeMillis();
		}
	    }
	    else if (!flipMode) {
		if (seconds2 >=20) {
		    flipMode = true;
		    timeToFlipMap = System.currentTimeMillis();
		}
	    }
	} //mapNo==2
		
	if (attackMode) {
	    // Stage 5 onwards, unlimited time of attackMode!!!
	    if (stage >= 5) {
		return ;
	    }		
	    else if (seconds >= 30) {
		ghostResetAttack();
		timeToAttack = System.currentTimeMillis();
		attackMode = false;
		stage++;
	    }
	} //attackMode
	else if (!attackMode) {
	    if ((stage == 1 && seconds >= 30)
		|| (stage == 2 && seconds >= 25)
		|| (stage == 3 && seconds >= 20) 
		|| (stage == 4 && seconds >= 15)
		|| (stage >= 5 && seconds >= 5)) {
		ghostSetAttack();
   		timeToAttack = System.currentTimeMillis();
		attackMode = true;
	    }
	}
    } //checkTime()
    
    public Map(int map) {
	this.mapNo = map;
	if (mapNo == 1) {
	    createMap(33,50,1);
	}
	else if (mapNo == 2) {
	    createMap(31,62,2);
	}
	
	pMan = new PacMan(board, this);
	ghost_1 = new Ghost(board, this, pMan);
	ghost_2 = new Ghost2(board, this, pMan);
	ghost_3 = new Ghost3(board, this, pMan);
	ghost_4 = new Ghost4(board, this, pMan);
    } //Map

    public boolean isGameStart() {
	return startGame;
    }
    
    public void gameStatus(boolean gS) {
	startGame = gS;
    }

    public int whichMap() {
	return mapNo;
    }

    public boolean getFlipMode() {
	return flipMode;
    }
    
    public boolean getAttackMode() {
	return ghost_4.getChaseMode();
    }

    public AllGhost getGhost(int ghostNo) {
	if (ghostNo == 1) return ghost_1;
	else if (ghostNo == 2) return ghost_2;
	else if (ghostNo == 3) return ghost_3;
	else if (ghostNo == 4) return ghost_4;
	
	return null;
    }


    public static void main(String [] args) throws InterruptedException{
	try {
	    Scanner scanRead = new Scanner(System.in);
	    System.out.println("Please input the Map sequence...");
	    int firstMap = -1;
	    int secondMap = -1;
	    
	    for (int i=0;i<3; ) {
		if (i == 2) {
		    Thread.sleep(750);
		    System.out.println("The map sequence is: " + 
				       firstMap + " --> " + secondMap);
		    i++;
		    Thread.sleep(1500);
		} //(i == 2) 	    
		else if (i == 1) {
		    System.out.print("Second Map: ");
		    String tmp = scanRead.nextLine();
		    if (tmp.equals("1") || tmp.equals("2")) {
			secondMap = Integer.parseInt(tmp);
			i++;
		    }
		    else {
			System.out.println();
			System.err.println("Invalid second Map detected!!!");
			System.err.println("Try Again!!!");
		    }
		    System.out.println();
		} // (i == 1)
		else if (i == 0) {
		    System.out.print("First Map: ");
		    String tmp = scanRead.nextLine();
		    if (tmp.equals("1") || tmp.equals("2")) {
			firstMap = Integer.parseInt(tmp);
			i++;
		    }
		    else {
			System.out.println();
			System.err.println("Invalid first Map detected!!!");
			System.err.println("Try Again!!!");
		    }
		    System.out.println();
		} // (i == 0)	    
	    } // for(int i=0;i<3; )
	    
	    Map amap = new Map(firstMap);
	    
	    String command;
	    InputStreamReader reader = new InputStreamReader(System.in);
	    BufferedReader buf = new BufferedReader(reader);
	    
	    for (int round=0; round<2; round++) {  //number of rounds
		
		while (true) {
		    
		    if (amap.isGameStart()) {
			amap.gameStart();
			if (reader.ready()) {
			    // just in case the player inputs something before
			    // the game starts
			    command = buf.readLine(); 
			}
		    } //amap.isGameStart
		    
		    else if (amap.pMan.pelletsAllEaten()) {
			Thread.sleep(1500);
			amap.nextRound(round);
			Thread.sleep(1500);
			if (round == 0) {
			    amap = new Map(secondMap);
			    amap.pMan.setRounds();
			}
			break;
		    }
		    
		    
		    else {
			amap.checkTime();
			Thread.sleep(1000);
			if (!reader.ready()) {
			    amap.pMan.action(amap.pMan.getDirection());
			}
			else {
			    command = buf.readLine();
			    if (command.equals("cc")) {
				System.out.print("Please input a cheat code: ");
				command = scanRead.nextLine();
				if (!(command.equals("infinite pope")
				      || command.equals("infinite life")
				      || command.equals("infinite port"))) {
				    System.err.println("Invalid cheatcode!!!");
				    Thread.sleep(1000);
				}
			    }
			    amap.setInput(command);
			    amap.pMan.action(command);
			} 
			
			amap.ghost_1.move();
			amap.ghost_2.move();
			amap.ghost_4.move();  // I set ghost_4 moves before
			amap.ghost_3.move();  // ghost_3 because lazer is only shot
			                      // after all ghosts have moved
			
			// have to reset the cell display if Ghost4 eats 
			// the pacman after the lazer has shot
			if (amap.ghost_3.isLazerShot() && amap.pMan.isEaten()) { 
			    amap.ghost_3.resetCellDisplay();
			}
			amap.printMap();
			
		    } //else
		} //while
	    } //for
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
} //Map