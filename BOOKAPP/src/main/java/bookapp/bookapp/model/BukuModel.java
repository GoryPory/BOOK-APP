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
import bookapp.bookapp.entity.Buku;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BukuModel implements BaseModel<Buku, Integer> {

    @Override
    public void create(Buku item) {
        String sql = "INSERT INTO buku (judul, penulis, harga, stok, tahun_terbit) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getJudul());
            ps.setString(2, item.getNamaPenulis());
            ps.setDouble(3, item.getHarga());
            ps.setInt(4, item.getStock());
            ps.setInt(5, item.getTahunTerbit());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setIdBuku(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menambahkan buku: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Buku> readAll() {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT id_buku, judul, penulis, harga, stok, tahun_terbit FROM buku ORDER BY id_buku ASC";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error membaca data buku: " + e.getMessage());
        }
        return list;
    }

    public Buku findById(int idBuku) {
        String sql = "SELECT id_buku, judul, penulis, harga, stok, tahun_terbit FROM buku WHERE id_buku = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBuku);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari buku by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Buku> search(String keyword) {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT id_buku, judul, penulis, harga, stok, tahun_terbit FROM buku "
                + "WHERE judul LIKE ? OR penulis LIKE ? ORDER BY id_buku ASC";
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
            System.err.println("Error mencari buku: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Buku item) {
        String sql = "UPDATE buku SET judul = ?, penulis = ?, harga = ?, stok = ?, tahun_terbit = ? WHERE id_buku = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getJudul());
            ps.setString(2, item.getNamaPenulis());
            ps.setDouble(3, item.getHarga());
            ps.setInt(4, item.getStock());
            ps.setInt(5, item.getTahunTerbit());
            ps.setInt(6, item.getIdBuku());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengubah buku: " + e.getMessage(), e);
        }
    }

    public boolean kurangiStok(int idBuku, int jumlah) {
        String sql = "UPDATE buku SET stok = stok - ? WHERE id_buku = ? AND stok >= ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, idBuku);
            ps.setInt(3, jumlah);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengurangi stok buku: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM buku WHERE id_buku = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus buku: " + e.getMessage(), e);
        }
    }

    private Buku mapRow(ResultSet rs) throws SQLException {
        return new Buku(
                rs.getInt("id_buku"),
                rs.getString("judul"),
                rs.getString("penulis"),
                rs.getDouble("harga"),
                rs.getInt("stok"),
                rs.getInt("tahun_terbit")
        );
    }
}