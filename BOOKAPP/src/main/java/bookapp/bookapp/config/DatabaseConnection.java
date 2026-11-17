package bookapp.bookapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_bookapp?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root"; 
    
    private static final String PASSWORD = ""; 
    
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Koneksi ke database db_bookapp berhasil!");
                } catch (ClassNotFoundException e) {
                    System.err.println("Driver MySQL tidak ditemukan: " + e.getMessage());
                } catch (SQLException e) {
                    System.err.println("Gagal terhubung ke database. Pastikan MySQL sudah menyala: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengecek status koneksi: " + e.getMessage());
        }
        
        return connection;
    }
}