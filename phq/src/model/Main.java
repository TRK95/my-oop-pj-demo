package model;

public class Main {
    public static void main(String[] args) {
        
        Employee thisEmployee = new Employee("id1", "usr1", "1", "Chin", "Em", "0101010", "idcad1");
        // thisEmployee.makeOrderFromCustomer();

        // Payment tesPayment = new Payment(300.00, "import", thisEmployee.getName());
        // tesPayment.processPayment();
        // System.out.println(tesPayment.getID());

        OrderExport tesOrderExport = new OrderExport(thisEmployee);
        tesOrderExport.takeOrder();
    }
    

}
