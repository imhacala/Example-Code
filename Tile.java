
public class Tile {
	
	private Piece piece;
	private boolean isEmpty;
	private boolean restricted = false;
	private int[] coords = new int[2];
	
	Tile(){
		isEmpty = true;
	}
	
	Tile(Piece piece)
	{
		this.piece = piece;
		isEmpty = false;
	}
	
	Tile(Piece piece, boolean restricted)
	{
		this.piece = piece;
		if(piece != null)
			isEmpty = false;
		else
			isEmpty = true;
		this.restricted = restricted;
	}
	
	Tile(boolean restricted)
	{
		this.restricted = restricted;
		isEmpty = true;
	}
	
	public boolean isEmpty()
	{
		return (isEmpty|| piece == null);
	}
	
	public void setEmpty()
	{
		isEmpty = true;
		piece = null;
	}
	
	public boolean isRestricted()
	{
		return restricted;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
		isEmpty = false;
	}
	
	public void setCoords(int[] coords)
	{
		this.coords = coords;
	}
	
	public int[] getCoords()
	{
		return coords;
	}
	
}
