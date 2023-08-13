package model;

public class Director extends Cast {

    public Director(Integer id,String nome, String cognome, String nazionalita) {
        super(id,nome, cognome, nazionalita, true);
    }

}