package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ricovero {

    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private Paziente paziente;
    private Letto letto;
    private List<Medico> medici;

    public Ricovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws NullPointerException {
        if(paziente==null || letto == null || dataOraInizio == null || dataOraFine == null) throw new NullPointerException("La classe Ricovero ha degli attributi NULLI.");

        this.paziente = paziente;
        this.letto = letto;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.medici = new ArrayList<>();

        paziente.aggiungiRicovero(this);
        letto.aggiungiRicovero(this);
    }

    public Ricovero richiedereRicovero() {
        return this;
    }

    private boolean turnoCompresoInRicovero(Medico medico) {
        boolean controllo = false;

        for(TurnoLavorativo turno : medico.getTurniLavorativi()) {
            if(!turno.getDataOraFine().isBefore(dataOraInizio)) {
                controllo = true;
                break;
            }
        }

        return controllo;
    }

    public void aggiungiMedico(Medico medico) throws RuntimeException {
        for(Medico dottore : this.medici) {
            if(dottore.equals(medico)) throw new RuntimeException("Il medico è gia stato aggiunto in questo ricovero");
        }
        if(!turnoCompresoInRicovero(medico)) throw new RuntimeException("Il medico sta venendo aggiunto in un ricovero fuori dai suoi turni");

        medici.add(medico);
        medico.aggiungiRicovero(this);
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public Letto getLetto() {
        return letto;
    }

    public List<Medico> getMedici() {
        return medici;
    }

    @Override
    public String toString() {
        return "Ricovero di " + paziente + " nel " + letto;
    }
}