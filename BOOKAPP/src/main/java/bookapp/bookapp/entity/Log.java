/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.entity;


/**
 *
 * @author Gori
 */
public class Log {
    
    public enum typeAksi {
        CREATE,
        UPDATE,
        DELETE
    }
    
    private int idLog;
    private int idUser;
    private int idBuku;
    private int cartId;
    private String desc;
    private typeAksi type;

    public Log() {
    }

    public Log(int idLog, int idUser, int idBuku, int cartId, String desc, typeAksi type) {
        this.idLog = idLog;
        this.idUser = idUser;
        this.idBuku = idBuku;
        this.cartId = cartId;
        this.desc = desc;
        this.type = type;
    }

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public typeAksi getType() {
        return type;
    }

    public void setType(typeAksi type) {
        this.type = type;
    }
}