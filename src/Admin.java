import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
public class Admin extends User
{
	public void menu()
	{
		try
		{
			System.out.println("\nBus Summary");
			String sql="select * from buses order by bus_id";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			ResultSet rs=st.executeQuery();
			while(rs.next())
			{
				System.out.println("\n"+rs.getString("name"));
				int book=rs.getInt("booked");
				int cancel=rs.getInt("cancelled");
				int fare=rs.getInt("fare");
				Boolean ac=rs.getBoolean("ac");
				
				int total=book * fare;
				if(ac)
					fare=fare/2;
				else
					fare=fare/4;
				total=total+(fare * cancel);
				System.out.println("Number of seats filled: "+book);
				System.out.println("Total Fare Collected: "+total+" ("+book+" tickets + "+cancel+" cancellation )");
				System.out.println("Seat Details");
				
				
				String sql1="select * from tickets where bus_id=?";
			    PreparedStatement st1=Main.dbConnection.prepareStatement(sql1);
				st1.setString(1,rs.getString("bus_id"));
				ResultSet rs1=st1.executeQuery();
				int i=1;
				while(rs1.next())
				{
					String temp=rs1.getString("ticket_id");
					System.out.println("\n"+i+". Ticket ID:"+temp);
					System.out.print("Seat Number : "+rs1.getString("seat_number")+", ");
					System.out.print("Name : "+rs1.getString("name")+", ");
					System.out.println("Gender : "+rs1.getString("gender")+", ");
					System.out.println("Booked By : "+rs1.getString("email"));
					i++;
				}
				if(i==1)
					System.out.println("-");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
}
