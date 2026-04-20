
package proyekpisau;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Application {
    private User currentUser;
    private final String DB_URL = "jdbc:mysql://localhost:3306/sistem_pisau";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    
    private BorderPane root;
    private Label lblTotal;
    private VBox pnlListEmoney;
    private TableView<String[]> tableTransaksi;

    public Dashboard(User user) {
        this.currentUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        loadList();

        root = new BorderPane();
        root.setStyle("-fx-background-color: #D5F4F9;");

        //set default view (home)
        root.setCenter(getHomeView());

        //navbar menu
        HBox navbar = new HBox();
        navbar.setPrefHeight(40);
        navbar.setStyle(
            "-fx-background-color: white; " + 
            "-fx-border-color: #ddd; " + 
            "-fx-border-width: 1 0 0 0; "
        );

        Button btnHome = new Button("Home");
        Button btnReport = new Button("Transaction Report");
        
        String navBtnStyle = 
            "-fx-background-color: transparent; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #555; " +
            "-fx-cursor: hand; " +
            "-fx-font-size: 14; ";

        btnHome.setStyle(navBtnStyle);
        btnReport.setStyle(navBtnStyle);
        
        btnHome.setMaxWidth(Double.MAX_VALUE); 
        btnReport.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnHome, Priority.ALWAYS); 
        HBox.setHgrow(btnReport, Priority.ALWAYS);

        //routing navbar
        btnHome.setOnAction(e -> {
            loadList();
            root.setCenter(getHomeView()); 
        });

        btnReport.setOnAction(e -> {
            ReportPage reportPage = new ReportPage(currentUser);
            root.setCenter(reportPage.getView()); 
        });

        navbar.getChildren().addAll(btnHome, btnReport);
        root.setBottom(navbar);

        primaryStage.setTitle("PISAU Dashboard");
        primaryStage.setScene(new Scene(root, 400, 550)); 
        primaryStage.show();
    }

    private Node getHomeView() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        Label lblWelcome = new Label("Welcome, " + currentUser.getNamaLengkap());
        lblWelcome.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTotal = new Label("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        lblTotal.setTextFill(Color.web("#0066CC"));
        
        Label lblTotalBalanceTitle = new Label("Total Balance:");
        lblTotalBalanceTitle.setStyle("-fx-text-fill: #333333;");
        header.getChildren().addAll(lblWelcome, lblTotalBalanceTitle, lblTotal);

        pnlListEmoney = new VBox(8);
        pnlListEmoney.setPadding(new Insets(15));
        pnlListEmoney.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        updateWalletDisplay();

        HBox methodBtns = new HBox(10);
        methodBtns.setAlignment(Pos.CENTER);
        Button btnAddMethod = new Button("Add Method");
        btnAddMethod.setStyle("-fx-background-radius: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-background-color: #28a745;");
        Button btnDelMethod = new Button("Delete Method");
        btnDelMethod.setStyle("-fx-background-radius: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-background-color: #dc3545;");

        btnAddMethod.setMaxWidth(Double.MAX_VALUE);
        btnDelMethod.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAddMethod, Priority.ALWAYS);
        HBox.setHgrow(btnDelMethod, Priority.ALWAYS);

        btnAddMethod.setOnAction(e -> addMethod());
        btnDelMethod.setOnAction(e -> deleteMethod());
        methodBtns.getChildren().addAll(btnAddMethod, btnDelMethod);

        tableTransaksi = new TableView<>();
        tableTransaksi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTransaksi.setPrefHeight(100);

        Label headerWallet = new Label("Wallet");
        headerWallet.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        TableColumn<String[], String> colWallet = new TableColumn<>();
        colWallet.setGraphic(headerWallet);
        colWallet.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));

        Label headerJumlah = new Label("Amount");
        headerJumlah.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        TableColumn<String[], String> colJumlah = new TableColumn<>();
        colJumlah.setGraphic(headerJumlah);
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

        Label lblWallets = new Label("Your Wallets:");
        lblWallets.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");

        Label lblRecent = new Label("Recent Transactions:");
        lblRecent.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");

        VBox centerContent = new VBox(15, lblWallets, pnlListEmoney, methodBtns, lblRecent, tableTransaksi);
        centerContent.setPadding(new Insets(10, 20, 20, 20));

        BorderPane homePane = new BorderPane();
        homePane.setTop(header);
        
        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        homePane.setCenter(scrollPane);
        
        return homePane;
    }

    private void addMethod(){
        List<String> emoneyLain = new ArrayList<>();
        String sql = "SELECT nama_layanan FROM jenis_emoney WHERE id_jenis_emoney NOT IN (SELECT id_jenis_emoney FROM user_emoney WHERE id_user = ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { emoneyLain.add(rs.getString("nama_layanan")); }
        } catch (Exception e) { e.printStackTrace(); }

        if (emoneyLain.isEmpty()) {
            showAlert("All available e-money types are already linked!");
            return;
        }

        ChoiceDialog<String> choiceDlg = new ChoiceDialog<>(emoneyLain.get(0), emoneyLain);
        choiceDlg.setTitle("Add Wallet");
        choiceDlg.setHeaderText("Link a new account");
        choiceDlg.showAndWait().ifPresent(jenisEmoney -> {
            TextInputDialog idDlg = new TextInputDialog();
            idDlg.setHeaderText("Enter Account Details for " + jenisEmoney);
            idDlg.setContentText("Account ID / Phone Number:");
            idDlg.showAndWait().ifPresent(noIdentitas -> {
                TextInputDialog pinDlg = new TextInputDialog();
                pinDlg.setHeaderText("Security Check");
                pinDlg.setContentText("Enter Pass/PIN:");
                pinDlg.showAndWait().ifPresent(passEmoney -> {
                    linkEmoneyAccount(jenisEmoney, noIdentitas, passEmoney);
                });
            });
        });
    }

    private void deleteMethod(){
        List<String> emoneySaatIni = new ArrayList<>();
        String sqlSelect = "SELECT je.nama_layanan FROM user_emoney ue JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney WHERE ue.id_user = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
            psSelect.setInt(1, currentUser.getIdUser());
            ResultSet rs = psSelect.executeQuery();
            while (rs.next()) { emoneySaatIni.add(rs.getString("nama_layanan")); }
        } catch (Exception e) { e.printStackTrace(); }

        if (emoneySaatIni.isEmpty()) {
            showAlert("You don't have any wallets!");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(emoneySaatIni.get(0), emoneySaatIni);
        dialog.setTitle("Delete Wallet");
        dialog.setHeaderText("Remove an E-Money account");
        dialog.setContentText("Choose wallet to disconnect:");
        dialog.showAndWait().ifPresent(jenisEmoney -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Are you sure?");
            confirm.setContentText("This will unlink " + jenisEmoney + " from your account.");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                deleteEmoneyAccount(jenisEmoney);
            }
        });
    }

    private void deleteEmoneyAccount(String jenisEmoney) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sqlUpdate = "UPDATE user_emoney ue JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney SET ue.id_user = NULL WHERE je.nama_layanan = ? AND ue.id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, jenisEmoney);
            ps.setInt(2, currentUser.getIdUser());
            if (ps.executeUpdate() > 0) {
                showAlert("Account " + jenisEmoney + " has been disconnected.");
                loadList();
                root.setCenter(getHomeView()); 
            }
        } catch (Exception e) { e.printStackTrace(); }
    }  

    private void linkEmoneyAccount(String jenisEmoney, String noIdentitas, String passEmoney) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE user_emoney ue JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney SET ue.id_user = ? WHERE je.nama_layanan = ? AND ue.nomor_identitas = ? AND ue.pass_emoney = ? AND ue.id_user IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ps.setString(2, jenisEmoney);
            ps.setString(3, noIdentitas);
            ps.setString(4, passEmoney);
            if (ps.executeUpdate() > 0) {
                showAlert("Success! Account linked.");
                loadList();
                root.setCenter(getHomeView()); 
            } else {
                showAlert("Error: Account not found or wrong credentials.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updateWalletDisplay() {
        pnlListEmoney.getChildren().clear();
        for (EMoney emoney : currentUser.getListEmoney()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT); 
            
            Label lblJenis = new Label(emoney.getJenisEmoney());
            lblJenis.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;"); 
            
            Region sp = new Region(); 
            HBox.setHgrow(sp, Priority.ALWAYS);
            
            Label lblSaldo = new Label("Rp " + String.format("%,.0f", (double)emoney.getSaldo()));
            lblSaldo.setStyle("-fx-text-fill: #333333; -fx-font-size: 14px;");
            
            row.getChildren().addAll(lblJenis, sp, lblSaldo);
            pnlListEmoney.getChildren().add(row);
        }
    }

    private void updateTransactionData() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT je.nama_layanan, log.jumlah, log.tipe_transaksi FROM log_transaksi log JOIN user_emoney ue ON log.id_user_emoney = ue.id_user_emoney JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney WHERE ue.id_user = ? ORDER BY log.waktu_transaksi DESC LIMIT 3";
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}