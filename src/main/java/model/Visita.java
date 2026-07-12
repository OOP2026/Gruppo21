package model;

import exceptions.BadArgsException;

public class Visita {
    private int idVisita;
    private String nomeVisita;

    // Foreign Keys mappate ad Oggetti
    private Ricovero ricovero;
    private Medico medico;


    public Visita(int idVisita) {
        this.idVisita = idVisita;
    }


    public Visita(String nomeVisita, Ricovero ricovero, Medico medico) throws BadArgsException {
        if (nomeVisita == null || ricovero == null || medico == null) {
            throw new BadArgsException("La classe Visita ha degli attributi NULLI.");
        }
        if (nomeVisita.isEmpty()) {
            throw new BadArgsException("La classe Visita ha degli attributi VUOTI.");
        }
        this.nomeVisita = nomeVisita;
        this.ricovero = ricovero;
        this.medico = medico;
    }

    public int getIdVisita() { return idVisita; }
    public void setIdVisita(int idVisita) { this.idVisita = idVisita; }

    public String getNomeVisita() { return nomeVisita; }
    public void setNomeVisita(String nomeVisita) { this.nomeVisita = nomeVisita; }

    public Ricovero getRicovero() { return ricovero; }
    public void setRicovero(Ricovero ricovero) { this.ricovero = ricovero; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    @Override
    public String toString() {
        String pNome = (ricovero != null && ricovero.getPaziente() != null) ? ricovero.getPaziente().getCodFiscale() : "Sconosciuto";
        return idVisita + " - " + nomeVisita + " (Paziente: " + pNome + ")";
    }
}