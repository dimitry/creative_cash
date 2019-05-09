package creative_cash_draw_solutions;

import java.util.ArrayList;
import java.util.Collections;

public class Denomination implements Comparable< Denomination >{
	private String name;
	private double value;
	private int intValue;
	private int multiplier;
	
	public Denomination(String name, Double value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public void setIntValue(int multiplier) {
		intValue = (int) (multiplier * value);
		this.multiplier = multiplier;
	}

	public int getIntValue() {
		return intValue;
	}
	
	@Override
	public int compareTo(Denomination d) {
		return Double.compare(this.getValue(), d.getValue());
	}
	
	private static int getMultiplier(ArrayList<Denomination> denominations) {
		Collections.sort(denominations);
		double smallest = denominations.get(0).getValue();
		int multiplier = (int) (1/smallest);
		return multiplier;
	}
	
	public static void assignIntValues(ArrayList<Denomination> denominations) {
		int multiplier = getMultiplier(denominations);
		for (Denomination d : denominations) {
			d.setIntValue(multiplier);
		}
	}

}
