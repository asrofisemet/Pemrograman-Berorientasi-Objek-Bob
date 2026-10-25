import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Latihan Materi GUI - 5C
 * Aplikasi sederhana "Manajemen Data Mahasiswa" berbasis Java Swing.
 *
 * Komponen yang dipakai:
 *  1. Top Level Container : JFrame, JDialog
 *  2. Intermediate Comp.  : JPanel, JTabbedPane, JScrollPane
 *  3. Atomic Component    : JLabel, JTextField, JComboBox, JRadioButton,
 *                           JCheckBox, JButton, JTable, JTextArea
 *  4. Layout Manager      : BorderLayout, GridBagLayout, FlowLayout, GridLayout
 *  5. Event Handling      : ActionListener, MouseListener, ItemListener,
 *                           KeyListener, WindowListener (5 interface)
 */
public class DataMahasiswaApp extends JFrame
        implements ActionListener, MouseListener, ItemListener, KeyListener, WindowListener {

    // ===== Atomic Components =====
    private JTextField txtNim, txtNama;
    private JComboBox<String> cmbJurusan;
    private JRadioButton rbLaki, rbPerempuan;
    private JCheckBox chkCoding, chkMusik, chkOlahraga;
    private JButton btnTambah, btnUbah, btnHapus, btnReset, btnTentang;
    private JTable tabel;
    private DefaultTableModel model;
    private JLabel lblStatus;
    private JTextArea areaStatistik;

    public DataMahasiswaApp() {
        // ===== TOP LEVEL CONTAINER (JFrame) =====
        super("Manajemen Data Mahasiswa");
        setSize(760, 560);
        setLocationRelativeTo(null);
        // Penutupan window ditangani sendiri di windowClosing (WindowListener)
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        // ===== LAYOUT MANAGER #1: BorderLayout pada content pane JFrame =====
        setLayout(new BorderLayout(8, 8));

        // ===== INTERMEDIATE COMPONENT: JTabbedPane =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Input Data", buatPanelInput());
        tabs.addTab("Statistik", buatPanelStatistik());
        // ChangeListener (event tambahan): update statistik saat tab dibuka
        tabs.addChangeListener(e -> updateStatistik());

        add(tabs, BorderLayout.CENTER);

        lblStatus = new JLabel(" Siap.");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());
        add(lblStatus, BorderLayout.SOUTH);
    }

    // ------------------------------------------------------------------
    // Panel tab 1: form input + tabel
    // ------------------------------------------------------------------
    private JPanel buatPanelInput() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Form (JPanel + LAYOUT MANAGER #2: GridBagLayout) ---
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Form Mahasiswa"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        txtNim = new JTextField(20);
        txtNim.addKeyListener(this); // KeyListener: NIM hanya boleh angka/huruf, max 12
        txtNama = new JTextField(20);

        cmbJurusan = new JComboBox<>(new String[]{
                "Teknik Informatika", "Sistem Informasi", "Teknik Elektro", "Teknik Mesin"});
        cmbJurusan.addActionListener(this); // ActionListener
        cmbJurusan.addItemListener(this);   // ItemListener

        // RadioButton dalam GridLayout + ButtonGroup
        rbLaki = new JRadioButton("Laki-laki", true);
        rbPerempuan = new JRadioButton("Perempuan");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbLaki);
        bg.add(rbPerempuan);
        rbLaki.addItemListener(this);
        rbPerempuan.addItemListener(this);
        // ===== LAYOUT MANAGER #3: GridLayout =====
        JPanel panelGender = new JPanel(new GridLayout(1, 2));
        panelGender.add(rbLaki);
        panelGender.add(rbPerempuan);

        chkCoding = new JCheckBox("Coding");
        chkMusik = new JCheckBox("Musik");
        chkOlahraga = new JCheckBox("Olahraga");
        chkCoding.addItemListener(this);
        chkMusik.addItemListener(this);
        chkOlahraga.addItemListener(this);
        JPanel panelHobi = new JPanel(new GridLayout(1, 3));
        panelHobi.add(chkCoding);
        panelHobi.add(chkMusik);
        panelHobi.add(chkOlahraga);

        tambahBaris(form, g, 0, "NIM", txtNim);
        tambahBaris(form, g, 1, "Nama", txtNama);
        tambahBaris(form, g, 2, "Jurusan", cmbJurusan);
        tambahBaris(form, g, 3, "Jenis Kelamin", panelGender);
        tambahBaris(form, g, 4, "Hobi", panelHobi);

        // --- Tombol (JPanel + LAYOUT MANAGER #4: FlowLayout) ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        btnTambah = new JButton("Tambah");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");
        btnTentang = new JButton("Tentang");
        for (JButton b : new JButton[]{btnTambah, btnUbah, btnHapus, btnReset, btnTentang}) {
            b.addActionListener(this); // ActionListener
            panelTombol.add(b);
        }

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(panelTombol, BorderLayout.SOUTH);

        // --- Tabel (JTable di dalam JScrollPane) ---
        model = new DefaultTableModel(
                new String[]{"NIM", "Nama", "Jurusan", "Gender", "Hobi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // tabel hanya untuk tampilan
            }
        };
        tabel = new JTable(model);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.addMouseListener(this); // MouseListener: klik baris -> isi form
        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createTitledBorder("Daftar Mahasiswa"));

        root.add(atas, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    private JPanel buatPanelStatistik() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        areaStatistik = new JTextArea();
        areaStatistik.setEditable(false);
        areaStatistik.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        p.add(new JScrollPane(areaStatistik), BorderLayout.CENTER);
        return p;
    }

    private void tambahBaris(JPanel p, GridBagConstraints g, int row, String label, JComponent comp) {
        g.gridy = row;
        g.gridx = 0;
        g.weightx = 0;
        p.add(new JLabel(label), g);
        g.gridx = 1;
        g.weightx = 1;
        p.add(comp, g);
    }

    // ------------------------------------------------------------------
    // Helper
    // ------------------------------------------------------------------
    private String ambilHobi() {
        StringBuilder sb = new StringBuilder();
        if (chkCoding.isSelected()) sb.append("Coding, ");
        if (chkMusik.isSelected()) sb.append("Musik, ");
        if (chkOlahraga.isSelected()) sb.append("Olahraga, ");
        if (sb.length() == 0) return "-";
        return sb.substring(0, sb.length() - 2);
    }

    private String ambilGender() {
        return rbLaki.isSelected() ? "Laki-laki" : "Perempuan";
    }

    private void resetForm() {
        txtNim.setText("");
        txtNama.setText("");
        cmbJurusan.setSelectedIndex(0);
        rbLaki.setSelected(true);
        chkCoding.setSelected(false);
        chkMusik.setSelected(false);
        chkOlahraga.setSelected(false);
        tabel.clearSelection();
        txtNim.setEditable(true);
        txtNim.requestFocus();
    }

    private boolean validasi() {
        if (txtNim.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Nama wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateStatistik() {
        int total = model.getRowCount();
        int laki = 0, perempuan = 0;
        for (int i = 0; i < total; i++) {
            if ("Laki-laki".equals(model.getValueAt(i, 3))) laki++;
            else perempuan++;
        }
        areaStatistik.setText(
                "=== STATISTIK MAHASISWA ===\n\n"
                        + "Total mahasiswa : " + total + "\n"
                        + "Laki-laki       : " + laki + "\n"
                        + "Perempuan       : " + perempuan + "\n");
    }

    private void tampilkanTentang() {
        // ===== TOP LEVEL CONTAINER kedua: JDialog =====
        JDialog dlg = new JDialog(this, "Tentang Aplikasi", true);
        dlg.setLayout(new BorderLayout(10, 10));
        JLabel isi = new JLabel("<html><center>Latihan Materi GUI - 5C<br>"
                + "Aplikasi Manajemen Data Mahasiswa<br>Java Swing</center></html>",
                SwingConstants.CENTER);
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dlg.dispose());
        JPanel bawah = new JPanel(new FlowLayout());
        bawah.add(ok);
        dlg.add(isi, BorderLayout.CENTER);
        dlg.add(bawah, BorderLayout.SOUTH);
        dlg.setSize(320, 180);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    // ------------------------------------------------------------------
    // EVENT HANDLING 1: ActionListener
    // ------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnTambah) {
            if (!validasi()) return;
            // cek NIM duplikat
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(txtNim.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "NIM sudah terdaftar!",
                            "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            model.addRow(new Object[]{txtNim.getText().trim(), txtNama.getText().trim(),
                    cmbJurusan.getSelectedItem(), ambilGender(), ambilHobi()});
            lblStatus.setText(" Data " + txtNama.getText().trim() + " berhasil ditambahkan.");
            resetForm();

        } else if (src == btnUbah) {
            int row = tabel.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih dulu baris yang mau diubah.");
                return;
            }
            if (!validasi()) return;
            String nimBaru = txtNim.getText().trim();
            // cek NIM baru tidak bentrok dengan baris lain
            for (int i = 0; i < model.getRowCount(); i++) {
                if (i != row && model.getValueAt(i, 0).equals(nimBaru)) {
                    JOptionPane.showMessageDialog(this, "NIM sudah dipakai mahasiswa lain!",
                            "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            model.setValueAt(nimBaru, row, 0);
            model.setValueAt(txtNama.getText().trim(), row, 1);
            model.setValueAt(cmbJurusan.getSelectedItem(), row, 2);
            model.setValueAt(ambilGender(), row, 3);
            model.setValueAt(ambilHobi(), row, 4);
            lblStatus.setText(" Data berhasil diubah.");
            resetForm();

        } else if (src == btnHapus) {
            int row = tabel.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih dulu baris yang mau dihapus.");
                return;
            }
            int jawab = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (jawab == JOptionPane.YES_OPTION) {
                model.removeRow(row);
                lblStatus.setText(" Data berhasil dihapus.");
                resetForm();
            }

        } else if (src == btnReset) {
            resetForm();
            lblStatus.setText(" Form direset.");

        } else if (src == btnTentang) {
            tampilkanTentang();

        } else if (src == cmbJurusan) {
            lblStatus.setText(" Jurusan dipilih: " + cmbJurusan.getSelectedItem());
        }
    }

    // ------------------------------------------------------------------
    // EVENT HANDLING 2: MouseListener (klik baris tabel -> isi form)
    // ------------------------------------------------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tabel.getSelectedRow();
        if (row < 0) return;
        txtNim.setText(model.getValueAt(row, 0).toString());
        txtNama.setText(model.getValueAt(row, 1).toString());
        cmbJurusan.setSelectedItem(model.getValueAt(row, 2));
        if ("Laki-laki".equals(model.getValueAt(row, 3))) rbLaki.setSelected(true);
        else rbPerempuan.setSelected(true);
        String hobi = model.getValueAt(row, 4).toString();
        chkCoding.setSelected(hobi.contains("Coding"));
        chkMusik.setSelected(hobi.contains("Musik"));
        chkOlahraga.setSelected(hobi.contains("Olahraga"));
        lblStatus.setText(" Baris dipilih: " + txtNama.getText());
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    // ------------------------------------------------------------------
    // EVENT HANDLING 3: ItemListener (checkbox / radio / combo)
    // ------------------------------------------------------------------
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) e.getSource();
            lblStatus.setText(" Hobi " + cb.getText()
                    + (cb.isSelected() ? " dicentang." : " dibatalkan."));
        } else if (e.getSource() instanceof JRadioButton && e.getStateChange() == ItemEvent.SELECTED) {
            lblStatus.setText(" Gender: " + ((JRadioButton) e.getSource()).getText());
        }
    }

    // ------------------------------------------------------------------
    // EVENT HANDLING 4: KeyListener (batasi input NIM)
    // ------------------------------------------------------------------
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isLetterOrDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            e.consume(); // tolak karakter selain huruf/angka
        } else if (txtNim.getText().length() >= 12 && c != KeyEvent.VK_BACK_SPACE) {
            e.consume(); // maksimal 12 karakter
        }
    }

    @Override public void keyPressed(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }

    // ------------------------------------------------------------------
    // EVENT HANDLING 5: WindowListener (konfirmasi saat keluar)
    // ------------------------------------------------------------------
    @Override
    public void windowClosing(WindowEvent e) {
        int jawab = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?",
                "Keluar", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    @Override public void windowOpened(WindowEvent e) { }
    @Override public void windowClosed(WindowEvent e) { }
    @Override public void windowIconified(WindowEvent e) { }
    @Override public void windowDeiconified(WindowEvent e) { }
    @Override public void windowActivated(WindowEvent e) { }
    @Override public void windowDeactivated(WindowEvent e) { }

    // ------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataMahasiswaApp().setVisible(true));
    }
}
