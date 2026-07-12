package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Paziente {

    private String nome;
    private String cognome;
    private String COD_FISCALE; // Questa è la Primary Key nel Database
    private List<Ricovero> ricoveri;

    // Costruttore Guscio per la Foreign Key (usato dal DAO)
    public Paziente(String COD_FISCALE) {
        this.COD_FISCALE = COD_FISCALE;
        this.ricoveri = new ArrayList<>();
    }

    // Costruttore Completo
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCOD_FISCALE() {
        return COD_FISCALE;
    }

    public void setCOD_FISCALE(String COD_FISCALE) {
        this.COD_FISCALE = COD_FISCALE;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    public void setRicoveri(List<Ricovero> ricoveri) {
        this.ricoveri = ricoveri;
    }

    @Override
    public String toString() {
        // Se il nome e cognome sono presenti (non è un semplice guscio)
        if (nome != null && cognome != null) {
            return nome + " " + cognome + " - CF: " + COD_FISCALE;
        }
        // Se è solo un guscio, restituiamo il Codice Fiscale
        return "Paziente CF: " + COD_FISCALE;
    }
}