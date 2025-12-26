package src.view;

import src.controller.ProdukController;
import src.model.Produk;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdukPanel extends JPanel {

    private ProdukController controller;
    private JTabbedPane categoryTabs;
    private JLabel lblTotalHarga;
    private BigDecimal totalHarga = BigDecimal.ZERO;
    private RiwayatPesananCRUDPanel riwayatPanel;

    private final Color BORDER_PAKET = new Color(0, 122, 255);
    private final Color BORDER_SEWA = new Color(142, 68, 173);
    private final Color BORDER_WEEDING = new Color(230, 126, 34);

    public ProdukPanel() {
        controller = new ProdukController();
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        categoryTabs = new JTabbedPane(JTabbedPane.TOP);
        categoryTabs.setFont(new Font("Arial", Font.BOLD, 14));

        categoryTabs.addTab("📦 PAKET FOTO", wrapScrollable(createCategoryPanel("PAKET")));
        categoryTabs.addTab("👗 SEWA PAKAIAN", wrapScrollable(createCategoryPanel("SEWA")));
        categoryTabs.addTab("💍 PAKET WEEDING", wrapScrollable(createCategoryPanel("WEEDING")));

        add(categoryTabs, BorderLayout.CENTER);

        // ===== BOTTOM TOTAL =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JLabel lblTotal = new JLabel("Total Belanja:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(Color.BLACK);

        lblTotalHarga = new JLabel("Rp 0");
        lblTotalHarga.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalHarga.setForeground(new Color(46, 204, 113));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(new Color(240, 240, 240));
        totalPanel.add(lblTotal);
        totalPanel.add(lblTotalHarga);

        bottomPanel.add(totalPanel, BorderLayout.WEST);

        JButton btnCheckout = new JButton("🚀 CHECKOUT");
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckout.setBackground(new Color(46, 204, 113));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setOpaque(true);
        btnCheckout.setContentAreaFilled(true);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setPreferredSize(new Dimension(160, 40));
        btnCheckout.addActionListener(e -> checkout());

        bottomPanel.add(btnCheckout, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        loadProducts();
    }

    public void setRiwayatPanel(RiwayatPesananCRUDPanel riwayatPanel) {
        this.riwayatPanel = riwayatPanel;
    }

    // ================= SCROLL HELPER =================
    private JScrollPane wrapScrollable(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.getHorizontalScrollBar().setUnitIncrement(25);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel createCategoryPanel(String category) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(category + " FOTO", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(getCategoryColor(category));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(Box.createVerticalStrut(15));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(20));

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(panel, BorderLayout.CENTER);

        return container;
    }

    private void loadProducts() {
        String[] categories = {"PAKET", "SEWA", "WEEDING"};

        for (String cat : categories) {
            List<Produk> list = controller.getProdukByJenis(cat);
            JPanel panel = getCategoryContentPanel(cat);

            for (Produk p : list) {
                panel.add(createProductCard(p));
            }
        }
    }

    private JPanel getCategoryContentPanel(String category) {
        JScrollPane scroll;
        switch (category) {
            case "PAKET":
                scroll = (JScrollPane) categoryTabs.getComponentAt(0);
                break;
            case "SEWA":
                scroll = (JScrollPane) categoryTabs.getComponentAt(1);
                break;
            case "WEEDING":
                scroll = (JScrollPane) categoryTabs.getComponentAt(2);
                break;
            default:
                scroll = (JScrollPane) categoryTabs.getComponentAt(0);
        }
        
        JPanel container = (JPanel) scroll.getViewport().getView();
        BorderLayout layout = (BorderLayout) container.getLayout();
        return (JPanel) layout.getLayoutComponent(BorderLayout.CENTER);
    }

    private JPanel createProductCard(Produk produk) {
        Color borderColor = getCategoryColor(produk.getJenis());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        
        // 🔥 PERBAIKAN: Tinggi kartu ditambah untuk menampung deskripsi
        card.setPreferredSize(new Dimension(320, 380));
        card.setMinimumSize(new Dimension(320, 380));
        card.setMaximumSize(new Dimension(320, 380));
        
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // 🔥 1. NAMA PRODUK
        JLabel lblNama = new JLabel(produk.getNama());
        lblNama.setFont(new Font("Arial", Font.BOLD, 17));
        lblNama.setForeground(borderColor.darker());
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 🔥 2. DESKRIPSI PRODUK (YANG DITAMBAHKAN)
        String deskripsi = produk.getDeskripsi();
        if (deskripsi == null || deskripsi.isEmpty()) {
            deskripsi = "Deskripsi tidak tersedia";
        }
        
        // 🔥 Potong deskripsi jika terlalu panjang
        String shortDesc = deskripsi;
        if (deskripsi.length() > 100) {
            shortDesc = deskripsi.substring(0, 97) + "...";
        }
        
        JTextArea txtDeskripsi = new JTextArea(shortDesc);
        txtDeskripsi.setEditable(false);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setFont(new Font("Arial", Font.PLAIN, 11));
        txtDeskripsi.setBackground(new Color(250, 250, 250));
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Deskripsi"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtDeskripsi.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 🔥 Tooltip dengan deskripsi lengkap
        txtDeskripsi.setToolTipText("<html><b>Deskripsi Lengkap:</b><br>" + 
                                  deskripsi.replace("\n", "<br>") + "</html>");
        card.setToolTipText("<html><b>" + produk.getNama() + "</b><br>" + 
                           "Harga: " + formatRupiah(produk.getHarga()) + "<br>" +
                           "Deskripsi: " + deskripsi + "</html>");

        // 🔥 3. HARGA PRODUK
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        JLabel lblHarga = new JLabel("💰 Price: " + rupiah.format(produk.getHarga()));
        lblHarga.setFont(new Font("Arial", Font.BOLD, 14));
        lblHarga.setForeground(new Color(231, 76, 60));
        lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 🔥 TAMBAHKAN KOMPONEN KE KARTU
        card.add(lblNama);
        card.add(Box.createVerticalStrut(8));
        card.add(txtDeskripsi);
        card.add(Box.createVerticalStrut(8));
        card.add(lblHarga);
        card.add(Box.createVerticalStrut(15));

        // 🔥 4. PANEL JUMLAH (QUANTITY)
        JPanel qtyPanel = new JPanel(new FlowLayout());
        JLabel lblJumlah = new JLabel("0");
        lblJumlah.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnPlus = new JButton("+");
        JButton btnMinus = new JButton("−");

        // 🔥 Styling tombol minus
        btnMinus.setFont(new Font("Arial", Font.BOLD, 16));
        btnMinus.setBackground(new Color(220, 50, 50));
        btnMinus.setForeground(Color.WHITE);
        btnMinus.setFocusPainted(false);
        btnMinus.setBorderPainted(false);
        btnMinus.setOpaque(true);
        btnMinus.setPreferredSize(new Dimension(35, 35));

        // 🔥 Styling tombol plus
        btnPlus.setFont(new Font("Arial", Font.BOLD, 16));
        btnPlus.setBackground(new Color(50, 180, 50));
        btnPlus.setForeground(Color.WHITE);
        btnPlus.setFocusPainted(false);
        btnPlus.setBorderPainted(false);
        btnPlus.setOpaque(true);
        btnPlus.setPreferredSize(new Dimension(35, 35));

        // 🔥 Event listener untuk tombol plus
        btnPlus.addActionListener(e -> {
            int q = Integer.parseInt(lblJumlah.getText()) + 1;
            lblJumlah.setText(String.valueOf(q));
            updateTotalHarga(produk.getHarga(), 1);
        });

        // 🔥 Event listener untuk tombol minus
        btnMinus.addActionListener(e -> {
            int q = Integer.parseInt(lblJumlah.getText());
            if (q > 0) {
                lblJumlah.setText(String.valueOf(q - 1));
                updateTotalHarga(produk.getHarga(), -1);
            }
        });

        qtyPanel.add(new JLabel("Jumlah:"));
        qtyPanel.add(btnMinus);
        
        JPanel quantityPanel = new JPanel(new BorderLayout());
        quantityPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        quantityPanel.setPreferredSize(new Dimension(50, 35));
        lblJumlah.setHorizontalAlignment(SwingConstants.CENTER);
        quantityPanel.add(lblJumlah, BorderLayout.CENTER);
        
        qtyPanel.add(quantityPanel);
        qtyPanel.add(btnPlus);

        card.add(qtyPanel);
        card.add(Box.createVerticalStrut(15));

        // 🔥 5. TOMBOL BUY NOW
        JButton btnBuy = new JButton("🛒 BUY NOW");
        btnBuy.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuy.setBackground(borderColor);
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setOpaque(true);
        btnBuy.setContentAreaFilled(true);
        btnBuy.setBorderPainted(false);
        btnBuy.setFocusPainted(false);
        btnBuy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBuy.setPreferredSize(new Dimension(180, 45));

        // 🔥 Event listener untuk BUY NOW
        btnBuy.addActionListener(e -> {
            int qty = Integer.parseInt(lblJumlah.getText());
            if (qty > 0) {
                controller.tambahKeKeranjang(produk, qty);
                JOptionPane.showMessageDialog(this,
                        "✅ " + qty + "x " + produk.getNama() + " ditambahkan ke keranjang!",
                        "Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
                lblJumlah.setText("0");
                updateCartDisplay();
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Pilih jumlah terlebih dahulu!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        card.add(btnBuy);

        return card;
    }

    // 🔥 HELPER METHOD untuk format rupiah
    private String formatRupiah(BigDecimal harga) {
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        return rupiah.format(harga);
    }

    private Color getCategoryColor(String kategori) {
        switch (kategori) {
            case "PAKET": return BORDER_PAKET;
            case "SEWA": return BORDER_SEWA;
            case "WEEDING": return BORDER_WEEDING;
        }
        return BORDER_PAKET;
    }

    private void updateTotalHarga(BigDecimal harga, int qty) {
        totalHarga = totalHarga.add(harga.multiply(BigDecimal.valueOf(qty)));
        lblTotalHarga.setText(formatRupiah(totalHarga));
    }

    private void updateCartDisplay() {
        totalHarga = controller.getTotalKeranjang();
        lblTotalHarga.setText(formatRupiah(totalHarga));
    }

    private void checkout() {
        if (totalHarga.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Keranjang masih kosong!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔥 Dialog checkout dengan input data pelanggan
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Nama Pemesan:"));
        JTextField txtNama = new JTextField();
        panel.add(txtNama);
        
        panel.add(new JLabel("Telepon:"));
        JTextField txtTelepon = new JTextField();
        panel.add(txtTelepon);
        
        panel.add(new JLabel("Email:"));
        JTextField txtEmail = new JTextField();
        panel.add(txtEmail);
        
        panel.add(new JLabel("Jadwal (dd-MM-yyyy):"));
        JTextField txtJadwal = new JTextField();
        panel.add(txtJadwal);
        
        panel.add(new JLabel("Total:"));
        panel.add(new JLabel(formatRupiah(totalHarga)));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Checkout", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nama = txtNama.getText().trim();
            String telepon = txtTelepon.getText().trim();
            String email = txtEmail.getText().trim();
            String jadwal = txtJadwal.getText().trim();
            
            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 🔥 Ambil produk dari keranjang untuk detail
            List<Produk> keranjang = controller.getKeranjang();
            StringBuilder produkDetail = new StringBuilder();
            
            for (Produk p : keranjang) {
                produkDetail.append(p.getNama())
                           .append(" (")
                           .append(p.getJenis())
                           .append("), ");
            }
            
            // 🔥 Hapus koma terakhir
            String produkString = produkDetail.toString();
            if (produkString.length() > 2) {
                produkString = produkString.substring(0, produkString.length() - 2);
            }
            
            // 🔥 Simpan ke riwayat melalui riwayatPanel
            if (riwayatPanel != null) {
                boolean success = riwayatPanel.tambahPesananDariCheckout(
                    nama, telepon, totalHarga, produkString, email, jadwal
                );
                
                if (success) {
                    // 🔥 Konfirmasi checkout berhasil
                    JOptionPane.showMessageDialog(this,
                        "✅ Checkout berhasil!\n" +
                        "Nama: " + nama + "\n" +
                        "Total: " + formatRupiah(totalHarga) + "\n" +
                        "Status: PROSES\n" +
                        "Lihat tab '📋 RIWAYAT' untuk melihat detail pesanan.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // 🔥 Reset keranjang
                    controller.clearCart();
                    totalHarga = BigDecimal.ZERO;
                    lblTotalHarga.setText("Rp 0");
                    
                    // 🔥 Otomatis pindah ke tab Riwayat
                    if (getParent() != null && getParent().getParent() != null) {
                        Container container = getParent().getParent();
                        if (container instanceof JTabbedPane) {
                            JTabbedPane tabPane = (JTabbedPane) container;
                            tabPane.setSelectedIndex(1); // Index 1 adalah tab Riwayat
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "❌ Gagal menyimpan pesanan ke database!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Riwayat panel tidak tersedia!", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}