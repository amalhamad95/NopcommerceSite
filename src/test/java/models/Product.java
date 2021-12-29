package models;

public class Product {

	private String name;
	private String image;
	private String short_description;
	private String full_description;
	private String sku;
	private String category;
	private String manufacturer;
	private String price;

	public Product(String name, String image, String short_description, String full_description, String sku,
			String category, String price) {
		this.name = name;
		this.image = image;
		this.short_description = short_description;
		this.full_description = full_description;
		this.sku = sku;
		this.category = category;
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getFull_description() {
		return full_description;
	}

	public void setFull_description(String full_description) {
		this.full_description = full_description;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
