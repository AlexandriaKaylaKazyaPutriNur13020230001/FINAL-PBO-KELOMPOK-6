package com.atk.app.dao;

import com.atk.app.util.DatabaseConnection;
import java.sql.Connection;

/**
 * Kelas abstrak yang menyediakan fungsionalitas dasar untuk semua DAO
 */
public abstract class AbstractDAO {
    
    protected Connection connection;
    
    /**
     * Konstruktor yang menginisialisasi koneksi database
     */
    public AbstractDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Mendapatkan koneksi database
     * @return objek Connection
     */
    protected Connection getConnection() {
        return this.connection;
    }
    
    /**
     * Menutup koneksi database jika diperlukan
     */
    protected void closeConnection() {
        // Tidak menutup koneksi di sini karena menggunakan singleton connection
        // Hanya untuk keperluan override jika diperlukan
    }
    
    /**
     * Metode abstrak untuk mendapatkan nama tabel
     * @return nama tabel dalam database
     */
    protected abstract String getTableName();
} 