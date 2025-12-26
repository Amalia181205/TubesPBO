package src.dao;

import src.config.DatabaseConnection;
import src.model.Pesanan;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PesananDao {
    
    // ========== CREATE ==========
    public boolean tambahPesanan(Pesanan pesanan) {
        // 🔥 SESUAIKAN DENGAN SCHEMA DATABASE BARU
        String sql = "INSERT INTO pesanan (pemesan, telepon, email, " +
                    "produk, total, status, keterangan, jadwal_foto, alamat) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, pesanan.getPemesan());
            pstmt.setString(2, pesanan.getTelepon());
            pstmt.setString(3, pesanan.getEmail());
            pstmt.setString(4, pesanan.getProduk());
            pstmt.setBigDecimal(5, pesanan.getTotal());
            pstmt.setString(6, pesanan.getStatus());
            pstmt.setString(7, pesanan.getKeterangan());
            
            // Set jadwal_foto jika ada
            try {
                pstmt.setDate(8, pesanan.getJadwalFoto() != null ? 
                    Date.valueOf(pesanan.getJadwalFoto()) : null);
            } catch (Exception e) {
                pstmt.setNull(8, Types.DATE);
            }
            
            // Set alamat jika ada
            try {
                pstmt.setString(9, pesanan.getAlamat());
            } catch (Exception e) {
                pstmt.setNull(9, Types.VARCHAR);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            
            // 🔥 AMBIL GENERATED ID
            if (rowsAffected > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pesanan.setId(generatedKeys.getInt(1));
                }
            }
            
            conn.commit();
            
            System.out.println("✅ Pesanan ditambahkan: ID=" + pesanan.getId() + 
                             ", Pemesan=" + pesanan.getPemesan());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error tambah pesanan: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, generatedKeys);
        }
    }
    
    // ========== READ ALL ==========
    public List<Pesanan> getAllPesanan() {
        List<Pesanan> pesananList = new ArrayList<>();
        // 🔥 SESUAIKAN DENGAN SCHEMA DATABASE BARU
        String sql = "SELECT id, pemesan, telepon, email, produk, total, " +
                    "status, keterangan, tanggal_pesan, tanggal_update, " +
                    "jadwal_foto, alamat, kode_pesanan " +
                    "FROM pesanan ORDER BY tanggal_pesan DESC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Pesanan pesanan = mapResultSetToPesanan(rs);
                pesananList.add(pesanan);
            }
            
            System.out.println("✅ Mengambil " + pesananList.size() + " pesanan");
            return pesananList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get all pesanan: " + e.getMessage());
            return pesananList;
        } finally {
            closeResources(stmt, rs);
        }
    }
    
    // ========== READ BY ID ==========
    public Pesanan getPesananById(int id) {
        String sql = "SELECT id, pemesan, telepon, email, produk, total, " +
                    "status, keterangan, tanggal_pesan, tanggal_update, " +
                    "jadwal_foto, alamat, kode_pesanan " +
                    "FROM pesanan WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPesanan(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get pesanan by id: " + e.getMessage());
            return null;
        } finally {
            closeResources(pstmt, rs);
        }
    }
    
    // ========== READ BY STATUS ==========
    public List<Pesanan> getPesananByStatus(String status) {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT id, pemesan, telepon, email, produk, total, " +
                    "status, keterangan, tanggal_pesan, tanggal_update, " +
                    "jadwal_foto, alamat, kode_pesanan " +
                    "FROM pesanan WHERE status = ? ORDER BY tanggal_pesan DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Pesanan pesanan = mapResultSetToPesanan(rs);
                pesananList.add(pesanan);
            }
            
            System.out.println("✅ Found " + pesananList.size() + " pesanan dengan status: " + status);
            return pesananList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get pesanan by status: " + e.getMessage());
            return pesananList;
        } finally {
            closeResources(pstmt, rs);
        }
    }
    
    // ========== UPDATE ==========
    public boolean updatePesanan(Pesanan pesanan) {
        // 🔥 SESUAIKAN DENGAN SCHEMA DATABASE BARU
        String sql = "UPDATE pesanan SET pemesan = ?, telepon = ?, email = ?, " +
                    "produk = ?, total = ?, status = ?, keterangan = ?, " +
                    "jadwal_foto = ?, alamat = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, pesanan.getPemesan());
            pstmt.setString(2, pesanan.getTelepon());
            pstmt.setString(3, pesanan.getEmail());
            pstmt.setString(4, pesanan.getProduk());
            pstmt.setBigDecimal(5, pesanan.getTotal());
            pstmt.setString(6, pesanan.getStatus());
            pstmt.setString(7, pesanan.getKeterangan());
            
            // Set jadwal_foto jika ada
            try {
                pstmt.setDate(8, pesanan.getJadwalFoto() != null ? 
                    Date.valueOf(pesanan.getJadwalFoto()) : null);
            } catch (Exception e) {
                pstmt.setNull(8, Types.DATE);
            }
            
            // Set alamat jika ada
            try {
                pstmt.setString(9, pesanan.getAlamat());
            } catch (Exception e) {
                pstmt.setNull(9, Types.VARCHAR);
            }
            
            pstmt.setInt(10, pesanan.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Pesanan diupdate: ID " + pesanan.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error update pesanan: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== DELETE ==========
    public boolean hapusPesanan(int id) {
        String sql = "DELETE FROM pesanan WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Pesanan dihapus: ID " + id);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error hapus pesanan: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== UPDATE STATUS ==========
    public boolean ubahStatusPesanan(int id, String status) {
        String sql = "UPDATE pesanan SET status = ?, tanggal_update = NOW() WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            
            System.out.println("✅ Status diubah: ID " + id + " -> " + status);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error ubah status: " + e.getMessage());
            rollback(conn);
            return false;
        } finally {
            closeResources(pstmt, null);
        }
    }
    
    // ========== STATISTICS ==========
    public int getTotalPesananCount() {
        String sql = "SELECT COUNT(*) FROM pesanan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get total pesanan count: " + e.getMessage());
            return 0;
        }
    }
    
    public BigDecimal getTotalPendapatan() {
        String sql = "SELECT SUM(total) FROM pesanan WHERE status = 'SUKSES'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total : BigDecimal.ZERO;
            }
            return BigDecimal.ZERO;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get total pendapatan: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    public int getJumlahPesananByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM pesanan WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error get jumlah pesanan by status: " + e.getMessage());
            return 0;
        }
    }
    
    // ========== HELPER METHODS ==========
    private Pesanan mapResultSetToPesanan(ResultSet rs) throws SQLException {
        Pesanan pesanan = new Pesanan();
        pesanan.setId(rs.getInt("id"));
        pesanan.setPemesan(rs.getString("pemesan"));
        pesanan.setTelepon(rs.getString("telepon"));
        pesanan.setEmail(rs.getString("email"));
        pesanan.setProduk(rs.getString("produk"));
        pesanan.setTotal(rs.getBigDecimal("total"));
        pesanan.setStatus(rs.getString("status"));
        pesanan.setKeterangan(rs.getString("keterangan"));
        pesanan.setTanggalPesan(rs.getTimestamp("tanggal_pesan").toLocalDateTime());
        
        if (rs.getTimestamp("tanggal_update") != null) {
            pesanan.setTanggalUpdate(rs.getTimestamp("tanggal_update").toLocalDateTime());
        }
        
        // 🔥 SET FIELD BARU
        try {
            if (rs.getDate("jadwal_foto") != null) {
                pesanan.setJadwal(rs.getDate("jadwal_foto").toString());
            }
        } catch (Exception e) {
            // Field tidak ada, skip
        }
        
        try {
            pesanan.setKodePesanan(rs.getString("kode_pesanan"));
        } catch (Exception e) {
            // Field tidak ada, skip
        }
        
        try {
            pesanan.setAlamat(rs.getString("alamat"));
        } catch (Exception e) {
            // Field tidak ada, skip
        }
        
        return pesanan;
    }
    
    private void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    
    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rollback: " + ex.getMessage());
            }
        }
    }
}