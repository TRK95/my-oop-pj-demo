package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Manager;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class StoreFrame extends JFrame {
    private JTable exportTable;
    private JTable importTable;
    private JTable expenseTable;
    private Manager manager;
    private String ordersFilePath = "/media/khanhtty/551473877464395A/MyPjOOP/phq/src/orders.csv";
    private String expensesFilePath = "/media/khanhtty/551473877464395A/MyPjOOP/phq/src/expense.csv";
    private JLabel totalBuyLabel;
    private JLabel totalSellLabel;
    private JLabel totalExpenseLabel;
    private JLabel netRevenueLabel;
	private ManagerFrame managerFrame;

    public StoreFrame(Manager manager, ManagerFrame managerFrame) {
    	this.managerFrame = managerFrame;
        this.manager = manager;
        setTitle("Store Revenue and Expenses");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox<String> monthComboBox = new JComboBox<>(new String[] {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        });
        JTextField yearField = new JTextField(4);
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            String selectedYear = yearField.getText();
            filterData(selectedYear + selectedMonth);
        });

        importTable = new JTable();
        exportTable = new JTable();
        expenseTable = new JTable();

        setLayout(new BorderLayout());
        JPanel tablePanel = new JPanel();
        JPanel importPanel = new JPanel(new BorderLayout());
        importPanel.add(new JLabel("Import Table", SwingConstants.CENTER), BorderLayout.NORTH);
        importPanel.add(new JScrollPane(importTable), BorderLayout.CENTER);

        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.add(new JLabel("Export Table", SwingConstants.CENTER), BorderLayout.NORTH); 
        exportPanel.add(new JScrollPane(exportTable), BorderLayout.CENTER);

        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.add(new JLabel("Expense Table", SwingConstants.CENTER), BorderLayout.NORTH);
        expensePanel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        tablePanel.add(importPanel);
        tablePanel.add(exportPanel);
        tablePanel.add(expensePanel);

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Month:"));
        filterPanel.add(monthComboBox);
        filterPanel.add(new JLabel("Year:"));
        filterPanel.add(yearField);
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(2, 3));

        // Labels
        totalBuyLabel = new JLabel("Total Buy: 0.0");
        totalSellLabel = new JLabel("Total Sell: 0.0");
        totalExpenseLabel = new JLabel("Total Expense: 0.0");
        netRevenueLabel = new JLabel("Net Revenue: 0.0");

        // Set font size for labels
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        totalBuyLabel.setFont(labelFont);
        totalSellLabel.setFont(labelFont);
        totalExpenseLabel.setFont(labelFont);
        netRevenueLabel.setFont(labelFont);

        JButton button1 = new JButton("Back");
        JButton button2 = new JButton("Shop history");
        JButton button3 = new JButton("Log history");
        button1.addActionListener(e -> back());		//back
        button2.addActionListener(e -> showShopHistory());		//lịch sử mua hàng
        button3.addActionListener(e -> back());     // lịch sử làm viêc

        summaryPanel.add(totalBuyLabel);
        summaryPanel.add(totalSellLabel);
        summaryPanel.add(totalExpenseLabel);

        summaryPanel.add(netRevenueLabel);
        summaryPanel.add(button1);
        summaryPanel.add(button2);
        summaryPanel.add(button3);

        add(summaryPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void filterData(String selectedDatePrefix) {
        Map<String, double[]> incomeSummary = new HashMap<>();
        Map<String, double[]> outcomeSummary = new HashMap<>();
        Map<String, Double> expenseDetails = new HashMap<>();

        // Load income and outcome data
        manager.loadData(ordersFilePath, incomeSummary, outcomeSummary, selectedDatePrefix);
        manager.loadExpenses(expensesFilePath, expenseDetails, selectedDatePrefix);
        
        Map<String, Double> employeeSalaries = manager.calculateSalaries(selectedDatePrefix);

        for (Map.Entry<String, Double> entry : employeeSalaries.entrySet()) {
            expenseDetails.put(entry.getKey(), expenseDetails.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
        }

        List<Map.Entry<String, Double>> expenseList = new ArrayList<>(expenseDetails.entrySet());
        expenseList.sort((entry1, entry2) -> {
            boolean isEntry1Salary = entry1.getKey().toLowerCase().contains("salary");
            boolean isEntry2Salary = entry2.getKey().toLowerCase().contains("salary");
            return isEntry1Salary == isEntry2Salary ? 0 : (isEntry1Salary ? 1 : -1);
        });

        Map<String, Double> sortedExpenseDetails = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : expenseList) {
            sortedExpenseDetails.put(entry.getKey(), entry.getValue());
        }

        exportTable.setModel(createTableModel(incomeSummary));
        importTable.setModel(createTableModel(outcomeSummary)); 
        expenseTable.setModel(createExpenseTableModel(sortedExpenseDetails));

        updateSummary(incomeSummary, outcomeSummary, sortedExpenseDetails);
    }

    private DefaultTableModel createTableModel(Map<String, double[]> data) {
        String[] columnNames = {"Date", "Total Quantity", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Map.Entry<String, double[]> entry : data.entrySet()) {
            String date = entry.getKey();
            double[] totals = entry.getValue();
            model.addRow(new Object[]{date, (int) totals[0], totals[1]});
        }

        return model;
    }

    private DefaultTableModel createExpenseTableModel(Map<String, Double> expenseDetails) {
        String[] columnNames = {"Description", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Map.Entry<String, Double> entry : expenseDetails.entrySet()) {
            String description = entry.getKey();
            Double amount = entry.getValue();
            model.addRow(new Object[]{description, amount});
        }

        return model;
    }

    private void updateSummary(Map<String, double[]> incomeSummary, Map<String, double[]> outcomeSummary, Map<String, Double> expenseDetails) {
        double totalBuy = 0.0;
        double totalSell = 0.0;
        double totalExpenses = expenseDetails.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, double[]> entry : outcomeSummary.entrySet()) {
            double[] values = entry.getValue();
            totalBuy += values[1];
        }

        for (Map.Entry<String, double[]> entry : incomeSummary.entrySet()) {
            double[] values = entry.getValue();
            totalSell += values[1];
        }

        double netRevenue = totalSell - totalBuy - totalExpenses;
        totalBuyLabel.setText(String.format("Total Buy: %.2f", totalBuy));
        totalSellLabel.setText(String.format("Total Sell: %.2f", totalSell));
        totalExpenseLabel.setText(String.format("Total Expenses: %.2f", totalExpenses));
        netRevenueLabel.setText(String.format("Net Revenue: %.2f", netRevenue));
    }
    private void back() {
        this.setVisible(false);
        managerFrame.setVisible(true);
    }
    private void showShopHistory() {
        // Define the columns for the table
        String[] columns = {"ID", "Type", "Name", "Price", "Quantity", "InputPrice", "Brand", "SuitAge", "Material", "Author", "ISBN", "PublicationYear", "Publisher", "Employee"};

        // Initialize the data container
        List<Object[]> data = new ArrayList<>();

        // Read the CSV file and load data into 'data' list using BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader("/media/khanhtty/551473877464395A/MyPjOOP/phq/src/orders.csv"))) {
            String line;
            // Skip the header row
            reader.readLine();

            // Read each line of the CSV
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                // Ensure the row has the expected number of columns
                // If a row has fewer columns, pad the missing columns with empty strings
                if (values.length < columns.length) {
                    String[] paddedValues = new String[columns.length];
                    System.arraycopy(values, 0, paddedValues, 0, values.length);

                    // Fill the remaining columns with empty strings
                    for (int i = values.length; i < columns.length; i++) {
                        paddedValues[i] = ""; // Empty string for missing values
                    }
                    values = paddedValues; // Use the padded values for processing
                }

                // Extract each column from the CSV record, filling missing values as necessary
                String id = values[0].isEmpty() ? "" : values[0];
                String type = values[1].isEmpty() ? "" : values[1];
                String name = values[2].isEmpty() ? "" : values[2];
                double price = values[3].isEmpty() ? 0.0 : Double.parseDouble(values[3]);
                int quantity = values[4].isEmpty() ? 0 : Integer.parseInt(values[4]);
                double inputPrice = values[5].isEmpty() ? 0.0 : Double.parseDouble(values[5]);
                String brand = values[6].isEmpty() ? "" : values[6];
                String suitAge = values[7].isEmpty() ? "" : values[7];
                String material = values[8].isEmpty() ? "" : values[8];
                String author = values[9].isEmpty() ? "" : values[9];
                String isbn = values[10].isEmpty() ? "" : values[10];
                int publicationYear = values[11].isEmpty() ? 0 : Integer.parseInt(values[11]);
                String publisher = values[12].isEmpty() ? "" : values[12];
                String employee = values[13].isEmpty() ? "" : values[13]; // Employee field

                // Add the data to the list in the required format
                data.add(new Object[]{id, type, name, price, quantity, inputPrice, brand, suitAge, material, author, isbn, publicationYear, publisher, employee});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the List to a 2D array for JTable
        Object[][] dataArray = new Object[data.size()][columns.length];
        data.toArray(dataArray);

        // Create the table model and set it to the table
        DefaultTableModel tableModel = new DefaultTableModel(dataArray, columns);
        JTable shopHistoryTable = new JTable(tableModel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(shopHistoryTable);

        // Add the scroll pane to the frame or a panel to display the table
        JFrame tableFrame = new JFrame("Shop History");
        tableFrame.setSize(800, 600);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

}
