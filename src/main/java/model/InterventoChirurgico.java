package model;

import exceptions.BadArgsException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InterventoChirurgico {
    private int idIntervento;
    private String nomeIntervento;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;

    // Collegamento 1:N
    private Visita visita;

    // Collegamento N:N (Tabella Opera)
    private List<Medico> medici;


     public InterventoChirurgico(String nomeIntervento, LocalDateTime dataOraInizio, LocalDateTime dataOraFine, Visita visita) throws BadArgsException {
        if(nomeIntervento == null || dataOraInizio == null || dataOraFine == null || visita == null) {
            throw new BadArgsException("La classe InterventoChirurgico ha degli attributi NULLI.");
        }
        if(nomeIntervento.isEmpty()) {
            throw new BadArgsException("La classe InterventoChirurgico ha degli attributi VUOTI.");
        }

        this.nomeIntervento = nomeIntervento;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.visita = visita;
        this.medici = new ArrayList<>();
    }

    public void aggiungiMedico(Medico medico) {
        if (!medici.contains(medico)) {
            medici.add(medico);
        }
    }

    // --- GETTERS E SETTERS ---
    public int getIdIntervento() { return idIntervento; }
    public void setIdIntervento(int idIntervento) { this.idIntervento = idIntervento; }

    public String getNomeIntervento() { return nomeIntervento; }
    public LocalDateTime getDataOraInizio() { return dataOraInizio; }
    public LocalDateTime getDataOraFine() { return dataOraFine; }
    public Visita getVisita() { return visita; }

    @Override
    public String toString() {
        return idIntervento + " - " + nomeIntervento;
    }
}