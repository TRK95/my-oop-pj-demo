package model;

import java.util.List;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OrderImport extends Order{
    private List<Product> productsList;
    private String ID;
    private double price = 0;
    private Employee activEmployee;

    public OrderImport(){}

    public OrderImport(Employee e) {
        super(e);
    }

    public OrderImport(List<Product> l, Employee e) {
        this.productsList = l;
        this.activEmployee = e;
    }

    public String getID() {
        return this.ID;
    }

    public List<Product> getProductList() {
        return this.productsList;
    }

    private void setID(String newID) {
        this.ID = newID;
    }

    public void getOrderImport() {
        if (productsList != null){
            for (Product item : productsList) {
                price += item.getInputPrice();
            }
        }

        Payment paymentMethod = new Payment(price, "import", this.activEmployee.getName());
        if (!paymentMethod.processPayment()) {
            this.setID(paymentMethod.getID());
            this.setPrice(price);
            super.saveToCSV(this.ID, this.productsList, this.activEmployee.getName());

        }
    }
    
    @Override
    public void getBill() {
        System.out.println("Order Import List Here!!!");

        String format =  "| %-10s | %-30s | %-10d | %-10.2f |%n";
        System.out.format("+------------+--------------------------------+------------+------------+%n");
        System.out.format("| ID         | Name                           | Quantity   | Price      |%n");
        System.out.format("+------------+--------------------------------+------------+------------+%n");

        for (int i = 0; i < this.productsList.size(); i++) {
            Product temp = this.productsList.get(i);
            System.out.format(format, temp.getId(), temp.getName(), temp.getQuantity(), temp.getInputPrice() * temp.getQuantity());
        }
        System.out.format("+------------+--------------------------------+------------+------------+%n");
    }
}
