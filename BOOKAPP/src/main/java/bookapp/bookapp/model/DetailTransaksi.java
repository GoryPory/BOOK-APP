/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

import java.time.LocalDateTime;

/**
 *
 * @author Gori
 */
public class DetailTransaksi {
    private int idDetail;
    private int idTransaksi;
    private int idBuku;
    private double jumlah;
    private double subTotal;
    
    public DetailTransaksi() {
        
    }
    
    public DetailTransaksi(int idDetail, int idTransaksi, int idBuku, double jumlah, double subTotal) {
        this.idDetail = idDetail;
        this.idTransaksi = idTransaksi;
        this.idBuku = idBuku;
        this.jumlah = jumlah;
        this.subTotal = subTotal;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}