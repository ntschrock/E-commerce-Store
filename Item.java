public class Item 
{
	private String itemID;
	private String Name;
	private boolean inStock;
	private double price;
	private int discount;
	private String finalID;
	
	//All the getters and setters
	public String getID() 
	{
		return itemID;
	}
	public void setID(String ID) 
	{
		this.itemID = ID;
	}

	public String getName() 
	{
		return Name;
	}
	public void setName(String Name) 
	{
		this.Name = Name;
	}
	
	public boolean getInStock() 
	{
		return inStock;
	}
	public void setInStock(boolean inStock) 
	{
		this.inStock = inStock;
	}

	public double getPrice() 
	{
		return price;
	}
	public void setPrice(double price) 
	{
		this.price = price;
	}
	
	public int getDiscount() 
	{
		return discount;
	}
	public void setDiscount(int discount) 
	{
		this.discount = discount;
	}
	
	public String getFinalID() 
	{
		return finalID;
	}
	public void setFinalID(String s) 
	{
		this.finalID = s;
	}
}