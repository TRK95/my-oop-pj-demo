package employee;

import java.util.*;
import product.Product;
import order.*;

public class Employee {
    private String id;
    private String name;
    private boolean status;

    public Employee() {

    }

    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Employee(String id, String name, String status) {

    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean getStatus() {
        return this.status;
    }

    public Product searchProduct(String prodID, List<Product> productData) {
        for (Product item : productData) {
            if (prodID.equals(item.getId())) {
                return item;
            }   
        }
        return null;
    }

    public void editProduct(Product product, int quantity) {
        product.setQuantity(product.getQuantity() + quantity);
    }

    public OrderImport takeOrderImport() {
        return new OrderImport(this);
    }

    public OrderExport takeOrderExport() {
        return new OrderExport(this);
    }
}
