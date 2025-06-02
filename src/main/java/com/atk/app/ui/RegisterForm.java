package com.atk.app.ui;

import com.atk.app.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterForm extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> cmbRole;
    private JButton btnRegister;
    private JButton btnCancel;
    private JLabel lblTitle;
    
    // Warna tema
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    private final Color BUTTON_COLOR = new Color(230, 230, 230);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    public RegisterForm() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Inventory System - Register");
        setSize(400, 450);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        lblTitle = new JLabel("Register");
        lblTitle.setFont(TITLE_FONT);
        titlePanel.add(lblTitle);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 1, 10, 15));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(LABEL_FONT);
        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(200, 30));
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(LABEL_FONT);
        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(200, 30));
        
        JLabel lblConfirmPassword = new JLabel("Konfirmasi Password");
        lblConfirmPassword.setFont(LABEL_FONT);
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setPreferredSize(new Dimension(200, 30));
        
        JLabel lblRole = new JLabel("Role");
        lblRole.setFont(LABEL_FONT);
        cmbRole = new JComboBox<>(new String[]{"employee", "admin"});
        cmbRole.setPreferredSize(new Dimension(200, 30));
        
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword);
        formPanel.add(txtConfirmPassword);
        formPanel.add(lblRole);
        formPanel.add(cmbRole);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(100, 30));
        btnRegister.setBackground(BUTTON_COLOR);
        btnRegister.setForeground(Color.BLACK);
        
        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(100, 30));
        btnCancel.setBackground(BUTTON_COLOR);
        btnCancel.setForeground(Color.BLACK);
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void register() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();
        
        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password dan konfirmasi password tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if username already exists
        UserDAO userDAO = new UserDAO();
        if (userDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Register user
        boolean success = userDAO.registerUser(username, password, role);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registrasi gagal! Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterForm().setVisible(true);
            }
        });
    }
} 