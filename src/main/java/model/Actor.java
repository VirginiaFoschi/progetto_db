package model;

public class Actor extends Cast {

    public Actor(Integer id,String nome, String cognome, String nazionalita) {
        super(id,nome, cognome, nazionalita, false);
        //TODO Auto-generated constructor stub
    }

    public Actor(String nome, String cognome, String nazionalita) {
        super(nome,cognome,nazionalita,false);
    }

}