/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import bookapp.bookapp.entity.Buku;
import java.util.List;

public class BukuModel implements BaseModel<Buku, Integer> { // Tipe ID Buku adalah Integer

    @Override
    public void create(Buku item) {
        // TODO: Implementasi INSERT INTO buku
    }

    @Override
    public List<Buku> readAll() {
        // TODO: Implementasi SELECT * FROM buku
        return null;
    }

    @Override
    public List<Buku> search(String keyword) {
        // TODO: Implementasi pencarian buku berdasarkan judul/penulis
        return null;
    }

    @Override
    public void update(Buku item) {
        // TODO: Implementasi UPDATE buku
    }

    @Override
    public void delete(Integer id) {
        // TODO: Implementasi DELETE buku
    }
}