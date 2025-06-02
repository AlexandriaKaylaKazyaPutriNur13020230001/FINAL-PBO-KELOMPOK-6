package com.atk.app.model;

public class Kategori {
    private int id;
    private String namaKategori;
    
    public Kategori() {
    }
    
    public Kategori(int id, String namaKategori) {
        this.id = id;
        this.namaKategori = namaKategori;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNamaKategori() {
        return namaKategori;
    }
    
    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }
    
    @Override
    public String toString() {
        return namaKategori;
    }
} 