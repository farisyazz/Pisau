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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        root.setStyle("-fx-background-color: #F8FAFC;");

        VBox fullHeader = new VBox(20);
        fullHeader.setPadding(new Insets(20, 20, 0, 20));
        fullHeader.setStyle("-fx-background-color: transparent;");

        HBox topHeader = new HBox();
        topHeader.setAlignment(Pos.CENTER_LEFT);
        Label lblWelcome = new Label(" Welcome, " + currentUser.getNamaLengkap());
        lblWelcome.setTextFill(Color.web("#2c2c2c"));

        Region spHeader = new Region();
        HBox.setHgrow(spHeader, Priority.ALWAYS);

        topHeader.getChildren().addAll(lblWelcome, spHeader);

        VBox card = new VBox(5);
        card.setPadding(new Insets(15, 20, 15, 20)); 
        card.setStyle("-fx-background-color: #2D5295; " + 
                        "-fx-background-radius: 12; " + 
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label lblTotalTxt = new Label("Total Balance:");
        lblTotalTxt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblTotalTxt.setTextFill(Color.web("#D1D5DB"));

        lblTotal = new Label("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblTotal.setTextFill(Color.WHITE);
        
        card.getChildren().addAll( lblTotalTxt, lblTotal);
        fullHeader.getChildren().addAll(topHeader, card);

        pnlListEmoney = new VBox(12);
        pnlListEmoney.setStyle("-fx-background-color: transparent;");
        updateWalletDisplay();

        HBox methodBtns = new HBox(12);
        methodBtns.setAlignment(Pos.CENTER);
        Button btnAddMethod = new Button("Add Wallet");
        btnAddMethod.setStyle("-fx-background-radius: 10; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 10 0; -fx-background-color: #5C6BC0;");
        Button btnDelMethod = new Button("Delete Wallet");
        btnDelMethod.setStyle("-fx-background-radius: 10; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 10 0; -fx-background-color: #CD5C5C;");

        btnAddMethod.setMaxWidth(Double.MAX_VALUE);
        btnDelMethod.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAddMethod, Priority.ALWAYS);
        HBox.setHgrow(btnDelMethod, Priority.ALWAYS);

        btnAddMethod.setOnAction(e -> addMethod());
        btnDelMethod.setOnAction(e -> deleteMethod());

        methodBtns.getChildren().addAll(btnAddMethod, btnDelMethod);

        tableTransaksi = new TableView<>();
        tableTransaksi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTransaksi.widthProperty().addListener((obs, oldVal, newVal) -> {
            Pane headerTable = (Pane) tableTransaksi.lookup("TableHeaderRow");
            if (headerTable != null && headerTable.isVisible()) {
                headerTable.setMaxHeight(0);
                headerTable.setMinHeight(0);
                headerTable.setPrefHeight(0);
                headerTable.setVisible(false);
            }
        });
        tableTransaksi.setPrefHeight(100);
        tableTransaksi.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                           "-fx-border-color: transparent; -fx-table-cell-border-color: transparent;");

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
                    setTextFill(item.contains("+") ? Color.web("#10B981") : Color.web("#EF4444"));
                    setStyle("-fx-font-weight: bold; -fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        tableTransaksi.getColumns().addAll(colWallet, colJumlah);
        updateTransactionData();

        VBox centerContent = new VBox(18);
        centerContent.setPadding(new Insets(15, 20, 20, 20));
        Label lblWallets = new Label("Your Accounts");
        lblWallets.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151; -fx-font-size: 15;");
        Label lblHistory = new Label("Recent Activity");
        lblHistory.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151; -fx-font-size: 15;");
        centerContent.getChildren().addAll(lblWallets, pnlListEmoney, methodBtns, lblHistory, tableTransaksi);
        
        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");

        HBox navbar = new HBox();
        navbar.setPrefHeight(30);
        navbar.setStyle(
            "-fx-background-color: #f4f4f4; " + 
            "-fx-border-color: #ddd; " + 
            "-fx-border-width: 1 0 0 0; " +
            "-fx-background-insets: 0; " + 
            "-fx-padding: 0;"              
        );
        Button btnHome = new Button("Home");
        Button btnReport = new Button("Report");
        String navBtnStyle = 
            "-fx-background-color: transparent; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #555; " +
            "-fx-cursor: hand; " +
            "-fx-font-size: 14; " +
            "-fx-background-radius: 0; " + 
            "-fx-background-insets: 0;";

        btnHome.setStyle(navBtnStyle);
        btnReport.setStyle(navBtnStyle);
        btnHome.setOnAction(e -> {
            loadList();
            lblTotal.setText("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
            updateWalletDisplay();
            updateTransactionData();

            root.setTop(fullHeader);  
            root.setCenter(scrollPane);
        });
        btnReport.setOnAction(e -> {
            ReportPage reportPage = new ReportPage(currentUser);
            ScrollPane reportView = reportPage.getView(); 
            reportView.setFitToWidth(true);
            
            root.setTop(null);         
            root.setCenter(reportView);
        });
        btnHome.setMaxWidth(Double.MAX_VALUE); 
        btnReport.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnHome, Priority.ALWAYS); 
        HBox.setHgrow(btnReport, Priority.ALWAYS);
        navbar.getChildren().addAll(btnHome, btnReport);

        root.setTop(fullHeader);
        root.setCenter(scrollPane);
        root.setBottom(navbar);

        primaryStage.setTitle("PISAU");
        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.show();
    }

    //progress
    private void addMethod(){
        List<String> emoneyLain = new ArrayList<>();
    
        String sql = "SELECT nama_layanan FROM jenis_emoney " +
                    "WHERE id_jenis_emoney NOT IN (" +
                    "    SELECT id_jenis_emoney FROM user_emoney WHERE id_user = ?" +
                    ")";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                emoneyLain.add(rs.getString("nama_layanan"));
            }
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
            while (rs.next()) {
                emoneySaatIni.add(rs.getString("nama_layanan"));
            }
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
            String sqlUpdate = "UPDATE user_emoney ue " +
                            "JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney " +
                            "SET ue.id_user = NULL " +
                            "WHERE je.nama_layanan = ? AND ue.id_user = ?";
            
            PreparedStatement ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, jenisEmoney);
            ps.setInt(2, currentUser.getIdUser());

            if (ps.executeUpdate() > 0) {
                showAlert("Account " + jenisEmoney + " has been disconnected.");
                loadList();
                lblTotal.setText("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
                updateWalletDisplay();
                updateTransactionData();
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
                lblTotal.setText("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
                updateWalletDisplay();
                updateTransactionData();
            } else {
                showAlert("Error: Account not found.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updateWalletDisplay() {
        pnlListEmoney.getChildren().clear();
        pnlListEmoney.setSpacing(8); 
        pnlListEmoney.setStyle("-fx-background-color: transparent;");
        for (EMoney emoney : currentUser.getListEmoney()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 15, 10, 15));
            row.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

            String logoPath = "/res/" + emoney.getJenisEmoney() + ".png";
            Image logoImg;
            try {
                logoImg = new Image(getClass().getResourceAsStream(logoPath));
            } catch (Exception e) {
                // Placeholder if logo not found
                logoImg = new Image(getClass().getResourceAsStream("/res/logos/default_logo.png"));
            }
            ImageView logoView = new ImageView(logoImg);
            logoView.setFitWidth(30); 
            logoView.setPreserveRatio(true);
            Circle clip = new Circle(15, 15, 15);
            logoView.setClip(clip);
            //HEREEEEEE
            Label lblJenis = new Label(emoney.getJenisEmoney());
            lblJenis.setStyle("-fx-font-weight: bold; -fx-text-fill: #1F2937; -fx-font-size: 14;");
            Region sp = new Region(); 
            HBox.setHgrow(sp, Priority.ALWAYS);

            Label lblSaldo = new Label("Rp " + String.format("%,.0f", (double)emoney.getSaldo()));
            lblSaldo.setStyle("-fx-font-weight: bold; -fx-text-fill: #111827;");

            row.getChildren().addAll(logoView, lblJenis, sp, lblSaldo);
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
            tableTransaksi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableTransaksi.setFixedCellSize(28);
            tableTransaksi.setPrefHeight(100); 
            tableTransaksi.setMinHeight(100);
            tableTransaksi.setMaxHeight(100);
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