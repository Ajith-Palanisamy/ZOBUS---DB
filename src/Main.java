import java.sql.*;
import java.util.*;
public class Main 
{
	static Scanner sc=new Scanner(System.in);
	static Connection dbConnection;
	public static void main(String[] args)
	{  
		DB.initialization();
		
		dbConnection=DB.getInstance().getConnection();
		
		System.out.println("**********************");
		System.out.println("ZOBUS APPLICATION");
		System.out.println("**********************");
		int op;
		do
		{
			System.out.println("\n1.SignIn");
			System.out.println("2.SignUp");
			System.out.println("3.Exit");
			op=Integer.parseInt(sc.nextLine());
			switch(op)
			{
			case 1:
				User.signIn();
				break;
			case 2:
				Customer.signUp();
				break;
			case 3:
				System.out.println("Thank you !!Application Ended");
				break;
			default:
				System.out.println("Please enter a valid option\n");
			}
		}
		while(op!=3);
	
		try 
		{
			if(dbConnection!=null)
			dbConnection.close();
		} 
		catch (SQLException e) 
		{
			System.out.println("Connection not closed "+e);
			
		}
	}
	
}
