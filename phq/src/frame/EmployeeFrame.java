package frame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.Employee;
import model.OrderExport;
import model.Product;

public class EmployeeFrame extends JFrame {
	private LoginFrame loginFrame;
    private String name; 
    private LocalDateTime loginTime;
    protected static final int WIDTH = 1300;
    protected static final int HEIGHT = 900;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm");
    private JTable productsTable;
    private JTextField searchField;
    private DefaultTableModel tableModel; 
    private Employee employee;
    private OrderExport export = new OrderExport();
    List<Product> products;

    public EmployeeFrame(LoginFrame loginFrame,Employee employee) {
        this.loginFrame = loginFrame;
        this.loginTime = LocalDateTime.now();
        this.employee = employee;
        this.name = employee.getName();
        setSize(WIDTH, HEIGHT);
        setTitle("Employee Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchProduct();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchProduct();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchProduct();
            }
        });
        searchPanel.add(new JLabel("Search: "), gbc);
        gbc.gridx = 1;
        searchPanel.add(searchField, gbc);
        
        headerPanel.add(searchPanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JButton backButton = new JButton("Back");
        JButton addProductButton = new JButton("Add Products");
        JButton removeProductButton = new JButton("Remove Products");
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Check out");
        addProductButton.addActionListener(e -> addProducts());		
        backButton.addActionListener(e -> back());
        removeProductButton.addActionListener(e -> removeProduct());
        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> export.viewCart(products)); 
        checkoutButton.addActionListener(e -> checkout());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProductButton);
        buttonPanel.add(removeProductButton);
        buttonPanel.add(backButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        productsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER); 

        add(mainPanel, BorderLayout.CENTER);
        displayAllProducts();
        products = employee.getProductsFromFile();
    }
    private void addProducts() {
        Object[] options = {"Add by CSV", "Add by hand", "Add by ID"};
        int choice = JOptionPane.showOptionDialog(this,
                "How would you like to add products:",
                "Add Products",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                employee.addProducts(selectedFile);
                displayAllProducts();
                break;}
            case 1:
                employee.addProducts();
                displayAllProducts();
                break;
            default:
                break;
            case 2:
                String id = JOptionPane.showInputDialog("Enter ID :");
            	employee.addProducts(id);
            	displayAllProducts();
        }
    }
    protected void back() {
        LocalDateTime logoutTime = LocalDateTime.now();
        long durationInMinutes = java.time.Duration.between(loginTime, logoutTime).toMinutes();
        logSession(logoutTime, durationInMinutes);
        loginFrame.setVisible(true);
        dispose();
    }

    private void logSession(LocalDateTime logoutTime, long durationInMinutes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.logFilePath, true))) {
            String formattedLoginTime = loginTime.format(dtf);
            String formattedLogoutTime = logoutTime.format(dtf);
            String logEntry = String.format("%s,%s,%s,%d", name, formattedLoginTime, formattedLogoutTime, durationInMinutes);
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error logging session data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void displayAllProducts() {
        String[] columnNames = { "ID", "Type", "Name", "Price", "Quantity", "Input Price ", "Brand", "Suit Age", "Material", "Author", "ISBN", "Publication Year", "Publisher" };
        tableModel = new DefaultTableModel(columnNames, 0);
        productsTable.setModel(tableModel);


        int[] columnWidths = {20, 60, 300, 50, 50, 50, 100, 60, 75, 100, 100, 125, 225}; 
        productsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        productsTable.setFillsViewportHeight(true); 

        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = productsTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]); 
            column.setResizable(true); 
        }
        List<Product> products = employee.getProductsFromFile();
        for (Product product : products) {
            String[] rowData = employee.getProductRowData(product);
            tableModel.addRow(rowData);
        }
    }
    //doi ten nua ne
    protected void searchProduct() {
        String query = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0); 

        if (query.isEmpty()) {
            displayAllProducts();
            return;
        }
        List<Product> products = employee.getProductsFromFile();
        List<Product> filteredProducts = products.stream()
            .filter(product -> employee.productMatchesQuery(product, query))
            .collect(Collectors.toList());

        for (Product product : filteredProducts) {
            String[] rowData = employee.getProductRowData(product);
            tableModel.addRow(rowData);
        }
    }
    private void removeProduct() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select one product to remove.");
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String type = (String) tableModel.getValueAt(selectedRow, 1);
        String name = (String) tableModel.getValueAt(selectedRow, 2);

        String price = (String)(tableModel.getValueAt(selectedRow, 3).toString());
        String quantity = (String)(tableModel.getValueAt(selectedRow, 4).toString());
        String inputPrice = (String)(tableModel.getValueAt(selectedRow, 5).toString());

        String brand = (String) tableModel.getValueAt(selectedRow, 6);
        String suitAge = (String) tableModel.getValueAt(selectedRow, 7);
        String material = (String) tableModel.getValueAt(selectedRow, 8);
        String author = (String) tableModel.getValueAt(selectedRow, 9);
        String isbn = (String) tableModel.getValueAt(selectedRow, 10);

        String publicationYear = tableModel.getValueAt(selectedRow, 11) != null
            ? (String)(tableModel.getValueAt(selectedRow, 11).toString())
            : "";

        String publisher = (String) tableModel.getValueAt(selectedRow, 12);


        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure to remove " + name + "?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            String[] data = new String[] {
                id,type, name, price,quantity,inputPrice,brand,suitAge, material,author,isbn,publicationYear,publisher
            };
            Product rm = employee.createProductFromData(data);
            employee.updateProductChange(rm,"delete");
            JOptionPane.showMessageDialog(this, name + " removed successfully.");
            tableModel.removeRow(selectedRow);
        }
    }

    private void addToCart() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) tableModel.getValueAt(selectedRow, 0);
            int productQuantity = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 4));
            String input = JOptionPane.showInputDialog("Enter quantity of " + (String) tableModel.getValueAt(selectedRow, 2) + " :");

            if (input == null) {
                return;
            }
            int quantity = 0;
            try {
                quantity = Integer.parseInt(input);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must bigger than 0.");
                    return;
                }
                if (quantity > productQuantity) {
                    quantity = productQuantity;
                    JOptionPane.showMessageDialog(this, "The quantity you entered bigger than available quantity: "+ productQuantity + "\n" + "Add all available quantity to cart.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
                return;
            }

            export.addToCart(productId, quantity,employee.getProductsFromFile());
        } else {
            JOptionPane.showMessageDialog(this, "Select 1 valid product to adđ to cart .");
        }
    }

    private void checkout() {
    	export.checkout(employee);
    	displayAllProducts();
    }
}
