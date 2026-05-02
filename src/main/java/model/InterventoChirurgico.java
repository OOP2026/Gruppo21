package model;

public class InterventoChirurgico extends Prestazione {

    private String nomeIntervento;

    public InterventoChirurgico(String nomeIntervento) {
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