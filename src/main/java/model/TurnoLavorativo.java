package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoLavorativo {

    private Giorno giornoInizio;
    private LocalTime oraInizio;
    private Giorno giornoFine;
    private LocalTime oraFine;
    private List<Medico> medici;

    public TurnoLavorativo(Giorno giornoInizio, LocalTime oraInizio, Giorno giornoFine, LocalTime oraFine) {
        this.giornoInizio = giornoInizio;
        this.oraInizio = oraInizio;
        this.giornoFine = giornoFine;
        this.oraFine = oraFine;
        this.medici = new ArrayList<>();
    }

    public void aggiungiMedico(Medico medico) {
        medici.add(medico);
        medico.aggiungiTurnoLavorativo(this);
    }

    public Giorno getGiornoInizio() {
        return giornoInizio;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public Giorno getGiornoFine() {
        return giornoFine;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public List<Medico> getMedici() {
        return medici;
    }

    @Override
    public String toString() {
        return "Turno da " + giornoInizio + " " + oraInizio + " a " + giornoFine + " " + oraFine;
    }
}