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

public class DetailTransaksi implements Base<DetailTransaksi, Integer> {

    @Override
    public void create(DetailTransaksi item) {
        // TODO: Implementasi simpan item ke detail_transaksi
    }

    public List<DetailTransaksi> searchByTransaksi(String idTransaksi) {
        // TODO: SELECT * FROM detail_transaksi WHERE id_transaksi = ?
        return null;
    }
    
}