package test;
// Author :: Pallab Kumar Mali
import org.testng.annotations.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import java.util.ListIterator;

public class NewTest {
	ChromeDriver driver;
	XSSFSheet dataSheet=null;
	String dir=System.getProperty("user.dir");
  @Test
  public void CompareTable() {
	  //ReadExcelData();
	  //System.out.println("Temp row size:"+tempRow.length);
	  List<WebElement> th=driver.findElementsByXPath("//table[@id='webtable']//th"); //Get Cols count
	  List<WebElement> tr=driver.findElementsByXPath("//table[@id='webtable']//tr"); // Get Rows count include header row
	  System.out.println("Rows:"+th.size()+" ; Cols:"+tr.size());
		  for(int i=1;i<=th.size();i++) { // Loop start for table column excluding header row
			  System.out.println("Data for Column: "+i);
			  List<WebElement> td=driver.findElementsByXPath("//table[@id='webtable']//tr//td["+i+"]");
			  ListIterator<WebElement> li=td.listIterator();
			  String[] tempRow=getColumnData(i);
			  int j=0;
			  System.out.println("Assert check for size");
			  Assert.assertEquals(td.size(), tempRow.length);//(td.size()==tempRow.length, "Verify size of column:"+i);
			  while(li.hasNext()) {
				  //System.out.println("Table Value:");
				  System.out.println("temp row data:"+tempRow[j]);
				  String webValue=li.next().getText();
				  String excelValue=tempRow[j].toString();
				  if(isNumeric(webValue)) {
					  double webNum=Double.parseDouble(webValue);
					  double excelNum=Double.parseDouble(webValue);
					  System.out.println(webNum+";"+excelNum);
				  }
				  else {
					  Assert.assertEquals(webValue, excelValue);
				  }
				  
				  j++;
			  }
		  }
	  
	  
  }
  @BeforeTest
  public void beforeTest() {
	  System.out.println(dir);
	   FileInputStream file;
	try {
		file = new FileInputStream(new File("Resource\\Data.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		 dataSheet = workbook.getSheetAt(0);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  System.setProperty("webdriver.chrome.driver", "Resource\\chromedriver.exe");
	  driver=new ChromeDriver();
	  driver.get("file:///"+dir+"/Resource/table.html");
	  
  }

  @AfterTest
  public void afterTest() {
	  driver.close();
  }
  
  public void ReadExcelData() {
	  try {
		  //String data[];
		  int rowCount=dataSheet.getLastRowNum();
		  int colCount=dataSheet.getRow(0).getLastCellNum();
		  System.out.println("Excel row count:"+rowCount+" ; Col count:"+colCount);
		  for(int i=0;i<=colCount;i++) {
			  System.out.println("Values for row:"+i);
			  for(int j=1;j<=rowCount;j++) {
				  System.out.println(dataSheet.getRow(j).getCell(i));
			  }
		  }
	  }catch(Exception e) {
		  System.out.println(e);
	  }
  }
  public String[] getColumnData(int columnNumber) {
	  String data[];
	  try {
		  
		  int rowCount=dataSheet.getLastRowNum();
		  int colCount=dataSheet.getRow(0).getLastCellNum();
		  System.out.println("Excel row count:"+rowCount+" ; Col count:"+colCount);
		  data=new String[rowCount];
		  for(int i=1;i<=rowCount;i++) {
			  Cell cell=dataSheet.getRow(i).getCell(columnNumber-1);
			  System.out.println("Cell type:"+cell.getCellType());
			  data[i-1]=dataSheet.getRow(i).getCell(columnNumber-1).toString();
		  }
		  //System.out.println(data.toString());
		  return data;
	  }catch(Exception e) {
		  System.out.println(e);
		  return null;
	  }
	  
  }
  public boolean isNumeric(String txt) {
	  try {
		  double num=Double.parseDouble(txt);
		  return true;
	  }
	  catch(Exception e) {
		  return false;
	  }
  }

}
