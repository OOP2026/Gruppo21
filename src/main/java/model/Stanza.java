package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Stanza {

    private int idStanza;  // Primary Key
    private int idReparto; // Foreign Key

    private Reparto reparto;
    private List<Letto> letti;

    public Stanza(int idStanza) {
        this.idStanza = idStanza;
        this.letti = new ArrayList<>();
    }

    public Stanza(Reparto reparto) throws BadArgsException {
        if(reparto == null) throw new BadArgsException("La classe Stanza ha degli attributi NULLI.");

        this.reparto = reparto;
        this.letti = new ArrayList<>();

        reparto.aggiungiStanza(this);
    }

    public void aggiungiLetto(Letto letto) {
        letti.add(letto);
    }

    public int getIdStanza() {
        return idStanza;
    }

    public void setIdStanza(int idStanza) {
        this.idStanza = idStanza;
    }

    public int getIdReparto() {
        return idReparto;
    }

    public void setIdReparto(int idReparto) {
        this.idReparto = idReparto;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    public List<Letto> getLetti() {
        return letti;
    }
}