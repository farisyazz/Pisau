package proyekpisau;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;

public class Authentication extends Application {
    private final String DB_URL = "jdbc:mysql://localhost:3306/sistem_pisau";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    private Connection conn;

    private StackPane root = new StackPane();
    private VBox loginView;
    private VBox registerView;

    @Override
    public void start(Stage primaryStage) {
        connectDatabase();

        loginView = loginView(primaryStage);
        registerView = registerView(primaryStage);

        root.getChildren().addAll(loginView, registerView);
        registerView.setVisible(false); 

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("PISAU");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            showAlert("Error", "Failed To Connect to Database!");
        }
    }

    private VBox loginView(Stage stage) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(40));
        vbox.setAlignment(Pos.CENTER);

        Image logo = new Image(getClass().getResourceAsStream("/res/knife.png"));
        ImageView imgLogo = new ImageView(logo);
        
        imgLogo.setFitWidth(90); 
        imgLogo.setPreserveRatio(true);

        Label lblHeader = new Label("Login");
        lblHeader.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        VBox userContainer = new VBox(5);
        userContainer.setAlignment(Pos.CENTER_LEFT); 
        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();
        txtUser.setPromptText("Username");
        userContainer.getChildren().addAll(lblUser, txtUser);

        VBox passContainer = new VBox(5);
        passContainer.setAlignment(Pos.CENTER_LEFT); 
        Label lblPass = new Label("Password:");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        passContainer.getChildren().addAll(lblPass, txtPass);

        Button btnLogin = new Button("Login");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: #0078D7; " +
                  "-fx-text-fill: white; " +
                  "-fx-background-radius: 25; " + 
                  "-fx-font-size: 14px; " +
                  "-fx-font-weight: bold; " +
                  "-fx-cursor: hand;");
        btnLogin.setOnAction(e -> handleLogin(txtUser.getText(), txtPass.getText(), stage));

        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Label lblNoAccount = new Label("Don't have an account?");
        Hyperlink linkRegister = new Hyperlink("Register here");
        
        linkRegister.setUnderline(true);
        linkRegister.setOnAction(e -> {
            loginView.setVisible(false);
            registerView.setVisible(true);
        });

        registerBox.getChildren().addAll(lblNoAccount, linkRegister);

        // Add everything to main vbox
        vbox.getChildren().addAll(imgLogo, lblHeader, userContainer, passContainer, btnLogin, registerBox);
        return vbox;
    }

    private VBox registerView(Stage stage) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);

        Image logo = new Image(getClass().getResourceAsStream("/res/knife.png"));
        ImageView imgLogo = new ImageView(logo);
        
        imgLogo.setFitWidth(90); 
        imgLogo.setPreserveRatio(true);

        Label lblHeader = new Label("Register");
        lblHeader.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        VBox namaContainer = new VBox(5);
        namaContainer.setAlignment(Pos.CENTER_LEFT); // Align label to the left
        Label lblNama = new Label("Full Name:");
        TextField txtNama = new TextField();
        txtNama.setPromptText("Full Name");
        namaContainer.getChildren().addAll(lblNama, txtNama);

        VBox emailContainer = new VBox(5);
        emailContainer.setAlignment(Pos.CENTER_LEFT); // Align label to the left
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        emailContainer.getChildren().addAll(lblEmail, txtEmail);

        VBox userContainer = new VBox(5);
        userContainer.setAlignment(Pos.CENTER_LEFT); // Align label to the left
        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();
        txtUser.setPromptText("Username");
        userContainer.getChildren().addAll(lblUser, txtUser);

        VBox passContainer = new VBox(5);
        passContainer.setAlignment(Pos.CENTER_LEFT); // Align label to the left
        Label lblPass = new Label("Password:");
        TextField txtPass = new TextField();
        txtPass.setPromptText("Password");
        passContainer.getChildren().addAll(lblPass, txtPass);

        
        Button btnReg = new Button("Register");
        btnReg.setMaxWidth(Double.MAX_VALUE);
        btnReg.setStyle("-fx-background-color: #0078D7; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 25; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand;");
        btnReg.setOnAction(e -> handleRegister(txtNama.getText(), txtEmail.getText(), txtUser.getText(), txtPass.getText(), stage));

        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label lblHasAccount = new Label("Already have an account?");
        Hyperlink linkLogin = new Hyperlink("Login here");
        linkLogin.setUnderline(true);
        linkLogin.setOnAction(e -> {
            registerView.setVisible(false);
            loginView.setVisible(true);
        });
        loginBox.getChildren().addAll(lblHasAccount, linkLogin);

        vbox.getChildren().addAll(imgLogo, lblHeader, namaContainer, emailContainer, userContainer, passContainer, btnReg, loginBox);

        return vbox;
    }

    private void handleLogin(String user, String pass, Stage stage) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User loggedUser = new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("nama_lengkap"));
                new Dashboard(loggedUser).start(new Stage());
                stage.close();
            } else {
                showAlert("Login Failed", "Account Not Found!");
            }
        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private void handleRegister(String nama, String email, String user, String pass, Stage stage) {
        try {
            if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                showAlert("Warning", "Fields cannot be empty!");
                return;
            }

            String insertSql = "INSERT INTO users (nama_lengkap, email, username, password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, user);
            ps.setString(4, pass);
            
            if (ps.executeUpdate() > 0) {
                ResultSet gk = ps.getGeneratedKeys();
                gk.next();
                User newUser = new User(gk.getInt(1), user, pass, email, nama);
                new Dashboard(newUser).start(new Stage());
                stage.close();
            }
        } catch (Exception ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

