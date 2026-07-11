package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Reparto {
    private List<Stanza> stanze;
    private List<Medico> medici;
    private String nome;
    private int id;

    public Reparto(String nome, int id) throws BadArgsException {
        if(nome == null) throw new BadArgsException("La classe Reparto ha degli attributi nulli.");
        if(nome.isEmpty()) throw new BadArgsException("La classe Reparto ha degli attributi vuoti.");

        this.stanze = new ArrayList<>();
        this.medici = new ArrayList<>();

        this.nome = nome;
        this.id = id;
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

    public int getId() {
        return id;
    }
}