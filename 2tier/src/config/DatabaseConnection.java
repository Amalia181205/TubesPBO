package src.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // Konfigurasi database - SESUAIKAN DENGAN DATABASE ANDA
    private static final String DATABASE_NAME = "panesya_studio";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    // Static block untuk load driver sekali saat class di-load
    static {
        loadJDBCDriver();
    }
    
    /**
     * Method untuk load MySQL JDBC Driver
     */
    private static void loadJDBCDriver() {
        System.out.println("🔧 Loading MySQL JDBC Driver...");
        
        // Coba beberapa cara load driver
        String[] driverClasses = {
            "com.mysql.cj.jdbc.Driver",  // Untuk MySQL 8.0+
            "com.mysql.jdbc.Driver"      // Untuk MySQL 5.x (legacy)
        };
        
        for (String driverClass : driverClasses) {
            try {
                Class.forName(driverClass);
                System.out.println("✅ Driver loaded successfully: " + driverClass);
                return; // Berhenti jika berhasil
            } catch (ClassNotFoundException e) {
                System.err.println("⚠️  Failed to load driver: " + driverClass);
            }
        }
        
        // Jika semua gagal, beri petunjuk
        System.err.println("\n❌ CRITICAL: MySQL JDBC Driver not found!");
        System.err.println("\n💡 SOLUTION:");
        System.err.println("1. Right-click project → Properties");
        System.err.println("2. Select 'Libraries'");
        System.err.println("3. Click 'Add JAR/Folder'");
        System.err.println("4. Browse to: " + System.getProperty("user.dir") + "/lib/");
        System.err.println("5. Select mysql-connector-java-8.0.33.jar");
        System.err.println("6. Click OK and Clean & Build project");
        
        // Coba alternatif: cek apakah JAR ada
        checkJarExists();
    }
    
    /**
     * Cek apakah file JAR ada di folder lib/
     */
    private static void checkJarExists() {
        String[] possibleJarNames = {
            "mysql-connector-java-8.0.33.jar",
            "mysql-connector-java-8.0.32.jar",
            "mysql-connector-java-8.0.31.jar",
            "mysql-connector-java-5.1.49.jar"
        };
        
        String projectDir = System.getProperty("user.dir");
        System.out.println("\n🔍 Checking JAR files in: " + projectDir + "/lib/");
        
        for (String jarName : possibleJarNames) {
            java.io.File jarFile = new java.io.File(projectDir + "/lib/" + jarName);
            if (jarFile.exists()) {
                System.out.println("✅ Found: " + jarName);
                System.out.println("   Path: " + jarFile.getAbsolutePath());
                System.out.println("   Size: " + jarFile.length() + " bytes");
            } else {
                System.out.println("❌ Not found: " + jarName);
            }
        }
    }
    
    /**
     * Mendapatkan koneksi ke database
     */
    public static Connection getConnection() {
        try {
            // Build connection URL
            String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=Asia/Jakarta",
                HOST, PORT, DATABASE_NAME
            );
            
            System.out.println("\n🔗 Attempting to connect...");
            System.out.println("   URL: " + url);
            System.out.println("   User: " + USERNAME);
            
            Connection conn = DriverManager.getConnection(url, USERNAME, PASSWORD);
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ SUCCESS: Connected to database!");
                System.out.println("   Database: " + conn.getCatalog());
                return conn;
            } else {
                throw new SQLException("Failed to establish connection");
            }
            
        } catch (SQLException e) {
            System.err.println("\n❌ CONNECTION FAILED!");
            System.err.println("   Error: " + e.getMessage());
            System.err.println("   SQL State: " + e.getSQLState());
            System.err.println("   Error Code: " + e.getErrorCode());
            
            // Berikan troubleshooting tips
            printTroubleshootingTips(e);
            return null;
        }
    }
    
    /**
     * Print troubleshooting tips berdasarkan error
     */
    private static void printTroubleshootingTips(SQLException e) {
        System.err.println("\n🔧 TROUBLESHOOTING:");
        
        if (e.getMessage().contains("Unknown database")) {
            System.err.println("1. Database '" + DATABASE_NAME + "' doesn't exist");
            System.err.println("   Run: CREATE DATABASE " + DATABASE_NAME + ";");
        } 
        else if (e.getMessage().contains("Access denied")) {
            System.err.println("1. Wrong username/password");
            System.err.println("   Try: mysql -u root -p");
            System.err.println("2. If no password, try empty string");
        }
        else if (e.getMessage().contains("Communications link failure")) {
            System.err.println("1. MySQL server not running");
            System.err.println("2. Check XAMPP/WAMP/MAMP control panel");
            System.err.println("3. Try: netstat -an | findstr 3306");
        }
        else {
            System.err.println("1. Check MySQL is running");
            System.err.println("2. Check database exists");
            System.err.println("3. Check username/password");
        }
        
        System.err.println("\n📋 QUICK CHECKS:");
        System.err.println("   - Open cmd/terminal");
        System.err.println("   - Type: mysql -u root");
        System.err.println("   - Type: SHOW DATABASES;");
        System.err.println("   - See if '" + DATABASE_NAME + "' is listed");
    }
    
    /**
     * Test koneksi database
     */
    public static void testConnection() {
        System.out.println("\n========================================");
        System.out.println("   DATABASE CONNECTION TEST");
        System.out.println("========================================");
        System.out.println("Database: " + DATABASE_NAME);
        System.out.println("Host: " + HOST + ":" + PORT);
        System.out.println("Username: " + USERNAME);
        System.out.println("========================================");
        
        Connection conn = null;
        try {
            conn = getConnection();
            
            if (conn != null) {
                // Test query sederhana
                System.out.println("\n🧪 Running test query...");
                var stmt = conn.createStatement();
                
                // Cek apakah ada tabel
                var rs = stmt.executeQuery("SHOW TABLES");
                int tableCount = 0;
                System.out.println("\n📊 Tables in database:");
                while (rs.next()) {
                    System.out.println("   - " + rs.getString(1));
                    tableCount++;
                }
                
                if (tableCount == 0) {
                    System.out.println("   ℹ️ No tables found. Database is empty.");
                    System.out.println("   Run SQL script to create tables.");
                }
                
                System.out.println("\n✅ TEST COMPLETE!");
                System.out.println("   Database is ready to use.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("   Connection closed.");
                } catch (SQLException e) {
                    // Ignore
                }
            }
        }
        
        System.out.println("========================================");
    }
    
    /**
     * Main method untuk testing langsung
     */
    public static void main(String[] args) {
        System.out.println("🚀 Starting Database Connection Test...");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        
        // Tampilkan classpath
        System.out.println("\n📁 Classpath:");
        String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(System.getProperty("path.separator"));
        for (String path : paths) {
            System.out.println("   " + path);
        }
        
        // Jalankan test
        testConnection();
        
        System.out.println("\n💡 Tips for NetBeans:");
        System.out.println("1. Project → Clean and Build");
        System.out.println("2. Project → Run");
        System.out.println("3. If error persists, restart NetBeans");
    }
}