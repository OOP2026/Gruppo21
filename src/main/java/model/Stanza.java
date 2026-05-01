package model;

import java.util.ArrayList;
import java.util.List;

public class Stanza {

    private String numeroStanza;
    private int piano;
    private Reparto reparto;
    private List<Letto> letti;

    public Stanza(String numeroStanza, int piano, Reparto reparto) {
        this.numeroStanza = numeroStanza;
        this.piano = piano;
        this.reparto = reparto;
        this.letti = new ArrayList<>();

        reparto.aggiungiStanza(this);
    }

    public void aggiungiLetto(Letto letto) {
        letti.add(letto);
    }

    public String getNumeroStanza() {
        return numeroStanza;
    }

    public List<Letto> getLetti() {
        return letti;
    }

    @Override
    public String toString() {
        return "Stanza " + numeroStanza + ", piano " + piano;
    }
}