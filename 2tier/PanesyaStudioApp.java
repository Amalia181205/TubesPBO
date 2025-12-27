import src.view.LoginDialog;
import src.view.MainFrame;
import javax.swing.*;

public class PanesyaStudioApp {
    public static void main(String[] args) {
        System.out.println("=== STARTING PANESYA STUDIO ===");
        System.out.println("Java version: " + System.getProperty("java.version"));
        
        // Initialize database jika kosong
        initDatabaseIfEmpty();
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                System.out.println("🔐 Opening login dialog...");
                LoginDialog loginDialog = new LoginDialog(null);
                loginDialog.setVisible(true);
                
                if (loginDialog.isLoginSuccess()) {
                    System.out.println("✅ Login successful!");
                    System.out.println("🚀 Creating MainFrame...");
                    
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                    
                    System.out.println("🎉 Application started successfully!");
                } else {
                    System.out.println("👋 Application closed by user");
                    System.exit(0);
                }
                
            } catch (Exception e) {
                System.err.println("❌ Fatal error: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to start application: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    private static void initDatabaseIfEmpty() {
        try {
            src.controller.ProdukController controller = new src.controller.ProdukController();
            int count = controller.getAllProduk().size();
            
            if (count == 0) {
                System.out.println("⚠️ Database empty, initializing...");
                // Jalankan InitDatabase
                src.init.InitDatabase.main(new String[]{});
            } else {
                System.out.println("✅ Database has " + count + " products");
            }
        } catch (Exception e) {
            System.err.println("❌ Error checking database: " + e.getMessage());
        }
    }
} // LAPTONYA NGEHANG PAK :(