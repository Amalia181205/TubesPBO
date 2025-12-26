package src.view;

import src.controller.PesananController;
import src.model.Pesanan;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RiwayatPesananCRUDPanel extends JPanel {
    private PesananController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh, btnDetail;
    private JComboBox<String> comboStatus;
    
    public RiwayatPesananCRUDPanel() {
        controller = new PesananController();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        
        JLabel lblTitle = new JLabel("📋 RIWAYAT PESANAN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.setBackground(new Color(41, 128, 185));
        
        btnTambah = createButton("➕ Tambah", new Color(46, 204, 113));
        btnEdit = createButton("✏️ Edit", new Color(52, 152, 219));
        btnHapus = createButton("🗑️ Hapus", new Color(231, 76, 60));
        btnDetail = createButton("👁️ Detail", new Color(155, 89, 182));
        btnRefresh = createButton("🔄 Refresh", new Color(149, 165, 166));
        
        toolbar.add(btnTambah);
        toolbar.add(btnEdit);
        toolbar.add(btnHapus);
        toolbar.add(btnDetail);
        toolbar.add(btnRefresh);
        headerPanel.add(toolbar, BorderLayout.EAST);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        filterPanel.add(new JLabel("Filter Status:"));
        comboStatus = new JComboBox<>(new String[]{"SEMUA", "PROSES", "SUKSES", "BATAL"});
        filterPanel.add(comboStatus);
        
        // Table
        String[] columns = {"ID", "Pemesan", "Produk", "Tanggal", "Total", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add panels
        add(headerPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);
        
        // Event Listeners
        setupListeners();
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }
    
    private void setupListeners() {
        btnTambah.addActionListener(e -> tambahPesanan());
        btnEdit.addActionListener(e -> editPesanan());
        btnHapus.addActionListener(e -> hapusPesanan());
        btnDetail.addActionListener(e -> lihatDetail());
        btnRefresh.addActionListener(e -> loadData());
        comboStatus.addActionListener(e -> filterByStatus());
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Pesanan> pesananList = controller.getAllPesanan();
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        for (Pesanan p : pesananList) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getPemesan(),
                p.getProduk(),
                p.getTanggalPesan() != null ? p.getTanggalPesan().format(fmt) : "-",
                p.getTotal() != null ? rupiah.format(p.getTotal()) : "Rp 0",
                p.getStatus()
            });
        }
    }
    
    private void tambahPesanan() {
        JTextField txtNama = new JTextField();
        JTextField txtProduk = new JTextField();
        JTextField txtTotal = new JTextField();
        JComboBox<String> comboStatus = new JComboBox<>(new String[]{"PROSES", "SUKSES", "BATAL"});
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Nama Pemesan:"));
        panel.add(txtNama);
        panel.add(new JLabel("Produk:"));
        panel.add(txtProduk);
        panel.add(new JLabel("Total:"));
        panel.add(txtTotal);
        panel.add(new JLabel("Status:"));
        panel.add(comboStatus);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Pesanan", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            Pesanan pesanan = new Pesanan();
            pesanan.setPemesan(txtNama.getText());
            pesanan.setProduk(txtProduk.getText());
            pesanan.setTotal(new BigDecimal(txtTotal.getText()));
            pesanan.setStatus((String) comboStatus.getSelectedItem());
            pesanan.setTanggalPesan(java.time.LocalDateTime.now());
            
            if (controller.tambahPesanan(pesanan)) {
                JOptionPane.showMessageDialog(this, "Pesanan berhasil ditambahkan!");
                loadData();
            }
        }
    }
    
    private void editPesanan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pesanan yang akan diedit!");
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Pesanan pesanan = controller.getPesananById(id);
        
        if (pesanan != null) {
            // Dialog edit (sama seperti tambah, tapi dengan data yang sudah ada)
            JOptionPane.showMessageDialog(this, "Edit pesanan #" + id);
            loadData();
        }
    }
    
    private void hapusPesanan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pesanan yang akan dihapus!");
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Hapus pesanan #" + id + "?", "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.hapusPesanan(id)) {
                JOptionPane.showMessageDialog(this, "Pesanan berhasil dihapus!");
                loadData();
            }
        }
    }
    
    private void lihatDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pesanan untuk melihat detail!");
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Pesanan pesanan = controller.getPesananById(id);
        
        if (pesanan != null) {
            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
            panel.add(new JLabel("ID:"));
            panel.add(new JLabel(String.valueOf(pesanan.getId())));
            panel.add(new JLabel("Pemesan:"));
            panel.add(new JLabel(pesanan.getPemesan()));
            panel.add(new JLabel("Produk:"));
            panel.add(new JLabel(pesanan.getProduk()));
            panel.add(new JLabel("Total:"));
            NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            panel.add(new JLabel(pesanan.getTotal() != null ? rupiah.format(pesanan.getTotal()) : "Rp 0"));
            panel.add(new JLabel("Status:"));
            panel.add(new JLabel(pesanan.getStatus()));
            panel.add(new JLabel("Tanggal:"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            panel.add(new JLabel(pesanan.getTanggalPesan() != null ? pesanan.getTanggalPesan().format(fmt) : "-"));
            
            JOptionPane.showMessageDialog(this, panel, "Detail Pesanan", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void filterByStatus() {
        String status = (String) comboStatus.getSelectedItem();
        if (status.equals("SEMUA")) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Pesanan> filteredList = controller.getPesananByStatus(status);
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        for (Pesanan p : filteredList) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getPemesan(),
                p.getProduk(),
                p.getTanggalPesan() != null ? p.getTanggalPesan().format(fmt) : "-",
                p.getTotal() != null ? rupiah.format(p.getTotal()) : "Rp 0",
                p.getStatus()
            });
        }
    }
    
    // Method untuk ditambah dari ProdukPanel setelah checkout
    public void tambahPesananDariCheckout(String nama, String telepon, BigDecimal total, String produk) {
        Pesanan pesanan = new Pesanan();
        pesanan.setPemesan(nama);
        pesanan.setTelepon(telepon);
        pesanan.setTotal(total);
        pesanan.setProduk(produk);
        pesanan.setStatus("PROSES");
        pesanan.setTanggalPesan(java.time.LocalDateTime.now());
        
        if (controller.tambahPesanan(pesanan)) {
            System.out.println("✅ Pesanan dari checkout berhasil disimpan!");
            loadData();
        }
    }
    
    // 🔥 Method untuk integrasi
    public void refreshTable() {
        loadData();
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}