package com.atk.app.model;

public class Barang extends AbstractModel {
    private String id;
    private String nama;
    private int kategoriId;
    private String kategoriNama;
    private double harga;
    private int stok;
    
    public Barang() {
    }
    
    public Barang(String id, String nama, int kategoriId, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.kategoriId = kategoriId;
        this.harga = harga;
        this.stok = stok;
    }
    
    // Constructor with kategori name
    public Barang(String id, String nama, int kategoriId, String kategoriNama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.kategoriId = kategoriId;
        this.kategoriNama = kategoriNama;
        this.harga = harga;
        this.stok = stok;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public int getKategoriId() {
        return kategoriId;
    }
    
    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }
    
    public String getKategoriNama() {
        return kategoriNama;
    }
    
    public void setKategoriNama(String kategoriNama) {
        this.kategoriNama = kategoriNama;
    }
    
    public double getHarga() {
        return harga;
    }
    
    public void setHarga(double harga) {
        this.harga = harga;
    }
    
    public int getStok() {
        return stok;
    }
    
    public void setStok(int stok) {
        this.stok = stok;
    }
    
    @Override
    public String toString() {
        return nama;
    }
    
    @Override
    public boolean validate() {
        // Validasi data barang
        return id != null && !id.isEmpty() && 
               nama != null && !nama.isEmpty() && 
               kategoriId > 0 && 
               harga >= 0 && 
               stok >= 0;
    }
    
    @Override
    public String getTableName() {
        return "barang";
    }
    
    @Override
    public Object getPrimaryKey() {
        return id;
    }
} 