
public class Tile {

	private Piece piece;
	private boolean isEmpty;
	private boolean restricted = false;
	private int[] coords = new int[2];

	/**
	* Creates a new tile, by default it is empty
	**/
	Tile(){
		isEmpty = true;
	}

	/**
	* Creates a new tile occupied by given piece
	*
	* @param piece piece occupying this tile
	**/
	Tile(Piece piece)
	{
		this.piece = piece;
		isEmpty = false;
	}

	/**
	* Creates a new tile occupied by given piece
	* tiles can be restricted meaning only the king can stand on them
	*
	* @param piece piece occupying this tile
	* @param restricted whether the tile is restricted
	**/
	Tile(Piece piece, boolean restricted)
	{
		this.piece = piece;
		if(piece != null)
			isEmpty = false;
		else
			isEmpty = true;
		this.restricted = restricted;
	}

	/**
	* Creates an empty restricted tile
	*
	* @param restricted whether the tile is restricted
	**/
	Tile(boolean restricted)
	{
		this.restricted = restricted;
		isEmpty = true;
	}

	/**
	* Checks if a tile is empty
	**/
	public boolean isEmpty()
	{
		return (isEmpty|| piece == null);
	}

	/**
	* Sets tile to empty
	**/
	public void setEmpty()
	{
		isEmpty = true;
		piece = null;
	}

	/**
	* returns whether tile is restricted
	**/
	public boolean isRestricted()
	{
		return restricted;
	}

	/**
	* Returns piece
	**/
	public Piece getPiece() {
		return piece;
	}

	/**
	* Sets piece to given piece
	*
	* @param piece piece set to occupy this tile
	**/
	public void setPiece(Piece piece) {
		this.piece = piece;
		isEmpty = false;
	}

	/**
	* Sets the coordinates of the tile
	*
	* @param coords coordinates of the tile
	**/
	public void setCoords(int[] coords)
	{
		this.coords = coords;
	}

	/**
	* Returns the coordinates of the tile
	**/
	public int[] getCoords()
	{
		return coords;
	}

}
