package model;

import exceptions.BadArgsException;

public class Amministratore extends UtenteRegistrato {
    private int id;

    public Amministratore(String nome, String cognome, String email, String password) throws BadArgsException {
        super(nome, cognome, email, password);
    }

    public Amministratore(String email, String password) throws BadArgsException {
        super(email, password);
    }

    private void gestireAnagraficaPazienti() {
        System.out.println("Gestione anagrafica pazienti");
    }

    private void rimuoviUtente() {
        System.out.println("Utente rimosso");
    }

    private boolean aggiungereRicovero() {
        System.out.println("Ricovero aggiunto");
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean aggiungereTurnoLavorativo() {
        System.out.println("Turno lavorativo aggiunto");
        return true;
    }
}