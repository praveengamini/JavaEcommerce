import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String query = "INSERT INTO userdetails(fullname, email, password) VALUES (?, ?, ?)";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quickmart","root","admin@123");) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, password);
            int count = ps.executeUpdate();
            if (count == 1) {
                pw.println("<h2>Account Created succcessfully</h2>");
            } else {
                pw.println("<h2>Failed in creating accounting</h2>");
            }
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se);
            pw.println("<h1>Failed to register record: " + se.getMessage() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>Failed to register record: " + e.getMessage() + "</h1>");
        }
    }
}
