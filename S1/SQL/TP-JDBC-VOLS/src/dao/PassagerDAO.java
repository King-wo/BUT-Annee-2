package dao;

import java.sql.*;
import java.util.*;
import model.Passager;

public class PassagerDAO {
    private final Connection cnx;
    public PassagerDAO(Connection cnx) { this.cnx = cnx; }

    public boolean create(Passager p) throws SQLException {
        String sql = "INSERT INTO passager(nom, prenom, nationalite) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getNationalite());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Passager> readAll() throws SQLException {
        String sql = "SELECT * FROM passager";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            List<Passager> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Passager(
                    rs.getInt("id_passager"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("nationalite")
                ));
            }
            return list;
        }
    }

    public boolean update(Passager p) throws SQLException {
        String sql = "UPDATE passager SET nom=?, prenom=?, nationalite=? WHERE id_passager=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getNationalite());
            ps.setInt(4, p.getIdPassager());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement chk = cnx.prepareStatement("SELECT COUNT(*) FROM reservation WHERE id_passager=?")) {
            chk.setInt(1, id);
            try (ResultSet rs = chk.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    System.out.println("Suppression refusée : des réservations utilisent encore le passager " + id + ".");
                    return false;
                }
            }
        }
        try (PreparedStatement ps = cnx.prepareStatement("DELETE FROM passager WHERE id_passager=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}
