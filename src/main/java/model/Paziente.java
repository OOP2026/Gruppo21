package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Paziente {

    private String nome;
    private String cognome;
    private String codFiscale; // Questa è la Primary Key nel Database
    private List<Ricovero> ricoveri;


    public Paziente(String codFiscale) {
        this.codFiscale = codFiscale;
        this.ricoveri = new ArrayList<>();
    }


    public Paziente(String nome, String cognome, String codFiscale) throws BadArgsException {
        if(codFiscale == null) throw new BadArgsException("La classe Paziente ha degli attributi NULLI.");
        if(codFiscale.isEmpty()) throw new BadArgsException("La classe Paziente ha degli attributi VUOTI.");

        this.codFiscale = codFiscale;
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodFiscale() {
        return codFiscale;
    }

    public void setCodFiscale(String codFiscale) {
        this.codFiscale = codFiscale;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    public void setRicoveri(List<Ricovero> ricoveri) {
        this.ricoveri = ricoveri;
    }

    @Override
    public String toString() {

        if (nome != null && cognome != null) {
            return nome + " " + cognome + " - CF: " + codFiscale;
        }

        return "Paziente CF: " + codFiscale;
    }
}