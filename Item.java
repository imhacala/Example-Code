
public class Item implements Comparable{
	int w;
	int v;
	
	public Item(int w, int v) {
		this.w = w;
		this.v = v;
	}

	@Override
	public int compareTo(Object o) {
		Item i = (Item) o;
		return Float.compare(((float)v/(float)w),((float)i.v/(float)i.w));
	}
}
