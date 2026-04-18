package proyekpisau;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;

public class Dashboard extends Application {
    private User currentUser;
    private final String DB_URL = "jdbc:mysql://localhost:3306/sistem_pisau";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    
    private Label lblTotal;
    private VBox pnlListEmoney;
    private TableView<String[]> tableTransaksi;

    public Dashboard(User user) {
        this.currentUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        loadList();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #D5F4F9;");

        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        Label lblWelcome = new Label("Welcome, " + currentUser.getNamaLengkap());
        lblWelcome.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTotal = new Label("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblTotal.setTextFill(Color.web("#0066CC"));
        header.getChildren().addAll(lblWelcome, new Label("Total Balance:"), lblTotal);

        pnlListEmoney = new VBox(8);
        pnlListEmoney.setPadding(new Insets(15));
        pnlListEmoney.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        updateWalletDisplay();

        tableTransaksi = new TableView<>();
        tableTransaksi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTransaksi.setPrefHeight(100);

        TableColumn<String[], String> colWallet = new TableColumn<>("Wallet");
        colWallet.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        TableColumn<String[], String> colJumlah = new TableColumn<>("Amount");
        colJumlah.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));

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

        tableTransaksi.getColumns().addAll(colWallet, colJumlah);
        updateTransactionData();

        VBox centerContent = new VBox(15, new Label("Your Wallets:"), pnlListEmoney, new Label("Recent Transactions:"), tableTransaksi);
        centerContent.setPadding(new Insets(10, 20, 20, 20));

        HBox navbar = new HBox();
        navbar.setPrefHeight(50);
        Button btnHome = new Button("Home");
        Button btnProfile = new Button("Profile");
        btnHome.setOnAction(e -> {
            loadList();
            lblTotal.setText("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
            updateWalletDisplay();
            updateTransactionData();
        });
        btnHome.setMaxWidth(Double.MAX_VALUE); 
        btnProfile.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnHome, Priority.ALWAYS); 
        HBox.setHgrow(btnProfile, Priority.ALWAYS);
        navbar.getChildren().addAll(btnHome, btnProfile);

        root.setTop(header);
        root.setCenter(centerContent);
        root.setBottom(navbar);

        primaryStage.setTitle("PISAU Dashboard");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }

    private void updateWalletDisplay() {
        pnlListEmoney.getChildren().clear();
        for (EMoney emoney : currentUser.getListEmoney()) {
            HBox row = new HBox();
            Label lblJenis = new Label(emoney.getJenisEmoney());
            Region sp = new Region(); 
            HBox.setHgrow(sp, Priority.ALWAYS);
            Label lblSaldo = new Label("Rp " + String.format("%,.0f", (double)emoney.getSaldo()));
            row.getChildren().addAll(lblJenis, sp, lblSaldo);
            pnlListEmoney.getChildren().add(row);
        }
    }

    private void updateTransactionData() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT je.nama_layanan, log.jumlah, log.tipe_transaksi FROM log_transaksi log " +
                         "JOIN user_emoney ue ON log.id_user_emoney = ue.id_user_emoney " +
                         "JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney " +
                         "WHERE ue.id_user = ? ORDER BY log.waktu_transaksi DESC LIMIT 3";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String prefix = rs.getString("tipe_transaksi").equals("MASUK") ? "+ " : "- ";
                data.add(new String[]{rs.getString("nama_layanan"), prefix + "Rp " + String.format("%,.0f", rs.getDouble("jumlah"))});
            }
            tableTransaksi.setItems(data);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void loadList() {
        currentUser.getListEmoney().clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM user_emoney WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_user_emoney");
                int jenisEmoney = rs.getInt("id_jenis_emoney");
                String noIdentitas = rs.getString("nomor_identitas");
                int saldo = rs.getInt("saldo");
                if (jenisEmoney == 1) currentUser.tambahMetode(new Mandiri(id, noIdentitas, saldo));
                else if (jenisEmoney == 2) currentUser.tambahMetode(new BCA(id, noIdentitas, saldo));
                else if (jenisEmoney == 3) currentUser.tambahMetode(new Gopay(id, noIdentitas, saldo));
                else if (jenisEmoney == 4) currentUser.tambahMetode(new Dana(id, noIdentitas, saldo));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
