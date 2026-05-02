package model;

import java.util.ArrayList;
import java.util.List;

public class Stanza {

    private Reparto reparto;
    private List<Letto> letti;

    public Stanza(Reparto reparto) {
        this.reparto = reparto;
        this.letti = new ArrayList<>();

        reparto.aggiungiStanza(this);
    }

    public void aggiungiLetto(Letto letto) {
        letti.add(letto);
    }

    public Reparto getReparto() {
        return reparto;
    }

    public List<Letto> getLetti() {
        return letti;
    }
}