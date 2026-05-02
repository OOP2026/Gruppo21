package model;

import java.util.ArrayList;
import java.util.List;

public class Medico extends UtenteRegistrato {

    private String tipoMedico;
    private Reparto reparto;
    private List<Ricovero> ricoveri;
    private List<Prestazione> prestazioni;
    private List<TurnoLavorativo> turniLavorativi;

    public Medico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto) {
        super(nome, cognome, email, password);
        this.tipoMedico = tipoMedico;
        this.reparto = reparto;
        this.ricoveri = new ArrayList<>();
        this.prestazioni = new ArrayList<>();
        this.turniLavorativi = new ArrayList<>();

        reparto.aggiungiMedico(this);
    }

    public void aggiungerePrestazione(Prestazione prestazione) {
        prestazioni.add(prestazione);
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public void aggiungiTurnoLavorativo(TurnoLavorativo turnoLavorativo) {
        turniLavorativi.add(turnoLavorativo);
    }

    public String getTipoMedico() {
        return tipoMedico;
    }

    public Reparto getReparto() {
        return reparto;
    }

    @Override
    public String toString() {
        return getNome() + " " + getCognome() + " - " + tipoMedico;
    }
}