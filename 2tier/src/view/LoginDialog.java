package src.view;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean loginSuccess = false;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    public LoginDialog(Frame parent) {
        super(parent, "Login - Panesya Studio", true);
        initComponents();
        setupDialog();
    }
    
    private void initComponents() {
        // Set background warna studio foto (gradient biru muda)
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(41, 128, 185); // Biru tua
                Color color2 = new Color(52, 152, 219); // Biru muda
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Panel untuk form login (background putih transparan)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255, 230)); // Putih transparan
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Title dengan icon kamera
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel(" PANESYA STUDIO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(41, 128, 185));
        formPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Sistem Pemesanan Foto", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(subtitleLabel, gbc);
        
        // Separator
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        formPanel.add(separator, gbc);
        
        // Username dengan icon
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblUser = new JLabel(" Username:");
        lblUser.setFont(new Font("Arial", Font.BOLD, 13));
        lblUser.setForeground(new Color(60, 60, 60));
        formPanel.add(lblUser, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtUsername, gbc);
        
        // Password dengan icon
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lblPass = new JLabel(" Password:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 13));
        lblPass.setForeground(new Color(60, 60, 60));
        formPanel.add(lblPass, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(txtPassword, gbc);
        
        // Remember me checkbox (opsional)
        // gbc.gridy = 5;
        // gbc.gridx = 1;
        // gbc.anchor = GridBagConstraints.LINE_START;
        // JCheckBox chkRemember = new JCheckBox("Remember me");
        // chkRemember.setFont(new Font("Arial", Font.PLAIN, 12));
        // chkRemember.setForeground(new Color(100, 100, 100));
        // chkRemember.setBackground(new Color(255, 255, 255, 0));
        // formPanel.add(chkRemember, gbc);
        
        // Buttons
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(255, 255, 255, 0));
        
        JButton btnLogin = createButton(" Login", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnKeluar = createButton("🚪 Keluar", new Color(231, 76, 60), new Color(192, 57, 43));
        
        btnLogin.addActionListener(e -> prosesLogin());
        btnKeluar.addActionListener(e -> dispose());
        
        // Enter key untuk login
        getRootPane().setDefaultButton(btnLogin);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnKeluar);
        formPanel.add(buttonPanel, gbc);
        
        // Info
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 0, 0, 0);
        JLabel lblInfo = new JLabel(" Gunakan username/password apapun untuk demo", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(150, 150, 150));
        formPanel.add(lblInfo, gbc);
        
        // Tambahkan form panel ke main panel
        mainPanel.add(formPanel, new GridBagConstraints());
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    private void setupDialog() {
        pack();
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Set minimum size
        setMinimumSize(new Dimension(500, 500));
        
        // Set icon (jika ada icon aplikasi)
        try {
            // Uncomment jika ada icon
            // ImageIcon icon = new ImageIcon(getClass().getResource("/images/camera-icon.png"));
            // setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Icon not loaded: " + e.getMessage());
        }
    }
    
    private JButton createButton(String text, Color color, Color hoverColor) {
        JButton button = new JButton(text) {
            private Color originalColor = color;
            private Color hover = hoverColor;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(hover.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hover);
                } else {
                    g2.setColor(originalColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
            
            @Override
            public void updateUI() {
                super.updateUI();
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        return button;
    }
    
    private void prosesLogin() {
    String username = txtUsername.getText().trim();
    String password = new String(txtPassword.getPassword());
    
    // Validasi input
    if (username.isEmpty() || password.isEmpty()) {
        showErrorDialog("Username dan password tidak boleh kosong!");
        txtUsername.requestFocus();
        return;
    }
    
    // Validasi panjang password
    if (password.length() < 3) {
        showErrorDialog("Password harus minimal 3 karakter!");
        txtPassword.requestFocus();
        txtPassword.selectAll();
        return;
    }
    
    // Tampilkan loading sederhana
    JOptionPane.showMessageDialog(this, 
        "Sedang memproses login...", 
        "Loading", 
        JOptionPane.INFORMATION_MESSAGE);
    
    // Untuk demo, semua login diterima
    loginSuccess = true;
    
    // Tampilkan pesan sukses
    JOptionPane.showMessageDialog(this, 
        "<html><div style='text-align: center;'>"
        + "<h3 style='color: #2ecc71;'>🎉 Login Berhasil!</h3>"
        + "<p>Selamat datang, <b>" + username + "</b></p>"
        + "<p>Sistem Panesya Studio siap digunakan.</p>"
        + "</div></html>", 
        "Success", 
        JOptionPane.INFORMATION_MESSAGE);
    
    dispose();
}
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, 
            "<html><div style='text-align: center;'>"
            + "<h3 style='color: #e74c3c;'>⚠️ Error</h3>"
            + "<p>" + message + "</p>"
            + "</div></html>", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private JDialog createLoadingDialog() {
        JDialog loadingDialog = new JDialog(this, "Memproses...", true);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.setSize(300, 150);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setUndecorated(true);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Spinner
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(false);
        
        // Label
        JLabel label = new JLabel("Sedang memproses login...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        
        loadingDialog.add(panel);
        loadingDialog.pack();
        
        return loadingDialog;
    }
    
    // Method untuk mendapatkan status login
    public boolean isLoginSuccess() {
        return loginSuccess;
    }
    
    // Method untuk mendapatkan username (jika diperlukan)
    public String getUsername() {
        return txtUsername.getText().trim();
    }
    
    // Main method untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog dialog = new LoginDialog(null);
            dialog.setVisible(true);
            
            if (dialog.isLoginSuccess()) {
                System.out.println("Login berhasil dengan username: " + dialog.getUsername());
            }
        });
    }
}