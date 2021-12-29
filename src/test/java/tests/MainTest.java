package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.AppConstants;
import utils.DriversHandler;

public class MainTest {

	public static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {

		driver = DriversHandler.initGoogleChromeDriver();
		driver.get(AppConstants.BASE_URL);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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

		// ------------------- Products Page -------------------
		WebElement navCatalog = driver
				.findElement(By.xpath("//p[contains(text(), 'Catalog')]/ancestor::a[@href='#']/parent::li"));
//		WebElement navCatalog = driver.findElements(By.xpath("//p[contains(text(), \"Catalog\")]")).get(0);
		navCatalog.click();
		Thread.sleep(1 * 1000);
		System.out.println("navCatalog: " + navCatalog.getAttribute("class"));
		Assert.assertTrue(navCatalog.getAttribute("class").contains("menu-open"));
//		Assert.assertEquals(navCatalog.getCssValue("color"), "rgb(255, 255, 255)");
//		System.out.println(navCatalog.getAttribute("class"));
//		Assert.assertTrue(navCatalog.getAttribute("class").contains("menu-open"));

		WebElement navProducts = wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/Admin/Product/List']")));
		navProducts.click();
//		Assert.assertTrue(navCatalog.getAttribute("class").contains("active"));

		checkTitleAndURL(AppConstants.PRODUCTS_URL, AppConstants.PRODUCTS_TITLE);
		System.out.println("Title: " + driver.getTitle());

//		Assert.assertTrue(driver.getCurrentUrl().contains("/Product/List"));
//		WebElement productsPageTitle = driver.findElement(By.className("float-left"));
//		Assert.assertTrue(productsPageTitle.getText().contains("Products"));

	}

//	public static void assertNavItemSelected(WebElement item) {
//		Assert.assertTrue(item.getAttribute("class").contains("active"));
//		Assert.assertEquals(item.getCssValue("color"), "rgb(255, 255, 255, 1)");
//	}

	public static void checkTitleAndURL(String url, String title) {
		Assert.assertTrue(driver.getCurrentUrl().contains(url));
		Assert.assertTrue(driver.getTitle().contains(title));

		WebElement titleElement = driver.findElement(By.className("float-left"));
		Assert.assertTrue(titleElement.getText().contains(title));
	}
}
