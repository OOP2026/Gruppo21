package model;

import java.util.ArrayList;
import java.util.List;

public class Letto {

    private String codiceLetto;
    private Stanza stanza;
    private List<Ricovero> ricoveri;

    public Letto(String codiceLetto, Stanza stanza) {
        this.codiceLetto = codiceLetto;
        this.stanza = stanza;
        this.ricoveri = new ArrayList<>();

        stanza.aggiungiLetto(this);
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public String getCodiceLetto() {
        return codiceLetto;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    @Override
    public String toString() {
        return "Letto " + codiceLetto;
    }
}