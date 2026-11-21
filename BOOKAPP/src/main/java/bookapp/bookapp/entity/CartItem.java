/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.entity;
import bookapp.bookapp.entity.Buku;
/**
 *
 * @author radit
 */
public class CartItem {
    private Buku buku;
    private int jmlh;
    
    public CartItem(Buku buku, int jmlh){
        this.buku = buku;
        this.jmlh = jmlh;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public int getJmlh() {
        return jmlh;
    }

    public void setJmlh(int jmlh) {
        this.jmlh = jmlh;
    }
    
    public Double subTotal(){
        return buku.getHarga()*jmlh;
    }
}
