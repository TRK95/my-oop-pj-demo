package product;

public class Stationery extends Product {

	private String brand, stationeryType, material;
	Stationery(String id, String name, double sellingPrice, double purchasePrice, int quantity,
			String brand, String stationeryType, String material) {
		super(id, name, sellingPrice, purchasePrice, quantity);
		this.brand = brand;
		this.stationeryType = stationeryType;
		this.material = material;
	}
	
	public void copy(Stationery otherStationery) {
		this.setId(otherStationery.getId());
		this.setName(otherStationery.getName());
		this.setSellingPrice(otherStationery.getSellingPrice());
		this.setPurchasePrice(otherStationery.getPurchasePrice());
		this.setQuantity(otherStationery.getQuantity());
		this.setBrand(otherStationery.getBrand());
		this.setStationeryType(otherStationery.getStationeryType());
		this.setMaterial(otherStationery.getMaterial());

	}
	
	// Stationery getter methods
	public String getBrand() {
		return this.brand;
	}
	public String getStationeryType() {
		return this.stationeryType;
	}
	public String getMaterial() {
		return this.material;
	}

	//Stationery setter methods
	public void setBrand(String newBrand) {
		this.brand = newBrand;
	}
	public void setStationeryType(String newStationeryType) {
		this.stationeryType = newStationeryType;
	}
	public void setMaterial(String newMaterial) {
		this.material = newMaterial;
	}


}