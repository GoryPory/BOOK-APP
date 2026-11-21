/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repo;
import java.util.ArrayList;
import bookapp.bookapp.entity.CartItem;
/**
 *
 * @author radit
 */
public class CartRepo {
    private ArrayList<CartItem> cart;

    public CartRepo(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    public Double total(){
        Double hasil=0.0;
        for(CartItem ci : cart){
            hasil+=ci.subTotal();
        }
        return hasil;
    }
}
