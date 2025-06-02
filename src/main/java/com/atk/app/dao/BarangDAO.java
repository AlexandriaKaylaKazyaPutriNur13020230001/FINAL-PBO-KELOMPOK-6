package com.atk.app.dao;

import com.atk.app.model.Barang;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO extends AbstractDAO {
    
    public BarangDAO() {
        super(); // Memanggil konstruktor parent class
    }
    
    @Override
    protected String getTableName() {
        return "barang";
    }
    
    public List<Barang> getAllBarang() {
        List<Barang> barangList = new ArrayList<>();
        String query = "SELECT b.*, k.nama_kategori FROM barang b " +
                      "JOIN kategori k ON b.kategori_id = k.id " +
                      "ORDER BY b.nama";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Barang barang = new Barang();
                barang.setId(rs.getString("id"));
                barang.setNama(rs.getString("nama"));
                barang.setKategoriId(rs.getInt("kategori_id"));
                barang.setKategoriNama(rs.getString("nama_kategori"));
                barang.setHarga(rs.getDouble("harga"));
                barang.setStok(rs.getInt("stok"));
                barangList.add(barang);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
        }
        
        return barangList;
    }
    
    public List<Barang> getBarangByKategori(int kategoriId) {
        List<Barang> barangList = new ArrayList<>();
        String query = "SELECT b.*, k.nama_kategori FROM barang b " +
                      "JOIN kategori k ON b.kategori_id = k.id " +
                      "WHERE b.kategori_id = ? " +
                      "ORDER BY b.nama";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, kategoriId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Barang barang = new Barang();
                    barang.setId(rs.getString("id"));
                    barang.setNama(rs.getString("nama"));
                    barang.setKategoriId(rs.getInt("kategori_id"));
                    barang.setKategoriNama(rs.getString("nama_kategori"));
                    barang.setHarga(rs.getDouble("harga"));
                    barang.setStok(rs.getInt("stok"));
                    barangList.add(barang);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
        }
        
        return barangList;
    }
    
    public Barang getBarangById(String id) {
        String query = "SELECT b.*, k.nama_kategori FROM barang b " +
                      "JOIN kategori k ON b.kategori_id = k.id " +
                      "WHERE b.id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Barang barang = new Barang();
                    barang.setId(rs.getString("id"));
                    barang.setNama(rs.getString("nama"));
                    barang.setKategoriId(rs.getInt("kategori_id"));
                    barang.setKategoriNama(rs.getString("nama_kategori"));
                    barang.setHarga(rs.getDouble("harga"));
                    barang.setStok(rs.getInt("stok"));
                    return barang;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by id: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean addBarang(Barang barang) {
        String query = "INSERT INTO barang (id, nama, kategori_id, harga, stok) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, barang.getId());
            ps.setString(2, barang.getNama());
            ps.setInt(3, barang.getKategoriId());
            ps.setDouble(4, barang.getHarga());
            ps.setInt(5, barang.getStok());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateBarang(Barang barang) {
        String query = "UPDATE barang SET nama = ?, kategori_id = ?, harga = ?, stok = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, barang.getNama());
            ps.setInt(2, barang.getKategoriId());
            ps.setDouble(3, barang.getHarga());
            ps.setInt(4, barang.getStok());
            ps.setString(5, barang.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateStok(String id, int stok) {
        String query = "UPDATE barang SET stok = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, stok);
            ps.setString(2, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product stock: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteBarang(String id) {
        String query = "DELETE FROM barang WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }
    
    public List<Barang> searchBarang(String keyword) {
        List<Barang> barangList = new ArrayList<>();
        String query = "SELECT b.*, k.nama_kategori FROM barang b " +
                      "JOIN kategori k ON b.kategori_id = k.id " +
                      "WHERE b.nama LIKE ? OR b.id LIKE ? " +
                      "ORDER BY b.nama";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Barang barang = new Barang();
                    barang.setId(rs.getString("id"));
                    barang.setNama(rs.getString("nama"));
                    barang.setKategoriId(rs.getInt("kategori_id"));
                    barang.setKategoriNama(rs.getString("nama_kategori"));
                    barang.setHarga(rs.getDouble("harga"));
                    barang.setStok(rs.getInt("stok"));
                    barangList.add(barang);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        
        return barangList;
    }
} 