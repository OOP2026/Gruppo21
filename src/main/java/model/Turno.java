package model;

import java.time.LocalDateTime;

public class Turno {

    private Medico medico;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;

    public Turno(Medico medico, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) {
        this.medico = medico;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;

        medico.aggiungiTurno(this);
    }

    @Override
    public String toString() {
        return "Turno di " + medico + " dal " + dataOraInizio + " al " + dataOraFine;
    }
}