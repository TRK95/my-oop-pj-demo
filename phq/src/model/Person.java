package model;


public class Person {
    private String id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String phone;
    private String idCard;

    public Person(String id, String username, String password, String name, String role, String phone, String idCard) {
        this.id = id;
        this.username = username; 
        this.password = password;
        this.name = name;
        this.role = role; 
        this.phone = phone;
        this.idCard = idCard;
    }
        public String getId() {
    		return id;
    	}

    	public void setId(String id) {
    		this.id = id;
    	}

    	public String getUsername() {
    		return username;
    	}

    	public void setUsername(String username) {
    		this.username = username;
    	}

    	public String getPassword() {
    		return password;
    	}

    	public void setPassword(String password) {
    		this.password = password;
    	}

    	public String getName() {
    		return name;
    	}

    	public void setName(String name) {
    		this.name = name;
    	}

    	public String getRole() {
    		return role;
    	}

    	public void setRole(String role) {
    		this.role = role;
    	}

    	public String getPhone() {
    		return phone;
    	}

    	public void setPhone(String phone) {
    		this.phone = phone;
    	}

    	public String getIdCard() {
    		return idCard;
    	}

    	public void setIdCard(String idCard) {
    		this.idCard = idCard;
    	}

}
