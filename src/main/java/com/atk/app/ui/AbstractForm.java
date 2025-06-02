package com.atk.app.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Kelas abstrak yang menyediakan fungsionalitas dasar untuk semua form UI
 */
public abstract class AbstractForm extends JPanel {
    
    // Warna tema standar
    protected final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    protected final Color BUTTON_COLOR = new Color(156, 138, 112); // Darker beige/brown
    protected final Color HEADER_COLOR = new Color(156, 138, 112);
    protected final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    protected final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    // Flag untuk menentukan mode embedded (dalam panel) atau standalone (dalam frame)
    protected boolean embeddedMode = false;
    
    /**
     * Konstruktor default
     */
    public AbstractForm() {
        setBackground(BACKGROUND_COLOR);
        initComponents();
    }
    
    /**
     * Set mode embedded (true jika form digunakan sebagai panel dalam form lain)
     * @param embedded status mode embedded
     */
    public void setEmbeddedMode(boolean embedded) {
        this.embeddedMode = embedded;
    }
    
    /**
     * Mendapatkan status mode embedded
     * @return true jika form dalam mode embedded
     */
    public boolean isEmbeddedMode() {
        return embeddedMode;
    }
    
    /**
     * Membuat tombol dengan styling standar
     * @param text teks tombol
     * @return tombol yang sudah distyling
     */
    protected JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(LABEL_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Membuat label dengan styling standar
     * @param text teks label
     * @return label yang sudah distyling
     */
    protected JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }
    
    /**
     * Membuat panel header dengan styling standar
     * @param title judul panel
     * @return panel header yang sudah distyling
     */
    protected JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        return headerPanel;
    }
    
    /**
     * Metode abstrak untuk inisialisasi komponen UI
     * Harus diimplementasikan oleh kelas turunan
     */
    protected abstract void initComponents();
    
    /**
     * Metode abstrak untuk membersihkan form
     * Harus diimplementasikan oleh kelas turunan
     */
    protected abstract void clearForm();
    
    /**
     * Metode abstrak untuk memuat data
     * Harus diimplementasikan oleh kelas turunan
     */
    protected abstract void loadData();
} 