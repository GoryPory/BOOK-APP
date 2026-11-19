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
import bookapp.bookapp.entity.Transaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaksiModel implements BaseModel<Transaksi, Integer> {

    @Override
    public void create(Transaksi item) {
        String sql = "INSERT INTO transaksi (datetime, total_belanja, tunai, id_user) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        LocalDateTime waktu = item.getDatetime() != null ? item.getDatetime() : LocalDateTime.now();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(waktu));
            ps.setDouble(2, item.getTotalBelanja());
            ps.setDouble(3, item.getUangPembayaran());
            ps.setInt(4, item.getIdUser());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setIdTransaksi(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan transaksi: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transaksi> readAll() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT id_transaksi, datetime, total_belanja, tunai, id_user FROM transaksi ORDER BY datetime DESC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error membaca data transaksi: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Transaksi> search(String keyword) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT id_transaksi, datetime, total_belanja, tunai, id_user FROM transaksi "
                + "WHERE CAST(id_transaksi AS CHAR) LIKE ? OR CAST(datetime AS CHAR) LIKE ? ORDER BY datetime DESC";
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
            System.err.println("Error mencari transaksi: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus transaksi: " + e.getMessage(), e);
        }
    }

    private Transaksi mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("datetime");
        return new Transaksi(
                rs.getInt("id_transaksi"),
                rs.getInt("id_user"),
                rs.getDouble("total_belanja"),
                rs.getDouble("tunai"),
                ts != null ? ts.toLocalDateTime() : null
        );
    }
}