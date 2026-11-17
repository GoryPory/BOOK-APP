/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import java.util.List;

public class TransaksiModel implements BaseModel<TransaksiModel, String> { // Tipe ID Transaksi adalah String (VARCHAR)

    @Override
    public void create(TransaksiModel item) {
        // TODO: Implementasi simpan transaksi baru
    }

    @Override
    public List<TransaksiModel> readAll() {
        // TODO: Implementasi tampilkan semua riwayat transaksi
        return null;
    }

    @Override
    public List<TransaksiModel> search(String keyword) {
        // TODO: (SURE/Opsional) Pencarian transaksi
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO: (SURE) Implementasi Hapus Transaksi
    }
}