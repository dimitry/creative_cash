package creative_cash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

//Random implementation for processing change. Calculation results in the random amounts of change for a 
//specific owed amount. The owed amount must be given in the config file under "random".
public class RandomChangeProcessor extends BasicChangeProcessor {
	final private String RAND_PROP_NAME = "random_divisor";
	int randomCondition;
	
	public RandomChangeProcessor() {
		super();
	}
	
	private void assignRandomTrigger() {
		String randomStr = super.getConfiguration(RAND_PROP_NAME);
		randomCondition = Integer.parseInt(randomStr);
	}
	
	private boolean meetsRandomCondition(String numString){
		String noDec = numString.replace(".", "");
		int num = Integer.parseInt(noDec);
		if ((num % randomCondition) == 0) {
			return true;
		}
		return false;
		
	}
	
	private int getRandWithinRange(Random r, int min, int max) {
		return r.nextInt(max - min + 1) + min;
	}
	
	private void initializeCurrencyIndexes(ArrayList<Integer> coinIndexes, int currencySize) {
		for (int i = 0; i < currencySize; i++) {
			coinIndexes.add(i);
		}
	}
	
	private void randPrintChangeLine(BufferedWriter writer, String owedString, String payedString) {
		Random r = new Random();
		BigDecimal owed;
		BigDecimal payed;
		BigDecimal change;
		BigDecimal zero = new BigDecimal(0);
		ArrayList<Integer> currencyIndexes = new ArrayList<Integer>();
		
		try {
			owed = getBigDecimalVal(owedString);
			payed = getBigDecimalVal(payedString);
			change = payed.subtract(owed);
			if (change.compareTo(zero) == 1) {
				int currencySize = super.getCurrency().getTotalDenominations();
				initializeCurrencyIndexes(currencyIndexes, currencySize);
				BigDecimal leftOver = change;
				int amount;
				int cur; 
				int randIndex;
				
				for (int i = currencySize - 1; i >= 0; i--) {
					randIndex = getRandWithinRange(r, 0, i);
					cur = currencyIndexes.get(randIndex);
					currencyIndexes.remove(randIndex);
					BigDecimal val = super.getCurrency().getDenominationByRank(cur).getValue();
					amount = super.getCurrency().maxChange(val, leftOver);
					
					if (amount > 0) {
						Denomination denom = super.getCurrency().getDenominationByRank(cur);
						writer.write(amount + " ");
						if (amount > 1) {
							writer.write(denom.getPluralName());
						}
						else {
							writer.write(denom.getSingularName());
						}
						
						leftOver = leftOver.subtract(denom.getValue().multiply(new BigDecimal(amount)));
						if (leftOver.compareTo(zero) == 1) {
							writer.write(",");
						}
					}
					
				}
				writer.write(System.getProperty("line.separator"));
			}
			else {
				writer.write("0\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			errorCalculatingChange();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			errorCalculatingChange();
		}
		
	}
	
	
	@Override
	public void initialize(String paymentFileLocation) {
		super.initialize(paymentFileLocation);
		assignRandomTrigger();
	}
	
	@Override
	public boolean inputIsValid() {
		return super.inputIsValid();
	}
	
	@Override
	public void calculateChange() {
		// TODO Auto-generated method stub
		try {
			FileReader fr = new FileReader(super.getPaymentFileName());
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(super.getOutputFileName());
			BufferedWriter writer = new BufferedWriter(fw);
			String nextLine;
			nextLine = br.readLine();
			String[] splitLine;

			while(nextLine != null && super.inputIsValid()) {
				splitLine = nextLine.split(",");
				if (meetsRandomCondition(splitLine[0])) {
					randPrintChangeLine(writer, splitLine[0], splitLine[1]);
				}
				else {
					super.printChangeLine(writer, splitLine[0], splitLine[1]);
				}
				nextLine = br.readLine();
			}
			br.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			super.errorCalculatingChange();
			e.printStackTrace();
		}
	}
	
}
