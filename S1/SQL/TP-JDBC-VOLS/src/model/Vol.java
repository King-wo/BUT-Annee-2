package model;

public class Vol {
	private Integer idVol;
	private String numeroVol; // correspond à numero_vol dans la BD
	private String villeDepart;
	private String villeArrivee;
	private java.sql.Timestamp dateDepart;
	private java.sql.Timestamp dateArrivee;
	private String statut; // prévu, retardé, annulé
	private int idPilote;
	private int idAvion;

	public Vol(Integer idVol, String numeroVol, String villeDepart, String villeArrivee, java.sql.Timestamp dateDepart, java.sql.Timestamp dateArrivee, String statut, int idPilote, int idAvion) {
		this.idVol = idVol;
		this.numeroVol = numeroVol;
		this.villeDepart = villeDepart;
		this.villeArrivee = villeArrivee;
		this.dateDepart = dateDepart;
		this.dateArrivee = dateArrivee;
		this.statut = statut;
		this.idPilote = idPilote;
		this.idAvion = idAvion;
	}
	public Vol(String numeroVol, String villeDepart, String villeArrivee, java.sql.Timestamp dateDepart, java.sql.Timestamp dateArrivee, String statut, int idPilote, int idAvion) {
		this(null, numeroVol, villeDepart, villeArrivee, dateDepart, dateArrivee, statut, idPilote, idAvion);
	}

	public Integer getIdVol() { return idVol; }
	public void setIdVol(Integer idVol) { this.idVol = idVol; }
	public String getNumeroVol() { return numeroVol; }
	public void setNumeroVol(String numeroVol) { this.numeroVol = numeroVol; }
	public String getVilleDepart() { return villeDepart; }
	public void setVilleDepart(String villeDepart) { this.villeDepart = villeDepart; }
	public String getVilleArrivee() { return villeArrivee; }
	public void setVilleArrivee(String villeArrivee) { this.villeArrivee = villeArrivee; }
	public java.sql.Timestamp getDateDepart() { return dateDepart; }
	public void setDateDepart(java.sql.Timestamp dateDepart) { this.dateDepart = dateDepart; }
	public java.sql.Timestamp getDateArrivee() { return dateArrivee; }
	public void setDateArrivee(java.sql.Timestamp dateArrivee) { this.dateArrivee = dateArrivee; }
	public String getStatut() { return statut; }
	public void setStatut(String statut) { this.statut = statut; }
	public int getIdPilote() { return idPilote; }
	public void setIdPilote(int idPilote) { this.idPilote = idPilote; }
	public int getIdAvion() { return idAvion; }
	public void setIdAvion(int idAvion) { this.idAvion = idAvion; }

	@Override
	public String toString() {
		return "Vol{id=" + idVol + ", numeroVol='" + numeroVol + "', depart='" + villeDepart + "', arrivee='" + villeArrivee + "', dateDepart='" + dateDepart + "', dateArrivee='" + dateArrivee + "', statut='" + statut + "', idPilote=" + idPilote + ", idAvion=" + idAvion + "}";
	}
}
