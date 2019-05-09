package creative_cash_draw_solutions;

public interface ChangeProcessor {
	public void initialize(String paymentFileName);
	public void inputIsValid();
	public void calculateChange();
}
