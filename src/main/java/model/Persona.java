package model;

public class Persona {
    protected String nome;
    protected String cognome;

    public Persona(String nome, String cognome) throws NullPointerException, IllegalArgumentException {
        if(nome == null || cognome == null) throw new NullPointerException("E' stato passato un attributo NULLO nella classe Persona.");
        if(nome.isEmpty() || cognome.isEmpty()) throw new IllegalArgumentException("E' stato passato un attributo VUOTO nella classe Persona.");

        this.nome = nome;
        this.cognome = cognome;
    }
}
