package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Avion;

public class AvionDAO {
    private final Connection cnx;
    public AvionDAO(Connection cnx) { this.cnx = cnx; }

    public boolean create(Avion a) throws SQLException {
        String sql = "INSERT INTO avion(modele, capacite) VALUES (?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, a.getModele());
            ps.setInt(2, a.getCapacite());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Avion> readAll() throws SQLException {
        String sql = "SELECT id_avion, modele, capacite FROM avion ORDER BY id_avion";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<Avion> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Avion(
                    rs.getInt("id_avion"),
                    rs.getString("modele"),
                    rs.getInt("capacite")
                ));
            }
            return list;
        }
    }

    public boolean update(Avion a) throws SQLException {
        String sql = "UPDATE avion SET modele=?, capacite=? WHERE id_avion=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, a.getModele());
            ps.setInt(2, a.getCapacite());
            ps.setInt(3, a.getIdAvion());
            return ps.executeUpdate() == 1;
        }
    }

    // Empêche de casser la FK si des vols référencent encore l'avion
    public boolean delete(int id) throws SQLException {
        try (PreparedStatement chk = cnx.prepareStatement("SELECT COUNT(*) FROM vol WHERE id_avion=?")) {
            chk.setInt(1, id);
            try (ResultSet rs = chk.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    System.out.println("Suppression refusée : des vols utilisent encore l’avion " + id + ".");
                    return false;
                }
            }
        }
        try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM avion WHERE id_avion=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
