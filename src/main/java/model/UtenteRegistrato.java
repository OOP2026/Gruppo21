package model;

public class UtenteRegistrato extends Persona {

    private String email;
    private String password;

    public UtenteRegistrato(String nome, String cognome, String email, String password) {
        super(nome, cognome);
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
}