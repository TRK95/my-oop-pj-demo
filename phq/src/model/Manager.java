package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Manager extends Employee {

    public Manager(String id, String username, String password, String name, String role, String phone, String idCard) {
        super(id, username, password, name, role, phone, idCard);
    }
    

    public void loadData(String filePath, Map<String, double[]> exportSummary,
                          Map<String, double[]> importSummary, String datePrefix) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; 
                }
                String[] values = line.split(",");
                if (values.length < 6) continue;

                String id = values[0];
                String date = id.substring(2, 10); 
                if (!date.startsWith(datePrefix)) continue; 

                double price = id.startsWith("im") ? Double.parseDouble(values[5]) : Double.parseDouble(values[3]);
                int quantity = Integer.parseInt(values[4]);
                if (id.startsWith("im")) {
                    double[] totals = importSummary.getOrDefault(date, new double[2]);
                    totals[0] += quantity;
                    totals[1] += price * quantity;
                    importSummary.put(date, totals);
                } else if (id.startsWith("ex")) {
                    double[] totals = exportSummary.getOrDefault(date, new double[2]);
                    totals[0] += quantity;
                    totals[1] += price * quantity;
                    exportSummary.put(date, totals);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadExpenses(String filePath, Map<String, Double> expenseDetails, String datePrefix) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; 
                }

                String[] values = line.split(",");
                if (values.length < 3) continue;

                String date = values[0].substring(0, 6); 
                if (!date.equals(datePrefix)) continue; 

                String name = values[1]; 
                double cost = Double.parseDouble(values[2]); 

                expenseDetails.put(name, expenseDetails.getOrDefault(name, 0.0) + cost);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Double> calculateSalaries(String selectedDatePrefix) {
        Map<String, Double> salaries = new HashMap<>();
        String logFilePath = "/media/khanhtty/551473877464395A/OOPLAB/phq/src/log.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; 
                }

                String[] values = line.split(",");
                if (values.length < 4) continue;

                String[] logValues = values[1].split("/");
                if (logValues.length < 3) continue; 
                String logYear = logValues[0]; 
                String logMonth = logValues[1]; 
                String logPrefix = logYear + logMonth; 

                if (!logPrefix.equals(selectedDatePrefix)) continue; 

                String name = values[0]; 
                double duration = Double.parseDouble(values[3]); 

                double salary = duration * 25;
                salaries.put(name + "'s salary ", salaries.getOrDefault(name, 0.0) + salary);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return salaries;
    }
}