package proyekpisau;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Authentication extends JFrame {
    private final String DB_URL = "jdbc:mysql://localhost:3306/sistem_pisau";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    Connection conn;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public Authentication(){
        setTitle("Sistem Manajeman Pembayaran PISAU");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(loginPanel(), "Login");
        mainPanel.add(registerPanel(), "Register");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel loginPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnToReg = new JButton("Go to Register Instead");

        JLabel lblHeader = new JLabel("Login", JLabel.CENTER);
        JLabel lblUser = new JLabel("Username: ");
        JLabel lblPass = new JLabel("Password: ");

        lblHeader.setBounds(0, 25, 400, 40);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblHeader);
        lblUser.setBounds(50, 100, 100, 25);
        panel.add(lblUser);
        txtUser.setBounds(150, 100, 180, 25);
        panel.add(txtUser);
        lblPass.setBounds(50, 150, 100, 25);
        panel.add(lblPass);
        txtPass.setBounds(150, 150, 180, 25);
        panel.add(txtPass);
        btnLogin.setBounds(100, 220, 200, 35);
        panel.add(btnLogin);
        btnToReg.setBounds(100, 275, 200, 35);
        panel.add(btnToReg);

        //db
        try {
            conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed To Connect!");
        }

        //action
        btnToReg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    cardLayout.show(mainPanel, "Register");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = txtUser.getText();
                    String password = new String(txtPass.getPassword());

                    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        Authentication.this.dispose();
                        User loggedUser = new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("password"), rs.getString("email"),  rs.getString("nama_lengkap"));
                        new Dashboard(loggedUser).setVisible(true);
                    } else{
                        JOptionPane.showMessageDialog(Authentication.this, "Account Not Found!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        return panel;
    }

    private JPanel registerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JTextField txtNama = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnReg = new JButton("Register");
        JButton btnToLogin = new JButton("Go to Login Instead");

        JLabel lblHeader = new JLabel("Register", JLabel.CENTER);
        JLabel lblNama = new JLabel("Full Name: ");
        JLabel lblEmail = new JLabel("Email: ");
        JLabel lblUser = new JLabel("Username: ");
        JLabel lblPass = new JLabel("Password: ");

        lblHeader.setBounds(0, 30, 400, 40);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblHeader);
        lblNama.setBounds(50, 100, 100, 25);
        panel.add(lblNama);
        txtNama.setBounds(150, 100, 180, 25);
        panel.add(txtNama);
        lblEmail.setBounds(50, 140, 100, 25);
        panel.add(lblEmail);
        txtEmail.setBounds(150, 140, 180, 25);
        panel.add(txtEmail);
        lblUser.setBounds(50, 180, 100, 25);
        panel.add(lblUser);
        txtUser.setBounds(150, 180, 180, 25);
        panel.add(txtUser);
        lblPass.setBounds(50, 220, 100, 25);
        panel.add(lblPass);
        txtPass.setBounds(150, 220, 180, 25);
        panel.add(txtPass);
        btnReg.setBounds(100, 280, 200, 35);
        panel.add(btnReg);
        btnToLogin.setBounds(100, 330, 200, 35);
        panel.add(btnToLogin);

        //db
        try {
            conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed To Connect!");
        }

        //action
        btnToLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    cardLayout.show(mainPanel, "Login");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnReg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String namaLengkap = txtNama.getText();
                    String email = txtEmail.getText();
                    String username = txtUser.getText();
                    String password = new String(txtPass.getPassword());

                    if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                        JOptionPane.showMessageDialog(Authentication.this, "Fields cannot be empty!");
                        return;
                    }

                    String checkSql = "SELECT * FROM users WHERE username = ?";
                    PreparedStatement checkPs = conn.prepareStatement(checkSql);
                    checkPs.setString(1, username);
                    ResultSet checkRs = checkPs.executeQuery();
                    
                    if(checkRs.next()){
                        JOptionPane.showMessageDialog(Authentication.this, "Username already taken.");
                    }else{
                        String insertSql = "INSERT INTO users (nama_lengkap, email, username, password) VALUES (?, ?, ?, ?)";
                            PreparedStatement ps = conn.prepareStatement(insertSql);
                            ps.setString(1, namaLengkap);
                            ps.setString(2, email);
                            ps.setString(3, username);
                            ps.setString(4, password);
                            int rowsAffected = ps.executeUpdate();
                        if(rowsAffected > 0){
                            ResultSet generatedKeys = ps.getGeneratedKeys();
                                int newIdUser = generatedKeys.getInt(1);
                            JOptionPane.showMessageDialog(Authentication.this, "Registration Successful!");
                            Authentication.this.dispose();
                            User newUser = new User(newIdUser, username, password, email, namaLengkap);
                            new Dashboard(newUser).setVisible(true);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        // btnReg.addActionListener(e -> {
        //     try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        //         // Using INSERT pattern [cite: 7]
        //         String sql = "INSERT INTO users (username, password, fullname, email) VALUES (?, ?, ?, ?)";
        //         PreparedStatement ps = conn.prepareStatement(sql);
        //         ps.setString(1, txtUser.getText());
        //         ps.setString(2, new String(txtPass.getPassword()));
        //         ps.setString(3, txtNama.getText());
        //         ps.setString(4, txtEmail.getText());

        //         if (ps.executeUpdate() > 0) { // Check if successful [cite: 18, 22]
        //             JOptionPane.showMessageDialog(this, "Registered!");
        //             this.dispose();
        //             new Dashboard(txtNama.getText()).setVisible(true);
        //         }
        //     } catch (SQLException ex) {
        //         JOptionPane.showMessageDialog(this, "Registration failed!");
        //     }
        // });
        return panel;
    }
}
