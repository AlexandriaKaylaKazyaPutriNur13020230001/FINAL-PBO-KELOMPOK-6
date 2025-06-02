package com.atk.app.model;

import java.util.Date;

public class BarangMasuk {
    private int id;
    private String barangId;
    private String barangNama;
    private int jumlah;
    private Date tanggal;
    
    public BarangMasuk() {
    }
    
    public BarangMasuk(int id, String barangId, int jumlah, Date tanggal) {
        this.id = id;
        this.barangId = barangId;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }
    
    public BarangMasuk(int id, String barangId, String barangNama, int jumlah, Date tanggal) {
        this.id = id;
        this.barangId = barangId;
        this.barangNama = barangNama;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getBarangId() {
        return barangId;
    }
    
    public void setBarangId(String barangId) {
        this.barangId = barangId;
    }
    
    public String getBarangNama() {
        return barangNama;
    }
    
    public void setBarangNama(String barangNama) {
        this.barangNama = barangNama;
    }
    
    public int getJumlah() {
        return jumlah;
    }
    
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    
    public Date getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
} 