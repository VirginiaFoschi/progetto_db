package model;

import java.util.Optional;

public class Actor extends Cast {

    public Actor(Integer id,String nome, String cognome, Optional<String> nazionalita) {
        super(id,nome, cognome, nazionalita, false);
    }

}