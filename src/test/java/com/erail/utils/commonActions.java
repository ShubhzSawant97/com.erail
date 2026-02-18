package com.erail.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import com.aventstack.extentreports.ExtentTest;
import com.erail.test.BaseClass;

public class commonActions {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected ExtentTest logger;
	BaseClass Bs;
	
	String path = System.getProperty("user.dir") + "/Excelsheet/erailDataSheet.xlsx";

	// Constructor

	public commonActions(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Bs= new BaseClass();
		
	}

	
	// Inject logger dynamically for each test

	public void setLogger(ExtentTest logger) {
		this.logger = logger;
	}

	/**
	 * Waits for an element to be clickable and clicks it.
	 */

	public void clickElement(WebElement elm, String msg) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(elm));
			elm.click();
			logger.pass(msg);

		} catch (Exception e) {
			logger.fail(msg);
			throw new RuntimeException("Failed to click: " + e);
		}
	}

	/**
	 * Clears an input field and enters given value.
	 */
	public void enterValue(WebElement elm, String value, String msg) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(elm));
			elm.clear();
			elm.sendKeys(value);
			logger.pass(msg);
		} catch (Exception e) {
			logger.fail(msg);
			throw new RuntimeException("Failed to enter value: " + e);
		}
	}

	/**
	 * Reads values from column 1 of Sheet1 and returns them as a List.
	 */

	public List<String> readExcelData(String msg, int num) {
		List<String> values = new ArrayList<>();

		try {
			
			File myFile = new File(path);
			Workbook workbook = WorkbookFactory.create(myFile);
			Sheet mysheet = workbook.getSheet("sheet1");
			int totalRows = mysheet.getPhysicalNumberOfRows();
			for (int i = 1; i < totalRows; i++) {
				Row row = mysheet.getRow(i);	
				if (row != null) {
					Cell cell = row.getCell(num);
					if (cell != null) {
						values.add(cell.getStringCellValue());
					}
				}
			}
			logger.pass(msg + values);
			workbook.close();
			return values;
		} catch (Exception e) {
			throw new RuntimeException("Failed to read data: " + e);
		}

	}

	/**
	 * Selects dropdown value by index and returns the selected text.
	 */

	public void selectValueByIndex(By locator, String msg, int index) {
		List<WebElement> options = driver.findElements(locator);

		try {
			   wait.until(d -> d.findElements(locator).size() > 0);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			String value = options.get(index).getText();
			options.get(index).click();
			logger.pass(msg + value);
			
		} catch (Exception e) {
			if (index >= options.size()) {
			    logger.fail("Index " + index + " Available options: " + options.size());
			}
			throw new RuntimeException("Failed to select dropdown index " + index+ ": " + e);
		}
	}

	/**
	 * Returns all dropdown values as a list.
	 */

	public List<String> getDropdownValues(By locator, String msg) {
		try {
			List<String> dropdownValues = new ArrayList<>();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));

			for (WebElement opt : driver.findElements(locator)) {
				dropdownValues.add(opt.getAttribute("innerText"));
			}
			logger.pass(msg + dropdownValues);
			return dropdownValues;

		} catch (Exception e) {
			logger.fail(msg);
			throw new RuntimeException("Failed to read dropdown values: " + e);
		}
	}

	/**
	 * Writes dropdown values into column 0 of Excel sheet.
	 */

	public void writeTExcel(List<String> values, int cellnum, int rownum) {

		try (FileInputStream fis = new FileInputStream(path);) {
			Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheet = workbook.getSheet("sheet1");

			for (int i = 0; i < values.size(); i++) {
				Row row = sheet.getRow(rownum + i);
				if (row == null)
					row = sheet.createRow(rownum + i);

				Cell cell = row.getCell(cellnum);
				if (cell == null)
					cell = row.createCell(cellnum);

				cell.setCellValue(values.get(i));
			}

			

			try (FileOutputStream fos = new FileOutputStream(path)) {
				workbook.write(fos);
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to write to Excel: " + e);
		}
	}

	/**
	 * Selects a future date from a multi-month static datepicker. Example:
	 * daysFromToday=30 -> selects date 30 days ahead.
	 */
	public void compareExcelAndDropdown(By locator, String msg) {

		List<String> excelValues = readExcelData("", 1);
		List<String> dropdownValues = getDropdownValues(locator, "");

		if (excelValues.equals(dropdownValues)) {
			logger.pass(msg + excelValues + "||" + dropdownValues);
		} else {
			logger.fail(msg);

		}
	}

	/*
	 * Selects a future date from a multi-month static datepicker. Example:
	 * daysFromToday=30 -> selects date 30 days ahead.
	 */
		public void selectDateWithDatePicker(int daysFromToday, String msg) {
	
			try {
	
				LocalDate target = LocalDate.now().plusDays(daysFromToday);
	
				String month = target.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // Jan, Feb, Mar
				String monthYear = month + "-" + (target.getYear() % 100); // Jan-26
				int day = target.getDayOfMonth();
	
				By monthTableBy = By.xpath("//table[@class='Month'][.//td[contains(text(),'" + monthYear + "')]]");
				wait.until(ExpectedConditions.visibilityOfElementLocated(monthTableBy));
	
				WebElement monthTable = driver.findElement(monthTableBy);
	
				By dayBy = By.xpath(".//td[normalize-space(text())='" + day + "']");
				WebElement dayCell = monthTable.findElement(dayBy);
	
				dayCell.click();
	
				logger.pass(msg + " Selected Date: " + day + "-" + monthYear);
	
			} catch (Exception e) {
				logger.fail(msg + " | Failed to select date | Error: " + e.getMessage());
				throw new RuntimeException("Datepicker selection failed: " + e);
			}
		}

}
