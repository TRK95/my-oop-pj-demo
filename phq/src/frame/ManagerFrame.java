package frame;

import java.awt.BorderLayout;
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

        logoutButton.addActionListener(e -> managerSelection(0));
        employeesButton.addActionListener(e -> managerSelection(2));
        storeButton.addActionListener(e -> managerSelection(3));
        productsButton.addActionListener(e -> managerSelection(1));
    }
    // thay the 4 void bang 1 void
    private void managerSelection(int choice) {
    	if (choice == 0) {     // logout
            setVisible(false);
            loginFrame.setVisible(true);
    	}else if (choice == 1) {
            ProductFrame productFrame = new ProductFrame(loginFrame,this,employee ); // show products
            productFrame.setVisible(true);
            this.setVisible(false);
    	}else if(choice == 2) {
            EmpmanaFrame empmanaFrame = new EmpmanaFrame(this,manager);   // show employees
            empmanaFrame.setVisible(true);
            this.setVisible(false);
    	}else if (choice == 3) {
        	StoreFrame storeFrame = new StoreFrame(manager,this);  // show store
        	storeFrame.setVisible(true);
        	this.setVisible(false);
    	}
    }

}
