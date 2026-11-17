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

public class DetailTransaksiModel implements BaseModel<DetailTransaksiModel, Integer> {

    @Override
    public void create(DetailTransaksiModel item) {
        // TODO: Implementasi simpan item ke detail_transaksi
    }

    public List<DetailTransaksiModel> searchByTransaksi(String idTransaksi) {
        // TODO: SELECT * FROM detail_transaksi WHERE id_transaksi = ?
        return null;
    }
    
}