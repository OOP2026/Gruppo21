package model;

import java.util.ArrayList;
import java.util.List;

public class Paziente extends Persona {

    private String COD_FISCALE;
    private List<Ricovero> ricoveri;

    public Paziente(String nome, String cognome, String COD_FISCALE) {
        super(nome, cognome);
        this.COD_FISCALE = COD_FISCALE;
        this.ricoveri = new ArrayList<>();
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public String getCOD_FISCALE() {
        return COD_FISCALE;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    @Override
    public String toString() {
        return getNome() + " " + getCognome() + " - CF: " + COD_FISCALE;
    }
}