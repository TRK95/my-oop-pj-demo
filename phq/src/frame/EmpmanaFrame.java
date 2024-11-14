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
        setTitle("User Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Username", "Password", "Name", "Role", "Phone", "ID Card"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        add(actionPanel, BorderLayout.SOUTH);

        addButton = new JButton("Add User");
        actionPanel.add(addButton);
        addButton.addActionListener(e -> addUser());

        editButton = new JButton("Edit User");
        actionPanel.add(editButton);
        editButton.addActionListener(e -> editUser());

        removeButton = new JButton("Remove User");
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
                if (data.length >= 7) {
                    String[] row = new String[8];
                    row[0] = data[0];
                    row[1] = data[1];
                    row[2] = data[2];
                    row[3] = data[3];
                    row[4] = data[4];
                    row[5] = data[5];
                    row[6] = data[6];
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from file.");
        }
    }

    private void addUser() {
        String id = JOptionPane.showInputDialog(this, "Enter ID:");
        String username = JOptionPane.showInputDialog(this, "Enter Username:");

        if (isIdOrUsernameExist(id, username)) {
            JOptionPane.showMessageDialog(this, "ID or Username already exists. Please enter a unique ID and Username.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Enter Password:");
        String name = JOptionPane.showInputDialog(this, "Enter Name:");

        String[] roles = {"Employee", "Manager"};
        String role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

        String phone = JOptionPane.showInputDialog(this, "Enter Phone:");
        String idCard = JOptionPane.showInputDialog(this, "Enter ID Card:");

        tableModel.addRow(new String[]{id, username, password, name, role, phone, idCard});
        saveUsersToFile();
    }

    private boolean isIdOrUsernameExist(String id, String username) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingId = (String) tableModel.getValueAt(i, 0);
            String existingUsername = (String) tableModel.getValueAt(i, 1);
            if (existingId.equals(id) || existingUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }


    private void editUser() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);
            String password = (String) tableModel.getValueAt(selectedRow, 2);
            String name = (String) tableModel.getValueAt(selectedRow, 3);
            String[] roles = {"Employee", "Manager"};
            String phone = (String) tableModel.getValueAt(selectedRow, 5);
            String idCard = (String) tableModel.getValueAt(selectedRow, 6);

            username = JOptionPane.showInputDialog(this, "Enter Username:", username);
            password = JOptionPane.showInputDialog(this, "Enter Password:", password);
            name = JOptionPane.showInputDialog(this, "Enter Name:", name);
            String role = (String) JOptionPane.showInputDialog(this, "Select Role:", "Role",
                    JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
            phone = JOptionPane.showInputDialog(this, "Enter Phone:", phone);
            idCard = JOptionPane.showInputDialog(this, "Enter ID Card:", idCard);

            tableModel.setValueAt(id, selectedRow, 0);
            tableModel.setValueAt(username, selectedRow, 1);
            tableModel.setValueAt(password, selectedRow, 2);
            tableModel.setValueAt(name, selectedRow, 3);
            tableModel.setValueAt(role, selectedRow, 4);
            tableModel.setValueAt(phone, selectedRow, 5);
            tableModel.setValueAt(idCard, selectedRow, 6);

            saveUsersToFile();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
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
