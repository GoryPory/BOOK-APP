/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.repo;
import java.util.ArrayList;
import bookapp.bookapp.entity.Buku;
import bookapp.bookapp.entity.CartItem;
/**
 *
 * @author radit
 */
public class CartRepo {
    private static CartRepo instance;
    private ArrayList<CartItem> cart;

    public CartRepo() {
        this.cart = new ArrayList<>();
    }

    public CartRepo(ArrayList<CartItem> cart) {
        this.cart = (cart != null) ? cart : new ArrayList<>();
    }

    public static CartRepo getInstance() {
        if (instance == null) {
            instance = new CartRepo();
        }
        return instance;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = (cart != null) ? cart : new ArrayList<>();
    }

    public int getQuantityByBookId(int idBuku) {
        for (CartItem ci : cart) {
            if (ci.getBuku().getIdBuku() == idBuku) {
                return ci.getJmlh();
            }
        }
        return 0;
    }

    public void addItem(Buku buku, int jumlah) {
        for (CartItem ci : cart) {
            if (ci.getBuku().getIdBuku() == buku.getIdBuku()) {
                ci.setBuku(buku);
                ci.setJmlh(ci.getJmlh() + jumlah);
                return;
            }
        }
        cart.add(new CartItem(buku, jumlah));
    }

    public void removeItemAt(int index) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
    }
    
    public void clear() {
        cart.clear();
    }

    public Double total() {
        Double hasil = 0.0;
        for (CartItem ci : cart) {
            hasil += ci.subTotal();
        }
        return hasil;
    }
}