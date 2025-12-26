package src.model;

import java.math.BigDecimal;

public class Produk {
    private Integer id;
    private String kodeProduk;
    private String nama;
    private String jenis; // "PAKET", "SEWA", "WEEDING"
    private String deskripsi;
    private BigDecimal harga;
    private Integer durasiJam;
    private String fotoPath;
    private Boolean isActive;
    
    // 🔥 Constructor lengkap sesuai database
    public Produk(Integer id, String kodeProduk, String nama, String jenis, 
                  String deskripsi, BigDecimal harga, Integer durasiJam, 
                  String fotoPath, Boolean isActive) {
        this.id = id;
        this.kodeProduk = kodeProduk;
        this.nama = nama;
        this.jenis = jenis;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.durasiJam = durasiJam;
        this.fotoPath = fotoPath;
        this.isActive = isActive;
    }
    
    // Constructor untuk kompatibilitas dengan kode lama
    public Produk(int id, String nama, String deskripsi, BigDecimal harga, String jenis) {
        this(id, generateKodeFromJenis(jenis), nama, jenis, deskripsi, 
             harga, 1, null, true);
    }
    
    // Constructor alternatif tanpa ID
    public Produk(String nama, String deskripsi, BigDecimal harga, String jenis) {
        this(0, generateKodeFromJenis(jenis), nama, jenis, deskripsi, 
             harga, 1, null, true);
    }
    
    // Constructor minimal untuk database
    public Produk(String kodeProduk, String nama, String jenis, String deskripsi, 
                  BigDecimal harga, Integer durasiJam) {
        this(null, kodeProduk, nama, jenis, deskripsi, harga, durasiJam, null, true);
    }
    
    // Helper method untuk generate kode produk
    private static String generateKodeFromJenis(String jenis) {
        switch (jenis.toUpperCase()) {
            case "PAKET": return "PKT-" + System.currentTimeMillis() % 10000;
            case "SEWA": return "SWA-" + System.currentTimeMillis() % 10000;
            case "WEEDING": return "WDG-" + System.currentTimeMillis() % 10000;
            default: return "PRD-" + System.currentTimeMillis() % 10000;
        }
    }
    
    // 🔥 Getters lengkap
    public Integer getId() { return id; }
    public String getKodeProduk() { return kodeProduk; }
    public String getNama() { return nama; }
    public String getJenis() { return jenis; }
    public String getDeskripsi() { return deskripsi; }
    public BigDecimal getHarga() { return harga; }
    public Integer getDurasiJam() { return durasiJam != null ? durasiJam : 1; }
    public String getFotoPath() { return fotoPath; }
    public Boolean getIsActive() { return isActive != null ? isActive : true; }
    
    // 🔥 Setters lengkap
    public void setId(Integer id) { this.id = id; }
    public void setKodeProduk(String kodeProduk) { this.kodeProduk = kodeProduk; }
    public void setNama(String nama) { this.nama = nama; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }
    public void setDurasiJam(Integer durasiJam) { this.durasiJam = durasiJam; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    @Override
    public String toString() {
        return nama + " - " + getFormattedHarga() + " (" + jenis + ")";
    }
    
    // Format harga dalam Rupiah
    public String getFormattedHarga() {
        if (harga == null) return "Rp 0";
        java.text.NumberFormat rupiahFormat = java.text.NumberFormat.getCurrencyInstance(
            java.util.Locale.forLanguageTag("id-ID"));
        return rupiahFormat.format(harga);
    }
    
    // Equals and hashCode for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produk other = (Produk) obj;
        return id != null && id.equals(other.id) || 
               (kodeProduk != null && kodeProduk.equals(other.kodeProduk));
    }
    
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (kodeProduk != null ? kodeProduk.hashCode() : 0);
        result = 31 * result + (nama != null ? nama.hashCode() : 0);
        return result;
    }
    
    // Helper method untuk cart operations
    public Produk copy() {
        return new Produk(this.id, this.kodeProduk, this.nama, this.jenis, 
                         this.deskripsi, this.harga, this.durasiJam, 
                         this.fotoPath, this.isActive);
    }
    
    // 🔥 Method untuk validasi
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty() &&
               jenis != null && !jenis.trim().isEmpty() &&
               harga != null && harga.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    // 🔥 Method untuk summary
    public String getSummary() {
        return String.format("%s - %s (Rp%,.0f)", 
            kodeProduk, nama, harga != null ? harga.doubleValue() : 0);
    }
}