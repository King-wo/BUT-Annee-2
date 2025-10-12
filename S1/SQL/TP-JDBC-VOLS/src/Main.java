import dao.AvionDAO;
import dao.PassagerDAO;
import dao.PiloteDAO;
import dao.ReservationDAO;
import dao.VolDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import model.Avion;
import model.Passager;
import model.Pilote;
import model.Reservation;
import model.Vol;

public class Main {
    public static void main(String[] args) {
        // Ton DatabaseConnection reste inchangé (classe de test). Ici on ouvre la connexion pour l'appli.
        String url  = "jdbc:mysql://127.0.0.1:8889/volsdb?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = "root";

        try (Connection cnx = DriverManager.getConnection(url, user, pass)) {
            // AVION CRUD
            AvionDAO avionDAO = new AvionDAO(cnx);
            System.out.println("[AVION] create ? " + avionDAO.create(new Avion("A320", 180)));
            List<Avion> avions = avionDAO.readAll();
            System.out.println("[AVION] list   : " + avions);
            if (!avions.isEmpty()) {
                Avion a = avions.get(0);
                a.setCapacite(a.getCapacite() + 6);
                System.out.println("[AVION] update ? " + avionDAO.update(a));
            }
            avions = avionDAO.readAll();
            if (!avions.isEmpty()) {
                Avion a = avions.get(0);
                System.out.println("[AVION] delete ? " + avionDAO.delete(a.getIdAvion()));
            }
            System.out.println("=== FIN TEST AVION ===");

            // PASSAGER CRUD
            PassagerDAO passagerDAO = new PassagerDAO(cnx);
            System.out.println("[PASSAGER] create ? " + passagerDAO.create(new Passager("Dupont", "Jean", "Française")));
            List<Passager> passagers = passagerDAO.readAll();
            System.out.println("[PASSAGER] list   : " + passagers);
            if (!passagers.isEmpty()) {
                Passager p = passagers.get(0);
                p.setNationalite("Italienne");
                System.out.println("[PASSAGER] update ? " + passagerDAO.update(p));
            }
            passagers = passagerDAO.readAll();
            if (!passagers.isEmpty()) {
                Passager p = passagers.get(0);
                System.out.println("[PASSAGER] delete ? " + passagerDAO.delete(p.getIdPassager()));
            }
            System.out.println("=== FIN TEST PASSAGER ===");

            // PILOTE CRUD
            PiloteDAO piloteDAO = new PiloteDAO(cnx);
            System.out.println("[PILOTE] create ? " + piloteDAO.create(new Pilote("Martin", "Paul", 10)));
            List<Pilote> pilotes = piloteDAO.readAll();
            System.out.println("[PILOTE] list   : " + pilotes);
            if (!pilotes.isEmpty()) {
                Pilote p = pilotes.get(0);
                p.setExperience(p.getExperience() + 1);
                System.out.println("[PILOTE] update ? " + piloteDAO.update(p));
            }
            pilotes = piloteDAO.readAll();
            if (!pilotes.isEmpty()) {
                Pilote p = pilotes.get(0);
                System.out.println("[PILOTE] delete ? " + piloteDAO.delete(p.getIdPilote()));
            }
            System.out.println("=== FIN TEST PILOTE ===");

            // RESERVATION CRUD
            ReservationDAO reservationDAO = new ReservationDAO(cnx);

            // Créer une réservation (adapte les id et la date selon ta base)
            Reservation r = new Reservation(1, 1, java.sql.Timestamp.valueOf("2025-08-15 10:00:00"));
            System.out.println("[RESERVATION] create ? " + reservationDAO.create(r));

            // Lister toutes les réservations d'un vol
            List<Reservation> reservationsVol = reservationDAO.getReservationsByVol(1);
            System.out.println("[RESERVATION] list by vol : " + reservationsVol);

            // Lister tous les vols réservés par un passager donné
            List<Vol> volsReserves = reservationDAO.getVolsByPassager(1);
            System.out.println("[RESERVATION] vols réservés par passager 1 : " + volsReserves);

            // Supprimer une réservation (sur le premier trouvé)
            if (!reservationsVol.isEmpty()) {
                Reservation res = reservationsVol.get(0);
                System.out.println("[RESERVATION] delete ? " + reservationDAO.delete(res.getIdReservation()));
            }
            System.out.println("=== FIN TEST RESERVATION ===");

            // VOL CRUD
            VolDAO volDAO = new VolDAO(cnx);

            // Créer un vol (en utilisant des id existants pour pilote et avion)
            Vol vol = new Vol(
                "V123",
                "Paris",
                "Londres",
                java.sql.Timestamp.valueOf("2025-09-01 08:00:00"),
                java.sql.Timestamp.valueOf("2025-09-01 10:00:00"),
                "prévu",
                1,
                1
            );
            System.out.println("[VOL] create ? " + volDAO.create(vol));

            // Lister tous les vols avec les infos détaillées
            List<Map<String, Object>> vols = volDAO.readAllDetails();
            System.out.println("[VOL] list   :");
            for (Map<String, Object> v : vols) {
                System.out.println(v);
            }

            // Modifier le statut d’un vol
            if (!vols.isEmpty()) {
                int idVol = (int) vols.get(0).get("id_vol");
                System.out.println("[VOL] update statut ? " + volDAO.updateStatut(idVol, "retardé"));
            }

            // Supprimer un vol
            vols = volDAO.readAllDetails();
            if (!vols.isEmpty()) {
                int idVol = (int) vols.get(0).get("id_vol");
                System.out.println("[VOL] delete ? " + volDAO.delete(idVol));
            }
            
            System.out.println("=== FIN TEST VOL ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
