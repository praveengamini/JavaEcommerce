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

@WebServlet("/reset_password")
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String query = "SELECT * FROM userdetails WHERE email = ? AND password = ?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        String email = req.getParameter("email");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quickmart","root","admin@123")) {
            // Check if old password matches
            PreparedStatement checkOldPassword = con.prepareStatement("SELECT * FROM userdetails WHERE email = ? AND password = ?");
            checkOldPassword.setString(1, email);
            checkOldPassword.setString(2, oldPassword);
            ResultSet rs = checkOldPassword.executeQuery();
            if (rs.next()) {
                // Update password
                PreparedStatement updatePassword = con.prepareStatement("UPDATE userdetails SET password = ? WHERE email = ?");
                updatePassword.setString(1, newPassword);
                updatePassword.setString(2, email);
                int rowsAffected = updatePassword.executeUpdate();
                if (rowsAffected > 0) {
                    pw.println("<h2>Password Updated Successfully</h2>");
                } else {
                    pw.println("<h2>Error Updating Password</h2>");
                }
            } else {
                pw.println("<h2>Invalid Email or Old Password</h2>");
            }
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se);
            pw.println("<h1>Failed to update password: " + se.getMessage() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h1>Failed to update password: " + e.getMessage() + "</h1>");
        }
    }
}
