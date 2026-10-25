import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Utility untuk mengambil screenshot otomatis dari FinanceTrackerApp.
 * Menangkap window utama dan semua dialog yang ditampilkan.
 */
public class ScreenshotHelper {

    private static final String SCREENSHOT_DIR = "screenshots";

    public static void main(String[] args) throws Exception {
        // Set Look and Feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeAndWait(() -> {
            try {
                FinanceTrackerApp app = new FinanceTrackerApp();
                app.setVisible(true);

                // Tunggu render selesai
                Timer timer = new Timer(1500, e -> {
                    try {
                        // Screenshot 1: Dashboard
                        captureComponent(app, "dashboard");
                        System.out.println("✅ Screenshot dashboard.png berhasil disimpan");

                        // Pindah ke tab Daftar Transaksi (index 1)
                        JTabbedPane tabbedPane = findTabbedPane(app);
                        if (tabbedPane != null) {
                            tabbedPane.setSelectedIndex(1);

                            Timer t2 = new Timer(500, e2 -> {
                                try {
                                    captureComponent(app, "daftar_transaksi");
                                    System.out.println("✅ Screenshot daftar_transaksi.png berhasil disimpan");

                                    // Pindah ke tab About (index 2)
                                    tabbedPane.setSelectedIndex(2);

                                    Timer t3 = new Timer(500, e3 -> {
                                        try {
                                            captureComponent(app, "tentang_aplikasi");
                                            System.out.println("✅ Screenshot tentang_aplikasi.png berhasil disimpan");
                                            System.out.println("\n📸 Semua screenshot berhasil disimpan di folder: " + SCREENSHOT_DIR + "/");
                                            System.out.println("Silakan tutup aplikasi.");
                                        } catch (Exception ex) { ex.printStackTrace(); }
                                    });
                                    t3.setRepeats(false);
                                    t3.start();

                                } catch (Exception ex) { ex.printStackTrace(); }
                            });
                            t2.setRepeats(false);
                            t2.start();
                        }

                    } catch (Exception ex) { ex.printStackTrace(); }
                });
                timer.setRepeats(false);
                timer.start();

            } catch (Exception ex) { ex.printStackTrace(); }
        });
    }

    private static void captureComponent(Component comp, String filename) throws Exception {
        BufferedImage image = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        comp.paint(image.getGraphics());

        File dir = new File(SCREENSHOT_DIR);
        if (!dir.exists()) dir.mkdirs();

        File outputFile = new File(dir, filename + ".png");
        ImageIO.write(image, "png", outputFile);
    }

    private static JTabbedPane findTabbedPane(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTabbedPane) return (JTabbedPane) comp;
            if (comp instanceof Container) {
                JTabbedPane found = findTabbedPane((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }
}
