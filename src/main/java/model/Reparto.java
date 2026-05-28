package model;

import java.util.ArrayList;
import java.util.List;

public class Reparto {
    private List<Stanza> stanze;
    private List<Medico> medici;

    public Reparto() {
        this.stanze = new ArrayList<>();
        this.medici = new ArrayList<>();
    }

    public void aggiungiStanza(Stanza stanza) {
        stanze.add(stanza);
    }
    public void aggiungiMedico(Medico medico) {
        medici.add(medico);
    }
    public List<Stanza> getStanze() {
        return stanze;
    }
    public List<Medico> getMedici() {
        return medici;
    }
}