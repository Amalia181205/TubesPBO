package src.controller;

import src.model.Pesanan;
import src.service.PesananService;
import src.service.PesananServiceDefault;
import java.util.List;
import java.util.ArrayList;

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
            boolean success = pesananService.tambahPesanan(pesanan);
            
            if (success) {
                System.out.println("✅ Pesanan berhasil ditambahkan!");
                return true;
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal tambah pesanan: " + e.getMessage());
            return false;
        }
    }
    
    // ========== READ ==========
    public List<Pesanan> getAllPesanan() {
        try {
            System.out.println("🎮 Controller: Mengambil semua pesanan...");
            return pesananService.getAllPesanan();
            
        } catch (Exception e) {
            System.err.println("❌ Gagal mengambil data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Pesanan getPesananById(int id) {
        try {
            System.out.println("🎮 Controller: Mencari pesanan ID " + id);
            return pesananService.getPesananById(id);
            
        } catch (Exception e) {
            System.err.println("❌ Pesanan tidak ditemukan: " + e.getMessage());
            return null;
        }
    }
    
    // ========== UPDATE ==========
    public boolean updatePesanan(Pesanan pesanan) {
        try {
            System.out.println("🎮 Controller: Memproses update pesanan ID " + pesanan.getId());
            boolean success = pesananService.updatePesanan(pesanan);
            
            if (success) {
                System.out.println("✅ Pesanan berhasil diupdate!");
                return true;
            }
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Gagal update pesanan: " + e.getMessage());
            return false;
        }
    }
    
    public boolean ubahStatusPesanan(int id, String status) {
        try {
            System.out.println("🎮 Controller: Mengubah status pesanan ID " + id + " -> " + status);
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
            return pesananService.getPesananByStatus(status);
            
        } catch (Exception e) {
            System.err.println("❌ Gagal filter pesanan: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public double getTotalPendapatan() {
        try {
            return pesananService.getTotalPendapatan();
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung pendapatan: " + e.getMessage());
            return 0.0;
        }
    }
    
    public int getJumlahPesananByStatus(String status) {
        try {
            return pesananService.getJumlahPesananByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung jumlah pesanan: " + e.getMessage());
            return 0;
        }
    }
    
    // ========== UTILITY ==========
    public void testConnection() {
        try {
            List<Pesanan> pesanan = getAllPesanan();
            System.out.println("✅ Koneksi database berhasil!");
            System.out.println("   Total pesanan: " + pesanan.size());
        } catch (Exception e) {
            System.err.println("❌ Koneksi database gagal: " + e.getMessage());
        }
    }
}