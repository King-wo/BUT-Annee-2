package devEff.td;

import devEff.util.OutilsTableau;

public class TableauTrie {
    private final int[] tab;
    private int taille;

    public TableauTrie(int capacite) {
        this.tab = new int[capacite];
        this.taille = 0;
    }

    public TableauTrie(int taille, int capacite) {
        this.tab = OutilsTableau.creerTableauAleatoire(taille, capacite);
        this.taille = taille;
        OutilsTableau.trier(tab, 0, taille); // tri de la partie utile
    }

    public void afficher() {
        System.out.print("[");
        for (int i = 0; i < taille; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(tab[i]);
        }
        System.out.println("]");
    }

    public int taille()   { return taille; }


}
