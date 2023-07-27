package model;

import java.util.Date;

public class Showing {
    
   private final Date data;
   private final String startTime;
   private final int numberSpectator;
   private final int theaterID;
   private final int filmID;
   private final String programmingMode;
   
   public Showing(Date data, String startTime, int numberSpectator, int theaterID, int filmID, String programmingMode) {
      this.data = data;
      this.startTime = startTime;
      this.numberSpectator = numberSpectator;
      this.theaterID = theaterID;
      this.filmID = filmID;
      this.programmingMode = programmingMode;
   }

   public Date getData() {
      return data;
   }

   public String getStartTime() {
      return startTime;
   }

   public int getNumberSpectator() {
      return numberSpectator;
   }

   public int getTheaterID() {
      return theaterID;
   }

   public int getFilmID() {
      return filmID;
   }

   public String getProgrammingMode() {
      return programmingMode;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((data == null) ? 0 : data.hashCode());
      result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
      result = prime * result + numberSpectator;
      result = prime * result + theaterID;
      result = prime * result + filmID;
      result = prime * result + ((programmingMode == null) ? 0 : programmingMode.hashCode());
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
      Showing other = (Showing) obj;
      if (data == null) {
         if (other.data != null)
            return false;
      } else if (!data.equals(other.data))
         return false;
      if (startTime == null) {
         if (other.startTime != null)
            return false;
      } else if (!startTime.equals(other.startTime))
         return false;
      if (numberSpectator != other.numberSpectator)
         return false;
      if (theaterID != other.theaterID)
         return false;
      if (filmID != other.filmID)
         return false;
      if (programmingMode == null) {
         if (other.programmingMode != null)
            return false;
      } else if (!programmingMode.equals(other.programmingMode))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "Showing [data=" + data + ", startTime=" + startTime + ", numberSpectator=" + numberSpectator
            + ", theaterID=" + theaterID + ", filmID=" + filmID + ", programmingMode=" + programmingMode + "]";
   }

}