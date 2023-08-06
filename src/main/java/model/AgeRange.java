package model;

public class AgeRange {
    
    private final int etaMin;
    private final int etaMax;
    private final Category categoria;

    public AgeRange(int etaMin, int etaMax, Category categoria) {
        this.etaMin = etaMin;
        this.etaMax = etaMax;
        this.categoria = categoria;
    }

    public int getEtaMin() {
        return etaMin;
    }

    public int getEtaMax() {
        return etaMax;
    }

    public Category getCategoria() {
        return categoria;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + etaMin;
        result = prime * result + etaMax;
        result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
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
        AgeRange other = (AgeRange) obj;
        if (etaMin != other.etaMin)
            return false;
        if (etaMax != other.etaMax)
            return false;
        if (categoria == null) {
            if (other.categoria != null)
                return false;
        } else if (!categoria.equals(other.categoria))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AgeRange [etaMin=" + etaMin + ", etaMax=" + etaMax + ", categoria=" + categoria + "]";
    }
 
}