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
    private JLabel totalBuyLabel;
    private JLabel totalSellLabel;
    private JLabel totalExpenseLabel;
    private JLabel netRevenueLabel;
	private ManagerFrame managerFrame;

	public StoreFrame(Manager manager, ManagerFrame managerFrame) {
	    this.managerFrame = managerFrame;
	    this.manager = manager;
	    setTitle("Revenue and Expense");
	    setSize(1400, 800);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JComboBox<String> monthComboBox = new JComboBox<>(new String[] {
	        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
	    });
	    JTextField yearField = new JTextField(4);
	    JButton filterButton = new JButton("Lọc");
	    filterButton.addActionListener(e -> {
	        String selectedMonth = (String) monthComboBox.getSelectedItem();
	        String selectedYear = yearField.getText();
	        filterData(selectedYear + selectedMonth);
	    });

	    importTable = new JTable();
	    exportTable = new JTable();
	    expenseTable = new JTable();

	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BorderLayout());

	    JPanel tablePanel = new JPanel();
	    tablePanel.setLayout(new GridLayout(1, 3));

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

	    mainPanel.add(filterPanel, BorderLayout.NORTH);
	    mainPanel.add(tablePanel, BorderLayout.CENTER);

	    JPanel summaryPanel = new JPanel();
	    summaryPanel.setLayout(new GridLayout(2, 3));

	    totalBuyLabel = new JLabel("Total Buy: 0.0");
	    totalSellLabel = new JLabel("Total Sell: 0.0");
	    totalExpenseLabel = new JLabel("Total Expense: 0.0");
	    netRevenueLabel = new JLabel("Revenue: 0.0");

	    Font labelFont = new Font("Arial", Font.BOLD, 16);
	    totalBuyLabel.setFont(labelFont);
	    totalSellLabel.setFont(labelFont);
	    totalExpenseLabel.setFont(labelFont);
	    netRevenueLabel.setFont(labelFont);

	    JButton backButton = new JButton("Back");
	    JButton shopHistoryButton = new JButton("Shop History");
	    JButton logHistoryButton = new JButton("Log History");
	    JButton payHistoryButton = new JButton("Payment History");

	    backButton.addActionListener(e -> back());    
	    shopHistoryButton.addActionListener(e -> showShopHistory());
	    logHistoryButton.addActionListener(e -> showLogHistory());     
	    payHistoryButton.addActionListener(e -> showPayHistory());

	    summaryPanel.add(totalBuyLabel);
	    summaryPanel.add(totalSellLabel);
	    summaryPanel.add(totalExpenseLabel);
	    summaryPanel.add(netRevenueLabel);
	    summaryPanel.add(backButton);
	    summaryPanel.add(shopHistoryButton);
	    summaryPanel.add(logHistoryButton);
	    summaryPanel.add(payHistoryButton);

	    mainPanel.add(summaryPanel, BorderLayout.SOUTH);

	    JScrollPane scrollPane = new JScrollPane(mainPanel);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	    setContentPane(scrollPane);

	    setVisible(true);
	}


    private void filterData(String selectedDatePrefix) {
        Map<String, double[]> incomeSummary = new HashMap<>();
        Map<String, double[]> outcomeSummary = new HashMap<>();
        Map<String, Double> expenseDetails = new HashMap<>();

        manager.loadData(Main.ordersFilePath, incomeSummary, outcomeSummary, selectedDatePrefix);
        manager.loadExpenses(Main.expensesFilePath, expenseDetails, selectedDatePrefix);
        
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
                Map<String, double[]> sortedData = new TreeMap<>(data);

        String[] columnNames = {"Date", "Total Amount", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Map.Entry<String, double[]> entry : sortedData.entrySet()) {
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
        totalExpenseLabel.setText(String.format("Total Expense: %.2f", totalExpenses));
        netRevenueLabel.setText(String.format("Revenue: %.2f", netRevenue));
    }
    private void back() {
        this.setVisible(false);
        managerFrame.setVisible(true);
    }
    private void showShopHistory() {
        String[] columns = {"ID", "Name", "Price", "Quantity" ,"Input Price", "Employee"};
        List<Object[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Main.ordersFilePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < columns.length) {
                    String[] paddedValues = new String[columns.length];
                    System.arraycopy(values, 0, paddedValues, 0, values.length);
                    for (int i = values.length; i < columns.length; i++) {
                        paddedValues[i] = ""; 
                    }
                    values = paddedValues;
                }

                String id = values[0].isEmpty() ? "" : values[0];
                String name = values[2].isEmpty() ? "" : values[2];
                double price = values[3].isEmpty() ? 0.0 : Double.parseDouble(values[3]);
                int quantity = values[4].isEmpty() ? 0 : Integer.parseInt(values[4]);
                double inputPrice = values[5].isEmpty() ? 0.0 : Double.parseDouble(values[5]);
                String employee = (values.length > 13 && !values[13].isEmpty()) ? values[13] : "";


                data.add(new Object[]{id, name, price, quantity, inputPrice, employee});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> employeeNames = new ArrayList<>();
        employeeNames.add("All");
        try (BufferedReader empReader = new BufferedReader(new FileReader(Main.userFilePath))) {
            String empLine;
            empReader.readLine(); 
            while ((empLine = empReader.readLine()) != null) {
                String[] empValues = empLine.split(",");
                if (empValues.length > 3 && empValues[4].equals("Employee")) {
                    employeeNames.add(empValues[3]);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object[][] dataArray = new Object[data.size()][columns.length];
        data.toArray(dataArray);
        DefaultTableModel tableModel = new DefaultTableModel(dataArray, columns);
        JTable shopHistoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(shopHistoryTable);
        JFrame tableFrame = new JFrame("Shop History");
        tableFrame.setSize(800, 600);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel(new FlowLayout());
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"All", "im", "ex"});
        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
            "Tất cả", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        });
        JTextField yearField = new JTextField(5);
        JComboBox<String> employeeComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));
        JButton filterButton = new JButton("Filter");
        JButton exitButton = new JButton("Exit");
        northPanel.add(new JLabel("Type:"));
        northPanel.add(typeComboBox);
        northPanel.add(new JLabel("Month:"));
        northPanel.add(monthComboBox);
        northPanel.add(new JLabel("Year:"));
        northPanel.add(yearField);
        northPanel.add(new JLabel("Employee:"));
        northPanel.add(employeeComboBox);
        northPanel.add(filterButton);

        tableFrame.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.add(exitButton);
        tableFrame.add(southPanel, BorderLayout.SOUTH);

        tableFrame.add(scrollPane, BorderLayout.CENTER);
        tableFrame.setVisible(true);
        
        exitButton.addActionListener(e -> tableFrame.dispose());


        filterButton.addActionListener(e -> {
            String selectedType = typeComboBox.getSelectedItem().toString();
            String selectedMonth = monthComboBox.getSelectedItem().toString();
            String selectedYear = yearField.getText().trim();
            String selectedEmployee = employeeComboBox.getSelectedItem().toString();

            applyOrderFilters(tableModel, data, selectedType, selectedMonth, selectedYear, selectedEmployee);
        });
    }
	private void applyOrderFilters(DefaultTableModel tableModel, List<Object[]> data, String type, String month, String year, String employee) {
        tableModel.setRowCount(0);

        for (Object[] row : data) {
            String id = row[0].toString();
            String employeeName = row[5].toString();

            boolean matchesType = type.equals("All") || id.startsWith(type);
            boolean matchesMonth = month.equals("All") || id.substring(6, 8).equals(month);
            boolean matchesYear = year.isEmpty() || id.substring(2, 6).equals(year);
            boolean matchesEmployee = employee.equals("All") || employeeName.equalsIgnoreCase(employee);
            if (matchesType && matchesMonth && matchesYear && matchesEmployee) {
                tableModel.addRow(row);
            }
        }
    }
	private void showLogHistory() {
	    String[] columns = {"Name", "Login Time", "Logout Time", "Duration"};
	    List<Object[]> data = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(Main.logFilePath))) {
	        String line;
	        reader.readLine();
	        while ((line = reader.readLine()) != null) {
	            String[] values = line.split(",");
	            if (values.length < columns.length) {
	                String[] paddedValues = new String[columns.length];
	                System.arraycopy(values, 0, paddedValues, 0, values.length);
	                for (int i = values.length; i < columns.length; i++) {
	                    paddedValues[i] = "";
	                }
	                values = paddedValues;
	            }
	            String name = values[0].isEmpty() ? "" : values[0];
	            String loginTime = values[1].isEmpty() ? "" : values[1];
	            String logoutTime = values[2].isEmpty() ? "" : values[2];
	            int duration = values[3].isEmpty() ? 0 : Integer.parseInt(values[3]);

	            data.add(new Object[]{name, loginTime, logoutTime, duration});
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    List<String> employeeNames = new ArrayList<>();
	    employeeNames.add("All");
	    try (BufferedReader empReader = new BufferedReader(new FileReader(Main.userFilePath))) {
	        String empLine;
	        empReader.readLine();
	        while ((empLine = empReader.readLine()) != null) {
	            String[] empValues = empLine.split(",");
	            if (empValues.length > 3 && empValues[4].equals("Employee")) {
	                employeeNames.add(empValues[3]);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    Object[][] dataArray = new Object[data.size()][columns.length];
	    data.toArray(dataArray);
	    DefaultTableModel tableModel = new DefaultTableModel(dataArray, columns);
	    JTable logHistoryTable = new JTable(tableModel);
	    JScrollPane scrollPane = new JScrollPane(logHistoryTable);
	    JFrame tableFrame = new JFrame("Log History");
	    tableFrame.setSize(800, 600);
	    tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    tableFrame.setLayout(new BorderLayout());

	    JPanel northPanel = new JPanel(new FlowLayout());
	    JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
	        "All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
	    });
	    JTextField yearField = new JTextField(5);
	    JComboBox<String> employeeComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));
	    JButton filterButton = new JButton("Filter");
	    JButton exitButton = new JButton("Exit");
;
	    northPanel.add(new JLabel("Month:"));
	    northPanel.add(monthComboBox);
	    northPanel.add(new JLabel("Year:"));
	    northPanel.add(yearField);
	    northPanel.add(new JLabel("Employee:"));
	    northPanel.add(employeeComboBox);
	    northPanel.add(filterButton);

	    tableFrame.add(northPanel, BorderLayout.NORTH);

	    JPanel southPanel = new JPanel();
	    southPanel.add(exitButton);
	    tableFrame.add(southPanel, BorderLayout.SOUTH);

	    tableFrame.add(scrollPane, BorderLayout.CENTER);
	    tableFrame.setVisible(true);
	    exitButton.addActionListener(e -> tableFrame.dispose());

	    filterButton.addActionListener(e -> {
	        String selectedMonth = monthComboBox.getSelectedItem().toString();
	        String selectedYear = yearField.getText().trim();
	        String selectedEmployee = employeeComboBox.getSelectedItem().toString();
	        String dateFilter = selectedYear.isEmpty() ? "All" : selectedYear;
	        if (!selectedMonth.equals("All")) {
	            dateFilter = dateFilter + "/" + selectedMonth; 
	        }
	        applyLogFilter(tableModel, data,  dateFilter, selectedEmployee);
	    });
	}
	private void applyLogFilter(DefaultTableModel tableModel, List<Object[]> data, String date, String employee) {
	    tableModel.setRowCount(0);

	    for (Object[] row : data) {
	        String logDate = row[1].toString();
	        String employeeName = row[0].toString();  

	        boolean matchesDate = date.equals("All") || logDate.startsWith(date); 

	        boolean matchesEmployee = employee.equals("All") || employeeName.equalsIgnoreCase(employee);
	        if (matchesDate && matchesEmployee) {
	            tableModel.addRow(row);
	        }
	    }
	}
	private void showPayHistory() {
	    String[] columns = {"ID", "Employee Name", "Payment Method", "Status", "Total Amount", "Card Number", "Reference ID"};
	    List<Object[]> data = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(Main.paymentFilePath))) {
	        String line;
	        reader.readLine();
	        while ((line = reader.readLine()) != null) {
	            String[] values = line.split(",");
	            if (values.length < columns.length) {
	                String[] paddedValues = new String[columns.length];
	                System.arraycopy(values, 0, paddedValues, 0, values.length);
	                for (int i = values.length; i < columns.length; i++) {
	                    paddedValues[i] = "";  
	                }
	                values = paddedValues;
	            }

	            String id = values[0];
	            String name = values[1];
	            String method = values[2];
	            String status = values[3];
	            String totalamount =(values[4]);
	            String card =  values[5];
	            String refID = values[6];

	            data.add(new Object[]{id, name, method, status, totalamount,card,refID});
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    List<String> employeeNames = new ArrayList<>();
	    employeeNames.add("All");
	    try (BufferedReader empReader = new BufferedReader(new FileReader(Main.userFilePath))) {
	        String empLine;
	        empReader.readLine();
	        while ((empLine = empReader.readLine()) != null) {
	            String[] empValues = empLine.split(",");
	            if (empValues.length > 3 && empValues[4].equals("Employee")) {
	                employeeNames.add(empValues[3]);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    Object[][] dataArray = new Object[data.size()][columns.length];
	    data.toArray(dataArray);
	    DefaultTableModel tableModel = new DefaultTableModel(dataArray, columns);
	    JTable shopHistoryTable = new JTable(tableModel);
	    JScrollPane scrollPane = new JScrollPane(shopHistoryTable);
	    JFrame tableFrame = new JFrame("Shop History");
	    tableFrame.setSize(800, 600);
	    tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    tableFrame.setLayout(new BorderLayout());
	    
	    JPanel northPanel = new JPanel(new FlowLayout());
	    JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"All", "im", "ex"});
	    JComboBox<String> methodComboBox = new JComboBox<>(new String[] {"All", "cash" , "card"});
	    JComboBox<String> statusComboBox = new JComboBox<>(new String[] {"All", "true", "false"});
	    JComboBox<String> monthComboBox = new JComboBox<>(new String[] {
	        "All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
	    });
	    JTextField yearField = new JTextField(5);
	    JComboBox<String> employeeComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));
	    JButton filterButton = new JButton("Filter");
	    JButton exitButton = new JButton("Exit");
	    
	    northPanel.add(new JLabel("Type:"));
	    northPanel.add(typeComboBox);
	    northPanel.add(new JLabel("Month:"));
	    northPanel.add(monthComboBox);
	    northPanel.add(new JLabel("Year:"));
	    northPanel.add(yearField);
	    northPanel.add(new JLabel("Employee:"));
	    northPanel.add(employeeComboBox);
	    northPanel.add(new JLabel("Status :"));
	    northPanel.add(statusComboBox);
	    northPanel.add(new JLabel("Method :"));
	    northPanel.add(methodComboBox);
	    northPanel.add(filterButton);

	    tableFrame.add(northPanel, BorderLayout.NORTH);
	    JPanel southPanel = new JPanel();
	    southPanel.add(exitButton);
	    tableFrame.add(southPanel, BorderLayout.SOUTH);

	    tableFrame.add(scrollPane, BorderLayout.CENTER);
	    tableFrame.setVisible(true);

	    exitButton.addActionListener(e -> tableFrame.dispose());

	    filterButton.addActionListener(e -> {
	        String selectedType = typeComboBox.getSelectedItem().toString();
	        String selectedMonth = monthComboBox.getSelectedItem().toString();
	        String selectedYear = yearField.getText().trim();
	        String selectedEmployee = employeeComboBox.getSelectedItem().toString();
	        String selectedMethod = methodComboBox.getSelectedItem().toString();
	        String selectedStatus = statusComboBox.getSelectedItem().toString();

	        applyPayFilters(tableModel, data, selectedType, selectedMonth, selectedYear, selectedEmployee, selectedMethod, selectedStatus);
	    });
	}

	private void applyPayFilters(DefaultTableModel tableModel, List<Object[]> data, String type, String month, String year, String employee,String method, String status) {
	    tableModel.setRowCount(0);

	    for (Object[] row : data) {
	        String id = row[0].toString();
	        String employeeName = row[5].toString();
	        String method1 = row[2].toString();
	        String status1 = row[3].toString();
	        

	        boolean matchesType = type.equals("All") || id.startsWith(type);
	        boolean matchesMonth = month.equals("All") || id.substring(6, 8).equals(month);
	        boolean matchesYear = year.isEmpty() || id.substring(2, 6).equals(year);
	        boolean matchesEmployee = employee.equals("All") || employeeName.equalsIgnoreCase(employee);
	        boolean matchesStatus = status.equals("All") || status1.equals(status);
	        boolean matchesMethod = method.equals("All") || method1.equals(method);

	        if (matchesType && matchesMonth && matchesYear && matchesEmployee && matchesMethod && matchesStatus) {
	            tableModel.addRow(row);
	        }
	    }
	}

}
