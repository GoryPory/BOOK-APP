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
import bookapp.bookapp.entity.DetailTransaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetailTransaksiModel implements BaseModel<DetailTransaksi, Integer> {

    @Override
    public void create(DetailTransaksi item) {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_buku, jumlah, subtotal) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getIdTransaksi());
            ps.setInt(2, item.getIdBuku());
            ps.setInt(3, item.getJumlah());
            ps.setDouble(4, item.getSubTotal());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setIdDetail(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan detail transaksi: " + e.getMessage(), e);
        }
    }

    public List<DetailTransaksi> searchByTransaksi(int idTransaksi) {
        List<DetailTransaksi> list = new ArrayList<>();
        String sql = "SELECT id_detail, id_transaksi, id_buku, jumlah, subtotal "
                + "FROM detail_transaksi WHERE id_transaksi = ? ORDER BY id_detail ASC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DetailTransaksi(
                            rs.getInt("id_detail"),
                            rs.getInt("id_transaksi"),
                            rs.getInt("id_buku"),
                            rs.getInt("jumlah"),
                            rs.getDouble("subtotal")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error membaca detail transaksi: " + e.getMessage());
        }
        return list;
    }
}