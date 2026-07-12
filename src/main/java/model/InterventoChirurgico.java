package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class InterventoChirurgico {

    private int idIntervento;
    private String nomeIntervento;
    private List<Medico> medici;

    public InterventoChirurgico(String nomeIntervento, int idIntervento) throws BadArgsException {
        if(nomeIntervento == null) throw new BadArgsException("La classe InterventoChirurgico ha degli attributi NULLI.");
        if(nomeIntervento.isEmpty()) throw new BadArgsException("La classe InterventoChirurgico ha degli attributi VUOTI.");

        this.nomeIntervento = nomeIntervento;
        this.idIntervento = idIntervento;
        medici = new ArrayList<Medico>();
    }

    public InterventoChirurgico(String nomeIntervento) throws BadArgsException {
        if(nomeIntervento == null) throw new BadArgsException("La classe InterventoChirurgico ha degli attributi NULLI.");
        if(nomeIntervento.isEmpty()) throw new BadArgsException("La classe InterventoChirurgico ha degli attributi VUOTI.");

        this.nomeIntervento = nomeIntervento;
        medici = new ArrayList<Medico>();
    }

    public int getIdIntervento() {
        return idIntervento;
    }

    public void setIdIntervento(int idIntervento) {
        this.idIntervento = idIntervento;
    }

    public void setNomeIntervento(String nomeIntervento) {
        this.nomeIntervento = nomeIntervento;
    }

    public String getNomeIntervento() {
        return nomeIntervento;
    }

    public List<Medico> getMedici() {
        return medici;
    }

    public void setMedici(List<Medico> medici) {
        this.medici = medici;
    }

    public void aggiungiMedico(Medico medico) {
        medici.add(medico);
    }

    @Override
    public String toString() {
        return "Intervento chirurgico: " + nomeIntervento;
    }
}