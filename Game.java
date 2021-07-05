import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	/**
	*	prints moves to the terminal so the player can choose their move
	*
	* @param attackMoves list of current moves
	**/
	public static void printMoves(ArrayList<Move> attackMoves)
	{
		for(int i = 0; i < attackMoves.size(); ++i)
		{
			System.out.print(i +"<" + attackMoves.get(i).pieceRow + "," + attackMoves.get(i).pieceCol + " - " + attackMoves.get(i).destRow + "," + attackMoves.get(i).destCol + "> ");
			if(i%9 == 0 && i!=0)
				System.out.println();
		}
		System.out.println();
	}

	/**
	*	AlphaBeta vs AlphaBeta
	**/
	public static void ABvsAB() throws IOException {
		//0-black/Attacker 1-white/Defender
			for(int k = 0; k < 100; ++k) {
				Board board = new Board();
				board.printBoard();
				boolean gameOver = false;
				AlphaBeta abPlayer = new AlphaBeta();
				ArrayList<Move> attackMoves = new ArrayList<Move>();
				ArrayList<Move> defendMoves = new ArrayList<Move>();


				int moveNum = 0;
				while(!gameOver)
				{
					moveNum++;
					attackMoves = board.checkAllMoves(false);
					System.out.println("Attacker's Turn " + moveNum);
					int move = abPlayer.maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
					Move bestMove = attackMoves.get(move);

					board.makeMove(bestMove);
					board.printBoard();

					gameOver = board.gameOver(true);

					if(!gameOver) {
						moveNum++;
						System.out.println("Defender's Turn" + moveNum);
						move = abPlayer.minValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
						bestMove = defendMoves.get(move);

						board.makeMove(bestMove);
						board.printBoard();

						gameOver = board.gameOver(false);
					}
				}
				int winner = board.winner();
				if(winner == 1)
					System.out.println("Attackers Win!");
				else if(winner == -1)
					System.out.println("Defenders Win!");
				else
					System.out.println("Draw!");
				}
	}

	/**
	*	Human as Defender vs AlphaBeta as Attacker
	**/
	public static void onePlayerDef() throws IOException {
		Scanner scnr = new Scanner(System.in);

		//0-black/Attacker 1-white/Defender
		Board board = new Board();

		board.printBoard();
		boolean gameOver = false;
		AlphaBeta abPlayer = new AlphaBeta();
		ArrayList<Move> attackMoves = new ArrayList<Move>();
		ArrayList<Move> defendMoves = new ArrayList<Move>();
		System.out.println("Enter a move, with row and col of the piece you want to move and the row and col you want to move to, all separated by a space.");
		System.out.println("Example: 0 3 2 3");
		System.out.println();

		int pRow;
		int pCol;
		int dRow;
		int dCol;
		while(!gameOver)
		{
			attackMoves = board.checkAllMoves(false);
			System.out.println("Attacker's Turn");
			int move = abPlayer.maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
			Move bestMove = attackMoves.get(move);
			board.makeMove(bestMove);
			board.printBoard();

			gameOver = board.gameOver(true);

			if(!gameOver) {
				defendMoves = board.checkAllMoves(true);
				System.out.println("Defender's Turn");
				pRow = scnr.nextInt();
				pCol = scnr.nextInt();
				dRow = scnr.nextInt();
				dCol = scnr.nextInt();
				while(!defendMoves.contains(new Move(pRow, pCol, dRow, dCol)))
				{
					System.out.println("Invalid move");
					System.out.println("Make sure the piece you are trying to move is a defender");
					System.out.println("and remember pieces cant move diagonally or go through other pieces");
					System.out.println("Please enter another move:");
					pRow = scnr.nextInt();
					pCol = scnr.nextInt();
					dRow = scnr.nextInt();
					dCol = scnr.nextInt();
				}
				board.makeMove(new Move(pRow, pCol, dRow, dCol));
				board.printBoard();

				gameOver = board.gameOver(false);
			}
		}
		int winner = board.winner();
		if(winner == 1)
			System.out.println("Attackers Win!");
		else
			System.out.println("Defenders Win!");
	}

	/**
	*	Human as Attacker vs AlphaBeta as Defender
	**/
	public static void onePlayerAtt() throws IOException {
		Scanner scnr = new Scanner(System.in);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		//0-black/Attacker 1-white/Defender
		Board board = new Board();

		board.printBoard();
		boolean gameOver = false;
		AlphaBeta abPlayer = new AlphaBeta();
		ArrayList<Move> attackMoves = new ArrayList<Move>();
		ArrayList<Move> defendMoves = new ArrayList<Move>();
		System.out.println("Enter a move, with row and col of the piece you want to move and the row and col you want to move to, all separated by a space.");
		System.out.println("Example: 0 3 2 3");
		System.out.println();

		int pRow;
		int pCol;
		int dRow;
		int dCol;
		int m;
		Move bestMove;

		while(!gameOver)
		{
			attackMoves = board.checkAllMoves(false);
			System.out.println("Attacker's Turn");
			pRow = scnr.nextInt();
			pCol = scnr.nextInt();
			dRow = scnr.nextInt();
			dCol = scnr.nextInt();
			Move move = new Move(pRow, pCol, dRow, dCol);
			while(!attackMoves.contains(new Move(pRow, pCol, dRow, dCol)))
			{
				System.out.println("Invalid move");
				System.out.println("Make sure the piece you are trying to move is an attacker");
				System.out.println("and remember pieces cant move diagonally or go through other pieces");
				System.out.println("Please enter another move:");
				pRow = scnr.nextInt();
				pCol = scnr.nextInt();
				dRow = scnr.nextInt();
				dCol = scnr.nextInt();
			}
			board.makeMove(new Move(pRow, pCol, dRow, dCol));
			System.out.println(dtf.format(LocalDateTime.now()));
			board.printBoard();

			gameOver = board.gameOver(true);

			if(!gameOver) {
				defendMoves = board.checkAllMoves(true);
				m = abPlayer.minValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
				bestMove = defendMoves.get(m);
				board.makeMove(bestMove);
				System.out.println("Defender's Turn");
				System.out.println(dtf.format(LocalDateTime.now()));
				board.printBoard();

				gameOver = board.gameOver(false);
			}
		}
		int winner = board.winner();
		if(winner == 1)
			System.out.println("Attackers Win!");
		else if(winner == -1)
			System.out.println("Defenders Win!");
		else
			System.out.println("Draw!");
	}

	/**
	*	Human vs Human
	**/
	public static void twoPlayer() throws IOException {

		Scanner scnr = new Scanner(System.in);

		//0-black/Attacker 1-white/Defender
		Board board = new Board();
		board.makeMove(new Move(3, 5, 3, 6));
		board.printBoard();
		boolean gameOver = false;
		ArrayList<Move> attackMoves = new ArrayList<Move>();
		ArrayList<Move> defendMoves = new ArrayList<Move>();
		System.out.println("Enter a move, with row and col of the piece you want to move and the row and col you want to move to, all separated by a space.");
		System.out.println("Example: 0 3 2 3");
		System.out.println();

		int pRow;
		int pCol;
		int dRow;
		int dCol;
		while(!gameOver)
		{
			attackMoves = board.checkAllMoves(false);
			System.out.println("Attacker's Turn");
			pRow = scnr.nextInt();
			pCol = scnr.nextInt();
			dRow = scnr.nextInt();
			dCol = scnr.nextInt();
			Move move = new Move(pRow, pCol, dRow, dCol);
			while(!attackMoves.contains(new Move(pRow, pCol, dRow, dCol)))
			{
				System.out.println("Invalid move");
				System.out.println("Make sure the piece you are trying to move is an attacker");
				System.out.println("and remember pieces cant move diagonally or go through other pieces");
				System.out.println("Please enter another move:");
				pRow = scnr.nextInt();
				pCol = scnr.nextInt();
				dRow = scnr.nextInt();
				dCol = scnr.nextInt();
			}
			board.makeMove(new Move(pRow, pCol, dRow, dCol));
			board.printBoard();

			gameOver = board.gameOver(true);

			if(!gameOver) {
				defendMoves = board.checkAllMoves(true);
				System.out.println("Defender's Turn");
				pRow = scnr.nextInt();
				pCol = scnr.nextInt();
				dRow = scnr.nextInt();
				dCol = scnr.nextInt();
				while(!defendMoves.contains(new Move(pRow, pCol, dRow, dCol)))
				{
					System.out.println("Invalid move");
					System.out.println("Make sure the piece you are trying to move is a defender");
					System.out.println("and remember pieces cant move diagonally or go through other pieces");
					System.out.println("Please enter another move:");
					pRow = scnr.nextInt();
					pCol = scnr.nextInt();
					dRow = scnr.nextInt();
					dCol = scnr.nextInt();
				}
				board.makeMove(new Move(pRow, pCol, dRow, dCol));
				board.printBoard();

				gameOver = board.gameOver(false);
			}
		}
		int winner = board.winner();
		if(winner == 1)
			System.out.println("Attackers Win!");
		else if(winner == -1)
			System.out.println("Defenders Win!");
		else
			System.out.println("Draw!");
	}

	/**
	*	AlphaBeta as Defender vs ReinforcementLearner player as Attacker
	**/
	public static void ABvsRFP() throws IOException {
		//AB is defender

		Board startboard = new Board();
		Board board = new Board();
		boolean print = true;

		int set = 2;
		float[] weights1= {-0.3806015f,0.0f,3.2810545f,-0.012918153f,-0.08309503f,-0.054301832f,-0.11671995f,-0.19934325f,-0.24144311f,-0.094996035f,-0.013387685f,-0.09792074f,-0.08841559f,0.011717776f,-0.058839675f,-0.038746674f,0.027929384f,0.010834f,0.015122965f,-0.028828625f,-0.098380364f,-0.11594161f,-0.021403445f,-0.019142706f,-0.022159388f,0.09813788f,-0.036829785f,0.033982318f,-0.09311209f,-0.06023634f,-0.32120407f,-0.03434368f,-0.023968736f,-0.038568035f,0.05178098f,0.031214258f,0.08129422f,-0.027790355f,-0.1920826f,-0.20463406f,-0.03136002f,0.012208966f,-0.066291496f,0.058884766f,0.14387378f,0.0717445f,0.06581178f,-0.13073857f,-0.3621839f,-0.073070474f,0.0014147171f,-0.041711327f,0.06578834f,0.08230451f,0.03787202f,-0.03579878f,-0.20139052f,-0.06969262f,-0.04996449f,7.203393E-4f,-0.043857656f,0.05886272f,0.026495902f,-0.003463533f,-0.0320914f,-0.06259586f,-0.120447904f,-0.0011610986f,-0.07558877f,-0.01323544f,-0.018675392f,-0.06006394f,-0.073353276f,-0.03329355f,-0.11520757f,-0.09863916f,-0.09936947f,-0.15152776f,-0.15896092f,-0.21291752f,-0.25115553f,-0.16684121f,-0.0475902f,-0.13447471f,0.0f,-7.289715f,-5.832768f,-5.2069345f,-4.8282375f,-4.7967753f,-4.82092f,-4.5535216f,-4.185531f,-3.7224452f,-3.321057f,-3.0500534f,-2.9109573f,-2.8089244f,-2.781233f,-2.7127478f,-2.661592f,-2.6614575f,-2.6074255f,-2.5865316f,-2.6059928f,-2.5665658f,-2.7374384f,-2.7714527f,-3.025446f,-3.3416586f,-3.6757388f,-3.9631505f,-4.147635f,-4.2574954f,-4.207954f,-4.073408f,-3.8312855f,-3.452653f,-3.0080411f,-2.6088333f,-2.0805929f,-1.3936316f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
		float[] weights2 = {0.29381686f, 0.0f, -0.10316127f, 0.47958216f, 0.0f, -0.34179604f, -0.32176661f, -0.19617052f, -0.31480432f, 0.0538347f, -0.34940967f, -0.1071125f, -0.3273485f, -0.29761973f, 0.0f, -0.32746312f, -0.32060423f, -0.32357377f, -0.3132656f, -0.058509868f, -0.29952294f, 0.045759413f, -0.1955517f, -0.27328232f, -0.28060845f, -0.2560712f, -0.31182253f, -0.28726262f, -0.29382205f, -0.06794517f, -0.09392067f, -0.27629516f, -0.12435098f, -0.23328349f, -0.2623191f, -0.28915143f, -0.2897677f, -0.11521422f, -0.32993215f, -0.33425623f, -0.25608698f, -0.2691418f, -0.26309067f, -0.25578937f, -0.29272214f, -0.3064018f, -0.2289047f, -0.19573785f, -0.26894543f, -0.33969045f, -0.28249234f, -0.25419703f, -0.24308504f, -0.19022958f, -0.23726022f, -0.25790685f, -0.27660468f, -0.3573626f, -0.26362905f, -0.14234999f, -0.25698388f, -0.30739635f, -0.10899901f, -0.26181835f, 0.0f, -0.04200297f, -0.31327716f, -0.31215188f, -0.12429337f, 0.08587328f, -0.03741414f, -0.2996889f, -0.343414f, -0.23558924f, -0.2974674f, -0.32167092f, -0.11246128f, -0.24359578f, -0.2571115f, -0.25669438f, -0.13929701f, -0.07087071f, -0.2627872f, -0.31119853f, -0.31577858f, -0.2771072f, -0.24501559f, -0.28536615f, -0.27646616f, -0.31025416f, -0.1519522f, -0.26746532f, -0.31454995f, -0.31501517f, -0.34725216f, -0.21495554f, -0.2996045f, -0.29673022f, -0.26478896f, -0.2957842f, -0.262441f, -0.18367435f, -0.28290072f, -0.34603184f, -0.32422745f, -0.2380782f, -0.29941836f, -0.29587486f, -0.030064205f, -0.09436763f, -0.31084487f, -0.28184757f, -0.2772468f, -0.27614385f, 0.0f, -0.33062637f, -0.2310077f, -0.34942093f, -0.22064507f, 0.038481932f, -0.35618025f, -0.10548986f, -0.32090038f, -0.26972508f, 0.0f};
		int[] results = new int[3];

		boolean gameOver = false;
		ReinforcementLearner RP1 = new ReinforcementLearner(false,weights2);
		AlphaBeta abPlayer = new AlphaBeta();
		ArrayList<Move> defendMoves = new ArrayList<Move>();
		int moveNum = 0;
		for(int k = 0; k < 100; ++k) {
			if(k%1000 == 0)
				print = true;
			board = new Board(startboard);
			if(print)
				board.printBoard();
			RP1.InitializePlayer(board,set);
			board.makeMove(RP1.a);
			//
			if(print)
				board.printBoard();
			//
			gameOver = board.gameOver(true);
			moveNum = 0;
		while(!gameOver)
		{
			moveNum++;
			defendMoves = board.checkAllMoves(true);
			//
			if(print)
				System.out.println("Attacker's Turn " + moveNum);
			//
			int move = abPlayer.minValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
			Move bestMove = defendMoves.get(move);

			board.makeMove(bestMove);
			//
			if(print)
				board.printBoard();
			//
			gameOver = board.gameOver(false);

				if(!gameOver) {
					RP1.UpdatePlayer(board,set);
					board.makeMove(RP1.a);
					//
					if(print)
						board.printBoard();
					//
					gameOver = board.gameOver(true);
				}else
				{
					RP1.UpdateWeights(board, set);
				}

		}
		int winner = board.winner();
		if(winner == 1) {
			System.out.println("Attackers Win!");
			results[0]++;
		}
		else if(winner == -1) {
			System.out.println("Defenders Win!");
			results[2]++;
		}
		else {
			System.out.println("Draw!");
			results[1]++;
		}



		print = false;
		}
		for(int i = 0; i < 3; ++i)
		{
			System.out.print(results[i] + " ");
		}
		System.out.println();
	}

	/**
	*	AlphaBeta as Attacker vs ReinforcementLearner player as Defender
	**/
	public static void ABvsRFP2() throws IOException {
		//AB is Attacker
		Board startboard = new Board();
		Board board = new Board();
		boolean print = true;

		int set = 1;
		float[] weights1= {-0.31592208f,0.0f,3.3196695f,0.03849493f,-0.06743157f,-0.0713964f,-0.11052737f,-0.2326871f,-0.33529422f,-0.10855265f,0.0058665555f,-0.050058447f,-0.048732743f,0.0124330465f,-0.050456475f,-0.026482722f,0.03190337f,0.030601794f,0.017886333f,-0.019848032f,-0.08815921f,-0.120531105f,-0.0297658f,-0.03056547f,0.01973001f,0.17205949f,-0.050425194f,0.048438076f,-0.070204265f,-0.056518313f,-0.3493505f,-0.03305351f,-0.047406934f,-0.0641597f,0.06900347f,0.026498655f,0.07815385f,-0.04035704f,-0.19830836f,-0.21608777f,-0.019020127f,0.0490665f,-0.028611304f,0.025416031f,0.091716066f,0.124828056f,0.08926956f,-0.1975717f,-0.3988808f,-0.09515572f,-0.023332026f,-0.064843796f,0.06575548f,0.06170859f,0.0034358401f,-0.054501645f,-0.25002474f,-0.07931485f,-0.038631905f,0.008989907f,-0.026088754f,0.09146222f,0.032926507f,0.023484804f,-0.016720599f,-0.030580807f,-0.12108614f,-0.014514958f,-0.069314055f,-0.023469266f,-0.004958263f,-0.04080517f,-0.05651418f,4.8690283E-4f,-0.10616438f,-0.051916428f,-0.058173742f,-0.15320727f,-0.20853263f,-0.19286904f,-0.3073467f,-0.1873555f,-0.04277839f,-0.12997785f,0.0f,-6.58135f,-5.313273f,-4.748118f,-4.281028f,-4.212187f,-4.4018455f,-3.9102986f,-3.4949338f,-3.124002f,-2.77458f,-2.4912047f,-2.4895532f,-2.4411469f,-2.4077737f,-2.3892086f,-2.3433163f,-2.3132205f,-2.247828f,-2.1899405f,-2.2211676f,-2.275352f,-2.2993383f,-2.3952048f,-2.4938102f,-2.7325587f,-3.0645466f,-3.4213915f,-3.742839f,-3.8180618f,-3.88387f,-3.6042445f,-3.3532546f,-2.9625123f,-2.5963824f,-2.183755f,-1.771189f,-1.0273753f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
		float[] weights2 = {0.27922928f, 0.0f, 0.061333966f, 0.32273996f, 0.0f, -0.3033143f, -0.33699754f, -0.167313f, -0.2897196f, 0.053964064f, -0.28140542f, -0.10056925f, -0.30690566f, -0.31556597f, 0.0f, -0.30306143f, -0.27912295f, -0.28154942f, -0.2740273f, -0.027637202f, -0.24959275f, 0.08075326f, -0.15615858f, -0.24147809f, -0.26059592f, -0.25897348f, -0.28207758f, -0.277596f, -0.2857674f, -0.05359645f, -0.056300506f, -0.27944395f, -0.096696645f, -0.20995514f, -0.22580744f, -0.25414187f, -0.3140282f, -0.101995535f, -0.33539087f, -0.26866573f, -0.24264053f, -0.28170958f, -0.2901055f, -0.23297638f, -0.23393214f, -0.2546414f, -0.25209072f, -0.20048264f, -0.26901224f, -0.28388622f, -0.29553556f, -0.25580987f, -0.2923118f, -0.18925405f, -0.26251966f, -0.24086589f, -0.27423525f, -0.302155f, -0.27450708f, -0.14986673f, -0.23141907f, -0.2741906f, -0.10033553f, -0.2857699f, 0.0f, -0.040669095f, -0.26232433f, -0.27869385f, -0.102505505f, 0.09859958f, -0.033292506f, -0.28349262f, -0.25392625f, -0.22234638f, -0.30031815f, -0.34387538f, -0.097730055f, -0.25053227f, -0.23450194f, -0.22685306f, -0.09634698f, -0.039555173f, -0.24576096f, -0.28457388f, -0.2730258f, -0.279379f, -0.2504446f, -0.26501876f, -0.2642375f, -0.26031438f, -0.13551424f, -0.22690395f, -0.29092562f, -0.30512333f, -0.271504f, -0.19993787f, -0.2625142f, -0.27153215f, -0.2600268f, -0.25886583f, -0.21517381f, -0.1619063f, -0.310462f, -0.3358355f, -0.29099172f, -0.2255749f, -0.25361508f, -0.26089397f, -0.062408175f, -0.06956962f, -0.27655324f, -0.26751032f, -0.2443788f, -0.27136135f, 0.0f, -0.3261364f, -0.24235435f, -0.34616902f, -0.19582526f, 0.06093751f, -0.316212f, -0.11970044f, -0.2966869f, -0.29256684f, 0.0f};
		int[] results = new int[3];

		boolean gameOver = false;
		ReinforcementLearner RP1 = new ReinforcementLearner(true, weights1);
		AlphaBeta abPlayer = new AlphaBeta();
		ArrayList<Move> attackMoves = new ArrayList<Move>();
		int moveNum = 0;
		for(int k = 0; k < 100; ++k) {
			if(k%100 == 0)
				print = true;
			board = new Board(startboard);
			if(print)
				board.printBoard();
			attackMoves = board.checkAllMoves(false);
			int move = abPlayer.maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
			Move bestMove = attackMoves.get(move);

			board.makeMove(bestMove);

			RP1.InitializePlayer(board,set);
			board.makeMove(RP1.a);
			//
			if(print)
				board.printBoard();
			//
			gameOver = board.gameOver(false);
			moveNum = 0;
		while(!gameOver)
		{
			moveNum++;
			attackMoves = board.checkAllMoves(false);
			//
			if(print)
				System.out.println("Attacker's Turn " + moveNum);
			//
			move = abPlayer.maxValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0)[1];
			 bestMove = attackMoves.get(move);

			board.makeMove(bestMove);
			//
			if(print)
				board.printBoard();
			//
			gameOver = board.gameOver(true);

				if(!gameOver) {
					RP1.UpdatePlayer(board,set);
					board.makeMove(RP1.a);
					//
					if(print)
						board.printBoard();
					//
					gameOver = board.gameOver(false);
				}else
				{
					RP1.UpdateWeights(board, set);
				}

		}
		int winner = board.winner();
		if(winner == 1) {
			System.out.println("Attackers Win!");
			results[0]++;
		}
		else if(winner == -1) {
			System.out.println("Defenders Win!");
			results[2]++;
		}
		else {
			System.out.println("Draw!");
			results[1]++;
		}


		print = false;
		}
		for(int i = 0; i < 3; ++i)
		{
			System.out.print(results[i] + " ");
		}
		System.out.println();
	}

	/**
	*	ReinforcementLearner player vs ReinforcementLearner player
	**/
	public static void RFPvsRFP() throws IOException{
		Board startboard = new Board();

		//FEATURES SET 1
		float[] attWeights1= {-0.3806015f,0.0f,3.2810545f,-0.012918153f,-0.08309503f,-0.054301832f,-0.11671995f,-0.19934325f,-0.24144311f,-0.094996035f,-0.013387685f,-0.09792074f,-0.08841559f,0.011717776f,-0.058839675f,-0.038746674f,0.027929384f,0.010834f,0.015122965f,-0.028828625f,-0.098380364f,-0.11594161f,-0.021403445f,-0.019142706f,-0.022159388f,0.09813788f,-0.036829785f,0.033982318f,-0.09311209f,-0.06023634f,-0.32120407f,-0.03434368f,-0.023968736f,-0.038568035f,0.05178098f,0.031214258f,0.08129422f,-0.027790355f,-0.1920826f,-0.20463406f,-0.03136002f,0.012208966f,-0.066291496f,0.058884766f,0.14387378f,0.0717445f,0.06581178f,-0.13073857f,-0.3621839f,-0.073070474f,0.0014147171f,-0.041711327f,0.06578834f,0.08230451f,0.03787202f,-0.03579878f,-0.20139052f,-0.06969262f,-0.04996449f,7.203393E-4f,-0.043857656f,0.05886272f,0.026495902f,-0.003463533f,-0.0320914f,-0.06259586f,-0.120447904f,-0.0011610986f,-0.07558877f,-0.01323544f,-0.018675392f,-0.06006394f,-0.073353276f,-0.03329355f,-0.11520757f,-0.09863916f,-0.09936947f,-0.15152776f,-0.15896092f,-0.21291752f,-0.25115553f,-0.16684121f,-0.0475902f,-0.13447471f,0.0f,-7.289715f,-5.832768f,-5.2069345f,-4.8282375f,-4.7967753f,-4.82092f,-4.5535216f,-4.185531f,-3.7224452f,-3.321057f,-3.0500534f,-2.9109573f,-2.8089244f,-2.781233f,-2.7127478f,-2.661592f,-2.6614575f,-2.6074255f,-2.5865316f,-2.6059928f,-2.5665658f,-2.7374384f,-2.7714527f,-3.025446f,-3.3416586f,-3.6757388f,-3.9631505f,-4.147635f,-4.2574954f,-4.207954f,-4.073408f,-3.8312855f,-3.452653f,-3.0080411f,-2.6088333f,-2.0805929f,-1.3936316f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
		float[] defWeights1= {-0.31592208f,0.0f,3.3196695f,0.03849493f,-0.06743157f,-0.0713964f,-0.11052737f,-0.2326871f,-0.33529422f,-0.10855265f,0.0058665555f,-0.050058447f,-0.048732743f,0.0124330465f,-0.050456475f,-0.026482722f,0.03190337f,0.030601794f,0.017886333f,-0.019848032f,-0.08815921f,-0.120531105f,-0.0297658f,-0.03056547f,0.01973001f,0.17205949f,-0.050425194f,0.048438076f,-0.070204265f,-0.056518313f,-0.3493505f,-0.03305351f,-0.047406934f,-0.0641597f,0.06900347f,0.026498655f,0.07815385f,-0.04035704f,-0.19830836f,-0.21608777f,-0.019020127f,0.0490665f,-0.028611304f,0.025416031f,0.091716066f,0.124828056f,0.08926956f,-0.1975717f,-0.3988808f,-0.09515572f,-0.023332026f,-0.064843796f,0.06575548f,0.06170859f,0.0034358401f,-0.054501645f,-0.25002474f,-0.07931485f,-0.038631905f,0.008989907f,-0.026088754f,0.09146222f,0.032926507f,0.023484804f,-0.016720599f,-0.030580807f,-0.12108614f,-0.014514958f,-0.069314055f,-0.023469266f,-0.004958263f,-0.04080517f,-0.05651418f,4.8690283E-4f,-0.10616438f,-0.051916428f,-0.058173742f,-0.15320727f,-0.20853263f,-0.19286904f,-0.3073467f,-0.1873555f,-0.04277839f,-0.12997785f,0.0f,-6.58135f,-5.313273f,-4.748118f,-4.281028f,-4.212187f,-4.4018455f,-3.9102986f,-3.4949338f,-3.124002f,-2.77458f,-2.4912047f,-2.4895532f,-2.4411469f,-2.4077737f,-2.3892086f,-2.3433163f,-2.3132205f,-2.247828f,-2.1899405f,-2.2211676f,-2.275352f,-2.2993383f,-2.3952048f,-2.4938102f,-2.7325587f,-3.0645466f,-3.4213915f,-3.742839f,-3.8180618f,-3.88387f,-3.6042445f,-3.3532546f,-2.9625123f,-2.5963824f,-2.183755f,-1.771189f,-1.0273753f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};

		//FEATURES SET 2
		float[] attWeights2 = {0.29381686f, 0.0f, -0.10316127f, 0.47958216f, 0.0f, -0.34179604f, -0.32176661f, -0.19617052f, -0.31480432f, 0.0538347f, -0.34940967f, -0.1071125f, -0.3273485f, -0.29761973f, 0.0f, -0.32746312f, -0.32060423f, -0.32357377f, -0.3132656f, -0.058509868f, -0.29952294f, 0.045759413f, -0.1955517f, -0.27328232f, -0.28060845f, -0.2560712f, -0.31182253f, -0.28726262f, -0.29382205f, -0.06794517f, -0.09392067f, -0.27629516f, -0.12435098f, -0.23328349f, -0.2623191f, -0.28915143f, -0.2897677f, -0.11521422f, -0.32993215f, -0.33425623f, -0.25608698f, -0.2691418f, -0.26309067f, -0.25578937f, -0.29272214f, -0.3064018f, -0.2289047f, -0.19573785f, -0.26894543f, -0.33969045f, -0.28249234f, -0.25419703f, -0.24308504f, -0.19022958f, -0.23726022f, -0.25790685f, -0.27660468f, -0.3573626f, -0.26362905f, -0.14234999f, -0.25698388f, -0.30739635f, -0.10899901f, -0.26181835f, 0.0f, -0.04200297f, -0.31327716f, -0.31215188f, -0.12429337f, 0.08587328f, -0.03741414f, -0.2996889f, -0.343414f, -0.23558924f, -0.2974674f, -0.32167092f, -0.11246128f, -0.24359578f, -0.2571115f, -0.25669438f, -0.13929701f, -0.07087071f, -0.2627872f, -0.31119853f, -0.31577858f, -0.2771072f, -0.24501559f, -0.28536615f, -0.27646616f, -0.31025416f, -0.1519522f, -0.26746532f, -0.31454995f, -0.31501517f, -0.34725216f, -0.21495554f, -0.2996045f, -0.29673022f, -0.26478896f, -0.2957842f, -0.262441f, -0.18367435f, -0.28290072f, -0.34603184f, -0.32422745f, -0.2380782f, -0.29941836f, -0.29587486f, -0.030064205f, -0.09436763f, -0.31084487f, -0.28184757f, -0.2772468f, -0.27614385f, 0.0f, -0.33062637f, -0.2310077f, -0.34942093f, -0.22064507f, 0.038481932f, -0.35618025f, -0.10548986f, -0.32090038f, -0.26972508f, 0.0f};
		float[] defWeights2 = {0.27922928f, 0.0f, 0.061333966f, 0.32273996f, 0.0f, -0.3033143f, -0.33699754f, -0.167313f, -0.2897196f, 0.053964064f, -0.28140542f, -0.10056925f, -0.30690566f, -0.31556597f, 0.0f, -0.30306143f, -0.27912295f, -0.28154942f, -0.2740273f, -0.027637202f, -0.24959275f, 0.08075326f, -0.15615858f, -0.24147809f, -0.26059592f, -0.25897348f, -0.28207758f, -0.277596f, -0.2857674f, -0.05359645f, -0.056300506f, -0.27944395f, -0.096696645f, -0.20995514f, -0.22580744f, -0.25414187f, -0.3140282f, -0.101995535f, -0.33539087f, -0.26866573f, -0.24264053f, -0.28170958f, -0.2901055f, -0.23297638f, -0.23393214f, -0.2546414f, -0.25209072f, -0.20048264f, -0.26901224f, -0.28388622f, -0.29553556f, -0.25580987f, -0.2923118f, -0.18925405f, -0.26251966f, -0.24086589f, -0.27423525f, -0.302155f, -0.27450708f, -0.14986673f, -0.23141907f, -0.2741906f, -0.10033553f, -0.2857699f, 0.0f, -0.040669095f, -0.26232433f, -0.27869385f, -0.102505505f, 0.09859958f, -0.033292506f, -0.28349262f, -0.25392625f, -0.22234638f, -0.30031815f, -0.34387538f, -0.097730055f, -0.25053227f, -0.23450194f, -0.22685306f, -0.09634698f, -0.039555173f, -0.24576096f, -0.28457388f, -0.2730258f, -0.279379f, -0.2504446f, -0.26501876f, -0.2642375f, -0.26031438f, -0.13551424f, -0.22690395f, -0.29092562f, -0.30512333f, -0.271504f, -0.19993787f, -0.2625142f, -0.27153215f, -0.2600268f, -0.25886583f, -0.21517381f, -0.1619063f, -0.310462f, -0.3358355f, -0.29099172f, -0.2255749f, -0.25361508f, -0.26089397f, -0.062408175f, -0.06956962f, -0.27655324f, -0.26751032f, -0.2443788f, -0.27136135f, 0.0f, -0.3261364f, -0.24235435f, -0.34616902f, -0.19582526f, 0.06093751f, -0.316212f, -0.11970044f, -0.2966869f, -0.29256684f, 0.0f};
		Board board = new Board();
		boolean print = true;

		boolean gameOver = false;
		ReinforcementLearner RP1 = new ReinforcementLearner(false,attWeights1);
		ReinforcementLearner RP2 = new ReinforcementLearner(true,defWeights1);

		int attSet = 1;
		int defSet = 1;

		int moveNum = 0;
		int[] results = new int[3];
		for(int k = 0; k < 1000; ++k) {
			if(k%1000 == 0)
				print = true;
			board = new Board(startboard);
			if(print)
				board.printBoard();
			RP1.InitializePlayer(board,attSet);
			board.makeMove(RP1.a);
			//
			if(print)
				board.printBoard();
			//
			if(!board.gameOver(true)) {
				RP2.InitializePlayer(board,defSet);
				board.makeMove(RP2.a);
				//
				if(print)
					board.printBoard();
				//
			}
			else {
				RP2.UpdateWeights(board,defSet);
			}
			gameOver = board.gameOver(false);
			moveNum = 0;
		while(!gameOver)
		{
			moveNum++;
			//
			if(print)
				System.out.println("Turn " + moveNum);
			//
			RP1.UpdatePlayer(board,attSet);
			board.makeMove(RP1.a);
			//
			if(print) {
				board.printBoard();
				System.out.println(RP1.reward);
			}
			//
			gameOver = board.gameOver(true);

				if(!gameOver) {
					RP2.UpdatePlayer(board,defSet);
					board.makeMove(RP2.a);
					//
					if(print) {
						board.printBoard();
						System.out.println(RP2.reward);
					}
					//
					gameOver = board.gameOver(false);
					if(gameOver)
						RP1.UpdateWeights(board,attSet);
				}else
				{
					RP2.UpdateWeights(board,defSet);
				}
		}
		int winner = board.winner();
		if(winner == 1)
			results[0]++;
		else if(winner == -1)
			results[2]++;
		else
			results[1]++;
		if(print)
		{
			winner = board.winner();
			if(winner == 1)
				System.out.println("Attackers Win!");
			else if(winner == -1)
				System.out.println("Defenders Win!");
			else
				System.out.println("Draw!");
			System.out.println("\t----After " + k + " games----");
			System.out.println("Player 1 weights:");
			for(int i = 0; i < RP1.weights.length ; ++i)
			{
				System.out.print(RP1.weights[i] + "f, ");
			}
			System.out.println();
			System.out.println("Player 2 weights:");
			for(int i = 0; i < RP2.weights.length ; ++i)
			{
				System.out.print(RP2.weights[i] + "f, ");
			}
			System.out.println();
			printWeightsToFile("AttackerWeights.txt", RP1.weights, k);
			printWeightsToFile("DefenderWeights.txt", RP2.weights, k);
		}
		print = false;
		}
		for(int i = 0; i < 3; ++i)
		{
			System.out.print(results[i] + " ");
		}
		System.out.println();
	}

	/**
	*	prints weights for ReinforcentLearner to provided file path
	* the weights are what the ReinforcentLearner is actually learning,
	* printing these weights to a file is sort of like a checkpoint of what it has learned
	*
	* @param file file path to print weights to
	* @param weights current weights for learner
	* @param numGames number of games played
	**/
	public static void printWeightsToFile(String file, float[] weights, int numGames)
	{
		try {
			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.append("After " + numGames + " games:\n");
			for(int i = 0; i < weights.length; ++i)
			{
				fileWriter.append(weights[i] + "f,");
			}
			fileWriter.append("\n\n");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	*	Displays opening text with options,
	* Currently set up to just test each type of game
	**/
	public static void main(String[] args) throws IOException {
		ABvsRFP2();
		/*Scanner scnr = new Scanner(System.in);
		System.out.println("Welcome to Hnefatafl");
		System.out.println("In this game, there are the Defenders, marked with a D,");
		System.out.println("Attackers marked with an A, and the King is marked with a K.");
		System.out.println("Restricted squares are marked with an X, only the King can be on them.\n");

		System.out.println("Pieces are captured when sandwiched between two opposing pieces or between an opposing piece and a restricted square.");
		System.out.println("The King is captured when surrounded on all 4 sides by attackers,");
		System.out.println("He can only be captured against the edge of the board if there are no other defenders left\n");

		System.out.println("The objective of the attackers is to capture the king, while the defenders are trying to escort their king");
		System.out.println("to any of the four corners.");
		System.out.println("Good luck!\n");
		System.out.println("\nHow many players? 0, 1 or 2?");
		int input = scnr.nextInt();
		while(input < 0 || input >2) {
			System.out.println("\nHow many players? 0, 1 or 2?");
			input = scnr.nextInt();
		}
		if(input == 0)
		{
			ABvsAB();
		}else if(input == 1) {
			System.out.println("What side would you like to play? 0-Attackers, 1-Defenders");
			input = scnr.nextInt();
			while(input != 0 && input != 1)
			{
				input = scnr.nextInt();
			}
			if(input == 0)
			{
				onePlayerAtt();
			}
			else{
				onePlayerDef();
			}
		}else {
			twoPlayer();
		}*/
	}
}
