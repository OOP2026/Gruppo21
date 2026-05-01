package model;

public class Persona {
    protected String nome;
    protected String cognome;

    public Persona(String nome, String cognome) throws NullPointerException {
        if(nome == null || cognome == null) throw new NullPointerException("E' stato passato un attributo nullo nella classe Persona");

        this.nome = nome;
        this.cognome = cognome;
    }
}
