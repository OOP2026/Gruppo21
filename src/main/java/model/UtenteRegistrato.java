package model;

public class UtenteRegistrato extends Persona {

    private String email;
    private String password;

    public UtenteRegistrato(String nome, String cognome, String email, String password) throws NullPointerException, IllegalArgumentException {
        super(nome, cognome);

        if(email == null || password == null) throw new NullPointerException("La classe UtenteRegistrato ha degli attributi nulli.");
        if(email.isEmpty() || password.isEmpty()) throw new IllegalArgumentException("La classe UtenteRegistrato ha degli attributi vuoti.");

        this.email = email;
        this.password = password;
    }

    public UtenteRegistrato(String email, String password) throws NullPointerException, IllegalArgumentException {
        super();

        if(email == null || password == null) throw new NullPointerException("La classe UtenteRegistrato ha degli attributi nulli.");
        if(email.isEmpty() || password.isEmpty()) throw new IllegalArgumentException("La classe UtenteRegistrato ha degli attributi vuoti.");

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
}