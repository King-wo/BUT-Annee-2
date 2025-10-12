package devEff.util;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public final class OutilsTableau {
    private OutilsTableau() {}

    // Génère un entier aléatoire entre 1 et n inclus
    public static int nombreAleatoire(int n) {
        return ThreadLocalRandom.current().nextInt(1, n + 1);
    }

    // Crée un tableau de capacité donnée et remplit les 'taille' premières cases
    public static int[] creerTableauAleatoire(int taille, int capacite) {
        if (taille < 0 || taille > capacite) {
            throw new IllegalArgumentException("taille doit être entre 0 et capacité");
        }
        int[] t = new int[capacite];
        for (int i = 0; i < taille; i++) {
            t[i] = nombreAleatoire(capacite * 10);
        }
        return t;
    }

    // Trie une partie du tableau [de, a)
    public static void trier(int[] t, int de, int a) {
        Arrays.sort(t, de, a);
    }
}
