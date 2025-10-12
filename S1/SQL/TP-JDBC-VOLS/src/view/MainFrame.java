package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Gestion de l'aéroport");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Vols", new VolPanel());
        tabs.addTab("Avions", new AvionPanel());
        tabs.addTab("Pilotes", new PilotePanel());
        tabs.addTab("Passagers", new PassagerPanel());
        tabs.addTab("Réservations", new ReservationPanel());

        add(tabs, BorderLayout.CENTER);
    }
}
