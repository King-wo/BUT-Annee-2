
package model;

public class Pilote {
    private Integer idPilote;
    private String nom;
    private String prenom;
    private int experience;

    public Pilote(Integer idPilote, String nom, String prenom, int experience) {
        this.idPilote = idPilote;
        this.nom = nom;
        this.prenom = prenom;
        this.experience = experience;
    }
    public Pilote(String nom, String prenom, int experience) {
        this(null, nom, prenom, experience);
    }

    public Integer getIdPilote() { return idPilote; }
    public void setIdPilote(Integer idPilote) { this.idPilote = idPilote; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    @Override
    public String toString() {
        return "Pilote{id=" + idPilote + ", nom='" + nom + "', prenom='" + prenom + "', experience=" + experience + "}";
    }
}
