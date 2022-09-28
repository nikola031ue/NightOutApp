package nightout.app.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    public int idReservation;

    @ColumnInfo(name = "numOfGuests")
    public int numOfGuests;

    public int reservationEventId;

    public int reservationUserId;

    public Reservation() {
    }


    public int getIdReservation() {
        return idReservation;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public int getReservationEventId() {
        return reservationEventId;
    }

    public void setReservationEventId(int reservationEventId) {
        this.reservationEventId = reservationEventId;
    }

    public int getReservationUserId() {
        return reservationUserId;
    }

    public void setReservationUserId(int reservationUserId) {
        this.reservationUserId = reservationUserId;
    }
}
