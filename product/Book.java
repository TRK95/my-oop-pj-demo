package product;

public class Book extends Product {

	private String author;
	private String publisher;
	private long ISBN;
	private int year;
	private String genre;
	
	Book(String id, String name, double sellingPrice, double purchasePrice, int quantity,
		String author, String publisher, long ISBN, int year, String genre) {
		super(id, name, sellingPrice, purchasePrice, quantity);	
		this.author = author;
		this.publisher = publisher;
		this.ISBN = ISBN;
		this.year = year;
		this.genre = genre;
	}
	
	public void copy(Book otherBook) {
		this.setId(otherBook.getId());
		this.setName(otherBook.getName());
		this.setSellingPrice(otherBook.getSellingPrice());
		this.setPurchasePrice(otherBook.getPurchasePrice());
		this.setQuantity(otherBook.getQuantity());
		this.setAuthor(otherBook.getAuthor());
		this.setPublisher(otherBook.getPublisher());
		this.setISBN(otherBook.getISBN());
		this.setYear(otherBook.getYear());
		this.setGenre(otherBook.getGenre());

	}

	//Book getter methods
	public String getAuthor() {
		return this.author;
	}
	public String getPublisher() {
		return this.publisher;
	}
	public long getISBN() {
		return this.ISBN;
	}
	public int getYear() {
		return this.year;
	}
	public String getGenre() {
		return this.genre;
	}
	
	//Book setter methods
	public void setAuthor(String newAuthor) {
		this.author = newAuthor;
	}
	public void setPublisher(String newPublisher) {
		this.publisher = newPublisher;
	}
	public void setISBN(long newISBN) {
		this.ISBN = newISBN;
	}
	public void setYear(int newYear) {
		this.year = newYear;
	}
	public void setGenre(String newGenre) {
		this.genre = newGenre;
	}
	
	
	
}
	