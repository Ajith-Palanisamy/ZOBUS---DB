import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class Bus 
{
	private String id;
	private String name;
	private String type;
	private String ac;
	private int available_seats;
	private int booked;
	private int cancelled;
	private int fare;
	Scanner sc=new Scanner(System.in);
	
	
	Bus(String id)
	{
		this.id=id;
		try
		{
			String sql="select * from buses where bus_id=?";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,id);
			ResultSet rs=st.executeQuery();
			while(rs.next())
			{
				this.name=rs.getString("name");
				this.type=rs.getString("type");
				this.ac=rs.getString("ac");
				this.available_seats=rs.getInt("available_seats");
				this.booked=rs.getInt("booked");
				this.cancelled=rs.getInt("cancelled");
				this.fare=rs.getInt("fare");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public int getAvailable_seats() {
		return available_seats;
	}

	public void setAvailable_seats(int available_seats) {
		this.available_seats = available_seats;
	}

	public int getBooked() {
		return booked;
	}

	public void setBooked(int booked) {
		this.booked = booked;
	}
	public int getCancelled() {
		return cancelled;
	}
	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}
	public int getFare() {
		return fare;
	}
	public void setFare(int fare) {
		this.fare = fare;
	}
	
    public abstract void showStatus(String email);
	public abstract void cancelSeat(String row,String column);
	
	public void updateBusData()
	{
		try
		{
			String sql="select * from buses where bus_id=?";
			PreparedStatement st=Main.dbConnection.prepareStatement(sql);
			st.setString(1,id);
			ResultSet rs=st.executeQuery();
			rs.next();
			this.available_seats=Integer.valueOf(rs.getString("available_seats"));
			this.booked=Integer.valueOf(rs.getString("booked"));
			this.cancelled=Integer.parseInt(rs.getString("cancelled"));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
}
