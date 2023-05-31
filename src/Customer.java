import java.sql.Connection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class Customer extends User
{
	private String gender;
	private String contact;
	private int tickets_booked;
	
	public Customer(){ super();}
	public Customer(String name,String email,String password,String gender,String contact,int tickets_booked)
	{
		super(name,email,password);
		this.gender=gender;
		this.contact=contact;
		this.tickets_booked=tickets_booked;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public int getTickets_booked() {
		return tickets_booked;
	}
	public void setTickets_booked(int tickets_booked) {
		this.tickets_booked = tickets_booked;
	}
	
	public static void signUp()
	{
		String name,email,contact,pwd,gender;
		System.out.println("Enter name:");
		name=sc.nextLine();

		do 
		{
			System.out.println("Enter email:");
			email=sc.nextLine();
			if(!Pattern.matches("[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*",email))
			{
				System.out.println("Enter valid Email");
				continue;
			}
			try {
				String sql="select * from users where email=?";
				PreparedStatement st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,email);
				ResultSet rs=st.executeQuery();
				
				if(!rs.next())
				break;
				else
				{
					System.out.println("User with this email already exist..");
				    continue;
				}
			}
			catch(Exception e) {
			System.out.println(e);
			}
		}
		while(true);
		
		do
		{
			System.out.println("Enter password:");
		    pwd=sc.nextLine();
			if(!Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{5,20}$",pwd))
			{
				System.out.println("Password should have atleast one uppercase.one lower case,one digit,one special character.length should be 8-20");
				continue;
			}
			break;
		}
		while(true);
		
		do
		{
			System.out.println("Enter Contact number:");
		    contact=sc.nextLine();
			if(!Pattern.matches("[6-9]{1}[0-9]{9}",contact))
			{
				System.out.println("Invalid contact Number");
				continue;
			}
			break;
		}
		while(true);
		
		do
		{
			System.out.println("Enter your gender (M/F) : ");
			gender=sc.nextLine();
			if(gender.equalsIgnoreCase("M")||gender.equalsIgnoreCase("F"))
			{
				break;
			}
			else
			{
				System.out.println("Gender value should be 'M' or 'F'");
				continue;
			}
			 
		}
		while(true);
		try
		{
			String hashPwd = BCrypt.hashpw(pwd, BCrypt.gensalt(10));//salt value should be in range 4 to 31
			String sql="insert into users values(?,?,?,?,?,0)";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,name);
			st.setString(2,email);
			st.setString(3,hashPwd);
			//System.out.println("Hashed pwd "+hashPwd);
			st.setString(4,gender);
			st.setString(5,contact);
			st.executeUpdate();
			System.out.println("SignUp successful..You can SignIn");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}  
	}
	
	public void menu()
	{
		int op;
		do
		{
			System.out.println("\n1.Book Tickets");
			System.out.println("2.View Tickets");
			System.out.println("3.Cancel Tickets");
			System.out.println("4.Exit\n");
			op=Integer.parseInt(sc.nextLine());
			switch(op)
			{
			case 1:
				bookTickets();
				break;
			case 2:
				viewTickets();
			    break;
			case 3:
				cancelTickets();
				break;
			case 4:
				break;
			default:
				System.out.println("Please enter a valid option\n");
			}
			
		}
		while(op!=4);
	}
	
	public void bookTickets()
	{
		int op;
		Bus bus;
		List<String> busIDs=new ArrayList<String>();
		int i=1;
		try
		{
			String sql="select * from buses order by available_seats desc,ac desc,type desc";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			ResultSet rs=st.executeQuery();
			while(rs.next())
			{
				System.out.println(i+" -  "+rs.getString("name")+" - "+rs.getInt("available_seats")+" seats");
				busIDs.add(rs.getString("bus_id"));
				i++;
			}
			System.out.println(i+" - Exit");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("\nSelect the Bus:");
		do
		{
			
			op=Integer.parseInt(sc.nextLine());
			String type="seater";
			if(op>0 && op<=busIDs.size())
			{
				try 
				{
					String sql="select type from buses where bus_id=?";
					PreparedStatement st = Main.dbConnection.prepareStatement(sql);
					st.setString(1,busIDs.get(op-1));
					ResultSet rs=st.executeQuery();
					rs.next();
					type=rs.getString("type");
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
				if(type.equalsIgnoreCase("seater"))
					bus=new SeaterBus(busIDs.get(op-1));
				else
					bus=new SleeperBus(busIDs.get(op-1));
				
				bus.showStatus(getEmail());
				break;
			}
			else if(op==i)
			{
				break;
			}
			else
			{
					System.out.println("Please enter a valid option\n");
					continue;
			}
		}
		while(op!=5);
		updateCustomerData();
		
	}
	List<String> tickets=new ArrayList<String>();
	public void viewTickets()
	{
		if(tickets_booked==0)
		{
			System.out.println("No Tickets Booked..\n");
			return;
		}
		System.out.println("Tickets Booked By You : "+getTickets_booked());
		try
		{
			String sql="select * from tickets where email=?";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,getEmail());
			ResultSet rs=st.executeQuery();
			
			
			int i=1;
			while(rs.next())
			{
				String temp=rs.getString("ticket_id");
				System.out.println("\n"+i+". Ticket ID:"+temp);
				tickets.add(temp.toLowerCase());
				String s=rs.getString("bus_id");
				
				sql="select name from buses where bus_id=?";
				st = Main.dbConnection.prepareStatement(sql);
				st.setString(1,s);
				ResultSet rs1=st.executeQuery();
				rs1.next();
				
				System.out.print("Bus : "+rs1.getString("name")+", ");
				System.out.print("Seat Number : "+rs.getString("seat_number")+", ");
				System.out.print("Name : "+rs.getString("name")+", ");
				System.out.println("Gender : "+rs.getString("gender")+", ");	
				i++;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
	public void cancelTickets()
	{
		viewTickets();
		if(tickets_booked==0)
		{
			return;
		}
		System.out.println("\nEnter Ticket ID to be cancelled  OR type 'ALL' to cancel all the tickets");
		String s;
		do
		{
			s=sc.nextLine();
			s=s.toLowerCase();
			if(tickets.contains(s))
			{
				cancelTickets(s);
				break;
			}
			if(s.equalsIgnoreCase("All"))
			{
				for(int i=0;i<tickets.size();i++)
				{
					cancelTickets(tickets.get(i));
				}
				break;
			}
			System.out.println("Enter valid Ticket Number : ");	
		}
		while(true);
		updateCustomerData();
		System.out.println("Cancellation successful.\n");
	}
    
	public void cancelTickets(String s)
	{
		Bus bus;
		try
		{
			String sql="select bus_id,seat_number from tickets where ticket_id ILIKE ?";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,s);
			ResultSet rs=st.executeQuery();
			String id="";
			if(!rs.next())
				return;
			id=rs.getString("bus_id");
			String seatNo=rs.getString("seat_number");
			String row=seatNo.substring(1);
			String column=seatNo.substring(0,1);
			
			sql="delete from tickets where ticket_id ILIKE ?";
			st = Main.dbConnection.prepareStatement(sql);
			st.setString(1,s);
			st.executeUpdate();
			
			sql="select type from buses where bus_id=?";
			st = Main.dbConnection.prepareStatement(sql);
			st.setString(1,id);
			rs=st.executeQuery();
			String type="";
			if(!rs.next())
				return;
			type=rs.getString("type");
			if(type.equalsIgnoreCase("seater"))
				bus=new SeaterBus(id);
			else
				bus=new SleeperBus(id);
			
			bus.cancelSeat(row,column);
			
			sql="insert into transactions(ticket_id,email,status) values(?,?,?)";
			st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,s);
			st.setString(2,getEmail());
			st.setString(3,"cancelled");
			st.executeUpdate();
			
			sql="update users set tickets_booked=tickets_booked-1 where email=?";
			st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,getEmail());
			st.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		updateCustomerData();
	}
	
	public void updateCustomerData()
	{
		try
		{
			String sql="select * from users where email=?";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,getEmail());
			ResultSet rs=st.executeQuery();
			rs.next();
			this.tickets_booked=Integer.valueOf(rs.getString("tickets_booked"));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
}

