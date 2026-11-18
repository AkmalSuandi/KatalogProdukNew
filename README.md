🛒 Aplikasi Katalog Produk

Aplikasi Katalog Produk adalah program desktop berbasis Graphical User Interface (GUI) yang dibangun menggunakan JavaFX. Aplikasi ini dirancang untuk memudahkan manajemen data produk (Makanan dan Minuman) seperti sistem kasir (POS) atau etalase toko digital secara real-time.

Aplikasi ini menerapkan konsep CRUD (Create, Read, Update, Delete) yang terhubung langsung dengan database SQLite, serta menggunakan pola arsitektur MVC (Model-View-Controller) agar kode lebih rapi dan terstruktur.


✨ Fitur Utama

Create (Tambah Produk): Menyimpan data produk baru (Nama, Harga, Kategori/Foto) ke dalam database.

Read (Tampil Katalog): Menampilkan daftar produk dalam bentuk "Kartu Produk" (Product Card) yang digambar secara dinamis, lengkap dengan gambar, nama, dan harga.

Update (Edit Produk): Mengubah detail produk yang sudah ada di dalam database cukup dengan mengklik kartu produk di layar.

Delete (Hapus Produk): Menghapus produk dari database melalui tombol "Hapus" pada masing-masing kartu.

Validasi Input: Mencegah aplikasi crash dengan memastikan harga diisi dengan angka dan memberikan feedback langsung jika terjadi kesalahan.

Auto-Database: Otomatis membuat file database (database.db) dan tabel jika belum ada saat aplikasi pertama kali dijalankan.


🛠️ Teknologi yang Digunakan

Bahasa Pemrograman: Java (JDK 11 atau lebih baru)

Framework GUI: JavaFX

Manajemen Proyek: Maven

Database: SQLite (menggunakan sqlite-jdbc)

Desain UI: FXML (Scene Builder) & CSS



🚀 Langkah-langkah Mengaplikasikan dan Menjalankan Kode

Berikut adalah cara menjalankan proyek ini dari awal di IDE seperti Apache NetBeans, IntelliJ IDEA, atau Eclipse:

Langkah 1: Buat Proyek Maven

Buka IDE Anda, lalu buat proyek baru dengan tipe JavaFX Maven Project.

Beri nama proyek Anda (misal: KatalogProduk).

Langkah 2: Tambahkan Dependency di pom.xml

Pastikan Anda menambahkan library SQLite agar Java bisa berkomunikasi dengan database. Buka file pom.xml dan tambahkan kode ini di dalam tag <dependencies>:

<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.42.0.0</version> <!-- Gunakan versi terbaru jika ada -->
</dependency>


Catatan: Jangan lupa untuk melakukan "Clean and Build" atau "Reload Project" agar Maven mengunduh library tersebut.

Langkah 3: Salin File Kode Java & Resources

Buat package sesuai struktur di atas (misal: com.upi.store.model, com.upi.store.controller, dll).

Salin kode MainApp, Produk, Database, ProdukRepository, dan KatalogController ke masing-masing package-nya.

Salin desain katalog.fxml dan app.css ke dalam folder src/main/resources/view/.

Siapkan dua gambar ikon berukuran kecil (sekitar 170x110 piksel), beri nama makanan.png dan minuman.png, lalu letakkan di folder src/main/resources/image/.

Langkah 4: Hubungkan FXML dengan Controller

Jika Anda membuka katalog.fxml menggunakan Scene Builder, pastikan:

Controller class sudah disetel mengarah ke KatalogController.

Semua elemen yang memiliki ID (seperti namaField, hargaField, fotoBox, katalogPane) sudah ditautkan menggunakan tag fx:id.

Tombol aksi sudah ditautkan ke method (misal tombol "Simpan" mengarah ke On Action: #saveProduct).

Langkah 5: Jalankan Aplikasi

Buka file MainApp.java.

Klik kanan di area kode, lalu pilih Run File (atau klik tombol Play di IDE Anda).

Aplikasi akan terbuka. File database.db akan otomatis terbuat di folder utama proyek Anda.

Coba masukkan produk baru, edit, dan hapus untuk memastikan semuanya berjalan dengan lancar!

Contoh Tampilan:

<img width="1245" height="978" alt="image" src="https://github.com/user-attachments/assets/3a010b09-86ea-461f-866e-1f90edbdfad9" />
