package model;

import java.time.LocalDate;
import java.util.Date;

import utils.Utils;

public class Ticket {

    private final Date dateShow;
    private final String startTime;
    private final int salaID;
    private final String letterLine;
    private final int numberSeat;
    private final Date purchaseDate;
    private final boolean cineCard;
    private final String clientID;
    
    public Ticket(Date dateShow, String startTime, int salaID, String letterLine, int numberSeat, Date purchaseDate,
            boolean cineCard, String clientID) {
        this.dateShow = dateShow;
        this.startTime = startTime;
        this.salaID = salaID;
        this.letterLine = letterLine;
        this.numberSeat = numberSeat;
        this.purchaseDate = purchaseDate;
        this.cineCard = cineCard;
        this.clientID = clientID;
    }

    public Ticket(Date dateShow, String startTime, int salaID, String letterLine, int numeberSeat, boolean cineCard,
            String clientID) {
        this(dateShow, startTime, salaID, letterLine, numeberSeat, Utils.localDateToDate(LocalDate.now()), cineCard, clientID);
    }

    public Date getDateShow() {
        return dateShow;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getSalaID() {
        return salaID;
    }

    public String getLetterLine() {
        return letterLine;
    }

    public int getNumberSeat() {
        return numberSeat;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isCineCard() {
        return cineCard;
    }

    public String getClientID() {
        return clientID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateShow == null) ? 0 : dateShow.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + salaID;
        result = prime * result + ((letterLine == null) ? 0 : letterLine.hashCode());
        result = prime * result + numberSeat;
        result = prime * result + ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
        result = prime * result + (cineCard ? 1231 : 1237);
        result = prime * result + ((clientID == null) ? 0 : clientID.hashCode());
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
        Ticket other = (Ticket) obj;
        if (dateShow == null) {
            if (other.dateShow != null)
                return false;
        } else if (!dateShow.equals(other.dateShow))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        if (salaID != other.salaID)
            return false;
        if (letterLine == null) {
            if (other.letterLine != null)
                return false;
        } else if (!letterLine.equals(other.letterLine))
            return false;
        if (numberSeat != other.numberSeat)
            return false;
        if (purchaseDate == null) {
            if (other.purchaseDate != null)
                return false;
        } else if (!purchaseDate.equals(other.purchaseDate))
            return false;
        if (cineCard != other.cineCard)
            return false;
        if (clientID == null) {
            if (other.clientID != null)
                return false;
        } else if (!clientID.equals(other.clientID))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Ticket [dateShow=" + dateShow + ", startTime=" + startTime + ", salaID=" + salaID + ", letterLine="
                + letterLine + ", numeberSeat=" + numberSeat + ", purchaseDate=" + purchaseDate + ", cineCard="
                + cineCard + ", clientID=" + clientID + "]";
    }

}