package com.SeleniumFramework.test;

import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelUtil {
	private Workbook workbook = null;
	private HashMap<String, HashMap<String, ArrayList<String>>> wbData = new HashMap<String, HashMap<String, ArrayList<String>>>();

	public ExcelUtil(String filePath) throws IOException {
		String fileExtensionName = filePath.substring(filePath.indexOf("."));

		File file = new File(filePath);
		FileInputStream inputStream = new FileInputStream(file);

		if (fileExtensionName.equals(".xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		}

		else if (fileExtensionName.equals(".xls")) {
			workbook = new HSSFWorkbook(inputStream);
		}
		try{
			createWorkBookData();
		}catch(Exception e){
			//throw new ExcelUtilException("Unable to create Workbook data in the form of Hash Map, Please ensure every cell has header associated with it at index 0 and no row is empty in between");
		}
	}

	private void createWorkBookData() {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int sheetCounter = 0; sheetCounter < numberOfSheets; sheetCounter++) {
			Sheet sheet = workbook.getSheetAt(sheetCounter);
			String sheetName = sheet.getSheetName();
			HashMap<String, ArrayList<String>> sheetData = getSheetData(sheet);
			wbData.put(sheetName, sheetData);
		}
	}

	private HashMap<String, ArrayList<String>> getSheetData(Sheet sheet) {
		HashMap<String, ArrayList<String>> sheetData = new HashMap<String, ArrayList<String>>();

		int colCount = sheet.getRow(0).getLastCellNum();
		int rowCount = sheet.getLastRowNum() + 1; // GetLast Row Count returns
													// LastRow Index

		for (int colIndex = 0; colIndex < colCount; colIndex++) {
			String headerName = getCellData(sheet, colIndex, 0);
			ArrayList<String> columnData = new ArrayList<String>();
			for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
				columnData.add(getCellData(sheet, colIndex, rowIndex));
			}
			sheetData.put(headerName, columnData);
		}
		return sheetData;
	}

	private String getCellData(Sheet sheet, int columnIndex, int rowIndex) {
		Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);
		return getCellValue(cell);
	}

	@SuppressWarnings("deprecation")
	private String getCellValue(Cell cell) {
		String returnValue;

		if (cell == null) {
			return "";
		}
		
		switch (cell.getCellType()) {
		case 0:
			if (DateUtil.isCellDateFormatted(cell)) {

                Date date = cell.getDateCellValue(); 
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                returnValue = dateFormat.format(date);
            } else {
            	DecimalFormat df = new DecimalFormat("##########.###");
    			returnValue = df.format(cell.getNumericCellValue());
            }
			break;
		case 1:
			returnValue = cell.getStringCellValue();
			break;
		case 2:
			if(cell.getCellFormula().contains("TODAY")){
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	            returnValue = dateFormat.format(cell.getDateCellValue());
			}else{
				returnValue = cell.getCellFormula();
			}
			break;
		case 3:
			returnValue = cell.getStringCellValue();
			break;
		case 4:
			returnValue = cell.getBooleanCellValue() ? "true" : "false";
			break;
		default:
			returnValue = "";
		}
		return returnValue;
	}

	public HashMap<String, HashMap<String, ArrayList<String>>> getWorkBookData() {
		return this.wbData;
	}

	public HashMap<String, ArrayList<String>> getWorkSheetData(String sheetName) {
		return getWorkBookData().get(sheetName);
	}

	public ArrayList<String> getColumnData(String sheetName, String headerName) {
		return getWorkBookData().get(sheetName).get(headerName);
	}

	// Row no is Excel Sheet row number starting with 1
	public String getCellData(String sheetName, String headerName, int rowNo) {
		return getWorkBookData().get(sheetName).get(headerName).get(rowNo - 2);
	}
	
	// Row no is Excel Sheet row number starting with 1
	public ArrayList<String> getRowData(String sheetName, int rowNo) {
		ArrayList<String> rowData = new ArrayList<String>();
		HashMap<String, ArrayList<String>> sheetData = getWorkBookData().get(sheetName);
		
		Set<String> headerNames = sheetData.keySet();
		for (String headerName : headerNames) {
			String cellData = sheetData.get(headerName).get(rowNo-2);
			rowData.add(cellData);
		}
		return rowData;
	}
	
	
	public static void storeResult(String testCaseName, 
			String envName,
			String portal,
			String password
			) throws IOException{

		File file =    new File("Optumation\\Reports\\TestResult.xlsx");
		
		FileInputStream inputStream = new FileInputStream(file);
		Workbook wb = new XSSFWorkbook(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		
		Row newRow = sheet.createRow(rowCount+1);
		Cell cell0 = newRow.createCell(0);
		cell0.setCellValue(testCaseName);
		Cell cell1 = newRow.createCell(1);
		cell1.setCellValue(envName);
		Cell cell2 = newRow.createCell(2);
		cell2.setCellValue(portal);
		Cell cell4 = newRow.createCell(4);
        cell4.setCellValue(password);
		
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(file);
		wb.write(outputStream);
		outputStream.close();
		wb.close();
		}
	
	public static void storeResult(String testCaseName, 
			String envName,
			String portal,
			int columnNo,
			String value
			) throws IOException{

		File file =    new File("Optumation\\Reports\\TestResult.xlsx");
		
		FileInputStream inputStream = new FileInputStream(file);
		Workbook wb = new XSSFWorkbook(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		
		Row newRow = sheet.getRow(rowCount);
		Cell cell = newRow.createCell(columnNo);
		cell.setCellValue(value);
		
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(file);
		wb.write(outputStream);
		outputStream.close();
		wb.close();
		}
	
	public static void storeSelkeySteps(HashMap<String,String> valueMapper) throws IOException{
		
		String applicationName = valueMapper.get("SelkeyAppName");
		String selkeyManualTestStepId = valueMapper.get("SelkeyManualTestStepId");
		String testCaseName = valueMapper.get("TestCaseName");
		String inputVariable = valueMapper.get("InputVariable");
		String outputVariable = valueMapper.get("OutputVariable");
		String selkey = valueMapper.get("Selkey");
		String screenName = valueMapper.get("ScreenName");
		String webElementName = valueMapper.get("WebElementName");
		String dataHeader = valueMapper.get("DataHeader");
		String fieldValue = valueMapper.get("FieldValue");
		String selkeyFileName = valueMapper.get("SelkeyFileName");
		File destFile=new File("SeleniumFramework\\Test_Modules\\" + selkeyFileName+".xls");
		
		/*if(selkeyManualTestStepId.equals("1")){
	        
			File srcFile=new File("Optumation\\Sample\\SampleSelkeySteps.xls");
			FileManipulation.moveFile(srcFile, destFile);
		}*/
		
		FileInputStream inputStream = new FileInputStream(destFile);
        Workbook wb = new HSSFWorkbook(inputStream);
        
			
        if (! selkey.equalsIgnoreCase("")){
        	
        	Sheet sheet = wb.getSheetAt(0);
 	        
 	        int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
 	        Row newRow = sheet.createRow(rowCount+1);
 	        
        	if (selkey.equalsIgnoreCase("END_OF_TEST")){
     	        Cell cell0 = newRow.createCell(0);
     	        cell0.setCellValue("END_OF_TEST");
            	
            }else{
	        
		        Cell cell0 = newRow.createCell(0);
		        cell0.setCellValue(applicationName);
		        Cell cell1 = newRow.createCell(1);
		        cell1.setCellValue(selkeyManualTestStepId);
		        Cell cell2 = newRow.createCell(2);
		        cell2.setCellValue(testCaseName);
		        Cell cell3 = newRow.createCell(3);
		        cell3.setCellValue(screenName);
		        Cell cell4 = newRow.createCell(4);
		        cell4.setCellValue(selkey);
		        Cell cell6 = newRow.createCell(6);
		        cell6.setCellValue(webElementName);
		        Cell cell7 = newRow.createCell(7);
		        cell7.setCellValue(fieldValue);
		        
		        if(inputVariable.equals("AccountHolder_FullName") 
		        || inputVariable.equals("ProfileId")
		        || inputVariable.equals("EligibilityId")
		        ){
		        	Cell cell5 = newRow.createCell(5);
			        cell5.setCellValue("APP_DATA");
			        cell7.setCellValue(inputVariable);
		        }
		        
		        if(		outputVariable.equals("AccountHolder_FullName") 
				        || outputVariable.equals("EligibilityId")
				        || outputVariable.equals("ProfileId")
				        ){
				        	Cell cell5 = newRow.createCell(5);
					        cell5.setCellValue("APP_DATA");
					        cell7.setCellValue(outputVariable);
				        }
		        
		        
		        int intSelkeyManualTestStepId = Integer.parseInt(selkeyManualTestStepId);
		        intSelkeyManualTestStepId++;
		        valueMapper.put("SelkeyManualTestStepId", intSelkeyManualTestStepId+"");
           }
		}
		
		inputStream.close();
        FileOutputStream outputStream = new FileOutputStream(destFile);
        wb.write(outputStream);
        outputStream.close();
        wb.close();
        
	}

	private static void insert_MMIS_DataToAlphaSheet(String dataHeader, String dataHeaderValue, String fieldValue, Workbook wb, int colNo) {
		if(dataHeader.equalsIgnoreCase(dataHeaderValue)){
	        //Update In Sheet Alpha  
	        
	        Sheet sheet = wb.getSheet("Test_ALPHA");
	        Row row = sheet.getRow(3);
	        
	        Cell cell0 = row.getCell(colNo);
	        if (cell0==null){
	        	cell0 = row.createCell(colNo);
	        }
	        cell0.setCellValue(fieldValue);
		}
	}
	
	public static void markTestCasesToBeExecutedInPhase(String phaseName){
		try {
			
		ExcelUtil testSuiteWorkBook = new ExcelUtil("SeleniumFramework" + File.separator + "Test_Suite" + File.separator + "Test_Suite.xls");
		HashMap<String, ArrayList<String>> testSuite = testSuiteWorkBook.getWorkBookData().get("ModuleList");
		
		ArrayList<String> testModules	= testSuite.get("TestModule Name");
		ArrayList<String> exeStatus	= testSuite.get("Execute");
		
		for(int scenarioCounter =0; scenarioCounter <  testModules.size()-1;scenarioCounter++ ){
			
			if(exeStatus.get(scenarioCounter).equalsIgnoreCase("yes")){
				
				File destFile=new File("SeleniumFramework" + File.separator + "Test_Modules"+ File.separator + testModules.get(scenarioCounter) + ".xls" );
				
				FileInputStream inputStream = new FileInputStream(destFile);
		        Workbook wb = new HSSFWorkbook(inputStream);
				
		        Sheet sheet = wb.getSheet("TestCases");
		        Row row=null;
		        Row rowToBeExecuted=null;
		        Cell cell=null;
		        
		        for (int rowCounter=1; rowCounter<15; rowCounter++){
		        	row = sheet.getRow(rowCounter);
		        	cell = row.getCell(0);
		        	if(cell.getStringCellValue().equalsIgnoreCase("end")){
		        		break;
		        	}
			        cell.setCellValue("N");
			        
			        cell = row.getCell(10);
			        if(cell.getStringCellValue().equalsIgnoreCase(phaseName)){
			        	rowToBeExecuted = sheet.getRow(rowCounter);
					cell = rowToBeExecuted.getCell(0);
				        cell.setCellValue("Y");
		        	}
			        
		        }
		
				inputStream.close();
		        FileOutputStream outputStream = new FileOutputStream(destFile);
		        wb.write(outputStream);
		        outputStream.close();
		        wb.close();
			}
		  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

public static void storeReportStepWise(HashMap<String,String> valueMapper) throws IOException{
		
		String testCaseName = valueMapper.get("TestCaseName");
		String testStepNo = valueMapper.get("TestStepNo");
		String sheetToExecute = valueMapper.get("SheetToExecute");
		String eventRowNoInExcel = valueMapper.get("EventRowNoInExcel");
		String eventName = valueMapper.get("EventName");
		String screenName = valueMapper.get("ScreenName");
		String webElementName = valueMapper.get("WebElementName");
		String fieldValue = valueMapper.get("FieldValue");
		
		File file =    new File("Reports\\ReportStepsWise.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        Workbook wb = new XSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheetAt(0);
        
        int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
        
        Row newRow = sheet.createRow(rowCount+1);
        Cell cell0 = newRow.createCell(0);
        cell0.setCellValue(testCaseName);
        Cell cell1 = newRow.createCell(1);
        cell1.setCellValue(testStepNo);
        Cell cell2 = newRow.createCell(2);
        cell2.setCellValue(sheetToExecute);
        Cell cell3 = newRow.createCell(3);
        cell3.setCellValue(eventRowNoInExcel);
        Cell cell4 = newRow.createCell(4);
        cell4.setCellValue(eventName);
        Cell cell5 = newRow.createCell(5);
        cell5.setCellValue(screenName);
        Cell cell6 = newRow.createCell(6);
        cell6.setCellValue(webElementName);
        Cell cell7 = newRow.createCell(7);
        cell7.setCellValue(fieldValue);
        
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream(file);
        wb.write(outputStream);
        outputStream.close();
        wb.close();
        
        int intTestStepNo = Integer.parseInt(testStepNo);
        intTestStepNo++;
        valueMapper.put("TestStepNo", intTestStepNo+"");
        
	}
	
}
