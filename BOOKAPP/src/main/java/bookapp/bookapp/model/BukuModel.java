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

public class BukuModel implements BaseModel<BukuModel, Integer> { // Tipe ID Buku adalah Integer

    @Override
    public void create(BukuModel item) {
        // TODO: Implementasi INSERT INTO buku
    }

    @Override
    public List<BukuModel> readAll() {
        // TODO: Implementasi SELECT * FROM buku
        return null;
    }

    @Override
    public List<BukuModel> search(String keyword) {
        // TODO: Implementasi pencarian buku berdasarkan judul/penulis
        return null;
    }

    @Override
    public void update(BukuModel item) {
        // TODO: Implementasi UPDATE buku
    }

    @Override
    public void delete(Integer id) {
        // TODO: Implementasi DELETE buku
    }
}