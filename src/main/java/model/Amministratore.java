package model;

import exceptions.BadArgsException;

public class Amministratore extends UtenteRegistrato {
    private int id;

    public Amministratore(String nome, String cognome, String email, String password) throws BadArgsException {
        super(nome, cognome, email, password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}