package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Letto {

    private int id_letto;
    private Stanza stanza;
    private List<Ricovero> ricoveri;

    public Letto(int id_letto, Stanza stanza) throws BadArgsException {
        if(stanza == null) throw new BadArgsException("La classe Letto ha degli attributi NULLI.");

        this.id_letto = id_letto;
        this.stanza = stanza;
        this.ricoveri = new ArrayList<>();

        stanza.aggiungiLetto(this);
    }

    public Letto(int id_letto) {
        this.id_letto = id_letto;
        this.ricoveri = new ArrayList<>();
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public int getId_letto() {
        return id_letto;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) { this.stanza = stanza; }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    @Override
    public String toString() {
        return "Letto " + id_letto;
    }
}