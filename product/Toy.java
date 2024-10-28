package product;

public class Toy extends Product{

	private String brand;
	private int suitableAge;
	private String safetyStandards;
	private String material;
	
	Toy(String id, String name, double sellingPrice, double purchasePrice, int quantity,
			String brand, int suitableAge, String safetyStandards, String material) {
		super(id, name, sellingPrice, purchasePrice, quantity);
		this.brand = brand;
		this.suitableAge = suitableAge;
		this.safetyStandards = safetyStandards;
		this.material = material;
	}

	public void copy(Toy otherToy) {
		this.setId(otherToy.getId());
		this.setName(otherToy.getName());
		this.setSellingPrice(otherToy.getSellingPrice());
		this.setPurchasePrice(otherToy.getPurchasePrice());
		this.setQuantity(otherToy.getQuantity());
		this.setBrand(otherToy.getBrand());
		this.setSuitableAge(otherToy.getSuitableAge());
		this.setSafetyStandards(otherToy.getSafetyStandards());
		this.setMaterial(otherToy.getMaterial());

	}
	
	//Toy getter methods
	public String getBrand() {
		return this.brand;
	}
	public int getSuitableAge() {
		return this.suitableAge;
	}
	public String getSafetyStandards() {
		return this.safetyStandards;
	}
	public String getMaterial() {
		return this.material;
	}
	
	//Toy setter methods
	public void setBrand(String newBrand) {
		this.brand = newBrand;
	}
	public void setSuitableAge(int newSuitableAge) {
		this.suitableAge = newSuitableAge;
	}
	public void setSafetyStandards(String newSafetyStandards) {
		this.safetyStandards = newSafetyStandards;
	}
	public void setMaterial(String newMaterial) {
		this.material = newMaterial;
	}


}