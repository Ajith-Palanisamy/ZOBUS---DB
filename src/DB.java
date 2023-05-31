import java.util.*;
import java.sql.*;

public class DB 
{
	private static DB obj;
	private DB(){	}
	private static Connection con=null;
	private static String db="zobus";
	
	public static void initialization()
	{
		try
		{
		Class.forName("org.postgresql.Driver");
	    con = DriverManager.getConnection("jdbc:postgresql://localhost:3307/","postgres","root");
	    
	    //To check whether the database is exist
	    String sql="select exists(SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower(?))";
	    PreparedStatement st=con.prepareStatement(sql);
	    st.setString(1,db);
	    ResultSet rs=st.executeQuery();
	    rs.next();
	    if(rs.getBoolean(1))
	    {
	    	con.close();
	    	return;
	    }
	    
	    
	    sql="create database "+db;
	    st=con.prepareStatement(sql);
		st.executeUpdate();
		
		con = getConnection();
		sql="create table if not exists users(name varchar(40),email varchar(20),password varchar(20),gender char(4),contact varchar(10),tickets_booked int,CONSTRAINT users_pk PRIMARY KEY(email) )";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		
		sql="insert into users(name,email,password) values(?,?,?)";
		st=con.prepareStatement(sql);
		st.setString(1,"admin");
		st.setString(2,"admin@gmail.com");
		st.setString(3,"Admin@123");
		st.executeUpdate();
		
		
		sql="create table if not exists buses(bus_id varchar(10),name varchar(20),type varchar(10),ac boolean,fare int,available_seats int,booked int,cancelled int,CONSTRAINT buses_pk PRIMARY KEY(bus_id))";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		
		sql="insert into buses values('ac001','AC Seater','seater',true,550,12,0,0),('ac002','AC Sleeper','sleeper',true,700,12,0,0),('non-ac001','Non-AC Seater','seater',false,450,12,0,0),('non-ac002','Non-AC Sleeper','sleeper',false,600,12,0,0)";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		
		sql="create table if not exists tickets(ticket_id varchar(30),email varchar(20),bus_id varchar(20),seat_number varchar(5),gender varchar(4),name varchar(20),CONSTRAINT tickets_pk PRIMARY KEY(ticket_id),CONSTRAINT tickets_fk1 FOREIGN KEY(email) REFERENCES Users(email),CONSTRAINT tickets_fk2 FOREIGN KEY(bus_id) REFERENCES Buses(bus_id))";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		sql="create table if not exists transactions(id SERIAL,ticket_id varchar(30),email varchar(20),status varchar(20),date timestamp without time zone DEFAULT LOCALTIMESTAMP(0),CONSTRAINT transactions_pk PRIMARY KEY(id),CONSTRAINT transactions_fk1 FOREIGN KEY(email) REFERENCES Users(email),CONSTRAINT transactions_fk2 FOREIGN KEY(ticket_id) REFERENCES Tickets(ticket_id))";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		sql="create table if not exists seater_seatings(bus_id varchar(10),row_id int,a varchar(10) default 'A',b varchar(10) default 'A',c varchar(10) default 'A',CONSTRAINT seater_seatings_fk FOREIGN KEY(bus_id) REFERENCES Buses(bus_id))";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		sql="insert into seater_seatings(bus_id,row_id) values('ac001',1),('ac001',2),('ac001',3),('ac001',4),('non-ac001',1),('non-ac001',2),('non-ac001',3),('non-ac001',4)";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		
		sql="create table if not exists sleeper_seatings(bus_id varchar(10),row_id int,a varchar(10) default 'A',b varchar(10) default 'A',CONSTRAINT sleeper_seatings_fk FOREIGN KEY(bus_id) REFERENCES Buses(bus_id))";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		sql="insert into sleeper_seatings(bus_id,row_id) values('ac002',1),('ac002',2),('ac002',3),('ac002',4),('ac002',5),('ac002',6),('non-ac002',1),('non-ac002',2),('non-ac002',3),('non-ac002',4),('non-ac002',5),('non-ac002',6)";
		st=con.prepareStatement(sql);
		st.executeUpdate();
		
		con.close();
		}
		catch(Exception e)
		{
			System.out.println("Problem in DB initialization "+e);
			System.exit(0);
		}
	}
	
	public static DB getInstance()
	{
		if(obj==null)
			obj=new DB();
		 
		return obj;
	}
	
	public static Connection getConnection()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:3307/"+db,"postgres","root");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return con;
	}

}
