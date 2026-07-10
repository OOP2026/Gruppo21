package model;

import exceptions.BadArgsException;

public class InterventoChirurgico {

    private String nomeIntervento;

    public InterventoChirurgico(String nomeIntervento) throws BadArgsException {
        if(nomeIntervento == null) throw new BadArgsException("La classe InterventoChirurgico ha degli attributi NULLI.");
        if(nomeIntervento.isEmpty()) throw new BadArgsException("La classe InterventoChirurgico ha degli attributi VUOTI.");

        this.nomeIntervento = nomeIntervento;
    }

    public String getNomeIntervento() {
        return nomeIntervento;
    }

    @Override
    public String toString() {
        return "Intervento chirurgico: " + nomeIntervento;
    }
}