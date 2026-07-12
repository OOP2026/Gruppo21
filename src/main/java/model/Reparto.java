package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Reparto {
    private List<Stanza> stanze;
    private List<Medico> medici;
    private String nome;
    private int id;

    // Costruttore Guscio per la Foreign Key (usato dal DAO)
    public Reparto(int id) {
        this.id = id;
        this.stanze = new ArrayList<>();
        this.medici = new ArrayList<>();
    }

    // Costruttore completo
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

    public void setStanze(List<Stanza> stanze) {
        this.stanze = stanze;
    }

    public List<Medico> getMedici() {
        return medici;
    }

    public void setMedici(List<Medico> medici) {
        this.medici = medici;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (nome != null && !nome.isEmpty()) {
            return nome;
        }
        return "Reparto ID: " + id;
    }
}