package model;

public class CinecardType {

    private final int entrancesNumber;
    private final double price;
    private final int validityMonths;

    public CinecardType(int entrancesNumber, double price, int validityMonths) {
        this.entrancesNumber = entrancesNumber;
        this.price = price;
        this.validityMonths = validityMonths;
    }

    public int getEntrancesNumber() {
        return entrancesNumber;
    }

    public double getPrice() {
        return price;
    }

    public int getValidityMonths() {
        return validityMonths;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + entrancesNumber;
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + validityMonths;
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
        CinecardType other = (CinecardType) obj;
        if (entrancesNumber != other.entrancesNumber)
            return false;
        if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
            return false;
        if (validityMonths != other.validityMonths)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CinecardType [entrancesNumber=" + entrancesNumber + ", price=" + price + ", validityMonths="
                + validityMonths + "]";
    }

}