package threelkdev.orreaGE.tools.math;

import java.util.LinkedList;

public class RollingAverage {
	
	private final int max;
	private int count = 0;
	private final LinkedList<Float> values = new LinkedList<Float>();

	public RollingAverage(int count) {
		this.max = count;
	}

	public void addValue(float value) {
		if (count >= max) {
			values.removeFirst();
		} else {
			count++;
		}
		values.addLast(value);
	}

	public float calculate() {
		float total = 0;
		for (Float f : values) {
			total += f;
		}
		return total / count;
	}
	
}
