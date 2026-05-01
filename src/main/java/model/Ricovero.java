package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ricovero {

    private Paziente paziente;
    private Medico medicoResponsabile;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFinePrevista;
    private LocalDateTime dataOraFineEffettiva;
    private String statoRicovero;
    private String note;

    private List<AssegnazioneLetto> assegnazioniLetto;
    private List<Prestazione> prestazioni;

    public Ricovero(Paziente paziente, Medico medicoResponsabile,
                    LocalDateTime dataOraInizio, LocalDateTime dataOraFinePrevista,
                    String statoRicovero, String note) {
        this.paziente = paziente;
        this.medicoResponsabile = medicoResponsabile;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFinePrevista = dataOraFinePrevista;
        this.statoRicovero = statoRicovero;
        this.note = note;
        this.assegnazioniLetto = new ArrayList<>();
        this.prestazioni = new ArrayList<>();

        paziente.aggiungiRicovero(this);
    }

    public void aggiungiAssegnazioneLetto(AssegnazioneLetto assegnazione) {
        assegnazioniLetto.add(assegnazione);
    }

    public void aggiungiPrestazione(Prestazione prestazione) {
        prestazioni.add(prestazione);
    }

    public void dimetti(LocalDateTime dataOraFineEffettiva) {
        this.dataOraFineEffettiva = dataOraFineEffettiva;
        this.statoRicovero = "DIMESSO";
    }

    public Paziente getPaziente() {
        return paziente;
    }

    @Override
    public String toString() {
        return "Ricovero di " + paziente.getNomeCompleto() + " - stato: " + statoRicovero;
    }
}