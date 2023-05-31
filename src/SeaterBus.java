import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SeaterBus extends Bus
{
	public SeaterBus(String id)
	{
		super(id);
	}
	public void showStatus(String email)
	{
		if(getAvailable_seats()<=0)
		{
			System.out.println("Seat is not available..");
			return;
		}
		System.out.println("\n"+getName()+" - Seating Status ");
		System.out.println("Available seats : "+getAvailable_seats()+"");
		try
		{
			String sql="select * from seater_seatings where bus_id=? order by row_id";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,getId());
			ResultSet rs=st.executeQuery();
			System.out.println("-------------------------");
			System.out.println("	A	B	C	");
			System.out.println("-------------------------");
			int i=1;
			while(rs.next())
			{
				System.out.println(rs.getString("row_id")+"	"+rs.getString("A")+"	"+rs.getString("B")+"	"+rs.getString("C"));
				
				if(i==2)
				{
					System.out.println("-------------------------");
					System.out.println("-------------------------");
				}
				i++;
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		int n;
		System.out.println("\nEnter number of tickects:");
		do
		{
			n=Integer.parseInt(sc.nextLine());
			if(n>getAvailable_seats())
			{
				System.out.println("Available seats : "+getAvailable_seats()+".Enter a valid count");
				continue;
			}
			break;
		}
		while(true);
		ArrayList<String> seatNoList=new ArrayList<String>();
		ArrayList<String> genderList=new ArrayList<String>();
		ArrayList<String> nameList=new ArrayList<String>();
		for(int i=1;i<=n;i++)
		{
			System.out.println("Person "+i+" :");
			System.out.println("Enter name");
			String name=sc.nextLine();
			System.out.println("Enter Gender");
			String gender;
			do 
			{
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
			gender=gender.toUpperCase();
			System.out.println("Enter Seat Number:");
			String seatNo;
			do 
			{
				seatNo=sc.nextLine();
				if(!Pattern.matches("[ABC][1-4]",seatNo))
				{
					System.out.println("Enter a valid SeatNo");
					continue;
				}
				else
				{
					int row=Integer.valueOf(seatNo.substring(1));
					String column=seatNo.substring(0,1);
					int t;
					if(row%2==0)
					{
						t=row-1;
					}
					else
						t=row+1;
					try
					{	
						String sql="select "+column+" from seater_seatings where bus_id=? AND row_id=?";
						PreparedStatement st=Main.dbConnection.prepareStatement(sql);
						st.setString(1,getId());
						st.setInt(2,row);
						ResultSet rs=st.executeQuery();
						rs.next();
						if(!rs.getString(column).equalsIgnoreCase("A"))
						{
							System.out.println(seatNo+" is not Available.Enter a valid seat number.");
							continue;
						}
						
						sql="select "+column+" from seater_seatings where bus_id=? AND row_id=?";
						st=Main.dbConnection.prepareStatement(sql);
						st.setString(1,getId());
						st.setInt(2,t);
						rs=st.executeQuery();
						rs.next();
						String temp=rs.getString(column);
						
						if(temp.equalsIgnoreCase("F") && gender.equalsIgnoreCase("M"))
						{
							
							sql="select email from tickets where bus_id=? AND seat_number=?";
							st=Main.dbConnection.prepareStatement(sql);
							st.setString(1,getId());
							st.setString(2,column+t);
							rs=st.executeQuery();
							if(rs.next())
							{
							String bookedEmail=rs.getString("email");
							if(!bookedEmail.equalsIgnoreCase(email))
							{
								System.out.println(seatNo+" is allocated for female passenger.Select a different seat.");
								continue;
							}
							}
						}
						if(!seatNoList.contains(seatNo))
						{
							seatNoList.add(seatNo);
							genderList.add(gender);
							nameList.add(name);
							System.out.println("Seat "+seatNo+" is selected.\n");
							break;
						}
						else
						{
							System.out.println("Seat "+seatNo+" is already selected.Select a different seat.");
						}
						
						
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
			while(true);	
		}
		
		int total=getFare()*n;
		System.out.println("\nNumber of tickets booked : "+n);
		System.out.println("Total amount : "+total);
		System.out.println("\nConfirm your tickets :");
		String op;
		do
		{
			System.out.println("1.Confirm");
			System.out.println("2.Cancel");
		    op=sc.nextLine();
			if(op.equals("2"))
				return;
			else if(op.equals("1"))
				break;
			else
			{
				System.out.println("Enter valid option..");
			}
		}
		while(true);
		
		String column,gender,seatNo;
		int row;
		for(int i=0;i<n;i++)
		{
			try
			{
				seatNo=seatNoList.get(i);
				gender=genderList.get(i);
				row=Integer.valueOf(seatNo.substring(1));
				column=seatNo.substring(0,1);
				String name=nameList.get(i);
				
				String sql="update seater_seatings set "+column+"=? where bus_id=? AND row_id=?";
				PreparedStatement st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,gender);
				st.setString(2,getId());
				st.setInt(3,row);
				st.executeUpdate();
				
				sql="insert into tickets(ticket_id,email,bus_id,seat_number,gender,name) values(?,?,?,?,?,?)";
				st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,getId()+"-"+seatNo);
				st.setString(2,email);
				st.setString(3,getId());
				st.setString(4,seatNo);
				st.setString(5,gender);
				st.setString(6,name);
				st.executeUpdate();
				
				sql="insert into transactions(ticket_id,email,status) values(?,?,?)";
				st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,getId()+"-"+seatNo);
				st.setString(2,email);
				st.setString(3,"booked");
				st.executeUpdate();
				
				sql="update buses set available_seats=available_seats-1,booked=booked+1 where bus_id=?";
				st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,getId());
				st.executeUpdate();
				
				sql="update users set tickets_booked=tickets_booked+1 where email=?";
				st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,email);
				st.executeUpdate();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("Tickets booked successfully..");
		updateBusData();
	}
	
	public void cancelSeat(String row,String column)
	{
		try 
		{
			String sql="update seater_seatings set "+column+"='A' where bus_id=? AND row_id=?";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,getId());
			st.setInt(2,Integer.parseInt(row));
			st.executeUpdate();
			

			sql="update buses set available_seats=available_seats+1,cancelled=cancelled+1,booked=booked-1 where bus_id=?";
			st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,getId());
			st.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		updateBusData();
	}
}
