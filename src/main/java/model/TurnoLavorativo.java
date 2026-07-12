package model;

import exceptions.BadArgsException;

import java.time.LocalDateTime;

public class TurnoLavorativo {
    private int idTurno;

    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;

    private Medico medico;

    public TurnoLavorativo(LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws BadArgsException {
        if(dataOraInizio == null || dataOraFine == null) throw new BadArgsException("La classe TurnoLavorativo ha degli attributi NULLI.");
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
    }

    public int getIdTurno() { return idTurno; }
    public void setIdTurno(int idTurno) { this.idTurno = idTurno; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDateTime getDataOraInizio() { return dataOraInizio; }
    public LocalDateTime getDataOraFine() { return dataOraFine; }

    @Override
    public String toString() {
        return "Turno da " + dataOraInizio.toString() + " a " + dataOraFine.toString();
    }
}