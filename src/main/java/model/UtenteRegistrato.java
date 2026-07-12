package model;

import exceptions.BadArgsException;

public class UtenteRegistrato {

    private String nome;
    private String cognome;
    private String email;
    private String password;

    public UtenteRegistrato(String nome, String cognome, String email, String password) throws BadArgsException {
        if(email == null || password == null) throw new BadArgsException("La classe UtenteRegistrato ha degli attributi nulli.");

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public UtenteRegistrato(String email, String password) throws BadArgsException {
        if(email == null || password == null) throw new BadArgsException("La classe UtenteRegistrato ha degli attributi nulli.");
        if(email.isEmpty() || password.isEmpty()) throw new BadArgsException("La classe UtenteRegistrato ha degli attributi vuoti.");

        this.email = email;
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    private void logout() {
        System.out.println("Logout effettuato");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}