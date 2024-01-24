
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;

/**
 * Servlet implementation class Purchase1
 */
public class Purchase1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Purchase1() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/database_1","root","root");
			Statement st = conn.createStatement();
			System.out.println("Connected");
			
			int mobileno = Integer.parseInt(request.getParameter("number"));
			int cost=Integer.parseInt(request.getParameter("cost")),received_amount=Integer.parseInt(request.getParameter("received_cost"));
			int cid;
			
			//PrintWriter out = response.getWriter();
			//int mobileno=1234567890;
			String sql="Select * from Customers where MobileNO="+mobileno+" ";
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

}
