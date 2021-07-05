
public class Piece {
	boolean isDefender;
	boolean isKing;

	/**
	* Creates a new blank piece
	*
	* A piece can never be a king and not a defender,
	* meaning if a piece has these set variables, it was
	* not assigned properly
	**/
	public Piece()
	{
		isDefender = false;
		isKing = true;
	}

	/**
	* Creates a new piece
	*
	* @param isDefender whether the piece is a defender
	**/
	public Piece(boolean isDefender)
	{
		this.isDefender = isDefender;
		isKing = false;
	}

	/**
	* Creates a new piece
	*
	* @param isDefender whether the piece is a defender
	* @param isKing whether the piece is a king
	**/
	public Piece(boolean isDefender, boolean isKing)
	{
		this.isDefender = isDefender;
		this.isKing = isKing;
	}
}
