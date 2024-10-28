package order;

import employee.Employee;
import product.Product;
import main.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Order {
    private Map<String, Integer> productList = new HashMap<String, Integer>();
	private long price = 0;
	private String id;
	private Employee employee = new Employee();
	
	public Order() {

	}

	public Order(String id) {
		this.id = id;
	}
	
    public Order(Employee e) {
        this.employee = e;
    }

	public Order(String id, Map<String, Integer> productList) {
		this.id = id;
		this.productList = productList;
	}
	
	public String getID() {
		return this.id;
	}
	
	public Map<String, Integer> getProductList() {
		return this.productList;
	}

    public void getEmployeeInfo() {
        Employee e = this.employee;
        System.out.printf("Employee ID: %s, Name: %s", e.getID(), e.getName());

    }
	
	public long getPrice() {
		return this.price;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setPrice(long p) {
		this.price = p;
	}

    public void setEmployee(Employee e) {
        this.employee = e;
    }

    private void addToCart(String proID, int quantty, List<Product> productData) {
		Product item = this.employee.searchProduct(proID, productData);
		if (this.productList.get(proID) != null) this.productList.put(proID, productList.get(proID) + quantty);
		else this.productList.put(proID, quantty);
		this.price += item.getSellingPrice() * quantty;
	}
	
	private void removeFromCart(String proID, ArrayList<Product> productData) {
		int quantty = this.productList.get(proID);
		this.addToCart(proID, -quantty, productData);
		this.productList.remove(proID);
	}
	
	private void reduceQuanttyFromCart(String proID, int quantty, ArrayList<Product> productData) {
		if (quantty < this.productList.get(proID))
		this.addToCart(proID, -quantty, productData);
		else {
			this.productList.remove(proID);
		}
	}

    private void cancelOrder() {
		this.productList.clear();
		this.price = 0;
	}
	
	private void writeToCSV() {
		
	}
	
	public void getOrder(ArrayList<Product> productData) {
		long price = 0;
		Scanner inp = new Scanner(System.in);
		String itemID = new String();
		int quantty;
		
		
		// Add item to cart by itemID
		boolean validInp = false;
		while(!validInp) {
			try {
				while(true) {
					/*
					 * 1. add to cart
					 * 2. remove from card
					 * 3. reduce quantity
					 * 4. end of add to cart
					 * 5. cancel order
					 * */
					int function = inp.nextInt();
					if (function == 1) {
						itemID = inp.nextLine();
						quantty = inp.nextInt();
						this.addToCart(itemID, quantty, productData);
					}
					else if(function == 2) {
						itemID = inp.nextLine();
						this.removeFromCart(itemID, productData);
					}
					else if (function == 3) {
						itemID = inp.nextLine();
					quantty = inp.nextInt();
						this.reduceQuanttyFromCart(itemID, quantty, productData);
					}
					else if (function == 4) {
						break;
					} 
					else if (function == 5) {
						this.cancelOrder(); 
						return;
					}
				} 
			}
			catch (InputMismatchException e) {
				inp.next();
			}
		}
		
		if (productList.isEmpty()) return;
		
		/*Call the paymentProcess is here*/
		Payment payMent = new Payment(totalSellingPrice);
		if (payMent.processPayment()) {
			/*Update orderID as paymentID*/							// Contact Hoang
			
			this.updateID(payMent.getID());
			this.setPrice(price);
            
			//Reduce quantity of item in product.csv after getorder ---is this part belong to Son?---
			for (Map.Entry<String, Integer> i : this.productList.entrySet()) {
				Product item = employee.searchProduct(i.getKey(), productData);
				
				employee.editProduct(item, -i.getValue()); 				// Contact Nguyet Anh 
			}
			this.saveToCSV();
		
			//Save the data to order.csv
		}
		
		public void saveToCSV() {
			String fileName = "order.csv";
			FileWriter outFile = null;
			try {
				outFile = new FileWriter(fileName, true);
				for (Map.Entry<String, Integer> item : this.productList.entrySet()) {
					outFile.append(String.join(",", this.getID(), item.getKey(), item.getValue().toString()));
					outFile.append("\n");
				}
				System.out.println("CSV file successfully written!");
				outFile.close();
			}
			catch (Exception e) {
				System.err.println("Error writing to file: " + e.getMessage());
	            e.printStackTrace(); 
			}
			finally {
				try {
					if (outFile != null) {
						outFile.flush();
						outFile.close();
					}
				} 
				catch (IOException e) {
					System.err.println("Error closing the file writer: " + e.getMessage());
	                e.printStackTrace();
				}
			}
		}
	}
}
