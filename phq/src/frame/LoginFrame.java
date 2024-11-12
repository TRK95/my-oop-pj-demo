package frame;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Employee;
import model.Manager;

public class LoginFrame extends JFrame {
	private JTextField userIdField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL url = getClass().getClassLoader().getResource("icon.png");
        if (url != null) {
            Image icon = Toolkit.getDefaultToolkit().getImage(url);
            setIconImage(icon);
        }
        
        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        setLayout(new GridLayout(4, 2));
        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });    
    }
    private void login() {
        String username = userIdField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both User ID and Password must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String[] userDetails = authenticate(username, password);
            if (userDetails != null) {
                openRoleBasedFrame(userDetails[4], userDetails);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User ID or Password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading user data file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] authenticate(String username, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("/media/khanhtty/551473877464395A/OOPLAB/phq/src/user.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length < 7) {
                    continue;
                }

                String csvId = record[0].trim();
                String csvUsername = record[1].trim();
                String csvPassword = record[2].trim();
                String name = record[3].trim();
                String role = record[4].trim();
                String phone = record[5].trim();
                String idCard = record[6].trim();

                if (csvUsername.equals(username) && csvPassword.equals(password)) {
                    return new String[] {csvId, csvUsername, csvPassword, name, role, phone, idCard};
                }
            }
        }
        return null; 
    }
    private void openRoleBasedFrame(String role, String[] userDetails) {
        String id = userDetails[0];
        String username = userDetails[1];
        String password = userDetails[2];
        String name = userDetails[3];
        String phone = userDetails[5];
        String idCard = userDetails[6];

        switch (role.toLowerCase()) {
            case "employee":
                Employee employee = new Employee(id, username, password, name, "employee", phone, idCard);
                EmployeeFrame employeeFrame = new EmployeeFrame(this, employee);
                employeeFrame.setVisible(true);
                break;
            case "manager":
                Manager manager = new Manager(id, username, password, name, "manager", phone, idCard);
                Employee employee1 = new Employee(id, username, password, name, "manager", phone, idCard);
            	ManagerFrame managerFrame = new ManagerFrame(this, manager,employee1);
                managerFrame.setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Role not recognized: " + role, "Role Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
        dispose();
    }

}
