package src.service;

import src.model.Pesanan;
import java.util.List;

public interface PesananService {
    // CREATE
    boolean tambahPesanan(Pesanan pesanan);
    
    // READ
    List<Pesanan> getAllPesanan();
    Pesanan getPesananById(int id);
    List<Pesanan> getPesananByStatus(String status);
    
    // UPDATE
    boolean updatePesanan(Pesanan pesanan);
    boolean ubahStatusPesanan(int id, String status);
    
    // DELETE
    boolean hapusPesanan(int id);
    
    // BUSINESS METHODS
    double getTotalPendapatan();
    int getJumlahPesananByStatus(String status);
    
    // 🔥 TAMBAHKAN METHOD BARU
    int getTotalPesananCount();
    List<Pesanan> searchPesanan(String keyword);
    void printStatistics();
}