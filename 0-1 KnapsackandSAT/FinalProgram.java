import java.util.*;

//runtime O(nW)
//start with 100 items of weight max 100 and ramp from there
public class FinalProgram {

	static Random rand = new Random();

	/**
	* Default dynamic programming approach to Knapsack problem
	*
	*	@param W maximum weight you can carry
	*	@param items list of items you can take
	**/
	//O(nW)
	public static Integer KnapsackDefault(int W, Item[] items){
		int n = items.length;
		int[][] F = new int[n+1][W+1];
		boolean[][] take = new boolean[n+1][W+1];
		for(int k = 0; k <= W; ++k)
		{
			F[n][k] = 0;
		}
		for(int i = n-1; i > -1; --i)
		{
			for(int w = 0; w <= W; ++w)
			{
				if(items[i].w <= w)
				{
					if(F[i+1][w-items[i].w] + items[i].v>F[i+1][w])
					{
						F[i][w] = F[i+1][w-items[i].w] + items[i].v;
						take[i][w] = true;
					}
					else
					{
						F[i][w] =F[i+1][w];
						take[i][w] = false;
					}
				}
				else
				{
					F[i][w] = F[i+1][w];
					take[i][w] = false;
				}
			}
		}
		return F[0][W];
		//return new ReturnType(F[0][W],F,take);
	}

	/**
	* Greedy approach to knapsack problem, sort items by value/weight and take until full
	* Find the item with the max value, if that value is higher than the accumulated
	*	value of the items taken, take that item instead
	*
	*	@param W maximum weight you can carry
	*	@param items list of items you can take
	**/
	public static ReturnType ModifiedGreedyKnapsack(int W, Item[] items){
		int w = W;
		int i = items.length-1;
		Arrays.sort(items);

		ArrayList<Item> itemList = new ArrayList<Item>();
		while (w > 0 && i >-1)
		{
			if(items[i].w < w)
			{
				itemList.add(items[i]);
				w -= items[i].w;
			}
			--i;
		}
		Item max = items[0];
		for(i = 1; i < items.length; ++i)
		{
			if(items[i].v > max.v)
				max = items[i];
		}
		int itemVals = 0;
		for(i = 0; i < itemList.size(); ++i)
		{
			itemVals += itemList.get(i).v;
		}
		if(max.v > itemVals) {
			itemVals = max.v;
			itemList = new ArrayList<Item>();
			itemList.add(max);
		}
		return new ReturnType(itemVals,itemList);

	}

	/**
	* A dynamic programming approach to knapsack that focuses on minimizing the cost
	* while achieving a target value: t
	*
	*	@param W maximum weight you can carry
	*	@param items list of items you can take
	**/
	//O(n^2 * v(max))
	public static ReturnType MinCostKnapsack(int W, Item[] items){
		int n = items.length;
		//Find max value item
		Item aMax = items[0];
		for(int i = 1; i < n; ++i)
		{
			if(aMax.v < items[i].v)
				aMax = items[i];
		}
		float[][] minCost = new float[n][n * aMax.v+1];
		boolean[][] take = new boolean[n][n * aMax.v+1];
		for(int i = 0; i < n; ++i)
			minCost[i][0] = 0;

		for(int t = 1; t <= items[0].v; ++t)
		{
			minCost[0][t] = items[0].w;
			take[0][t] = true;
		}

		for(int t = items[0].v+1; t <= n * aMax.v; ++t)
		{
			minCost[0][t] = Float.POSITIVE_INFINITY;
			take[0][t] = false;
		}
		int nextT;
		for(int i = 1; i < n; ++i)
		{
			for(int t = 1; t <= n * aMax.v; ++t)
			{
				nextT = Math.max(0, t - items[i].v);
				if(minCost[i-1][t] <= items[i].w + minCost[i-1][nextT])
				{
					minCost[i][t] = minCost[i-1][t];
					take[i][t] = false;
				}
				else
				{
					minCost[i][t] = items[i].w + minCost[i-1][nextT];
					take[i][t] = true;
				}
			}
		}
		int[][] minCostInt = new int[minCost.length][minCost[0].length];
		for(int i = 0; i < minCost.length; ++i)
		{
			for(int j = 0; j < minCost[0].length; ++j)
			{
				minCostInt[i][j] = (int)minCost[i][j];
			}
		}
		int i = 0;
		while(i < (n* aMax.v) && (minCost[n-1][i] <= W))
			++i;
		return new ReturnType(i-1, minCostInt, take);
	}

	/**
	* Fully Polynomial Time Approximation Scheme
	* approximates a solution within the given error tolerance, runs in polynomial time
	*
	*	@param W maximum weight you can carry
	*	@param itms list of items you can take
	* @param e tolerance for error, within how much of the optimal solution
	**/
	public static ReturnType FPTAS(int W, Item[] itms, float e) {

		Item[] items = new Item[itms.length];
		for(int i = 0; i < itms.length; ++i)
		{
			items[i] = new Item(itms[i].w, itms[i].v);
		}
		Item aMax = items[0];
		int n = items.length;
		for(int i = 1; i < n; ++i)
		{
			if(aMax.v < items[i].v)
				aMax = items[i];
		}
		float scale = (1.0f/(float)aMax.v) * (float)n * (1.0f/e);
		for(int i = 0; i < items.length; ++i)
		{
			items[i].v = (int) ((float)items[i].v * scale);
		}
		ReturnType rt = MinCostKnapsack(W, items);

		int r = rt.b.length-1;
		int c = rt.i;
		int valSum = 0;

		while(r >-1)
		{
			if(rt.b[r][c]) {
				valSum += itms[r].v;
				c -= items[r].v;
				if(c < 0)
					c = 0;
			}
			r-=1;
		};
		rt.i = valSum;

		return rt;
	}

	/**
	* GSAT flips each variable and finds which flip lead to the most satisfied clauses
	* you then flip that variable and start the process over again
	*
	*	@param clauses list of clauses to be satisfied
	*	@param literals array of variable true/false values
	* @param numFlip number of times you want to do the process of fliping variable values
	**/
	public static ReturnType GSAT(ArrayList<int[]> clauses, boolean[] literals, int numFlip) {
		int numUnsat = 0;
		int minUnsat = clauses.size()+1;
		int minFlip = 0;

		for(int i = 0; i < literals.length; ++i)
		{
			if(rand.nextDouble()>.5)
				literals[i] = true;
			else
				literals[i] = false;
		}

		for(int k = 0; k < numFlip; ++k) {
			for(int j = 0; j < literals.length; ++j)
			{
				literals[j] = !literals[j];
				numUnsat = 0;
				boolean sat;
				for(int i = 0; i < clauses.size(); ++i)
				{
					sat = false;
					int[] temp = clauses.get(i);
					for(int l = 0; l < temp.length; ++l)
					{
						if(temp[l]<0)
						{
							if(!literals[(temp[l] * -1) - 1])
							{
								sat = true;
								break;
							}
						}
						else {
							if(literals[temp[l] - 1])
							{
								sat = true;
								break;
							}
						}
					}
					if(!sat)
						numUnsat++;

				}
				literals[j] = !literals[j];
				if(numUnsat < minUnsat)
				{
					minUnsat = numUnsat;
					minFlip = j;
				}
			}
			literals[minFlip]=  !literals[minFlip];

		}

		return new ReturnType(clauses.size()-minUnsat, literals);
	}

	/**
	* Reformats truthAssignment so it is a matrix where the second row indicated whether or not the variable
	* has been assigned, then calls DPLL
	*
	*	@param formula list of truth clauses, variables are listed as their variable number, negative numbers mean they are a NOT
	*	@param truthAssignment the true false values assigned to the variables to satify the formula
	**/
	public static ReturnType DPLL(ArrayList<int[]> formula, boolean[] truthAssignment)
	{
		boolean[][] newAssignment = new boolean[truthAssignment.length][2];
		for(int i = 0; i < truthAssignment.length; ++i)
		{
			newAssignment[i][0] = truthAssignment[0];
			newAssignment[i][1] = false;

		}
		return DPLL(formula,newAssignment);
	}

	/**
	* Davis Putnam Logemann Loveland Algorithm
	* Calls unit propagation, if the returned formula is empty then return it is satisfiable.
	* Otherwise, add a new unit clause with the variable and set it to true, call DPLL again,
	* if satisifiable return, otherwise replace unit clause with NOT equivalent and set variable to false and call DPLL again.
	*
	*	@param formula list of truth clauses, variables are listed as their variable number, negative numbers mean they are a NOT
	*	@param truthAssignment the true false values assigned to the variables to satify the formula
	**/
	public static ReturnType DPLL(ArrayList<int[]> formula, boolean[][] truthAssignment) {
		ReturnType rt = UnitPropagation(formula, truthAssignment);
		ArrayList<int[]> newFormula = rt.clause;
		boolean[][] newAssignment = rt.b;
		if(newFormula.isEmpty()) {
			return new ReturnType(true, newAssignment);
		}
		for(int i = 0; i < formula.size(); ++i)
		{
			if(newFormula.get(i) == null) {
				return new ReturnType(false,newAssignment);
			}
		}
		int i = 0;
		while(i < newAssignment.length && newAssignment[i][1])
		{
			i++;
		}
		if(i >= newAssignment.length)
		{
			for(int k = 0; k < newFormula.size(); ++k)
			{
				System.out.print(k+ ": ");
				for(int j =0; j< newFormula.get(k).length; ++j) {
					System.out.print(newFormula.get(k)[j]+" ");
				}
				System.out.println();
			}
		}

		newAssignment[i][0] = true;
		newAssignment[i][1] = true;
		newFormula.add(0,new int[]{i+1});

		ReturnType result = DPLL(makeCopy(newFormula), makeCopy(newAssignment));
		if(result.satisfiable)
		{
			return new ReturnType(true, result.b);
		}
		else {
			newFormula.get(0)[0]= newFormula.get(0)[0] * -1;
			newAssignment[i][0] = false;
			return DPLL(newFormula, newAssignment);
		}

	}

	/**
	* Unit Propogation
	* While the formula contains a unit clause and not an empty clause, select the variable from the unit clause,
	* if the variable appears positively in the clause then set it to true,
	* remove every clause with it, and remove its NOT from each clause it is in.
	* Otherwise if it apears negatively (NOT), set it to false, remove ever clause with it's NOT
	* and remove it from ever clause it is in.
	* continute this until the while conditional is not met
	*
	*	@param formula list of truth clauses, variables are listed as their variable number, negative numbers mean they are a NOT
	*	@param truthAssignment the true false values assigned to the variables to satify the formula
	**/
	public static ReturnType UnitPropagation(ArrayList<int[]> formula, boolean[][] truthAssignment)
	{
		//unit clause = clause with one variable
		while(!formula.isEmpty() && containsUnit(formula) && !containsEmpty(formula))
		{
			int x = 0;
			for(int i = 0; i < formula.size(); ++i)
			{
				if(formula.get(i).length == 1) {
					x = formula.get(i)[0];
					break;
				}
			}
			//if x is not negated
			if(x ==0)
				System.out.println("ERROR X SHOULD NOT BE 0");
			if(!(x<0)) {
				truthAssignment[x-1][0] = true;
				truthAssignment[x-1][1] = true;
			}
			else {
				truthAssignment[(x * -1)-1][0] = false;
				truthAssignment[(x * -1)-1][1] = true;
			}

				for(int i = 0; i < formula.size(); ++i)
				{
					int[] temp = formula.get(i);
					for(int k = 0; k < temp.length; ++k)
					{
						if(temp[k] == x)
						{
							formula.remove(i);
							i--;
							break;
						}
						else if(temp[k] == (x* -1))
						{
							if(temp.length-1 <= 0){
								formula.remove(i);
								formula.add(i, null);
							}
							else {
							int[] t = new int[temp.length-1];
							int index = 0;
							for(int j = 0; j < temp.length;++j)
							{
								if((x * -1)!=temp[j])
								{
									t[index] = temp[j];
									index++;
								}
							}
							formula.remove(i);
							formula.add(i, t);
							i--;
							break;
							}
						}
					}
				}

		}
		return new ReturnType(formula, truthAssignment);
	}

	/**
	* Checks if the formula/clause list contains a unit clause
	*
	*	@param c list of clauses
	**/
	public static boolean containsUnit(ArrayList<int[]> c) {
		for(int i = 0; i < c.size(); ++i)
		{
			if(c.get(i) != null && c.get(i).length == 1)
				return true;
		}
		return false;

	}

	/**
	* Checks if the formula/clause list contains an empty clause
	*
	*	@param c list of clauses
	**/
	public static boolean containsEmpty(ArrayList<int[]> c){
		for(int i = 0; i < c.size(); ++i)
		{
			if(c.get(i) == null)
				return true;
		}
		return false;
	}

	/**
	* Randomized approach to MAX3SAT, selects true/false values randomly
	*
	*	@param clauses list of clauses to be satisfied
	*	@param literals array of variable true/false values
	**/
	public static ReturnType RandomizedMAX3SAT(ArrayList<int[]> clauses, boolean[] literals)
	{
		int numSat = 0;
		for(int i = 0; i < literals.length; ++i)
		{
			if(rand.nextDouble()>.5)
				literals[i] = true;
			else
				literals[i]= false;
		}
		boolean sat;
		for(int i = 0; i < clauses.size(); ++i)
		{
			sat = false;
			int[] temp = clauses.get(i);
			for(int k = 0; k < temp.length; ++k)
			{
				if(temp[k]<0)
				{
					if(!literals[temp[k] * -1 - 1])
					{
						sat = true;
						break;
					}
				}
				else {
					if(literals[temp[k] - 1])
					{
						sat = true;
						break;
					}
				}
			}
			if(sat)
				numSat++;
		}
		return new ReturnType(numSat, literals);
	}

	/**
	* Creates a list of items with random weight and value between specified values
	*
	*	@param numItems number of items you want created
	*	@param minWeight minimum weight the items can have
	*	@param maxWeight maximum weight the items can have
	*	@param minVal minimum value the items can have
	*	@param maxVal maximum value the items can have
	**/
	public static Item[] GenerateKnapsack(int numItems, int minWeight, int maxWeight, int minVal, int maxVal) {
		Item[] items = new Item[numItems];
		for(int i = 0; i < numItems; ++i)
		{
			items[i] = new Item(rand.nextInt(maxWeight-minWeight)+minWeight, rand.nextInt(maxVal-minVal)+minVal);
		}
		return items;
	}

	/**
	* Generates clauses of three variables for the 3SAT problem
	*
	*	@param numClauses number of clauses to create/satisfy
	*	@param numLiterals number of variables total amongst those clauses
	**/
	static public ReturnType Generate3SAT(int numClause, int numLiterals) {
		ArrayList<int[]> clauses = new ArrayList<int[]>();
		boolean[] literals = new boolean[numLiterals];

		for(int i = 0; i < numClause; ++i)
		{	ArrayList<Integer> index = new ArrayList<Integer>();
			for(int k = 0; k < 3; ++k)
			{
				int l = rand.nextInt(literals.length);
				l+=1;
				if(rand.nextDouble()>.5)
					l = l * -1;

				while(index.contains(l))
				{
					l = rand.nextInt(literals.length);
					l+=1;
					if(rand.nextDouble()>.5)
						l = l * -1;
				}
				index.add(l);

			}

			clauses.add(new int[]{index.get(0), index.get(1), index.get(2)});

		}
		return new ReturnType(clauses,literals);
	}

	/**
	* Creates a copy of the given arraylist
	*
	*	@param c arraylist to copy
	**/
	public static ArrayList<int[]> makeCopy(ArrayList<int[]> c){
		ArrayList<int[]> copy = new ArrayList<int[]>();
		for(int i = 0; i < c.size(); ++i)
		{
			copy.add(c.get(i));
		}
		return copy;
	}

	/**
	* Creates a copy of the given boolean matrix
	*
	*	@param c boolean matrix to copy
	**/
	public static boolean[][] makeCopy(boolean[][] c){
		boolean[][] copy = new boolean[c.length][c[0].length];
		for(int i = 0; i < c.length; ++i)
		{
			for(int j = 0; j < c[0].length; ++j)
			{
				copy[i][j] = c[i][j];
			}
		}
		return copy;
	}

	/**
	* Tests different Algorithms to the SAT problem, how well they perform and how long they take
	* DPLL, Random, and GSAT
	*
	*	@param numTest number of tests
	*	@param numL number of literals
	*	@param numC number of clauses
	*	@param numFlip number of times you want to do the process of fliping variable values in GSAT
	**/
	public static void SAT(int numTest, int numL, int numC, int numFlip)
	{

		long timeStart;
	    long timeEnd;
	    int time;

		int[] DPLLRunTime = new int[numTest];
	    int DPLLSumSat = 0;
	    double DPLLSumTime = 0;

	    int[] randRunTime = new int[numTest];
	    int[] randNumSat = new int[numTest];
	    double randSumSat = 0;
	    double randSumTime = 0;

	    int[] GSATRunTime = new int[numTest];
	    int[] GSATNumSat = new int[numTest];
	    double GSATSumSat = 0;
	    double GSATSumTime = 0;


	    System.out.println("Running " + numTest + " tests, with " + numC + " clauses and " + numL + " literals.");
	    for(int n = 0; n < numTest; ++n) {
		    	ReturnType SAT = Generate3SAT(numC, numL);
			ArrayList<int[]> clauses = SAT.clause;
			boolean[] literals = SAT.literals;

			timeStart = System.currentTimeMillis();
			ReturnType rtDPLL = DPLL(makeCopy(clauses), literals);
			timeEnd = System.currentTimeMillis();
			time = (int)(timeEnd - timeStart);
			System.out.println("DPLL ran in " + time + " milliseconds");
			DPLLSumTime += time;
			DPLLRunTime[n] = time;

			timeStart = System.currentTimeMillis();
			ReturnType rtGSAT = GSAT(makeCopy(clauses), literals, numFlip);
			timeEnd = System.currentTimeMillis();
			time = (int)(timeEnd - timeStart);
			System.out.println("GSAT ran in " + time + " milliseconds");
			GSATSumTime += time;
			GSATRunTime[n] = time;
			GSATNumSat[n] = rtGSAT.i;

			timeStart = System.currentTimeMillis();
			ReturnType rtRandom = RandomizedMAX3SAT(makeCopy(clauses), literals);
			timeEnd = System.currentTimeMillis();
			time = (int)(timeEnd - timeStart);
			System.out.println("rtRandom ran in " + time + " milliseconds");
			randSumTime += time;
			randRunTime[n] = time;
			randNumSat[n] = rtRandom.i;

			System.out.println("DPLL returned: " + rtDPLL.satisfiable);
			System.out.println("GSAT satisfied " + rtGSAT.i+ " clauses out of " + clauses.size() + " clauses. Flip value was " + numFlip);
			System.out.println("Random satisfied " + rtRandom.i+ " clauses out of " + clauses.size() + " clauses.");

			if(rtDPLL.satisfiable) {
				DPLLSumSat++;
			}
			GSATSumSat+= rtGSAT.i;
			randSumSat += rtRandom.i;
	    }
	    Arrays.sort(DPLLRunTime);

	    Arrays.sort(GSATRunTime);
	    Arrays.sort(GSATNumSat);

	    Arrays.sort(randRunTime);
	    Arrays.sort(randNumSat);

	    System.out.println("\nDPLL on average took " + (DPLLSumTime/numTest) + " milliseconds to run.");
	    System.out.println(DPLLSumSat + " problems were satisfiable out of " + numTest);
	    System.out.println("Longest(max) run time: " + DPLLRunTime[numTest-1]);
	    System.out.println("Median run time: " + DPLLRunTime[numTest/2]);
	    System.out.println("Shortest(min) run time: " + DPLLRunTime[0]);

	    System.out.println("\nGSAT on average took " + (GSATSumTime/numTest) + " milliseconds to run.");
	    System.out.println("On average, GSAT satisfied " + (GSATSumSat/(numTest)) + " of the clauses");
	    System.out.println("Longest(max) run time: " + GSATRunTime[numTest-1]);
	    System.out.println("Median run time: " + GSATRunTime[numTest/2]);
	    System.out.println("Shortest(min) run time: " + GSATRunTime[0]);
	    System.out.println("Max clauses satisfied: " + GSATNumSat[numTest-1]);
	    System.out.println("Median clauses satisfied: " + GSATNumSat[numTest/2]);
	    System.out.println("Min clauses satisfied: " + GSATNumSat[0]);

	    System.out.println("\nRandomMAXSAT on average took " + (randSumTime/numTest) + " milliseconds to run.");
	    System.out.println("On average, Random satisfied " + (randSumSat/(numTest)) + " of the clauses");
	    System.out.println("Longest(max) run time: " + randRunTime[numTest-1]);
	    System.out.println("Median run time: " + randRunTime[numTest/2]);
	    System.out.println("Shortest(min) run time: " + randRunTime[0]);
	    System.out.println("Max clauses satisfied: " + randNumSat[numTest-1]);
	    System.out.println("Median clauses satisfied: " + randNumSat[numTest/2]);
	    System.out.println("Min clauses satisfied: " + randNumSat[0]);
	}

	/**
	* Tests different Algorithms to the 0-1 Knapsack problem, how well they perform and how long they take
	* O(nW) Knapsack, MinCost Knapsack, Greedy Knapsack, and FPTAS Knapsack
	*
	*	@param numTest number of tests
	*	@param numItems number of items per test
	*	@param Capacity knapsack capacity (how much weight you can hold)
	*	@param minW minimum item weight
	*	@param maxW maximum item weight
	*	@param minV minimum item value
	*	@param maxV maximum item value
	*	@param e tolerance for error, within how much of the optimal solution
	**/
	public static void Knapsack(int numTest, int numItems, int Capacity, int minW, int maxW, int minV, int maxV, float e) {

		long timeStart;
	    long timeEnd;
	    int time;

		int[] KnapRunTime = new int[numTest];
		int[] MinCostRunTime = new int[numTest];
		int[] GreedyRunTime = new int[numTest];
		int[] FPTASRunTime = new int[numTest];

	    double KnapSumTime = 0;
	    double MinCostSumTime = 0;
	    double GreedySumTime = 0;
	    double FPTASSumTime = 0;

	    double GreedyOpt = 0;
	    double FPTASOpt = 0;


		for(int i = 0; i < numTest; ++i)
		{
			Item[] items = GenerateKnapsack(numItems,minW,maxW,minV,maxV);

			timeStart = System.currentTimeMillis();
			//ReturnType rtKnap = KnapsackDefault(Capacity, items);
      int rtKnap = KnapsackDefault(Capacity, items);
			timeEnd = System.currentTimeMillis();
			time = (int) (timeEnd-timeStart);
			KnapSumTime+= time;
			KnapRunTime[i] = time;

			timeStart = System.currentTimeMillis();
			ReturnType rtMinCost = MinCostKnapsack(Capacity, items);
			timeEnd = System.currentTimeMillis();
			time = (int) (timeEnd-timeStart);
			MinCostSumTime+= time;
			MinCostRunTime[i] = time;

			timeStart = System.currentTimeMillis();
			ReturnType rtGreedy = ModifiedGreedyKnapsack(Capacity, items);
			timeEnd = System.currentTimeMillis();
			time = (int) (timeEnd-timeStart);
			GreedySumTime+= time;
			GreedyRunTime[i] = time;
			//GreedyOpt += ((double)Math.abs(rtGreedy.i - rtKnap.i)/(double) rtKnap.i) * 100;
      GreedyOpt += ((double)Math.abs(rtGreedy.i - rtKnap)/(double) rtKnap) * 100;

			timeStart = System.currentTimeMillis();
			ReturnType rtFPTAS = FPTAS(Capacity, items, e);
			timeEnd = System.currentTimeMillis();
			time = (int) (timeEnd-timeStart);
			FPTASSumTime+= time;
			FPTASRunTime[i] = time;
			FPTASOpt += ((double)Math.abs(rtFPTAS.i - rtKnap)/(double) rtKnap) * 100;


		}

		Arrays.sort(KnapRunTime);
		Arrays.sort(MinCostRunTime);
		Arrays.sort(GreedyRunTime);
		Arrays.sort(FPTASRunTime);

		System.out.println("--O(nW) Knapsack--");
		System.out.println("Average RunTime: " + KnapSumTime/numTest);
		System.out.println("Worst(Max) RunTime: " + KnapRunTime[numTest-1]);
		System.out.println("Best(Min) RunTime: " + KnapRunTime[0]);
		System.out.println("Median RunTime: " + KnapRunTime[numTest/2]);

		System.out.println("--MinCost Knapsack--");
		System.out.println("Average RunTime: " + MinCostSumTime/numTest);
		System.out.println("Worst(Max) RunTime: " + MinCostRunTime[numTest-1]);
		System.out.println("Best(Min) RunTime: " + MinCostRunTime[0]);
		System.out.println("Median RunTime: " + MinCostRunTime[numTest/2]);

		System.out.println("--Greedy Knapsack--");
		System.out.println("Average RunTime: " + GreedySumTime/numTest);
		System.out.println("Worst(Max) RunTime: " + GreedyRunTime[numTest-1]);
		System.out.println("Best(Min) RunTime: " + GreedyRunTime[0]);
		System.out.println("Median RunTime: " + GreedyRunTime[numTest/2]);
		System.out.println("Percentage within OPT: " + (GreedyOpt/(double)numTest) + "%");

		System.out.println("--FPTAS Knapsack--");
		System.out.println("Average RunTime: " + FPTASSumTime/numTest);
		System.out.println("Worst(Max) RunTime: " + FPTASRunTime[numTest-1]);
		System.out.println("Best(Min) RunTime: " + FPTASRunTime[0]);
		System.out.println("Median RunTime: " + FPTASRunTime[numTest/2]);
		System.out.println("Percentage within OPT: " + (FPTASOpt/(double)numTest) + "%");
	}

	public static void main(String[] args)
	{
		//SAT(numTest, numL, numC)
		SAT(100,200,200,20);

		//Knapsack(int numTest, int numItems, int Capacity, int minW, int maxW, int minV, int maxV, float e)
		Knapsack(100,200,1000,100,1000,100,1000,0.5f);
	}
}
