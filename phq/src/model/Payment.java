package model;

import javax.swing.*;

public class Payment {
	private double totalAmount;
	private String paymentID;
	private boolean status;
	private String employeeHandle;
	
	public Payment(double totalAmount, String type, String employeeHandle) 
	{
	    this.totalAmount = totalAmount;
	    this.employeeHandle = employeeHandle;
	    this.status = false;
	    this.paymentID = generateNewPaymentID(type);
	}


	public String getID() 
	{
		return paymentID;
	}
	
	public boolean getStatus()
	{
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	private String generateNewPaymentID(String type) 
	{
	    String newPaymentID = null; 
	    try 
	    {
	        newPaymentID = PaymentIDController.generatePaymentID(type);
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace(); 
	    }
	    return newPaymentID; 
	}
	

	public boolean processPayment() {
	    // Create a JDialog instead of a JFrame to prevent immediate return
	    JDialog dialog = new JDialog((JFrame) null, "Payment Processing", true);
	    dialog.setSize(400, 200);
	    dialog.setLocationRelativeTo(null);

	    JLabel label = new JLabel("Select your payment method:");
	    JButton cashButton = new JButton("Cash");
	    JButton cardButton = new JButton("Card");

	    JPanel panel = new JPanel();
	    panel.add(label);
	    panel.add(cashButton);
	    panel.add(cardButton);
	    dialog.add(panel);

	    final boolean[] paymentStatus = {false}; // Use an array to modify status in inner class

	    // Cash payment processing
	    cashButton.addActionListener(e -> {
	        CashPayment cash = new CashPayment(totalAmount, paymentID, employeeHandle);
	        paymentStatus[0] = cash.processPayment();
	        JOptionPane.showMessageDialog(dialog, "Cash payment processed.");
	        dialog.dispose();
	    });

	    // Card payment processing
	    cardButton.addActionListener(e -> {
	        CardPayment card = new CardPayment(totalAmount, paymentID, employeeHandle);
	        paymentStatus[0] = card.processPayment();
	        JOptionPane.showMessageDialog(dialog, "Card payment processed.");
	        dialog.dispose();
	    });

	    // Display dialog and wait until a payment method is processed
	    dialog.setVisible(true);

	    // Set the main status based on payment outcome
	    return paymentStatus[0];
	}


}
