package model;

public class Cast {
    
    private final int codice;
    private final String nome;
    private final String cognome;
    private final String nazionalita;
    private final boolean regista;

    public Cast(int codice, String nome, String cognome, String nazionalita, boolean regista) {
        this.codice = codice;
        this.nome = nome;
        this.cognome = cognome;
        this.nazionalita = nazionalita;
        this.regista = regista;
    }

    public int getId() {
        return codice;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public boolean isRegista() {
        return regista;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codice;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
        result = prime * result + ((nazionalita == null) ? 0 : nazionalita.hashCode());
        result = prime * result + (regista ? 1231 : 1237);
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
        Cast other = (Cast) obj;
        if (codice != other.codice)
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
        if (nazionalita == null) {
            if (other.nazionalita != null)
                return false;
        } else if (!nazionalita.equals(other.nazionalita))
            return false;
        if (regista != other.regista)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Cast [codice=" + codice + ", nome=" + nome + ", cognome=" + cognome + ", nazionalità=" + nazionalita
                + ", regista=" + regista + "]";
    }

    
}