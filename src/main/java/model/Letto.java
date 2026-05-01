package model;

import java.util.ArrayList;
import java.util.List;

public class Letto {

    private String numeroLetto;
    private String statoStrutturale;
    private Stanza stanza;
    private List<AssegnazioneLetto> assegnazioni;

    public Letto(String numeroLetto, String statoStrutturale, Stanza stanza) {
        this.numeroLetto = numeroLetto;
        this.statoStrutturale = statoStrutturale;
        this.stanza = stanza;
        this.assegnazioni = new ArrayList<>();

        stanza.aggiungiLetto(this);
    }

    public void aggiungiAssegnazione(AssegnazioneLetto assegnazione) {
        assegnazioni.add(assegnazione);
    }

    public String getNumeroLetto() {
        return numeroLetto;
    }

    public String getStatoStrutturale() {
        return statoStrutturale;
    }

    @Override
    public String toString() {
        return "Letto " + numeroLetto + " (" + statoStrutturale + ")";
    }
}