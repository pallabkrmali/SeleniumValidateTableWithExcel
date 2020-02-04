package test;
// Author :: Pallab Kumar Mali
import org.testng.annotations.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
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
			  System.out.println("Check A Sorted for col:"+i+" is ="+CheckShorted('A',td));
			  System.out.println("Check D Sorted for col:"+i+" is ="+CheckShorted('D',td));
			  System.out.println("Check Numeric A Sorted for col:"+i+" is ="+checkNumericSorted('A',td));
			  System.out.println("Check Numeric D Sorted for col:"+i+" is ="+checkNumericSorted('D',td));
			  //checkNumericSorted
			  ListIterator<WebElement> li=td.listIterator();
			  String[] tempRow=getColumnData(i);
			  //Arrays.sort(tempRow);
			  //System.out.println("Sorted Array:"+Arrays.toString(tempRow));
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
    
  public boolean CheckShorted(char type,List<WebElement> elmList) { //type A for ascending D for descending
	  ListIterator<WebElement> li=elmList.listIterator();
	  String s1=elmList.get(0).getText(); //Initially hold 1st Elemet text
	  String s2=elmList.get(elmList.size()-1).getText(); // Initially hold Last elemet text
	  //System.out.println("First:"+s1+"; Last:"+s2);
	  while(li.hasNext()) {
		  if(type=='A') {
			  s2=li.next().getText();
			  //System.out.println(s1+";"+s2+"Compare Value:"+s1.compareTo(s2));
			  if(s1.compareTo(s2)>0) {
				  System.out.println("Ascending order fail for:"+s1+" , "+s2);
				  return false;
			  }
			  s1=s2;
		  }
		  else if(type=='D') {
			  //System.out.println(s1+";"+s2);
			  
			  s2=li.next().getText();
			  if(s1.compareTo(s2)<0) {
				  System.out.println("Descending order fail for:"+s1+" , "+s2);
				  return false;
			  }
			  s1=s2;
		  }
		  else {
			  System.out.println("Order type not match");
			  return false;
		  }
		  
	  }
	  return false;
  }
  
  public boolean checkNumericSorted(char type,List<WebElement> elmList) {
	  ListIterator<WebElement> li=elmList.listIterator();
	  String webVal;
	  double[] doubleArr=new double[elmList.size()];
	  double[] sortedArr=new double[elmList.size()];
	  int counter=0;
	  while(li.hasNext()) {
		  webVal=li.next().getText();
		  if(isNumeric(webVal)) {
			  doubleArr[counter]=Double.parseDouble(webVal);
			  sortedArr[counter]=Double.parseDouble(webVal);
			  counter++;
		  }else {
			  System.out.println(webVal+" is not a number");
			  return false;
		  }
	  }
	  System.out.println(Arrays.toString(doubleArr));
	  //=doubleArr;
	  Arrays.sort(sortedArr);
	  System.out.println("Sorted number array:"+Arrays.toString(sortedArr));
	  if(type=='A') {
		  for(int i=0;i<doubleArr.length;i++) {
			  System.out.println(doubleArr[i]+" ; "+sortedArr[i]);
			  if(doubleArr[i]!=sortedArr[i]) {
				  System.out.println("ascending number order fails for"+doubleArr[i]+" ; "+sortedArr[i]);
				  return false;
			  }
		  }
	  }
	  else if(type=='D') {
		  int j=doubleArr.length-1;
		  for(int i=0;i<doubleArr.length;i++) {
			  if(doubleArr[i]!=sortedArr[j]) {
				  System.out.println("ascending number order fails for"+doubleArr[i]+" ; "+sortedArr[i]);
				  return false;
			  }
			  j--;
		  }
	  }
	  return true;
  }

}
