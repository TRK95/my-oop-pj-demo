package model;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class CardPayment {

    private double amount;
    private String customerCardNumber;
    private String paymentID;
    private boolean isCancelled; // Track if payment is canceled
    private String employeeHandle;

    public CardPayment(double amount, String paymentID, String employeeHandle) 
    {
        this.amount = amount;
        this.customerCardNumber = null;
        this.paymentID = paymentID;
        this.isCancelled = false; // Initially, payment is not canceled
        this.employeeHandle = employeeHandle;
    }

    public void addPaymentInfoToCSV(double amount, String paymentID, String customerCardNumber) 
    {
        try 
        {
            String data[] = new String[]{paymentID, employeeHandle, "card", "true", String.valueOf(amount), customerCardNumber};
            
            String paymentHashValue = generateHash(data);

        	String extendedData[] = Arrays.copyOf(data, data.length + 1);
        	extendedData[extendedData.length - 1] = paymentHashValue; 

        	String message = "Your payment of $" + amount + " is completed.\n" 
        	               + "referenceID : " + paymentHashValue;
        	JOptionPane.showMessageDialog(null, message, 
        					"Payment Completed", JOptionPane.INFORMATION_MESSAGE);

        	PaymentIDController.writeToCSV(extendedData);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public boolean isCardNumberValid(String cardNumber) 
    {
        return cardNumber.matches("\\d{16}"); // Simple card number validation
    }
    

    
    public void cancelPayment() 
    {
    	
        if (isCancelled == false) 
        {
            isCancelled = true; // Mark the payment as canceled
            JOptionPane.showMessageDialog(null, "Payment with ID " + paymentID + " has been canceled.", 
                    "Payment Canceled", JOptionPane.INFORMATION_MESSAGE);
            
            // log cancellation in CSV
            try 
            {
            	String data[] = new String[]{paymentID, String.format(employeeHandle), "card", "false"};
            	
            	String paymentHashValue = generateHash(data);

            	String extendedData[] = Arrays.copyOf(data, data.length + 1);
            	extendedData[extendedData.length - 1] = paymentHashValue; 

            	String message = "Payment with ID " + paymentID + " has been canceled.\n" 
            	               + "referenceID : " + paymentHashValue;
            	JOptionPane.showMessageDialog(null, message, 
            	                              "Payment Canceled", JOptionPane.INFORMATION_MESSAGE);

            	PaymentIDController.writeToCSV(extendedData);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
        } 
    }

    public static String generateHash(String[] paymentData) 
    {
        try {
            // join string elements in array to a union string
            String input = String.join(",", paymentData);

            // use SHA-256 to create hash
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

            // change hash to hex-string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) 
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    public boolean processPayment() 
    {
    	
    	// Display confirmation dialog to continue with the payment
        int userChoice = JOptionPane.showConfirmDialog(null, 
            "Do you wish to continue with your payment?", 
            "Payment Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (userChoice == JOptionPane.NO_OPTION) 
        {
            cancelPayment();
            return true;
        }
    	
    	if (!isCancelled) 
    	{
    	    boolean isValidCardNumber = false;

    	    // Prompt user for credit card number
    	    while (!isValidCardNumber) 
    	    {
    	        customerCardNumber = JOptionPane.showInputDialog(null, 
    	            "Please provide your credit card number:");

    	        if (customerCardNumber == null) // Handle cancel action in the input dialog
    	        {
    	            cancelPayment();
    	            return false; // Exit if the user cancels the input
    	        }

    	        // Validate the card number
    	        if (isCardNumberValid(customerCardNumber)) 
    	        {
    	            isValidCardNumber = true; // Valid card number, exit loop
    	        } 
    	        else 
    	        {
    	            JOptionPane.showMessageDialog(null, 
    	                "Invalid card number. Please try again.", 
    	                "Invalid Input", 
    	                JOptionPane.ERROR_MESSAGE);
    	        }
    	    }
    	    
    	    addPaymentInfoToCSV(amount, paymentID, customerCardNumber);
    	}
        
        return false;
    }
}