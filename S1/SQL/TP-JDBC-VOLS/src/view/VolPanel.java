package view;

import dao.VolDAO;
import model.Vol;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class VolPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField statutField;
    private JButton updateBtn, deleteBtn;
    private Integer selectedId = null;

    public VolPanel() {
        setLayout(new BorderLayout());

        String[] colonnes = {"ID", "Numéro", "Ville départ", "Ville arrivée", "Statut", "Pilote", "Avion"};
        model = new DefaultTableModel(colonnes, 0);
        table = new JTable(model);

        loadVols();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        JTextField numField = new JTextField(6);
        JTextField villeDepField = new JTextField(8);
        JTextField villeArrField = new JTextField(8);
        JTextField dateDepField = new JTextField(16); // format : 2025-09-01 08:00:00
        JTextField dateArrField = new JTextField(16);
        statutField = new JTextField(8);
        JTextField idPiloteField = new JTextField(4);
        JTextField idAvionField = new JTextField(4);
        JButton addBtn = new JButton("Ajouter");
        updateBtn = new JButton("Modifier statut");
        deleteBtn = new JButton("Supprimer");

        form.add(new JLabel("Numéro:"));
        form.add(numField);
        form.add(new JLabel("Ville départ:"));
        form.add(villeDepField);
        form.add(new JLabel("Ville arrivée:"));
        form.add(villeArrField);
        form.add(new JLabel("Date départ:"));
        form.add(dateDepField);
        form.add(new JLabel("Date arrivée:"));
        form.add(dateArrField);
        form.add(new JLabel("Statut:"));
        form.add(statutField);
        form.add(new JLabel("ID Pilote:"));
        form.add(idPiloteField);
        form.add(new JLabel("ID Avion:"));
        form.add(idAvionField);
        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);

        add(form, BorderLayout.SOUTH);

        // Ajout
        addBtn.addActionListener(e -> {
            String num = numField.getText();
            String villeDep = villeDepField.getText();
            String villeArr = villeArrField.getText();
            String dateDep = dateDepField.getText();
            String dateArr = dateArrField.getText();
            String statut = statutField.getText();
            String idPilote = idPiloteField.getText();
            String idAvion = idAvionField.getText();
            if (!num.isEmpty() && !villeDep.isEmpty() && !villeArr.isEmpty() && !dateDep.isEmpty() && !dateArr.isEmpty() && !statut.isEmpty() && !idPilote.isEmpty() && !idAvion.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    VolDAO dao = new VolDAO(conn);
                    Vol v = new Vol(
                        null, num, villeDep, villeArr,
                        Timestamp.valueOf(dateDep), Timestamp.valueOf(dateArr),
                        statut,
                        Integer.parseInt(idPilote),
                        Integer.parseInt(idAvion)
                    );
                    dao.create(v);
                    loadVols();
                    numField.setText("");
                    villeDepField.setText("");
                    villeArrField.setText("");
                    dateDepField.setText("");
                    dateArrField.setText("");
                    statutField.setText("");
                    idPiloteField.setText("");
                    idAvionField.setText("");
                    selectedId = null;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du vol : " + ex.getMessage());
                }
            }
        });

        // Suppression
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce vol ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        VolDAO dao = new VolDAO(conn);
                        dao.delete(id);
                        loadVols();
                        selectedId = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Pré-remplir le champ statut pour modification
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedId = (int) model.getValueAt(row, 0);
                statutField.setText((String) model.getValueAt(row, 4));
            }
        });

        // Modification du statut
        updateBtn.addActionListener(e -> {
            if (selectedId != null) {
                String statut = statutField.getText();
                if (!statut.isEmpty()) {
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        VolDAO dao = new VolDAO(conn);
                        dao.updateStatut(selectedId, statut);
                        loadVols();
                        statutField.setText("");
                        selectedId = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadVols() {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            VolDAO dao = new VolDAO(conn);
            List<Map<String, Object>> vols = dao.readAllDetails();
            for (Map<String, Object> v : vols) {
                model.addRow(new Object[]{
                    v.get("id_vol"),
                    v.get("numero_vol"),
                    v.get("ville_depart"),
                    v.get("ville_arrivee"),
                    v.get("statut"),
                    v.get("nom_pilote"),
                    v.get("modele_avion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
