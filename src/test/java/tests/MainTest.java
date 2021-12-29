package tests;

import java.time.Duration;
import java.util.List;

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

		Product object = new Product(randomProduct,
				"https://admin-demo.nopcommerce.com/images/thumbs/default-image_75.png", "Short Description Test",
				"Full Description Test", "COMP_HP_GAME", "Computers >> Software", "12000");

		Discount discount = new Discount(randomDisount, "Assigned to products", "10", "50", "12/31/2021 12:00:00 AM",
				"2/28/2022 12:00:00 AM");

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

		String bodyClasses = driver.findElement(By.tagName("body")).getAttribute("class");
		System.out.println("bodyClasses: " + bodyClasses);
		if (!bodyClasses.contains("basic-settings-mode")) {
			WebElement onoffswitch = driver.findElement(By.className("onoffswitch"));
			onoffswitch.click();
		}

		openCardIfClosed(driver.findElement(By.cssSelector("#product-info")), "#product-info");
		openCardIfClosed(driver.findElement(By.cssSelector("#product-price")), "#product-price");
		openCardIfClosed(driver.findElement(By.cssSelector("#product-inventory")), "#product-inventory");

		// ***************Product Info
		// ***Product Name
		WebElement productName = driver.findElement(By.id("Name"));
		setFieldText(productName, object.getName());
		checkFieldTooltip("Name", "The name of the product.");

		// ***Product ShortDescription
		WebElement productShortDescription = driver.findElement(By.id("ShortDescription"));
		setFieldText(productShortDescription, object.getShort_description());
		checkFieldTooltip("ShortDescription",
				"Short description is the text that is displayed in product list i.e. сategory / manufacturer pages.");

		// ***Product FullDescription
		driver.switchTo().frame("FullDescription_ifr");
		WebElement editable = driver.switchTo().activeElement();
		editable.sendKeys(object.getFull_description());
		// Assert entered value
		WebElement iFramebody = driver.findElement(By.cssSelector("body"));
		System.out.println(iFramebody.getText());
		Assert.assertTrue(iFramebody.getText().contains(object.getFull_description()));
		driver.switchTo().defaultContent();
		checkFieldTooltip("FullDescription", "Full description is the text that is displayed in product page.");

		// ***Product Sku
		WebElement productSku = driver.findElement(By.id("Sku"));
		setFieldText(productSku, object.getSku());
		checkFieldTooltip("Sku",
				"Product stock keeping unit (SKU). Your internal unique identifier that can be used to track this product.");

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
		checkSuccessAlert("The new product has been added successfully.");
		// Search for added product in products list
		WebElement SearchProductName = driver.findElement(By.id("SearchProductName"));
		WebElement btnSearch = driver.findElement(By.id("search-products"));
		checkFieldTooltip("SearchProductName", "A product name.");
		searchForItem(SearchProductName, btnSearch, object.getName());
		// check product data in table row
		WebElement productRow = driver.findElement(By.xpath("//td[text() = '" + object.getName() + "']/parent::tr"));
		List<WebElement> productColumns = productRow.findElements(By.tagName("td"));
		String productRowAttrs = "";
		for (WebElement column : productColumns) {
			productRowAttrs += column.getText() + "\t";
		}
		System.out.println("discountRowAttrs: " + productRowAttrs);
		Assert.assertTrue(productRowAttrs.contains(object.getName()));
//		Assert.assertTrue(productRowAttrs.contains(object.getImage()));
		Assert.assertTrue(productRowAttrs.contains(object.getSku()));
		Assert.assertTrue(productRowAttrs.contains(object.getPrice()));

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
		checkSuccessAlert("The new discount has been added successfully.");
		// Search for added discount in products list
		WebElement SearchDiscountName = driver.findElement(By.id("SearchDiscountName"));
		WebElement btnSearchDiscounts = driver.findElement(By.id("search-discounts"));
		checkFieldTooltip("SearchDiscountName", "Search by discount name.");
		searchForItem(SearchDiscountName, btnSearchDiscounts, discount.getName());

		// ------------------- Edit Discount Page -------------------
		WebElement discountRow = driver.findElement(By.xpath("//td[text() = '" + discount.getName() + "']/parent::tr"));
		List<WebElement> discountColumns = discountRow.findElements(By.tagName("td"));
		String discountRowAttrs = "";
		for (WebElement column : discountColumns) {
			discountRowAttrs += column.getText() + "\t";
		}
		System.out.println("discountRowAttrs: " + discountRowAttrs);
		Assert.assertTrue(discountRowAttrs.contains(discount.getName()));
		Assert.assertTrue(discountRowAttrs.contains(discount.getType()));
		Assert.assertTrue(discountRowAttrs.contains(discount.getPercentage()));
		Assert.assertTrue(discountRowAttrs.contains(discount.getStartDate()));
		Assert.assertTrue(discountRowAttrs.contains(discount.getEndDate()));

		WebElement editDiscountBtn = driver.findElement(By
				.xpath("//td[text() = '" + discount.getName() + "']/following-sibling::td[@class=' button-column']/a"));
		editDiscountBtn.click();
		checkTitleAndURL(AppConstants.EDIT_DISCOUNT_URL, AppConstants.EDIT_DISCOUNT_TITLE);
		WebElement titleElement = driver.findElement(By.className("float-left"));
		Assert.assertTrue(
				titleElement.getText().contains(AppConstants.EDIT_DISCOUNT_TITLE + " - " + discount.getName()));

		openCardIfClosed(driver.findElement(By.cssSelector("#discount-info")), "#discount-info");
		openCardIfClosed(driver.findElement(By.cssSelector("#discount-applied-to-products")),
				"#discount-applied-to-products");

		// ***Discount Name
		WebElement discountNameE = driver.findElement(By.id("Name"));
		Assert.assertTrue(discountNameE.getAttribute("value").contains(discount.getName()));
		checkFieldTooltip("Name", "The name of the discount.");

		// ***Discount Type
		WebElement SelectedDiscountType = driver
				.findElement(By.xpath("//select[@id='DiscountTypeId']/option[text() = '" + discount.getType() + "']"));
		Assert.assertEquals(SelectedDiscountType.getAttribute("selected"), "true");
		checkFieldTooltip("DiscountTypeId", "The type of discount.");

		// ***Discount UsePercentage
		WebElement UsePercentageE = driver.findElement(By.id("UsePercentage"));
		Assert.assertTrue(UsePercentageE.isSelected());
		checkFieldTooltip("UsePercentage",
				"Determines whether to apply a percentage discount to the order/SKUs. If not enabled, a set value is discounted.");

		// ***Discount Percentage
		WebElement discountPercentageE = driver
				.findElement(By.xpath("//*[@id=\"pnlDiscountPercentage\"]/div[2]/span/span/input[1]"));
		Assert.assertEquals(discountPercentageE.getAttribute("aria-valuenow"), discount.getPercentage());
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
		WebElement StartDateUtcE = driver.findElement(By.id("StartDateUtc"));
		Assert.assertTrue(StartDateUtcE.getAttribute("value").contains(discount.getStartDate()));
		checkFieldTooltip("StartDateUtc", "The start of the discount period in Coordinated Universal Time (UTC).");

		// ***Discount End Date
		WebElement EndDateUtcE = driver.findElement(By.id("EndDateUtc"));
		Assert.assertTrue(EndDateUtcE.getAttribute("value").contains(discount.getEndDate()));
		checkFieldTooltip("EndDateUtc", "The end of the discount period in Coordinated Universal Time (UTC).");

		// ------------------- Add Product To Discount -------------------
		WebElement btnAddNewProduct = driver.findElement(By.id("btnAddNewProduct"));
		actions.moveToElement(btnAddNewProduct).build().perform();
//				Assert.assertEquals(Color.fromString(addNewDiscountBtn.getCssValue("background-color")).asHex(), "#4580a2");
//				onclick="javascript:OpenWindow('/Admin/Discount/ProductAddPopup?discountId=12&btnId=btnRefreshProducts&formId=discount-form', 800, 800, true); return false;"
		String onclickJavascript = btnAddNewProduct.getAttribute("onclick");
		System.out.println(onclickJavascript);
		String[] splitStrings = onclickJavascript.split("\'");
		String onclickUrl = splitStrings[1];
		System.out.println(onclickUrl);

		String parentWindowID = driver.getWindowHandle();
		btnAddNewProduct.click();

		// Get all opened browser windows
		for (String windowID : driver.getWindowHandles()) {
			String title = driver.switchTo().window(windowID).getTitle();
			System.out.println("Window Title: " + driver.getTitle() + " *** " + title);

			if (title.contains("Add a new product")) {
				checkTitleAndURL(onclickUrl, AppConstants.ADD_PRODUCT_POPUP_TITLE);
				Assert.assertEquals(title, driver.getTitle());
				// Add new product to current discount
				// Search for added product in products list
				WebElement SearchProductNameP = driver.findElement(By.id("SearchProductName"));
				setFieldText(SearchProductNameP, object.getName());
				checkFieldTooltip("SearchProductName", "A product name.");
				WebElement btnSearchP = driver.findElement(By.id("search-products"));
				btnSearchP.click();
				// Select product
				WebElement productRowCheckBox = driver
						.findElement(By.xpath("//td[text() = '" + object.getName() + "']/parent::tr/td/input"));
				if (!productRowCheckBox.isSelected()) {
					productRowCheckBox.click();
					Assert.assertTrue(productRowCheckBox.isSelected());
				}
				// Save Selected Product
				WebElement SaveSelectedProduct = driver.findElement(By.name("save"));
				SaveSelectedProduct.click();

//						driver.close();
				break;
			}
		}
		// Return to Edit page
		driver.switchTo().window(parentWindowID);
		checkTitleAndURL(AppConstants.EDIT_DISCOUNT_URL, AppConstants.EDIT_DISCOUNT_TITLE);
		// Check if product added to list
//		Assert.assertTrue(driver.findElements(By.cssSelector("#products-grid_wrapper .dataTables_empty")).isEmpty());
		Assert.assertTrue(!driver.findElements(By.xpath("//td[text() = '" + object.getName() + "']")).isEmpty());

		// Save changes on Discount
		WebElement saveDiscountChangesBtn = driver.findElement(By.name("save"));
		actions.moveToElement(saveDiscountChangesBtn).build().perform();
//				Assert.assertEquals(Color.fromString(saveDiscountChangesBtn.getCssValue("background-color")).asHex(), "#4580a2");
		saveDiscountChangesBtn.click();

		// successMsg
		checkTitleAndURL(AppConstants.DISCOUTS_URL, AppConstants.DISCOUNTS_TITLE);
		checkSuccessAlert("he discount has been updated successfully.");

		// *** Check if product found in Discount products list after reOpen this
		// discount page
		WebElement SearchDiscountName2 = driver.findElement(By.id("SearchDiscountName"));
		WebElement btnSearchDiscounts2 = driver.findElement(By.id("search-discounts"));
		checkFieldTooltip("SearchDiscountName", "Search by discount name.");
		searchForItem(SearchDiscountName2, btnSearchDiscounts2, discount.getName());
		WebElement DiscountRowAfterAddingProduct = driver.findElement(By
				.xpath("//td[text() = '" + discount.getName() + "']/following-sibling::td[@class=' button-column']/a"));
		DiscountRowAfterAddingProduct.click();
		checkTitleAndURL(AppConstants.EDIT_DISCOUNT_URL, AppConstants.EDIT_DISCOUNT_TITLE);
		openCardIfClosed(driver.findElement(By.cssSelector("#discount-applied-to-products")),
				"#discount-applied-to-products");
		Assert.assertTrue(!driver.findElements(By.xpath("//td[text() = '" + object.getName() + "']")).isEmpty());

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

	public static void openSearchCardIfClosed() {
		WebElement searchCard = driver.findElement(By.className("search-row"));
		System.out.println("searchCard: " + searchCard.getAttribute("class"));
		if (!searchCard.getAttribute("class").contains("opened")) {
			searchCard.click();
			System.out.println("openCardIfClosed: true");
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

	public static void searchForItem(WebElement inputSearch, WebElement btnSearch, String name) {
		openSearchCardIfClosed();
		setFieldText(inputSearch, name);
		btnSearch.click();
		// check empty result label not exist
		Assert.assertTrue(driver.findElements(By.className("dataTables_empty")).isEmpty());
		// check item found in search results
		Assert.assertTrue(!driver.findElements(By.xpath("//td[text() = '" + name + "']")).isEmpty());

	}
}
