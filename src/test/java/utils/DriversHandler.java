package utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class DriversHandler {

	public static WebDriver initGoogleChromeDriver() {
		System.setProperty("webdriver.chrome.driver", "D:\\drivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		return driver;
	}

	public static WebDriver initFirefoxDriver() {
		System.setProperty("webdriver.gecko.driver", "D:\\drivers\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		return driver;
	}

	public static WebDriver initInternetExplorerDriver() {
		System.setProperty("webdriver.ie.driver", "D:\\drivers\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		return driver;
	}
}
