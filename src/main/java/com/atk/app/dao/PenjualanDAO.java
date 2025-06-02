package com.atk.app.dao;

import com.atk.app.model.Barang;
import com.atk.app.model.DetailPenjualan;
import com.atk.app.model.Penjualan;
import com.atk.app.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PenjualanDAO {
    
    private Connection connection;
    
    public PenjualanDAO() {
        connection = DatabaseConnection.getConnection();
    }
    
    public List<Penjualan> getAllPenjualan() {
        List<Penjualan> penjualanList = new ArrayList<>();
        String query = "SELECT * FROM penjualan ORDER BY tanggal DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Penjualan penjualan = new Penjualan();
                penjualan.setId(rs.getString("id"));
                penjualan.setTanggal(rs.getTimestamp("tanggal"));
                penjualan.setTotal(rs.getDouble("total"));
                penjualanList.add(penjualan);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all sales: " + e.getMessage());
        }
        
        return penjualanList;
    }
    
    public List<Penjualan> getPenjualanByDateRange(Date startDate, Date endDate) {
        List<Penjualan> penjualanList = new ArrayList<>();
        String query = "SELECT * FROM penjualan WHERE tanggal BETWEEN ? AND ? ORDER BY tanggal DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Penjualan penjualan = new Penjualan();
                    penjualan.setId(rs.getString("id"));
                    penjualan.setTanggal(rs.getTimestamp("tanggal"));
                    penjualan.setTotal(rs.getDouble("total"));
                    penjualanList.add(penjualan);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales by date range: " + e.getMessage());
        }
        
        return penjualanList;
    }
    
    public Penjualan getPenjualanById(String id) {
        String query = "SELECT * FROM penjualan WHERE id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Penjualan penjualan = new Penjualan();
                    penjualan.setId(rs.getString("id"));
                    penjualan.setTanggal(rs.getTimestamp("tanggal"));
                    penjualan.setTotal(rs.getDouble("total"));
                    return penjualan;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting sale by id: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<DetailPenjualan> getDetailPenjualanByPenjualanId(String penjualanId) {
        List<DetailPenjualan> detailList = new ArrayList<>();
        String query = "SELECT dp.*, b.nama as barang_nama FROM detail_penjualan dp " +
                      "JOIN barang b ON dp.barang_id = b.id " +
                      "WHERE dp.penjualan_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, penjualanId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetailPenjualan detail = new DetailPenjualan();
                    detail.setId(rs.getInt("id"));
                    detail.setPenjualanId(rs.getString("penjualan_id"));
                    detail.setBarangId(rs.getString("barang_id"));
                    detail.setBarangNama(rs.getString("barang_nama"));
                    detail.setJumlah(rs.getInt("jumlah"));
                    detail.setSubtotal(rs.getDouble("subtotal"));
                    detailList.add(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting sale details: " + e.getMessage());
        }
        
        return detailList;
    }
    
    public boolean addPenjualan(Penjualan penjualan, List<DetailPenjualan> detailList) {
        String penjualanQuery = "INSERT INTO penjualan (id, tanggal, total) VALUES (?, ?, ?)";
        String detailQuery = "INSERT INTO detail_penjualan (penjualan_id, barang_id, jumlah, subtotal) VALUES (?, ?, ?, ?)";
        
        try {
            // Disable auto-commit for transaction
            connection.setAutoCommit(false);
            
            // Insert penjualan
            try (PreparedStatement ps = connection.prepareStatement(penjualanQuery)) {
                ps.setString(1, penjualan.getId());
                ps.setTimestamp(2, new java.sql.Timestamp(penjualan.getTanggal().getTime()));
                ps.setDouble(3, penjualan.getTotal());
                ps.executeUpdate();
            }
            
            // Insert detail_penjualan and update stock
            try (PreparedStatement ps = connection.prepareStatement(detailQuery, Statement.RETURN_GENERATED_KEYS)) {
                BarangDAO barangDAO = new BarangDAO();
                
                for (DetailPenjualan detail : detailList) {
                    ps.setString(1, penjualan.getId());
                    ps.setString(2, detail.getBarangId());
                    ps.setInt(3, detail.getJumlah());
                    ps.setDouble(4, detail.getSubtotal());
                    ps.executeUpdate();
                    
                    // Update stock
                    Barang barang = barangDAO.getBarangById(detail.getBarangId());
                    if (barang != null) {
                        int newStock = barang.getStok() - detail.getJumlah();
                        if (newStock < 0) {
                            throw new SQLException("Insufficient stock for product: " + barang.getNama());
                        }
                        barangDAO.updateStok(detail.getBarangId(), newStock);
                    }
                    
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            detail.setId(generatedKeys.getInt(1));
                        }
                    }
                }
            }
            
            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                // Rollback transaction on error
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error adding sale: " + e.getMessage());
            return false;
        } finally {
            try {
                // Restore auto-commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error restoring auto-commit: " + e.getMessage());
            }
        }
    }
    
    public double getTotalPenjualanByDateRange(Date startDate, Date endDate) {
        String query = "SELECT SUM(total) as total_penjualan FROM penjualan WHERE tanggal BETWEEN ? AND ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_penjualan");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total sales: " + e.getMessage());
        }
        
        return 0.0;
    }
} 