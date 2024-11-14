package frame;

public class Main {
	private static String root = "D:\\OOPpj\\phq\\phq\\src\\"; 
    public static String ordersFilePath = root + "orders.csv";
    public static String expensesFilePath = root + "expense.csv";
    public static String userFilePath =  root + "user.csv";
    public static String logFilePath = root + "log.csv";
    public static String paymentFilePath = root + "payment.csv";
    public static String productFilePath = root + "products.csv";
	public static void main(String[] args) {
		LoginFrame loginFrame = new LoginFrame();
		loginFrame.setVisible(true);
	}
}	
