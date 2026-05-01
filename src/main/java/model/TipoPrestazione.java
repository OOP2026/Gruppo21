package model;

public class TipoPrestazione {

    private String nome;
    private String descrizione;

    public TipoPrestazione(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}