package creative_cash;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ChangeProcessor changeProcessor = new RandomChangeProcessor();
		changeProcessor.initialize(args[0]);
		if (!(changeProcessor.inputIsValid())) {
			System.exit(1);
		}
		changeProcessor.calculateChange();
	}

}
