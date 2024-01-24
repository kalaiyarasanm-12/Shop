

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.sql.DriverManager;


/**
 * Servlet implementation class NewUser
 */
public class NewUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewUser() {
        super();
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
			PreparedStatement st = conn.prepareStatement("Insert into Customers values(default,?,?,?,0)");
			
			String name = request.getParameter("name");
			String mobileno= request.getParameter("mobileno");
			String Address = request.getParameter("address");
			
			st.setString(1, name);
			st.setString(2, mobileno);
			st.setString(3, Address);
			
			Statement st1 = conn.createStatement();
			
			PrintWriter out = response.getWriter();
			if(st.executeUpdate()==1) { out.println("New User added");
			String sql = "Select CId from Customers where Mobileno="+mobileno+";";
			ResultSet rs = st1.executeQuery(sql);
			rs.next();
			sql ="Insert into Account Values("+rs.getInt("CId")+",(Select Max(PId) From Purchase where Purchase.CID="+rs.getInt("CId")+"),'"+name+"',0);";
			st1.executeUpdate(sql);
			
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
}
