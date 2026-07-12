package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Letto {

    private int idLetto;
    private Stanza stanza;
    private List<Ricovero> ricoveri;

    public Letto(int idLetto, Stanza stanza) throws BadArgsException {
        if(stanza == null) throw new BadArgsException("La classe Letto ha degli attributi NULLI.");

        this.idLetto = idLetto;
        this.stanza = stanza;
        this.ricoveri = new ArrayList<>();

        stanza.aggiungiLetto(this);
    }

    public Letto(int idLetto) {
        this.idLetto = idLetto;
        this.ricoveri = new ArrayList<>();
    }

    public Letto(Stanza stanza) throws BadArgsException {
        if(stanza==null) throw new BadArgsException("La classe Letto ha degli attributi NULLI.");

        this.stanza = stanza;
        this.ricoveri = new ArrayList<>();
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public int getIdLetto() {
        return idLetto;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) { this.stanza = stanza; }

    @Override
    public String toString() {
        return "Letto " + idLetto;
    }
}