package model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import Order;


public class Payment {
	
	private static boolean isProcessingPayment = false;
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
		
		if(isProcessingPayment) 
		{
            System.out.println("A payment is already being processed. Please wait.");
            return false;
        }
		
		isProcessingPayment = true;
		
        JFrame frame = new JFrame("Payment Processing");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Select your payment method:");
        JButton cashButton = new JButton("Cash");
        JButton cardButton = new JButton("Card");

        // Panel to hold components
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(cashButton);
        panel.add(cardButton);
        frame.add(panel);

        // Button actions
        cashButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                CashPayment cash = new CashPayment(totalAmount, paymentID, employeeHandle);
                status = cash.processPayment();
                JOptionPane.showMessageDialog(frame, "Cash payment processed.");
                frame.dispose();
            }
        });

        cardButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                CardPayment card = new CardPayment(totalAmount, paymentID, employeeHandle);
                status = card.processPayment();
                JOptionPane.showMessageDialog(frame, "Card payment processed.");
                frame.dispose();
            }
        });

        frame.setVisible(true);
        
        isProcessingPayment = false;

        // Status will be updated by the action listeners
        return status;
    }

}
