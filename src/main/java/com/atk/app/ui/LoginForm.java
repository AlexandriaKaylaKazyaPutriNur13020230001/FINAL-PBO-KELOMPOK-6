package com.atk.app.ui;

import com.atk.app.dao.UserDAO;
import com.atk.app.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblTitle;
    
    // Warna tema
    private final Color BACKGROUND_COLOR = new Color(240, 217, 181); // Beige
    private final Color BUTTON_COLOR = new Color(230, 230, 230);
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    
    public LoginForm() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Inventory System - Login");
        setSize(400, 350);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        lblTitle = new JLabel("Sign In");
        lblTitle.setFont(TITLE_FONT);
        titlePanel.add(lblTitle);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 10, 15));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel lblUsername = new JLabel("Nama");
        lblUsername.setFont(LABEL_FONT);
        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(200, 30));
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(LABEL_FONT);
        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(200, 30));
        
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        btnLogin = new JButton("Sign In");
        btnLogin.setPreferredSize(new Dimension(100, 30));
        btnLogin.setBackground(BUTTON_COLOR);
        btnLogin.setForeground(Color.BLACK);
        
        btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(100, 30));
        btnRegister.setBackground(BUTTON_COLOR);
        btnRegister.setForeground(Color.BLACK);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listener to login button
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Add action listener to register button
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterForm();
            }
        });
    }
    
    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        boolean authenticated = userDAO.authenticate(username, password);
        
        if (authenticated) {
            User user = userDAO.getUser(username);
            
            if (user != null) {
                // Open appropriate form based on user role
                if ("admin".equals(user.getRole())) {
                    // Open main form for admin
                    MainForm mainForm = new MainForm(user);
                    mainForm.setVisible(true);
                    System.out.println("Admin login: " + user.getUsername());
                } else {
                    // Open karyawan form for employee
                    KaryawanForm karyawanForm = new KaryawanForm(user);
                    karyawanForm.setVisible(true);
                    System.out.println("Karyawan login: " + user.getUsername());
                }
                this.dispose();
            }
        } else {
            // Custom error dialog styling
            JDialog dialog = new JDialog(this, "Login Gagal", true);
            dialog.setLayout(new BorderLayout());
            
            JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
            messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
            JLabel messageLabel = new JLabel("Username atau password salah!");
            
            JButton okButton = new JButton("OK");
            okButton.setPreferredSize(new Dimension(80, 30));
            okButton.addActionListener(e -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(okButton);
            
            messagePanel.add(iconLabel, BorderLayout.WEST);
            messagePanel.add(messageLabel, BorderLayout.CENTER);
            messagePanel.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.add(messagePanel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }
    
    private void openRegisterForm() {
        RegisterForm registerForm = new RegisterForm();
        registerForm.setVisible(true);
    }
} 