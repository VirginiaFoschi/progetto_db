package model;

import java.util.Date;

public class CineCard {
    
    private final String client_cf;
    private final Date dataAcquisto;
    private final int ingressiDisponibili;
    private final int ingressiTotali;
    private final boolean isValid;
    
    public CineCard(String client_cf, Date dataAcquisto, int ingressiDisponibili, int ingressiTotali) {
        this.client_cf = client_cf;
        this.dataAcquisto = dataAcquisto;
        this.ingressiDisponibili = ingressiDisponibili;
        this.ingressiTotali = ingressiTotali;
        this.isValid=true;
    }

    public CineCard(String client_cf, Date dataAcquisto, int ingressiDisponibili, int ingressiTotali, boolean isValid) {
        this.client_cf = client_cf;
        this.dataAcquisto = dataAcquisto;
        this.ingressiDisponibili = ingressiDisponibili;
        this.ingressiTotali = ingressiTotali;
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getClient_cf() {
        return client_cf;
    }

    public Date getDataAcquisto() {
        return dataAcquisto;
    }

    public int getIngressiDisponibili() {
        return ingressiDisponibili;
    }

    public int getIngressiTotali() {
        return ingressiTotali;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((client_cf == null) ? 0 : client_cf.hashCode());
        result = prime * result + ((dataAcquisto == null) ? 0 : dataAcquisto.hashCode());
        result = prime * result + ingressiDisponibili;
        result = prime * result + ingressiTotali;
        result = prime * result + (isValid ? 1231 : 1237);
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
        CineCard other = (CineCard) obj;
        if (client_cf == null) {
            if (other.client_cf != null)
                return false;
        } else if (!client_cf.equals(other.client_cf))
            return false;
        if (dataAcquisto == null) {
            if (other.dataAcquisto != null)
                return false;
        } else if (!dataAcquisto.equals(other.dataAcquisto))
            return false;
        if (ingressiDisponibili != other.ingressiDisponibili)
            return false;
        if (ingressiTotali != other.ingressiTotali)
            return false;
        if (isValid != other.isValid)
            return false;
        return true;
    }
    
}