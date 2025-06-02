package com.atk.app.model;

import java.util.Date;

public class Penjualan {
    private String id;
    private Date tanggal;
    private double total;
    
    public Penjualan() {
    }
    
    public Penjualan(String id, Date tanggal, double total) {
        this.id = id;
        this.tanggal = tanggal;
        this.total = total;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Date getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
} 