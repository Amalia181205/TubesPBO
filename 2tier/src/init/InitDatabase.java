package src.init;

import src.controller.ProdukController;
import src.model.Produk;
import java.math.BigDecimal;

public class InitDatabase {
    public static void main(String[] args) {
        System.out.println("=== INITIALIZING DATABASE ===");
        
        ProdukController controller = new ProdukController();
        
        // Hapus data lama jika ada
        controller.hapusSemuaProduk();
        System.out.println("🗑️  Data lama dihapus");
        
        // Tambah data produk
        tambahProduk(controller);
        
        System.out.println("✅ Database initialized successfully!");
        System.out.println("📊 Total produk: " + controller.getAllProduk().size());
    }
    
    private static void tambahProduk(ProdukController controller) {
        // PAKET FOTO
        controller.tambahProduk(new Produk(
            "Paket Basic", 
            "PAKET", 
            new BigDecimal("500000"), 
            "2 Jam pemotretan, 50 foto edited, 1 lokasi"
        ));
        
        controller.tambahProduk(new Produk(
            "Paket Premium", 
            "PAKET", 
            new BigDecimal("1000000"), 
            "4 Jam pemotretan, 100 foto edited, 2 lokasi, Cetak foto 10R"
        ));
        
        controller.tambahProduk(new Produk(
            "Paket Luxury", 
            "PAKET", 
            new BigDecimal("2500000"), 
            "Full day pemotretan, 250 foto edited, Unlimited lokasi, Album foto premium"
        ));
        
        // SEWA PAKAIAN
        controller.tambahProduk(new Produk(
            "Gaun Pengantin", 
            "SEWA", 
            new BigDecimal("1500000"), 
            "Gaun pengantin premium, Free alterations, Dry cleaning"
        ));
        
        controller.tambahProduk(new Produk(
            "Jas Pria", 
            "SEWA", 
            new BigDecimal("300000"), 
            "Jas formal pria, berbagai ukuran"
        ));
        
        controller.tambahProduk(new Produk(
            "Kebaya Modern", 
            "SEWA", 
            new BigDecimal("400000"), 
            "Kebaya modern dengan payet, berbagai warna"
        ));
        
        // PAKET WEDDING
        controller.tambahProduk(new Produk(
            "Paket Silver Wedding", 
            "WEEDING", 
            new BigDecimal("15000000"), 
            "Pre-wedding, 8 jam photography, 300 foto, Album 20x30"
        ));
        
        controller.tambahProduk(new Produk(
            "Paket Gold Wedding", 
            "WEEDING", 
            new BigDecimal("25000000"), 
            "Silver + 2 photographer, Videography, 500 foto, Cinematic video"
        ));
        
        controller.tambahProduk(new Produk(
            "Paket Platinum Wedding", 
            "WEEDING", 
            new BigDecimal("40000000"), 
            "Gold + Drone coverage, 360° photos, Wedding planner"
        ));
        
        System.out.println("✅ 9 produk berhasil ditambahkan:");
        System.out.println("   - 3 Paket Foto");
        System.out.println("   - 3 Sewa Pakaian"); 
        System.out.println("   - 3 Paket Wedding");
    }
}