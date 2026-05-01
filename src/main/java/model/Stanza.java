package model;

import java.util.ArrayList;
import java.util.List;

public class Stanza {

    private String numeroStanza;
    private int piano;
    private Reparto reparto;
    private List<Letto> letti;

    public Stanza(String numeroStanza, int piano, Reparto reparto) throws NullPointerException, IllegalArgumentException {
        if(numeroStanza == null
                || reparto == null)
            throw new NullPointerException("E' stato passato un attributo NULLO nella classe Stanza.");

        if(numeroStanza.isEmpty()) throw new IllegalArgumentException("E' stato passato un attributo VUOTO nella classe Stanza.");

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