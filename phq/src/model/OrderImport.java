package model;

import java.util.List;


public class OrderImport extends Order{
    private List<Product> productsList;
    private String ID;
    private double price = 0;
    private Employee activEmployee;

    public OrderImport(){}

    public OrderImport(List<Product> l, Employee e) {
        this.productsList = l;
        this.activEmployee = e;
    }

    public String getID() {
        return this.ID;
    }

    public List<Product> getProductList() {
        return productsList;
    }

    private void setID(String newID) {
        this.ID = newID;
    }

    public void getOrderImport() {
        if (productsList != null){
            for (Product item : productsList) {
                price += item.getInputPrice() * item.getQuantity();
            }
        }

        Payment paymentMethod = new Payment(price, "import", activEmployee.getName());
        if (!paymentMethod.processPayment()) {
        	for (Product product : productsList) {
        		activEmployee.updateProductChange(product, "add");
        	}
            setID(paymentMethod.getID());
            setPrice(price);
            super.saveToCSV(ID, productsList, activEmployee.getName());
            getBill();

        }
        productsList.clear();
    }
    
    @Override
    public void getBill() {
        System.out.println("Order Import List Here!!!");

        String format = "| %-10s | %-30s | %-10d | %-10.2f |%n";
        String formatTotalPrice = "| %-56s | %-10.2f |%n";
        System.out.format("+------------+--------------------------------+------------+------------+%n");
        System.out.format("| ID         | Name                           | Quantity   | Price      |%n");
        System.out.format("+------------+--------------------------------+------------+------------+%n");

        double total = 0;

        for (Product product : productsList) {
            double productTotal = product.getQuantity() * product.getInputPrice();
            total += productTotal;
            System.out.format(format, product.getId(), product.getName(), product.getQuantity(), productTotal);
        }

        System.out.format("+------------+--------------------------------+------------+------------+%n");
        System.out.format(formatTotalPrice, "Total price:", total);
        System.out.format("+------------+--------------------------------+------------+------------+%n");
    }

}
