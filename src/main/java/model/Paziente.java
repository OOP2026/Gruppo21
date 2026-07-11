package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Paziente {

    private String nome;
    private String cognome;
    private String COD_FISCALE;
    private List<Ricovero> ricoveri;

    public Paziente(String nome, String cognome, String COD_FISCALE) throws BadArgsException {
        if(COD_FISCALE == null) throw new BadArgsException("La classe Paziente ha degli attributi NULLI.");
        if(COD_FISCALE.isEmpty()) throw new BadArgsException("La classe Paziente ha degli attributi VUOTI.");

        this.COD_FISCALE = COD_FISCALE;
        this.ricoveri = new ArrayList<>();
        this.nome = nome;
        this.cognome = cognome;
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setRicoveri(List<Ricovero> ricoveri) {
        this.ricoveri = ricoveri;
    }

    public void setCOD_FISCALE(String COD_FISCALE) {
        this.COD_FISCALE = COD_FISCALE;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCOD_FISCALE() {
        return COD_FISCALE;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " - CF: " + COD_FISCALE;
    }
}