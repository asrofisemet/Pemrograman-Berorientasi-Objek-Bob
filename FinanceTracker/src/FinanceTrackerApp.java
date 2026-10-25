import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FinanceTrackerApp - Aplikasi Manajemen Keuangan Pribadi
 * 
 * Komponen GUI yang digunakan:
 * ============================================
 * 1. Top Level Container    : JFrame, JDialog
 * 2. Intermediate Component : JPanel, JScrollPane, JTabbedPane
 * 3. Atomic Component       : JButton, JLabel, JTextField, JComboBox, JRadioButton, JTable, JTextArea
 * 4. Layout Manager         : BorderLayout, GridBagLayout, FlowLayout, BoxLayout, GridLayout
 * 5. Event Handling         : ActionListener (Interface 1), MouseListener (Interface 2), 
 *                             ItemListener (Interface 3), KeyListener (Interface 4)
 */
public class FinanceTrackerApp extends JFrame implements ActionListener, MouseListener, ItemListener, KeyListener {

    // ===================== DATA MODEL =====================
    private ArrayList<String[]> transactions = new ArrayList<>();
    private DefaultTableModel tableModel;
    private double totalIncome = 0;
    private double totalExpense = 0;

    // ===================== KOMPONEN GUI =====================
    // --- Top Level Container ---
    // JFrame (this) & JDialog (untuk tambah transaksi)

    // --- Intermediate Component ---
    private JTabbedPane tabbedPane;
    private JPanel dashboardPanel, transactionPanel, aboutPanel;
    private JScrollPane tableScrollPane, aboutScrollPane;

    // --- Atomic Component ---
    private JLabel lblBalance, lblIncome, lblExpense, lblStatus, lblTransactionCount;
    private JButton btnAddTransaction, btnDeleteTransaction, btnClearAll, btnAbout, btnRefresh;
    private JTable transactionTable;
    private JTextField txtSearch;
    private JComboBox<String> cmbFilter;

    // --- Warna Tema ---
    private static final Color PRIMARY = new Color(30, 41, 59);       // Slate 800
    private static final Color SECONDARY = new Color(51, 65, 85);     // Slate 700
    private static final Color ACCENT = new Color(99, 102, 241);      // Indigo 500
    private static final Color ACCENT_HOVER = new Color(79, 70, 229); // Indigo 600
    private static final Color SUCCESS = new Color(34, 197, 94);      // Green 500
    private static final Color DANGER = new Color(239, 68, 68);       // Red 500
    private static final Color WARNING = new Color(245, 158, 11);     // Amber 500
    private static final Color TEXT_PRIMARY = new Color(241, 245, 249);// Slate 100
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184); // Slate 400
    private static final Color CARD_BG = new Color(30, 41, 59);       // Slate 800
    private static final Color BG_DARK = new Color(15, 23, 42);       // Slate 900
    private static final Color BORDER_COLOR = new Color(51, 65, 85);  // Slate 700

    // ===================== CONSTRUCTOR =====================
    public FinanceTrackerApp() {
        // --- Konfigurasi JFrame (Top Level Container #1) ---
        setTitle("💰 Finance Tracker - Manajemen Keuangan Pribadi");
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        // Tambah data contoh
        addSampleData();

        // Bangun GUI
        initComponents();

        // Update dashboard
        updateDashboard();

        setVisible(true);
    }

    // ===================== INISIALISASI KOMPONEN =====================
    private void initComponents() {
        // Layout utama menggunakan BorderLayout (Layout Manager #1)
        setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // --- TABBED PANE (Intermediate Component #1) ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_DARK);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Tab 1: Dashboard
        dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("📊 Dashboard", dashboardPanel);

        // Tab 2: Transaksi
        transactionPanel = createTransactionPanel();
        tabbedPane.addTab("📋 Daftar Transaksi", transactionPanel);

        // Tab 3: Tentang
        aboutPanel = createAboutPanel();
        tabbedPane.addTab("ℹ️ Tentang Aplikasi", aboutPanel);

        // Warna masing-masing tab
        tabbedPane.setForegroundAt(0, new Color(56, 189, 248));   // Sky 400 - Dashboard
        tabbedPane.setForegroundAt(1, new Color(74, 222, 128));   // Green 400 - Transaksi
        tabbedPane.setForegroundAt(2, new Color(251, 191, 36));   // Amber 400 - Tentang

        add(tabbedPane, BorderLayout.CENTER);

        // --- STATUS BAR ---
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    // ===================== HEADER PANEL =====================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Judul aplikasi
        JLabel titleLabel = new JLabel("💰 Finance Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Kelola Keuangan Pribadi Anda dengan Mudah");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        // Panel judul menggunakan BoxLayout (Layout Manager #2)
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(PRIMARY);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);

        header.add(titlePanel, BorderLayout.WEST);

        // Tombol-tombol header menggunakan FlowLayout (Layout Manager #3)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(PRIMARY);

        btnRefresh = createStyledButton("🔄 Refresh", SECONDARY);
        btnRefresh.addActionListener(this); // Event Handling - ActionListener (Interface #1)

        btnAbout = createStyledButton("ℹ️ Info", SECONDARY);
        btnAbout.addActionListener(this);

        btnPanel.add(btnRefresh);
        btnPanel.add(btnAbout);

        header.add(btnPanel, BorderLayout.EAST);

        return header;
    }

    // ===================== DASHBOARD PANEL =====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Kartu Ringkasan menggunakan GridLayout (Layout Manager #4) ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(BG_DARK);

        // Kartu Saldo
        lblBalance = new JLabel("Rp 0");
        JPanel balanceCard = createSummaryCard("💳 Saldo", lblBalance, ACCENT);

        // Kartu Pemasukan
        lblIncome = new JLabel("Rp 0");
        JPanel incomeCard = createSummaryCard("📈 Pemasukan", lblIncome, SUCCESS);

        // Kartu Pengeluaran
        lblExpense = new JLabel("Rp 0");
        JPanel expenseCard = createSummaryCard("📉 Pengeluaran", lblExpense, DANGER);

        // Kartu Jumlah Transaksi
        lblTransactionCount = new JLabel("0");
        JPanel countCard = createSummaryCard("📝 Transaksi", lblTransactionCount, WARNING);

        cardsPanel.add(balanceCard);
        cardsPanel.add(incomeCard);
        cardsPanel.add(expenseCard);
        cardsPanel.add(countCard);

        panel.add(cardsPanel, BorderLayout.NORTH);

        // --- Tabel Transaksi Terbaru ---
        JPanel recentPanel = new JPanel(new BorderLayout(0, 10));
        recentPanel.setBackground(BG_DARK);
        recentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel recentTitle = new JLabel("📋 Transaksi Terbaru");
        recentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentTitle.setForeground(TEXT_PRIMARY);
        recentPanel.add(recentTitle, BorderLayout.NORTH);

        // Tabel
        String[] columns = {"No", "Tanggal", "Kategori", "Keterangan", "Tipe", "Jumlah (Rp)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tidak bisa diedit langsung
            }
        };

        transactionTable = new JTable(tableModel);
        styleTable(transactionTable);

        // MouseListener pada tabel (Event Handling - Interface #2)
        transactionTable.addMouseListener(this);

        // JScrollPane (Intermediate Component #2)
        tableScrollPane = new JScrollPane(transactionTable);
        tableScrollPane.getViewport().setBackground(CARD_BG);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        recentPanel.add(tableScrollPane, BorderLayout.CENTER);

        panel.add(recentPanel, BorderLayout.CENTER);

        return panel;
    }

    // ===================== TRANSACTION PANEL =====================
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Toolbar ---
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(CARD_BG);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        // Panel tombol aksi (kiri)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionPanel.setBackground(CARD_BG);

        btnAddTransaction = createStyledButton("➕ Tambah Transaksi", ACCENT);
        btnAddTransaction.addActionListener(this);

        btnDeleteTransaction = createStyledButton("🗑️ Hapus", DANGER);
        btnDeleteTransaction.addActionListener(this);

        btnClearAll = createStyledButton("🧹 Hapus Semua", WARNING);
        btnClearAll.addActionListener(this);

        actionPanel.add(btnAddTransaction);
        actionPanel.add(btnDeleteTransaction);
        actionPanel.add(btnClearAll);

        toolbar.add(actionPanel, BorderLayout.WEST);

        // Panel pencarian & filter (kanan)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(CARD_BG);

        JLabel lblSearch = new JLabel("🔍");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setBackground(SECONDARY);
        txtSearch.setForeground(TEXT_PRIMARY);
        txtSearch.setCaretColor(TEXT_PRIMARY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtSearch.setToolTipText("Ketik untuk mencari transaksi...");

        // KeyListener pada field pencarian (Event Handling - Interface #4)
        txtSearch.addKeyListener(this);

        String[] filterOptions = {"Semua", "Pemasukan", "Pengeluaran"};
        cmbFilter = new JComboBox<>(filterOptions);
        cmbFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbFilter.setBackground(SECONDARY);
        cmbFilter.setForeground(TEXT_PRIMARY);

        // ItemListener pada ComboBox (Event Handling - Interface #3)
        cmbFilter.addItemListener(this);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(cmbFilter);

        toolbar.add(searchPanel, BorderLayout.EAST);

        panel.add(toolbar, BorderLayout.NORTH);

        // Info label
        JLabel infoLabel = new JLabel("💡 Klik kanan pada baris tabel untuk melihat detail transaksi");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        panel.add(infoLabel, BorderLayout.SOUTH);

        // Reuse tabel dari dashboard (sudah dibuat)
        JScrollPane sp = new JScrollPane(transactionTable);
        sp.getViewport().setBackground(CARD_BG);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    // ===================== ABOUT PANEL =====================
    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Menggunakan GridBagLayout (Layout Manager #5)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(CARD_BG);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Judul
        gbc.gridy = 0;
        JLabel aboutTitle = new JLabel("💰 Finance Tracker v1.0", JLabel.CENTER);
        aboutTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        aboutTitle.setForeground(ACCENT);
        contentPanel.add(aboutTitle, gbc);

        gbc.gridy = 1;
        JLabel aboutSubtitle = new JLabel("Aplikasi Manajemen Keuangan Pribadi", JLabel.CENTER);
        aboutSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutSubtitle.setForeground(TEXT_SECONDARY);
        contentPanel.add(aboutSubtitle, gbc);

        gbc.gridy = 2;
        contentPanel.add(Box.createVerticalStrut(15), gbc);

        // Info komponen
        String aboutText = 
            "══════════════════════════════════════════\n" +
            "          KOMPONEN GUI YANG DIGUNAKAN\n" +
            "══════════════════════════════════════════\n\n" +
            "🏗️ TOP LEVEL CONTAINER:\n" +
            "   • JFrame  → Window utama aplikasi\n" +
            "   • JDialog → Dialog tambah transaksi & detail\n\n" +
            "📦 INTERMEDIATE COMPONENT:\n" +
            "   • JPanel      → Container untuk mengelompokkan komponen\n" +
            "   • JScrollPane → Scroll pada tabel & halaman about\n" +
            "   • JTabbedPane → Tab navigasi (Dashboard, Transaksi, About)\n\n" +
            "🔘 ATOMIC COMPONENT:\n" +
            "   • JButton      → Tombol aksi (Tambah, Hapus, dll)\n" +
            "   • JLabel       → Label teks & judul\n" +
            "   • JTextField   → Input teks (keterangan, jumlah, pencarian)\n" +
            "   • JComboBox    → Dropdown kategori & filter\n" +
            "   • JRadioButton → Pilihan tipe transaksi (Pemasukan/Pengeluaran)\n" +
            "   • JTable       → Tabel data transaksi\n" +
            "   • JTextArea    → Area teks di halaman About\n\n" +
            "📐 LAYOUT MANAGER:\n" +
            "   • BorderLayout  → Layout utama JFrame & panel\n" +
            "   • GridBagLayout → Layout halaman About\n" +
            "   • FlowLayout    → Layout tombol-tombol aksi\n" +
            "   • BoxLayout     → Layout vertikal judul header\n" +
            "   • GridLayout    → Layout kartu ringkasan dashboard\n\n" +
            "⚡ EVENT HANDLING (4 Interface):\n" +
            "   • ActionListener → Klik tombol (tambah, hapus, refresh)\n" +
            "   • MouseListener  → Klik kanan pada tabel untuk detail\n" +
            "   • ItemListener   → Perubahan pilihan filter ComboBox\n" +
            "   • KeyListener    → Real-time search saat mengetik\n\n" +
            "══════════════════════════════════════════\n" +
            "  Dibuat untuk Tugas Latihan Materi GUI\n" +
            "  Mata Kuliah: Pemrograman Berorientasi Objek\n" +
            "══════════════════════════════════════════\n";

        gbc.gridy = 3;
        JTextArea aboutTextArea = new JTextArea(aboutText);
        aboutTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        aboutTextArea.setBackground(new Color(15, 23, 42));
        aboutTextArea.setForeground(TEXT_PRIMARY);
        aboutTextArea.setEditable(false);
        aboutTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // JScrollPane (Intermediate Component #3)
        aboutScrollPane = new JScrollPane(aboutTextArea);
        aboutScrollPane.setPreferredSize(new Dimension(600, 350));
        aboutScrollPane.getViewport().setBackground(BG_DARK);
        aboutScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        contentPanel.add(aboutScrollPane, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // ===================== STATUS BAR =====================
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(PRIMARY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        lblStatus = new JLabel("✅ Aplikasi siap digunakan");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(TEXT_SECONDARY);

        JLabel lblVersion = new JLabel("Finance Tracker v1.0 | Java Swing GUI");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(TEXT_SECONDARY);

        statusBar.add(lblStatus, BorderLayout.WEST);
        statusBar.add(lblVersion, BorderLayout.EAST);

        return statusBar;
    }

    // ===================== DIALOG TAMBAH TRANSAKSI =====================
    /**
     * JDialog (Top Level Container #2)
     * Menggunakan GridBagLayout (Layout Manager #5)
     */
    private void showAddTransactionDialog() {
        JDialog dialog = new JDialog(this, "➕ Tambah Transaksi Baru", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(CARD_BG);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Judul dialog
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel dialogTitle = new JLabel("Masukkan Detail Transaksi", JLabel.CENTER);
        dialogTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dialogTitle.setForeground(ACCENT);
        formPanel.add(dialogTitle, gbc);

        gbc.gridwidth = 1;

        // Tanggal
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblDate = createFormLabel("📅 Tanggal:");
        formPanel.add(lblDate, gbc);

        gbc.gridx = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JTextField txtDate = createFormTextField(sdf.format(new Date()));
        formPanel.add(txtDate, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblCategory = createFormLabel("📁 Kategori:");
        formPanel.add(lblCategory, gbc);

        gbc.gridx = 1;
        String[] categories = {"Makanan", "Transportasi", "Belanja", "Tagihan", "Hiburan", "Gaji", "Investasi", "Lainnya"};
        JComboBox<String> cmbCategory = new JComboBox<>(categories);
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbCategory.setBackground(SECONDARY);
        cmbCategory.setForeground(TEXT_PRIMARY);
        formPanel.add(cmbCategory, gbc);

        // Keterangan
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblDesc = createFormLabel("📝 Keterangan:");
        formPanel.add(lblDesc, gbc);

        gbc.gridx = 1;
        JTextField txtDesc = createFormTextField("");
        formPanel.add(txtDesc, gbc);

        // Tipe (JRadioButton - Atomic Component)
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblType = createFormLabel("🔄 Tipe:");
        formPanel.add(lblType, gbc);

        gbc.gridx = 1;
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        typePanel.setBackground(CARD_BG);

        JRadioButton rbIncome = new JRadioButton("Pemasukan");
        rbIncome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rbIncome.setForeground(SUCCESS);
        rbIncome.setBackground(CARD_BG);
        rbIncome.setSelected(true);

        JRadioButton rbExpense = new JRadioButton("Pengeluaran");
        rbExpense.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rbExpense.setForeground(DANGER);
        rbExpense.setBackground(CARD_BG);

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(rbIncome);
        typeGroup.add(rbExpense);

        typePanel.add(rbIncome);
        typePanel.add(rbExpense);
        formPanel.add(typePanel, gbc);

        // Jumlah
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblAmount = createFormLabel("💰 Jumlah (Rp):");
        formPanel.add(lblAmount, gbc);

        gbc.gridx = 1;
        JTextField txtAmount = createFormTextField("");
        formPanel.add(txtAmount, gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        JPanel btnDialogPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnDialogPanel.setBackground(CARD_BG);

        JButton btnSave = createStyledButton("💾 Simpan", ACCENT);
        JButton btnCancel = createStyledButton("❌ Batal", DANGER);

        // ActionListener pada tombol dialog
        btnSave.addActionListener(e -> {
            String date = txtDate.getText().trim();
            String category = (String) cmbCategory.getSelectedItem();
            String desc = txtDesc.getText().trim();
            String type = rbIncome.isSelected() ? "Pemasukan" : "Pengeluaran";
            String amountStr = txtAmount.getText().trim();

            // Validasi
            if (date.isEmpty() || desc.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "⚠️ Semua field harus diisi!", 
                    "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr.replace(",", "").replace(".", ""));
                String[] transaction = {date, category, desc, type, String.valueOf(amount)};
                transactions.add(transaction);
                refreshTable();
                updateDashboard();
                lblStatus.setText("✅ Transaksi berhasil ditambahkan: " + desc);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "⚠️ Jumlah harus berupa angka!", 
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnDialogPanel.add(btnSave);
        btnDialogPanel.add(btnCancel);
        formPanel.add(btnDialogPanel, gbc);

        dialog.add(formPanel);
        dialog.setVisible(true);
    }

    // ===================== DIALOG DETAIL TRANSAKSI =====================
    private void showTransactionDetail(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) return;

        JDialog detailDialog = new JDialog(this, "📄 Detail Transaksi", true);
        detailDialog.setSize(400, 350);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.getContentPane().setBackground(CARD_BG);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("📄 Detail Transaksi #" + tableModel.getValueAt(row, 0), JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(ACCENT);
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        String[][] details = {
            {"📅 Tanggal", tableModel.getValueAt(row, 1).toString()},
            {"📁 Kategori", tableModel.getValueAt(row, 2).toString()},
            {"📝 Keterangan", tableModel.getValueAt(row, 3).toString()},
            {"🔄 Tipe", tableModel.getValueAt(row, 4).toString()},
            {"💰 Jumlah", tableModel.getValueAt(row, 5).toString()},
        };

        for (int i = 0; i < details.length; i++) {
            gbc.gridy = i + 1;

            gbc.gridx = 0;
            JLabel lbl = new JLabel(details[i][0] + ":");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(TEXT_SECONDARY);
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            JLabel val = new JLabel(details[i][1]);
            val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            if (details[i][0].contains("Tipe")) {
                val.setForeground(details[i][1].equals("Pemasukan") ? SUCCESS : DANGER);
                val.setFont(new Font("Segoe UI", Font.BOLD, 13));
            } else {
                val.setForeground(TEXT_PRIMARY);
            }
            panel.add(val, gbc);
        }

        gbc.gridx = 0; gbc.gridy = details.length + 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton btnClose = createStyledButton("✔️ Tutup", ACCENT);
        btnClose.addActionListener(e -> detailDialog.dispose());
        panel.add(btnClose, gbc);

        detailDialog.add(panel);
        detailDialog.setVisible(true);
    }

    // ===================== HELPER METHODS =====================

    private JPanel createSummaryCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor.darker(), 1),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);

        // MouseListener untuk efek hover pada kartu
        card.addMouseListener(this);

        return card;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Hover effect menggunakan MouseListener
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    private JTextField createFormTextField(String defaultText) {
        JTextField txt = new JTextField(defaultText, 18);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBackground(SECONDARY);
        txt.setForeground(TEXT_PRIMARY);
        txt.setCaretColor(TEXT_PRIMARY);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return txt;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(SECONDARY);
        header.setForeground(TEXT_PRIMARY);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        header.setReorderingAllowed(false);

        // Lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(40);   // No
        table.getColumnModel().getColumn(1).setPreferredWidth(100);  // Tanggal
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Kategori
        table.getColumnModel().getColumn(3).setPreferredWidth(200);  // Keterangan
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Tipe
        table.getColumnModel().getColumn(5).setPreferredWidth(130);  // Jumlah

        // Custom renderer untuk pewarnaan tipe
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(CARD_BG);
                    if ("Pemasukan".equals(value)) {
                        c.setForeground(SUCCESS);
                    } else {
                        c.setForeground(DANGER);
                    }
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // Custom renderer untuk kolom jumlah
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(CARD_BG);
                    String type = table.getValueAt(row, 4).toString();
                    c.setForeground("Pemasukan".equals(type) ? SUCCESS : DANGER);
                }
                setHorizontalAlignment(JLabel.RIGHT);
                return c;
            }
        });

        // Renderer default untuk kolom lain
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(CARD_BG);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        };
        for (int i = 0; i < 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp " + nf.format(amount);
    }

    private void addSampleData() {
        transactions.add(new String[]{"25/10/2026", "Gaji", "Gaji Bulanan Oktober", "Pemasukan", "5000000"});
        transactions.add(new String[]{"25/10/2026", "Makanan", "Makan siang di kantin", "Pengeluaran", "25000"});
        transactions.add(new String[]{"24/10/2026", "Transportasi", "Bensin motor", "Pengeluaran", "50000"});
        transactions.add(new String[]{"24/10/2026", "Investasi", "Dividen saham BBCA", "Pemasukan", "150000"});
        transactions.add(new String[]{"23/10/2026", "Belanja", "Beli buku kuliah PBO", "Pengeluaran", "85000"});
        transactions.add(new String[]{"23/10/2026", "Hiburan", "Nonton bioskop", "Pengeluaran", "50000"});
        transactions.add(new String[]{"22/10/2026", "Tagihan", "Bayar listrik", "Pengeluaran", "200000"});
        transactions.add(new String[]{"22/10/2026", "Lainnya", "Freelance project", "Pemasukan", "750000"});
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String filterType = (String) cmbFilter.getSelectedItem();
        String searchText = txtSearch.getText().trim().toLowerCase();

        int no = 1;
        totalIncome = 0;
        totalExpense = 0;

        for (String[] t : transactions) {
            double amount = Double.parseDouble(t[4]);
            if (t[3].equals("Pemasukan")) totalIncome += amount;
            else totalExpense += amount;

            // Filter
            boolean matchFilter = filterType.equals("Semua") || t[3].equals(filterType);
            boolean matchSearch = searchText.isEmpty() || 
                                  t[1].toLowerCase().contains(searchText) || 
                                  t[2].toLowerCase().contains(searchText) ||
                                  t[3].toLowerCase().contains(searchText);

            if (matchFilter && matchSearch) {
                tableModel.addRow(new Object[]{
                    no++, t[0], t[1], t[2], t[3], formatRupiah(amount)
                });
            }
        }
    }

    private void updateDashboard() {
        refreshTable();
        double balance = totalIncome - totalExpense;
        lblBalance.setText(formatRupiah(balance));
        lblIncome.setText(formatRupiah(totalIncome));
        lblExpense.setText(formatRupiah(totalExpense));
        lblTransactionCount.setText(String.valueOf(transactions.size()));

        // Warna saldo berdasarkan positif/negatif
        lblBalance.setForeground(balance >= 0 ? ACCENT : DANGER);
    }

    // ===================== EVENT HANDLING =====================

    // --- Interface #1: ActionListener ---
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == btnAddTransaction) {
            showAddTransactionDialog();
        } 
        else if (source == btnDeleteTransaction) {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "🗑️ Yakin ingin menghapus transaksi ini?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    transactions.remove(selectedRow);
                    updateDashboard();
                    lblStatus.setText("✅ Transaksi berhasil dihapus");
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Pilih transaksi yang ingin dihapus terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        } 
        else if (source == btnClearAll) {
            if (!transactions.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "🧹 Yakin ingin menghapus SEMUA transaksi?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    transactions.clear();
                    updateDashboard();
                    lblStatus.setText("✅ Semua transaksi berhasil dihapus");
                }
            }
        } 
        else if (source == btnRefresh) {
            updateDashboard();
            lblStatus.setText("🔄 Dashboard berhasil di-refresh");
        } 
        else if (source == btnAbout) {
            tabbedPane.setSelectedIndex(2); // Pindah ke tab About
        }
    }

    // --- Interface #2: MouseListener ---
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == transactionTable) {
            // Klik kanan untuk detail
            if (SwingUtilities.isRightMouseButton(e)) {
                int row = transactionTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    transactionTable.setRowSelectionInterval(row, row);
                    showTransactionDetail(row);
                }
            }
            // Double click juga bisa untuk detail
            if (e.getClickCount() == 2) {
                int row = transactionTable.getSelectedRow();
                showTransactionDetail(row);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        // Efek hover pada kartu summary
        if (e.getSource() instanceof JPanel) {
            JPanel panel = (JPanel) e.getSource();
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2),
                BorderFactory.createEmptyBorder(17, 17, 17, 17)
            ));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JPanel) {
            JPanel panel = (JPanel) e.getSource();
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
            ));
        }
    }

    // --- Interface #3: ItemListener ---
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cmbFilter && e.getStateChange() == ItemEvent.SELECTED) {
            refreshTable();
            lblStatus.setText("🔍 Filter: " + cmbFilter.getSelectedItem());
        }
    }

    // --- Interface #4: KeyListener ---
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == txtSearch) {
            refreshTable();
            lblStatus.setText("🔍 Mencari: \"" + txtSearch.getText() + "\"");
        }
    }

    // ===================== MAIN METHOD =====================
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Override beberapa UI default untuk dark theme
            UIManager.put("OptionPane.background", CARD_BG);
            UIManager.put("Panel.background", CARD_BG);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Jalankan di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new FinanceTrackerApp();
        });
    }
}
