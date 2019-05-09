package creative_cash_draw_solutions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

public class BasicChangeProcessor implements ChangeProcessor {
	private final String CONFIG_FILE_NAME = "config.txt";
	
	private Boolean inputIsValid;
	private StringBuilder errorLog;
	private ArrayList<Denomination> denominationsList;
	private String paymentFileName;
	private String countryFileName;
	
	public BasicChangeProcessor() {
		errorLog = new StringBuilder("Error loading file(s): ");
		inputIsValid = true;
		denominationsList = new ArrayList<Denomination>();
		
	}
	
	private void readInConfiguration() {
		Properties config = new Properties();
		try {
			InputStream inputStream = new FileInputStream(CONFIG_FILE_NAME);
			config.load(inputStream);
		}
		catch(FileNotFoundException e) {
			inputIsValid = false;
			errorLog.append(CONFIG_FILE_NAME + " is not in working directory" + "\n");
		}
		catch(IOException e) {
			inputIsValid = false;
			errorLog.append(CONFIG_FILE_NAME + " is not properly formatted" + "\n");
		}
		assignConfigurations(config);
	}
	
	private void assignConfigurations(Properties config) {
		this.countryFileName = config.getProperty("country") + ".txt";
	}
	
	private void readInDenominations() {
		File denominationsFile = new File(countryFileName);
		try {
			Scanner dScanner = new Scanner(denominationsFile);
			String dLine;
			int lineCount = 1;
			while(dScanner.hasNextLine()) {
				dLine = dScanner.nextLine();
				String[] denomNameAndVal = dLine.split("=");
				String dName = denomNameAndVal[0];
				try {
					Double dValue = Double.parseDouble(denomNameAndVal[1]);
					Denomination denom = new Denomination(dName, dValue);
					denominationsList.add(denom);
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
					inputIsValid = false;
					errorLog.append("Line " + lineCount + " of " + countryFileName + " is not properly"
							+ " formatted" + "\n");
					dScanner.close();
				}
				lineCount++;
			}
			Collections.sort(denominationsList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			inputIsValid = false;
			errorLog.append("Denominations file " + countryFileName + " in working directory" + countryFileName + " is not properly"
					+ " formatted" + "\n");
		}
	}
	
	@Override
	public void initialize(String paymentFileName) {
		// TODO Auto-generated method stub
		this.paymentFileName = paymentFileName;
		readInConfiguration();
		readInDenominations();
	}

	@Override
	public void inputIsValid() {
		// TODO Auto-generated method stub
		System.out.println(errorLog.toString());
		
	}
	
	@Override
	public void calculateChange() {
		// TODO Auto-generated method stub
		Denomination.assignIntValues(denominationsList);
		for (Denomination d : denominationsList) {
			System.out.println(d.getIntValue());
		}
		
	}

}
