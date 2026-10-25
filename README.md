# Latihan Materi GUI - 5C

**NIM  :** F1D20410123
**Nama :** Bob
**Kelas:** C

Aplikasi **Manajemen Data Mahasiswa** berbasis **Java Swing**. Pengguna bisa menambah, mengubah, menghapus, dan mereset data mahasiswa, serta melihat statistik jumlah mahasiswa.

## Cara Menjalankan

Pastikan JDK 11+ sudah terinstall.

```bash
javac DataMahasiswaApp.java
java DataMahasiswaApp
```

Atau langsung (JDK 11+): `java DataMahasiswaApp.java`

## Struktur Repositori

```
.
├── DataMahasiswaApp.java   # kode program
├── README.md               # dokumentasi ini
└── screenshots/            # tangkapan layar aplikasi
```

## Komponen GUI yang Digunakan

### 1. Top Level Container
Container tingkat paling atas yang bisa berdiri sendiri sebagai window dan menampung komponen lain.

| Komponen | Kegunaan di kode |
|---|---|
| `JFrame` | Window utama aplikasi (`DataMahasiswaApp extends JFrame`) |
| `JDialog` | Window dialog modal "Tentang Aplikasi" (method `tampilkanTentang()`) |

### 2. Intermediate Component
Container perantara yang menampung atomic component dan bisa ditaruh di dalam top level container.

| Komponen | Kegunaan di kode |
|---|---|
| `JPanel` | Mengelompokkan form, tombol, panel gender, panel hobi |
| `JTabbedPane` | Dua tab: "Input Data" dan "Statistik" |
| `JScrollPane` | Membungkus `JTable` dan `JTextArea` agar bisa di-scroll |

### 3. Atomic Component
Komponen dasar yang berinteraksi langsung dengan pengguna dan tidak bisa menampung komponen lain.

| Komponen | Kegunaan di kode |
|---|---|
| `JLabel` | Label form dan status bar |
| `JTextField` | Input NIM dan Nama |
| `JComboBox` | Pilihan jurusan |
| `JRadioButton` + `ButtonGroup` | Pilihan jenis kelamin |
| `JCheckBox` | Pilihan hobi (boleh lebih dari satu) |
| `JButton` | Tambah, Ubah, Hapus, Reset, Tentang |
| `JTable` | Menampilkan daftar mahasiswa |
| `JTextArea` | Menampilkan statistik |

### 4. Layout Manager
Mengatur posisi dan ukuran komponen di dalam container.

| Layout | Dipakai pada |
|---|---|
| `BorderLayout` | Content pane `JFrame` (tab di tengah, status bar di bawah), panel utama tab, dan `JDialog` |
| `GridBagLayout` | Panel form (label di kiri, input di kanan, rapi per baris) |
| `GridLayout` | Panel radio button gender (1x2) dan panel checkbox hobi (1x3) |
| `FlowLayout` | Panel tombol (tombol berjajar di tengah) |

### 5. Event Handling (5 interface)
Class `DataMahasiswaApp` meng-*implements* beberapa listener sekaligus.

| Interface | Method | Kegunaan |
|---|---|---|
| `ActionListener` | `actionPerformed()` | Klik tombol Tambah/Ubah/Hapus/Reset/Tentang dan pilih item combo jurusan |
| `MouseListener` | `mouseClicked()` | Klik baris tabel, data baris otomatis terisi ke form untuk diedit |
| `ItemListener` | `itemStateChanged()` | Perubahan checkbox hobi dan radio gender, info tampil di status bar |
| `KeyListener` | `keyTyped()` | Batasi input NIM: hanya huruf/angka dan maksimal 12 karakter |
| `WindowListener` | `windowClosing()` | Dialog konfirmasi "Yakin ingin keluar?" saat window ditutup |

Tambahan: `ChangeListener` (lambda) pada `JTabbedPane` untuk memperbarui statistik saat tab dibuka.

## Fitur
- **Tambah** data (validasi field kosong dan NIM duplikat)
- **Ubah** data (klik baris tabel, edit form, klik Ubah)
- **Hapus** data (dengan konfirmasi)
- **Reset** form
- **Statistik** jumlah mahasiswa total, laki-laki, dan perempuan
- **Tentang** (JDialog)

## Screenshot

> Tambahkan screenshot hasil run di folder `screenshots/`, lalu sesuaikan nama file di bawah.

**Tampilan awal**
![Tampilan awal](screenshots/01-tampilan-awal.png)

**Data ditambahkan dan baris dipilih**
![Pilih baris](screenshots/02-pilih-baris.png)

**Tab Statistik**
![Statistik](screenshots/03-statistik.png)

**Dialog Tentang**
![Tentang](screenshots/04-tentang.png)
