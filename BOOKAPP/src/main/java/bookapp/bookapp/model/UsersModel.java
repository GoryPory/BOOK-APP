/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import bookapp.bookapp.config.DatabaseConnection;
import bookapp.bookapp.entity.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsersModel implements BaseModel<Users, Integer> {

    public Users login(String username, String password) {
        String sql = "SELECT id_user, username, password, role FROM users WHERE username = ? AND password = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error login user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void create(Users item) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getUsername());
            ps.setString(2, item.getPassword());
            ps.setString(3, item.getRole());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setIdUser(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menambahkan user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Users> readAll() {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT id_user, username, password, role FROM users ORDER BY id_user ASC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error membaca data users: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Users> search(String keyword) {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT id_user, username, password, role FROM users WHERE username LIKE ? OR role LIKE ? ORDER BY id_user ASC";
        Connection conn = DatabaseConnection.getConnection();
        String pattern = "%" + keyword + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari user: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Users item) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id_user = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getUsername());
            ps.setString(2, item.getPassword());
            ps.setString(3, item.getRole());
            ps.setInt(4, item.getIdUser());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengubah user: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id_user = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus user: " + e.getMessage(), e);
        }
    }

    private Users mapRow(ResultSet rs) throws SQLException {
        return new Users(
                rs.getInt("id_user"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}