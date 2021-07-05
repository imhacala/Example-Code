
public class Move {
	int pieceRow;
	int pieceCol;
	int destRow;
	int destCol;

	/**
	*	creates a move by setting all the necessary variables
	*
	*	@param r piece's current row
	*	@param c piece's current col
	*	@param dr piece's desired row
	*	@param dc piece's desired col
	**/
	Move(int r, int c, int dr, int dc)
	{
		pieceRow = r;
		pieceCol = c;
		destRow = dr;
		destCol = dc;
	}

	/**
	*	Sets this move to given move
	*
	*	@param m given move
	**/
	Move(Move m)
	{
		if(m != null) {
			pieceRow = m.pieceRow;
			pieceCol = m.pieceCol;
			destRow = m.destRow;
			destCol = m.destCol;
		}
	}

	/**
	*	Compares given move to this move, checks if they are the same
	*
	*	@param o given move
	**/
	@Override
    public boolean equals(Object o) {
		if(o==null)
			return false;
		Move m = (Move) o;
		if(this.pieceRow == m.pieceRow && this.pieceCol == m.pieceCol &&
				this.destRow == m.destRow && this.destCol == m.destCol)
			return true;
		return false;
	}
}
