package model;

import java.util.Date;
import java.util.Optional;

public class Client {
    
    private final String cf;
    private final String nome;
    private final String cognome;
    private final Optional<String> telefono;
    private final Date dataNascita;

    private final String mail;
    
    public Client(String cf, String nome, String cognome, Date dataNascita, Optional<String> telefono, String mail) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.mail = mail;
    }

    public String getCf() {
        return cf;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public Optional<String> getTelefono() {
        return telefono;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cf == null) ? 0 : cf.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
        result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
        result = prime * result + ((dataNascita == null) ? 0 : dataNascita.hashCode());
        result = prime * result + ((mail == null) ? 0 : mail.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Client other = (Client) obj;
        if (cf == null) {
            if (other.cf != null)
                return false;
        } else if (!cf.equals(other.cf))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (cognome == null) {
            if (other.cognome != null)
                return false;
        } else if (!cognome.equals(other.cognome))
            return false;
        if (telefono == null) {
            if (other.telefono != null)
                return false;
        } else if (!telefono.equals(other.telefono))
            return false;
        if (dataNascita == null) {
            if (other.dataNascita != null)
                return false;
        } else if (!dataNascita.equals(other.dataNascita))
            return false;
        if (mail == null) {
            if (other.mail != null)
                return false;
        } else if (!mail.equals(other.mail))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Client [cf=" + cf + ", nome=" + nome + ", cognome=" + cognome + ", telefono=" + telefono
                + ", dataNascita=" + dataNascita + ", mail=" + mail + "]";
    }

}