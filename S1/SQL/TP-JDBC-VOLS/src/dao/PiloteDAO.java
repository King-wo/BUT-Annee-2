package dao;

import java.sql.*;
import java.util.*;
import model.Pilote;

public class PiloteDAO {
    private final Connection cnx;
    public PiloteDAO(Connection cnx) { this.cnx = cnx; }

    public boolean create(Pilote p) throws SQLException {
        String sql = "INSERT INTO pilote(nom, prenom, experience) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setInt(3, p.getExperience());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Pilote> readAll() throws SQLException {
        String sql = "SELECT * FROM pilote";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<Pilote> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Pilote(
                    rs.getInt("id_pilote"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getInt("experience")
                ));
            }
            return list;
        }
    }

    public boolean update(Pilote p) throws SQLException {
        String sql = "UPDATE pilote SET nom=?, prenom=?, experience=? WHERE id_pilote=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setInt(3, p.getExperience());
            ps.setInt(4, p.getIdPilote());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement chk = cnx.prepareStatement("SELECT COUNT(*) FROM vol WHERE id_pilote=?")) {
            chk.setInt(1, id);
            try (ResultSet rs = chk.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    System.out.println("Suppression refusée : des vols utilisent encore le pilote " + id + ".");
                    return false;
                }
            }
        }
        try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM pilote WHERE id_pilote=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
