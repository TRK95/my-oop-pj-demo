package model;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.*;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OrderExport extends Order{
	List<Product> productsList = new ArrayList<>();
	private double price = 0;
	private String id;
	
	public OrderExport() {
	}

	public OrderExport(String id) {
		this.id = id;
	}
	

	public OrderExport(List<Product> e) {
        this.productsList= e;
    }

	
	public String getID() {
		return id;
	}
	
	public List<Product> getProductsList() {
		return productsList;
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
	
	public void addToCart(String proID, int quantity, List<Product> productsData) {
	    Product item = searchProductById(proID, productsData);
	    if (item == null) {
	        return;
	    }

	    boolean productExists = false;
	    for (Product existingProduct : productsList) {
	        if (existingProduct.getName().equals(item.getName())) {
	            existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
	            productExists = true;
	            break;
	        }
	    }

	    if (!productExists) {
	        item.setQuantity(quantity);
	        productsList.add(item);
	    }
	}
	
	public void viewCart(List<Product> productsData) {
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    JTextField[] quantityFields = new JTextField[productsList.size()];
	        for (int i = 0; i < productsList.size(); i++) {
	        Product product = productsList.get(i);
	        JPanel productPanel = new JPanel();
	        productPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

	        JLabel productLabel = new JLabel(product.getName() + " - Price: " + product.getPrice());
	        productPanel.add(productLabel);

	        JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()), 5);
	        quantityFields[i] = quantityField;
	        productPanel.add(new JLabel("Quantity:"));
	        productPanel.add(quantityField);

	        double total = product.getQuantity() * product.getPrice();
	        JLabel totalLabel = new JLabel("Total: " + total);
	        productPanel.add(totalLabel);

	        panel.add(productPanel);
	    }

	    double totalCartPrice = productsList.stream()
	        .mapToDouble(p -> p.getQuantity() * p.getPrice())
	        .sum();

	    JLabel cartTotalLabel = new JLabel("Cart Total Price: " + totalCartPrice);
	    cartTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel.add(cartTotalLabel);

	    int option = JOptionPane.showConfirmDialog(null, panel, "View and Edit Cart", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    if (option == JOptionPane.OK_OPTION) {
	        Iterator<Product> iterator = productsList.iterator();
	        int index = 0;

	        while (iterator.hasNext()) {
	            Product product = iterator.next();
	            try {
	                int newQuantity = Integer.parseInt(quantityFields[index].getText().trim());

	                for (Product exist : productsData) {
	                    if (product.getName().equals(exist.getName())) {
	                        if (newQuantity > exist.getQuantity()) {
	                            newQuantity = exist.getQuantity();
	                            JOptionPane.showMessageDialog(null, "Only " + exist.getQuantity() + " units of " + product.getName() + " are available. Quantity adjusted.");
	                        }
	                        break;
	                    }
	                }

	                if (newQuantity == 0) {
	                    iterator.remove();
	                    JOptionPane.showMessageDialog(null, "Removed " + product.getName() + " from the cart.");
	                } else {
	                    product.setQuantity(newQuantity);
	                }
	            } catch (NumberFormatException e) {
	                JOptionPane.showMessageDialog(null, "Invalid quantity for product: " + product.getName());
	            }
	            index++;
	        }
	        
	        JOptionPane.showMessageDialog(null, "Cart updated successfully!" );
	    }
	    else {
	    	productsList.clear();
	    }
	}
	
	public void checkout(Employee employee) {
	    double total = productsList.stream().mapToDouble(p -> p.getQuantity() * p.getPrice()).sum();
	    System.out.print(total);

	    if (total != 0.0) {
	        Payment payment = new Payment(total, "export", employee.getName());
	        boolean status = payment.processPayment();

	        if (status) {
	            setID(payment.getID());
	            super.saveToCSV(getID(), productsList, employee.getName());
	            getBill();
	            employee.removeProductsFromList(productsList);
	            productsList.clear();
	        } else {
	            JOptionPane.showMessageDialog(null, "Payment failed!");
	        }
	    } else {
	        JOptionPane.showMessageDialog(null, "Empty Cart!");
	    }
	}

	@Override
	public void getBill() {
	    if (!this.productsList.isEmpty()) {
	        System.out.println("Your Order List Is Here!!!");

	        String format = "| %-10s | %-30s | %-10d | %-10.2f |%n";
	        String formatTotalPrice = "| %-56s | %-10.2f |%n";
	        System.out.format("+------------+--------------------------------+------------+------------+%n");
	        System.out.format("| ID         | Name                           | Quantity   | Price      |%n");
	        System.out.format("+------------+--------------------------------+------------+------------+%n");

	        double total = 0;

	        for (Product product : this.productsList) {
	            double productTotal = product.getQuantity() * product.getPrice();
	            total += productTotal;
	            System.out.format(format, product.getId(), product.getName(), product.getQuantity(), productTotal);
	        }

	        System.out.format("+------------+--------------------------------+------------+------------+%n");
	        System.out.format(formatTotalPrice, "Total price:", total); // Print the accumulated total
	        System.out.format("+------------+--------------------------------+------------+------------+%n");
	    }
	}


}