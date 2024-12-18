import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private static final String query = "SELECT * FROM userdetails WHERE email = ? AND password = ?";
    private static final String HOME_PAGE = "front_page.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quickmart","root","admin@123")) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Redirect to home.html upon successful login
                res.sendRedirect(HOME_PAGE);
            } else {
                // Display error message for invalid credentials
                pw.println("<h2>Login Failed</h2>");
                pw.println("<p>Invalid email or password</p>");
            }
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se);
            pw.println("<h1>Failed to login: " + se.getMessage() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>Failed to login: " + e.getMessage() + "</h1>");
        }
    }
}
