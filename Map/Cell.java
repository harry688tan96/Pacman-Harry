package Map;

public class Cell implements Comparable <Cell> {
    private int xPos, yPos;
    private float cost;     //The path cost for this cell
    private float heuristic;    //The heuristic cost of this cell
    private Cell parent;   //The parent of this cell
    private char display;
    private char prevState;
    private boolean validMove;
    //private boolean visit_or_not;

    //    private GameObject contents;

    public Cell() {
	cost = 0;
	heuristic = 0;
	parent = null;
	display = ' ';
	prevState = ' ';
	validMove = false;
    }
    
    public Cell(int xPos, int yPos) {
	this.xPos = xPos;
	this.yPos = yPos;
    }

    public int getX() {
	return xPos;
    }

    public int getY() {
	return yPos;
    }

    public void setParent(Cell parent) {
	this.parent = parent;
    }
    
    public Cell getParent() {
	return parent;
    }

    public void setDisplay(char _display_) {
	display = _display_;
	prevState = _display_;
	if (_display_=='.' || _display_=='O' || _display_ == 'c') {
	    validMove = true;
	}
	else if (_display_ == ' ') {
	    validMove = true;
	    prevState = 0;   //null character
	}
	else if (_display_ == ':') {
	    display = ' ';
	    prevState = 0;
	}

    } //setDisplay

    public void changeDisplay(char _display_) {
	display = _display_;
	// if changeDisplay does not fall in PACMAN heading
	if (display == ' ') prevState = ' ';
    }

    public char getDisplay(){
	return display;
    }
    
    public char getPrevState() {
	return prevState;
    }

    public void setMove(){
	validMove = true;
    }
    
    public void resetMove() {
	validMove = false;
    }
    
    public boolean isValidMove(){
	return validMove;
    }
    
    public boolean isGhostHere() {
	return (display == '1' || display == '2' || display == '3'
		|| display == '4');
    }

    public boolean isWall() {
	return (display == '#' || display == '|' || display == '_'
		|| display == '/' || display == '\\');
    }
    

    public float getCost() {
	return cost;
    }

    public void setCost(float cost) {
	this.cost = cost;
    }

    public void setHeuristic(float heuristic) {
	this.heuristic = heuristic;
    }
    
    public void reset_A_Star() {
	cost = 0;
	heuristic = 0;
	parent = null;
    }
    
    @Override
    public int compareTo(Cell obj) {
	Cell neigh = obj;
	float f = heuristic + cost;
	float neigh_f = neigh.heuristic + neigh.cost;
	
	if (f<neigh_f) return -1;
	else if (f>neigh_f) return 1;
	else return 0;
	
    }

    @Override
    public boolean equals(Object obj) {
	Cell aCell = (Cell) obj;
	return (this.xPos == aCell.getX() && this.yPos == aCell.getY());
    }
}