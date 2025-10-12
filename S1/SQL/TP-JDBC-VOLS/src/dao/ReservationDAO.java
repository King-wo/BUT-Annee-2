package dao;

import java.sql.*;
import java.util.*;
import model.Passager;
import model.Reservation;
import model.Vol;

public class ReservationDAO {
	private final Connection cnx;
	public ReservationDAO(Connection cnx) { this.cnx = cnx; }

	// 1. Créer une réservation
	public boolean create(Reservation r) throws SQLException {
		String sql = "INSERT INTO reservation(id_passager, id_vol, date_reservation) VALUES (?, ?, ?)";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setInt(1, r.getIdPassager());
			ps.setInt(2, r.getIdVol());
			ps.setTimestamp(3, r.getDateReservation());
			return ps.executeUpdate() > 0;
		}
	}

	// 2. Lister tous les passagers d’un vol donné
	public List<Passager> getPassagersByVol(int idVol) throws SQLException {
		String sql = "SELECT p.id_passager, p.nom, p.prenom, p.nationalite FROM passager p JOIN reservation r ON p.id_passager = r.id_passager WHERE r.id_vol = ?";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setInt(1, idVol);
			try (ResultSet rs = ps.executeQuery()) {
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
	}

	// 3. Lister tous les vols réservés par un passager donné
	public List<Vol> getVolsByPassager(int idPassager) throws SQLException {
		String sql = "SELECT v.id_vol, v.numero_vol, v.ville_depart, v.ville_arrivee, v.date_depart, v.date_arrivee, v.statut, v.id_pilote, v.id_avion FROM vol v JOIN reservation r ON v.id_vol = r.id_vol WHERE r.id_passager = ?";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setInt(1, idPassager);
			try (ResultSet rs = ps.executeQuery()) {
				List<Vol> list = new ArrayList<>();
				while (rs.next()) {
					list.add(new Vol(
						rs.getInt("id_vol"),
						rs.getString("numero_vol"),
						rs.getString("ville_depart"),
						rs.getString("ville_arrivee"),
						rs.getTimestamp("date_depart"),
						rs.getTimestamp("date_arrivee"),
						rs.getString("statut"),
						rs.getInt("id_pilote"),
						rs.getInt("id_avion")
					));
				}
				return list;
			}
		}
	}

	public List<Reservation> getReservationsByVol(int idVol) throws SQLException {
    String sql = "SELECT * FROM reservation WHERE id_vol = ?";
    try (PreparedStatement ps = cnx.prepareStatement(sql)) {
        ps.setInt(1, idVol);
        try (ResultSet rs = ps.executeQuery()) {
            List<Reservation> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Reservation(
                    rs.getInt("id_reservation"),
                    rs.getInt("id_passager"),
                    rs.getInt("id_vol"),
                    rs.getTimestamp("date_reservation")
                ));
            }
            return list;
        }
    }
}

	// 4. Supprimer une réservation
	public boolean delete(int idReservation) throws SQLException {
		String sql = "DELETE FROM reservation WHERE id_reservation=?";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setInt(1, idReservation);
			return ps.executeUpdate() == 1;
		}
	}
}
