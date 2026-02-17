package com.erail.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.erail.pages.erailHomepage;
import com.erail.utils.commonActions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {
	// GLOBAL OBJECTS USED BY ALL TEST CLASSES

	protected ExtentReports extent;
	protected ExtentSparkReporter spark;
	protected ExtentTest logger;
	protected WebDriver driver;
	protected commonActions ca;
	erailHomepage er; 
	
	
	// BEFORE SUITE → Setup Extent Report + WebDriver
	// Runs ONCE before entire test suite


	@BeforeSuite
	public void Base() {
		createExtentReport();
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

	}

	// BEFORE CLASS → Initialize Page Objects & Utils
	// Runs once before each test class
	@BeforeClass
	public void init() {
		ca = new commonActions(driver);
		er = new erailHomepage(driver);
		driver.get("https://erail.in/");
		driver.manage().window().maximize();
	}

	// BEFORE METHOD → Create logger node per test case
	// Inject logger into POM + commonActions
	// Runs before EVERY @Test method

	@BeforeMethod
	public void setupLoggerTedt(Method method) throws EncryptedDocumentException, IOException {
		logger = extent.createTest(method.getName());
		ca.setLogger(logger);
		er.setLogger(logger);
	}

	// AFTER SUITE → Quit Driver + Flush Report
	// Runs ONCE after entire test suite

	@AfterSuite
	public void quit() {
		driver.quit();
		extent.flush();
	}

	// EXTENT REPORT INITIALIZATION
	// Sets report name, location, system info

	public void createExtentReport() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
		String fileName = formatter.format(date);
		String reportPath = System.getProperty("user.dir") + "/src/test/java/com/erail/reports/ExtentReport" + fileName
				+ ".html";
		spark = new ExtentSparkReporter(reportPath);
		extent = new ExtentReports();
		extent.attachReporter(spark);
		extent.setSystemInfo("orangeHRM", "QA Assignment");
		extent.setSystemInfo("Environment", "Test");
		spark.config().setDocumentTitle("login regression");
		spark.config().setReportName("Shubham Sawant");
	}

	public int index() {
		int inde = 5;
		return inde;
	}
	
	public void abc ()
	{
		System.out.println("asdfdg");
	}
	
}
	