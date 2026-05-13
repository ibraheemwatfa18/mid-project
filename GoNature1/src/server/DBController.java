package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBController {
    
    public static Connection conn;

    @SuppressWarnings("deprecation")
    public static void connectToDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
            System.out.println("Driver definition failed");
        }
        
        try {
            // IMPORTANT: Change "fadikaz1234" to your actual database password!
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/park_system_db?serverTimezone=UTC", "root", "fadikaz1234");
            System.out.println("SQL connection succeed");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    public static String parsingTheData(ArrayList<String> data) {
        // data.get(0) is the "send" keyword
        String id = data.get(1);
        String name = data.get(2);
        String surname = data.get(3);
        String faculty = data.get(4);
        
        return saveUserToDB(id, name, surname, faculty);
    }

    public static String saveUserToDB(String id, String name, String surname, String faculty) {
        String query = "INSERT INTO students (id, name, surname, faculty) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, faculty);
            
            ps.executeUpdate();
            return "Success! Student " + name + " " + surname + " was saved to the DB.";
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error saving student: " + e.getMessage();
        }
    }
}