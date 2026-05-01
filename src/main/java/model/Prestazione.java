package model;

import java.time.LocalDateTime;

public class Prestazione {

    private Ricovero ricovero;
    private Medico medico;
    private TipoPrestazione tipoPrestazione;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private String statoPrestazione;
    private String esito;
    private String descrizione;

    public Prestazione(Ricovero ricovero, Medico medico, TipoPrestazione tipoPrestazione,
                       LocalDateTime dataOraInizio, LocalDateTime dataOraFine,
                       String statoPrestazione, String esito, String descrizione) throws NullPointerException {
        this.ricovero = ricovero;
        this.medico = medico;
        this.tipoPrestazione = tipoPrestazione;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.statoPrestazione = statoPrestazione;
        this.esito = esito;
        this.descrizione = descrizione;

        ricovero.aggiungiPrestazione(this);
        medico.aggiungiPrestazione(this);
    }

    @Override
    public String toString() {
        return tipoPrestazione + " eseguita da " + medico + " - stato: " + statoPrestazione;
    }
}