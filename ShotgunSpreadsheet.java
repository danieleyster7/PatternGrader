import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ShotgunSpreadsheet 
{
	public static void newSpreadsheet(String serial, String filePath, int choke, String shooter, String view, 
			String ammo, String distance, Pattern userPattern, Settings userSettings)
	{
		//Load template spreadsheet
		try 
		{
			FileInputStream templateFile = new FileInputStream(new File("C:/Template/template.xlsx"));
			XSSFWorkbook templateWB = new XSSFWorkbook(templateFile);
			templateFile.close();
			FileOutputStream outputFile = new FileOutputStream(new File(filePath + "/" + serial + ".xlsx"));
			templateWB.write(outputFile);
			outputFile.close();
		}
		catch (FileNotFoundException f) {
			f.printStackTrace();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		//call update spreadsheet
		String newFilePath = filePath + "/" + serial + ".xlsx";
		ShotgunSpreadsheet.updateSpreadsheet(newFilePath, choke, shooter, view, ammo, distance, userPattern, userSettings);
	}
	
	public static void updateSpreadsheet(String filePath, int choke, String shooter, String view, 
			String ammo, String distance, Pattern userPattern, Settings userSettings)
	{
		//Load spreadsheet
		try
		{
			System.out.println(filePath);
			FileInputStream inputFile = new FileInputStream(new File(filePath));
			XSSFWorkbook inputWB = new XSSFWorkbook(inputFile);
			
			int currentRow = 17;
			
			XSSFSheet chokeSheet = inputWB.getSheetAt(choke);
			Cell activeCell = chokeSheet.getRow(currentRow).getCell(1);
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
			Calendar calendar = Calendar.getInstance();
			
			boolean keepLooking = true;
			
			do
			{
				//If specified cell is empty
				if(activeCell.getStringCellValue().equals(""))
				{
					//date
					activeCell = chokeSheet.getRow(currentRow).getCell(1);
					activeCell.setCellValue(dateFormat.format(calendar.getTime()));
					//shooter
					activeCell = chokeSheet.getRow(currentRow).getCell(2);
					activeCell.setCellValue(shooter);
					//view
					activeCell = chokeSheet.getRow(currentRow).getCell(3);
					activeCell.setCellValue(view);
					//ammo
					activeCell = chokeSheet.getRow(currentRow).getCell(4);
					activeCell.setCellValue(ammo);
					DecimalFormat numOfDec = new DecimalFormat("#.0");
					//POI
					if(userSettings.getPOI())
					{	
						//height
						activeCell = chokeSheet.getRow(currentRow).getCell(7);
						String POIText;
						if(userPattern.getYCenter() < 0)
							POIText = "U: " + numOfDec.format(userPattern.getYCenter() / userPattern.getPixelsPerInch());
						else
							POIText = "O: " + numOfDec.format(userPattern.getYCenter() / userPattern.getPixelsPerInch());
						activeCell.setCellValue(POIText);
					
						//L/R
						activeCell = chokeSheet.getRow(currentRow).getCell(8);
						if(userPattern.getXCenter() < 0)
							POIText = "L: " + numOfDec.format(userPattern.getXCenter() / userPattern.getPixelsPerInch());
						else
							POIText = "R: " + numOfDec.format(userPattern.getXCenter() / userPattern.getPixelsPerInch());
						activeCell.setCellValue(POIText);
					}
					//Distance
					activeCell = chokeSheet.getRow(currentRow).getCell(9);
					activeCell.setCellValue(distance);
					//Coverage
					activeCell = chokeSheet.getRow(currentRow).getCell(10);
					activeCell.setCellValue("Cov");
					//Efficiency
					if(userSettings.getEfficiency())
					{
						numOfDec = new DecimalFormat("#.00");
						activeCell = chokeSheet.getRow(currentRow).getCell(11);
						activeCell.setCellValue(numOfDec.format(userPattern.getEfficiency()));
					}
					//Yield
					if(userSettings.getYield())
					{
						activeCell = chokeSheet.getRow(currentRow).getCell(12);
						activeCell.setCellValue(userPattern.getYield());
					}
					keepLooking = false;
				}
				currentRow++;
				activeCell = chokeSheet.getRow(currentRow).getCell(1);
				System.out.println("loop");
			}
			while(keepLooking);
			
			inputFile.close();
			
			FileOutputStream outputSave = new FileOutputStream(new File(filePath));
			inputWB.write(outputSave);
			outputSave.close();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}
}
