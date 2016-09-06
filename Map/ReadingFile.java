package Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class ReadingFile {
    
    static void readfile(Cell [][] board, int mapNo) {
	FileReader reader = null;
	
	try {
	    if (mapNo == 1) {
		File file = new File("Map/pacman map.txt");
		String currentDir = file.getAbsolutePath();
		int c=0;	    
		reader = new FileReader(file);
	    
		for (int row=0; row<33; row++) {
		    for (int col=0; col<50; col++) {
			c = reader.read();
			board[row][col].setDisplay((char) c);
		    }
		}
	    } //mapNo == 1
	    
	    else if (mapNo == 2) {
		File file = new File("Map/pacman map 2.txt");
		String currentDir = file.getAbsolutePath();
		int c=0;	    
		reader = new FileReader(file);
	    
		for (int row=0; row<31; row++) {
		    for (int col=0; col<62; col++) {
			c = reader.read();
			board[row][col].setDisplay((char) c);
		    }
		}
	    } //mapNo == 2
	    
	} catch (IOException e) {
	    System.err.println("IOException");
	    e.printStackTrace();

	} finally {
	    if (reader != null) {
		try { 
		    reader.close();
		} catch (Exception e) {}
	    } 
	} //finally	      
    } //readfile method
} //ReadingFile