package com.atk.app.ui;

import com.atk.app.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {
    
    // Warna tema
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    private final Color BUTTON_COLOR = new Color(156, 138, 112); // Darker beige/brown
    private final Color HEADER_COLOR = BUTTON_COLOR;
    
    private JButton btnManajemenProduk;
    private JButton btnManajemenStok;
    private JButton btnLaporan;
    private JButton btnLogout;
    private JPanel contentPanel;
    private User currentUser;
    
    // Panel konten
    private BarangForm barangPanel;
    private BarangMasukForm stokPanel;
    private LaporanForm laporanPanel;
    
    public MainForm(User user) {
        this.currentUser = user;
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistem Manajemen Inventaris - Admin Dashboard");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header navigation panel
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        navPanel.setBackground(HEADER_COLOR);
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        btnManajemenProduk = createNavButton("Manajemen Produk");
        btnManajemenStok = createNavButton("Manajemen Stok");
        btnLaporan = createNavButton("Laporan");
        btnLogout = createNavButton("Logout");
        
        navPanel.add(btnManajemenProduk);
        navPanel.add(btnManajemenStok);
        navPanel.add(btnLaporan);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(HEADER_COLOR);
        JLabel lblUser = new JLabel("User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 12));
        userPanel.add(lblUser);
        userPanel.add(btnLogout);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.add(navPanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        // Content panel - menggunakan CardLayout untuk menukar panel konten
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Inisialisasi panel konten
        barangPanel = new BarangForm();
        barangPanel.setEmbeddedMode(true); // Mode embedded agar tidak memunculkan frame baru
        
        stokPanel = new BarangMasukForm();
        stokPanel.setEmbeddedMode(true);
        
        laporanPanel = new LaporanForm();
        laporanPanel.setEmbeddedMode(true);
        
        contentPanel.add(barangPanel, "PRODUK");
        contentPanel.add(stokPanel, "STOK");
        contentPanel.add(laporanPanel, "LAPORAN");
        
        // Tambahkan panels ke main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel ke frame
        setContentPane(mainPanel);
        
        // Tambahkan listeners
        btnManajemenProduk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("PRODUK");
                setActiveButton(btnManajemenProduk);
            }
        });
        
        btnManajemenStok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("STOK");
                setActiveButton(btnManajemenStok);
            }
        });
        
        btnLaporan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("LAPORAN");
                setActiveButton(btnLaporan);
            }
        });
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        // Tampilkan panel produk secara default
        showPanel("PRODUK");
        setActiveButton(btnManajemenProduk);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }
    
    private void setActiveButton(JButton activeButton) {
        // Reset semua button
        btnManajemenProduk.setBackground(Color.WHITE);
        btnManajemenStok.setBackground(Color.WHITE);
        btnLaporan.setBackground(Color.WHITE);
        
        // Set button aktif
        activeButton.setBackground(BACKGROUND_COLOR);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin keluar?", 
                "Konfirmasi Logout", 
                JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            this.dispose();
        }
    }
} 