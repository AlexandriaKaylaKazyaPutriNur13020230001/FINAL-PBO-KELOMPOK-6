package com.atk.app.ui;

import com.atk.app.dao.BarangDAO;
import com.atk.app.dao.BarangMasukDAO;
import com.atk.app.dao.PenjualanDAO;
import com.atk.app.model.Barang;
import com.atk.app.model.BarangMasuk;
import com.atk.app.model.DetailPenjualan;
import com.atk.app.model.Penjualan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LaporanForm extends JPanel {
    
    private JTabbedPane tabbedPane;
    private JTable tblPenjualan;
    private JTable tblDetailPenjualan;
    private JTable tblBarangMasuk;
    private JTable tblMutasiBarang;
    private DefaultTableModel penjualanTableModel;
    private DefaultTableModel detailPenjualanTableModel;
    private DefaultTableModel barangMasukTableModel;
    private DefaultTableModel mutasiBarangTableModel;
    private JTextField txtTanggalAwal;
    private JTextField txtTanggalAkhir;
    private JButton btnFilter;
    private JComboBox<Barang> cmbBarang;
    private JPanel menuPanel;
    
    private BarangDAO barangDAO;
    private BarangMasukDAO barangMasukDAO;
    private PenjualanDAO penjualanDAO;
    private boolean embeddedMode = false;
    
    public LaporanForm() {
        barangDAO = new BarangDAO();
        barangMasukDAO = new BarangMasukDAO();
        penjualanDAO = new PenjualanDAO();
        
        initComponents();
        loadBarang();
        setDefaultDates();
        loadLaporanPenjualan();
    }
    
    public void setEmbeddedMode(boolean embeddedMode) {
        this.embeddedMode = embeddedMode;
        if (embeddedMode) {
            menuPanel.setVisible(false);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Menu Panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        menuPanel.setBackground(new Color(150, 120, 100));
        
        JButton btnManajemenProduk = new JButton("Manajemen Produk");
        JButton btnManajemenStok = new JButton("Manajemen Stok");
        JButton btnTransaksi = new JButton("Transaksi");
        
        menuPanel.add(btnManajemenProduk);
        menuPanel.add(btnManajemenStok);
        menuPanel.add(btnTransaksi);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        filterPanel.add(new JLabel("Tanggal Awal:"));
        txtTanggalAwal = new JTextField(10);
        filterPanel.add(txtTanggalAwal);
        
        filterPanel.add(new JLabel("Tanggal Akhir:"));
        txtTanggalAkhir = new JTextField(10);
        filterPanel.add(txtTanggalAkhir);
        
        filterPanel.add(new JLabel("Barang:"));
        cmbBarang = new JComboBox<>();
        cmbBarang.addItem(null); // Untuk filter semua barang
        filterPanel.add(cmbBarang);
        
        btnFilter = new JButton("Filter");
        filterPanel.add(btnFilter);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        
        // Penjualan Panel
        JPanel penjualanPanel = new JPanel(new BorderLayout(10, 10));
        String[] penjualanColumnNames = {"ID", "Tanggal", "Total"};
        penjualanTableModel = new DefaultTableModel(penjualanColumnNames, 0);
        tblPenjualan = new JTable(penjualanTableModel);
        JScrollPane penjualanScrollPane = new JScrollPane(tblPenjualan);
        penjualanPanel.add(penjualanScrollPane, BorderLayout.CENTER);
        
        // Detail Penjualan Panel
        JPanel detailPenjualanPanel = new JPanel(new BorderLayout(10, 10));
        String[] detailPenjualanColumnNames = {"ID Transaksi", "Barang", "Jumlah", "Subtotal"};
        detailPenjualanTableModel = new DefaultTableModel(detailPenjualanColumnNames, 0);
        tblDetailPenjualan = new JTable(detailPenjualanTableModel);
        JScrollPane detailPenjualanScrollPane = new JScrollPane(tblDetailPenjualan);
        detailPenjualanPanel.add(detailPenjualanScrollPane, BorderLayout.CENTER);
        
        // Barang Masuk Panel
        JPanel barangMasukPanel = new JPanel(new BorderLayout(10, 10));
        String[] barangMasukColumnNames = {"ID", "Barang", "Jumlah", "Tanggal"};
        barangMasukTableModel = new DefaultTableModel(barangMasukColumnNames, 0);
        tblBarangMasuk = new JTable(barangMasukTableModel);
        JScrollPane barangMasukScrollPane = new JScrollPane(tblBarangMasuk);
        barangMasukPanel.add(barangMasukScrollPane, BorderLayout.CENTER);
        
        // Mutasi Barang Panel
        JPanel mutasiBarangPanel = new JPanel(new BorderLayout(10, 10));
        String[] mutasiBarangColumnNames = {"Barang", "Stok Awal", "Masuk", "Keluar", "Stok Akhir"};
        mutasiBarangTableModel = new DefaultTableModel(mutasiBarangColumnNames, 0);
        tblMutasiBarang = new JTable(mutasiBarangTableModel);
        JScrollPane mutasiBarangScrollPane = new JScrollPane(tblMutasiBarang);
        mutasiBarangPanel.add(mutasiBarangScrollPane, BorderLayout.CENTER);
        
        // Add tabs
        tabbedPane.addTab("Penjualan", penjualanPanel);
        tabbedPane.addTab("Detail Penjualan", detailPenjualanPanel);
        tabbedPane.addTab("Barang Masuk", barangMasukPanel);
        tabbedPane.addTab("Mutasi Barang", mutasiBarangPanel);
        
        // Add panels to main panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(menuPanel, BorderLayout.NORTH);
        northPanel.add(filterPanel, BorderLayout.SOUTH);
        
        add(northPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add action listeners
        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterLaporan();
            }
        });
        
        tblPenjualan.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblPenjualan.getSelectedRow();
                if (row != -1) {
                    String penjualanId = tblPenjualan.getValueAt(row, 0).toString();
                    loadDetailPenjualan(penjualanId);
                    tabbedPane.setSelectedIndex(1); // Switch to detail tab
                }
            }
        });
        
        btnManajemenProduk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!embeddedMode) {
                    BarangForm form = new BarangForm();
                    form.setVisible(true);
                    Window window = SwingUtilities.getWindowAncestor(LaporanForm.this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispose();
                    }
                }
            }
        });
        
        btnManajemenStok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!embeddedMode) {
                    BarangMasukForm form = new BarangMasukForm();
                    form.setVisible(true);
                    Window window = SwingUtilities.getWindowAncestor(LaporanForm.this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispose();
                    }
                }
            }
        });
        
        btnTransaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!embeddedMode) {
                    PenjualanForm form = new PenjualanForm();
                    form.setVisible(true);
                    Window window = SwingUtilities.getWindowAncestor(LaporanForm.this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispose();
                    }
                }
            }
        });
    }
    
    private void loadBarang() {
        cmbBarang.removeAllItems();
        cmbBarang.addItem(null); // Untuk filter semua barang
        List<Barang> barangList = barangDAO.getAllBarang();
        for (Barang barang : barangList) {
            cmbBarang.addItem(barang);
        }
    }
    
    private void setDefaultDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        // Set tanggal awal ke awal bulan ini
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        txtTanggalAwal.setText(dateFormat.format(cal.getTime()));
        
        // Set tanggal akhir ke hari ini
        txtTanggalAkhir.setText(dateFormat.format(new Date()));
    }
    
    private void loadLaporanPenjualan() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date tanggalAwal = dateFormat.parse(txtTanggalAwal.getText());
            Date tanggalAkhir = dateFormat.parse(txtTanggalAkhir.getText());
            
            // Set waktu tanggal akhir ke 23:59:59
            Calendar cal = Calendar.getInstance();
            cal.setTime(tanggalAkhir);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            tanggalAkhir = cal.getTime();
            
            loadPenjualan(tanggalAwal, tanggalAkhir);
            loadBarangMasuk(tanggalAwal, tanggalAkhir);
            loadMutasiBarang(tanggalAwal, tanggalAkhir);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal tidak valid! Gunakan format dd-MM-yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPenjualan(Date tanggalAwal, Date tanggalAkhir) {
        penjualanTableModel.setRowCount(0);
        List<Penjualan> penjualanList = penjualanDAO.getPenjualanByDateRange(tanggalAwal, tanggalAkhir);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        for (Penjualan penjualan : penjualanList) {
            Object[] row = {
                penjualan.getId(),
                dateFormat.format(penjualan.getTanggal()),
                penjualan.getTotal()
            };
            penjualanTableModel.addRow(row);
        }
    }
    
    private void loadDetailPenjualan(String penjualanId) {
        detailPenjualanTableModel.setRowCount(0);
        List<DetailPenjualan> detailList = penjualanDAO.getDetailPenjualanByPenjualanId(penjualanId);
        
        for (DetailPenjualan detail : detailList) {
            Object[] row = {
                detail.getPenjualanId(),
                detail.getBarangNama(),
                detail.getJumlah(),
                detail.getSubtotal()
            };
            detailPenjualanTableModel.addRow(row);
        }
    }
    
    private void loadBarangMasuk(Date tanggalAwal, Date tanggalAkhir) {
        barangMasukTableModel.setRowCount(0);
        List<BarangMasuk> barangMasukList = barangMasukDAO.getBarangMasukByDateRange(tanggalAwal, tanggalAkhir);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        Barang selectedBarang = (Barang) cmbBarang.getSelectedItem();
        
        for (BarangMasuk barangMasuk : barangMasukList) {
            if (selectedBarang == null || barangMasuk.getBarangId().equals(selectedBarang.getId())) {
                Object[] row = {
                    barangMasuk.getId(),
                    barangMasuk.getBarangNama(),
                    barangMasuk.getJumlah(),
                    dateFormat.format(barangMasuk.getTanggal())
                };
                barangMasukTableModel.addRow(row);
            }
        }
    }
    
    private void loadMutasiBarang(Date tanggalAwal, Date tanggalAkhir) {
        mutasiBarangTableModel.setRowCount(0);
        List<Barang> barangList;
        Barang selectedBarang = (Barang) cmbBarang.getSelectedItem();
        
        if (selectedBarang != null) {
            barangList = new ArrayList<>();
            barangList.add(selectedBarang);
        } else {
            barangList = barangDAO.getAllBarang();
        }
        
        for (Barang barang : barangList) {
            // Ini hanya contoh sederhana, dalam implementasi nyata perlu query khusus untuk mendapatkan stok awal
            int stokAwal = barang.getStok();
            int masuk = 0;
            int keluar = 0;
            
            // Hitung barang masuk
            List<BarangMasuk> barangMasukList = barangMasukDAO.getBarangMasukByBarangId(barang.getId());
            for (BarangMasuk bm : barangMasukList) {
                if (bm.getTanggal().compareTo(tanggalAwal) >= 0 && bm.getTanggal().compareTo(tanggalAkhir) <= 0) {
                    masuk += bm.getJumlah();
                }
            }
            
            // Hitung barang keluar dari penjualan
            List<Penjualan> penjualanList = penjualanDAO.getPenjualanByDateRange(tanggalAwal, tanggalAkhir);
            for (Penjualan p : penjualanList) {
                List<DetailPenjualan> detailList = penjualanDAO.getDetailPenjualanByPenjualanId(p.getId());
                for (DetailPenjualan dp : detailList) {
                    if (dp.getBarangId().equals(barang.getId())) {
                        keluar += dp.getJumlah();
                    }
                }
            }
            
            int stokAkhir = stokAwal + masuk - keluar;
            
            Object[] row = {
                barang.getNama(),
                stokAwal,
                masuk,
                keluar,
                stokAkhir
            };
            mutasiBarangTableModel.addRow(row);
        }
    }
    
    private void filterLaporan() {
        loadLaporanPenjualan();
    }
} 