package model;

public class UtenteRegistrato extends Persona {
    private String login;
    private String password;

    public UtenteRegistrato(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean login(String login, String password) {
        return ( login.equals(this.login) && password.equals(this.password));
    }
}
