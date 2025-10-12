package model;

public class Reservation {
	private Integer idReservation;
	private int idPassager;
	private int idVol;
	private java.sql.Timestamp dateReservation;

	public Reservation(Integer idReservation, int idPassager, int idVol, java.sql.Timestamp dateReservation) {
		this.idReservation = idReservation;
		this.idPassager = idPassager;
		this.idVol = idVol;
		this.dateReservation = dateReservation;
	}
	public Reservation(int idPassager, int idVol, java.sql.Timestamp dateReservation) {
		this(null, idPassager, idVol, dateReservation);
	}

	public Integer getIdReservation() { return idReservation; }
	public void setIdReservation(Integer idReservation) { this.idReservation = idReservation; }
	public int getIdPassager() { return idPassager; }
	public void setIdPassager(int idPassager) { this.idPassager = idPassager; }
	public int getIdVol() { return idVol; }
	public void setIdVol(int idVol) { this.idVol = idVol; }
	public java.sql.Timestamp getDateReservation() { return dateReservation; }
	public void setDateReservation(java.sql.Timestamp dateReservation) { this.dateReservation = dateReservation; }

	@Override
	public String toString() {
		return "Reservation{id=" + idReservation + ", idPassager=" + idPassager + ", idVol=" + idVol + ", dateReservation=" + dateReservation + "}";
	}
}
