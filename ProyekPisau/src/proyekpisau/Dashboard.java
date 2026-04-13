package proyekpisau;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Dashboard extends JFrame {
    private User currentUser;
    private JLabel lblTotal; 
    private JPanel pnlListEmoney;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private final String DB_URL = "jdbc:mysql://localhost:3306/sistem_pisau";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";
    Color bg = new Color(213,244,249);
    Connection conn;

    public Dashboard(User user) {
        this.currentUser = user; 
        
        setTitle("Sistem Manajeman Pembayaran PISAU");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        loadList();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(homePanel(), "Home");
        mainPanel.add(new JPanel(), "Profile"); 

        JPanel navbar = new JPanel(new GridLayout(1, 2));
        JButton btnHome = new JButton("Home");
        JButton btnProfile = new JButton("Profile");

        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    updateSaldo();
                    updateDashboard();
                    cardLayout.show(mainPanel, "Home");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        navbar.add(btnHome);
        navbar.add(btnProfile);

        add(mainPanel, BorderLayout.CENTER);
        add(navbar, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel homePanel(){
        JPanel panel = new JPanel(null);
        panel.setBackground(bg);

        JLabel lblWelcome = new JLabel("Welcome " + currentUser.getNamaLengkap());
        lblWelcome.setBounds(20, 20, 300, 30);
        panel.add(lblWelcome);

        JLabel lblTotalText = new JLabel("You Currently Have:");
        lblTotalText.setBounds(20, 60, 200, 20);
        panel.add(lblTotalText);

        lblTotal = new JLabel("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotal.setForeground(new Color(0, 102, 204));
        lblTotal.setBounds(20, 80, 300, 40);
        panel.add(lblTotal);

        JLabel lblEmoneyText = new JLabel("Your Wallets:");
        lblEmoneyText.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmoneyText.setBounds(20, 140, 200, 25);
        panel.add(lblEmoneyText);

        pnlListEmoney = new JPanel();
        for (EMoney emoney : currentUser.getListEmoney()) {
            JLabel lblEmoney = new JLabel(emoney.getJenisEmoney() + "\t: Rp " + String.format("%,.2f", (double)emoney.getSaldo()));
            pnlListEmoney.add(lblEmoney);
        }
        pnlListEmoney.setLayout(new BoxLayout(pnlListEmoney, BoxLayout.Y_AXIS));
        pnlListEmoney.setBackground(Color.white);
        pnlListEmoney.setBounds(20, 170, 340, 200);
        panel.add(pnlListEmoney);
        updateDashboard();

        return panel;
    }

    private void updateDashboard() {
        lblTotal.setText("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
        pnlListEmoney.removeAll(); 
        for (EMoney emoney : currentUser.getListEmoney()) {
            JLabel lblEmoney = new JLabel(emoney.getJenisEmoney() + "\t: Rp " + String.format("%,.2f", (double)emoney.getSaldo()));
            pnlListEmoney.add(lblEmoney);
        }
        pnlListEmoney.revalidate();
        pnlListEmoney.repaint();
    }

    private void loadList(){
        currentUser.getListEmoney().clear();
        try {
            conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed To Connect!");
        }

        try{
            String sql = "SELECT * FROM user_emoney WHERE id_user = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int idUserEmoney = rs.getInt("id_user_emoney");
                int idJenisEmoney = rs.getInt("id_jenis_emoney");
                String nomorId = rs.getString("nomor_identitas");
                int updatedSaldo = rs.getInt("saldo");

                EMoney emoney;
                switch (idJenisEmoney) {
                    case 1 :
                        emoney = new Mandiri(idUserEmoney, nomorId, updatedSaldo);
                        break;
                    case 2 :
                        emoney = new BCA(idUserEmoney, nomorId, updatedSaldo);
                        break;
                    case 3 :
                        emoney = new Gopay(idUserEmoney, nomorId, updatedSaldo);
                        break;
                    case 4 :
                        emoney = new Dana(idUserEmoney, nomorId, updatedSaldo);
                        break;
                    default:
                        throw new AssertionError();
                }
                currentUser.tambahMetode(emoney);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void updateSaldo(){
        try {
            conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed To Connect!");
        }

        try{
            String sql = "SELECT id_user_emoney, saldo FROM user_emoney WHERE id_user = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idUserEmoney = rs.getInt("id_user_emoney");
                int saldo = rs.getInt("saldo");

                for (EMoney emoney : currentUser.getListEmoney()) {
                    if (emoney.getIdUserEmoney() == idUserEmoney) { 
                        emoney.setSaldo(saldo);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}

