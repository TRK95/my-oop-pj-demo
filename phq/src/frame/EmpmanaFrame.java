package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EmpmanaFrame extends JFrame {
	private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, removeButton, backButton;
	private ManagerFrame managerFrame;

    public EmpmanaFrame(ManagerFrame managerFrame) {
    	this.managerFrame =  managerFrame;
        setTitle("Employee Manage.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Username", "Password", "Name", "Role", "Phone", "ID Card"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        add(actionPanel, BorderLayout.SOUTH);

        addButton = new JButton("Add user");
        actionPanel.add(addButton);
        addButton.addActionListener(e -> addUser());

        editButton = new JButton("Edit user");
        actionPanel.add(editButton);
        editButton.addActionListener(e -> editUser());

        removeButton = new JButton("Remove user");
        actionPanel.add(removeButton);
        removeButton.addActionListener(e -> removeUser());

        backButton = new JButton("Back");
        actionPanel.add(backButton);
        backButton.addActionListener(e -> back());

        loadUsersFromFile(Main.userFilePath);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadUsersFromFile(String users) {
        try (BufferedReader br = new BufferedReader(new FileReader(users))) {
            String line;
            
            tableModel.setRowCount(0);
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length == 7) {
                    String[] row = new String[7];
                    for (int i = 0; i < 7; i++) {
                        row[i] = data[i].trim();
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from file: " + e.getMessage());
        }
    }
    public String generateNewUserID() {
        int maxID = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                String id = (String) tableModel.getValueAt(i, 0);
                int userID = Integer.parseInt(id);
                if (userID > maxID) {
                    maxID = userID;
                }
            } catch (NumberFormatException e) {
            }
        }
        String newID = String.valueOf(maxID + 1);
        return newID;
    }
    private void addUser() {
        String id = generateNewUserID();
        
        String username = JOptionPane.showInputDialog(this, "Enter username :");
        if (username == null) return;

        if (isUsernameExist( username)) {
            JOptionPane.showMessageDialog(this, "Username existed. Retry.");
            return;
        }
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(passwordField);
        passwordPanel.add(new JLabel("Confirm password:"));
        passwordPanel.add(confirmPasswordField);

        int option = JOptionPane.showConfirmDialog(this, passwordPanel, "Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.CANCEL_OPTION) return; 

        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Different password." +"\n" + "Retry.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter name:");
        if (name == null) return;

        String[] roles = {"Employee", "Manager"};
        String role = (String) JOptionPane.showInputDialog(this, "Select Role :", "Role",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
        if (role == null) return;

        String phone = getValidPhoneNumber();
        if (phone == null) return;

        String idCard = getValidIdCard();
        if (idCard == null) return;

        tableModel.addRow(new String[]{id, username, password, name, role, phone, idCard});
        saveUsersToFile();
    }

    private String getValidPhoneNumber() {
        String phone;
        while (true) {
            phone = JOptionPane.showInputDialog(this, "Enter phone number(10 or 11 digits and starts with 0):");
            if (phone == null) return null;
            else if ( phone.matches("^0\\d{9,10}$")) {
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid phone number.");
            }
        }
        return phone;
    }

    private String getValidIdCard() {
        String idCard;
        while (true) {
            idCard = JOptionPane.showInputDialog(this, "Enter idCard(12 digits):");
            if (idCard == null) return null;
            else if ( idCard.matches("^\\d{12}$")) {
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid idCard.");
            }
        }
        return idCard;
    }


    private boolean isUsernameExist( String username) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingUsername = (String) tableModel.getValueAt(i, 1);
            if ( existingUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }
    private void editUser() {
        int selectedRow = employeeTable.getSelectedRow();
        int selectedColumn = employeeTable.getSelectedColumn();

        if (selectedRow != -1 && selectedColumn != -1) {
            if (selectedColumn == 0) {
                JOptionPane.showMessageDialog(this, "ID cannot be edited.");
                return;
            }

            String currentValue = (String) tableModel.getValueAt(selectedRow, selectedColumn);

            if (selectedColumn == 1) {
                String newValue = JOptionPane.showInputDialog(this, "Edit username:", currentValue);
                if (newValue == null) return;
                if (isUsernameExist(newValue)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                    return;
                }
                tableModel.setValueAt(newValue, selectedRow, selectedColumn);
            }

            if (selectedColumn == 2) {
                JPasswordField passwordField = new JPasswordField();
                JPasswordField confirmPasswordField = new JPasswordField();

                JPanel passwordPanel = new JPanel();
                passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
                passwordPanel.add(new JLabel("Enter Password:"));
                passwordPanel.add(passwordField);
                passwordPanel.add(new JLabel("Confirm Password:"));
                passwordPanel.add(confirmPasswordField);

                int option = JOptionPane.showConfirmDialog(this, passwordPanel, "Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (option == JOptionPane.CANCEL_OPTION) return;

                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Password cannot be empty.");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
                    return;
                }
                tableModel.setValueAt(password, selectedRow, selectedColumn);
            }

            if (selectedColumn == 3) {
                String newValue = JOptionPane.showInputDialog(this, "Edit name:", currentValue);
                if (newValue == null) return;
                tableModel.setValueAt(newValue, selectedRow, selectedColumn);
            }

            if (selectedColumn == 4) {
                String[] roles = {"Employee", "Manager"};
                String newRole = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role",
                        JOptionPane.QUESTION_MESSAGE, null, roles, currentValue);
                if (newRole == null) return;
                tableModel.setValueAt(newRole, selectedRow, selectedColumn);
            }

            if (selectedColumn == 5) {
                String newPhone = getValidPhoneNumber();
                if (newPhone == null) return;
                tableModel.setValueAt(newPhone, selectedRow, selectedColumn);
            }

            if (selectedColumn == 6) {
                String newIdCard = getValidIdCard();
                if (newIdCard == null) return;
                tableModel.setValueAt(newIdCard, selectedRow, selectedColumn);
            }

            saveUsersToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user and a column to edit.");
        }
    }

    private void removeUser() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this user?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                saveUsersToFile();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to remove.");
        }
    }

    private void back() {
    	this.setVisible(false);
    	managerFrame.setVisible(true);
    }

    private void saveUsersToFile() {
        try (FileWriter writer = new FileWriter(Main.userFilePath)) {

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data to file.");
        }
    }

}