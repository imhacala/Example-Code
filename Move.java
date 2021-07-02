
public class Move {
	int pieceRow;
	int pieceCol;
	int destRow;
	int destCol;
	
	Move(int r, int c, int dr, int dc)
	{
		pieceRow = r;
		pieceCol = c;
		destRow = dr;
		destCol = dc;
	}
	
	Move(Move m)
	{
		if(m != null) {
			pieceRow = m.pieceRow;
			pieceCol = m.pieceCol;
			destRow = m.destRow;
			destCol = m.destCol;
		}
	}
	
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
