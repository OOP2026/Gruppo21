package model;

public class Visita extends Prestazione {

    private String nomeVisita;

    public Visita(String nomeVisita) throws NullPointerException, IllegalArgumentException {
        if(nomeVisita==null) throw new NullPointerException("La classe Visita ha degli attributi NULLI.");
        if(nomeVisita.isEmpty()) throw new IllegalArgumentException("La classe Visita ha degli attributi VUOTI.");

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