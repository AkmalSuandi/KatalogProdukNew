package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import data.ProdukRepository;
import model.Produk;

public class KatalogController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private Label jumlahLabel;
    @FXML
    private TilePane katalogPane;

    private final ProdukRepository repository = new ProdukRepository();
    private Produk produkTerpilih = null;

    @FXML
    private void initialize() {
        fotoBox.getItems().add("makanan.png");
        fotoBox.getItems().add("minuman.png");
        fotoBox.setValue("makanan.png");
        try {
            showData();
        } catch (SQLException e) {
            pesanLabel.setText("Gagal membaca database. Lihat Output.");
            e.printStackTrace();
        }
    }

    private void showData() throws SQLException {
        ArrayList<Produk> daftar = repository.getAll();
        katalogPane.getChildren().clear();
        for (Produk produk : daftar) {
            VBox kartu = createProductCard(produk);
            katalogPane.getChildren().add(kartu);
        }
        jumlahLabel.setText("Jumlah produk: " + daftar.size());
    }

    private String resolveNamaFileFoto(String foto) {
        if (foto == null || foto.isBlank()) {
            return "makanan.png";
        }
        String bersih = foto.trim().toLowerCase();
        if (bersih.equals("makanan") || bersih.equals("makanan.png")) {
            return "makanan.png";
        }
        if (bersih.equals("minuman") || bersih.equals("minuman.png")) {
            return "minuman.png";
        }
        return bersih.endsWith(".png") ? bersih : bersih + ".png";
    }

    private VBox createProductCard(Produk produk) {
        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(170);
        fotoView.setFitHeight(110);
        fotoView.setPreserveRatio(true);
        StackPane bingkai = new StackPane();
        bingkai.setPrefSize(180, 120);
        bingkai.setMinSize(180, 120);
        bingkai.setMaxSize(180, 120);

        String namaFile = resolveNamaFileFoto(produk.getFoto());
        URL lokasi = getClass().getResource("/image/" + namaFile);
        if (lokasi != null) {
            Image gambar = new Image(lokasi.toExternalForm(),
                    170, 110, true, true);
            if (!gambar.isError()) {
                fotoView.setImage(gambar);
                bingkai.getChildren().add(fotoView);
            } else {
                bingkai.getChildren().add(new Label("Gambar tidak terbaca"));
            }
        } else {
            bingkai.getChildren().add(new Label("Gambar tidak tersedia"));
        }

        Label namaLabel = new Label(produk.getNama());
        namaLabel.setWrapText(true);
        namaLabel.getStyleClass().add("nama-produk");
        Label hargaLabel = new Label("Rp" + produk.getHarga());
        Label kategoriLabel = new Label("Kategori: " + (namaFile.startsWith("minuman") ? "Minuman" : "Makanan"));
        kategoriLabel.getStyleClass().add("muted");
        Label idLabel = new Label("ID: " + produk.getId());
        idLabel.getStyleClass().add("muted");

        Button pilihBtn = new Button("Edit");
        pilihBtn.getStyleClass().add("button-secondary");
        pilihBtn.setOnAction(e -> pilihProduk(produk));

        Button hapusBtn = new Button("Hapus");
        hapusBtn.getStyleClass().add("button-danger");
        hapusBtn.setOnAction(e -> hapusProdukLangsung(produk));

        HBox aksiBox = new HBox(8, pilihBtn, hapusBtn);

        VBox kartu = new VBox();
        kartu.setSpacing(8);
        kartu.setMinWidth(220);
        kartu.setPrefWidth(220);
        kartu.setMaxWidth(220);
        kartu.getStyleClass().add("kartu-produk");
        if (produkTerpilih != null && produkTerpilih.getId() == produk.getId()) {
            kartu.getStyleClass().add("kartu-terpilih");
        }
        kartu.setOnMouseClicked(e -> pilihProduk(produk));
        kartu.getChildren().addAll(bingkai, namaLabel, hargaLabel, kategoriLabel, idLabel, aksiBox);
        return kartu;
    }

    private void pilihProduk(Produk produk) {
        this.produkTerpilih = produk;
        namaField.setText(produk.getNama());
        hargaField.setText(String.valueOf(produk.getHarga()));
        String namaFile = resolveNamaFileFoto(produk.getFoto());
        fotoBox.setValue(namaFile);
        pesanLabel.setText("Produk dipilih (ID: " + produk.getId() + "). Ubah data lalu klik Update, atau klik Hapus.");
        try {
            showData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void hapusProdukLangsung(Produk produk) {
        try {
            repository.delete(produk.getId());
            if (produkTerpilih != null && produkTerpilih.getId() == produk.getId()) {
                resetFormInput();
            }
            showData();
            pesanLabel.setText("Produk '" + produk.getNama() + "' (ID: " + produk.getId() + ") berhasil dihapus.");
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menghapus produk. Lihat Output.");
            e.printStackTrace();
        }
    }

    @FXML
    private void saveProduct() {
        Produk produk;
        try {
            String nama = namaField.getText();
            long harga = Long.parseLong(hargaField.getText().trim());
            String foto = fotoBox.getValue();
            produk = new Produk(nama, harga, foto);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }
        try {
            repository.add(produk);
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menyimpan. Lihat Output.");
            e.printStackTrace();
            return;
        }
        resetFormInput();
        try {
            showData();
            pesanLabel.setText("Produk baru berhasil disimpan.");
        } catch (SQLException e) {
            pesanLabel.setText("Data tersimpan, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }

    @FXML
    private void updateProduct() {
        if (produkTerpilih == null) {
            pesanLabel.setText("Pilih produk pada katalog terlebih dahulu sebelum melakukan update.");
            return;
        }
        Produk produkBaru;
        try {
            String nama = namaField.getText();
            long harga = Long.parseLong(hargaField.getText().trim());
            String foto = fotoBox.getValue();
            produkBaru = new Produk(produkTerpilih.getId(), nama, harga, foto);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus angka bulat, contoh 12000.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }
        try {
            repository.update(produkBaru);
        } catch (SQLException e) {
            pesanLabel.setText("Gagal memperbarui produk. Lihat Output.");
            e.printStackTrace();
            return;
        }
        long idUpdated = produkTerpilih.getId();
        resetFormInput();
        try {
            showData();
            pesanLabel.setText("Produk ID " + idUpdated + " berhasil diperbarui.");
        } catch (SQLException e) {
            pesanLabel.setText("Data diperbarui, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteProduct() {
        if (produkTerpilih == null) {
            pesanLabel.setText("Pilih produk pada katalog terlebih dahulu sebelum menghapus.");
            return;
        }
        long idHapus = produkTerpilih.getId();
        String namaHapus = produkTerpilih.getNama();
        try {
            repository.delete(idHapus);
        } catch (SQLException e) {
            pesanLabel.setText("Gagal menghapus produk. Lihat Output.");
            e.printStackTrace();
            return;
        }
        resetFormInput();
        try {
            showData();
            pesanLabel.setText("Produk '" + namaHapus + "' (ID: " + idHapus + ") berhasil dihapus.");
        } catch (SQLException e) {
            pesanLabel.setText("Data dihapus, tetapi katalog gagal dimuat ulang.");
            e.printStackTrace();
        }
    }

    @FXML
    private void clearForm() {
        resetFormInput();
        pesanLabel.setText("Form dibersihkan.");
        try {
            showData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetFormInput() {
        produkTerpilih = null;
        namaField.clear();
        hargaField.clear();
        fotoBox.setValue("makanan.png");
    }
}
