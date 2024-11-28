package frame;

import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Employee;
import model.Manager;
import model.Person;

public class LoginFrame extends JFrame {
	private JTextField userIdField;
    private JPasswordField passwordField;


    public LoginFrame() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        setLayout(new GridLayout(4, 1, 0, 15));
        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
                userIdField.setText("");
                passwordField.setText("");
            }
        });    
    }
    
    // o day bo duoc 1 void ne
    
    private void login() {
        String username = userIdField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both User ID and Password must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Person user = authenticate(username, password);
            if (user != null) {
                Employee employee = new Employee(user.getId(), username, password, user.getName(), "employee", user.getPhone(), user.getIdCard());
            	switch (user.getRole().toLowerCase()) {
 
                case "employee":
                    EmployeeFrame employeeFrame = new EmployeeFrame(this, employee);
                    employeeFrame.setVisible(true);
                    break;
                case "manager":
                    Manager manager = new Manager(user.getId(), username, password, user.getName(), "manager", user.getPhone(), user.getIdCard());
                	ManagerFrame managerFrame = new ManagerFrame(this, manager,employee);
                    managerFrame.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Role not recognized: " + user.getRole(), "Role Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
            	dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User ID or Password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading user data file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Person authenticate(String username, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(Main.userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length < 7) {
                    continue;             }

                String csvId = record[0].trim();
                String csvUsername = record[1].trim();
                String csvPassword = record[2].trim();
                String name = record[3].trim();
                String role = record[4].trim();
                String phone = record[5].trim();
                String idCard = record[6].trim();

                if (csvUsername.equals(username) && csvPassword.equals(password)) {
                    return new Person (csvId, csvUsername, csvPassword, name, role, phone, idCard);
                }
            }
        }
        return null; 
    }
}
