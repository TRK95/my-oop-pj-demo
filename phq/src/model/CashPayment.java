package model;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CashPayment {

    private double amount;
    private String paymentID;
    private boolean isCancelled;
    private String employeeHandle;

    public CashPayment(double amount, String paymentID, String employeeHandle) 
    {
        this.amount = amount;
        this.paymentID = paymentID;
        this.isCancelled = false;
        this.employeeHandle = employeeHandle;
    }

    public void addPaymentInfoToCSV(double amount, String paymentID) 
    {
        try 
        {
        	String data[] = new String[]{paymentID, this.employeeHandle, "cash", "true", String.valueOf(amount), "null"};
            
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
    
    public void cancelPayment() 
    {
        if (isCancelled == false) 
        {
            isCancelled = true;
            
            try 
            {
            	String data[] = new String[]{paymentID, employeeHandle, "cash", "false"};

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
            String input = String.join(",", paymentData);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

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
            addPaymentInfoToCSV(amount, paymentID);
        }
        
        return false;
    }


   
}