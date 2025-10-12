package view;

import dao.PiloteDAO;
import model.Pilote;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class PilotePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField nomField, prenomField, expField;
    private JButton addBtn, updateBtn, deleteBtn;
    private Integer selectedId = null;

    public PilotePanel() {
        setLayout(new BorderLayout());

        String[] colonnes = {"ID", "Nom", "Prénom", "Expérience"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);

        loadPilotes();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        nomField = new JTextField(10);
        prenomField = new JTextField(10);
        expField = new JTextField(5);
        addBtn = new JButton("Ajouter");
        updateBtn = new JButton("Modifier");
        deleteBtn = new JButton("Supprimer");

        form.add(new JLabel("Nom:"));
        form.add(nomField);
        form.add(new JLabel("Prénom:"));
        form.add(prenomField);
        form.add(new JLabel("Expérience:"));
        form.add(expField);
        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);

        add(form, BorderLayout.SOUTH);

        // Ajout
        addBtn.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String exp = expField.getText();
            if (!nom.isEmpty() && !prenom.isEmpty() && !exp.isEmpty()) {
                try {
                    int experience = Integer.parseInt(exp);
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PiloteDAO dao = new PiloteDAO(conn);
                        Pilote pilote = new Pilote(null, nom, prenom, experience);
                        dao.create(pilote);
                        loadPilotes();
                        nomField.setText("");
                        prenomField.setText("");
                        expField.setText("");
                        selectedId = null;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Expérience invalide !");
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
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce pilote ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PiloteDAO dao = new PiloteDAO(conn);
                        dao.delete(id);
                        loadPilotes();
                        nomField.setText("");
                        prenomField.setText("");
                        expField.setText("");
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
                nomField.setText((String) model.getValueAt(row, 1));
                prenomField.setText((String) model.getValueAt(row, 2));
                expField.setText(model.getValueAt(row, 3).toString());
            }
        });

        // Modification
        updateBtn.addActionListener(e -> {
            if (selectedId != null) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String exp = expField.getText();
                if (!nom.isEmpty() && !prenom.isEmpty() && !exp.isEmpty()) {
                    try {
                        int experience = Integer.parseInt(exp);
                        try (Connection conn = DatabaseConnection.getConnection()) {
                            PiloteDAO dao = new PiloteDAO(conn);
                            Pilote pilote = new Pilote(selectedId, nom, prenom, experience);
                            dao.update(pilote);
                            loadPilotes();
                            nomField.setText("");
                            prenomField.setText("");
                            expField.setText("");
                            selectedId = null;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Expérience invalide !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadPilotes() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PiloteDAO dao = new PiloteDAO(conn);
            List<Pilote> pilotes = dao.readAll();
            for (Pilote p : pilotes) {
                model.addRow(new Object[]{p.getIdPilote(), p.getNom(), p.getPrenom(), p.getExperience()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
