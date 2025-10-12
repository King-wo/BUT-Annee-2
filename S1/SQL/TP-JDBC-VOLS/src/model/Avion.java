package model;

public class Avion {
    private Integer idAvion;   
    private String  modele;    
    private int     capacite;  

    public Avion(Integer idAvion, String modele, int capacite) {
        this.idAvion = idAvion;
        this.modele = modele;
        this.capacite = capacite;
    }
    public Avion(String modele, int capacite) { this(null, modele, capacite); }

    public Integer getIdAvion() { return idAvion; }
    public void setIdAvion(Integer idAvion) { this.idAvion = idAvion; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    @Override public String toString() {
        return "Avion{id=" + idAvion + ", modele='" + modele + "', capacite=" + capacite + "}";
    }
}
