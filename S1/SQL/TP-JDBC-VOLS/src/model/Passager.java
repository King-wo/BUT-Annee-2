
package model;

public class Passager {
    private Integer idPassager;
    private String nom;
    private String prenom;
    private String nationalite;

    public Passager(Integer idPassager, String nom, String prenom, String nationalite) {
        this.idPassager = idPassager;
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
    }
    public Passager(String nom, String prenom, String nationalite) {
        this(null, nom, prenom, nationalite);
    }

    public Integer getIdPassager() { return idPassager; }
    public void setIdPassager(Integer idPassager) { this.idPassager = idPassager; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }

    @Override
    public String toString() {
        return "Passager{id=" + idPassager + ", nom='" + nom + "', prenom='" + prenom + "', nationalite='" + nationalite + "'}";
    }
}
