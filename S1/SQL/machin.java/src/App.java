import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:8889/volsdb"; // nom de ta BDD ici vols
        String user = "root"; // ton utilisateur
        String password = "root"; // ton mot de passe
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexion réussie !");
            // requête SQL à exécuter
            String sql = "SELECT * FROM vol";
            // prépare un objet `Statement` pour que vous puissiez ensuite lui donner une
            // requête SQL à exécuter.
            Statement stmt = conn.createStatement();
            // envoi de la requête et récupération du résultat
            ResultSet rs = stmt.executeQuery(sql);
            // on parcourt le résultat de la requete en SELECT tant qu'il y a une ligne
            // suivante
            while (rs.next()) {
                Vol v = new Vol(
                        rs.getInt("id_vol"),
                        rs.getString("numero_vol"),
                        rs.getString("ville_depart"),
                        rs.getString("ville_arrivee"),
                        rs.getString("date_depart"),
                        rs.getString("date_arrivee"),
                        rs.getString("statut"));
                System.out.println(v.toString() + '\n');
            }
            //insertion d'un nouveau vol
//Préparation de la requête avec des ? au niveau des valeurs pour plus de sécurité et possibilité de réutiliser
String sqlInsert = "INSERT INTO vol (numero_vol, ville_depart, ville_arrivee, date_depart, date_arrivee, statut) VALUES (?, ?, ?, ?, ?, ?)";
try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
//on complète les valeurs en précisant leur type
pstmt.setString(1, "AF502");
pstmt.setString(2, "Paris");
pstmt.setString(3, "London");
// Assurez-vous que le format de date correspond à ce que votre BDD attend (ex: YYYY-MM-DD HH:MI:SS)
pstmt.setString(4, "2025-09-21 07:00:00");
pstmt.setString(5, "2025-09-21 09:00:00");
pstmt.setString(6, "prévu");
//on récupère le nombre de lignes insérées (normalement 1) après avoir exécuter la requête
int affectedRows = pstmt.executeUpdate();
if (affectedRows > 0) {
System.out.println("Vol inséré avec succès !");
}
} catch (SQLException e) {
System.out.println("Erreur lors de l'insertion du vol.");
e.printStackTrace();
}
System.out.println("\n--- Liste de tous les vols ---");//on poursuit avec le SELECT pour vérifier
String sqlupdate = "UPDATE vol SET statut = ? WHERE id_vol = ?";
try (PreparedStatement stmtupdate = conn.prepareStatement(sqlupdate)) {
stmtupdate.setString(1, "retardé");
stmtupdate.setInt(2, 10);
stmtupdate.executeUpdate();
}
String sqldel = "DELETE FROM vol WHERE id_vol = ?";
try (PreparedStatement stmtdel = conn.prepareStatement(sqldel)) {
stmtdel.setInt(1, 11);
stmtdel.executeUpdate();
}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}