import java.util.Arrays;
import java.util.Random;

public class TableauTrie {
    private int[] tab;
    private int taille;

    // Génère un entier aléatoire entre 1 et n inclus
    public static int nombreAleatoire(int n) {
        Random r = new Random();
        return r.nextInt(n) + 1;
    }

    // Crée un tableau de capacite donnée rempli d'entiers aléatoires
    public static int[] creerTableauAleatoire(int capacite) {
        int[] t = new int[capacite];
        for (int i = 0; i < capacite; i++) {
            t[i] = nombreAleatoire(capacite * 10);
        }
        return t;
    }

    // Trie un tableau
    public static void trier(int[] t) {
        Arrays.sort(t);
    }

    // --- Constructeurs ---

    // Tableau vide
    public TableauTrie() {
        this.tab = new int[0];
        this.taille = 0;
    }

    // Tableau de capacite donnée puis trié
    public TableauTrie(int tailleD) {
    this.tab = creerTableauAleatoire(tailleD*5);
    trier(this.tab);
    this.taille = tailleD;
    }

    //Q1 Retourner le nombre d’éléments.
    public int taille() {
    return this.taille;
    }
    // Q2 Ajout d'un élément en conservant l'ordre croissant
      public void push(int val) {
         int i = taille;
        while (i > 0 && tab[i-1] > val) {
            tab[i] = tab[i-1];
            i--;
        }
        tab[i] = val;
        taille++;
    }
        //Q3 Afficher le contenu du tableau.
    public void afficher() {
        System.out.println(Arrays.toString(this.tab));
    }
    //Q4 Retourner la valeur à l’indice ind. Lever une exception si l’indice est invalide.
    public int get(int ind) {
        if (ind < 0 || ind >= taille) {
            throw new IndexOutOfBoundsException("Indice invalide : " + ind);
        }
        return tab[ind];
    }
    //Q5 Retirer l’élément à ind et retourner sa valeur. Le tableau doit rester trié.
    public int remove(int ind) {
        if (ind < 0 || ind >= taille) {
            throw new IndexOutOfBoundsException("Indice invalide : " + ind);
        }
        int val = tab[ind];
        // Décaler les éléments vers la gauche
        for (int i = ind; i < taille - 1; i++) {
            tab[i] = tab[i + 1];
        }
        taille--;
        return val;
    }
    //Q8 recherche dichotomique
    public int indexOf(int val) {
        int gauche = 0;
        int droite = taille - 1;
        while (gauche <= droite) {
            int milieu = (gauche + droite) / 2;
            if (tab[milieu] == val) {
                return milieu;
            } else if (tab[milieu] < val) {
                gauche = milieu + 1;
            } else {
                droite = milieu - 1;
            }
        }
        return -1;
    }

    public static void main (String[]args)
    {
        // Création d'un tableau trié de taille 10
        TableauTrie tableau = new TableauTrie(10);
        System.out.println("Tableau initial :");
        tableau.afficher();
        System.out.println("Taille initiale : " + tableau.taille());

        // Ajout d'un élément
        int valeurAjoutee = 15;
        System.out.println("Ajout de la valeur " + valeurAjoutee + " :");
        // Agrandir le tableau pour push
        tableau.tab = Arrays.copyOf(tableau.tab, tableau.taille + 1);
        tableau.push(valeurAjoutee);
        tableau.afficher();
        System.out.println("Nouvelle taille : " + tableau.taille());
    }
}