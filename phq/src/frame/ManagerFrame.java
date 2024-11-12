package frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Employee;
import model.Manager;

public class ManagerFrame extends JFrame {
	private LoginFrame loginFrame;
	private Employee employee;
	private Manager manager;
    public ManagerFrame(LoginFrame loginFrame,Manager manager, Employee employee) { 
        this.loginFrame = loginFrame;
        this.manager = manager;
        this.employee = employee;
        setTitle("Manager Frame");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        JButton productsButton = new JButton("Products");
        JButton employeesButton = new JButton("Users");
        JButton storeButton = new JButton("Store");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(productsButton);
        buttonPanel.add(employeesButton);
        buttonPanel.add(storeButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> logout());
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProducts();
            }
        });

        employeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEmployees();
            }
        });

        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStore();
            }
        });
    }
    private void showProducts() {
        ProductFrame productFrame = new ProductFrame(loginFrame,this,employee );
        productFrame.setVisible(true);
        this.setVisible(false);
    }

    private void showEmployees() {
        EmpmanaFrame empmanaFrame = new EmpmanaFrame(this);
        empmanaFrame.setVisible(true);
        this.setVisible(false);
    }

    private void showStore() {
    	StoreFrame storeFrame = new StoreFrame(manager,this);
    	storeFrame.setVisible(true);
    	this.setVisible(false);
    }

    private void logout() {
        setVisible(false);
        loginFrame.setVisible(true);
    }

}
