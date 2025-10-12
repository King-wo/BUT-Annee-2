package view;

import dao.ReservationDAO;
import model.Reservation;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

public class ReservationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField volField, passagerField, dateField;
    private JButton addBtn, deleteBtn;
    private Integer selectedId = null;
    private int lastVolSearched = -1;

    public ReservationPanel() {
        setLayout(new BorderLayout());

        String[] colonnes = {"ID", "ID Vol", "ID Passager", "Date"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Formulaire pour ajouter une réservation
        JPanel form = new JPanel();
        volField = new JTextField(5);
        passagerField = new JTextField(5);
        dateField = new JTextField(16); // format : 2025-09-01 10:00:00
        addBtn = new JButton("Ajouter");
        deleteBtn = new JButton("Supprimer");

        form.add(new JLabel("ID Vol:"));
        form.add(volField);
        form.add(new JLabel("ID Passager:"));
        form.add(passagerField);
        form.add(new JLabel("Date:"));
        form.add(dateField);
        form.add(addBtn);
        form.add(deleteBtn);

        add(form, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String idVol = volField.getText();
            String idPassager = passagerField.getText();
            String date = dateField.getText();
            if (!idVol.isEmpty() && !idPassager.isEmpty() && !date.isEmpty()) {
                try {
                    int vol = Integer.parseInt(idVol);
                    int passager = Integer.parseInt(idPassager);
                    Timestamp dateRes = Timestamp.valueOf(date);
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        ReservationDAO dao = new ReservationDAO(conn);
                        Reservation r = new Reservation(null, passager, vol, dateRes);
                        dao.create(r);
                        loadReservationsForVol(vol);
                        volField.setText("");
                        passagerField.setText("");
                        dateField.setText("");
                        selectedId = null;
                        lastVolSearched = vol;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID invalide !");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Suppression
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette réservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        ReservationDAO dao = new ReservationDAO(conn);
                        dao.delete(id);
                        if (lastVolSearched != -1) {
                            loadReservationsForVol(lastVolSearched);
                        }
                        selectedId = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Formulaire pour afficher les réservations d'un vol
        JPanel searchPanel = new JPanel();
        JTextField searchVolField = new JTextField(5);
        JButton searchBtn = new JButton("Afficher réservations du vol");
        searchPanel.add(new JLabel("ID Vol:"));
        searchPanel.add(searchVolField);
        searchPanel.add(searchBtn);

        add(searchPanel, BorderLayout.NORTH);

        searchBtn.addActionListener(e -> {
            String idVol = searchVolField.getText();
            if (!idVol.isEmpty()) {
                try {
                    int vol = Integer.parseInt(idVol);
                    loadReservationsForVol(vol);
                    lastVolSearched = vol;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID Vol invalide !");
                }
            }
        });
    }

    private void loadReservationsForVol(int idVol) {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            ReservationDAO dao = new ReservationDAO(conn);
            List<Reservation> reservations = dao.getReservationsByVol(idVol);
            for (Reservation r : reservations) {
                model.addRow(new Object[]{r.getIdReservation(), r.getIdVol(), r.getIdPassager(), r.getDateReservation()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
