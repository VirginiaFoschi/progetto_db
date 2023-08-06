package model;

public class Rate {

    private final Category categoria;
    private final double prezzo;
    private final String tipo;

    public Rate(Category categoria, double prezzo, String tipo) {
        this.categoria = categoria;
        this.prezzo = prezzo;
        this.tipo = tipo;
    }

    public Category getCategoria() {
        return categoria;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
        long temp;
        temp = Double.doubleToLongBits(prezzo);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
        Rate other = (Rate) obj;
        if (categoria == null) {
            if (other.categoria != null)
                return false;
        } else if (!categoria.equals(other.categoria))
            return false;
        if (Double.doubleToLongBits(prezzo) != Double.doubleToLongBits(other.prezzo))
            return false;
        if (tipo == null) {
            if (other.tipo != null)
                return false;
        } else if (!tipo.equals(other.tipo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Rate [categoria=" + categoria + ", prezzo=" + prezzo + ", tipo=" + tipo + "]";
    }
    
}