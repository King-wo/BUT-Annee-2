package devEff.td;

public class ListeDoublementChainee {
    private Maillon head;
    private Maillon tail;
    private int size;

    public ListeDoublementChainee() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void ajouterEnTete(int valeur) {
        Maillon nouveauMaillon = new Maillon(valeur);
        if (head == null) {
            head = nouveauMaillon;
            tail = nouveauMaillon;
        } else {
            nouveauMaillon.setSuivant(head);
            head.setPrecedent(nouveauMaillon);
            head = nouveauMaillon;
        }
        size++;
    }

    public void ajouterEnQueue(int valeur) {
        Maillon nouveauMaillon = new Maillon(valeur);
        if (tail == null) {
            head = nouveauMaillon;
            tail = nouveauMaillon;
        } else {
            tail.setSuivant(nouveauMaillon);
            nouveauMaillon.setPrecedent(tail);
            tail = nouveauMaillon;
        }
        size++;
    }

    public void supprimerEnTete() {
        if (head != null) {
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.getSuivant();
                if (head != null) {
                    head.setPrecedent(null);
                }
            }
            size--;
        }
    }

    public void supprimerEnQueue() {
        if (tail != null) {
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                tail = tail.getPrecedent();
                if (tail != null) {
                    tail.setSuivant(null);
                }
            }
            size--;
        }
    }

    public int getTaille() {
        return size;
    }

    public boolean estVide() {
        return size == 0;
    }

    public void relierDebut() {
        if (head != null && tail != null) {
            tail.setSuivant(head);
            head.setPrecedent(tail);
        }
    }
    public void relierFin() {
        if (head != null && tail != null) {
            head.setPrecedent(tail);
            tail.setSuivant(head);
        }
    }
    public Maillon rechercher(int e) {
        Maillon courant = head;
        while (courant != null) {
            if (courant.getDonnee() == e) {
                return courant;
            }
            courant = courant.getSuivant();
        }
        return null;
    }
    public Maillon accessMaillon(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
        }
        Maillon courant = head;
        for (int j = 0; j < i; j++) {
            courant = courant.getSuivant();
        }
        return courant;
    }
    public Maillon insert(int e, Maillon m) {
        if (m == null) {
            throw new IllegalArgumentException("Le maillon de référence ne peut pas être null.");
        }
        Maillon nouveauMaillon = new Maillon(e);
        Maillon precedent = m.getPrecedent();
        nouveauMaillon.setSuivant(m);
        nouveauMaillon.setPrecedent(precedent);
        m.setPrecedent(nouveauMaillon);
        if (precedent != null) {
            precedent.setSuivant(nouveauMaillon);
        } else {
            head = nouveauMaillon; 
        }
        size++;
        return nouveauMaillon;
    }
}