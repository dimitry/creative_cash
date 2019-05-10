package creative_cash;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ChangeProcessor changeProcessor = new RandomChangeProcessor();
		System.out.println(args[0]);
		changeProcessor.initialize(args[0]);
		System.out.println("t");
		if (!(changeProcessor.inputIsValid())) {
			System.exit(1);
		}
		changeProcessor.calculateChange();
	}

}
