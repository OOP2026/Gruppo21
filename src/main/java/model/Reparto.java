package model;

import java.util.ArrayList;
import java.util.List;

public class Reparto {

    private String nome;
    private String descrizione;
    private List<Stanza> stanze;
    private List<Medico> medici;

    public Reparto(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.stanze = new ArrayList<>();
        this.medici = new ArrayList<>();
    }

    public void aggiungiStanza(Stanza stanza) {
        stanze.add(stanza);
    }

    public void aggiungiMedico(Medico medico) {
        medici.add(medico);
    }

    public String getNome() {
        return nome;
    }

    public List<Stanza> getStanze() {
        return stanze;
    }

    @Override
    public String toString() {
        return "Reparto: " + nome;
    }
}