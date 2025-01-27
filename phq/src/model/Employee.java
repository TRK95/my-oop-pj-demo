package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import frame.EmployeeFrame;


public class Employee extends Person {

    private EmployeeFrame employeeframe;
    private List<Product> newlyAddedProducts;

    public Employee() {}

    public Employee(String id, String username, String password, String name, String role, String phone, String idCard) {
        super(id, username, password, name, role);
        this.newlyAddedProducts = new ArrayList<>();
    }

    public void addProductsFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(employeeframe);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            StringBuilder addedProductsInfo = new StringBuilder("Added Products:\n");

            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length < 13) continue;

                    try {
                        String id = values[0].trim();
                        String type = values[1].trim().toLowerCase();
                        String name = values[2].trim();
                        double price = Double.parseDouble(values[3].trim());
                        int quantity = Integer.parseInt(values[4].trim());
                        double inputPrice = Double.parseDouble(values[5].trim());
                        String brand = values.length > 6 ? values[6].trim() : "";
                        String suitAge = values.length > 7 ? values[7].trim() : "";
                        String material = values.length > 8 ? values[8].trim() : "";
                        String author = values.length > 9 ? values[9].trim() : "";
                        String isbn = values.length > 10 ? values[10].trim() : "";
                        int publicationYear = values.length > 11 ? Integer.parseInt(values[11].trim()) : 0;
                        String publisher = values.length > 12 ? values[12].trim() : "";

                        Product product = createProduct(id, type, name, price, quantity, inputPrice, brand, suitAge, material, author, isbn, publicationYear, publisher);
                        if (product != null) {
                            addProductToList(product);
                            newlyAddedProducts.add(product);

                            OrderImport order = new OrderImport(newlyAddedProducts, this);
                            order.getOrderImport();

                            addedProductsInfo.append(name).append(" - Quantity: ").append(quantity).append(" - Input Price: ").append(inputPrice).append("\n");
                       }
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                }

                JOptionPane.showMessageDialog(employeeframe, addedProductsInfo.toString(), "Products Added Successfully", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(employeeframe, "Error reading from file: " + e.getMessage());
            }
        }
        // Them chuc nang xem lai hoa don truoc khi hoan tat order, in ra, ban co muon xem hoa don khong
        
    }

    public void addProductByHand() {
        int id = getNextProductId();
        String[] types = {"Book", "Toy", "Stationery"};
        String type = (String) JOptionPane.showInputDialog(employeeframe, "Select Product Type:", "Product Selection",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (type == null) return;

        String name = JOptionPane.showInputDialog("Product " + id, "Enter Product Name:");
        if (name == null) return;

        String priceStr = JOptionPane.showInputDialog("Product " + id, "Enter Product Price:");
        if (priceStr == null) return; 

        String quantityStr = JOptionPane.showInputDialog("Product " + id, "Enter Quantity:");
        if (quantityStr == null) return;

        String inputPriceStr = JOptionPane.showInputDialog("Product " + id, "Enter Input Price:");
        if (inputPriceStr == null) return;

        if (name != null && priceStr != null && quantityStr != null && type != null) {
            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);
                double inputPrice = Double.parseDouble(inputPriceStr);
                Product product = null;

                switch (type) {
                    case "Book":
                        String author = JOptionPane.showInputDialog(this, "Enter Author:");
                        if (author == null) return; 

                        String publisher = JOptionPane.showInputDialog(this, "Enter Publisher:");
                        if (publisher == null) return;
                        String isbn = JOptionPane.showInputDialog(this, "Enter ISBN:");
                        if (isbn == null) return;

                        int publicationYear = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Publication Year:"));
                        product = new Book(String.valueOf(id), name, price, quantity, inputPrice, author, isbn, publicationYear, publisher);
                        break;
                    case "Toy":
                        String brand = JOptionPane.showInputDialog(this, "Enter Brand:");
                        if (brand == null) return; 

                        String material = JOptionPane.showInputDialog(this, "Enter Material:");
                        if (material == null) return;

                        String suitAge = JOptionPane.showInputDialog(this, "Enter Suitable Age:");
                        if (suitAge == null) return;

                        product = new Toy(String.valueOf(id), name, price, quantity, inputPrice, brand, suitAge, material);
                        break;
                    case "Stationery":
                        String brandSta = JOptionPane.showInputDialog(this, "Enter Brand:");
                        if (brandSta == null) return;

                        String materialSta = JOptionPane.showInputDialog(this, "Enter Material:");
                        if (materialSta == null) return;

                        product = new Stationery(String.valueOf(id), name, price, quantity, inputPrice, brandSta, materialSta);
                        break;
                    default:
                        JOptionPane.showMessageDialog(employeeframe, "Invalid product type.");
                        return;
                }

                addProductToList(product);
                newlyAddedProducts.add(product);
                JOptionPane.showMessageDialog(employeeframe, "Product added successfully.");
                OrderImport order = new OrderImport(newlyAddedProducts, this);
                order.getOrderImport();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(employeeframe, "Invalid input for price or quantity.");
            }
            
        }
    }

    public List<Product> getProductsFromFile() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/media/khanhtty/551473877464395A/OOPLAB/phq/src/products.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID")) continue;
                String[] values = line.split(",");
                Product product = createProductFromData(values);
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product createProductFromData(String[] data) {
        String id = data[0].trim();
        String type = data[1].trim();
        String name = data[2].trim();
        
        double price = parseDoubleOrDefault(data[3], 0.0);
        int quantity = parseIntOrDefault(data[4], 0);
        double inputPrice = parseDoubleOrDefault(data[5], 0.0);

        String brand = data.length > 6 ? data[6].trim() : "";
        String suitAge = data.length > 7 ? data[7].trim() : "";
        String material = data.length > 8 ? data[8].trim() : "";
        String author = data.length > 9 ? data[9].trim() : "";
        String isbn = data.length > 10 ? data[10].trim() : "";
        int publicationYear = data.length > 11 ? parseIntOrDefault(data[11], 0) : 0;
        String publisher = data.length > 12 ? data[12].trim() : "";

        switch (type.toLowerCase()) {
            case "book":
                return new Book(id, name, price, quantity, inputPrice, author, isbn, publicationYear, publisher);
            case "stationery":
                return new Stationery(id, name, price, quantity, inputPrice, brand, material);
            case "toy":
                return new Toy(id, name, price, quantity, inputPrice, brand, suitAge, material);
            default:
                throw new IllegalArgumentException("Unknown product type: " + type);
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Product createProduct(String id, String type, String name, double price, int quantity, double inputPrice, 
                                   String brand, String suitAge, String material, String author, 
                                   String isbn, int publicationYear, String publisher) {
        switch (type.toLowerCase()) {
            case "book":
                return new Book(id, name, price, quantity, inputPrice, author, isbn, publicationYear, publisher);
            case "stationery":
                return new Stationery(id, name, price, quantity, inputPrice, brand, material);
            case "toy":
                return new Toy(id, name, price, quantity, inputPrice, brand, suitAge, material);
            default:
                return null;
        }
    }

    private void addProductToList(Product product ) {
        List<Product> products = getProductsFromFile();
        boolean productExists = false;

        for (Product existingProduct : products) {
            if (existingProduct.getName().equals(product.getName())) {
                existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
                existingProduct.setInputPrice(product.getInputPrice());
                existingProduct.setPrice(product.getPrice());
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            product.setId(String.valueOf(getNextProductId()));
            products.add(product);
        }
        writeProductsToFile(products);
    }

    private void removeProductsFromList(List<Product> soldProducts) {
        List<Product> products = getProductsFromFile();
        for (Product soldProduct : soldProducts) {
            for (Product existingProduct : products) {
                if (existingProduct.getName().equals(soldProduct.getName())) {
                    int newQuantity = existingProduct.getQuantity() - soldProduct.getQuantity();
                    
                    if (newQuantity <= 0) {
                        products.remove(existingProduct);
                    } else {
                        existingProduct.setQuantity(newQuantity);
                    }
                    break;
                }
            }
        }
        writeProductsToFile(products);
    }

    private void writeProductsToFile(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/media/khanhtty/551473877464395A/OOPLAB/phq/src/products.csv"))) {
            writer.write("ID,Type,Name,Price,Quantity,InputPrice,Brand,SuitAge,Material,Author,ISBN,PublicationYear,Publisher");
            writer.newLine();
            for (Product product : products) {
                String[] data = getProductRowData(product);
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getProductRowData(Product product) {
        String[] data = new String[13];
        data[0] = product.getId();
        data[1] = product.getClass().getSimpleName().toLowerCase();
        data[2] = product.getName();
        data[3] = String.valueOf(product.getPrice());
        data[4] = String.valueOf(product.getQuantity());
        data[5] = String.valueOf(product.getInputPrice());
        if (product instanceof Book) {
            Book book = (Book) product;
            data[6] = "";
            data[7] = "";
            data[8] = "";
            data[9] = book.getAuthor();
            data[10] = book.getIsbn();
            data[11] = String.valueOf(book.getPublicationYear());
            data[12] = book.getPublisher();
        } else if (product instanceof Stationery) {
            Stationery stationery = (Stationery) product;
            data[6] = stationery.getBrand();
            data[7] = "";
            data[8] = stationery.getMaterial();
            data[9] = "";
            data[10] = "";
            data[11] = "";
            data[12] = "";
        } else if (product instanceof Toy) {
            Toy toy = (Toy) product;
            data[6] = toy.getBrand();
            data[7] = toy.getSuitAge();
            data[8] = toy.getMaterial();
            data[9] = "";
            data[10] = "";
            data[11] = "";
            data[12] = "";
        }
        return data;
    }

    private int getNextProductId() {
        int maxId = 0;
        List<Product> products = getProductsFromFile();
        
        for (Product product : products) {
            try {
                int productId = Integer.parseInt(product.getId());
                if (productId > maxId) {
                    maxId = productId;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format for product: " + product.getName());
            }
        }
        
        return maxId + 1;
    }

    public void removeProduct(String productIdOrName) {
        List<Product> products = getProductsFromFile();
        boolean productFound = false;

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getId().equals(productIdOrName) || product.getName().equalsIgnoreCase(productIdOrName)) {
                products.remove(i);
                productFound = true;
                break;
            }
        }

        if (productFound) {
            writeProductsToFile(products);
            JOptionPane.showMessageDialog(employeeframe, "Product removed successfully.");
        } else {
            JOptionPane.showMessageDialog(employeeframe, "Product not found.");
        }
    }
    public boolean productMatchesQuery(Product product, String query) {
        if (product.getId().toLowerCase().contains(query) || 
            product.getName().toLowerCase().contains(query) || 
            String.valueOf(product.getPrice()).contains(query) ||
            String.valueOf(product.getQuantity()).contains(query) || 
            String.valueOf(product.getInputPrice()).contains(query)) {
            return true;
        }

        if (product instanceof Book) {
            Book book = (Book) product;
            return book.getAuthor().toLowerCase().contains(query) || 
                   book.getIsbn().toLowerCase().contains(query) ||
                   String.valueOf(book.getPublicationYear()).contains(query) ||
                   book.getPublisher().toLowerCase().contains(query);
        } else if (product instanceof Toy) {
            Toy toy = (Toy) product;
            return toy.getBrand().toLowerCase().contains(query) || 
                   toy.getSuitAge().toLowerCase().contains(query) || 
                   toy.getMaterial().toLowerCase().contains(query);
        } else if (product instanceof Stationery) {
            Stationery stationery = (Stationery) product;
            return stationery.getBrand().toLowerCase().contains(query) || 
                   stationery.getMaterial().toLowerCase().contains(query);
        }

        return false;
    }
    
    public void makeOrderFromCustomer() {
        OrderExport order = new OrderExport(this);
        order.takeOrder();
        this.removeProductsFromList(order.getProductsList());
    }
}
