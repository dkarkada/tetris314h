package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
	private boolean[][] state;
	private int width;
	private int height;
	TetrisPiece nextPiece;
	Board.Action lastAction;
	Board.Result lastResult;
	int rowsCleared;
	int maxHeight;
	
	
    // JTetris will use this constructor
    public TetrisBoard(int w, int h) {
    	width = w;
    	height = h;
    	state = new boolean[height][width];
    	nextPiece = null;
    	lastAction = Board.Action.NOTHING;
    	lastResult = Board.Result.NO_PIECE;
    }

    @Override
    public Result move(Action act) {
    	if (nextPiece == null)
    		return Result.NO_PIECE;
    	Pivot loc = nextPiece.location;
    	Pivot center = nextPiece.getPivot(); 
    	switch(act) {
    		case NOTHING:
    			return Result.SUCCESS;
    		case DOWN:
    			loc.y -= 1;
    			int rowBelow = (int) (loc.y - center.y - 1);
    			int startX = (int) (loc.x - center.x);
    			for(int x = startX; x < startX + nextPiece.getWidth(); x++) {
    				int i = (int) (x - loc.x + center.x);
    				int y = rowBelow + nextPiece.getSkirt()[i];
    				if(getGrid(x, y)) {
    					place();
    					return Result.PLACE;
    				}
    			}
    			return Result.SUCCESS;
    		case LEFT:
    			loc.x -= 1;
    			return Result.SUCCESS;
    		case RIGHT:
    			nextPiece.location.x += 1;
    			return Result.SUCCESS;
    		case COUNTERCLOCKWISE:
    			nextPiece = (TetrisPiece) nextPiece.nextRotation();
    			nextPiece.location = loc;
    			return Result.SUCCESS;
    		case CLOCKWISE:
    			nextPiece = (TetrisPiece) nextPiece.prevRotation();
    			nextPiece.location = loc;
    			return Result.SUCCESS;
    		case DROP:
    			Result r = null;
    			while(r != Result.PLACE) {
    				r = move(Action.DOWN);
    			}
    			return r;
    	}
    	return Result.SUCCESS;
    }

    @Override
    public Board testMove(Action act) { 
    	return null;
    	//TODO implement
    }

    @Override
    public void nextPiece(Piece p) {
    	nextPiece = (TetrisPiece) p;
		TetrisPiece.createCircularLL(nextPiece);
    	nextPiece.initLocation(height, width/2);
    }

    @Override
    public boolean equals(Object other) {
    	TetrisBoard otherBoard = (TetrisBoard) other;
    	boolean[][] otherBoardState = otherBoard.getState();
    	
    	for (int r = 0; r < height; r++) {
    		for (int c = 0; c < width; c++) {
    			if (!valid(r,c) || !(otherBoard.valid(r,c))) {
    				return false;
    			}
    			else if (state[r][c] != otherBoardState[r][c]) {
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    public boolean[][] getState() {
    	return state;
    }

    @Override
    public Result getLastResult() { 
    	return lastResult; 
    }

    @Override
    public Action getLastAction() { 
    	return lastAction; 
    }

    @Override
    public int getRowsCleared() { 
    	return -1; 
    }

    @Override
    public int getWidth() { 
    	return width; 
    }

    @Override
    public int getHeight() {
    	return height;
    }

    @Override
    public int getMaxHeight() { return -1; }

    @Override
    public int dropHeight(Piece piece, int x) { return -1; }

    @Override
    public int getColumnHeight(int x) { return -1; }

    @Override
    public int getRowWidth(int y) { return -1; }

    @Override
    public boolean getGrid(int x, int y) {
    	if (!valid(x, y))
    		return true;
    	if (nextPiece != null) {
	    	Point[] piece = nextPiece.getBody();
	    	Pivot center = nextPiece.getPivot();
	    	for (Point p : piece) {
	    		int px = (int) (p.x - center.x + nextPiece.location.x);
	    		int py = (int) (p.y - center.y + nextPiece.location.y);
	    		if (px == x && py == y)
	    			return true;
	    	}
    	}
    	return state[yToRow(y)][xToCol(x)];
    }
    
    public boolean valid(int x, int y) {
    	return x >= 0 && x < width && y >=0 && y < height;
    }
    private int xToCol(int x) {
    	return x;
    }
    private int yToRow(int y) {
    	return height - y - 1;
    }
    
    private void place() {
    	Point[] piece = nextPiece.getBody();
    	Pivot center = nextPiece.getPivot();
    	for (Point p : piece) {
    		int x = (int) (p.x - center.x + nextPiece.location.x);
    		int y = (int) (p.y - center.y + nextPiece.location.y);
    		state[yToRow(y)][xToCol(x)] = true;
    	}
    	nextPiece = null;
    }
}
