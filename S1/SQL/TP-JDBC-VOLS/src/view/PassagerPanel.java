package view;

import dao.PassagerDAO;
import model.Passager;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class PassagerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField nomField, prenomField, natField;
    private JButton addBtn, updateBtn, deleteBtn;
    private Integer selectedId = null;

    public PassagerPanel() {
        setLayout(new BorderLayout());

        String[] colonnes = {"ID", "Nom", "Prénom", "Nationalité"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);

        loadPassagers();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        nomField = new JTextField(10);
        prenomField = new JTextField(10);
        natField = new JTextField(10);
        addBtn = new JButton("Ajouter");
        updateBtn = new JButton("Modifier");
        deleteBtn = new JButton("Supprimer");

        form.add(new JLabel("Nom:"));
        form.add(nomField);
        form.add(new JLabel("Prénom:"));
        form.add(prenomField);
        form.add(new JLabel("Nationalité:"));
        form.add(natField);
        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);

        add(form, BorderLayout.SOUTH);

        // Ajout
        addBtn.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String nat = natField.getText();
            if (!nom.isEmpty() && !prenom.isEmpty() && !nat.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    PassagerDAO dao = new PassagerDAO(conn);
                    Passager passager = new Passager(null, nom, prenom, nat);
                    dao.create(passager);
                    loadPassagers();
                    nomField.setText("");
                    prenomField.setText("");
                    natField.setText("");
                    selectedId = null;
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
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce passager ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PassagerDAO dao = new PassagerDAO(conn);
                        dao.delete(id);
                        loadPassagers();
                        nomField.setText("");
                        prenomField.setText("");
                        natField.setText("");
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
                natField.setText((String) model.getValueAt(row, 3));
            }
        });

        // Modification
        updateBtn.addActionListener(e -> {
            if (selectedId != null) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String nat = natField.getText();
                if (!nom.isEmpty() && !prenom.isEmpty() && !nat.isEmpty()) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        PassagerDAO dao = new PassagerDAO(conn);
                        Passager passager = new Passager(selectedId, nom, prenom, nat);
                        dao.update(passager);
                        loadPassagers();
                        nomField.setText("");
                        prenomField.setText("");
                        natField.setText("");
                        selectedId = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadPassagers() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            PassagerDAO dao = new PassagerDAO(conn);
            List<Passager> passagers = dao.readAll();
            for (Passager p : passagers) {
                model.addRow(new Object[]{p.getIdPassager(), p.getNom(), p.getPrenom(), p.getNationalite()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
