/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author Gori
 */
public class Buku {
    private int idBuku;
    private String judul;
    private String namaPenulis;
    private double harga;
    private int stock;
    private int tahunTerbit;
    
    public Buku(){
  
    }
    
    public Buku(int idBuku, String judul, String namaPenulis, double harga, int stock, int tahunTerbit){
        this.idBuku = idBuku;
        this.judul = judul;
        this.namaPenulis = namaPenulis;
        this.harga = harga;
        this.stock = stock;
        this.tahunTerbit = tahunTerbit;
    }
    
    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getNamaPenulis() {
        return namaPenulis;
    }

    public void setNamaPenulis(String namaPenulis) {
        this.namaPenulis = namaPenulis;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }
    
}
