package com.erail.test;

import org.testng.annotations.Test;

public class erailHomepageTest extends BaseClass {

	// Test Case 1: Enter station name & select from dropdown

	@Test
	public void TC_01()  {

		er.enterStationName("DEL", 5);
	}

	// Test Case 2: Read values from Excel file

	@Test
	public void TC_02() {

		er.readExcel(1);
		
	}

	// Test Case 3: Read all dropdown values & write them to Excel

	@Test
	public void TC_03() {

		er.readDropdownValues(0,1);
	}

	// Test Case 4: Compare Excel values with dropdown values

	@Test
	public void TC_04() {

		er.compareValues();
	}
	
	// Test Case 5: Enter station name in station to
	@Test
	public void TC_05() {
		er.EnterValueinStationTo("MUM");
		}

	
	// Test Case 6: Entered the stations in excel through dropdown & selected the value from dropdown through idex
	
	@Test
	public void TC_06() {
		er.SelectStationTo(4, 2, 1);
		}
	
	// Test Case 7: Select a date 30 days from today using datepicker
	@Test
	public void TC_07() {
		er.SelectCalendar(30);
	}

}
