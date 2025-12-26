package src.controller;

import src.model.Produk;
import src.dao.ProdukDao; // 🔥 TAMBAHKAN
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdukController {
    
    private List<Produk> produkList;
    private List<CartItem> cartItems;
    private ProdukDao produkDao; // 🔥 TAMBAHKAN DAO
    
    // 🔥 BUAT CLASS INNER PUBLIK untuk akses dari luar
    public class CartItem {
        private Produk produk;
        private int quantity;
        
        public CartItem(Produk produk, int quantity) {
            this.produk = produk;
            this.quantity = quantity;
        }
        
        public Produk getProduk() {
            return produk;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        public BigDecimal getSubtotal() {
            return produk.getHarga().multiply(BigDecimal.valueOf(quantity));
        }
        
        @Override
        public String toString() {
            return quantity + "x " + produk.getNama() + " (" + produk.getJenis() + ")";
        }
    }
    
    public ProdukController() {
        this.produkDao = new ProdukDao(); // 🔥 INISIALISASI DAO
        this.cartItems = new ArrayList<>();
        initProdukListFromDatabase(); // 🔥 UBAH: Load dari database
        System.out.println("🎮 Controller Produk siap!");
    }
    
    private void initProdukListFromDatabase() {
        try {
            produkList = produkDao.getAllProduk();
            System.out.println("📦 Loaded " + produkList.size() + " produk dari database");
            
            // 🔥 JIKA DATABASE KOSONG, BUAT DATA DEFAULT
            if (produkList.isEmpty()) {
                System.out.println("⚠️ Database produk kosong, membuat data default...");
                createDefaultProduk();
                produkList = produkDao.getAllProduk(); // Load ulang
            }
            
            // Tampilkan produk yang di-load
            for (Produk p : produkList) {
                System.out.println("   - " + p.getId() + ": " + p.getNama() + 
                                 " (" + p.getJenis() + ") Rp" + p.getHarga());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Gagal load produk dari database: " + e.getMessage());
            e.printStackTrace();
            createDefaultProduk(); // Fallback ke data default
        }
    }
    
    private void createDefaultProduk() {
        produkList = new ArrayList<>();
        
        // Data produk default sesuai desain Figma
        produkList.add(new Produk(1, "Paket Hemat", 
            "• Makeup biasa + pakaian + foto (HANYA FILE MENTAHAN SAJA)\n" +
            "• Foto Keluarga Perorang (+Rp50.000.00)",
            new BigDecimal("160000.00"),
            "PAKET"));
        
        produkList.add(new Produk(2, "Paket Sedang",
            "• Makeup biasa + pakaian + foto (SUDAH DI CETAK)\n" +
            "• Foto Keluarga Perorang (+Rp50.000.00)",
            new BigDecimal("270000.00"),
            "PAKET"));
        
        produkList.add(new Produk(3, "Paket Combo",
            "• Makeup biasa + pakaian + foto + bingkai (SUDAH DI CETAK)\n" +
            "• Foto Keluarga Perorang (+Rp50.000.00)",
            new BigDecimal("470000.00"),
            "PAKET"));
        
        produkList.add(new Produk(4, "Sewa Pakaian",
            "• Penyewaan pakaian perkostum Rp 35.000.00\n" +
            "• Pakaian bayi sampai dewasa",
            new BigDecimal("35000.00"),
            "SEWA"));
        
        produkList.add(new Produk(5, "WeedingGo",
            "• 1 Roll (40 foto) + Cetak album ukuran 4R\n" +
            "• Model Book (+Rp1.000.000.00)",
            new BigDecimal("400000.00"),
            "WEEDING"));
        
        // 🔥 SIMPAN KE DATABASE
        for (Produk p : produkList) {
            try {
                produkDao.tambahProduk(p);
            } catch (Exception e) {
                System.err.println("❌ Gagal menyimpan produk " + p.getNama() + ": " + e.getMessage());
            }
        }
        
        System.out.println("📦 Created " + produkList.size() + " default produk");
    }
    
    // 🔥 TAMBAHKAN METHOD CRUD UNTUK DATABASE
    public boolean tambahProduk(Produk produk) {
        try {
            boolean success = produkDao.tambahProduk(produk);
            if (success) {
                refreshProdukList();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Gagal tambah produk: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateProduk(Produk produk) {
        try {
            boolean success = produkDao.updateProduk(produk);
            if (success) {
                refreshProdukList();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Gagal update produk: " + e.getMessage());
            return false;
        }
    }
    
    public boolean hapusProduk(int id) {
        try {
            boolean success = produkDao.hapusProduk(id);
            if (success) {
                refreshProdukList();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Gagal hapus produk: " + e.getMessage());
            return false;
        }
    }
    
    private void refreshProdukList() {
        produkList = produkDao.getAllProduk();
        System.out.println("🔄 Produk list di-refresh, total: " + produkList.size());
    }
    
    public List<Produk> getAllProduk() {
        return new ArrayList<>(produkList);
    }
    
    public List<Produk> getProdukByJenis(String jenis) {
        List<Produk> result = new ArrayList<>();
        for (Produk p : produkList) {
            if (jenis.equals(p.getJenis())) {
                result.add(p);
            }
        }
        System.out.println("🔍 Found " + result.size() + " produk dengan jenis: " + jenis);
        return result;
    }
    
    public Produk getProdukById(int id) {
        for (Produk p : produkList) {
            if (p.getId() == id) {
                return p;
            }
        }
        System.err.println("⚠️ Produk ID " + id + " tidak ditemukan");
        return null;
    }
    
    // 🔥 TAMBAHKAN METHOD UNTUK PENCARIAN
    public List<Produk> searchProduk(String keyword) {
        List<Produk> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Produk p : produkList) {
            if (p.getNama().toLowerCase().contains(lowerKeyword) ||
                p.getJenis().toLowerCase().contains(lowerKeyword) ||
                (p.getDeskripsi() != null && p.getDeskripsi().toLowerCase().contains(lowerKeyword))) {
                result.add(p);
            }
        }
        
        System.out.println("🔍 Search '" + keyword + "' found " + result.size() + " produk");
        return result;
    }
    
    public void tambahKeKeranjang(Produk produk, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity harus lebih dari 0");
        }
        
        // Cek apakah produk sudah ada di keranjang
        for (CartItem item : cartItems) {
            if (item.getProduk().getId() == produk.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("🛒 Update keranjang: " + produk.getNama() + " (+" + quantity + ")");
                return;
            }
        }
        
        // Jika belum ada, tambah baru
        cartItems.add(new CartItem(produk, quantity));
        System.out.println("🛒 Tambah ke keranjang: " + produk.getNama() + " x" + quantity);
    }
    
    public BigDecimal getTotalKeranjang() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }
        System.out.println("💰 Total keranjang: Rp" + total);
        return total;
    }
    
    public List<String> getCartSummary() {
        List<String> summary = new ArrayList<>();
        for (CartItem item : cartItems) {
            summary.add(item.getQuantity() + "x " + item.getProduk().getNama());
        }
        return summary;
    }
    
    public void clearCart() {
        System.out.println("🗑️ Keranjang dikosongkan, items: " + cartItems.size());
        cartItems.clear();
    }
    
    public int getCartItemCount() {
        int count = cartItems.size();
        System.out.println("🛒 Jumlah item di keranjang: " + count);
        return count;
    }
    
    // 🔥 Method untuk mendapatkan daftar CartItem
    public List<CartItem> getCartItems() {
        System.out.println("🛒 Mengambil " + cartItems.size() + " cart items");
        return new ArrayList<>(cartItems);
    }
    
    // 🔥 Method untuk mendapatkan daftar Produk di keranjang (COMPATIBILITY)
    public List<Produk> getKeranjang() {
        List<Produk> produkList = new ArrayList<>();
        for (CartItem item : cartItems) {
            produkList.add(item.getProduk());
        }
        return produkList;
    }
    
    // 🔥 Method untuk mendapatkan detail keranjang lengkap
    public List<String> getCartDetails() {
        List<String> details = new ArrayList<>();
        for (CartItem item : cartItems) {
            details.add(item.getQuantity() + "x " + 
                       item.getProduk().getNama() + " (" + 
                       item.getProduk().getJenis() + ") - " + 
                       "Rp" + item.getSubtotal());
        }
        return details;
    }
    
    // 🔥 Method untuk menghapus item dari keranjang
    public boolean removeFromCart(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProduk().getId() == productId) {
                cartItems.remove(item);
                System.out.println("🗑️ Hapus dari keranjang: Product ID " + productId);
                return true;
            }
        }
        return false;
    }
    
    // 🔥 Method untuk mendapatkan jumlah item tertentu di keranjang
    public int getQuantityInCart(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProduk().getId() == productId) {
                return item.getQuantity();
            }
        }
        return 0;
    }
    
    // 🔥 Method untuk cek apakah produk ada di keranjang
    public boolean isProdukInCart(int productId) {
        return getQuantityInCart(productId) > 0;
    }
    
    // 🔥 Method untuk mendapatkan statistik
    public void printStatistics() {
        System.out.println("📊 ===== STATISTIK PRODUK =====");
        System.out.println("Total Produk: " + produkList.size());
        
        int paketCount = getProdukByJenis("PAKET").size();
        int sewaCount = getProdukByJenis("SEWA").size();
        int weedingCount = getProdukByJenis("WEEDING").size();
        
        System.out.println("PAKET: " + paketCount + " produk");
        System.out.println("SEWA: " + sewaCount + " produk");
        System.out.println("WEEDING: " + weedingCount + " produk");
        System.out.println("Item di Keranjang: " + getCartItemCount());
        System.out.println("Total Keranjang: Rp" + getTotalKeranjang());
        System.out.println("=================================");
    }

	public void hapusSemuaProduk() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'hapusSemuaProduk'");
	}
}