package Server;
import logic.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static DBController instance = null;
    private Connection connection;

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/park_db?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static String dbPass = "";

    public static void setPassword(String pass) { dbPass = pass; }

    private DBController() {}

    public static DBController getInstance() {
        if (instance == null) instance = new DBController();
        return instance;
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, dbPass);
            System.out.println("DB connected!");
            return true;
        } catch (Exception e) {
            System.out.println("DB connection failed: " + e.getMessage());
            return false;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM order_table");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Order(
                    rs.getInt("order_number"),
                    rs.getString("order_date"),
                    rs.getInt("number_of_visitors"),
                    rs.getInt("confirmation_code"),
                    rs.getInt("subscriber_id"),
                    rs.getString("date_of_placing_order")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Query error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateOrder(int orderNumber, String newDate, int newVisitors) {
        try {
            PreparedStatement st = connection.prepareStatement(
                "UPDATE order_table SET order_date=?, number_of_visitors=? WHERE order_number=?");
            st.setString(1, newDate);
            st.setInt(2, newVisitors);
            st.setInt(3, orderNumber);
            return st.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }
}