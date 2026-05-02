package model;

public class InterventoChirurgico extends Prestazione {

    private String nomeIntervento;

    public InterventoChirurgico(String nomeIntervento) throws NullPointerException, IllegalArgumentException {
        if(nomeIntervento == null) throw new NullPointerException("La classe InterventoChirurgico ha degli attributi NULLI.");
        if(nomeIntervento.isEmpty()) throw new IllegalArgumentException("La classe InterventoChirurgico ha degli attributi VUOTI.");

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