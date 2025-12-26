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

    // ================= SCROLL HELPER =================
    private JScrollPane wrapScrollable(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // ✅ Izinkan horizontal scroll
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.getHorizontalScrollBar().setUnitIncrement(25);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel createCategoryPanel(String category) {
        // ✅ PERUBAHAN: Gunakan FlowLayout dengan orientasi HORIZONTAL
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)); // ✅ Spasi horizontal 20px
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(category + " FOTO", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(getCategoryColor(category));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ Buat panel khusus untuk title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(Box.createVerticalStrut(15));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(20));

        // ✅ Bungkus dalam container dengan BorderLayout
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
            JPanel panel = getCategoryContentPanel(cat); // ✅ Ambil panel FlowLayout

            for (Produk p : list) {
                panel.add(createProductCard(p));
            }
        }
    }

    // ✅ Method baru untuk mendapatkan panel FlowLayout (bukan container)
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
        
        // ✅ UKURAN KARTU YANG LEBIH SESUAI UNTUK LAYOUT HORIZONTAL
        card.setPreferredSize(new Dimension(320, 340)); // ✅ Lebar tetap, tinggi fleksibel
        card.setMinimumSize(new Dimension(320, 340));
        card.setMaximumSize(new Dimension(320, 340));
        
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblNama = new JLabel(produk.getNama());
        lblNama.setFont(new Font("Arial", Font.BOLD, 17));
        lblNama.setForeground(borderColor.darker());
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

        NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        JLabel lblHarga = new JLabel("💰 Price: " + rupiah.format(produk.getHarga()));
        lblHarga.setFont(new Font("Arial", Font.BOLD, 14));
        lblHarga.setForeground(new Color(231, 76, 60));
        lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblNama);
        card.add(Box.createVerticalStrut(5));
        card.add(lblHarga);
        card.add(Box.createVerticalStrut(15));

        JPanel qtyPanel = new JPanel(new FlowLayout());
        JLabel lblJumlah = new JLabel("0");
        lblJumlah.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnPlus = new JButton("+");
        JButton btnMinus = new JButton("−");

        // ✅ STYLING TOMBOL +/- YANG LEBIH BAIK
        btnMinus.setFont(new Font("Arial", Font.BOLD, 16));
        btnMinus.setBackground(new Color(220, 50, 50));
        btnMinus.setForeground(Color.WHITE);
        btnMinus.setFocusPainted(false);
        btnMinus.setBorderPainted(false);
        btnMinus.setOpaque(true);
        btnMinus.setPreferredSize(new Dimension(35, 35));

        btnPlus.setFont(new Font("Arial", Font.BOLD, 16));
        btnPlus.setBackground(new Color(50, 180, 50));
        btnPlus.setForeground(Color.WHITE);
        btnPlus.setFocusPainted(false);
        btnPlus.setBorderPainted(false);
        btnPlus.setOpaque(true);
        btnPlus.setPreferredSize(new Dimension(35, 35));

        btnPlus.addActionListener(e -> {
            int q = Integer.parseInt(lblJumlah.getText()) + 1;
            lblJumlah.setText(String.valueOf(q));
            updateTotalHarga(produk.getHarga(), 1);
        });

        btnMinus.addActionListener(e -> {
            int q = Integer.parseInt(lblJumlah.getText());
            if (q > 0) {
                lblJumlah.setText(String.valueOf(q - 1));
                updateTotalHarga(produk.getHarga(), -1);
            }
        });

        qtyPanel.add(new JLabel("Jumlah:"));
        qtyPanel.add(btnMinus);
        
        // ✅ PANEL UNTUK QUANTITY DENGAN BORDER
        JPanel quantityPanel = new JPanel(new BorderLayout());
        quantityPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        quantityPanel.setPreferredSize(new Dimension(50, 35));
        lblJumlah.setHorizontalAlignment(SwingConstants.CENTER);
        quantityPanel.add(lblJumlah, BorderLayout.CENTER);
        
        qtyPanel.add(quantityPanel);
        qtyPanel.add(btnPlus);

        card.add(qtyPanel);
        card.add(Box.createVerticalStrut(15));

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

        btnBuy.addActionListener(e -> {
            int qty = Integer.parseInt(lblJumlah.getText());
            if (qty > 0) {
                controller.tambahKeKeranjang(produk, qty);
                JOptionPane.showMessageDialog(this,
                        "✅ " + qty + "x " + produk.getNama() + " ditambahkan!",
                        "Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
                lblJumlah.setText("0");
                updateCartDisplay();
            }
        });

        card.add(btnBuy);

        return card;
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
        lblTotalHarga.setText(
                NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
                        .format(totalHarga)
        );
    }

    private void updateCartDisplay() {
        totalHarga = controller.getTotalKeranjang();
        lblTotalHarga.setText(
                NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
                        .format(totalHarga)
        );
    }

    private void checkout() {
        if (totalHarga.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Keranjang masih kosong!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Checkout dialog
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
        
        panel.add(new JLabel("Jadwal (yy-xx-zz):"));
        JTextField txtJadwal = new JTextField("yy-xx-zz");
        panel.add(txtJadwal);
        
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        panel.add(new JLabel("Total:"));
        panel.add(new JLabel(rupiahFormat.format(totalHarga)));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Checkout", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            if (txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this,
                "✅ Checkout berhasil!\nTotal: " + rupiahFormat.format(totalHarga),
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            controller.clearCart();
            totalHarga = BigDecimal.ZERO;
            lblTotalHarga.setText("Rp 0");
        }
    }

    public void setRiwayatPanel(RiwayatPesananCRUDPanel riwayatPanel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRiwayatPanel'");
    }
}