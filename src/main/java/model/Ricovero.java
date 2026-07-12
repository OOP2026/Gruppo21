package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ricovero {

    private int idRicovero;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private Paziente paziente;
    private Letto letto;
    private List<Medico> medici;

    public Ricovero(int idRicovero) {
        this.idRicovero = idRicovero;
        this.medici = new ArrayList<>();
    }

    public int getIdRicovero() {
        return idRicovero;
    }

    public void aggiungiMedico(Medico medico) throws RuntimeException {
        medici.add(medico);
        medico.aggiungiRicovero(this);
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Letto getLetto() {
        return letto;
    }

    public void setLetto(Letto letto) {
        this.letto = letto;
    }

    @Override
    public String toString() {
        if (paziente != null && letto != null) {
            return "Ricovero di " + paziente.getNome() + " " + paziente.getCognome() + " nel letto " + letto;
        }
        return "Ricovero ID: " + idRicovero;
    }
}