package creative_cash;

import java.math.BigDecimal;

public class Denomination implements Comparable< Denomination >{
	private String singularName;
	private String pluralName;
	private BigDecimal value;
	
	public Denomination(String singularName, String pluralName, BigDecimal value) {
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.value = value;
	}
	
	public String getSingularName() {
		return singularName;
	}

	public void setSingularName(String singularName) {
		this.singularName = singularName;
	}

	public String getPluralName() {
		return pluralName;
	}


	public void setPluralName(String pluralName) {
		this.pluralName = pluralName;
	}


	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public int compareTo(Denomination d) {
		return this.getValue().compareTo(d.getValue());
	}

}
