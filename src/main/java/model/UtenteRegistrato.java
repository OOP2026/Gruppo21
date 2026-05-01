package model;

public class UtenteRegistrato extends Persona {
    private String login;
    private String password;

    public UtenteRegistrato(String login, String password, String nome, String cognome) throws NullPointerException {
        super(nome, cognome);

        if(login == null || password == null)
            throw new NullPointerException("E' stato passato un attributo nullo nella classe UtenteRegistrato.");

        this.login = login;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }

    public boolean login(String login, String password) {
        return ( login.equals(this.login) && password.equals(this.password));
    }
}
