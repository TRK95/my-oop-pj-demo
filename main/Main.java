package main;

import java.util.*;

import employee.Employee;
import order.OrderExport;
import product.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
public class Main {
    public static void main(String[] args) {
        String employeePath = "/media/khanhtty/551473877464395A/MyPjOOP/data/employee.csv";
        ArrayList<ArrayList<String>> employeeData = getData(employeePath);
        
        Employee currentUser = null;
        currentUser = login(employeeData);


        if (currentUser != null) {
            String productPath = "/media/khanhtty/551473877464395A/MyPjOOP/data/products.csv";
            ArrayList<ArrayList<String>> productDataFile = getData(productPath);
            List<Product> productsInfo = null;

            for (ArrayList<String> item : productDataFile) {
                Product prodConstruct = new Product(item.get(0), item.get(1), Double.valueOf(item.get(3)), Double.valueOf(item.get(12)), Integer.valueOf(item.get(4)));
            }

            //Take order here
            OrderExport newOrder = currentUser.takeOrderExport(productsInfo);
        }
    }

    private static Employee login(ArrayList<ArrayList<String>> employeeData) {
        Employee currentUser = null;
        System.out.println("Please login"); 
        Scanner inp = new Scanner(System.in);
        String userName = "";
        String password = "";
                
        while (currentUser == null) {
            System.out.println("Please input username:");
            userName = inp.nextLine();
            password = inp.nextLine();
            
            for (ArrayList<String> employee : employeeData) {
                if (employee.get(2).equals(userName) && employee.get(3).equals(password)){
                    System.out.println("Logged in successfully");
                    currentUser = new Employee(employee.get(0), employee.get(1));
                    
                    break;
                };
            } 
            if (currentUser == null) {
                System.out.println("Invalid username or password. Please try again:");
            }
        }
        inp.close();

        return currentUser;
    }

    private static ArrayList<ArrayList<String>> getData(String path) {
        LoadData file = new LoadData(path);
        file.readFile();
        return file.getFileData();
    }
}
