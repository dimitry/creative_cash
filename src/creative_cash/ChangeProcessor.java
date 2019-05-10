package creative_cash;

public interface ChangeProcessor {
	public void initialize(String paymentFileName);
	public boolean inputIsValid();
	public void calculateChange();
}
