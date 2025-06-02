package com.atk.app.ui;

import com.atk.app.dao.KategoriDAO;
import com.atk.app.model.Kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class KategoriForm extends JFrame {
    
    private JTextField txtId;
    private JTextField txtNama;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JTable tblKategori;
    private DefaultTableModel tableModel;
    
    private KategoriDAO kategoriDAO;
    
    public KategoriForm() {
        kategoriDAO = new KategoriDAO();
        initComponents();
        loadKategoriData();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Form Data Kategori");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        menuPanel.setBackground(new Color(150, 120, 100));
        
        JButton btnManajemenProduk = new JButton("Manajemen Produk");
        JButton btnManajemenStok = new JButton("Manajemen Stok");
        JButton btnTransaksi = new JButton("Transaksi");
        
        menuPanel.add(btnManajemenProduk);
        menuPanel.add(btnManajemenStok);
        menuPanel.add(btnTransaksi);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Data Kategori"));
        
        formPanel.add(new JLabel("ID Kategori:"));
        txtId = new JTextField(20);
        txtId.setEditable(false); // ID is auto-generated
        formPanel.add(txtId);
        
        formPanel.add(new JLabel("Nama Kategori:"));
        txtNama = new JTextField(20);
        formPanel.add(txtNama);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnTambah = new JButton("Tambah");
        btnTambah.setForeground(Color.BLACK);
        btnUpdate = new JButton("Update");
        btnUpdate.setForeground(Color.BLACK);
        btnHapus = new JButton("Hapus");
        btnHapus.setForeground(Color.BLACK);
        btnBersihkan = new JButton("Bersihkan");
        btnBersihkan.setForeground(Color.BLACK);
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnBersihkan);
        
        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Kategori"));
        
        String[] columnNames = {"ID", "Nama Kategori"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblKategori = new JTable(tableModel);
        tblKategori.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblKategori.getTableHeader().setBackground(new Color(156, 138, 112));
        tblKategori.getTableHeader().setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(tblKategori);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to main panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(menuPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahKategori();
            }
        });
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateKategori();
            }
        });
        
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusKategori();
            }
        });
        
        btnBersihkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bersihkanForm();
            }
        });
        
        tblKategori.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKategori.getSelectedRow();
                if (row != -1) {
                    pilihKategori(row);
                }
            }
        });
        
        btnManajemenProduk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarangForm form = new BarangForm();
                form.setVisible(true);
                dispose();
            }
        });
        
        btnManajemenStok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarangMasukForm form = new BarangMasukForm();
                form.setVisible(true);
                dispose();
            }
        });
        
        btnTransaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenjualanForm form = new PenjualanForm();
                form.setVisible(true);
                dispose();
            }
        });
    }
    
    private void loadKategoriData() {
        tableModel.setRowCount(0);
        List<Kategori> kategoriList = kategoriDAO.getAllKategori();
        
        for (Kategori kategori : kategoriList) {
            Object[] row = {
                kategori.getId(),
                kategori.getNamaKategori()
            };
            tableModel.addRow(row);
        }
    }
    
    private void tambahKategori() {
        String nama = txtNama.getText();
        
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kategori harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Kategori kategori = new Kategori();
        kategori.setNamaKategori(nama);
        
        boolean success = kategoriDAO.addKategori(kategori);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadKategoriData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan kategori!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateKategori() {
        String idStr = txtId.getText();
        String nama = txtNama.getText();
        
        if (idStr.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih kategori yang akan diupdate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            Kategori kategori = new Kategori();
            kategori.setId(id);
            kategori.setNamaKategori(nama);
            
            boolean success = kategoriDAO.updateKategori(kategori);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Kategori berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadKategoriData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate kategori!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID kategori tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusKategori() {
        String idStr = txtId.getText();
        
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih kategori yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus kategori ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = kategoriDAO.deleteKategori(id);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadKategoriData();
                    bersihkanForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus kategori! Pastikan tidak ada barang yang menggunakan kategori ini.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID kategori tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void bersihkanForm() {
        txtId.setText("");
        txtNama.setText("");
    }
    
    private void pilihKategori(int row) {
        String id = tblKategori.getValueAt(row, 0).toString();
        String nama = tblKategori.getValueAt(row, 1).toString();
        
        txtId.setText(id);
        txtNama.setText(nama);
    }
} 