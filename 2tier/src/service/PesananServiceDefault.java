package src.service;

import src.dao.PesananDao;
import src.model.Pesanan;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PesananServiceDefault implements PesananService {
    
    private final PesananDao pesananDao;
    
    public PesananServiceDefault() {
        this.pesananDao = new PesananDao();
        System.out.println("🔧 PesananServiceDefault initialized");
    }
    
    // ========== CREATE dengan Validasi ==========
    @Override
    public boolean tambahPesanan(Pesanan pesanan) {
        try {
            System.out.println("🔧 Service: Memproses tambah pesanan...");
            
            // 🔥 VALIDASI LENGKAP
            validatePesanan(pesanan);
            
            // 🔥 SET DEFAULT VALUES
            if (pesanan.getStatus() == null || pesanan.getStatus().trim().isEmpty()) {
                pesanan.setStatus("PROSES");
            }
            
            // 🔥 GENERATE KODE PESANAN JIKA KOSONG
            if (pesanan.getKodePesanan() == null || pesanan.getKodePesanan().trim().isEmpty()) {
                pesanan.setKodePesanan(generateKodePesanan());
            }
            
            System.out.println("🔧 Service: Menambahkan pesanan - " + 
                             pesanan.getPemesan() + " - Rp" + pesanan.getTotal());
            
            return pesananDao.tambahPesanan(pesanan);
            
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validasi gagal: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error service tambah pesanan: " + e.getMessage());
            throw new RuntimeException("Gagal menambah pesanan: " + e.getMessage(), e);
        }
    }
    
    // 🔥 METHOD VALIDASI TERPISAH
    private void validatePesanan(Pesanan pesanan) {
        if (pesanan == null) {
            throw new IllegalArgumentException("Pesanan tidak boleh null");
        }
        
        if (pesanan.getPemesan() == null || pesanan.getPemesan().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pemesan harus diisi!");
        }
        
        if (pesanan.getProduk() == null || pesanan.getProduk().trim().isEmpty()) {
            throw new IllegalArgumentException("Produk harus diisi!");
        }
        
        if (pesanan.getTotal() == null || pesanan.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total harus lebih dari 0!");
        }
        
        // Validasi status
        if (pesanan.getStatus() != null && !pesanan.getStatus().trim().isEmpty()) {
            List<String> allowedStatus = List.of("PROSES", "SUKSES", "BATAL", "MENUNGGU");
            if (!allowedStatus.contains(pesanan.getStatus().toUpperCase())) {
                throw new IllegalArgumentException("Status tidak valid: " + pesanan.getStatus());
            }
        }
    }
    
    // 🔥 GENERATE KODE PESANAN
    private String generateKodePesanan() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "PSN-" + timestamp.substring(timestamp.length() - 8);
    }
    
    // ========== READ ==========
    @Override
    public List<Pesanan> getAllPesanan() {
        try {
            System.out.println("🔧 Service: Mengambil semua pesanan...");
            List<Pesanan> pesananList = pesananDao.getAllPesanan();
            System.out.println("   Ditemukan " + pesananList.size() + " pesanan");
            return pesananList;
            
        } catch (Exception e) {
            System.err.println("❌ Error service get all pesanan: " + e.getMessage());
            throw new RuntimeException("Gagal mengambil data pesanan", e);
        }
    }
    
    @Override
    public Pesanan getPesananById(int id) {
        try {
            System.out.println("🔧 Service: Mencari pesanan ID " + id + "...");
            Pesanan pesanan = pesananDao.getPesananById(id);
            
            if (pesanan == null) {
                throw new RuntimeException("Pesanan dengan ID " + id + " tidak ditemukan!");
            }
            
            return pesanan;
            
        } catch (Exception e) {
            System.err.println("❌ Error service get pesanan by id: " + e.getMessage());
            throw new RuntimeException("Gagal mengambil pesanan ID " + id, e);
        }
    }
    
    @Override
    public List<Pesanan> getPesananByStatus(String status) {
        try {
            System.out.println("🔧 Service: Filter pesanan by status: " + status);
            // 🔥 GUNAKAN METHOD DAO LANGSUNG UNTUK LEBIH EFFISIEN
            List<Pesanan> filteredList = pesananDao.getPesananByStatus(status);
            System.out.println("   Ditemukan " + filteredList.size() + " pesanan dengan status " + status);
            return filteredList;
            
        } catch (Exception e) {
            System.err.println("❌ Error service get pesanan by status: " + e.getMessage());
            throw new RuntimeException("Gagal filter pesanan by status: " + status, e);
        }
    }
    
    // ========== UPDATE dengan Business Rules ==========
    @Override
    public boolean updatePesanan(Pesanan pesanan) {
        try {
            System.out.println("🔧 Service: Mengupdate pesanan ID " + pesanan.getId());
            
            // BUSINESS RULE: Cek apakah pesanan ada
            Pesanan existing = getPesananById(pesanan.getId());
            
            // BUSINESS RULE: Pesanan yang sudah SUKSES tidak bisa diubah
            if ("SUKSES".equals(existing.getStatus())) {
                throw new IllegalStateException("❌ Pesanan yang sudah SUKSES tidak bisa diubah!");
            }
            
            // Validasi input update
            validatePesanan(pesanan);
            
            return pesananDao.updatePesanan(pesanan);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("❌ Business rule violation: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Error service update pesanan: " + e.getMessage());
            throw new RuntimeException("Gagal update pesanan ID " + pesanan.getId(), e);
        }
    }
    
    @Override
    public boolean ubahStatusPesanan(int id, String status) {
        try {
            System.out.println("🔧 Service: Mengubah status pesanan ID " + id + " -> " + status);
            
            // BUSINESS RULE: Validasi status
            List<String> allowedStatus = List.of("PROSES", "SUKSES", "BATAL", "MENUNGGU");
            if (!allowedStatus.contains(status.toUpperCase())) {
                throw new IllegalArgumentException("❌ Status tidak valid: " + status);
            }
            
            // BUSINESS RULE: Cek apakah pesanan ada
            Pesanan existing = getPesananById(id);
            
            // BUSINESS RULE: Status transition validation
            if ("SUKSES".equals(existing.getStatus()) && !"SUKSES".equals(status)) {
                throw new IllegalStateException("❌ Pesanan yang sudah SUKSES tidak bisa diubah statusnya!");
            }
            
            return pesananDao.ubahStatusPesanan(id, status);
            
        } catch (Exception e) {
            System.err.println("❌ Error service ubah status: " + e.getMessage());
            throw new RuntimeException("Gagal ubah status pesanan ID " + id, e);
        }
    }
    
    // ========== DELETE dengan Business Rules ==========
    @Override
    public boolean hapusPesanan(int id) {
        try {
            System.out.println("🔧 Service: Menghapus pesanan ID " + id);
            
            // BUSINESS RULE: Cek apakah pesanan ada
            Pesanan pesanan = getPesananById(id);
            
            // BUSINESS RULE: Hanya pesanan dengan status PROSES atau BATAL yang bisa dihapus
            if ("SUKSES".equals(pesanan.getStatus())) {
                throw new IllegalStateException("❌ Pesanan yang sudah SUKSES tidak bisa dihapus!");
            }
            
            return pesananDao.hapusPesanan(id);
            
        } catch (Exception e) {
            System.err.println("❌ Error service hapus pesanan: " + e.getMessage());
            throw new RuntimeException("Gagal hapus pesanan ID " + id, e);
        }
    }
    
    // ========== BUSINESS METHODS ==========
    @Override
    public double getTotalPendapatan() {
        try {
            System.out.println("🔧 Service: Menghitung total pendapatan...");
            // 🔥 GUNAKAN METHOD DAO YANG LEBIH EFFISIEN
            BigDecimal total = pesananDao.getTotalPendapatan();
            System.out.println("   Total pendapatan: Rp" + total);
            return total.doubleValue();
            
        } catch (Exception e) {
            System.err.println("❌ Error service get total pendapatan: " + e.getMessage());
            return 0.0;
        }
    }
    
    @Override
    public int getJumlahPesananByStatus(String status) {
        try {
            System.out.println("🔧 Service: Menghitung jumlah pesanan status " + status);
            // 🔥 GUNAKAN METHOD DAO YANG LEBIH EFFISIEN
            int jumlah = pesananDao.getJumlahPesananByStatus(status);
            System.out.println("   Jumlah pesanan status " + status + ": " + jumlah);
            return jumlah;
            
        } catch (Exception e) {
            System.err.println("❌ Error service get jumlah pesanan by status: " + e.getMessage());
            return 0;
        }
    }
    
    // 🔥 TAMBAHKAN METHOD-METHOD BARU
    public int getTotalPesananCount() {
        try {
            return pesananDao.getTotalPesananCount();
        } catch (Exception e) {
            System.err.println("❌ Error get total pesanan count: " + e.getMessage());
            return 0;
        }
    }
    
    // 🔥 METHOD UNTUK STATISTIK LENGKAP
    public void printStatistics() {
        System.out.println("📊 ===== STATISTIK PESANAN SERVICE =====");
        System.out.println("Total Pesanan: " + getTotalPesananCount());
        System.out.println("Pesanan PROSES: " + getJumlahPesananByStatus("PROSES"));
        System.out.println("Pesanan SUKSES: " + getJumlahPesananByStatus("SUKSES"));
        System.out.println("Pesanan BATAL: " + getJumlahPesananByStatus("BATAL"));
        System.out.println("Pesanan MENUNGGU: " + getJumlahPesananByStatus("MENUNGGU"));
        System.out.println("Total Pendapatan: Rp" + getTotalPendapatan());
        System.out.println("=========================================");
    }
    
    // 🔥 METHOD UNTUK SEARCH PESANAN
    public List<Pesanan> searchPesanan(String keyword) {
        try {
            System.out.println("🔧 Service: Mencari pesanan dengan keyword: " + keyword);
            List<Pesanan> allPesanan = getAllPesanan();
            String lowerKeyword = keyword.toLowerCase();
            
            return allPesanan.stream()
                .filter(p -> 
                    (p.getPemesan() != null && p.getPemesan().toLowerCase().contains(lowerKeyword)) ||
                    (p.getProduk() != null && p.getProduk().toLowerCase().contains(lowerKeyword)) ||
                    (p.getKodePesanan() != null && p.getKodePesanan().toLowerCase().contains(lowerKeyword)) ||
                    (p.getTelepon() != null && p.getTelepon().contains(keyword))
                )
                .collect(Collectors.toList());
            
        } catch (Exception e) {
            System.err.println("❌ Error service search pesanan: " + e.getMessage());
            throw new RuntimeException("Gagal mencari pesanan dengan keyword: " + keyword, e);
        }
    }
}