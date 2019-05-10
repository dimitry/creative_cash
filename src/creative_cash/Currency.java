package creative_cash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class Currency {
	private ArrayList<Denomination> denominations;
	
	public Currency() {
		denominations = new ArrayList<Denomination>();
	}
	
	public Denomination getDenominationByRank(int rank) {
		if (rank <= denominations.size()) {
			return denominations.get(rank);
		}
		else {
			return null;
		}
	}
	
	public void addDenomination(Denomination denomination) {
		denominations.add(denomination);
		Collections.sort(denominations);
	}
	
	public int getTotalDenominations() {
		return denominations.size();
	}
	
	public void setDenominations(ArrayList<Denomination> denominations) {
		Collections.sort(denominations);
		this.denominations = denominations;
	}
	
	public int maxChange(BigDecimal denominationValue, BigDecimal value) {
		BigDecimal result = value.divide(denominationValue);
		int maxChange = result.intValue();
		return maxChange;
	}
	
}
