package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Stanza {

    private Reparto reparto;
    private List<Letto> letti;

    public Stanza(Reparto reparto) throws BadArgsException {
        if(reparto == null) throw new BadArgsException("La classe Stanza ha degli attributi NULLI.");

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

    public void setReparto(Reparto reparto) {this.reparto = reparto;}

    public List<Letto> getLetti() {
        return letti;
    }
}