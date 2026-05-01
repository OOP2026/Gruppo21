package model;

import java.time.LocalDateTime;

public class AssegnazioneLetto {

    private Ricovero ricovero;
    private Letto letto;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;

    public AssegnazioneLetto(Ricovero ricovero, Letto letto,
                             LocalDateTime dataOraInizio, LocalDateTime dataOraFine) {
        this.ricovero = ricovero;
        this.letto = letto;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;

        ricovero.aggiungiAssegnazioneLetto(this);
        letto.aggiungiAssegnazione(this);
    }

    @Override
    public String toString() {
        return letto + " assegnato dal " + dataOraInizio + " al " + dataOraFine;
    }
}