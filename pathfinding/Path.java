package pathfinding;

import java.util.Stack;

public class Path {
    private Stack<Integer> xPos;
    private Stack<Integer> yPos;

    public Path() {
	xPos = new Stack<Integer>();
	yPos = new Stack<Integer>();
    }
    
    public void prependSteps(int x, int y) {
	xPos.push(x);
	yPos.push(y);
    }

    public void clearEverything() {
	xPos.clear();
	yPos.clear();
    }

    public int getFirstX() {
	return xPos.pop();
    }
    
    public int getFirstY() {
	return yPos.pop();
    }
    
    public int getSize() {
	// only test on xPos, since xPos and yPos will always have the same amount of elements
	return xPos.size();
		}

    public boolean isPathEmpty() {
	// only test on xPos, since xPos and yPos will always have the same amount of elements 
	return xPos.empty();
    }
		    
}
