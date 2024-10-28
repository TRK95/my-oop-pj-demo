package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LoadData {
    private String fileName = "";
    private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    public LoadData() {

    }

    public LoadData(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public ArrayList<ArrayList<String>> getFileData() {
        return this.data;
    }

    public void setData(ArrayList<ArrayList<String>> data) {
        this.data = data;
    }

    public void readFile() {
        String fileName = this.getFileName();
        BufferedReader fileReader = null;
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        String line = ""; 
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            while((line = fileReader.readLine()) != null) {
                ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split(",")));
                dataList.add(data);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.setData(dataList);
    }
}