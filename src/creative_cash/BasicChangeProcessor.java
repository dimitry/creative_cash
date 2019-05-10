package creative_cash;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.Scanner;

//Normal implementation for processing change. Calculation results in the least amount of change possible.
public class BasicChangeProcessor implements ChangeProcessor {
	private final String CONFIG_FILE_NAME = "config.txt";
	private final String OUTPUT_FILE_NAME = "change_to_give_back.txt";
	
	private Boolean inputIsValid;
	private StringBuilder errorLog;
	private Currency currency;
	private String paymentFileName;
	private String currencyFileName;
	private Properties config;
	
	public BasicChangeProcessor() {
		errorLog = new StringBuilder("Error: " + System.getProperty("line.separator"));
		inputIsValid = true;
		currency = new Currency();
	}
	
	public Boolean getInputIsValid() {
		return inputIsValid;
	}
	
	public StringBuilder getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(StringBuilder errorLog) {
		this.errorLog = errorLog;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getPaymentFileName() {
		return paymentFileName;
	}

	public void setPaymentFileName(String paymentFileName) {
		this.paymentFileName = paymentFileName;
	}

	public String getCurrencyFileName() {
		return currencyFileName;
	}

	public void setCurrencyFileName(String currencyFileName) {
		this.currencyFileName = currencyFileName;
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public String getConfigFileName() {
		return CONFIG_FILE_NAME;
	}

	public String getOutputFileName() {
		return OUTPUT_FILE_NAME;
	}

	public void setInputIsValid(Boolean inputIsValid) {
		this.inputIsValid = inputIsValid;
	}
	
	//get name of currency file from config file in working directory
	private void readInConfiguration() {
		config = new Properties();
		try {
			InputStream inputStream = new FileInputStream(CONFIG_FILE_NAME);
			config.load(inputStream);
		}
		catch(FileNotFoundException e) {
			inputIsValid = false;
			errorLog.append(CONFIG_FILE_NAME + " is not in working directory" + System.getProperty("line.separator"));
		}
		catch(IOException e) {
			inputIsValid = false;
			errorLog.append(CONFIG_FILE_NAME + " is not properly formatted" + System.getProperty("line.separator"));
		}
		assignConfiguration("currency");
	}
	
	protected String getConfiguration(String prop) {
		System.out.println(prop);
		System.out.println(config.getProperty(prop));
		return config.getProperty(prop);
		
	}
	
	private void assignConfiguration(String prop) {
		
		this.currencyFileName = getConfiguration(prop) + ".txt";
		
	}
	
	//read denominations from currency file in working directory
	private void readInDenominations() {
		File denominationsFile = new File(currencyFileName);
		try {
			String dLine;
			int lineCount = 1;
			Scanner dScanner = new Scanner(denominationsFile);
			
			while(dScanner.hasNextLine()) {
				dLine = dScanner.nextLine();
				String[] denomNameAndVal = dLine.split("=");
				String singularName = denomNameAndVal[0];
				String pluralName = dScanner.nextLine();
				
				try {
					Double value = Double.parseDouble(denomNameAndVal[1]);
					BigDecimal bdValue = BigDecimal.valueOf(value);
					Denomination denom = new Denomination(singularName, pluralName, bdValue);
					currency.addDenomination(denom);
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
					errorAtLine(currencyFileName, lineCount);
					dScanner.close();
				}
				lineCount++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			inputIsValid = false;
			errorLog.append("Denominations file " + currencyFileName + " in working directory " + System.getProperty("line.separator"));
		}
	}
	
	private void errorAtLine(String fileName, int lineNum) {
		inputIsValid = false;
		errorLog.append("Line " + lineNum + " of " + fileName + " is not properly"
				+ " formatted" + System.getProperty("line.separator"));
	}
	
	private Boolean isValidNum(String stringNum) {
		try {
			new BigDecimal(stringNum);
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void checkPaymentFile(String paymentFileName) {
		try {
			FileReader fr = new FileReader(paymentFileName);
			BufferedReader br = new BufferedReader(fr);
			try {
				int lineCount = 1;
				String nextLine;
				nextLine = br.readLine();
				String[] splitLine;
				while(nextLine != null) {
					if ((!(nextLine.contains(",")) && !(nextLine.isEmpty()))) {
						inputIsValid = false;
						errorAtLine(paymentFileName, lineCount);
					}
					splitLine = nextLine.split(",");
					for (String numString : splitLine) {
						if (!isValidNum(numString)){
							inputIsValid = false;
						}
					}
					nextLine = br.readLine();
					lineCount++;
				}
				br.close();
			} catch (IOException e) {
				inputIsValid = false;
				errorLog.append(paymentFileName + " is improperly formatted " + System.getProperty("line.separator"));
				e.printStackTrace();
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
			inputIsValid = false;
			errorLog.append(paymentFileName + " could not be found " + System.getProperty("line.separator"));
		}
		
	}
	
	
	@Override
	public void initialize(String paymentFileName) {
		readInConfiguration();
		readInDenominations();
		this.paymentFileName = paymentFileName;
	}

	@Override
	public boolean inputIsValid() {
		checkPaymentFile(paymentFileName);
		if (inputIsValid == false) {
			System.out.println(errorLog.toString());
			try {
				FileWriter fw = new FileWriter("error_log.txt");
				BufferedWriter writer = new BufferedWriter(fw);
			    writer.write(errorLog.toString());
			    writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		else {
			return true;
		}
	}
	
	protected BigDecimal getBigDecimalVal(String strVal) throws NumberFormatException{
		BigDecimal bd = new BigDecimal(strVal);
		return bd;
	}
	
	protected void errorCalculatingChange() {
		inputIsValid = false;
		errorLog.append("Error processing change file");
	}
	
	protected void printChangeLine(BufferedWriter writer, String owedString, String payedString) {
		BigDecimal owed;
		BigDecimal payed;
		BigDecimal change;
		BigDecimal zero = new BigDecimal(0);
		try {
			owed = getBigDecimalVal(owedString);
			payed = getBigDecimalVal(payedString);
			change = payed.subtract(owed);
			if (change.compareTo(zero) == 1) {
				int currencySize = currency.getTotalDenominations();
				BigDecimal leftOver = change;
				int amount;
				
				//from largest to smallest denomination, calculate the change, and write to the file
				for (int i = currencySize - 1; i >= 0; i--) {
					amount = currency.maxChange(currency.getDenominationByRank(i).getValue(), leftOver);

					if (amount > 0) {
						Denomination denom = currency.getDenominationByRank(i);
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
	public void calculateChange() {
		try {
			FileReader fr = new FileReader(paymentFileName);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(OUTPUT_FILE_NAME);
			BufferedWriter writer = new BufferedWriter(fw);
			String nextLine;
			nextLine = br.readLine();
			String[] splitLine;
			
			while(nextLine != null && inputIsValid) {
				splitLine = nextLine.split(",");
				printChangeLine(writer, splitLine[0], splitLine[1]);
				nextLine = br.readLine();
			}
			br.close();
			writer.close();
		} catch (IOException e) {
			errorCalculatingChange();
			e.printStackTrace();
		}
}
		

}
