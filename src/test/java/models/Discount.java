package models;

public class Discount {

	private String name;
	private String type;
	private String percentage;
	private String amount;
	private String startDate;
	private String endDate;

	public Discount(String name, String type, String percentage, String amount, String startDate, String endDate) {
		this.name = name;
		this.type = type;
		this.percentage = percentage;
		this.amount = amount;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
