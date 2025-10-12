package view;

import dao.AvionDAO;
import model.Avion;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AvionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField modeleField, capaciteField;
    private JButton addBtn, updateBtn, deleteBtn;
    private Integer selectedId = null;

    public AvionPanel() {
        setLayout(new BorderLayout());

        String[] colonnes = {"ID", "Modèle", "Capacité"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);

        loadAvions();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        modeleField = new JTextField(10);
        capaciteField = new JTextField(5);
        addBtn = new JButton("Ajouter");
        updateBtn = new JButton("Modifier");
        deleteBtn = new JButton("Supprimer");

        form.add(new JLabel("Modèle:"));
        form.add(modeleField);
        form.add(new JLabel("Capacité:"));
        form.add(capaciteField);
        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);

        add(form, BorderLayout.SOUTH);

        // Ajout
        addBtn.addActionListener(e -> {
            String modele = modeleField.getText();
            String capacite = capaciteField.getText();
            if (!modele.isEmpty() && !capacite.isEmpty()) {
                try {
                    int cap = Integer.parseInt(capacite);
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        AvionDAO dao = new AvionDAO(conn);
                        Avion avion = new Avion(modele, cap);
                        dao.create(avion);
                        loadAvions();
                        modeleField.setText("");
                        capaciteField.setText("");
                        selectedId = null;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Capacité invalide !");
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
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet avion ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        AvionDAO dao = new AvionDAO(conn);
                        dao.delete(id);
                        loadAvions();
                        modeleField.setText("");
                        capaciteField.setText("");
                        selectedId = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Pré-remplir le formulaire pour modification
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedId = (int) model.getValueAt(row, 0);
                modeleField.setText((String) model.getValueAt(row, 1));
                capaciteField.setText(model.getValueAt(row, 2).toString());
            }
        });

        // Modification
        updateBtn.addActionListener(e -> {
            if (selectedId != null) {
                String modele = modeleField.getText();
                String capacite = capaciteField.getText();
                if (!modele.isEmpty() && !capacite.isEmpty()) {
                    try {
                        int cap = Integer.parseInt(capacite);
                        try (Connection conn = DatabaseConnection.getConnection()) {
                            AvionDAO dao = new AvionDAO(conn);
                            Avion avion = new Avion(selectedId, modele, cap);
                            dao.update(avion);
                            loadAvions();
                            modeleField.setText("");
                            capaciteField.setText("");
                            selectedId = null;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Capacité invalide !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadAvions() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            AvionDAO dao = new AvionDAO(conn);
            List<Avion> avions = dao.readAll();
            for (Avion a : avions) {
                model.addRow(new Object[]{a.getIdAvion(), a.getModele(), a.getCapacite()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
