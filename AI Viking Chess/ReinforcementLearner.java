
import java.util.ArrayList;
import java.util.Random;

public class ReinforcementLearner {

	/*
	 * player 1- take action a(set earlier)
	 *
	 * for each move
	 * Player 2- take action a
	 * Player 1- observe s' and choose a'
	 * Player 1- update weights
	 * Player 1- set s -> s' and a -> a'
	 * Player 1- takes action a
	 * Player 2- observe s' and choose a'
	 * Player 2- update weights
	 * Player 2- set s -> s' and a -> a'
	 *
	 *
	 * try step size of 0.1, can play around with that
	 *
	 */
	float s;
	float sPrime;
	Move a;
	Move aPrime;
	float[] weights = new float[125];

	float[] featuresS = new float[weights.length];
	float[] featuresSPrime = new float[weights.length];
	public int reward = 0;
	Random random = new Random();
	boolean min;
	boolean rand = false;

	/**
	*	Creates new learner and initializes weights
	*
	*	@param min whether the player is trying to minimize their reward
	**/
	public ReinforcementLearner(boolean min) {
		this.min = min;
		for(int i = 0; i < weights.length; i++)
		{
			weights[i] = 0.0f;
		}
	}

	/**
	*	Creates new learner and initializes weights
	*
	*	@param min whether the player is trying to minimize their reward
	*	@param w weights for learner
	**/
	public ReinforcementLearner(boolean min, float[] w) {
		this.min = min;
		weights = new float[w.length];
		for(int i = 0; i < weights.length; i++)
		{
			weights[i] = w[i];
		}
	}

	/**
	*	Initializes learner and makes first move
	*
	*	@param board current board
	*	@param set dictates which feature set to use (1,2,3)
	**/
	public void InitializePlayer(Board board, int set)
	{
		ArrayList<Move> moves = board.checkAllMoves(min);

		if(random.nextFloat() == .1)
		{
			a = moves.get(random.nextInt(moves.size()));
		}
		else {
			int sMoveIndex = 0;
			float val;
			float[] features;

			if(min)
				s = Float.POSITIVE_INFINITY;
			else
				s = Float.NEGATIVE_INFINITY;
			for(int i = 0; i < moves.size(); ++i)
			{
				board.makeMove(moves.get(i));
				if(set ==1)
					features = board.calculateFeatures1();
				else if(set ==2)
					features = board.calculateFeatures2();
				else
					features = board.calculateFeatures3();
				val = 0;
				if(!board.gameOver(!min))
				{
					for(int f = 0; f < features.length; ++f)
					{
						val += features[f] * weights[f];
					}
				}
				if(min) {
					if(val < s)
					{
					s = val;
					sMoveIndex = i;
					featuresS = features;
					}
				}else
				{
					if(val > s)
					{
					s = val;
					sMoveIndex = i;
					featuresS = features;
					}
				}
				board.undoMove();
			}
			a = moves.get(sMoveIndex);
		}
	}

	/**
	*	Updates the player/weights for the learner throughout the game
	*
	*	@param board current board
	*	@param set dictates which feature set to use (1,2,3)
	**/
	public void UpdatePlayer(Board board, int set) {

			ArrayList<Move> moves = board.checkAllMoves(min);
			float[] features = new float[weights.length];
			//For each game (episode)
			if(moves.size()==0) {
				board.printBoard();
				System.out.print(board.movesLeft(min));
				System.out.print(board.gameOver(min));
			}
				int bestMoveIndex = 0;
				int tempReward;
				if(min)
					sPrime = Float.POSITIVE_INFINITY;
				else
					sPrime = Float.NEGATIVE_INFINITY;
				float val = 0;
				for(int i = 0; i < moves.size(); ++i)
				{
					board.makeMove(moves.get(i));
					switch(set){
						case 1: features = board.calculateFeatures1();
										break;
						case 2: features = board.calculateFeatures2();
										break;
						default: features = board.calculateFeatures3();
										break;
					}
					tempReward = board.EvaluateReward(min);
					val = 0;
					if(!board.gameOver(min)) {
						for(int f = 0; f < features.length; ++f)
						{
							val += features[f] * weights[f];
						}
					}
					if(min) {
						if(val + tempReward < sPrime+ reward)
						{
							sPrime = val;
							bestMoveIndex = i;
							featuresSPrime = features;
							reward = tempReward;
						}
					}
					else {
						if(val + tempReward> sPrime+reward)
						{
							sPrime = val;
							bestMoveIndex = i;
							featuresSPrime = features;
							reward = tempReward;
						}
					}
					board.undoMove();
				//}end else
				aPrime = moves.get(bestMoveIndex);
			}
			//update weights
			for(int i = 0; i < weights.length; ++i)
			{
				weights[i] = (float)(weights[i] + 0.1 * (reward + sPrime - s) * featuresS[i]);
				featuresS[i] = featuresSPrime[i];
			}
			s = sPrime;
			a = aPrime;
	}

	/**
	* Plays the games with learned weights without further updating them
	*
	*	@param board current board
	*	@param set dictates which feature set to use (1,2,3)
	**/
	public void TestPlayer(Board board, int set) {

		ArrayList<Move> moves = board.checkAllMoves(min);
		float[] features = new float[weights.length];
		//For each game (episode)
		if(moves.size()==0)
			board.printBoard();

			int bestMoveIndex = 0;
			int tempReward;
			if(min)
				sPrime = Float.POSITIVE_INFINITY;
			else
				sPrime = Float.NEGATIVE_INFINITY;
			float val = 0;
			for(int i = 0; i < moves.size(); ++i)
			{
				board.makeMove(moves.get(i));
				switch(set){
					case 1: features = board.calculateFeatures1();
									break;
					case 2: features = board.calculateFeatures2();
									break;
					default: features = board.calculateFeatures3();
									break;
				}

				tempReward = board.EvaluateReward(min);
				val = 0;
				if(!board.gameOver(min)) {
					for(int f = 0; f < features.length; ++f)
					{
						val += features[f] * weights[f];
					}
				}
				if(min) {
					if(val + tempReward < sPrime+ reward)
					{
						sPrime = val;
						bestMoveIndex = i;
						featuresSPrime = features;
						reward = tempReward;
					}
				}
				else {
					if(val + tempReward> sPrime+reward)
					{
						sPrime = val;
						bestMoveIndex = i;
						featuresSPrime = features;
						reward = tempReward;
					}
				}
				board.undoMove();
			}
			aPrime = moves.get(bestMoveIndex);
		s = sPrime;
		a = aPrime;
}

/**
*	updates the weights once the game is over
*
*	@param board current board
*	@param set dictates which feature set to use (1,2,3)
**/
	public void UpdateWeights(Board board, int set) {
		float[] features;
		if(set ==1)
			features = board.calculateFeatures1();
		else if(set ==2)
			features = board.calculateFeatures2();
		else
			features = board.calculateFeatures3();
		sPrime = 0;
		reward = board.EvaluateReward(!min);
		if(!board.gameOver(min)) {
			for(int f = 0; f < features.length; ++f)
			{
				sPrime += features[f] * weights[f];
			}
		}
		for(int i = 0; i < weights.length; ++i)
		{
			weights[i] = (float)(weights[i] + 0.1 * (reward + 0.1* sPrime - s) * featuresS[i]);
		}
	}


}
