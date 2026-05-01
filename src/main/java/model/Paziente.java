package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paziente extends Persona {

    private String codiceFiscale;
    private LocalDate dataNascita;
    private String telefono;
    private String indirizzo;
    private List<Ricovero> ricoveri;

    public Paziente(String codiceFiscale, String nome, String cognome,
                    LocalDate dataNascita, String telefono, String indirizzo) throws NullPointerException, IllegalArgumentException {
        super(nome, cognome);

        if(codiceFiscale==null
                || dataNascita == null
                || telefono == null
                || indirizzo == null)
            throw new NullPointerException("E' stato passato un attributo NULLO nella classe Paziente.");

        if(codiceFiscale.isEmpty()
                || telefono.isEmpty()
                || indirizzo.isEmpty())
            throw new IllegalArgumentException("E' stato passato un attributo VUOTO nella classe Paziente.");

        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.ricoveri = new ArrayList<>();
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public String getNomeCompleto() {
        return nome + " " + cognome;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " - CF: " + codiceFiscale;
    }
}