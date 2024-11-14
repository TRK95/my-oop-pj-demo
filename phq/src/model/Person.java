package model;


public class Person {
    private String id;
    private String username;
    private String password;
    private String name;
    private String role;


    public Person(){}

    public Person(String id, String username, String password, String name, String role, String phone, String idCard) {
        this.id = id;
        this.username = username; 
        this.password = password;
        this.name = name;
        this.role = role; 
    }
    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getRole() {
        return role;
    }
}
