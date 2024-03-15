import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class insertfood extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/track";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if (connection != null) {
                String foodName = request.getParameter("foodName");
                int protein = Integer.parseInt(request.getParameter("protein"));
                int fat = Integer.parseInt(request.getParameter("fat"));
                int carbs = Integer.parseInt(request.getParameter("carbs"));
                int calories = Integer.parseInt(request.getParameter("calories"));

                // Insert data into the database
                String query = "INSERT INTO food_tracker (food_name, protein, fat, carbs, calories) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, foodName);
                    preparedStatement.setInt(2, protein);
                    preparedStatement.setInt(3, fat);
                    preparedStatement.setInt(4, carbs);
                    preparedStatement.setInt(5, calories);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        request.setAttribute("foodName", foodName);
                    request.setAttribute("protein", protein);
                    request.setAttribute("fat", fat);
                    request.setAttribute("carbs", carbs);
                    request.setAttribute("calories", calories);

                    // Forward to a success page
                    RequestDispatcher dispatcher = request.getRequestDispatcher("insertfood.jsp");
                    dispatcher.forward(request, response);
                    } else {
                        response.getWriter().println("Failed to insert data into the database.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error connecting to the database.");
        }
    }
}
