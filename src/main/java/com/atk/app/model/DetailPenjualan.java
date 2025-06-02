package com.atk.app.model;

public class DetailPenjualan {
    private int id;
    private String penjualanId;
    private String barangId;
    private String barangNama;
    private int jumlah;
    private double subtotal;
    
    public DetailPenjualan() {
    }
    
    public DetailPenjualan(int id, String penjualanId, String barangId, int jumlah, double subtotal) {
        this.id = id;
        this.penjualanId = penjualanId;
        this.barangId = barangId;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }
    
    public DetailPenjualan(int id, String penjualanId, String barangId, String barangNama, int jumlah, double subtotal) {
        this.id = id;
        this.penjualanId = penjualanId;
        this.barangId = barangId;
        this.barangNama = barangNama;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPenjualanId() {
        return penjualanId;
    }
    
    public void setPenjualanId(String penjualanId) {
        this.penjualanId = penjualanId;
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
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
} 