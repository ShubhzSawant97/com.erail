package com.erail.pages;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.erail.utils.commonActions;

public class erailHomepage {
	// Class Level Variables

	protected WebDriver driver;
	protected PageFactory pagefactory;
	protected commonActions ca;
	protected ExtentTest logger;
	
	// Constructor
	// Initializes PageFactory and commonActions

	public erailHomepage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		ca = new commonActions(driver);
		
	}

	// Inject Extent logger into both PageObject & commonActions
	public void setLogger(ExtentTest logger) {
		this.logger = logger;
		ca.setLogger(logger);
	}

	// Page Elements (PageFactory)

	// Input field: FROM station
	@FindBy(xpath = "//input[@id = 'txtStationFrom']")
	private WebElement FromInputFild;

	// Dropdown options that appear after entering station name
	private By valueSelectionFromDropdown = By.xpath("//div[contains(@class,'autocomplete')]/div[@title]");

	// Datepicker input element (opens calendar)
	@FindBy(xpath = "//input[@title = 'Select Departure date for availability']")
	private WebElement Calender;
	
	@FindBy(xpath = "//input[@id='txtStationTo']")
	private WebElement TextStationTo;
	
	private By ToStationSelection = By.xpath("//div[@class='autocomplete' and not(contains(@style,'display: none'))]//div[@title]");


	/**
	 * Enters station name into "From" textbox and selects a value by index.
	 * 
	 * @param StationName - text to type
	 * @param index       - dropdown option index
	 * @return selected value from dropdown
	 */
	public String enterStationName(String StationName, int index){
		ca.enterValue(FromInputFild, StationName, "Entered value: " + StationName + " Successfully");
		return ca.selectValueByIndex(valueSelectionFromDropdown, index, "Value Selected ");
	}

	/**
	 * Reads values from Excel sheet (column 1).
	 */
	public void readExcel(int num) {
		ca.readExcelData("Values Read: ", num);
	}

	/**
	 * Reads dropdown values and writes them into Excel column 0.
	 */
	public void readDropdownValues(int cellnum, int rownum) {
		List<String> uiValues = ca.getDropdownValues(valueSelectionFromDropdown, "Values read from Dropdown: ");
		ca.writeTExcel(uiValues,cellnum,rownum);
	}

	/**
	 * Compares values from Excel vs dropdown and logs PASS/FAIL.
	 */
	public void compareValues() {
		ca.compareExcelAndDropdown(valueSelectionFromDropdown, "values are compared");
	}

	/**
	 * Enters station name into "From" textbox 	 */
	public void EnterValueinStationTo(String StationName) {
		ca.enterValue(TextStationTo, StationName,"Entered value: " + StationName + " Successfully");
	}
	
	/**
	selects a value by index. & wrote values in excel
	 * 
	 * @param StationName - text to type
	 * @param index       - dropdown option index
	 * @return selected value from dropdown
	*/
	public String SelectStationTo(int index,int cellnum, int rownum) {
		List <String> val = ca.getDropdownValues(ToStationSelection, "Values read from Dropdown:");
		System.out.println(val);
		ca.writeTExcel(val, cellnum, rownum);
		return ca.selectValueByIndex(ToStationSelection, index, "value selected");
	}
	

	/**
	 * Opens calendar and selects a date dynamically based on given days.
	 * 
	 * @param daysFromToday - number of days ahead
	 */
	public void SelectCalendar(int daysFromToday) {
		ca.clickElement(Calender, "clicked on elm");
		ca.selectDateWithDatePicker( daysFromToday, "Date selected through datepicker");

	}
}
