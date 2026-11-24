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
import bookapp.bookapp.entity.Log;
import bookapp.bookapp.entity.Users;
import bookapp.bookapp.repo.AuthRepo;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BukuModel implements BaseModel<Buku, Integer> {

    private final LogModel logModel = new LogModel();

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

            catatLog(
                    item.getIdBuku(),
                    Log.typeAksi.CREATE,
                    "Menambah buku \"" + item.getJudul() + "\" (" + item.getNamaPenulis()
                    + ", harga " + formatPlainNumber(item.getHarga())
                    + ", stok " + item.getStock() + ")"
            );
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
        Buku lama = findById(item.getIdBuku());
        String sql = "UPDATE buku SET judul = ?, penulis = ?, harga = ?, stok = ?, tahun_terbit = ? WHERE id_buku = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getJudul());
            ps.setString(2, item.getNamaPenulis());
            ps.setDouble(3, item.getHarga());
            ps.setInt(4, item.getStock());
            ps.setInt(5, item.getTahunTerbit());
            ps.setInt(6, item.getIdBuku());
            int affected = ps.executeUpdate();

            if (affected > 0) {
                String deskripsi;
                if (lama != null) {
                    String diff = buildDiff(lama, item);
                    deskripsi = "Mengubah buku \"" + lama.getJudul() + "\": "
                            + (diff.isEmpty() ? "tidak ada perubahan" : diff);
                } else {
                    deskripsi = "Mengubah buku #" + item.getIdBuku() + " \"" + item.getJudul() + "\"";
                }
                catatLog(item.getIdBuku(), Log.typeAksi.UPDATE, deskripsi);
            }
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
        Buku lama = findById(id);
        String sql = "DELETE FROM buku WHERE id_buku = ?";
        Connection conn = DatabaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();

            if (affected > 0) {
                String deskripsi = (lama != null)
                        ? "Menghapus buku #" + lama.getIdBuku() + " \"" + lama.getJudul()
                          + "\" (" + lama.getNamaPenulis() + ")"
                        : "Menghapus buku #" + id;
                // idBuku = 0 agar disimpan sebagai NULL (menghindari error FK karena baris buku sudah dihapus)
                catatLog(0, Log.typeAksi.DELETE, deskripsi);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus buku: " + e.getMessage(), e);
        }
    }

    private void catatLog(int idBuku, Log.typeAksi type, String desc) {
        Users user = AuthRepo.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        try {
            logModel.create(new Log(
                    0,
                    user.getIdUser(),
                    user.getUsername(),
                    idBuku,
                    LocalDateTime.now(),
                    desc,
                    type
            ));
        } catch (RuntimeException e) {
            System.err.println("Gagal mencatat log: " + e.getMessage());
        }
    }

    private String buildDiff(Buku lama, Buku baru) {
        StringBuilder sb = new StringBuilder();
        if (!Objects.equals(lama.getJudul(), baru.getJudul())) {
            sb.append("judul: \"").append(lama.getJudul()).append("\" -> \"").append(baru.getJudul()).append("\"; ");
        }
        if (!Objects.equals(lama.getNamaPenulis(), baru.getNamaPenulis())) {
            sb.append("penulis: ").append(lama.getNamaPenulis()).append(" -> ").append(baru.getNamaPenulis()).append("; ");
        }
        if (Double.compare(lama.getHarga(), baru.getHarga()) != 0) {
            sb.append("harga: ").append(formatPlainNumber(lama.getHarga())).append(" -> ").append(formatPlainNumber(baru.getHarga())).append("; ");
        }
        if (lama.getStock() != baru.getStock()) {
            sb.append("stok: ").append(lama.getStock()).append(" -> ").append(baru.getStock()).append("; ");
        }
        if (lama.getTahunTerbit() != baru.getTahunTerbit()) {
            sb.append("tahun: ").append(lama.getTahunTerbit()).append(" -> ").append(baru.getTahunTerbit()).append("; ");
        }
        return sb.toString();
    }

    private String formatPlainNumber(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
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