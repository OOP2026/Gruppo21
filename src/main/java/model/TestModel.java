package model;

public class TestModel {

	public static void main(String[] args) {
		UtenteRegistrato u = new UtenteRegistrato("topolino","minni");
		System.out.println(u.login("pippo","pluto"));
		System.out.println(u.login("topolino","minni"));

	}

}
