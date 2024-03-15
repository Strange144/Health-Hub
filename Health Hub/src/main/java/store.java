import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class store extends HttpServlet {
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
                String button1Value = request.getParameter("button1");

                if (button1Value != null && button1Value.equals("b1")) {
                    String name = request.getParameter("name");
                    String workoutDate = request.getParameter("workoutDate");
                    String exerciseType = request.getParameter("exerciseType");
                    int sets = Integer.parseInt(request.getParameter("sets"));
                    // Assuming these are parameters from an HTTP request
                    String paramRep1 = request.getParameter("rep1");
                    String paramRep2 = request.getParameter("rep2");
                    String paramRep3 = request.getParameter("rep3");
                    String paramWeight1 = request.getParameter("weight1");
                    String paramWeight2 = request.getParameter("weight2");
                    String paramWeight3 = request.getParameter("weight3");

                    // Parse parameters or set to 0 if empty
                    int rep1 = paramRep1.isEmpty() ? 0 : Integer.parseInt(paramRep1);
                    int rep2 = paramRep2.isEmpty() ? 0 : Integer.parseInt(paramRep2);
                    int rep3 = paramRep3.isEmpty() ? 0 : Integer.parseInt(paramRep3);
                    int weight1 = paramWeight1.isEmpty() ? 0 : Integer.parseInt(paramWeight1);
                    int weight2 = paramWeight2.isEmpty() ? 0 : Integer.parseInt(paramWeight2);
                    int weight3 = paramWeight3.isEmpty() ? 0 : Integer.parseInt(paramWeight3);

                    // Insert data into the database
                    String query = "INSERT INTO gymprogress (name, workout_date, exercise_type, setss, rep1, weight1, rep2, weight2, rep3, weight3) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, workoutDate);
                        preparedStatement.setString(3, exerciseType);
                        preparedStatement.setInt(4, sets);
                        preparedStatement.setInt(5, rep1);
                        preparedStatement.setInt(6, weight1);
                        preparedStatement.setInt(7, rep2);
                        preparedStatement.setInt(8, weight2);
                        preparedStatement.setInt(9, rep3);
                        preparedStatement.setInt(10, weight3);

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            request.setAttribute("name", name);
                            request.setAttribute("workoutDate", workoutDate);
                            request.setAttribute("exerciseType", exerciseType);
                            request.setAttribute("sets", sets);
                            request.setAttribute("rep1", rep1);
                            request.setAttribute("rep2", rep2);
                            request.setAttribute("rep3", rep3);
                            request.setAttribute("weight1", weight1);
                            request.setAttribute("weight2", weight2);
                            request.setAttribute("weight3", weight3);
                            RequestDispatcher dispatcher = request.getRequestDispatcher("display.jsp");
                            dispatcher.forward(request, response);
                        } else {
                            response.getWriter().println("Failed to insert data into the database.");
                        }
                    }
                } else {
                    if (connection != null) {
                        // Extract data from the form
                        response.setContentType("text/html");
                        PrintWriter out = response.getWriter();

                        try {
                            String searchName = request.getParameter("searchname");
                            String searchDate = request.getParameter("searchDate");

                            // Select data from the database based on search criteria
                            String query = "SELECT * FROM gymprogress WHERE name LIKE ? AND workout_date = ?";
                            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                preparedStatement.setString(1, "%" + searchName + "%");
                                preparedStatement.setString(2, searchDate);

                                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                    out.print("<body  style='background-color:#333232;'>");
                                    out.print("<a href=\"index.html\" style=\"text-decoration: none;\"><h1 style='background-color: black; color: #cc5500; text-align: center; padding-top: 5px; padding-bottom: 5px;'>Search Result</h1></a>");
                                    out.print("<table width=50% border=1 style='border-collapse: collapse;margin: 0 auto; margin-top: 20px; border-color:black;'>");
                                    
                                    // Printing column names with styles
                                    ResultSetMetaData rsmd = resultSet.getMetaData();
                                    int total = rsmd.getColumnCount();
                                    out.print("<tr style='background-color: #000000;'>");
                                    for (int i = 1; i <= total; i++) {
                                        out.print("<th style='padding: 10px; color: #cc5500 ;'>" + rsmd.getColumnName(i) + "</th>");
                                    }
                                    out.print("</tr>");

                                    // Printing result rows with alternating row colors
                                    boolean alternateColor = false;
                                    while (resultSet.next()) {
                                        out.print("<tr style='background-color: " + (alternateColor ? "#c5c5c5" : "#636363") + ";'>");
                                        for (int i = 1; i <= total; i++) {
                                            out.print("<td style='padding: 10px; color:white;'>" + resultSet.getString(i) + "</td>");
                                        }
                                        out.print("</tr>");
                                        alternateColor = !alternateColor; // Toggle between true and false
                                    }

                                    out.print("</table>");
                                    out.print("</body>");
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(store.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            out.close();
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
