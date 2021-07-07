import java.util.ArrayList;

public class ReturnType {

	int i;
	ArrayList<int[]> clause;
	boolean satisfiable;
	int[][] a;
	boolean[] literals;
	boolean[][] b;
	Item[] items;

	public ReturnType(ArrayList<int[]> a, boolean[] b) {
		clause = a;
		literals = b;
	}
	public ReturnType(ArrayList<int[]> a, boolean[][] b) {
		clause = a;
		this.b = b;
	}
	public ReturnType(int[][]a, boolean[][]b) {
		this.a = a;
		this.b = b;
	}
	public ReturnType(int i, boolean[] l) {
		this.i = i;
		literals = l;
	}
	public ReturnType(boolean c, boolean[][] l) {
		satisfiable = c;
		b = l;
	}
	public ReturnType(int j, ArrayList<Item> items) {
		i = j;
		this.items = new Item[items.size()];
		for(int k = 0; k < items.size(); ++k)
		{
			this.items[k] = items.get(k);
		}
	}
	public ReturnType(int j, int[][] a, boolean[][] take) {
		i = j;
		this.a = a;
		b = take;
	}
}
