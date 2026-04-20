
package proyekpisau;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.*;

public class ReportPage {
    private User currentUser;
    private final String DB_URL = "jdbc:mysql://localhost:3306/sistem_pisau";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    private TableView<String[]> tableReport;
    private ComboBox<String> cbBulan;
    private ComboBox<String> cbEmoney;
    private Label lblTotalPemasukan;
    private Label lblTotalPengeluaran;

    public ReportPage(User user) {
        this.currentUser = user;
    }

    public ScrollPane getView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #D5F4F9;");

        //header title
        Label lblTitle = new Label("Transaction Report");
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblTitle.setTextFill(Color.web("#0066CC"));

        //filtering area (dikurangi spacing-nya dari 8 ke 5 agar lebih muat)
        HBox filterBox = new HBox(5);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        //label filter bulan 
        Label lblBulan = new Label("Month:");
        lblBulan.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
        lblBulan.setMinWidth(Region.USE_PREF_SIZE);

        cbBulan = new ComboBox<>();
        cbBulan.getItems().addAll("All Months", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        cbBulan.setValue("All Months");
        cbBulan.setPrefWidth(90); 

        //label filter wallet 
        Label lblWallet = new Label("Wallet:");
        lblWallet.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
        lblWallet.setMinWidth(Region.USE_PREF_SIZE);

        cbEmoney = new ComboBox<>();
        cbEmoney.getItems().add("All Wallet");
        loadUserWallets();
        cbEmoney.setValue("All Wallet");
        cbEmoney.setPrefWidth(90); 

        Button btnFilter = new Button("Apply");
        btnFilter.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        btnFilter.setMinWidth(Region.USE_PREF_SIZE); 
        btnFilter.setOnAction(e -> loadTransactionData());

        filterBox.getChildren().addAll(lblBulan, cbBulan, lblWallet, cbEmoney, btnFilter);

        //table data area
        tableReport = new TableView<>();
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableReport.setPrefHeight(300);
        
        Label headerTanggal = new Label("Date");
        headerTanggal.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        TableColumn<String[], String> colTanggal = new TableColumn<>();
        colTanggal.setGraphic(headerTanggal); 
        colTanggal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        Label headerWallet = new Label("Wallet");
        headerWallet.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        TableColumn<String[], String> colWallet = new TableColumn<>();
        colWallet.setGraphic(headerWallet);
        colWallet.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

        Label headerJumlah = new Label("Amount");
        headerJumlah.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        TableColumn<String[], String> colJumlah = new TableColumn<>();
        colJumlah.setGraphic(headerJumlah);
        colJumlah.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));

        colJumlah.setCellFactory(column -> new TableCell<String[], String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    setTextFill(item.contains("+") ? Color.GREEN : Color.RED);
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });

        tableReport.getColumns().addAll(colTanggal, colWallet, colJumlah);

        //summary area (total transaksi masuk & keluar)
        VBox summaryBox = new VBox(5);
        summaryBox.setPadding(new Insets(15));
        summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        lblTotalPemasukan = new Label("Total Pemasukan: Rp 0.00");
        lblTotalPemasukan.setTextFill(Color.GREEN);
        lblTotalPemasukan.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        lblTotalPengeluaran = new Label("Total Pengeluaran: Rp 0.00");
        lblTotalPengeluaran.setTextFill(Color.RED);
        lblTotalPengeluaran.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        summaryBox.getChildren().addAll(lblTotalPemasukan, lblTotalPengeluaran);

        loadTransactionData();

        container.getChildren().addAll(lblTitle, filterBox, tableReport, summaryBox);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: #D5F4F9; -fx-padding: 0;");
        return scrollPane;
    }

    //method dropdown daftar jenis e-money 
    private void loadUserWallets() {
        String sql = "SELECT DISTINCT je.nama_layanan FROM user_emoney ue JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney WHERE ue.id_user = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbEmoney.getItems().add(rs.getString("nama_layanan"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    //method filter data dari db log_transaksi
    private void loadTransactionData() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        double totalMasuk = 0;
        double totalKeluar = 0;

        StringBuilder sql = new StringBuilder(
            "SELECT log.waktu_transaksi, je.nama_layanan, log.jumlah, log.tipe_transaksi " +
            "FROM log_transaksi log " +
            "JOIN user_emoney ue ON log.id_user_emoney = ue.id_user_emoney " +
            "JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney " +
            "WHERE ue.id_user = ?"
        );

        String selectedBulan = cbBulan.getValue();
        String selectedWallet = cbEmoney.getValue();

        //filter jika user memilih bulan tertentu
        if (selectedBulan != null && !selectedBulan.equals("All Months")) {
            sql.append(" AND MONTH(log.waktu_transaksi) = ").append(selectedBulan);
        }

        //filter jika user memilih wallet tertentu
        if (selectedWallet != null && !selectedWallet.equals("All Wallet")) {
            sql.append(" AND je.nama_layanan = ?");
        }

        sql.append(" ORDER BY log.waktu_transaksi DESC");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, currentUser.getIdUser());

            //set parameter PreparedStatement jika filter wallet aktif
            if (selectedWallet != null && !selectedWallet.equals("All Wallet")) {
                ps.setString(2, selectedWallet);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tanggalRaw = rs.getString("waktu_transaksi");
                String tanggal = (tanggalRaw != null && tanggalRaw.length() >= 10) ? tanggalRaw.substring(0, 10) : tanggalRaw; 
                
                String wallet = rs.getString("nama_layanan");
                double jumlah = rs.getDouble("jumlah");
                String tipe = rs.getString("tipe_transaksi");

                if (tipe.equals("MASUK")) {
                    totalMasuk += jumlah;
                    data.add(new String[]{tanggal, wallet, "+ Rp " + String.format("%,.0f", jumlah)});
                } else {
                    totalKeluar += jumlah;
                    data.add(new String[]{tanggal, wallet, "- Rp " + String.format("%,.0f", jumlah)});
                }
            }
            
            //update UI tabel n label total
            tableReport.setItems(data);
            lblTotalPemasukan.setText("Total Income   : Rp " + String.format("%,.2f", totalMasuk));
            lblTotalPengeluaran.setText("Total Expense : Rp " + String.format("%,.2f", totalKeluar));

        } catch (Exception ex) { ex.printStackTrace(); }
    }
}