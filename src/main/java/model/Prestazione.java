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
                       String statoPrestazione, String esito, String descrizione) throws NullPointerException, IllegalArgumentException {

        if(ricovero==null
                || medico==null
                || tipoPrestazione==null
                || dataOraInizio==null
                || dataOraFine==null
                || statoPrestazione==null
                || esito == null)
            throw new NullPointerException("E' stato passato un attributo NULLO nella classe Prestazione");

        if(esito.isEmpty() || statoPrestazione.isEmpty())
            throw new IllegalArgumentException("E' stato passato un attributo VUOTO nella classe Prestazione");

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