package devEff.td;

public class Main {
    public static void main(String[] args) {
        // Test : tableau vide
        TableauTrie t1 = new TableauTrie(100);
        System.out.println("Tableau vide créé avec capacité " );
        t1.afficher();

        // Test : tableau aléatoire trié
        TableauTrie t2 = new TableauTrie(20, 100);
        System.out.println("Tableau aléatoire trié (taille 20), capacité " );
        t2.afficher();
    }
}

