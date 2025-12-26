package src.dao;



import src.config.DatabaseConnection;
import src.model.Produk;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdukDao {
    
    // ========== CREATE ==========
    public boolean tambahProduk(Produk produk) {
        String sql = "INSERT INTO produk (kode_produk, nama, jenis, deskripsi, harga, durasi_jam, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Generate kode produk jika kosong
            String kodeProduk = produk.getKodeProduk();
            if (kodeProduk == null || kodeProduk.trim().isEmpty()) {
                kodeProduk = generateKodeProduk(produk.getJenis());
            }
            
            pstmt.setString(1, kodeProduk);
            pstmt.setString(2, produk.getNama());
            pstmt.setString(3, produk.getJenis());
            pstmt.setString(4, produk.getDeskripsi());
            pstmt.setBigDecimal(5, produk.getHarga());
            pstmt.setInt(6, produk.getDurasiJam() != null ? produk.getDurasiJam() : 1);
            pstmt.setBoolean(7, produk.getIsActive() != null ? produk.getIsActive() : true);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    produk.setId(generatedKeys.getInt(1));
                    produk.setKodeProduk(kodeProduk);
                }
                System.out.println("✅ Produk ditambahkan: " + produk.getNama() + " (ID: " + produk.getId() + ")");
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error tambah produk: " + e.getMessage());
            return false;
        }
    }
    
    // ========== READ ALL ==========
    public List<Produk> getAllProduk() {
        List<Produk> produkList = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE is_active = TRUE ORDER BY jenis, nama";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Produk produk = mapResultSetToProduk(rs);
                produkList.add(produk);
            }
            
            System.out.println("✅ Mengambil " + produkList.size() + " produk aktif");
            return produkList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get all produk: " + e.getMessage());
            return produkList;
        }
    }
    
    // ========== READ BY ID ==========
    public Produk getProdukById(int id) {
        String sql = "SELECT * FROM produk WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProduk(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get produk by id: " + e.getMessage());
            return null;
        }
    }
    
    // ========== READ BY JENIS ==========
    public List<Produk> getProdukByJenis(String jenis) {
        List<Produk> produkList = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE jenis = ? AND is_active = TRUE ORDER BY nama";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, jenis);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Produk produk = mapResultSetToProduk(rs);
                produkList.add(produk);
            }
            
            System.out.println("✅ Found " + produkList.size() + " produk dengan jenis: " + jenis);
            return produkList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get produk by jenis: " + e.getMessage());
            return produkList;
        }
    }
    
    // ========== READ BY KODE ==========
    public Produk getProdukByKode(String kodeProduk) {
        String sql = "SELECT * FROM produk WHERE kode_produk = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kodeProduk);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProduk(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get produk by kode: " + e.getMessage());
            return null;
        }
    }
    
    // ========== UPDATE ==========
    public boolean updateProduk(Produk produk) {
        String sql = "UPDATE produk SET nama = ?, jenis = ?, deskripsi = ?, " +
                    "harga = ?, durasi_jam = ?, foto_path = ?, is_active = ? " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, produk.getNama());
            pstmt.setString(2, produk.getJenis());
            pstmt.setString(3, produk.getDeskripsi());
            pstmt.setBigDecimal(4, produk.getHarga());
            pstmt.setInt(5, produk.getDurasiJam() != null ? produk.getDurasiJam() : 1);
            pstmt.setString(6, produk.getFotoPath());
            pstmt.setBoolean(7, produk.getIsActive() != null ? produk.getIsActive() : true);
            pstmt.setInt(8, produk.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("✅ Produk diupdate: ID " + produk.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error update produk: " + e.getMessage());
            return false;
        }
    }
    
    // ========== DELETE (SOFT DELETE) ==========
    public boolean hapusProduk(int id) {
        // Soft delete: set is_active = false
        String sql = "UPDATE produk SET is_active = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("✅ Produk dihapus (soft delete): ID " + id);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error hapus produk: " + e.getMessage());
            return false;
        }
    }
    
    // ========== SEARCH ==========
    public List<Produk> searchProduk(String keyword) {
        List<Produk> produkList = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE is_active = TRUE AND " +
                    "(nama LIKE ? OR deskripsi LIKE ? OR jenis LIKE ? OR kode_produk LIKE ?) " +
                    "ORDER BY nama";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Produk produk = mapResultSetToProduk(rs);
                produkList.add(produk);
            }
            
            System.out.println("✅ Search found " + produkList.size() + " produk for: " + keyword);
            return produkList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error search produk: " + e.getMessage());
            return produkList;
        }
    }
    
    // ========== STATISTICS ==========
    public int getTotalProdukCount() {
        String sql = "SELECT COUNT(*) FROM produk WHERE is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get total produk count: " + e.getMessage());
            return 0;
        }
    }
    
    public int getProdukCountByJenis(String jenis) {
        String sql = "SELECT COUNT(*) FROM produk WHERE jenis = ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, jenis);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get produk count by jenis: " + e.getMessage());
            return 0;
        }
    }
    
    // ========== HELPER METHODS ==========
    private Produk mapResultSetToProduk(ResultSet rs) throws SQLException {
        return new Produk(
            rs.getInt("id"),
            rs.getString("kode_produk"),
            rs.getString("nama"),
            rs.getString("jenis"),
            rs.getString("deskripsi"),
            rs.getBigDecimal("harga"),
            rs.getInt("durasi_jam"),
            rs.getString("foto_path"),
            rs.getBoolean("is_active")
        );
    }
    
    private String generateKodeProduk(String jenis) {
        String prefix;
        switch (jenis.toUpperCase()) {
            case "PAKET": prefix = "PKT"; break;
            case "SEWA": prefix = "SWA"; break;
            case "WEEDING": prefix = "WDG"; break;
            default: prefix = "PRD"; break;
        }
        
        // Cari nomor urut terakhir
        String sql = "SELECT MAX(kode_produk) FROM produk WHERE kode_produk LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, prefix + "%");
            ResultSet rs = pstmt.executeQuery();
            
            int lastNumber = 0;
            if (rs.next()) {
                String lastKode = rs.getString(1);
                if (lastKode != null && lastKode.startsWith(prefix)) {
                    try {
                        lastNumber = Integer.parseInt(lastKode.substring(prefix.length()));
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            }
            
            return prefix + String.format("%03d", lastNumber + 1);
            
        } catch (SQLException e) {
            System.err.println("❌ Error generate kode produk: " + e.getMessage());
            // Fallback ke timestamp
            return prefix + "-" + System.currentTimeMillis() % 10000;
        }
    }
} 
    

