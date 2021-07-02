
public class Piece {
	boolean isDefender;
	boolean isKing;

	public Piece()
	{
		/* A piece can never be a king and not a defender, 
		meaning if a piece has these set variables, it was
		not set properly */
		isDefender = false;
		isKing = true;
	}

	public Piece(boolean isDefender)
	{
		this.isDefender = isDefender;
		isKing = false;
	}

	public Piece(boolean isDefender, boolean isKing)
	{
		this.isDefender = isDefender;
		this.isKing = isKing;
	}
}
