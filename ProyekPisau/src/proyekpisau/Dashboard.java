package proyekpisau;
import java.awt.*;
import javax.swing.*;
// import java.awt.event.*;
// import java.awt.image.RescaleOp;
// import java.sql.*;

public class Dashboard extends JFrame {
    private User currentUser;

    public Dashboard(User user) {
        this.currentUser = user; 
        
        setTitle("Dashboard - " + currentUser.getNamaLengkap());
        setSize(400, 500);
        
        JLabel welcome = new JLabel("Welcome, " + currentUser.getNamaLengkap());
        welcome.setBounds(0, 30, 400, 40);
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcome);
    }
}
