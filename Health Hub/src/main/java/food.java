import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class food extends HttpServlet {
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
        String foodName = request.getParameter("foodName");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if (connection != null) {
                String button1Value = request.getParameter("b1");

                if (button1Value != null && button1Value.equals("b1")) {
                    String query = "SELECT * FROM food_tracker WHERE food_name = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, foodName);

                        ResultSet resultSet = preparedStatement.executeQuery();

                        PrintWriter out = response.getWriter();
                        out.print("<html><body style='background-color:#333232;'>");
                        out.print("<a href='javascript:history.go(-1);' style=\"text-decoration: none;\"><h1 style='background-color: black; color: #cc5500; text-align: center; padding-top: 5px; padding-bottom: 5px;'>Search Result</h1></a>");
                        out.print("<table width='30%' border='1' style='border-collapse: collapse; margin: 0 auto; margin-top: 20px; border-color: black;'>");

                        if (resultSet.next()) {
                            int protein = resultSet.getInt("protein");
                            int fat = resultSet.getInt("fat");
                            int carbs = resultSet.getInt("carbs");
                            int calories = resultSet.getInt("calories");

                            // Multiply values by quantity
                            protein *= quantity;
                            fat *= quantity;
                            carbs *= quantity;
                            calories *= quantity;

                            // Add table rows
                            out.println("<tr><td style='background-color: #000000;  color: #cc5500;'>Food Name:</td><td style='padding: 10px; color: white; background-color: #636363;'>" + foodName + "</td></tr>");
                            out.println("<tr><td style='background-color: #000000;   color: #cc5500;'>Quantity:</td><td style='padding: 10px; color: white; background-color: #c5c5c5;'>" + quantity + "</td></tr>");
                            out.println("<tr><td style='background-color: #000000;  color: #cc5500;'>Protein:</td><td style='padding: 10px; color: white; background-color: #636363;'>" + protein + "g</td></tr>");
                            out.println("<tr><td style='background-color: #000000;  color: #cc5500;'>Fat:</td><td style='padding: 10px; color: white; background-color: #c5c5c5;'>" + fat + "g</td></tr>");
                            out.println("<tr><td style='background-color: #000000; color: #cc5500;'>Carbs:</td><td style='padding: 10px; color: white; background-color: #636363;'>" + carbs + "g</td></tr>");
                            out.println("<tr><td style='background-color: #000000;  color: #cc5500;'>Calories:</td><td style='padding: 10px; color: white; background-color: #c5c5c5;'>" + calories + "</td></tr>");

                            String insertQuery = "INSERT INTO search (food_name, quantity, protein, fat, carbs, calories) VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                insertStatement.setString(1, foodName);
                                insertStatement.setInt(2, quantity);
                                insertStatement.setInt(3, protein);
                                insertStatement.setInt(4, fat);
                                insertStatement.setInt(5, carbs);
                                insertStatement.setInt(6, calories);
                                int rowsAffected = insertStatement.executeUpdate();

                                if (rowsAffected > 0) {
                                    response.getWriter().println(":)");
                                } else {
                                    // No rows affected, insertion failed
                                    response.getWriter().println("Failed to insert search data into the database.");
                                }
                            }
                        } else {
                            // No result found
                            out.println("<tr><td colspan='2'>No result found for food: " + foodName + "</td></tr>");
                        }

                        out.println("</table></body></html>");
                    }
                } else {
                    String searchQuery = "SELECT * FROM search";
                    try (PreparedStatement searchStatement = connection.prepareStatement(searchQuery)) {
                        ResultSet searchResultSet = searchStatement.executeQuery();

                        PrintWriter out = response.getWriter();
                        out.print("<html><body style='background-color:#333232;'>");
                        out.print("<a href=\"food.html\" style=\"text-decoration: none;\"><h1 style='background-color: black; color: #cc5500; text-align: center; padding-top: 5px; padding-bottom: 5px;'>Search Result</h1></a>");
                        out.print("<table width='50%' border='1' style='border-collapse: collapse; margin: 0 auto; margin-top: 20px; border-color: black;'>");

                        // Printing column names with styles
                        ResultSetMetaData searchRsmd = searchResultSet.getMetaData();
                        int totalColumns = searchRsmd.getColumnCount();
                        out.print("<tr style='background-color: #000000;'>");
                        for (int i = 1; i <= totalColumns; i++) {
                            out.print("<th style='padding: 10px; color: #cc5500 ;'>" + searchRsmd.getColumnName(i) + "</th>");
                        }
                        out.print("</tr>");

                        // Printing result rows with alternating row colors
                        boolean alternateColor = false;
                        while (searchResultSet.next()) {
                            out.print("<tr style='background-color: " + (alternateColor ? "#c5c5c5" : "#636363") + ";'>");
                            for (int i = 1; i <= totalColumns; i++) {
                                out.print("<td style='padding: 10px; color:white;'>" + searchResultSet.getString(i) + "</td>");
                            }
                            out.print("</tr>");
                            alternateColor = !alternateColor; // Toggle between true and false
                        }

                        out.print("<html><body style='background-color:#333232;'>");
                        out.print("<table width='20%' border='1' style='border-collapse: collapse; margin: 0 auto; margin-top: 20px; border-color: black;'>");

                        // Calculate and display total of protein, carbs, fats, and calories
                        String totalQuery = "SELECT SUM(protein) AS totalProtein, SUM(fat) AS totalFat, SUM(carbs) AS totalCarbs, SUM(calories) AS totalCalories FROM search";
                        try (PreparedStatement totalStatement = connection.prepareStatement(totalQuery)) {
                            ResultSet totalResultSet = totalStatement.executeQuery();
                            if (totalResultSet.next()) {
                                int totalProtein = totalResultSet.getInt("totalProtein");
                                int totalFat = totalResultSet.getInt("totalFat");
                                int totalCarbs = totalResultSet.getInt("totalCarbs");
                                int totalCalories = totalResultSet.getInt("totalCalories");

                                out.print("<tr><td style='background-color: #000000;   color: #cc5500;'>Total Protein:</td><td style='padding: 10px; color: white; background-color: #636363;'>" + totalProtein + "g</td></tr>");
                                out.print("<tr><td style='background-color: #000000;  color: #cc5500;'>Total Fat:</td><td style='padding: 10px; color: white; background-color: #c5c5c5;'>" + totalFat + "g</td></tr>");
                                out.print("<tr><td style='background-color: #000000;  color: #cc5500;'>Total Carbs:</td><td style='padding: 10px; color: white; background-color: #636363;'>" + totalCarbs + "g</td></tr>");
                                out.print("<tr><td style='background-color: #000000; color: #cc5500;'>Total Calories:</td><td style='padding: 10px; color: white; background-color: #c5c5c5;'>" + totalCalories + "</td></tr>");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error connecting to the database.");
        }
    }
}
