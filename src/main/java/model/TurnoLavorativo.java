package model;

import exceptions.BadArgsException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoLavorativo {

    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private List<Medico> medici;

    public TurnoLavorativo(LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws BadArgsException {
        if(dataOraInizio == null || dataOraFine == null) throw new BadArgsException("La classe TurnoLavorativo ha degli attributi NULLI.");

        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.medici = new ArrayList<>();
    }

    public void aggiungiMedico(Medico medico) throws RuntimeException {
        medici.add(medico);
        medico.aggiungiTurnoLavorativo(this);
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public List<Medico> getMedici() {
        return medici;
    }

    @Override
    public String toString() {
        return String.valueOf("Turno da " + dataOraInizio.toString() + " a " + dataOraFine.toString());
    }
}