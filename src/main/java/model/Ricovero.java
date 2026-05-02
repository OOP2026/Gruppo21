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

    public Ricovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) {
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

    public void aggiungiMedico(Medico medico) {
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