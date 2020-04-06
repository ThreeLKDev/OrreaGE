package threelkdev.orreaGE.tools.utils;

public class Pair<T1,T2> {
	
	public T1 key;
	public T2 value;
	
	public Pair(T1 key, T2 value) {
		this.key = key;
		this.value = value;
	}
	
	public Pair() {}
	
	public void set(T1 key, T2 value) { this.key = key; this.value = value; }
	public void set(Pair<T1, T2> other) { this.key = other.key; this.value = other.value; }
}