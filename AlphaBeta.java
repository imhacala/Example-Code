import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AlphaBeta{
	/**
	*	Finds the move with the lowest board value
	*
	*	@param board current board
	*	@param alpha best move score for alpha, initially set to an extremely low value
	*	@param beta best move score for beta, initially set to an extremely high value
	*	@param depth how many moves ahead the player is looking
	**/
	public int[] minValue(Board board, int alpha, int beta, int depth) {
		if(board.gameOver(false) || depth > 3) {
			return new int[]{board.EvaluateBoard(),0};
		}
		//defenderPlayer
		ArrayList<Move> moves = board.checkAllMoves(true);
		int count = 0;
		int bestMove = 0;
		int[] V;
		while(alpha < beta && count < moves.size())
		{
			board.makeMove(moves.get(count));
			V = maxValue(board, alpha, beta, depth+1);
			if(V[0]<beta) {
				beta = V[0];
				bestMove =count;
			}
			board.undoMove();
			count++;
		}
		return new int[]{beta,bestMove};
	}

	/**
	*	Finds the move with the highest board value
	*
	*	@param board current board
	*	@param alpha best move score for alpha, initially set to an extremely low value
	*	@param beta best move score for beta, initially set to an extremely high value
	*	@param depth how many moves ahead the player is looking
	**/
	public int[] maxValue(Board board, int alpha, int beta, int depth){
		if(board.gameOver(true) || depth > 3) {
			return new int[]{board.EvaluateBoard(),0};
		}
		//attacker player
		ArrayList<Move> moves = board.checkAllMoves(false);
		int count = 0;
		int bestMove = 0;
		int[] V;
		while(alpha < beta && count < moves.size())
		{
			board.makeMove(moves.get(count));
			V = minValue(board, alpha, beta, depth+1);
			if(V[0]>alpha) {
				alpha = V[0];
				bestMove = count;
			}
			board.undoMove();
			count++;
		}
		return new int[]{alpha, bestMove};
	}

	/**
	*	Prints board to file of given path
	*
	*	@param board current board
	*	@param file file path to print the board to
	**/
	public void printBoardToFile(Tile[][] board, String file) throws IOException {
		FileWriter fileWriter = new FileWriter(file, true);

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

}
