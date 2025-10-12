package devEff.td;

public class Maillon {
    private int donnee;
    private Maillon precedent;
    private Maillon suivant;

    public Maillon(int donnee, Maillon precedent, Maillon suivant) {
        this.donnee = donnee;
        this.precedent = precedent;
        this.suivant = suivant;
    }

    public Maillon(int donnee) {
        this(donnee, null, null);
    }

    public int getDonnee() {
        return donnee;
    }

    public void setDonnee(int valeur) {
        this.donnee = valeur;
    }

    public Maillon getSuivant() {
        return suivant;
    }

    public void setSuivant(Maillon suivant) {
        this.suivant = suivant;
    }

    public Maillon getPrecedent() {
        return precedent;
    }

    public void setPrecedent(Maillon precedent) {
        this.precedent = precedent;
    }
}
