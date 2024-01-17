import java.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Purchase {
	public static void main(String args[])
	{
		try {
			//Class.forName(null);
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database_1","root","root");
			Statement st = conn.createStatement();
			System.out.println("Connected");
			
			int cid,cost=3000,received_amount=1500;
			int mobileno=1234567890;
			String sql="Select * from Customers where MobileNO="+1234567890+" ";
			ResultSet rs = st.executeQuery(sql);
			//System.out.println(rs.toString());
			if(rs.next())
			{
				//System.out.println(rs.getInt("Balance"));
				cid = rs.getInt("CId");
				String sql1 = "Insert into Purchase Values(default,"+cid+",sysdate(),"+cost+","+received_amount+");";
				
				//For new Customer;
				//sql ="Insert into Account Values("+cid+",(Select Max(PId) From Purchase where Purchase.CID="+cid+"),'"+rs.getString("CName")+"',0);";
				st.executeUpdate(sql1);
				//st.executeUpdate(sql);
				sql="Update Account set Balance = (\r\n"
						+ "							Select sum(Cost) as c from Purchase where Purchase.CId="+cid+"\r\n"
						+ "							)-(\r\n"
						+ "							Select sum(Received_Amount) as received from Purchase where Purchase.CId="+cid+"\r\n"
						+ "                            )\r\n"
						+ "                            where CId="+cid+";\r\n";
				st.executeUpdate(sql);
				
				sql="Select * From Account where CId="+cid+";";
				//rs=st.executeQuery(sql);
				ResultSet rs1 = st.executeQuery(sql);
				rs1.next();
				//System.out.println(rs1.getInt("CId"));
				if(rs1.getInt("Balance")==0)
				{
					sql="Insert into PurchaseHistory Values("+cid+","+mobileno+",sysdate(),(Select Received_Amount as a from Purchase where CId="+cid+" order by PurchaseDate desc LIMIT 1));";
					st.executeUpdate(sql);
				}
				sql ="Update Customers,Account Set Customers.Balance= (Select Balance From Account where Account.CId="+cid+") where Customers.CId="+cid+";";
				st.executeUpdate(sql);
				sql ="Delete from Purchase where Purchase.CId=+"+cid+" and (Select Balance from Account where Account.CId="+cid+") =0 Order By PurchaseDate asc LIMIT 3;";
				st.executeUpdate(sql);
				
				//Print Account Remaining Balance to the Customer;
				
				sql ="Select * From Customers where CId ="+cid+";";
				rs1=st.executeQuery(sql);
				rs1.next();
				System.out.println("The Account Balance of the Customer is : "+rs1.getInt("Balance"));
				
			}
			conn.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
