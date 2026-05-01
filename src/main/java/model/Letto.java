package model;

import java.util.ArrayList;
import java.util.List;

public class Letto {

    private String codiceLetto;
    private String statoStrutturale;
    private Stanza stanza;
    private List<AssegnazioneLetto> assegnazioni;

    public Letto(String codiceLetto, String statoStrutturale, Stanza stanza) throws NullPointerException, IllegalArgumentException {
        if(codiceLetto==null
                || statoStrutturale == null
                || stanza == null)
            throw new NullPointerException("E' stato passato un attributo NULL nella classe Letto.");

        if(codiceLetto.isEmpty()) throw new IllegalArgumentException("E' stato passato un attributo VUOTO nella classe Letto");

        if(!statoStrutturale.equals("DISPONIBILE") && !statoStrutturale.equals("OCCUPATO"))
            throw new IllegalArgumentException("statoStrutturale nella classe Letto non rispetta i valori enumerativi (Valore non consentito)");

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