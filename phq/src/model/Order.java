package model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import frame.Main;

public abstract class Order {
    private double price = 0;
	private String id;
	
	public Order() {
	}

	public Order(String id) {
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(double p) {
		this.price = p;
	}
	
	public abstract void getBill();

	public void saveToCSV(String genId, List<Product> productsList, String employeeName) {
        FileWriter desFile = null;

        try {
            desFile = new FileWriter(Main.ordersFilePath, true);

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
