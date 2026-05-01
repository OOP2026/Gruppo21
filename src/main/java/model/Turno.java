package model;

import java.time.LocalDateTime;

public class Turno {
    private Medico medico;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;

    public Turno(Medico medico, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws NullPointerException {
        if(medico==null
                || dataOraInizio == null
                || dataOraFine == null)
            throw new NullPointerException("E' stato passato un attributo nullo nella classe Turno.");
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