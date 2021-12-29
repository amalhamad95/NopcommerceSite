package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import utils.AppConstants;
import utils.DriversHandler;

public class MainTest {

	public static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {

		driver = DriversHandler.initGoogleChromeDriver();
		driver.get(AppConstants.BASE_URL);

		// ------------------- Login Page -------------------
		Assert.assertTrue(driver.getCurrentUrl().contains(AppConstants.LOGIN_URL));
		Assert.assertTrue(driver.getTitle().contains(AppConstants.LOGIN_PAGE));
		WebElement loginEmail = driver.findElement(By.id("Email"));
		WebElement loginPassword = driver.findElement(By.id("Password"));
		WebElement loginBtn = driver.findElement(By.className("login-button"));
		Assert.assertEquals(loginEmail.getAttribute("value"), "admin@yourstore.com");
		Assert.assertEquals(loginPassword.getAttribute("value"), "admin");
		loginBtn.click();

		// ------------------- Dashboard Page -------------------
		Assert.assertTrue(driver.getCurrentUrl().contains(AppConstants.DASHBOARD_URL));
		WebElement dashboardTitle = driver.findElement(By.cssSelector(".content-header > h1"));
		Assert.assertTrue(dashboardTitle.getText().contains(AppConstants.DASHBOARD_TITLE));
		System.out.println("Title: " + driver.getTitle());

		WebElement navDashboard = driver.findElement(By.cssSelector("li.nav-item a[href='/Admin']"));
		Assert.assertTrue(navDashboard.getAttribute("class").contains("active"));
		Assert.assertEquals(navDashboard.getCssValue("color"), "rgba(255, 255, 255, 1)");

	}
}
