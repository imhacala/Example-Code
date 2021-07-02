import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {

	Tile[][] board = new Tile[11][11];
	int[] kingCoords = new int[2];
	
	int numMoves;
	
	Scanner scnr = new Scanner(System.in); 
	Board lastBoard;
	
	Move deflastMove;
	Move deflastlastMove;
	Move attlastMove;
	Move attlastlastMove;
	
	public Board(Board b) {
		for(int i = 0; i<b.board.length; ++i)
		{
			for(int j = 0; j<b.board.length; ++j)
			{
				if(b.board[i][j].isRestricted())
				{
					board[i][j] = new Tile(b.board[i][j].getPiece(), true);
				}
				else if(b.board[i][j].isEmpty())
				{
					board[i][j] = new Tile();
				}
				else {
					board[i][j] = new Tile(b.board[i][j].getPiece());
				}
			}
		}
		numMoves = b.numMoves;
		kingCoords = new int[] {b.kingCoords[0],b.kingCoords[1]};
		deflastMove = new Move(b.deflastMove);
		deflastlastMove = new Move(b.deflastlastMove);
		attlastMove = new Move(b.attlastMove);
		attlastlastMove = new Move(b.attlastlastMove);
		
		lastBoard = b.lastBoard;
		
	}
	
	public Board() throws IOException {
		numMoves = 0;
		for(int i = 3; i<8; ++i)
		{
			board[0][i] = new Tile(new Piece(false));
			board[10][i] = new Tile(new Piece(false));
			board[i][0] = new Tile(new Piece(false));
			board[i][10] = new Tile(new Piece(false));
		}
		board[1][5] = new Tile(new Piece(false));
		board[9][5] = new Tile(new Piece(false));
		board[5][1] = new Tile(new Piece(false));
		board[5][9] = new Tile(new Piece(false));
		
		for(int i = 4; i < 7; ++i)
		{
			for(int j = 4; j < 7; ++j)
			{
				if(i == 5 && j ==5)
					board[i][j] = new Tile(new Piece(true, true), true);
				else
					board[i][j] = new Tile(new Piece(true));
			}
		}
		board[5][3] = new Tile(new Piece(true));
		board[5][7] = new Tile(new Piece(true));
		board[3][5] = new Tile(new Piece(true));
		board[7][5] = new Tile(new Piece(true));
		
		board[0][0] = new Tile(true);
		board[0][10] = new Tile(true);
		board[10][10] = new Tile(true);
		board[10][0] = new Tile(true);
		
		//Fill rest with empty
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board.length; ++c)
			{
				if(board[r][c] == null)
					board[r][c] = new Tile();
			}
		}
		
		kingCoords = new int[]{5,5};
	}
	
	public void printBoard()
	{
		int count = 0;
		System.out.println("\t 0  1  2  3  4  5  6  7  8  9  10\n\n");
		for(int r = 0; r < board.length; ++r)
		{
			System.out.print(count + "\t");
			count++;
			for(int c = 0; c < board.length; ++c)
			{
				if(board[r][c].isRestricted() && board[r][c].getPiece()==null)
					System.out.print(" X ");
				else if(board[r][c].isEmpty())
					System.out.print(" . ");
				else if(board[r][c].getPiece().isKing)
					System.out.print(" K ");
				else if(!board[r][c].getPiece().isDefender)
					System.out.print(" A ");
				else if(board[r][c].getPiece().isDefender)
					System.out.print(" D ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void printToFile() throws IOException {
		FileWriter fileWriter = new FileWriter("GameOutput.txt", true);
		
		int count = 0;
		fileWriter.append("\t 0  1  2  3  4  5  6  7  8  9  10\n\n\n");
		for(int r = 0; r < board.length; ++r)
		{
			fileWriter.append(count + "\t");
			count++;
			for(int c = 0; c < board.length; ++c)
			{
				if(board[r][c].isRestricted() && board[r][c].getPiece()==null)
					fileWriter.append(" X ");
				else if(board[r][c].isEmpty())
					fileWriter.append(" . ");
				else if(board[r][c].getPiece().isKing)
					fileWriter.append(" K ");
				else if(!board[r][c].getPiece().isDefender)
					fileWriter.append(" A ");
				else if(board[r][c].getPiece().isDefender)
					fileWriter.append(" D ");
			}
			fileWriter.append("\n");
		}
		fileWriter.append("\n");
		fileWriter.close();
	}

	public boolean movesLeft(boolean defenders)
	{
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board[0].length; ++c)
			{
				if(!board[r][c].isEmpty() && board[r][c].getPiece().isDefender == defenders)
				{
					int row = r+1;
					int col = c;
					boolean validMove = true;
					//CHECK DOWN
					while(row < board.length && col < board[0].length && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							Move temp = new Move(r,c,row,col);
							if(defenders)
							{
								if(!temp.equals(deflastlastMove))
								{
									return true;
								}
							}
							else
							{
								if(!temp.equals(attlastlastMove))
								{
									return true;
								}
							}
						}
						else {
							validMove = false;
						}
						row++;
					}
					
					row = r-1;
					col = c;
					validMove = true;
					//CHECK UP
					while(row > -1 && col > -1 && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							Move temp = new Move(r,c,row,col);
							if(defenders)
							{
								if(!temp.equals(deflastlastMove))
								{
									return true;
								}
							}
							else
							{
								if(!temp.equals(attlastlastMove))
								{
									return true;
								}
							}
						}
						else {
							validMove = false;
						}
						row--;
					}
					
					row = r;
					col = c+1;
					validMove = true;
					//CHECK RIGHT
					while(row < board.length && col < board[0].length && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							Move temp = new Move(r,c,row,col);
							if(defenders)
							{
								if(!temp.equals(deflastlastMove))
								{
									return true;
								}
							}
							else
							{
								if(!temp.equals(attlastlastMove))
								{
									return true;
								}
							}
						}
						else {
							validMove = false;
						}
						col++;
					}
					
					row = r;
					col = c-1;
					validMove = true;
					//CHECK LEFT
					while(row > -1 && col > -1 && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							Move temp = new Move(r,c,row,col);
							if(defenders)
							{
								if(!temp.equals(deflastlastMove))
								{
									return true;
								}
							}
							else
							{
								if(!temp.equals(attlastlastMove))
								{
									return true;
								}
							}
						}
						else {
							validMove = false;
						}
						col--;
					}
				}
			}
		}
		return false;
	}
	public ArrayList<Move> checkAllMoves(boolean defenders){
		ArrayList<Move> movesList = new ArrayList<Move>();
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board[0].length; ++c)
			{
				if(!board[r][c].isEmpty() && board[r][c].getPiece().isDefender == defenders)
				{
					int row = r+1;
					int col = c;
					boolean validMove = true;
					//CHECK DOWN
					while(row < board.length && col < board[0].length && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							movesList.add(new Move(r,c,row,col));
						}
						else {
							validMove = false;
						}
						row++;
					}
					
					row = r-1;
					col = c;
					validMove = true;
					//CHECK UP
					while(row > -1 && col > -1 && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							movesList.add(new Move(r,c,row,col));
						}
						else {
							validMove = false;
						}
						row--;
					}
					
					row = r;
					col = c+1;
					validMove = true;
					//CHECK RIGHT
					while(row < board.length && col < board[0].length && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							movesList.add(new Move(r,c,row,col));
						}
						else {
							validMove = false;
						}
						col++;
					}
					
					row = r;
					col = c-1;
					validMove = true;
					//CHECK LEFT
					while(row > -1 && col > -1 && validMove)
					{
						if(board[row][col].isEmpty() && (!board[row][col].isRestricted() || board[r][c].getPiece().isKing))
						{
							movesList.add(new Move(r,c,row,col));
						}
						else {
							validMove = false;
						}
						col--;
					}
				}
			}
		}
		if(defenders) {
			if(movesList.contains(deflastlastMove))
			{
				movesList.remove(deflastlastMove);
			}
		}else {
			if(movesList.contains(attlastlastMove))
			{
				movesList.remove(attlastlastMove);
			}
		}
		return movesList;
	}
	
	public void makeMove(Move move) {
		lastBoard = new Board(this);
		numMoves++;
		if(board[move.pieceRow][move.pieceCol].getPiece().isDefender)
		{
			deflastlastMove = new Move(deflastMove);
			deflastMove = new Move(move);
		}
		else {
			attlastlastMove = new Move(attlastMove);
			attlastMove = new Move(move);
		}

		if(board[move.pieceRow][move.pieceCol].getPiece().isKing)
			kingCoords = new int[]{move.destRow,move.destCol};
		
		board[move.destRow][move.destCol].setPiece(board[move.pieceRow][move.pieceCol].getPiece());
		board[move.pieceRow][move.pieceCol].setEmpty();

		checkCapture(move.destRow,move.destCol);
	}
	
	public void undoMove() {
		for(int i = 0; i < board.length; ++i)
		{
			for(int j = 0; j < board.length; ++j)
			{
				board[i][j] = lastBoard.board[i][j];
			}
		}
		this.numMoves = lastBoard.numMoves;
		this.kingCoords = lastBoard.kingCoords;
		this.deflastlastMove = lastBoard.deflastlastMove;
		this.deflastMove = lastBoard.deflastMove;
		this.attlastlastMove = lastBoard.attlastlastMove;
		this.attlastMove = lastBoard.attlastMove;
		
		this.lastBoard = lastBoard.lastBoard;
		
	}
	
	public void checkCapture(int row, int col){
		if(!board[row][col].isEmpty()){
			if(row+2 < board.length && !board[row+1][col].isEmpty() && !board[row+1][col].getPiece().isKing && board[row+1][col].getPiece().isDefender!= board[row][col].getPiece().isDefender)
			{
				if(!board[row+2][col].isEmpty() && (board[row+2][col].getPiece().isDefender == board[row][col].getPiece().isDefender || board[row+2][col].isRestricted()))
				{
					board[row+1][col].setEmpty();
				}
			}
			if(row-2 >-1 && !board[row-1][col].isEmpty() && !board[row-1][col].getPiece().isKing && board[row-1][col].getPiece().isDefender!= board[row][col].getPiece().isDefender)
			{
				if(!board[row-2][col].isEmpty() && (board[row-2][col].getPiece().isDefender == board[row][col].getPiece().isDefender || board[row-2][col].isRestricted()))
				{
					board[row-1][col].setEmpty();
				}
			}
			if(col+2 < board.length && !board[row][col+1].isEmpty() && !board[row][col+1].getPiece().isKing && board[row][col+1].getPiece().isDefender!= board[row][col].getPiece().isDefender)
			{
				if(!board[row][col+2].isEmpty() && (board[row][col+2].getPiece().isDefender == board[row][col].getPiece().isDefender || board[row][col+2].isRestricted()))
				{
					board[row][col+1].setEmpty();
				}
			}
			if(col-2 >-1 && !board[row][col-1].isEmpty() && !board[row][col-1].getPiece().isKing && board[row][col-1].getPiece().isDefender!= board[row][col].getPiece().isDefender)
			{
				if(!board[row][col-2].isEmpty() && (board[row][col-2].getPiece().isDefender == board[row][col].getPiece().isDefender || board[row][col-2].isRestricted()))
				{
					board[row][col-1].setEmpty();
				}
			}
		}
	}
	
	public boolean checkKingCapture() {
		int kingRow = kingCoords[0];
		int kingCol = kingCoords[1];
		boolean otherDefenders = false;
		for(int r = 0; r < 11; ++r)
		{
			for(int c = 0; c < 11; ++c)
			{
				if(!board[r][c].isEmpty() && board[r][c].getPiece()!=null) {
						if(board[r][c].getPiece().isDefender && !board[r][c].getPiece().isKing)
						{
							otherDefenders = true;
							break;
						}
				}
			}
			if(otherDefenders)
				break;
		}
		if(((!otherDefenders && kingRow+1 >= board.length) || ((kingRow+1 < board.length && !board[kingRow+1][kingCol].isEmpty() && (!board[kingRow+1][kingCol].getPiece().isDefender)|| kingRow+1 == 5 && kingCol == 5))) &&
				
				((!otherDefenders && kingRow-1 < 0) || ((kingRow-1 >-1 && !board[kingRow-1][kingCol].isEmpty() && (!board[kingRow-1][kingCol].getPiece().isDefender) || kingRow-1 == 5 && kingCol == 5))) &&
						
				((!otherDefenders && kingCol+1 >= board[0].length) || ((kingCol+1 < board[0].length && !board[kingRow][kingCol+1].isEmpty() && !board[kingRow][kingCol+1].getPiece().isDefender) || kingRow == 5 && kingCol+1 == 5)) &&
				
				((!otherDefenders && kingCol-1 < 0) || ((kingCol-1 >-1 && !board[kingRow][kingCol-1].isEmpty() && !board[kingRow][kingCol-1].getPiece().isDefender) || kingRow == 5 && kingCol-1 == 5)))
		{
			return true;
		}
		return false;
	}
	
	public boolean checkKingEscape() {
		if(kingCoords[0] == 0 && kingCoords[1]==0 || kingCoords[0] == 10 && kingCoords[1]==0 || kingCoords[0] == 0 && kingCoords[1]==10 || kingCoords[0] == 10 && kingCoords[1]==10)
			return true;
		return false;
	}
	
	public boolean gameOver(boolean defenders)
	{
		if(checkKingEscape() || checkKingCapture() || numMoves > 120 || !movesLeft(defenders)) {
			return true;
		}
		return false;
	}
	
	public int EvaluateReward(boolean defLastTurn)
	{
		int rtrn = 0;
		if(checkKingCapture()) {
			rtrn = 1;
		}
		else if(checkKingEscape()) {
			rtrn = -1;
		}
		else if(!movesLeft(!defLastTurn))
		{
			if(defLastTurn)
				rtrn=-1;
			else
				rtrn = 1;
		}
		
		return rtrn;
	}
	
	public int EvaluateBoard()
	{
		//lower the better defenders are doing
		int boardVal = 0;
		boolean isKing = false;
		for(int i = 0; i < board.length; ++i)
		{
			for(int j = 0; j < board.length; ++j)
			{
				if(!board[i][j].isEmpty()) {
					if(!board[i][j].getPiece().isDefender){
						++boardVal;
					}else if(!board[i][j].getPiece().isKing) {
						--boardVal;
					}else
					{
						isKing = true;
						boardVal-=12;
					}
				}
					
			}
		}
		if(!isKing)
			boardVal += 100;
		else if(checkKingEscape())
			boardVal -= 100;
		
		if(kingCoords[0]+1<board.length && !board[kingCoords[0]+1][kingCoords[1]].isEmpty()) {
			if(!board[kingCoords[0]+1][kingCoords[1]].getPiece().isDefender)
				boardVal +=1;
			else
				boardVal -=1;
		}
		if(kingCoords[0]-1>0 && !board[kingCoords[0]-1][kingCoords[1]].isEmpty()) {
			if(!board[kingCoords[0]-1][kingCoords[1]].getPiece().isDefender)
				boardVal +=1;
			else
				boardVal -=1;
		}
		if(kingCoords[1]+1<board.length && !board[kingCoords[0]][kingCoords[1]+1].isEmpty())
			if(!board[kingCoords[0]][kingCoords[1]+1].getPiece().isDefender)
				boardVal +=1;
			else
				boardVal -=1;
		if(kingCoords[1]-1>0 && !board[kingCoords[0]][kingCoords[1]-1].isEmpty())
			if(!board[kingCoords[0]][kingCoords[1]-1].getPiece().isDefender)
				boardVal +=1;
			else
				boardVal -=1;
			
		if(kingCoords[0]>5)
		{
			boardVal-= kingCoords[0]-5;
		}
		else 
		{
			boardVal-= 5-kingCoords[0];
		}
		
		if(kingCoords[1]>5)
		{
			boardVal-= kingCoords[1]-5;
		}
		else 
		{
			boardVal-= 5-kingCoords[1];
		}
		
		return boardVal;
	}
	
	/////////////FEATURE SET 1///////////////////
	public float[] calculateFeatures1() {
		float[] features = new float[206];
		int neighborCount = 2;
		int kingIndex = 85;
		boolean isKing = false;
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board.length; ++c)
			{
				if(!board[r][c].isEmpty()) {
					if(!board[r][c].getPiece().isDefender){
						features[0]+=1;
					}else if(!board[r][c].getPiece().isKing) {
						features[0]-=2;
					}else
					{
						features[kingIndex]=1;
						isKing = true;
					}
					kingIndex++;
				}
				if(r>0 && r< board.length-1 && c>0 && c < board.length-1)
				{
					neighborCount++;
					for(int i = r-1; i < r+2; ++i)
					{
						for(int j = c-1; j < c+2; ++j)
						{
							if(!board[i][j].isEmpty()) {
								if(board[i][j].getPiece().isDefender)
									features[neighborCount]--;
								else
									features[neighborCount]++;
							}
						}
					}
				}
			}
		}
		for(int i = 0; i < 206; ++i)
		{
			if(features[i]>0)
				features[i]=1;
			else if(features[i]<0)
				features[i]=-1;
		}
		if(!isKing)
		{
			features[1]=1;
		}
		else
			features[1]=0;
		
		if(kingCoords[0] == 10 || kingCoords[1] == 10)
			features[2] = -1;
		else
			features[2] = 0;
		
		for(int i = 0; i < features.length; ++i)
		{
			features[i] = features[i]/10;
		}
		
		return features;
	}
	
	/////////////FEATURE SET 2///////////////////
	public float[] calculateFeatures2() {
		float[] features = new float[125];
		int index = 4;
		boolean isKing = false;
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board.length; ++c)
			{
				if(!board[r][c].isEmpty()) {
					if(!board[r][c].getPiece().isDefender){
						features[0]++;
						features[index]=1;
					}else if(!board[r][c].getPiece().isKing) {
						features[0]-=2;
						features[index]=-1;
					}else
					{
						isKing=true;
					}
				}
				else
					features[index]=0;
				
				index++;
			}
		}
		if(!isKing)
			features[1] =1;
		else
			features[1] = 0;
		
		if(kingCoords[0]+1<board.length && !board[kingCoords[0]+1][kingCoords[1]].isEmpty()) {
			if(!board[kingCoords[0]+1][kingCoords[1]].getPiece().isDefender)
				features[2] +=1;
			else
				features[2] -=1;
		}
		if(kingCoords[0]-1>0 && !board[kingCoords[0]-1][kingCoords[1]].isEmpty()) {
			if(!board[kingCoords[0]-1][kingCoords[1]].getPiece().isDefender)
				features[2] +=1;
			else
				features[2] -=1;
		}
		if(kingCoords[1]+1<board.length && !board[kingCoords[0]][kingCoords[1]+1].isEmpty())
			if(!board[kingCoords[0]][kingCoords[1]+1].getPiece().isDefender)
				features[2] +=1;
			else
				features[2] -=1;
		if(kingCoords[1]-1>0 && !board[kingCoords[0]][kingCoords[1]-1].isEmpty())
			if(!board[kingCoords[0]][kingCoords[1]-1].getPiece().isDefender)
				features[2] +=1;
			else
				features[2] -=1;
			
		if(kingCoords[0]>5)
		{
			features[3]-= kingCoords[0]-5;
		}
		else 
		{
			features[3]-= 5-kingCoords[0];
		}
		
		if(kingCoords[1]>5)
		{
			features[3]-= kingCoords[1]-5;
		}
		else 
		{
			features[3]-= 5-kingCoords[1];
		}
		
		for(int i = 0; i < 3; ++i)
		{
			if(features[i]>0)
				features[i]=1;
			else if(features[i]<0)
				features[i]=-1;
		}
		if(features[3]<-3)
			features[3]=-1;
		else
			features[3]=0;
		
		for(int i = 0; i < features.length; ++i)
		{
			features[i] = features[i]/10;
		}
		
		return features;
	}
	
	public float[] calculateFeatures3() {
		float[] features = new float[242];
		int neighborCount = 2;
		int index = 85;
		int atRisk = 205;
		boolean isKing = false;
		for(int r = 0; r < board.length; ++r)
		{
			for(int c = 0; c < board.length; ++c)
			{
				if(!board[r][c].isEmpty()) {
					if(!board[r][c].getPiece().isDefender){
						atRisk++;
						features[0]+=1;
						features[index]=1;
						boolean risk = false;
						if(r-1>-1 && ((!board[r-1][c].isEmpty() &&(board[r-1][c].getPiece().isDefender) || board[r-1][c].isRestricted()))) {
							features[atRisk]=-1;
							risk = true;
						}
						if(!risk && r+1<board.length && ((!board[r+1][c].isEmpty() &&(board[r+1][c].getPiece().isDefender) || board[r+1][c].isRestricted()))) {
							features[atRisk]=-1;
							risk = true;
						}
						if(!risk && c-1>-1 && ((!board[r][c-1].isEmpty() &&(board[r][c-1].getPiece().isDefender) || board[r][c-1].isRestricted()))) {
							features[atRisk]=-1;
							risk = true;
						}
						if(!risk && c+1<board.length && ((!board[r][c+1].isEmpty() &&(board[r][c+1].getPiece().isDefender) || board[r][c+1].isRestricted()))) {
							features[atRisk]=-1;
							risk = true;
						}
						
					}else if(!board[r][c].getPiece().isKing) {
						atRisk++;
						features[0]-=2;
						features[index]=-1;
						boolean risk = false;
						if(r-1>-1 && ((!board[r-1][c].isEmpty() &&(!board[r-1][c].getPiece().isDefender) || board[r-1][c].isRestricted()))) {
							features[atRisk]=1;
							risk = true;
						}
						if(!risk && r+1<board.length && ((!board[r+1][c].isEmpty() &&(!board[r+1][c].getPiece().isDefender) || board[r+1][c].isRestricted()))) {
							features[atRisk]=1;
							risk = true;
						}
						if(!risk && c-1>-1 && ((!board[r][c-1].isEmpty() &&(!board[r][c-1].getPiece().isDefender) || board[r][c-1].isRestricted()))) {
							features[atRisk]=1;
							risk = true;
						}
						if(!risk && c+1<board.length && ((!board[r][c+1].isEmpty() &&(!board[r][c+1].getPiece().isDefender) || board[r][c+1].isRestricted()))) {
							features[atRisk]=1;
							risk = true;
						}
					}else
					{
						isKing = true;
					}
					
				}
				if(r>0 && r< board.length-1 && c>0 && c < board.length-1)
				{
					neighborCount++;
					for(int i = r-1; i < r+2; ++i)
					{
						for(int j = c-1; j < c+2; ++j)
						{
							if(!board[i][j].isEmpty()) {
								if(board[i][j].getPiece().isDefender)
									features[neighborCount]--;
								else
									features[neighborCount]++;
							}
						}
					}
				}
				index++;
			}
		}
		for(int i = 0; i < 206; ++i)
		{
			if(features[i]>0)
				features[i]=1;
			else if(features[i]<0)
				features[i]=-1;
		}
		if(!isKing)
		{
			features[1]=1;
		}
		else
			features[1]=0;
		
		if(kingCoords[0] == 10 || kingCoords[1] == 10)
			features[2] = -1;
		else
			features[2] = 0;
		
		for(int i = 0; i < features.length; ++i)
		{
			features[i] = features[i]/10;
		}
		
		return features;
	}
	
	public int winner() {
		if(checkKingCapture())
			return 1;
		else if(checkKingEscape())
			return -1;
		else if(!movesLeft(true))
			return 1;
		else if(!movesLeft(false))
			return -1;
		return 0;
	}
	
}
