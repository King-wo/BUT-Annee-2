package dao;

import java.sql.*;
import java.util.*;
import model.Vol;

public class VolDAO {
	private final Connection cnx;
	public VolDAO(Connection cnx) { this.cnx = cnx; }

	// 1. Créer un vol
	public boolean create(Vol v) throws SQLException {
		String sql = "INSERT INTO vol(numero_vol, ville_depart, ville_arrivee, date_depart, date_arrivee, statut, id_pilote, id_avion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setString(1, v.getNumeroVol());
			ps.setString(2, v.getVilleDepart());
			ps.setString(3, v.getVilleArrivee());
			ps.setTimestamp(4, v.getDateDepart());
			ps.setTimestamp(5, v.getDateArrivee());
			ps.setString(6, v.getStatut());
			ps.setInt(7, v.getIdPilote());
			ps.setInt(8, v.getIdAvion());
			return ps.executeUpdate() > 0;
		}
	}

	// 2. Lister tous les vols avec infos détaillées
	public List<Map<String, Object>> readAllDetails() throws SQLException {
		String sql = "SELECT v.id_vol, v.numero_vol, v.ville_depart, v.ville_arrivee, v.statut, p.nom AS nom_pilote, a.modele AS modele_avion " +
					 "FROM vol v JOIN pilote p ON v.id_pilote=p.id_pilote JOIN avion a ON v.id_avion=a.id_avion ORDER BY v.id_vol";
		try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			List<Map<String, Object>> list = new ArrayList<>();
			while (rs.next()) {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("id_vol", rs.getInt("id_vol"));
				map.put("numero_vol", rs.getString("numero_vol"));
				map.put("ville_depart", rs.getString("ville_depart"));
				map.put("ville_arrivee", rs.getString("ville_arrivee"));
				map.put("statut", rs.getString("statut"));
				map.put("nom_pilote", rs.getString("nom_pilote"));
				map.put("modele_avion", rs.getString("modele_avion"));
				list.add(map);
			}
			return list;
		}
	}

	// 3. Modifier le statut d'un vol
	public boolean updateStatut(int idVol, String statut) throws SQLException {
		String sql = "UPDATE vol SET statut=? WHERE id_vol=?";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setString(1, statut);
			ps.setInt(2, idVol);
			return ps.executeUpdate() == 1;
		}
	}

	// 4. Supprimer un vol
	public boolean delete(int idVol) throws SQLException {
		// Vérifier s'il existe des réservations pour ce vol
		String check = "SELECT COUNT(*) FROM reservation WHERE id_vol=?";
		try (PreparedStatement psCheck = cnx.prepareStatement(check)) {
			psCheck.setInt(1, idVol);
			try (ResultSet rs = psCheck.executeQuery()) {
				rs.next();
				if (rs.getInt(1) > 0) {
					System.out.println("Suppression refusée : des réservations utilisent encore le vol " + idVol + ".");
					return false;
				}
			}
		}
		String sql = "DELETE FROM vol WHERE id_vol=?";
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			ps.setInt(1, idVol);
			return ps.executeUpdate() == 1;
		}
	}
}
