package com.atk.app.dao;

import com.atk.app.model.Barang;
import com.atk.app.model.BarangMasuk;
import com.atk.app.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BarangMasukDAO {
    
    private Connection connection;
    
    public BarangMasukDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    public List<BarangMasuk> getAllBarangMasuk() {
        List<BarangMasuk> barangMasukList = new ArrayList<>();
        String query = "SELECT bm.*, b.nama as barang_nama FROM barang_masuk bm " +
                      "JOIN barang b ON bm.barang_id = b.id " +
                      "ORDER BY bm.tanggal DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                BarangMasuk barangMasuk = new BarangMasuk();
                barangMasuk.setId(rs.getInt("id"));
                barangMasuk.setBarangId(rs.getString("barang_id"));
                barangMasuk.setBarangNama(rs.getString("barang_nama"));
                barangMasuk.setJumlah(rs.getInt("jumlah"));
                barangMasuk.setTanggal(rs.getTimestamp("tanggal"));
                barangMasukList.add(barangMasuk);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all incoming products: " + e.getMessage());
        }
        
        return barangMasukList;
    }
    
    public List<BarangMasuk> getBarangMasukByDateRange(Date startDate, Date endDate) {
        List<BarangMasuk> barangMasukList = new ArrayList<>();
        String query = "SELECT bm.*, b.nama as barang_nama FROM barang_masuk bm " +
                      "JOIN barang b ON bm.barang_id = b.id " +
                      "WHERE bm.tanggal BETWEEN ? AND ? " +
                      "ORDER BY bm.tanggal DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BarangMasuk barangMasuk = new BarangMasuk();
                    barangMasuk.setId(rs.getInt("id"));
                    barangMasuk.setBarangId(rs.getString("barang_id"));
                    barangMasuk.setBarangNama(rs.getString("barang_nama"));
                    barangMasuk.setJumlah(rs.getInt("jumlah"));
                    barangMasuk.setTanggal(rs.getTimestamp("tanggal"));
                    barangMasukList.add(barangMasuk);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting incoming products by date range: " + e.getMessage());
        }
        
        return barangMasukList;
    }
    
    public List<BarangMasuk> getBarangMasukByBarangId(String barangId) {
        List<BarangMasuk> barangMasukList = new ArrayList<>();
        String query = "SELECT bm.*, b.nama as barang_nama FROM barang_masuk bm " +
                      "JOIN barang b ON bm.barang_id = b.id " +
                      "WHERE bm.barang_id = ? " +
                      "ORDER BY bm.tanggal DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, barangId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BarangMasuk barangMasuk = new BarangMasuk();
                    barangMasuk.setId(rs.getInt("id"));
                    barangMasuk.setBarangId(rs.getString("barang_id"));
                    barangMasuk.setBarangNama(rs.getString("barang_nama"));
                    barangMasuk.setJumlah(rs.getInt("jumlah"));
                    barangMasuk.setTanggal(rs.getTimestamp("tanggal"));
                    barangMasukList.add(barangMasuk);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting incoming products by product id: " + e.getMessage());
        }
        
        return barangMasukList;
    }
    
    public boolean addBarangMasuk(BarangMasuk barangMasuk) {
        String query = "INSERT INTO barang_masuk (barang_id, jumlah, tanggal) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, barangMasuk.getBarangId());
            ps.setInt(2, barangMasuk.getJumlah());
            ps.setTimestamp(3, new java.sql.Timestamp(barangMasuk.getTanggal().getTime()));
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                // Update stock in barang table
                BarangDAO barangDAO = new BarangDAO();
                Barang barang = barangDAO.getBarangById(barangMasuk.getBarangId());
                if (barang != null) {
                    int newStock = barang.getStok() + barangMasuk.getJumlah();
                    barangDAO.updateStok(barangMasuk.getBarangId(), newStock);
                }
                
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        barangMasuk.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding incoming product: " + e.getMessage());
            return false;
        }
    }
} 