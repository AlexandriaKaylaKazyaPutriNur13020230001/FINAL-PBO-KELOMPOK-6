package com.atk.app.dao;

import com.atk.app.model.Kategori;
import com.atk.app.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {
    
    private Connection connection;
    
    public KategoriDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    public List<Kategori> getAllKategori() {
        List<Kategori> kategoriList = new ArrayList<>();
        String query = "SELECT * FROM kategori ORDER BY nama_kategori";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Kategori kategori = new Kategori();
                kategori.setId(rs.getInt("id"));
                kategori.setNamaKategori(rs.getString("nama_kategori"));
                kategoriList.add(kategori);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
        }
        
        return kategoriList;
    }
    
    public Kategori getKategoriById(int id) {
        String query = "SELECT * FROM kategori WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Kategori kategori = new Kategori();
                    kategori.setId(rs.getInt("id"));
                    kategori.setNamaKategori(rs.getString("nama_kategori"));
                    return kategori;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting category by id: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean addKategori(Kategori kategori) {
        String query = "INSERT INTO kategori (nama_kategori) VALUES (?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, kategori.getNamaKategori());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateKategori(Kategori kategori) {
        String query = "UPDATE kategori SET nama_kategori = ? WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, kategori.getNamaKategori());
            ps.setInt(2, kategori.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteKategori(int id) {
        String query = "DELETE FROM kategori WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }
} 