package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.Thread;
public class OrderExport extends Order{
    private Map<String, Integer> orderList = new HashMap<String, Integer>();
	List<Product> productsList = new ArrayList<>();
	private double price = 0;
	private String id;
	private Employee activEmployee = new Employee() ;
	
	public OrderExport() {
	}

	public OrderExport(String id) {
		this.id = id;
	}
	
    public OrderExport(Employee e) {
        this.activEmployee = e;
    }

	public OrderExport(List<Product> e) {
        this.productsList= e;
    }

	public OrderExport(String id, Map<String, Integer> orderList) {
		this.id = id;
		this.orderList = orderList;
	}
	
	public String getID() {
		return this.id;
	}
	
	public List<Product> getProductsList() {
		return this.productsList;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setPrice(long p) {
		this.price = p;
	}

    public void setEmployee(Employee e) {
        this.activEmployee = e;
    }

	public Product searchProductById(String id, List<Product> productsData) {
        for (Product product : productsData) {
            if (product.getId().equals(id)) {
                if (product instanceof Book) {
                    return (Book) product;
                } else if (product instanceof Toy) {
                    return (Toy) product;
                } else if (product instanceof Stationery) {
                    return (Stationery) product;
                }
            }
        }
        return null; 
    }

	public int findProductInCart(String idProdNeedToFind) {
		for (int ithItem = 0; ithItem < this.productsList.size(); ithItem++) {
			if (idProdNeedToFind.equals(this.productsList.get(ithItem).getId()))
				return ithItem;
		} 
		return this.productsList.size();
	}

    private void addToCart(String proID, int quantty, List<Product> productsData) {
		Product item = this.searchProductById(proID, productsData);
		if (item == null) {
			System.out.println("Product does not exist");	
			return;
		}
		item.setQuantity(quantty);
		if (this.orderList.containsKey(proID)) {
			
			this.productsList.get(findProductInCart(proID)).setQuantity(orderList.get(proID) + quantty);
			this.orderList.put(proID, orderList.get(proID) + quantty);
		}
		else {
			this.orderList.put(proID, quantty);
		 	this.productsList.add(item);
		}
		this.price += item.getInputPrice() * quantty;
		System.out.println(this.productsList.size());
		System.out.println("Item is added to cart.");
	}
	
	private void removeFromCart(String proID, List<Product> productData) {
		this.orderList.remove(proID);
		int indexOfRmProd = findProductInCart(proID);
		this.productsList.remove(indexOfRmProd);
		System.out.println("Item is removed from cart");
	}
	
	private void reduceQuanttyFromCart(String proID, int quantty, List<Product> productData) {
		if (quantty < this.orderList.get(proID))
		this.addToCart(proID, -(this.orderList.get(proID) - quantty), productData);
		else {
			this.orderList.remove(proID);
		}
	}

    private void cancelOrder() {
		this.orderList.clear();
		this.price = 0;
		this.productsList.clear();
	}
	
	public void takeOrder() {
		List<Product> productData = this.activEmployee.getProductsFromFile();
		Scanner inp = new Scanner(System.in);
		String itemID = new String();
		int quantty;
		
		
		// Add item to cart by itemID
		
			try {
				while(true) {
					/*
					 * 1. add to cart
					 * 2. remove from card
					 * 3. reduce quantity
					 * 4. end of add to cart
					 * 5. cancel order
					 * */

					System.out.println("1. add an item to cart\n" + //
										"2. remove an item from card\n" + //
												"3. reduce quantity of an item\n" + //
												"4. end of add to cart\n" + //
												
												"5. cancel order\n");
					int function = inp.nextInt();
					inp.nextLine();
					if (function == 1) {
						System.out.print("ID: ");
						itemID = inp.nextLine();
						System.out.print("Quantity: ");
						quantty = inp.nextInt();
						this.addToCart(itemID, quantty, productData);
						
						this.getBill();
					}
					else if(function == 2) {
						if (this.orderList.isEmpty()) {
							System.out.println("Your cart is empty.");
							continue;
						}
						itemID = inp.nextLine();
						this.removeFromCart(itemID, productData);
	
						this.getBill();
					}
					else if (function == 3) {
						itemID = inp.nextLine();
						quantty = inp.nextInt();
						this.reduceQuanttyFromCart(itemID, quantty, productData);
						this.getBill();
					}
					else if (function == 4) {
						break;
					} 
					else if (function == 5) {
						this.cancelOrder(); 
						System.out.println("Your order is cancelled.");
						return;
					}
					
				} 
			}
			catch (InputMismatchException e) {
				inp.next();
			}
		
		
		if (orderList.isEmpty()) {
			System.out.println("Order is cancelled successfully");
			return;
		}
		
		/*Call the paymentProcess is here*/
		Payment payMent = new Payment(this.price, "export", this.activEmployee.getName());
		boolean status = payMent.processPayment();

		if (!status) {
			this.setID(payMent.getID());
			super.saveToCSV(this.getID(), this.productsList, this.activEmployee.getName());
			this.getBill();
		}
	}

	@Override
    public void getBill() {
		if (!this.productsList.isEmpty()){
			System.out.println("Your Order List Is Here!!!");

			String format =  "| %-10s | %-30s | %-10d | %-10.2f |%n";
			String formatTotalPrice =  "| %-56s | %-10.2f |%n";
			System.out.format("+------------+--------------------------------+------------+------------+%n");
			System.out.format("| ID         | Name                           | Quantity   | Price      |%n");
			System.out.format("+------------+--------------------------------+------------+------------+%n");

			for (int i = 0; i < this.productsList.size(); i++) {
				Product temp = this.productsList.get(i);
					System.out.format(format, temp.getId(), temp.getName(), temp.getQuantity(), temp.getInputPrice()*temp.getQuantity());
					
			}
			System.out.format("+------------+--------------------------------+------------+------------+%n");
			System.out.format(formatTotalPrice, "Total price:", this.price);
			System.out.format("+------------+--------------------------------+------------+------------+%n");
    	}
		}

}
