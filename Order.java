import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Order 
{
	//trackers for all of the Final invoice information
	String[] fullItemInformation;
	String   view;
	String   returnedView = "";
	String   finalView = "";
	String   finalReturnedView = "";
	public double 	subtotal;
	public double 	total;
	public int 		maxItems;
	public int 		numItems;
	public int 		numTransactions = 0;
	public int 		number = 1;
	File transactions = new File("transactions.txt");
	ArrayList<String> orders = new ArrayList<String>();
	ArrayList<String> ordersFinal = new ArrayList<String>();
	StringBuilder finishOrder = new StringBuilder(); 

	//format the date 
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss a z");
	Date dateOrig = new Date(System.currentTimeMillis());
	String date = dateFormat.format(dateOrig);
	
	//outputs the final invoice 
	public String FinishOrderAndPrint() throws IOException 
	{
		double taxAmount;
		finishOrder.append("Date: " + date + "\n\n");
		finishOrder.append("Number of line items: " + numTransactions + "\n\n");
		finishOrder.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal\n\n");
		finishOrder.append(printOrder() + "\n\n");
		finishOrder.append("Order Subtotal: $" + String.format("%.2f", subtotal) + "\n\n");
		finishOrder.append("Tax rate:       6%\n\n");
		taxAmount = total - subtotal;
		finishOrder.append("Tax Amount:     $" + String.format("%.2f", taxAmount) + "\n\n");
		finishOrder.append("Order total:    $" + String.format("%.2f", total) + "\n\n");
		finishOrder.append("Thanks for shopping at Nile Dot Com!");
		
		printFinalTransactionList();
		
		return finishOrder.toString();
	}
	
	public void addToOrder(String s) 
	{
		orders.add(number + ". " + s);
		this.number = number + 1;
	}

	public void addToFinalOrder(String s) 
	{
		String id = "";
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		
		if (month < 10) 
		{
			id += Integer.toString(day) + "0" + Integer.toString(month) + Integer.toString(year) + Integer.toString(hour) + Integer.toString(min);
		}
		else 
		{
			id += Integer.toString(day) + Integer.toString(month) + Integer.toString(year) + Integer.toString(hour) + Integer.toString(min);
		}
		
		ordersFinal.add(id + ", " + s + date);
	}
	
	public String printOrder() 
	{
		returnedView = "";
		view = orders.toString();

		for (int i = 1; i < view.length() - 1; i++) 
		{
			if (view.charAt(i) == ',') 
			{
				returnedView += '\n';
				i++;
			}
			else 
			{
				returnedView += view.charAt(i);
			}			
		}
		return returnedView;
	}
	
	//creates and write the transaction to the transaction.txt file
	public void printFinalTransactionList() throws IOException 
	{
		if(transactions.exists() == false) 
		{
			transactions.createNewFile();
		}
		
		PrintWriter FinalTransaction = new PrintWriter(new FileWriter(transactions, true));
		FinalTransaction.append(printOrderTransactions());
		
		FinalTransaction.close();
	}
	
	/*public String printOrderTransactions() 
	{
		int count = 0;
		
		finalView = ordersFinal.toString();
		System.out.println("\n\n\n\n\n" + finalView);
		for (int i = 1; i < finalView.length() - 1; i++) 
		{
			if (finalView.charAt(i) == ',') 
			{
				count++;
				if (count % 8 == 0) 
				{	
					finalReturnedView += "\n";
					i++;
				}
				else 
				{
					finalReturnedView += ',';
				}
			}
			else 
			{
				finalReturnedView += finalView.charAt(i);
			}
		}
		System.out.println(finalReturnedView);
		return finalReturnedView;
	}*/

	public String printOrderTransactions() 
	{
		int count = 0;
		
		finalView = ordersFinal.toString();
		System.out.println(finalView);
		for (int i = 1; i < finalView.length(); i++) 
		{
			
			if(finalView.charAt(i) == ',') 
			{
				count++;
				if (count % 8 == 0) 
				{	
					finalReturnedView += "\n";
					i++;
				}
				else 
				{
					finalReturnedView += ',';
				}
			}
			else if(finalView.charAt(i) == ']')
			{
				finalReturnedView += "\n";
			}
			else 
			{
				finalReturnedView += finalView.charAt(i);
			}
		}
		System.out.println(finalReturnedView);
		return finalReturnedView;
	}


	//number of transactions
	public void setTransactions() 
	{
		this.numTransactions++;
	}
	public int getTransactions() 
	{
		return numTransactions;
	}
	
	//all the getters and setters
	public double getSubtoal() 
	{
		return subtotal;
	}
	public void setSubtotal(double subtotal) 
	{
		this.subtotal = subtotal;
	}

	public double getTotal() 
	{
		return subtotal * 1.06;
	}	
	public void setTotal(double subtotal) 
	{
		this.total = this.subtotal * 1.06;
	}

	public void setTotalItemsInOrder(int totalItems) 
	{
		this.numItems = totalItems;
	}
	public int getTotalItemsInOrder() 
	{
		return numItems;
	}

	public void setMaximumItemsInOrder(int maxItems) 
	{
		this.maxItems = maxItems;
	}
	public int getMaximumItemsInOrder() 
	{
		return maxItems;
	}
}