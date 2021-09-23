/* Name: Noah Schrock
 * Course: CNT 4714 - Fall 2021
 * Assignment title: Project 1
 * Date: September 2nd 2021
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Main extends JFrame 
{
	public int totalOrders = 0;
	public int totalItems  = 0;
	public int maxItems;
	public int temp;
	public boolean inStock;
	public double subtotal = 0;
	private ArrayList<Item> inventory;
	private Order order = new Order();

	// Making the Text Fields, buttons, and labels for the GUI
	public JLabel subTotalLabel = new JLabel ("Order Subtotal for 0 item(s):");
	public JLabel IDLabel = new JLabel ("Enter Item ID for Item #1:");
	public JLabel itemQuantityLabel = new JLabel ("Enter Quantity for Item #1:");
	public JLabel infoLabel = new JLabel ("Item #1 Info:");
	
	private JTextField MaxItemsInOrder = new JTextField();
	private JTextField ItemID = new JTextField();
	private JTextField ItemQuantity = new JTextField();
	private JTextField ItemInformation = new JTextField();
	private JTextField OrderSubtotal = new JTextField();
	
	private JButton processButton = new JButton("Process Item #1");
	private JButton confirmButton = new JButton("Confirm Item #1");
	private JButton viewButton = new JButton("View Order");
	private JButton finishButton = new JButton("Finish Order");
	private JButton newOrderButton = new JButton("New Order");
	private JButton exitButton = new JButton("Exit");
	
	public Main() throws FileNotFoundException 
	{
		this.ReadAndProcessFile();
		JPanel EnterFields = new JPanel(new GridLayout(5,2));
		//items in order label and field
		EnterFields.add(new JLabel("Enter number of items in this order:"));
		EnterFields.add(MaxItemsInOrder);
		//ID label and field
		EnterFields.add(IDLabel);
		EnterFields.add(ItemID);
		//number of item label and field
		EnterFields.add(itemQuantityLabel);
		EnterFields.add(ItemQuantity);
		//item info label and field
		EnterFields.add(infoLabel);
		EnterFields.add(ItemInformation);
		//subtotal label and field
		EnterFields.add(subTotalLabel);
		EnterFields.add(OrderSubtotal);
		
		//the 6 main buttons
		JPanel Buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		Buttons.add(processButton);
		Buttons.add(confirmButton);
		Buttons.add(viewButton);
		Buttons.add(finishButton);
		Buttons.add(newOrderButton);
		Buttons.add(exitButton);
		
		//text fields that are initially false
		this.OrderSubtotal.setEnabled(false);
		this.ItemInformation.setEnabled(false);
		
		//buttons that are initially false
		this.confirmButton.setEnabled(false);
		this.finishButton.setEnabled(false);
		this.viewButton.setEnabled(false);
		
		//buttons on the top, text fields on the bottom
		//I think it looks better like this, but if it needs to be the way shown in the screenshots the code is there for that
		add(EnterFields, BorderLayout.SOUTH);
		add(Buttons, BorderLayout.NORTH);

		/*add(EnterFields, BorderLayout.NORTH);
		add(Buttons, BorderLayout.SOUTH);*/
		
		//button action listeners
		processButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				double totalPrice = 0;
				String ID = ItemID.getText();
				int max = Integer.parseInt(MaxItemsInOrder.getText());
				
				confirmButton.setEnabled(true);
				order.setMaximumItemsInOrder(max);
				MaxItemsInOrder.setEnabled(false);
				
				int i = searchForID(ID);
				int quantity = Integer.parseInt(ItemQuantity.getText());
				int discount = 0;
				double discountPercentage = 0;
				
				if (i == -1) 
				{
					JOptionPane.showMessageDialog(null, "item ID " + ID + " not in file");
					ItemID.setText("");
					ItemQuantity.setText("");
				}
				
				if (i != -1) 
				{
					String itemID = inventory.get(i).getID();
					String itemName = inventory.get(i).getName();
					Double itemPrice = inventory.get(i).getPrice();
					boolean inStock = inventory.get(i).getInStock();

					//item is out of stock
					if (!inStock) 
					{
						JOptionPane.showMessageDialog(null, "Sorry... that item is out of stock, please try another item");
						//clears text for customer and disables button
						ItemID.setText("");
						ItemQuantity.setText("");
						confirmButton.setEnabled(false);
					}
					//item is in stock and sets the dicount
					if (inStock) 
					{
						if (quantity >= 1 && quantity <= 4) 
						{
							inventory.get(i).setDiscount(0);
							discountPercentage = 1;
						}
						if (quantity >= 5 && quantity <= 9) 
						{
							inventory.get(i).setDiscount(10);
							discountPercentage = 0.9;
						}
						if (quantity >= 10 && quantity <= 14) 
						{
							inventory.get(i).setDiscount(15);
							discountPercentage = 0.85;
						}
						if (quantity >= 15) 
						{
							inventory.get(i).setDiscount(20);
							discountPercentage = 0.8;
						}
						
						discount = inventory.get(i).getDiscount();
						totalPrice = (itemPrice * quantity) * discountPercentage;
						String info = itemID + " " + itemName + " $" + String.format("%.2f", itemPrice) + " " + quantity + " " + discount + "% $" + String.format("%.2f", totalPrice);
						
						confirmButton.setEnabled(true);
						processButton.setEnabled(false);
						ItemInformation.setText(info);
					}
				}
			}
		});
		
		//action listener for confirm
		confirmButton.addActionListener(new ActionListener()
		{		
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int num = 0;
				maxItems = Integer.parseInt(MaxItemsInOrder.getText());
				order.setMaximumItemsInOrder(maxItems);
				
				String ID = ItemID.getText();
				int quantity = Integer.parseInt(ItemQuantity.getText());
				int i = searchForID(ID);
				double finalDiscount = 0;
				
				if (i != -1) 
				{
					//gets the item prices and discounts
					Double itemPrice = inventory.get(i).getPrice();
					int discount = inventory.get(i).getDiscount();
					double discountPercentage = 1;
					
					if (discount == 0) 
					{
						discountPercentage = 1;
					}
					if (discount == 10) 
					{
						discountPercentage = 0.9;
					}
					if (discount == 15) 
					{
						discountPercentage = 0.85;
					}
					if (discount == 20) 
					{
						discountPercentage = 0.8;
					}

					subtotal += (itemPrice * quantity * discountPercentage);
					order.setSubtotal(subtotal);

					if (num <= order.getMaximumItemsInOrder()) 
					{
						//add item confirmation
						JOptionPane.showMessageDialog(null, "Item #" + (order.getTransactions() + 1) + " accepted. Added to your cart.");
						totalItems += Integer.parseInt(ItemQuantity.getText());
						
						order.setTotalItemsInOrder(totalItems);
						order.setTransactions();
						num += order.getTransactions();
						OrderSubtotal.setText(String.format("%.2f", subtotal));
						viewButton.setEnabled(true);
					
						if (num < (maxItems + 1)) 
						{	
							//lables
							order.setTotalItemsInOrder(totalItems);
							processButton.setText("Process Item #" + (num + 1));
							confirmButton.setText("Confirm Item #" + (num + 1));
							subTotalLabel.setText("Order Subtotal for " + (num) + " item(s):");
							IDLabel.setText("Enter Item ID for Item #" + (num + 1) + ":");
							itemQuantityLabel.setText("Enter Quantity for Item #" +  (num + 1) + ":");
							infoLabel.setText("Item #" + (num + 1) + " Info:");
							
							//auto filled from inventory
							ItemInformation.setText("");
							//you fill these in
							ItemID.setText("");
							ItemQuantity.setText("");
							
							String itemID = inventory.get(i).getID();
							String itemName = inventory.get(i).getName();
							double totalPrice = (itemPrice * quantity) * discountPercentage;
							
							String info = itemID + " " + itemName + " $" + String.format("%.2f", itemPrice) + " " + quantity + " " + discount + "% $" + String.format("%.2f", totalPrice);

							if (discount == 0) { //might not need lines 236 - 249
								finalDiscount = 0;
							}
							else if (discount == 10) {
								finalDiscount = 0.1;
							}
							else if (discount == 15) {
								finalDiscount = 0.15;
							}
							else if (discount == 20) {
								finalDiscount = 0.2;
							}
							
							String info1 = itemID + "," + itemName + ", " + String.format("%.2f", itemPrice) + ", " + quantity + ", " + finalDiscount + ", $" + String.format("%.2f", totalPrice) + ", ";

							order.addToFinalOrder(info1); //might not need
							order.addToOrder(info);
							
							confirmButton.setEnabled(false);
							processButton.setEnabled(true);
						}
						if (maxItems <= num) 
						{
							//prompts the end of the order information
							JOptionPane.showMessageDialog(null, "Your cart is full, please click 'Finish Order'");
							finishButton.setEnabled(true);
							processButton.setEnabled(false);
							confirmButton.setEnabled(false);
							order.setTotal(subtotal);
						}
					}
				}
			}
		});
		
		//Add to order listener
		viewButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, order.printOrder());
				order.printOrder();
			}
		});
		
		//Finish order listener
		finishButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					JOptionPane.showMessageDialog(null, order.FinishOrderAndPrint());
				}catch (HeadlessException e1){
					e1.printStackTrace();
				}catch (IOException e1){
					e1.printStackTrace();
				}
			}
		});

		//action listener for finish order
		newOrderButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{				
				JOptionPane.showMessageDialog(null, "Begin your new Order");
				Main.super.setVisible(true);
				Main.super.dispose(); 
					try
					{
						Main.main(null);
					}catch (FileNotFoundException e1){
						e1.printStackTrace();
					}
			}
		});

		//action listener for finish order
		exitButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Main.super.setVisible(true);
				Main.super.dispose();
			}
		});
	}

	//input file shit
	public void ReadAndProcessFile() throws FileNotFoundException 
	{
		this.inventory = new ArrayList<Item>();
		File file = new File("inventory.txt");
		Scanner scnr = new Scanner(file);

		while (scnr.hasNextLine()) 
		{
			//gets the item and divides it at the commas
			String item = scnr.nextLine();
			String[] information = item.split(",");

			//creates an item
			Item selectedItem = new Item();
			
			//sets the items ID
			selectedItem.setID((information[0]));
			
			//sets the items Name
			selectedItem.setName(information[1]);

			//sets the stock status
			if (information[2].contains("true")) 
			{
				selectedItem.setInStock(true);
			}
			else 
			{
				selectedItem.setInStock(false);
			}
			
			//sets the items price
			selectedItem.setPrice(Double.parseDouble(information[3]));
			
			//puts all of the items in an array list so we can access it without the inventory text
			inventory.add(selectedItem);
		}
		scnr.close();
	}
	
	//searches the array for the item ID
	public int searchForID(String ID) 
	{
		int size = this.inventory.size();
		Item searchedItem;
		
		for(int i = 0; i < size; i++) 
		{
			searchedItem = inventory.get(i);
				
			//returns the IDs
			if(searchedItem.getID().equals(ID)) 
			{
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) throws FileNotFoundException 
	{
		//creates and formats the window
		Main frame = new Main();
		frame.pack();
		frame.setTitle("Nile Dot Com - Fall 2021");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}