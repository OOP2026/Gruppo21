package model;

import java.util.ArrayList;
import java.util.List;

public class Letto {

    private String codiceLetto;
    private String statoStrutturale;
    private Stanza stanza;
    private List<AssegnazioneLetto> assegnazioni;

    public Letto(String codiceLetto, String statoStrutturale, Stanza stanza) {
        this.codiceLetto = codiceLetto;
        this.statoStrutturale = statoStrutturale;
        this.stanza = stanza;
        this.assegnazioni = new ArrayList<>();

        stanza.aggiungiLetto(this);
    }

    public void aggiungiAssegnazione(AssegnazioneLetto assegnazione) {
        assegnazioni.add(assegnazione);
    }

    public String getCodiceLetto() {
        return codiceLetto;
    }

    public String getStatoStrutturale() {
        return statoStrutturale;
    }

    @Override
    public String toString() {
        return "Letto " + codiceLetto + " (" + statoStrutturale + ")";
    }
}