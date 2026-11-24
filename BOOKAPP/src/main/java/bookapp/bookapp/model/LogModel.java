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
import java.time.LocalDate;

public class LogModel implements BaseModel<Log, Integer> {

    @Override
    public void create(Log item) {
        String sql = "INSERT INTO log_inventori (id_user, id_buku, datetime, `desc`, type) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        LocalDateTime waktu = item.getDatetime() != null ? item.getDatetime() : LocalDateTime.now();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getIdUser());
            ps.setInt(2, item.getIdBuku());
            ps.setTimestamp(3, Timestamp.valueOf(waktu));
            ps.setString(4, item.getDesc());
            ps.setString(5, item.getType().name());
            ps.executeUpdate();

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
        String sql = "SELECT id_log, id_user, id_buku, datetime, `desc`, type FROM log_inventori ORDER BY datetime DESC";
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
    
    public List<Log> findByFilter(Log.typeAksi type, LocalDate dari, LocalDate sampai) {
        List<Log> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id_log, id_user, id_buku, datetime, `desc`, type FROM log_inventori WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (type != null) {
            sql.append(" AND type = ?");
            params.add(type.name());
        }
        if (dari != null) {
            sql.append(" AND datetime >= ?");
            params.add(Timestamp.valueOf(dari.atStartOfDay()));
        }
        if (sampai != null) {
            sql.append(" AND datetime < ?");
            params.add(Timestamp.valueOf(sampai.plusDays(1).atStartOfDay()));
        }
        sql.append(" ORDER BY datetime DESC");

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String s) {
                    ps.setString(i + 1, s);
                } else if (param instanceof Timestamp ts) {
                    ps.setTimestamp(i + 1, ts);
                }
            }
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
                rs.getInt("id_buku"),
                ts != null ? ts.toLocalDateTime() : null,
                rs.getString("desc"),
                Log.typeAksi.valueOf(rs.getString("type"))
        );
    }
}