package model;

public class Toy extends Product {
	private String brand;
	private String suitage;
	private String material;

	public Toy(String id, String name, double price, int quantity, double inputprice, String brand, String suitage, String material) {
		super(id, name, price, quantity, inputprice);
		this.brand = brand;
		this.suitage = suitage;
		this.material = material;
	}
	public String getBrand() {
		return brand;
	}
	public String getSuitAge() {
		return suitage;
	}
	public String getMaterial() {
		return material;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setSuitAge(String suitage) {
		this.suitage = suitage;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
}
