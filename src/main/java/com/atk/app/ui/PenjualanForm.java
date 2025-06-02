package com.atk.app.ui;

import com.atk.app.dao.BarangDAO;
import com.atk.app.dao.PenjualanDAO;
import com.atk.app.model.Barang;
import com.atk.app.model.DetailPenjualan;
import com.atk.app.model.Penjualan;
import com.atk.app.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PenjualanForm extends JPanel {
    
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    private final Color BUTTON_COLOR = new Color(156, 138, 112); // Darker beige/brown
    private final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private final Color HEADER_COLOR = BUTTON_COLOR;
    
    private JTextField txtIdTransaksi;
    private JTextField txtTanggal;
    private JComboBox<String> cmbJenisTransaksi;
    private JComboBox<Barang> cmbBarang;
    private JTextField txtJumlah;
    private JTextField txtTotal;
    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnBersihkan;
    private JButton btnLogout;
    private JTable tblDetailPenjualan;
    private DefaultTableModel tableModel;
    
    private BarangDAO barangDAO;
    private PenjualanDAO penjualanDAO;
    private List<DetailPenjualan> detailPenjualanList;
    private double totalPenjualan;
    private boolean embeddedMode = false;
    private User currentUser = null;
    private JPanel headerPanel;
    
    // Array metode pembayaran
    private final String[] METODE_PEMBAYARAN = {"Cash", "QRIS", "Debit"};
    
    public PenjualanForm() {
        barangDAO = new BarangDAO();
        penjualanDAO = new PenjualanDAO();
        detailPenjualanList = new ArrayList<>();
        totalPenjualan = 0.0;
        
        initComponents();
        loadBarang();
    }
    
    public PenjualanForm(User user) {
        this();
        this.currentUser = user;
        
        // Add user info regardless of embedded mode for employee view
        addUserInfo();
    }
    
    public void setEmbeddedMode(boolean embeddedMode) {
        this.embeddedMode = embeddedMode;
        
        // If in embedded mode and header panel exists, hide it
        if (embeddedMode && headerPanel != null) {
            headerPanel.setVisible(false);
        }
    }
    
    private void addUserInfo() {
        if (currentUser != null) {
            headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(HEADER_COLOR);
            
            JLabel titleLabel = new JLabel("Sistem Transaksi Penjualan");
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
            
            JLabel welcomeLabel = new JLabel("Selamat datang, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            welcomeLabel.setForeground(Color.WHITE);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
            
            btnLogout = new JButton("Logout");
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
            
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.setBackground(HEADER_COLOR);
            titlePanel.add(titleLabel);
            
            JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            userPanel.setBackground(HEADER_COLOR);
            userPanel.add(welcomeLabel);
            userPanel.add(btnLogout);
            
            headerPanel.add(titlePanel, BorderLayout.CENTER);
            headerPanel.add(userPanel, BorderLayout.EAST);
            
            // Add to the top of the panel
            add(headerPanel, BorderLayout.NORTH);
        }
    }
    
    private void logout() {
        // Check if we're in a JFrame
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            int confirm = JOptionPane.showConfirmDialog(window, 
                    "Apakah Anda yakin ingin keluar?", 
                    "Konfirmasi Logout", 
                    JOptionPane.YES_NO_OPTION);
                    
            if (confirm == JOptionPane.YES_OPTION) {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
                ((JFrame) window).dispose();
            }
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Form Transaksi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        formPanel.add(new JLabel("ID Transaksi:"));
        txtIdTransaksi = new JTextField(20);
        txtIdTransaksi.setPreferredSize(new Dimension(300, 30));
        txtIdTransaksi.setEditable(false);
        txtIdTransaksi.setText("Auto-generated");
        formPanel.add(txtIdTransaksi);
        
        formPanel.add(new JLabel("Tanggal:"));
        txtTanggal = new JTextField(20);
        txtTanggal.setEditable(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        txtTanggal.setText(dateFormat.format(new Date()));
        formPanel.add(txtTanggal);
        
        formPanel.add(new JLabel("Jenis Transaksi:"));
        cmbJenisTransaksi = new JComboBox<>(METODE_PEMBAYARAN);
        formPanel.add(cmbJenisTransaksi);
        
        formPanel.add(new JLabel("Produk:"));
        cmbBarang = new JComboBox<>();
        formPanel.add(cmbBarang);
        
        formPanel.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField(20);
        formPanel.add(txtJumlah);
        
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
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        totalPanel.setBackground(BACKGROUND_COLOR);
        totalPanel.add(new JLabel("Total:"));
        txtTotal = new JTextField(12);
        txtTotal.setEditable(false);
        txtTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtTotal.setText("0");
        totalPanel.add(txtTotal);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        String[] columnNames = {"ID Transaksi", "Tanggal", "Jenis Transaksi", "Produk", "Jumlah"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblDetailPenjualan = new JTable(tableModel);
        tblDetailPenjualan.setRowHeight(25);
        tblDetailPenjualan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblDetailPenjualan.getTableHeader().setBackground(BUTTON_COLOR);
        tblDetailPenjualan.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(tblDetailPenjualan);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(totalPanel, BorderLayout.SOUTH);
        
        // Add mouse listener to table for row selection
        tblDetailPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = tblDetailPenjualan.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        // Get the selected item data
                        String produkNama = tableModel.getValueAt(selectedRow, 3).toString();
                        int jumlah = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
                        
                        // Get the detail item to ensure we have the correct product
                        DetailPenjualan detail = detailPenjualanList.get(selectedRow);
                        
                        // Set the form fields
                        boolean found = false;
                        for (int i = 0; i < cmbBarang.getItemCount(); i++) {
                            Barang barang = cmbBarang.getItemAt(i);
                            if (barang.getId().equals(detail.getBarangId())) {
                                cmbBarang.setSelectedIndex(i);
                                found = true;
                                break;
                            }
                        }
                        
                        if (!found && cmbBarang.getItemCount() > 0) {
                            // Fallback to name matching if ID matching fails
                            for (int i = 0; i < cmbBarang.getItemCount(); i++) {
                                Barang barang = cmbBarang.getItemAt(i);
                                if (barang.getNama().equals(produkNama)) {
                                    cmbBarang.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                        
                        txtJumlah.setText(String.valueOf(jumlah));
                    } catch (Exception e) {
                        // Handle any exceptions that might occur
                        System.err.println("Error loading item data: " + e.getMessage());
                    }
                }
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        JButton btnProses = createButton("Proses Transaksi");
        bottomPanel.add(btnProses);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahItem();
            }
        });
        
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update transaksi
                updateItem();
            }
        });
        
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusItem();
            }
        });
        
        btnBersihkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                batalPenjualan();
            }
        });
        
        btnProses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesPenjualan();
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
    
    private void updateSubtotal() {
        totalPenjualan = 0.0;
        for (DetailPenjualan detail : detailPenjualanList) {
            totalPenjualan += detail.getSubtotal();
        }
        txtTotal.setText(String.format("%.2f", totalPenjualan));
    }
    
    private void tambahItem() {
        Barang barang = (Barang) cmbBarang.getSelectedItem();
        String jumlahStr = txtJumlah.getText();
        
        if (barang == null || jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk dan masukkan jumlah!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int jumlah = Integer.parseInt(jumlahStr);
            
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (jumlah > barang.getStok()) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + barang.getStok(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simpan ke model
            double subtotal = barang.getHarga() * jumlah;
            DetailPenjualan detail = new DetailPenjualan();
            detail.setBarangId(barang.getId());
            detail.setJumlah(jumlah);
            detail.setSubtotal(subtotal);
            
            // Add to table
            Object[] row = {
                txtIdTransaksi.getText(),
                txtTanggal.getText(),
                cmbJenisTransaksi.getSelectedItem(),
                barang.getNama(),
                jumlah
            };
            tableModel.addRow(row);
            
            // Add to list
            detailPenjualanList.add(detail);
            
            // Update total
            updateSubtotal();
            
            // Clear fields
            if (cmbBarang.getItemCount() > 0) {
                cmbBarang.setSelectedIndex(0);
            }
            txtJumlah.setText("");
            txtJumlah.requestFocus();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusItem() {
        int selectedRow = tblDetailPenjualan.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        detailPenjualanList.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        
        // Update total
        updateSubtotal();
    }
    
    private void updateItem() {
        int selectedRow = tblDetailPenjualan.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang akan diupdate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Barang barang = (Barang) cmbBarang.getSelectedItem();
        String jumlahStr = txtJumlah.getText();
        
        if (barang == null || jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih produk dan masukkan jumlah!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int jumlah = Integer.parseInt(jumlahStr);
            
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get the old item to calculate stock adjustment
            DetailPenjualan oldDetail = detailPenjualanList.get(selectedRow);
            Barang oldBarang = null;
            for (int i = 0; i < cmbBarang.getItemCount(); i++) {
                Barang b = cmbBarang.getItemAt(i);
                if (b.getId().equals(oldDetail.getBarangId())) {
                    oldBarang = b;
                    break;
                }
            }
            
            // Check if product is changed
            if (!barang.getId().equals(oldDetail.getBarangId())) {
                // This is a different product, check its stock
                if (jumlah > barang.getStok()) {
                    JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + barang.getStok(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Same product, check if we have enough stock for the additional amount
                int additionalAmount = jumlah - oldDetail.getJumlah();
                if (additionalAmount > 0 && additionalAmount > barang.getStok()) {
                    JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Stok tersedia: " + barang.getStok(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Update model
            double subtotal = barang.getHarga() * jumlah;
            DetailPenjualan detail = detailPenjualanList.get(selectedRow);
            detail.setBarangId(barang.getId());
            detail.setJumlah(jumlah);
            detail.setSubtotal(subtotal);
            
            // Update table
            tableModel.setValueAt(txtIdTransaksi.getText(), selectedRow, 0);
            tableModel.setValueAt(txtTanggal.getText(), selectedRow, 1);
            tableModel.setValueAt(cmbJenisTransaksi.getSelectedItem(), selectedRow, 2);
            tableModel.setValueAt(barang.getNama(), selectedRow, 3);
            tableModel.setValueAt(jumlah, selectedRow, 4);
            
            // Update total
            updateSubtotal();
            
            // Clear fields
            if (cmbBarang.getItemCount() > 0) {
                cmbBarang.setSelectedIndex(0);
            }
            txtJumlah.setText("");
            txtJumlah.requestFocus();
            
            JOptionPane.showMessageDialog(this, "Item berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void prosesPenjualan() {
        if (detailPenjualanList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada item yang ditambahkan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generate ID transaksi
        String id = "TRX" + System.currentTimeMillis();
        
        // Create penjualan object
        Penjualan penjualan = new Penjualan();
        penjualan.setId(id);
        penjualan.setTanggal(new Date());
        penjualan.setTotal(totalPenjualan);
        
        // Save to database
        boolean success = penjualanDAO.addPenjualan(penjualan, detailPenjualanList);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Transaksi berhasil diproses!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            cetakStruk(penjualan);
            batalPenjualan();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memproses transaksi!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void batalPenjualan() {
        detailPenjualanList.clear();
        tableModel.setRowCount(0);
        totalPenjualan = 0.0;
        if (cmbBarang.getItemCount() > 0) {
            cmbBarang.setSelectedIndex(0);
        }
        if (cmbJenisTransaksi.getItemCount() > 0) {
            cmbJenisTransaksi.setSelectedIndex(0);
        }
        txtJumlah.setText("");
        txtTotal.setText("0");
        
        // Generate new transaction ID
        txtIdTransaksi.setText("Auto-generated");
        
        // Update tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        txtTanggal.setText(dateFormat.format(new Date()));
    }
    
    private void cetakStruk(Penjualan penjualan) {
        // Sederhana: Tampilkan struk dalam dialog
        StringBuilder struk = new StringBuilder();
        struk.append("==============================\n");
        struk.append("      STRUK PENJUALAN ATK     \n");
        struk.append("==============================\n");
        struk.append("No: ").append(penjualan.getId()).append("\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        struk.append("Tanggal: ").append(dateFormat.format(penjualan.getTanggal())).append("\n");
        struk.append("Metode: ").append(cmbJenisTransaksi.getSelectedItem()).append("\n");
        struk.append("------------------------------\n");
        
        for (int i = 0; i < detailPenjualanList.size(); i++) {
            DetailPenjualan detail = detailPenjualanList.get(i);
            Barang barang = barangDAO.getBarangById(detail.getBarangId());
            
            struk.append(barang.getNama()).append("\n");
            struk.append(detail.getJumlah()).append(" x ").append(barang.getHarga());
            struk.append(" = ").append(detail.getSubtotal()).append("\n");
        }
        
        struk.append("------------------------------\n");
        struk.append("Total: ").append(penjualan.getTotal()).append("\n");
        struk.append("==============================\n");
        struk.append("Terima kasih atas pembelian Anda!");
        
        JOptionPane.showMessageDialog(this, struk.toString(), "Struk Penjualan", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Method tambahan untuk standalone mode
    public static void main(String[] args) {
        JFrame frame = new JFrame("Form Transaksi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        PenjualanForm panel = new PenjualanForm();
        frame.add(panel);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
} 