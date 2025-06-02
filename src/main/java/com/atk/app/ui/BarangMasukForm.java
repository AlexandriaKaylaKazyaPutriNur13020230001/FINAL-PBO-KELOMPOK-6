package com.atk.app.ui;

import com.atk.app.dao.BarangDAO;
import com.atk.app.dao.BarangMasukDAO;
import com.atk.app.model.Barang;
import com.atk.app.model.BarangMasuk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BarangMasukForm extends JPanel {
    
    // Warna tema
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181);
    private final Color BUTTON_COLOR = new Color(156, 138, 112); 
    private final Color BUTTON_TEXT_COLOR = Color.BLACK;
    
    private JTextField txtIdStok;
    private JComboBox<Barang> cmbBarang;
    private JTextField txtJumlah;
    private JTextField txtLokasi;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JTable tblBarangMasuk;
    private DefaultTableModel tableModel;
    
    private BarangDAO barangDAO;
    private BarangMasukDAO barangMasukDAO;
    private boolean embeddedMode = false;
    
    public BarangMasukForm() {
        barangDAO = new BarangDAO();
        barangMasukDAO = new BarangMasukDAO();
        initComponents();
        loadBarang();
        loadBarangMasukData();
    }
    
    public void setEmbeddedMode(boolean embeddedMode) {
        this.embeddedMode = embeddedMode;
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form judul
        JLabel titleLabel = new JLabel("Form Data Stok");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        formPanel.add(new JLabel("ID Stok:"));
        txtIdStok = new JTextField(20);
        txtIdStok.setPreferredSize(new Dimension(300, 30));
        txtIdStok.setEditable(false);
        txtIdStok.setText("Auto-generated");
        formPanel.add(txtIdStok);
        
        formPanel.add(new JLabel("Produk:"));
        cmbBarang = new JComboBox<>();
        formPanel.add(cmbBarang);
        
        formPanel.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField(20);
        formPanel.add(txtJumlah);
        
        formPanel.add(new JLabel("Lokasi:"));
        txtLokasi = new JTextField(20);
        formPanel.add(txtLokasi);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        btnTambah = createButton("Tambah");
        btnUpdate = createButton("Update");
        btnHapus = createButton("Hapus");
        btnBersihkan = createButton("Bersihkan");
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnBersihkan);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        String[] columnNames = {"Title 1", "Title 2", "Title 3", "Lokasi", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblBarangMasuk = new JTable(tableModel);
        tblBarangMasuk.setRowHeight(25);
        tblBarangMasuk.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblBarangMasuk.getTableHeader().setBackground(BUTTON_COLOR);
        tblBarangMasuk.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(tblBarangMasuk);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Menggabungkan semua panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahBarangMasuk();
            }
        });
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update stok
                JOptionPane.showMessageDialog(BarangMasukForm.this, "Fitur update belum diimplementasikan");
            }
        });
        
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hapus stok
                JOptionPane.showMessageDialog(BarangMasukForm.this, "Fitur hapus belum diimplementasikan");
            }
        });
        
        btnBersihkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bersihkanForm();
            }
        });
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void loadBarang() {
        cmbBarang.removeAllItems();
        List<Barang> barangList = barangDAO.getAllBarang();
        for (Barang barang : barangList) {
            cmbBarang.addItem(barang);
        }
    }
    
    private void loadBarangMasukData() {
        tableModel.setRowCount(0);
        List<BarangMasuk> barangMasukList = barangMasukDAO.getAllBarangMasuk();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        for (BarangMasuk barangMasuk : barangMasukList) {
            Object[] row = {
                barangMasuk.getId(),
                barangMasuk.getBarangNama(),
                barangMasuk.getJumlah(),
                "Gudang", // Default lokasi
                "Tersedia" // Default status
            };
            tableModel.addRow(row);
        }
    }
    
    private void tambahBarangMasuk() {
        Barang barang = (Barang) cmbBarang.getSelectedItem();
        String jumlahStr = txtJumlah.getText();
        String lokasi = txtLokasi.getText();
        
        if (barang == null || jumlahStr.isEmpty() || lokasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int jumlah = Integer.parseInt(jumlahStr);
            
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            BarangMasuk barangMasuk = new BarangMasuk();
            barangMasuk.setBarangId(barang.getId());
            barangMasuk.setJumlah(jumlah);
            barangMasuk.setTanggal(new Date());
            
            boolean success = barangMasukDAO.addBarangMasuk(barangMasuk);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Data stok berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangMasukData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data stok", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void bersihkanForm() {
        txtIdStok.setText("Auto-generated");
        if (cmbBarang.getItemCount() > 0) {
            cmbBarang.setSelectedIndex(0);
        }
        txtJumlah.setText("");
        txtLokasi.setText("");
        txtJumlah.requestFocus();
    }
    
    // Method tambahan untuk standalone mode
    public static void main(String[] args) {
        JFrame frame = new JFrame("Form Data Stok");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        BarangMasukForm panel = new BarangMasukForm();
        frame.add(panel);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 