package assignment;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
	private boolean[][] state;
	TetrisPiece nextPiece;
	Board.Action lastAction;
	Board.Result lastResult;
	int rowsCleared;
	int maxHeight;
	
	
    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
    	state = new boolean[height][width];
    	nextPiece = null;
    	lastAction = Board.Action.NOTHING;
    	lastResult = Board.Result.NO_PIECE;
    }

    @Override
    public Result move(Action act) {
    	if (nextPiece == null)
    		return Result.NO_PIECE;
    	
    	return Result.SUCCESS;
    }

    @Override
    public Board testMove(Action act) { 
    	return null; 
    }

    @Override
    public void nextPiece(Piece p) {
    	nextPiece = (TetrisPiece) p;
    }

    @Override
    public boolean equals(Object other) {
    	TetrisBoard otherBoard = (TetrisBoard) other;
    	boolean[][] otherBoardState = otherBoard.getState();
    	
    	for (int r = 0; r < state[0].length; r++) {
    		for (int c = 0; c < state.length; c++) {
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
    	return state.length; 
    }

    @Override
    public int getHeight() {
    	return state[0].length;
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
    	return state[x][y];
    }
    
    public boolean valid(int x, int y) {
    	return x >= 0 && x < getWidth() && y >=0 && y < getHeight();
    }

}
