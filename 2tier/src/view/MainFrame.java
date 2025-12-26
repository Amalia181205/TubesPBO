package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ProdukPanel produkPanel;
    private RiwayatPesananCRUDPanel riwayatPanel;
    private Timer statusTimer;
    private JScrollPane mainScrollPane;
    
    public MainFrame() {
        initComponents();
        setupFrame();
        startStatusAnimation();
        
        // 🔥 SETUP INTEGRASI - PENTING!
        setupIntegration();
    }
    
    private void initComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        setTitle("PANESYA STUDIO - Sistem Pemesanan Foto");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Main container panel
        JPanel mainContainer = new JPanel(new BorderLayout());
        setContentPane(mainContainer);
        
        // Header dengan gradient dan shadow
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Panel utama yang akan di-scroll
        JPanel mainContentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(245, 245, 245);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(250, 250, 250));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Create panels
        produkPanel = new ProdukPanel();
        riwayatPanel = new RiwayatPesananCRUDPanel();
        
        // Custom tab renderer
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, 
                int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    Color color1 = new Color(41, 128, 185);
                    Color color2 = new Color(52, 152, 219);
                    GradientPaint gradient = new GradientPaint(x, y, color1, x, y + h, color2);
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 15, 15);
                } else {
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 15, 15);
                }
            }
        });
        
        // Add tabs
        tabbedPane.addTab("🛍️ PRODUK", produkPanel);
        tabbedPane.addTab("📋 RIWAYAT", riwayatPanel);
        
        // Panel untuk tabbed pane
        JPanel tabContainer = new JPanel(new BorderLayout());
        tabContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tabContainer.setBackground(Color.WHITE);
        
        JScrollPane tabScrollPane = new JScrollPane(tabbedPane);
        tabScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tabContainer.add(tabScrollPane, BorderLayout.CENTER);
        mainContentPanel.add(tabContainer, BorderLayout.CENTER);
        
        mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        mainContainer.add(mainScrollPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainContainer.add(footerPanel, BorderLayout.SOUTH);
    }
    
    // 🔥 METHOD UNTUK INTEGRASI - PENTING!
    private void setupIntegration() {
        // Hubungkan ProdukPanel dengan RiwayatPesananCRUDPanel
        produkPanel.setRiwayatPanel(riwayatPanel);
        System.out.println("✅ Integrasi berhasil: ProdukPanel -> RiwayatPesananCRUDPanel");
        System.out.println("   - Setiap checkout akan otomatis masuk ke Riwayat Pesanan");
        System.out.println("   - Data akan tersimpan di database");
        System.out.println("   - Tab akan otomatis pindah ke Riwayat setelah checkout");
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Icon kamera
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel iconLabel = new JLabel("📸");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        titlePanel.add(iconLabel, gbc);
        
        // Title text
        gbc.gridx = 1; gbc.gridy = 0;
        JLabel titleLabel = new JLabel("PANESYA STUDIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, gbc);
        
        // Subtitle
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel subtitleLabel = new JLabel("Sistem Pemesanan Foto Profesional");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 255));
        titlePanel.add(subtitleLabel, gbc);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        footerPanel.setBackground(new Color(250, 250, 250));
        
        // Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusPanel.setBackground(new Color(250, 250, 250));
        
        JLabel statusIcon = new JLabel("🟢");
        statusIcon.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel statusLabel = new JLabel("Status: Online");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(new Color(46, 204, 113));
        
        statusPanel.add(statusIcon);
        statusPanel.add(statusLabel);
        
        // Copyright
        JLabel copyrightLabel = new JLabel("© 2025 Panesya Studio - All Rights Reserved", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(150, 150, 150));
        
        // Version
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Arial", Font.BOLD, 11));
        versionLabel.setForeground(new Color(100, 100, 100));
        
        footerPanel.add(statusPanel, BorderLayout.WEST);
        footerPanel.add(copyrightLabel, BorderLayout.CENTER);
        footerPanel.add(versionLabel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    private void setupFrame() {
        setSize(1300, 850);
        setLocationRelativeTo(null);
        
        try {
            setIconImage(createCameraIcon());
        } catch (Exception e) {
            System.err.println("Error creating icon: " + e.getMessage());
        }
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
            
            @Override
            public void windowOpened(WindowEvent e) {
                showWelcomeMessage();
            }
        });
        
        setVisible(true);
    }
    
    private Image createCameraIcon() {
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillRoundRect(4, 8, 24, 18, 5, 5);
        
        g2d.setColor(Color.WHITE);
        g2d.fillOval(12, 12, 8, 8);
        
        g2d.dispose();
        return image;
    }
    
    private void startStatusAnimation() {
        statusTimer = new Timer(1000, e -> {
            // Animasi sederhana
        });
        statusTimer.start();
    }
    
    private void showWelcomeMessage() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>"
                + "<h2 style='color: #2980b9;'>🌟 Selamat Datang di Panesya Studio! 🌟</h2>"
                + "<p>Sistem manajemen pemesanan studio foto siap digunakan.</p>"
                + "<p><b>Fitur Integrasi:</b></p>"
                + "<p>✅ Checkout dari Produk -> Otomatis masuk ke Riwayat</p>"
                + "<p>✅ Tab otomatis pindah ke Riwayat setelah checkout</p>"
                + "<p>✅ Data tersimpan permanen di database</p>"
                + "</div></html>",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar dari aplikasi?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (statusTimer != null) {
                statusTimer.stop();
            }
            System.exit(0);
        }
    }
    
    // Getter methods
    public ProdukPanel getProdukPanel() {
        return produkPanel;
    }
    
    public RiwayatPesananCRUDPanel getRiwayatPanel() {
        return riwayatPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
                System.out.println("✅ MainFrame berhasil dijalankan!");
                System.out.println("✅ Sistem integrasi Produk -> Riwayat siap digunakan!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error memulai aplikasi: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}