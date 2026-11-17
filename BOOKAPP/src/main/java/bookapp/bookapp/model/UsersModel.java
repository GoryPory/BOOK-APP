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

public class UsersModel implements BaseModel<UsersModel, Integer> {

    @Override
    public void create(UsersModel item) {
        // TODO: Implementasi tambah user/kasir baru
    }

    @Override
    public List<UsersModel> readAll() {
        // TODO: Implementasi ambil data semua user
        return null;
    }

    @Override
    public List<UsersModel> search(String keyword) {
        // TODO: Implementasi cari user
        return null;
    }

    @Override
    public void update(UsersModel item) {
        // TODO: Implementasi ubah password/role user
    }

    @Override
    public void delete(Integer id) {
        // TODO: Implementasi hapus akun user
    }
}