package com.atk.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/atk";
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;
    
    static {
        try {
            // Explicitly load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
            
            // Try to create the database if it doesn't exist
            try {
                createDatabaseIfNotExists();
                System.out.println("Database setup completed successfully");
            } catch (SQLException e) {
                System.err.println("Error creating database: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }
    
    private static void createDatabaseIfNotExists() throws SQLException {
        Connection tempConnection = null;
        Statement statement = null;
        
        try {
            System.out.println("Connecting to MySQL server...");
            tempConnection = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
            statement = tempConnection.createStatement();
            
            // Create database if it doesn't exist
            System.out.println("Creating database if not exists...");
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS atk_inventory");
            
            // Use the database
            System.out.println("Using database atk_inventory...");
            statement.executeUpdate("USE atk_inventory");
            
            // Create tables
            System.out.println("Creating tables...");
            createTables(statement);
            
            // Insert initial data
            System.out.println("Inserting initial data...");
            insertInitialData(statement);
            
            System.out.println("Database setup completed successfully");
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (tempConnection != null) {
                tempConnection.close();
            }
        }
    }
    
    private static void createTables(Statement statement) throws SQLException {
        // Create users table
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL" +
                ")");
        
        // Create kategori table
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS kategori (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nama_kategori VARCHAR(100) NOT NULL UNIQUE" +
                ")");
        
        // Create barang table
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS barang (" +
                "id VARCHAR(20) PRIMARY KEY, " +
                "nama VARCHAR(100) NOT NULL, " +
                "kategori_id INT NOT NULL, " +
                "harga DOUBLE NOT NULL, " +
                "stok INT NOT NULL DEFAULT 0, " +
                "FOREIGN KEY (kategori_id) REFERENCES kategori(id) ON DELETE RESTRICT" +
                ")");
        
        // Create barang_masuk table
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS barang_masuk (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "barang_id VARCHAR(20) NOT NULL, " +
                "jumlah INT NOT NULL, " +
                "tanggal TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (barang_id) REFERENCES barang(id) ON DELETE CASCADE" +
                ")");
        
        // Create penjualan table
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS penjualan (" +
                "id VARCHAR(20) PRIMARY KEY, " +
                "tanggal TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "total DOUBLE NOT NULL" +
                ")");
        
        // Create detail_penjualan table
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS detail_penjualan (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "penjualan_id VARCHAR(20) NOT NULL, " +
                "barang_id VARCHAR(20) NOT NULL, " +
                "jumlah INT NOT NULL, " +
                "subtotal DOUBLE NOT NULL, " +
                "FOREIGN KEY (penjualan_id) REFERENCES penjualan(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (barang_id) REFERENCES barang(id) ON DELETE RESTRICT" +
                ")");
    }
    
    private static void insertInitialData(Statement statement) throws SQLException {
        // Check if users table is empty
        java.sql.ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users");
        rs.next();
        int userCount = rs.getInt(1);
        
        if (userCount == 0) {
            // Insert admin user
            statement.executeUpdate("INSERT INTO users (username, password) VALUES ('admin', 'admin123')");
            
            // Insert categories
            statement.executeUpdate("INSERT INTO kategori (nama_kategori) VALUES " +
                    "('Alat Tulis'), " +
                    "('Kertas'), " +
                    "('Buku'), " +
                    "('Amplop & Map'), " +
                    "('Perlengkapan Kantor')");
            
            // Insert products
            statement.executeUpdate("INSERT INTO barang (id, nama, kategori_id, harga, stok) VALUES " +
                    "('ATK001', 'Pensil 2B', 1, 3000, 100), " +
                    "('ATK002', 'Pulpen Standard', 1, 2500, 150), " +
                    "('ATK003', 'Kertas HVS A4 80gr', 2, 45000, 50), " +
                    "('ATK004', 'Buku Tulis', 3, 5000, 80), " +
                    "('ATK005', 'Amplop Putih', 4, 15000, 100), " +
                    "('ATK006', 'Stapler', 5, 18000, 30), " +
                    "('ATK007', 'Spidol Whiteboard', 1, 8500, 60), " +
                    "('ATK008', 'Penghapus', 1, 2000, 120), " +
                    "('ATK009', 'Kertas HVS F4', 2, 48000, 40), " +
                    "('ATK010', 'Buku Nota', 3, 12000, 70)");
        }
    }
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                System.out.println("Attempting to connect to database...");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connection successful!");
            } catch (SQLException e) {
                System.err.println("Database Connection Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
} 