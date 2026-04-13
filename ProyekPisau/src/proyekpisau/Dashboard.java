package proyekpisau;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JFrame {
    private User currentUser;
    private JLabel lblTotal; 
    private JPanel pnlListEmoney;
    private DefaultTableModel modelTransaksi;
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
                    updateTransaction(modelTransaksi);
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblWelcome = new JLabel("Welcome " + currentUser.getNamaLengkap());
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblWelcome);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblTotalText = new JLabel("You Currently Have:");
        lblTotalText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTotalText);

        lblTotal = new JLabel("Rp " + String.format("%,.2f", currentUser.getTotalSaldo()));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotal.setForeground(new Color(0, 102, 204));
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTotal);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblEmoneyText = new JLabel("Your Wallets:");
        lblEmoneyText.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmoneyText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblEmoneyText);

        pnlListEmoney = new JPanel();
        for (EMoney emoney : currentUser.getListEmoney()) {
            JLabel lblEmoney = new JLabel(emoney.getJenisEmoney() + "\t: Rp " + String.format("%,.2f", (double)emoney.getSaldo()));
            pnlListEmoney.add(lblEmoney);
        }
        pnlListEmoney.setLayout(new BoxLayout(pnlListEmoney, BoxLayout.Y_AXIS));
        pnlListEmoney.setBackground(Color.white);
        pnlListEmoney.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlListEmoney.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlListEmoney.setMaximumSize(new Dimension(350, Integer.MAX_VALUE)); 
        panel.add(pnlListEmoney);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblTrasactionTxt = new JLabel("Recent Transactions:");
        lblTrasactionTxt.setFont(new Font("Arial", Font.BOLD, 14));
        lblTrasactionTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTrasactionTxt);

        String[] columnNames = {"Wallet", "Amount"};
        Object[][] data = {};
        modelTransaksi = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(modelTransaksi) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setBackground(bg);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String text = value.toString();
            if (text.startsWith("+")) label.setForeground(new Color(0, 153, 0)); 
            else if (text.startsWith("-")) label.setForeground(Color.RED); 
            return label;
        }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(bg);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(350, 100));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scrollPane);

        updateTransaction(modelTransaksi);
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

    public void updateTransaction(DefaultTableModel model){
        model.setRowCount(0);
        try {
            conn = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed To Connect!");
        }

        try{
            String sql = "SELECT je.nama_layanan, log.jumlah, log.tipe_transaksi " +
                     "FROM log_transaksi log " +
                     "JOIN user_emoney ue ON log.id_user_emoney = ue.id_user_emoney " +
                     "JOIN jenis_emoney je ON ue.id_jenis_emoney = je.id_jenis_emoney " +
                     "WHERE ue.id_user = ? ORDER BY log.waktu_transaksi DESC LIMIT 3";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUser.getIdUser());
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String jenis_emoney = rs.getString("nama_layanan");
                double jumlah = rs.getInt("jumlah");
                String tipe = rs.getString("tipe_transaksi");
                String displayJumlah = (tipe.equalsIgnoreCase("MASUK") ? "+ " : "- ") + 
                                   String.format("Rp %,.0f", jumlah);
                model.addRow(new Object[]{jenis_emoney, displayJumlah});
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }            
}


