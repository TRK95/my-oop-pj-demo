package frame;

import javax.swing.JFrame;
import model.Employee;

public class ProductFrame extends EmployeeFrame {
    private ManagerFrame managerFrame;

    public ProductFrame(LoginFrame loginFrame, ManagerFrame managerFrame, Employee employee) {
        super(loginFrame, employee);
        this.managerFrame = managerFrame;
        setTitle("Product Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    @Override
    protected void back() {
        this.setVisible(false);
        managerFrame.setVisible(true);
    }
}
