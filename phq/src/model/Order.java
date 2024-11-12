package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class Order {
    private double price = 0;
	private String id;
	private Employee activeEployee ;
	
	public Order() {
	}

	public Order(String id) {
		this.id = id;
	}
	
    public Order(Employee e) {
        this.activeEployee = e;
    }
	
	public String getID() {
		return this.id;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	private void setID(String id) {
		this.id = id;
	}
	
	public void setPrice(double p) {
		this.price = p;
	}

    // public void setEmployee(Employee e) {
    //     this.employee = e;
    // }

	
	public abstract void getBill();

	public void saveToCSV(String genId, List<Product> productsList, String employeeName) {
        String filePath = "/media/khanhtty/551473877464395A/MyPjOOP/phq/src/orders.csv";
        FileWriter desFile = null;

        try {
            desFile = new FileWriter(filePath, true);

            for (int i = 0; i < productsList.size(); i++) {
                Product temp = productsList.get(i);
                if (temp instanceof Book) {
                    Book book = (Book) temp;
                    desFile.append(genId + "," 
                                    + "book," 
                                    + temp.getName() + "," 
                                    + String.valueOf(temp.getPrice()) + "," 
                                    + String.valueOf(temp.getQuantity()) + "," 
                                    + String.valueOf(temp.getInputPrice()) + ","  
                                    
                                    
                                    
                                    
                                    
                                    + ","
                                    + ","
                                    + ","
                                    + book.getAuthor() + ","
                                    + book.getIsbn() + ","
                                    + book.getPublicationYear() + ","
                                    + book.getPublisher() + "," 
									+ employeeName + "\n");
                }
                else if (temp instanceof Toy) {
                    Toy toy = (Toy) temp;
                    desFile.append(genId + "," 
                                    + "toy," 
                                    + temp.getName() + "," 
                                    + String.valueOf(temp.getPrice()) + "," 
                                    + String.valueOf(temp.getQuantity()) + "," 
                                    + String.valueOf(temp.getInputPrice()) + "," 
                                    + toy.getBrand() + ","
                                    + toy.getSuitAge() + ","
                                    + toy.getMaterial() + ","
                                    + ","
                                    + ","
                                    + "," 
									+ "," 
									+ employeeName
                                    + "\n");
                }
                else if (temp instanceof Stationery) {
                    Stationery station = (Stationery) temp;
                    desFile.append(genId + "," 
                                    + "stationery," 
                                    + temp.getName() + "," 
                                    + String.valueOf(temp.getPrice()) + "," 
                                    + String.valueOf(temp.getQuantity()) + "," 
                                    + String.valueOf(temp.getInputPrice()) + ","  
                                    + station.getBrand() + ","
                                    + ","
                                    + station.getMaterial() + ","
                                    + ","
                                    + ","
                                    + ","
									+ "," 
									+ employeeName
                                    + "\n");
                }
            }

            System.out.println("Saved to CSV successfully.");
        } 
        catch (Exception e) {
            System.out.println("Error in order.csv file.");
            e.printStackTrace();
        }
        finally {
            try {
                desFile.flush();
                desFile.close();
            } catch (IOException e) {
                System.out.println("Error while closing file.");
                e.printStackTrace();
            }
        }
    }
	
}
