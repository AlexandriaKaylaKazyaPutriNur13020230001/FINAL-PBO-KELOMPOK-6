package com.atk.app.ui;

import com.atk.app.dao.BarangDAO;
import com.atk.app.dao.KategoriDAO;
import com.atk.app.model.Barang;
import com.atk.app.model.Kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BarangForm extends JPanel {
    
    // Warna tema
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    private final Color BUTTON_COLOR = new Color(156, 138, 112); // Darker beige/brown
    private final Color BUTTON_TEXT_COLOR = Color.BLACK;
    
    private JTextField txtId;
    private JTextField txtNama;
    private JTextField txtHarga;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JTable tblBarang;
    private DefaultTableModel tableModel;
    
    private BarangDAO barangDAO;
    private KategoriDAO kategoriDAO;
    private boolean embeddedMode = false;
    
    public BarangForm() {
        barangDAO = new BarangDAO();
        kategoriDAO = new KategoriDAO();
        initComponents();
        loadBarangData();
    }
    
    public void setEmbeddedMode(boolean embeddedMode) {
        this.embeddedMode = embeddedMode;
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form judul
        JLabel titleLabel = new JLabel("Form Data Produk");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        formPanel.add(new JLabel("ID Produk:"));
        txtId = new JTextField(20);
        txtId.setPreferredSize(new Dimension(300, 30));
        formPanel.add(txtId);
        
        formPanel.add(new JLabel("Nama Produk:"));
        txtNama = new JTextField(20);
        formPanel.add(txtNama);
        
        formPanel.add(new JLabel("Harga:"));
        txtHarga = new JTextField(20);
        formPanel.add(txtHarga);
        
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
        
        String[] columnNames = {"ID Produk", "Nama Produk", "Harga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblBarang = new JTable(tableModel);
        tblBarang.setRowHeight(25);
        tblBarang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblBarang.getTableHeader().setBackground(BUTTON_COLOR);
        tblBarang.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(tblBarang);
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
                tambahBarang();
            }
        });
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBarang();
            }
        });
        
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusBarang();
            }
        });
        
        btnBersihkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bersihkanForm();
            }
        });
        
        tblBarang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblBarang.getSelectedRow();
                if (row != -1) {
                    pilihBarang(row);
                }
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
    
    private void loadBarangData() {
        tableModel.setRowCount(0);
        
        List<Barang> barangList = barangDAO.getAllBarang();
        
        for (Barang barang : barangList) {
            Object[] row = {
                barang.getId(),
                barang.getNama(),
                barang.getHarga()
            };
            tableModel.addRow(row);
        }
    }
    
    private void tambahBarang() {
        try {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            double harga = Double.parseDouble(txtHarga.getText().trim());
            
            if (id.isEmpty() || nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Barang barang = new Barang();
            barang.setId(id);
            barang.setNama(nama);
            barang.setKategoriId(1); // Default kategori
            barang.setHarga(harga);
            barang.setStok(0);
            
            boolean success = barangDAO.addBarang(barang);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Data barang berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data barang", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBarang() {
        try {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            double harga = Double.parseDouble(txtHarga.getText().trim());
            
            if (id.isEmpty() || nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Barang barang = new Barang();
            barang.setId(id);
            barang.setNama(nama);
            barang.setKategoriId(1); // Default kategori
            barang.setHarga(harga);
            barang.setStok(0); // Akan diupdate dari database
            
            boolean success = barangDAO.updateBarang(barang);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Data barang berhasil diupdate", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate data barang", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusBarang() {
        String id = txtId.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih barang yang akan dihapus terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus barang ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = barangDAO.deleteBarang(id);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Data barang berhasil dihapus", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data barang", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void bersihkanForm() {
        txtId.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtId.requestFocus();
    }
    
    private void pilihBarang(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String nama = tableModel.getValueAt(row, 1).toString();
        String harga = tableModel.getValueAt(row, 2).toString();
        
        txtId.setText(id);
        txtNama.setText(nama);
        txtHarga.setText(harga);
    }
    
    // Method tambahan untuk standalone mode
    public static void main(String[] args) {
        JFrame frame = new JFrame("Form Data Barang");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        BarangForm panel = new BarangForm();
        frame.add(panel);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 