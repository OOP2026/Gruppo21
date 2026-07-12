package model;

import exceptions.BadArgsException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ricovero {

    private int idRicovero; // Aggiunto per mappare la Primary Key del DB
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private Paziente paziente;
    private Letto letto;
    private List<Medico> medici;

    // Costruttore Guscio per la Foreign Key (usato dal DAO)
    public Ricovero(int idRicovero) {
        this.idRicovero = idRicovero;
        this.medici = new ArrayList<>();
    }

    public Ricovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws BadArgsException {
        if(paziente==null || letto == null || dataOraInizio == null || dataOraFine == null) throw new BadArgsException("La classe Ricovero ha degli attributi NULLI.");

        this.paziente = paziente;
        this.letto = letto;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.medici = new ArrayList<>();

        paziente.aggiungiRicovero(this);
        letto.aggiungiRicovero(this);
    }

    public int getIdRicovero() {
        return idRicovero;
    }

    public void setIdRicovero(int idRicovero) {
        this.idRicovero = idRicovero;
    }

    public Ricovero richiedereRicovero() {
        return this;
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

    public List<Medico> getMedici() {
        return medici;
    }

    // Metodo fondamentale per il DAO per il riempimento forzato dell'array
    public void setMedici(List<Medico> medici) {
        this.medici = medici;
    }

    @Override
    public String toString() {
        // Controllo di sicurezza per evitare errori se l'oggetto è un guscio vuoto
        if (paziente != null && letto != null) {
            return "Ricovero di " + paziente.getNome() + " " + paziente.getCognome() + " nel letto " + letto;
        }
        return "Ricovero ID: " + idRicovero;
    }
}