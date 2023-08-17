package model;

import java.util.Optional;

public class Director extends Cast {

    public Director(Integer id,String nome, String cognome, Optional<String> nazionalita) {
        super(id,nome, cognome, nazionalita, true);
    }

}