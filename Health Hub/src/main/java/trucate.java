import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/TruncateServlet")
public class trucate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // JDBC Database URL
            String jdbcUrl = "jdbc:mysql://localhost:3306/track";

            // Database credentials
            String username = "root";
            String password = "root";

            try {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish the connection
                try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                    // Truncate data from the table (replace "your_table_name" with your actual table name)
                    String truncateQuery = "TRUNCATE TABLE search";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(truncateQuery)) {
                        preparedStatement.executeUpdate();
                        out.println("<html><body>");
                        out.println("<h2>Data truncated successfully!</h2>");
                        out.println("</body></html>");

                        // Redirect to another page after truncation
                        response.sendRedirect("home.html"); // Replace with your actual success page
                    }
                }
            } catch (Exception e) {
                out.println("<html><body>");
                out.println("<h2>Error truncating data: " + e.getMessage() + "</h2>");
                out.println("</body></html>");
                e.printStackTrace();
            }
        }
    }
}
