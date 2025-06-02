package com.atk.app.ui;

import com.atk.app.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KaryawanForm extends JFrame {
    
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    private final Color HEADER_COLOR = new Color(156, 138, 112); // Darker beige/brown
    private User currentUser;
    private PenjualanForm penjualanPanel;
    
    public KaryawanForm(User user) {
        this.currentUser = user;
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistem Transaksi - Karyawan");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        
        JLabel titleLabel = new JLabel("Sistem Transaksi Penjualan", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(HEADER_COLOR);
        
        JLabel welcomeLabel = new JLabel("Selamat datang, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        userPanel.add(welcomeLabel);
        userPanel.add(btnLogout);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Create PenjualanForm
        penjualanPanel = new PenjualanForm();
        
        // Add panels to frame
        contentPanel.add(penjualanPanel, BorderLayout.CENTER);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
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
    
    public static void main(String[] args) {
        // For testing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                User testUser = new User();
                testUser.setUsername("karyawan");
                testUser.setRole("employee");
                
                KaryawanForm form = new KaryawanForm(testUser);
                form.setVisible(true);
            }
        });
    }
} 