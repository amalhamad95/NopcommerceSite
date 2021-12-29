package tests;

import org.openqa.selenium.WebDriver;

import utils.AppConstants;
import utils.DriversHandler;

public class MainTest {

	public static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {

		driver = DriversHandler.initGoogleChromeDriver();
		driver.get(AppConstants.BASE_URL);
	
	}
}
