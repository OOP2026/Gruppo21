package model;

public class Persona {

    private String nome;
    private String cognome;

    public Persona(String nome, String cognome) throws NullPointerException, IllegalArgumentException {
        if(nome==null || cognome == null) throw new NullPointerException("La classe Persona ha degli attributi NULLI.");
        if(nome.isEmpty() || cognome.isEmpty()) throw new IllegalArgumentException("La classe Persona ha degli attributi VUOTI.");

        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    @Override
    public String toString() {
        return nome + " " + cognome;
    }
}