package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import frame.Main;

public class Manager extends Employee {


    public Manager(String id, String username, String password, String name, String role, String phone, String idCard) {
        super(id, username, password, name, role, phone, idCard);
    }

    public List<Person> getUsersFromFile() {
        List<Person> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Main.userFilePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID")) continue;
                String[] values = line.split(",");
                Person user = createUserFromData(values);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public Person createUserFromData(String[] data) {
    	String id = data[0].trim();
    	String username = data[1];
    	String password = data[2];
    	String name = data[3];
    	String role = data[4];
    	String phone = data[5];
    	String idCard = data[6].trim();
    	

        switch (role.toLowerCase()) {
            case "employee":
                return new Employee(id,username, password,name,role,phone,idCard);
            case "manager":
                return new Manager(id,username,password,name,role,phone,idCard);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
    
    
    
    
    
    //thay 4 void bang 1 void
    public String[] getUserRowData(Person user) {
        String[] data = new String[7];
        data[0] = user.getId();
        data[1] = user.getUsername();
        data[2] = user.getPassword();
        data[3] = user.getName();
        data[4] = user.getRole();
        data[5] = user.getPhone();
        data[6] = user.getIdCard();
        return data;
    }
    public void updateUserChange(Person user,String change) {
        List<Person> users = getUsersFromFile();
        if(change.equals("add")) {
            users.add(user);
        }else if (change.equals("remove")) {


            Iterator<Person> iterator = users.iterator();

            while (iterator.hasNext()) {
                Person delete = iterator.next();
                if (delete.getName().equals(user.getName())) { 
                    iterator.remove(); 
                    break;
                }
            }
        }else if (change.equals("edit")) {
        	for (Person edit :users) {
        		if(edit.getId().equals(user.getId())) {
        			edit.setName(user.getName());
        			edit.setRole(user.getRole());
        			edit.setUsername(user.getUsername());
        			edit.setPassword(user.getPassword());
        			edit.setPhone(user.getPhone());
        			edit.setIdCard(user.getIdCard());
        		}
        	}
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.userFilePath))) {
            writer.write("ID,username,password,name,role,phone,idCard");
            writer.newLine();
            for (Person tmp : users) {
                String[] data = getUserRowData(tmp);
                writer.write(String.join(",", data));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // doi ten void cho hop ly
    /////////
    ////////
    ///////
    //////
    /////
    ////
    ///
    //
    //
    public void calculateIncomeAndOutcome(String filePath, Map<String, double[]> exportSummary,
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
                if(datePrefix.equals("0000All")) {
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
                }else if (datePrefix.contains("All")){
                	if(!(date.substring(0,4)).equals(datePrefix.substring(0,4))) continue;
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
                else {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void calculateExpenses(String filePath, Map<String, Double> expenseDetails, String datePrefix) {
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
                if (datePrefix.equals("0000All")) {
                	String name = values[1]; 
                    double cost = Double.parseDouble(values[2]); 

                    expenseDetails.put(name, expenseDetails.getOrDefault(name, 0.0) + cost);
                }
                else if(datePrefix.contains("All")) {
                	if(!date.substring(0,4).equals(datePrefix.substring(0,4))) continue;
                	String name = values[1]; 
                    double cost = Double.parseDouble(values[2]); 

                    expenseDetails.put(name, expenseDetails.getOrDefault(name, 0.0) + cost);
                }
                else {
                if (!date.equals(datePrefix)) continue; 

                String name = values[1]; 
                double cost = Double.parseDouble(values[2]); 

                expenseDetails.put(name, expenseDetails.getOrDefault(name, 0.0) + cost);
            }
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Double> calculateSalaries(String selectedDatePrefix) {
        Map<String, Double> salaries = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(Main.logFilePath))) {
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

                if (selectedDatePrefix.equals("0000All")) {
                	
	                String name = values[0]; 
	                double duration = Double.parseDouble(values[3]); 

	                double salary = duration * 25;
	                salaries.put(name + "'s salary", salaries.getOrDefault(name + "'s salary", 0.0) + salary);

                }else if (selectedDatePrefix.contains("All")) {
                	if (!logYear.equals(selectedDatePrefix.substring(0,4))) continue;
                	
                	String name = values[0]; 
	                double duration = Double.parseDouble(values[3]); 

	                double salary = duration * 25;
	                salaries.put(name + "'s salary", salaries.getOrDefault(name + "'s salary", 0.0) + salary);
                }
                else {
                	if (!logPrefix.equals(selectedDatePrefix)) continue; 

	                String name = values[0]; 
	                double duration = Double.parseDouble(values[3]); 
	
	                double salary = duration * 25;
	                salaries.put(name + "'s salary", salaries.getOrDefault(name + "'s salary", 0.0) + salary);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return salaries;
    }
    
}