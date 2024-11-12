package model;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PaymentIDController {
	
    private static final String CSV_FILE_PATH = "/media/khanhtty/551473877464395A/OOPLAB/phq/src/payment.csv";
    private static final String DATE_FORMAT = "yyyyMMdd";

    
    // Generate a new paymentID
    public static String generatePaymentID(String type) throws IOException 
    {
        String today = getCurrentDate(); // YYYYMMDD
        String currentMonth = today.substring(0, 6); // YYYYMM
        List<String[]> data = readCSV(CSV_FILE_PATH);
        int currentMaxID = 0;

        // Get the prefix of ID base on type (import or export)
        String prefix = type.equals("import") ? "im" : "ex";

        // Check weather it had paymentID for this type and in this month
        for (String[] row : data) {
            String paymentID = row[0]; // row[0] refers to paymentID in CSV file
            if (paymentID.startsWith(prefix + currentMonth)) 
            { 
                // Get ID's 3 last digit number
                int paymentNumber = Integer.parseInt(paymentID.substring(10)); // "XXX" part
                if (paymentNumber > currentMaxID) 
                {
                    currentMaxID = paymentNumber; // Get max ID in the month for this type of Order
                }
            }
        }

        // Create new ID
        String newID = prefix + today + String.format("%03d", currentMaxID + 1);
        return newID;
    }

    
    // Get the current day according to format YYYYMMDD
    private static String getCurrentDate() 
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }
    

    // Read CSV file and return row list
    private static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        File csvFile = new File(filePath);

        if (csvFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                boolean firstLine = true; // check if reading first line
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // ignore first line
                        continue;
                    }
                    data.add(line.split(",")); // CSV separated by comma
                }
            }
        }

        return data;
    }


    
    // Write a new line to CSV
    public static void writeToCSV(String[] newRow) throws Exception 
    {
        try (FileWriter fw = new FileWriter(CSV_FILE_PATH, true); // true to add to file
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) 
        {
            out.println(String.join(",", newRow));
        }
    }
    
    
}