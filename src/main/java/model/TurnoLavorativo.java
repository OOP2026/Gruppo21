package model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoLavorativo {

    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private List<Medico> medici;

    public TurnoLavorativo(LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws NullPointerException {
        if(dataOraInizio == null || dataOraFine == null) throw new NullPointerException("La classe TurnoLavorativo ha degli attributi NULLI.");

        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.medici = new ArrayList<>();
    }

    private boolean turnoCompresoInTurno(Medico medico) {
        for(TurnoLavorativo turno : medico.getTurniLavorativi())
            if(turno.getDataOraFine().isAfter(dataOraInizio)) return true;
        return false;
    }

    public void aggiungiMedico(Medico medico) throws RuntimeException {
        for(Medico dottore : medici) {
            if(dottore.equals(medico)) throw new RuntimeException("Il medico è già stato aggiunto in quel turno");
        }

        if(turnoCompresoInTurno(medico)) throw new RuntimeException("Un turno del medico si sovrappone col turno che si vuole aggiungere");

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