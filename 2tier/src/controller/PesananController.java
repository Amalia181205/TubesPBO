package src.controller;

import src.model.Pesanan;
import src.service.PesananService;
import src.service.PesananServiceDefault;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class PesananController {
    
    private final PesananService pesananService;
    
    public PesananController() {
        this.pesananService = new PesananServiceDefault();
        System.out.println("🎮 Controller Pesanan siap!");
    }
    
    // ========== CREATE ==========
    public boolean tambahPesanan(Pesanan pesanan) {
        try {
            System.out.println("🎮 Controller: Memproses tambah pesanan...");
            
            // 🔥 TAMBAHKAN VALIDASI SEBELUM KE SERVICE
            if (pesanan == null) {
                System.err.println("❌ Pesanan object null!");
                return false;
            }
            
            if (pesanan.getPemesan() == null || pesanan.getPemesan().trim().isEmpty()) {
                System.err.println("❌ Nama pemesan tidak boleh kosong!");
                return false;
            }
            
            if (pesanan.getProduk() == null || pesanan.getProduk().trim().isEmpty()) {
                System.err.println("❌ Produk tidak boleh kosong!");
                return false;
            }
            
            if (pesanan.getTotal() == null || pesanan.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("❌ Total harus lebih dari 0!");
                return false;
            }
            
            boolean success = pesananService.tambahPesanan(pesanan);
            
            if (success) {
                System.out.println("✅ Pesanan berhasil ditambahkan!");
                System.out.println("   ID: " + pesanan.getId());
                System.out.println("   Pemesan: " + pesanan.getPemesan());
                System.out.println("   Total: " + pesanan.getTotal());
                return true;
            }
            
            System.err.println("❌ Service mengembalikan false!");
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Exception di controller: " + e.getMessage());
            e.printStackTrace(); // 🔥 TAMBAHKAN STACK TRACE UNTUK DEBUG
            return false;
        }
    }
    
    // 🔥 TAMBAHKAN METHOD UNTUK CHECKOUT LANGSUNG
    public boolean tambahPesananDariCheckout(String nama, String telepon, BigDecimal total, 
                                            String produk, String email, String jadwal) {
        try {
            System.out.println("🎮 Controller: Memproses checkout...");
            System.out.println("   Nama: " + nama);
            System.out.println("   Produk: " + produk);
            System.out.println("   Total: " + total);
            
            Pesanan pesanan = new Pesanan();
            pesanan.setPemesan(nama);
            pesanan.setTelepon(telepon);
            pesanan.setTotal(total);
            pesanan.setProduk(produk);
            pesanan.setStatus("PROSES");
            
            // Coba set email jika field ada di model
            try {
                pesanan.setEmail(email);
            } catch (Exception e) {
                System.out.println("⚠️ Email field tidak tersedia");
            }
            
            // Coba set jadwal jika field ada
            try {
                pesanan.setJadwal(jadwal);
            } catch (Exception e) {
                System.out.println("⚠️ Jadwal field tidak tersedia");
            }
            
            return tambahPesanan(pesanan);
            
        } catch (Exception e) {
            System.err.println("❌ Gagal proses checkout: " + e.getMessage());
            return false;
        }
    }
    
    // ========== READ ==========
    public List<Pesanan> getAllPesanan() {
        try {
            System.out.println("🎮 Controller: Mengambil semua pesanan...");
            List<Pesanan> pesananList = pesananService.getAllPesanan();
            System.out.println("   Ditemukan " + pesananList.size() + " pesanan");
            return pesananList;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal mengambil data: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public Pesanan getPesananById(int id) {
        try {
            System.out.println("🎮 Controller: Mencari pesanan ID " + id);
            Pesanan pesanan = pesananService.getPesananById(id);
            
            if (pesanan == null) {
                System.err.println("⚠️ Pesanan ID " + id + " tidak ditemukan");
            }
            
            return pesanan;
            
        } catch (Exception e) {
            System.err.println("❌ Pesanan tidak ditemukan: " + e.getMessage());
            return null;
        }
    }
    
    // ========== UPDATE ==========
    public boolean updatePesanan(Pesanan pesanan) {
        try {
            System.out.println("🎮 Controller: Memproses update pesanan ID " + pesanan.getId());
            
            // Validasi sebelum update
            if (pesanan.getId() == null) {
                System.err.println("❌ ID pesanan null!");
                return false;
            }
            
            boolean success = pesananService.updatePesanan(pesanan);
            
            if (success) {
                System.out.println("✅ Pesanan berhasil diupdate!");
                return true;
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal update pesanan: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean ubahStatusPesanan(int id, String status) {
        try {
            System.out.println("🎮 Controller: Mengubah status pesanan ID " + id + " -> " + status);
            
            // Validasi status
            List<String> validStatus = List.of("PROSES", "SUKSES", "BATAL", "MENUNGGU");
            if (!validStatus.contains(status)) {
                System.err.println("❌ Status tidak valid: " + status);
                return false;
            }
            
            boolean success = pesananService.ubahStatusPesanan(id, status);
            
            if (success) {
                System.out.println("✅ Status berhasil diubah!");
                return true;
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal ubah status: " + e.getMessage());
            return false;
        }
    }
    
    // ========== DELETE ==========
    public boolean hapusPesanan(int id) {
        try {
            System.out.println("🎮 Controller: Memproses hapus pesanan ID " + id);
            
            // Cek apakah pesanan ada
            Pesanan pesanan = getPesananById(id);
            if (pesanan == null) {
                System.err.println("❌ Pesanan tidak ditemukan, tidak bisa dihapus");
                return false;
            }
            
            boolean success = pesananService.hapusPesanan(id);
            
            if (success) {
                System.out.println("✅ Pesanan berhasil dihapus!");
                return true;
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal hapus pesanan: " + e.getMessage());
            return false;
        }
    }
    
    // ========== BUSINESS METHODS ==========
    public List<Pesanan> getPesananByStatus(String status) {
        try {
            System.out.println("🎮 Controller: Filter pesanan by status: " + status);
            List<Pesanan> filtered = pesananService.getPesananByStatus(status);
            System.out.println("   Ditemukan " + filtered.size() + " pesanan dengan status " + status);
            return filtered;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal filter pesanan: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Pesanan> getPesananByPemesan(String namaPemesan) {
        try {
            System.out.println("🎮 Controller: Mencari pesanan untuk pemesan: " + namaPemesan);
            List<Pesanan> allPesanan = getAllPesanan();
            List<Pesanan> result = new ArrayList<>();
            
            for (Pesanan p : allPesanan) {
                if (p.getPemesan() != null && 
                    p.getPemesan().toLowerCase().contains(namaPemesan.toLowerCase())) {
                    result.add(p);
                }
            }
            
            System.out.println("   Ditemukan " + result.size() + " pesanan");
            return result;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal mencari pesanan: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public double getTotalPendapatan() {
        try {
            double pendapatan = pesananService.getTotalPendapatan();
            System.out.println("🎮 Total pendapatan: Rp " + pendapatan);
            return pendapatan;
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung pendapatan: " + e.getMessage());
            return 0.0;
        }
    }
    
    public double getTotalPendapatanByStatus(String status) {
        try {
            List<Pesanan> pesananList = getPesananByStatus(status);
            double total = 0.0;
            
            for (Pesanan p : pesananList) {
                if (p.getTotal() != null) {
                    total += p.getTotal().doubleValue();
                }
            }
            
            System.out.println("🎮 Total pendapatan status " + status + ": Rp " + total);
            return total;
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung pendapatan: " + e.getMessage());
            return 0.0;
        }
    }
    
    public int getJumlahPesananByStatus(String status) {
        try {
            int jumlah = pesananService.getJumlahPesananByStatus(status);
            System.out.println("🎮 Jumlah pesanan status " + status + ": " + jumlah);
            return jumlah;
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung jumlah pesanan: " + e.getMessage());
            return 0;
        }
    }
    
    public int getTotalPesananCount() {
        try {
            int total = getAllPesanan().size();
            System.out.println("🎮 Total semua pesanan: " + total);
            return total;
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung total pesanan: " + e.getMessage());
            return 0;
        }
    }
    
    // 🔥 METHOD BARU: STATISTIK LENGKAP
    public void printStatistics() {
        System.out.println("📊 ===== STATISTIK PESANAN =====");
        System.out.println("Total Pesanan: " + getTotalPesananCount());
        System.out.println("PROSES: " + getJumlahPesananByStatus("PROSES"));
        System.out.println("SUKSES: " + getJumlahPesananByStatus("SUKSES"));
        System.out.println("BATAL: " + getJumlahPesananByStatus("BATAL"));
        System.out.println("MENUNGGU: " + getJumlahPesananByStatus("MENUNGGU"));
        System.out.println("Total Pendapatan: Rp " + getTotalPendapatan());
        System.out.println("=================================");
    }
    
    // ========== UTILITY ==========
    public void testConnection() {
        try {
            List<Pesanan> pesanan = getAllPesanan();
            System.out.println("✅ Koneksi database berhasil!");
            System.out.println("   Total pesanan: " + pesanan.size());
            
            // Tampilkan beberapa pesanan terbaru
            int limit = Math.min(pesanan.size(), 3);
            if (limit > 0) {
                System.out.println("   Contoh pesanan terbaru:");
                for (int i = 0; i < limit; i++) {
                    Pesanan p = pesanan.get(i);
                    System.out.println("   - ID: " + p.getId() + 
                                     ", Pemesan: " + p.getPemesan() +
                                     ", Status: " + p.getStatus());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Koneksi database gagal: " + e.getMessage());
        }
    }
    
    // 🔥 METHOD BARU: VALIDASI PESANAN
    public boolean isValidPesanan(Pesanan pesanan) {
        if (pesanan == null) {
            System.err.println("❌ Pesanan null");
            return false;
        }
        
        List<String> errors = new ArrayList<>();
        
        if (pesanan.getPemesan() == null || pesanan.getPemesan().trim().isEmpty()) {
            errors.add("Nama pemesan kosong");
        }
        
        if (pesanan.getProduk() == null || pesanan.getProduk().trim().isEmpty()) {
            errors.add("Produk kosong");
        }
        
        if (pesanan.getTotal() == null || pesanan.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Total invalid");
        }
        
        if (pesanan.getStatus() == null) {
            errors.add("Status kosong");
        } else {
            List<String> validStatus = List.of("PROSES", "SUKSES", "BATAL", "MENUNGGU");
            if (!validStatus.contains(pesanan.getStatus())) {
                errors.add("Status tidak valid: " + pesanan.getStatus());
            }
        }
        
        if (!errors.isEmpty()) {
            System.err.println("❌ Validasi gagal:");
            for (String error : errors) {
                System.err.println("   - " + error);
            }
            return false;
        }
        
        return true;
    }
    
    // 🔥 METHOD BARU: CLEANUP DATA TEST
    public void cleanupTestData() {
        try {
            System.out.println("🧹 Membersihkan data test...");
            List<Pesanan> allPesanan = getAllPesanan();
            int deletedCount = 0;
            
            for (Pesanan p : allPesanan) {
                // Hapus pesanan dengan nama test atau email test
                if (p.getPemesan() != null && 
                    (p.getPemesan().toLowerCase().contains("test") ||
                     p.getPemesan().toLowerCase().contains("contoh"))) {
                    
                    if (hapusPesanan(p.getId())) {
                        deletedCount++;
                    }
                }
            }
            
            System.out.println("✅ Cleanup selesai. Dihapus: " + deletedCount + " data test");
        } catch (Exception e) {
            System.err.println("❌ Gagal cleanup: " + e.getMessage());
        }
    }
}