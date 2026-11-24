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
import bookapp.bookapp.entity.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogModel implements BaseModel<Log, Integer> {

    @Override
    public void create(Log item) {
        String sql = "INSERT INTO log_inventori (id_user, username, id_buku, datetime, `desc`, type) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        LocalDateTime waktu = item.getDatetime() != null ? item.getDatetime() : LocalDateTime.now();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getIdUser());
            ps.setString(2, item.getUsername());
            if (item.getIdBuku() > 0) {
                ps.setInt(3, item.getIdBuku());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setTimestamp(4, Timestamp.valueOf(waktu));
            ps.setString(5, item.getDesc());
            ps.setString(6, item.getType().name());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setIdLog(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan log inventori: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Log> readAll() {
        List<Log> list = new ArrayList<>();
        String sql = "SELECT id_log, id_user, username, id_buku, datetime, `desc`, type FROM log_inventori ORDER BY datetime DESC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error membaca log inventori: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Log> search(String keyword) {
        List<Log> list = new ArrayList<>();
        String sql = "SELECT id_log, id_user, id_buku, datetime, `desc`, type FROM log_inventori "
                + "WHERE `desc` LIKE ? OR type LIKE ? ORDER BY datetime DESC";
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
            System.err.println("Error mencari log inventori: " + e.getMessage());
        }
        return list;
    }

    public List<Log> readByType(Log.typeAksi type) {
        List<Log> list = new ArrayList<>();
        String sql = "SELECT id_log, id_user, id_buku, datetime, `desc`, type "
                + "FROM log_inventori WHERE type = ? ORDER BY datetime DESC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error memfilter log inventori: " + e.getMessage());
        }
        return list;
    }
    
    private Log mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("datetime");
        return new Log(
                rs.getInt("id_log"),
                rs.getInt("id_user"),
                rs.getString("username"),
                rs.getInt("id_buku"),
                ts != null ? ts.toLocalDateTime() : null,
                rs.getString("desc"),
                Log.typeAksi.valueOf(rs.getString("type"))
        );
    }
}