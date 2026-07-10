package model;

import exceptions.BadArgsException;

public class Visita {

    private String nomeVisita;

    public Visita(String nomeVisita) throws BadArgsException {
        if(nomeVisita==null) throw new BadArgsException("La classe Visita ha degli attributi NULLI.");
        if(nomeVisita.isEmpty()) throw new BadArgsException("La classe Visita ha degli attributi VUOTI.");

        this.nomeVisita = nomeVisita;
    }

    public String getNomeVisita() {
        return nomeVisita;
    }

    @Override
    public String toString() {
        return "Visita: " + nomeVisita;
    }
}