public class Vol {
private int id;
private String numero;
private String villeDepart;
private String villeArrivee;
private String dateDepart;
private String dateArrivee;
private String statut;
public Vol(int id, String numero, String villeDepart, String villeArrivee,
String dateDepart, String dateArrivee, String statut) {
this.id = id;
this.numero = numero;
this.villeDepart = villeDepart;
this.villeArrivee = villeArrivee;
this.dateDepart = dateDepart;
this.dateArrivee = dateArrivee;
this.statut = statut;
}
// Getters / setters / toString()
@Override
public String toString() {
return id + " - " + numero + " (" + villeDepart + " ->" + villeArrivee + ") " + statut;
}
}
