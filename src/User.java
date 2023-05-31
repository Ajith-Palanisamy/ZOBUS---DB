import java.util.*;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
public abstract class User 
{
	private String name;
	private String email;
	private String password;
	static Scanner sc=new Scanner(System.in);

	public User(){}
	
	public User(String name,String email,String password)
	{
		this.name=name;
		this.email=email;
		this.password=password;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static void signIn()
	{
		do
		{
			System.out.println("Enter username : ");
			String email=sc.nextLine();
			try {
				String sql="select * from users where email=?";
				PreparedStatement st=Main.dbConnection.prepareStatement(sql);
				st.setString(1,email);
				ResultSet rs=st.executeQuery();
				if(rs.next())
				{
					System.out.println("Enter password: ");
					String pwd=sc.nextLine();
					boolean check = BCrypt.checkpw(pwd, rs.getString("password"));
					
					if(rs.getString("name").equals("admin") && check)
					{
						System.out.println("Welcome Admin");
						Admin obj=new Admin();
						obj.menu();
						break;
					}
					else if(check)
					{
						String name=rs.getString("name");
						
						System.out.println("\nWelcome "+name);
						Customer obj=new Customer(name,email,pwd,rs.getString("gender"),rs.getString("contact"),rs.getInt("tickets_booked"));
						obj.menu();
						break;
					}
					else
					{
						System.out.println("Incorrect password\n");
						continue;
					}
				}
				else
				{
					System.out.println("User not exist..Please SignUp");
					break;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		while(true);
	}
	
	public abstract void menu();

}

