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
public class Transaksi {
    private int idTransaksi;
    private int idUser;
    private double totalBelanja;
    private double uangPembayaran;
    private LocalDateTime datetime;
    
    public Transaksi() {
        
    }
    
    public Transaksi(int idTransaksi, int idUser, double totalBelanja, double uangPembayaran, LocalDateTime datetime) {
        this.idTransaksi = idTransaksi;
        this.idUser = idUser;
        this.totalBelanja = totalBelanja;
        this.uangPembayaran = uangPembayaran;
        this.datetime = datetime;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public double getTotalBelanja() {
        return totalBelanja;
    }

    public void setTotalBelanja(double totalBelanja) {
        this.totalBelanja = totalBelanja;
    }

    public double getUangPembayaran() {
        return uangPembayaran;
    }

    public void setUangPembayaran(double uangPembayaran) {
        this.uangPembayaran = uangPembayaran;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}