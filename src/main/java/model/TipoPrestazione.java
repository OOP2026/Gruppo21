package model;

public class TipoPrestazione {

    private String nome;
    private String descrizione;

    public TipoPrestazione(String nome, String descrizione) throws NullPointerException {
        if (nome==null) throw new NullPointerException("E' stato passato un attributo null nella classe TipoPrestazione");

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