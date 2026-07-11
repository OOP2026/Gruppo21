package model;

import exceptions.BadArgsException;

public class Visita {
    private String nomeVisita;
    // Aggiunte le Foreign Key per mappare il DB
    private Ricovero ricovero;
    private Medico medico;

    public Visita(String nomeVisita) throws BadArgsException {
        if(nomeVisita==null) throw new BadArgsException("La classe Visita ha degli attributi NULLI.");
        if(nomeVisita.isEmpty()) throw new BadArgsException("La classe Visita ha degli attributi VUOTI.");
        this.nomeVisita = nomeVisita;
    }

    // Costruttore completo per il DAO (con FK)
    public Visita(String nomeVisita, Ricovero ricovero, Medico medico) throws BadArgsException {
        this(nomeVisita);
        this.ricovero = ricovero;
        this.medico = medico;
    }

    public String getNomeVisita() { return nomeVisita; }
    public Ricovero getRicovero() { return ricovero; }
    public Medico getMedico() { return medico; }

    @Override
    public String toString() {
        return "Visita: " + nomeVisita;
    }
}