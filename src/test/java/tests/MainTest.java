package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import models.Discount;
import models.Product;
import utils.AppConstants;
import utils.DriversHandler;
import utils.Helper;

public class MainTest {

	public static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {

		driver = DriversHandler.initGoogleChromeDriver();
		driver.get(AppConstants.BASE_URL);

		String randomProduct = Helper.genarateRandomString(10);
		String randomDisount = Helper.genarateRandomString(5);
		System.out.println("Product: " + randomProduct + "  --Discount:" + randomDisount);

		Product object = new Product(randomProduct, "Short Description Test", "Full Description Test", "COMP_HP_GAME",
				"Computers >> Software", "12000");

		Discount discount = new Discount(randomDisount, "Assigned to products", "10", "50", "12/31/2021 12:00:00 AM",
				"02/28/2022 12:00:00 AM");

		Actions actions = new Actions(driver);
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

		// ------------------- Add New Product Page -------------------
		WebElement addNewProductBtn = driver.findElement(By.cssSelector("a[href='/Admin/Product/Create']"));
		System.out.println("addNewProductBtn: " + addNewProductBtn.getCssValue("background-color"));
		actions.moveToElement(addNewProductBtn).build().perform();
//		String hexColor = Color.fromString(addNewProductBtn.getCssValue("background-color")).asHex();
//		// #4580a2
//		System.out.println("addNewProductBtn: " + hexColor);
//		Assert.assertEquals(hexColor, "#4580a2");
//		actions.release().build().perform();
		addNewProductBtn.click();

		Thread.sleep(3 * 1000);
		checkTitleAndURL(AppConstants.ADD_PRODUCT_URL, AppConstants.ADD_PRODUCT_TITLE);
		System.out.println("Title: " + driver.getTitle());
//		Assert.assertTrue(driver.getCurrentUrl().contains("/Admin/Product/Create"));
//		WebElement newProductPageTitle = driver.findElement(By.className("float-left"));
//		Assert.assertTrue(newProductPageTitle.getText().contains("Add a new product"));

		String bodyClasses = driver.findElement(By.tagName("body")).getAttribute("class");
		System.out.println("bodyClasses: " + bodyClasses);
		if (!bodyClasses.contains("basic-settings-mode")) {
			WebElement onoffswitch = driver.findElement(By.className("onoffswitch"));
			onoffswitch.click();
		}

		openCardIfClosed(driver.findElement(By.cssSelector("#product-info")), "#product-info");
		openCardIfClosed(driver.findElement(By.cssSelector("#product-price")), "#product-price");
		openCardIfClosed(driver.findElement(By.cssSelector("#product-inventory")), "#product-inventory");

//		WebElement infoCard = driver.findElement(By.cssSelector("#product-info"));
//		System.out.println("infoCard: " + infoCard.getAttribute("class"));
//		if (!infoCard.getAttribute("class").contains("collapsed-card")) {
//			WebElement toggle = driver.findElement(By.cssSelector("#product-info i.toggle-icon"));
//			toggle.click();
//		}
//
//		WebElement priceCard = driver.findElement(By.cssSelector("#product-price"));
//		System.out.println("priceArea: " + priceCard.getAttribute("class"));
//		if (!priceCard.getAttribute("class").contains("collapsed-card")) {
//			WebElement toggle = driver.findElement(By.cssSelector("#product-price i.toggle-icon"));
//			toggle.click();
//		}
//
//		WebElement inventoryCard = driver.findElement(By.cssSelector("#product-inventory"));
//		System.out.println("inventoryCard: " + inventoryCard.getAttribute("class"));
//		if (!inventoryCard.getAttribute("class").contains("collapsed-card")) {
//			WebElement toggle = driver.findElement(By.cssSelector("#product-inventory i.toggle-icon"));
//			toggle.click();
//		}

		// ***************Product Info
		// ***Product Name
		WebElement productName = driver.findElement(By.id("Name"));
		setFieldText(productName, object.getName());
		checkFieldTooltip("Name", "The name of the product.");
//		productName.sendKeys(object.getName());
//		// Assert entered value
//		Assert.assertTrue(productName.getAttribute("value").contains(object.getName()));
//		// Assert tootip
//		WebElement nameTootip = driver.findElement(By.cssSelector("label[for=\"Name\"]+div[data-toggle=\"tooltip\"]"));
//		Assert.assertTrue(nameTootip.getAttribute("data-original-title").contains("The name of the product."));

		// ***Product ShortDescription
		WebElement productShortDescription = driver.findElement(By.id("ShortDescription"));
		setFieldText(productShortDescription, object.getShort_description());
		checkFieldTooltip("ShortDescription",
				"Short description is the text that is displayed in product list i.e. сategory / manufacturer pages.");
//		productShortDescription.sendKeys(object.getShort_description());
//		// Assert entered value
//		Assert.assertTrue(productShortDescription.getAttribute("value").contains(object.getShort_description()));
//		// Assert tootip
//		WebElement ShortDescriptionTootip = driver
//				.findElement(By.cssSelector("label[for=\"ShortDescription\"]+div[data-toggle=\"tooltip\"]"));
//		Assert.assertTrue(ShortDescriptionTootip.getAttribute("data-original-title").contains(
//				"Short description is the text that is displayed in product list i.e. сategory / manufacturer pages."));

		// ***Product FullDescription
//		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("FullDescription_ifr"))));
		driver.switchTo().frame("FullDescription_ifr");
		WebElement editable = driver.switchTo().activeElement();
		editable.sendKeys(object.getFull_description());
		// Assert entered value
		WebElement iFramebody = driver.findElement(By.cssSelector("body"));
		System.out.println(iFramebody.getText());
		Assert.assertTrue(iFramebody.getText().contains(object.getFull_description()));
		driver.switchTo().defaultContent();
		checkFieldTooltip("FullDescription", "Full description is the text that is displayed in product page.");
//		// Assert tootip
//		WebElement ShortFullDescription = driver
//				.findElement(By.cssSelector("label[for=\"FullDescription\"]+div[data-toggle=\"tooltip\"]"));
//		Assert.assertTrue(ShortFullDescription.getAttribute("data-original-title")
//				.contains("Full description is the text that is displayed in product page."));

		// ***Product Sku
		WebElement productSku = driver.findElement(By.id("Sku"));
		setFieldText(productSku, object.getSku());
		checkFieldTooltip("Sku",
				"Product stock keeping unit (SKU). Your internal unique identifier that can be used to track this product.");
//		productSku.sendKeys(object.getSku());
//		// Assert entered value
//		Assert.assertTrue(productSku.getAttribute("value").contains(object.getSku()));
//		// Assert tootip
//		WebElement SkuTootip = driver.findElement(By.cssSelector("label[for=\"Sku\"]+div[data-toggle=\"tooltip\"]"));
//		Assert.assertTrue(SkuTootip.getAttribute("data-original-title").contains(
//				"Product stock keeping unit (SKU). Your internal unique identifier that can be used to track this product."));

		// ***Product Category
		WebElement productCategory = driver
				.findElement(By.cssSelector("input[aria-labelledby=\"SelectedCategoryIds_label\"]"));
		productCategory.click();
		WebElement selectedCategory = driver
				.findElement(By.xpath("//ul[@id='SelectedCategoryIds_listbox']/li[text() = 'Computers']"));
		selectedCategory.click();
		Assert.assertEquals(selectedCategory.getAttribute("aria-selected"), "true");

		// ***************Product Price
		// ***Product Price
		WebElement productPrice = driver
				.findElement(By.xpath("//div[@id=\"product-price-area\"]/div[2]/span/span/input"));
		productPrice.sendKeys(object.getPrice() + Keys.TAB);
		Assert.assertEquals(productPrice.getAttribute("aria-valuenow"), object.getPrice());
//		setFieldText(productPrice, object.getPrice());
		checkFieldTooltip("Price",
				"The price of the product. You can manage currency by selecting Configuration > Currencies.");

		// ***Product Price Tax
		WebElement IsTaxExempt = driver.findElement(By.id("IsTaxExempt"));
		if (!IsTaxExempt.isSelected()) {
			IsTaxExempt.click();
			Assert.assertTrue(IsTaxExempt.isSelected());
		}
		checkFieldTooltip("IsTaxExempt",
				"Determines whether this product is tax exempt (tax will not be applied to this product at checkout).");

		// ***************Product Inventory
		// ***Product Inventory
		WebElement manageInventoryMethodId = driver.findElement(By.id("ManageInventoryMethodId"));
		Select dropdown = new Select(manageInventoryMethodId);
		dropdown.selectByVisibleText("Track inventory");
		Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Track inventory");
		checkFieldTooltip("ManageInventoryMethodId",
				"Select inventory method. There are three methods: Don’t track inventory, Track inventory and Track inventory by attributes. You should use Track inventory by attributes when the product has different combinations of these attributes and then manage inventory for these combinations.");
		// StockQuantity
		WebElement StockQuantity = driver
				.findElement(By.xpath("//*[@id=\"pnlStockQuantity\"]/div[2]/span/span/input[1]"));
		Assert.assertEquals(StockQuantity.getAttribute("aria-valuenow"), "10000");
		checkFieldTooltip("StockQuantity", "The current stock quantity of this product.");

		// ********* Save Product
		WebElement saveBtn = driver.findElement(By.name("save"));
		actions.moveToElement(saveBtn).build().perform();
//		Assert.assertEquals(Color.fromString(saveBtn.getCssValue("background-color")).asHex(), "#4580a2");
		saveBtn.click();
		// successMsg
		checkTitleAndURL(AppConstants.PRODUCTS_URL, AppConstants.PRODUCTS_TITLE);
		System.out.println("Title: " + driver.getTitle());
//		Assert.assertTrue(driver.getCurrentUrl().contains("/Product/List"));
		checkSuccessAlert("The new product has been added successfully.");
//		WebElement successMsg = driver.findElement(By.className("alert-success"));
//		Assert.assertTrue(successMsg.getText().contains("The new product has been added successfully."));
		// Search for added product in products list
		WebElement SearchProductName = driver.findElement(By.id("SearchProductName"));
		setFieldText(SearchProductName, object.getName());
		checkFieldTooltip("SearchProductName", "A product name.");
		WebElement btnSearch = driver.findElement(By.id("search-products"));
		btnSearch.click();
		// check empty result label not exist
		Assert.assertTrue(driver.findElements(By.className("dataTables_empty")).isEmpty());
		// check product found in search results
		Assert.assertTrue(!driver.findElements(By.xpath("//td[text() = '" + object.getName() + "']")).isEmpty());

		// ------------------- Discounts Page -------------------
		WebElement navPromotions = driver
				.findElement(By.xpath("//p[contains(text(), 'Promotions')]/ancestor::a[@href='#']/parent::li"));
		actions.moveToElement(navPromotions).click().perform();
		Thread.sleep(1 * 1000);
		System.out.println("navPromotions: " + navPromotions.getAttribute("class"));
		Assert.assertTrue(navPromotions.getAttribute("class").contains("menu-open"));

		WebElement navDiscounts = wait
				.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/Admin/Discount/List")));
		navDiscounts.click();

		checkTitleAndURL(AppConstants.DISCOUTS_URL, AppConstants.DISCOUNTS_TITLE);
		System.out.println("Title: " + driver.getTitle());

		// ------------------- Add New Discount Page -------------------
		WebElement addNewDiscountBtn = driver.findElement(By.cssSelector("a[href='/Admin/Discount/Create']"));
		actions.moveToElement(addNewDiscountBtn).build().perform();
//				Assert.assertEquals(Color.fromString(addNewDiscountBtn.getCssValue("background-color")).asHex(), "#4580a2");
		addNewDiscountBtn.click();

		Thread.sleep(3 * 1000);
		checkTitleAndURL(AppConstants.ADD_DISCOUNT_URL, AppConstants.ADD_DISCOUNT_TITLE);
		System.out.println("Title: " + driver.getTitle());
		openCardIfClosed(driver.findElement(By.cssSelector("#discount-info")), "#discount-info");

		// ***Discount Name
		WebElement discountName = driver.findElement(By.id("Name"));
		setFieldText(discountName, discount.getName());
		checkFieldTooltip("Name", "The name of the discount.");

		// ***Discount Type
		WebElement DiscountTypeId = driver.findElement(By.id("DiscountTypeId"));
		Select typeDropdown = new Select(DiscountTypeId);
		typeDropdown.selectByVisibleText(discount.getType());
		Assert.assertEquals(typeDropdown.getFirstSelectedOption().getText(), discount.getType());
		checkFieldTooltip("DiscountTypeId", "The type of discount.");

		// ***Discount UsePercentage
		WebElement UsePercentage = driver.findElement(By.id("UsePercentage"));
		if (!UsePercentage.isSelected()) {
			UsePercentage.click();
			Assert.assertTrue(UsePercentage.isSelected());
		}
		checkFieldTooltip("UsePercentage",
				"Determines whether to apply a percentage discount to the order/SKUs. If not enabled, a set value is discounted.");

		// ***Discount Percentage
		WebElement discountPercentage = driver
				.findElement(By.xpath("//*[@id=\"pnlDiscountPercentage\"]/div[2]/span/span/input[1]"));
		discountPercentage.sendKeys(discount.getPercentage() + Keys.TAB);
		Assert.assertEquals(discountPercentage.getAttribute("aria-valuenow"), discount.getPercentage());
//				setFieldText(productPrice, object.getPrice());
		checkFieldTooltip("DiscountPercentage", "The percentage discount to apply to order/SKUs.");

		// ***Discount Amount
//				WebElement MaximumDiscountAmount = driver
//						.findElement(By.xpath("//*[@id=\"pnlMaximumDiscountAmount\"]/div[2]/span/span/input[1]"));
//				MaximumDiscountAmount.sendKeys(discount.getAmount() + Keys.TAB);
//				Assert.assertEquals(MaximumDiscountAmount.getAttribute("aria-valuenow"), discount.getAmount());
//						setFieldText(productPrice, object.getPrice());
		checkFieldTooltip("MaximumDiscountAmount",
				"Maximum allowed discount amount. Leave empty to allow any discount amount. If you're using \"Assigned to products\" discount type, then it's applied to each product separately.");

		// ***Discount Start Date
		WebElement StartDateUtc = driver.findElement(By.id("StartDateUtc"));
		setFieldText(StartDateUtc, discount.getStartDate());
		checkFieldTooltip("StartDateUtc", "The start of the discount period in Coordinated Universal Time (UTC).");

		// ***Discount End Date
		WebElement EndDateUtc = driver.findElement(By.id("EndDateUtc"));
		setFieldText(EndDateUtc, discount.getEndDate());
		checkFieldTooltip("EndDateUtc", "The end of the discount period in Coordinated Universal Time (UTC).");

		// ********* Save Discount
		WebElement saveDiscountBtn = driver.findElement(By.name("save"));
//				WebElement saveDiscountBtn = driver.findElement(By.name("save-continue"));
		actions.moveToElement(saveDiscountBtn).build().perform();
//				Assert.assertEquals(Color.fromString(saveDiscountBtn.getCssValue("background-color")).asHex(), "#4580a2");
		saveDiscountBtn.click();
		// successMsg
		checkTitleAndURL(AppConstants.DISCOUTS_URL, AppConstants.DISCOUNTS_TITLE);
		System.out.println("Title: " + driver.getTitle());
		checkSuccessAlert("The new discount has been added successfully.");
//				WebElement successDisountMsg = driver.findElement(By.className("alert-success"));
//				Assert.assertTrue(successDisountMsg.getText().contains("The new discount has been added successfully."));
		// Search for added discount in products list
		WebElement SearchDiscountName = driver.findElement(By.id("SearchDiscountName"));
		setFieldText(SearchDiscountName, discount.getName());
		checkFieldTooltip("SearchDiscountName", "Search by discount name.");
		WebElement searchDiscounts = driver.findElement(By.id("search-discounts"));
		searchDiscounts.click();
		// check empty result label not exist
		Assert.assertTrue(driver.findElements(By.className("dataTables_empty")).isEmpty());
		// check discount found in search results
		Assert.assertTrue(!driver.findElements(By.xpath("//td[text() = '" + discount.getName() + "']")).isEmpty());

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

	public static void openCardIfClosed(WebElement card, String cardSelector) {
		System.out.println(cardSelector + ": " + card.getAttribute("class"));
		if (card.getAttribute("class").contains("collapsed-card")) {
			WebElement toggle = driver.findElement(By.cssSelector(cardSelector + " .card-title"));
			toggle.click();
			System.out.println("openCardIfClosed: " + cardSelector);
		}
	}

	public static void setFieldText(WebElement element, String value) {
		element.clear();
		element.sendKeys(value);
		// Assert entered value
		Assert.assertTrue(element.getAttribute("value").contains(value));
	}

	public static void checkFieldTooltip(String field, String tipText) {
		// Assert tootip
		WebElement tooltip = driver
				.findElement(By.cssSelector("label[for='" + field + "']+div[data-toggle=\"tooltip\"]"));
		Assert.assertTrue(tooltip.getAttribute("data-original-title").contains(tipText));
	}

	public static void checkSuccessAlert(String message) {
		WebElement successAlert = driver.findElement(By.className("alert-success"));
		Assert.assertTrue(successAlert.getText().contains(message));
		String hexColor = Color.fromString(successAlert.getCssValue("background-color")).asHex();
		Assert.assertEquals(hexColor, "#17b76d");
	}
}
