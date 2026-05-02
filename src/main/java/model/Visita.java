package model;

public class Visita extends Prestazione {

    private String nomeVisita;

    public Visita(String nomeVisita) {
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